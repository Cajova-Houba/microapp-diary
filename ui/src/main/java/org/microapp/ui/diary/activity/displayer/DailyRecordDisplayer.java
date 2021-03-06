package org.microapp.ui.diary.activity.displayer;

import java.io.Serializable;
import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.microapp.Diary.model.Activity;
import org.microapp.Diary.model.DailyRecord;
import org.microapp.Diary.service.DailyRecordManager;
import org.microapp.ui.base.genericTable.ActivityValueColumn;
import org.microapp.ui.base.genericTable.ButtonColumn;
import org.microapp.ui.base.genericTable.ComponentColumn;
import org.microapp.ui.base.genericTable.GenericTable;
import org.microapp.ui.diary.activity.form.ActivityFormPage;
import org.microapp.ui.diary.activity.report.DailyRecordReportPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.yui.calendar.*;

public class DailyRecordDisplayer extends Panel {
	
	private static final long serialVersionUID = 1L;

	private static final String CSS_FILE_NAME = "drdStyle.css";
	
	private final String REPORT_BTN_ID = "reportBtn";
	private final String NO_ACTIVITY_ID = "noAct";
	
	protected final static Logger logger = LogManager.getLogger(DailyRecordDisplayer.class);
	
	@SpringBean
	private DailyRecordManager dailyRecordManager;
	
	private final long loggedId;
	private final long personId;

	private DateField dateField;
	private GenericTable actTable;
	private Label noActLabel;
	
	//selected date
	private Date selectedDate;
	
	private List<String> fieldNames = Arrays.asList(new String[] {"id","name","activityType"});
	
	/**
	 * Model for GenericTable. The list of objects to be displyed should be there.
	 */
	private IModel<List<? extends Serializable>> values;
	
	public DailyRecordDisplayer(String id, long loggedId, long personId, Date initialDate) {
		super(id);
		this.loggedId = loggedId;
		this.personId = personId;
		this.selectedDate = initialDate;
		
		addComponents();
	}
	
	public DailyRecordDisplayer(String id, long loggedId, long personId) {
		this(id,loggedId,personId,new Date(Calendar.getInstance().getTimeInMillis()));
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		//load css
		PackageResourceReference resRef = new PackageResourceReference(this.getClass(),DailyRecordDisplayer.CSS_FILE_NAME);
		CssHeaderItem cssItem = CssHeaderItem.forReference(resRef);
		response.render(cssItem);
	}
	
	public void addComponents() {
		addDateForm("dateForm",selectedDate);
		addActivityTable("activityTable", selectedDate);
		addNewActButton("btnNewAct");
		addReportButton();
	}
	
	private void addNewActButton(String buttonId) {
		Link link = new Link(buttonId) {

			@Override
			public void onClick() {
				PageParameters params = new PageParameters();
				params.add("date", selectedDate);
				params.add("personId",personId);
				
				setResponsePage(ActivityFormPage.class,params);
			}
		};
		add(link);
	}
	
	private void addReportButton() {
		Link link = new Link(REPORT_BTN_ID) {
			@Override
			public void onClick() {
				PageParameters params = new PageParameters();
				params.add("personId",personId);
				
				setResponsePage(DailyRecordReportPage.class, params);
			}
		};
		add(link);
	}
	
	private Date getSelectedDate() {
		return this.selectedDate;
	}
	
	private void addDateForm(String dateFormId, Date initialDate) {
		dateField = new DateField("dateField", new Model(initialDate));
		
		Form form = new Form(dateFormId);
		form.add(dateField);
		
		// show activities for selected date
		AjaxSubmitLink asl = new AjaxSubmitLink("btnShow", form) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				
				Date date = new Date(dateField.getModelObject().getTime());
				selectedDate = date;
				
				DailyRecord dr = dailyRecordManager.getDailyRecordFrom(date, personId);
				List<Activity> activities = dr.getActivities();
				logger.debug(activities.size()+" activities loaded.");
				actTable.newData(new ListModel(activities));
								
				if(activities.isEmpty()) {
					noActLabel.setDefaultModel(Model.of("No activities"));
				} else {
					noActLabel.setDefaultModel(Model.of(""));
				}
				
				target.add(noActLabel);
				target.add(actTable);
			}
		};
		
		form.add(asl);
		add(form);
		
	}
	
	private void addActivityTable(String activityTableId, Date initialDate) {
		
		//get activities to display
		DailyRecord dailyRecord = dailyRecordManager.getDailyRecordFrom(initialDate, personId);
		
		
		
		List<Activity> activities = dailyRecord.getActivities();
		//add noAct label
		if(activities.isEmpty()) {
			noActLabel = new Label(NO_ACTIVITY_ID, "No activities");
		} else {
			noActLabel = new Label(NO_ACTIVITY_ID);
		}
		noActLabel.setOutputMarkupId(true);
		add(noActLabel);
		
		//new table using GenericTable
		actTable = new ActivityTable(activityTableId, Activity.class, values, fieldNames, null);
		actTable.newData(new ListModel(activities));
		actTable.setOutputMarkupId(true);
		
		add(actTable);
	}
}

/**
 * Table for displaying activities. 
 * @author Zdenda
 *
 */
class ActivityTable extends GenericTable {

	private static final long serialVersionUID = 1L;
	
	public ActivityTable(String id, Class<? extends Serializable> clazz,
			IModel<List<? extends Serializable>> values,
			List<String> fieldNames, Map<String, String> headers) {
		super(id, clazz, values, fieldNames, headers);
	}
	
	@Override
	protected List<ComponentColumn> getComponentColumns() {
		List<ComponentColumn> componentColumns = super.getComponentColumns();
		
		ActivityValueColumn valCol = new ActivityValueColumn("value","activityUnit","actVal");
		componentColumns.add(valCol);
		
		ButtonColumn editColumn = new ButtonColumn("id", "editColumn");
		componentColumns.add(editColumn);
		
		return componentColumns;
	}
	
}
