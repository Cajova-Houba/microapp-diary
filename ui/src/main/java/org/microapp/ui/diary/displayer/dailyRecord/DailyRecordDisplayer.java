package org.microapp.ui.diary.displayer.dailyRecord;

import java.io.Serializable;
import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.microapp.Diary.model.Activity;
import org.microapp.Diary.model.DailyRecord;
import org.microapp.Diary.service.ActivityManager;
import org.microapp.Diary.service.DailyRecordManager;
import org.microapp.ui.base.genericTable.ButtonColumn;
import org.microapp.ui.base.genericTable.ComponentColumn;
import org.microapp.ui.base.genericTable.CustomButton;
import org.microapp.ui.base.genericTable.GenericTable;
import org.microapp.ui.diary.activity.ActivityFormPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.*;

public class DailyRecordDisplayer extends Panel {
	
	private static final long serialVersionUID = 1L;

	private static final String CSS_FILE_NAME = "drdStyle.css";
	
	private static final Logger logger = LogManager.getLogger(DailyRecordDisplayer.class);
	
	@SpringBean
	private DailyRecordManager dailyRecordManager;
	
	private final long loggedId;
	private final long personId;

	//for test purposes
	private TextField tf;
	private Label testLabel;
	private DateTextField dtf;

	private DateField dateField;
	private GenericTable actTable;
	
	//selected date
	private Date selectedDate;
	
	private List<String> fieldNames = Arrays.asList(new String[] {"id","name","activityType","value","activityUnit"});
	
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
	}
	
	private void addNewActButton(String buttonId) {
		Link link = new Link(buttonId) {

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				PageParameters params = new PageParameters();
				params.add("date", selectedDate);
				params.add("memberId",personId);
				
				setResponsePage(ActivityFormPage.class,params);
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
		//new table using GenericTable
//		actTable = new GenericTable(activityTableId, Activity.class, activities, fieldNames, null);
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
		
		ButtonColumn editColumn = new ButtonColumn("id", "editColumn");
		componentColumns.add(editColumn);
		
		return componentColumns;
	}
	
}
