package org.microapp.ui.diary.plans.displayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.microapp.Diary.model.Plan;
import org.microapp.Diary.service.PlanManager;
import org.microapp.ui.base.genericTable.ButtonColumn;
import org.microapp.ui.base.genericTable.ComponentColumn;
import org.microapp.ui.base.genericTable.CustomButton;
import org.microapp.ui.base.genericTable.GenericTable;
import org.microapp.ui.diary.plans.report.PlanReportPage;

public class PlanDisplayer extends Panel {
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LogManager.getLogger(PlanDisplayer.class);

	private final String PLAN_TABLE_ID = "planTable";
	private final String ADD_PLAN_BUTTON_ID = "addPlanButton";
	private final String CSS_FILE_NAME = "pdStyle.css";
	private final String REPORT_BUTTON_ID = "reportButton";
	
	private long loggedId;
	private long personId;
	
	private GenericTable planTable;
	
	private List<String> fieldNames = Arrays.asList("id", "name", "startDate", "endDate");
	
	
	private CheckBox completedCB;
	private CheckBox uncompletedCB;
	
	@SpringBean
	PlanManager planManager;
	
	public PlanDisplayer(String id, long loggedId, long personId) {
		super(id);
		
		this.loggedId = loggedId;
		this.personId = personId;
		
		addComponents();
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		//load css
		PackageResourceReference resRef = new PackageResourceReference(this.getClass(),CSS_FILE_NAME);
		CssHeaderItem cssItem = CssHeaderItem.forReference(resRef);
		response.render(cssItem);
	}
	
	private void addComponents() {
		addPlanTable();
		addPlanButton();
		addSearchForm();
		addReportButton();
	}
	
	private void addPlanTable() {
		
		List<Plan> values = planManager.getAllUnfinishedPlans(personId);
		planTable = new PlanTable(values,loggedId,personId);
		planTable.setOutputMarkupId(true);
		
		add(planTable);
	}
	
	private void addPlanButton() {
		ExternalLink link = new ExternalLink(ADD_PLAN_BUTTON_ID, "/diary/plans/form?personId="+personId);
		add(link);
	}
	
	private void addReportButton() {
		Link link = new Link(REPORT_BUTTON_ID) {

			@Override
			public void onClick() {
				setResponsePage(PlanReportPage.class);
			}
		};
		
		add(link);
	}
	
	public void addSearchForm() {
		Form form = new Form("searchForm");
		completedCB = new CheckBox("completed", Model.of(Boolean.FALSE));
		uncompletedCB = new CheckBox("uncompleted", Model.of(Boolean.TRUE));
		
		AjaxSubmitLink asl = new AjaxSubmitLink("btnShow", form) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
				boolean completed = completedCB.getModelObject();
				boolean uncompleted = uncompletedCB.getModelObject();
				
				logger.debug("Search form submited. Completed="+completed+"; uncompleted="+uncompleted);
				
				List<Plan> plans = new ArrayList<>();
				
				if(completed) {
					plans.addAll(planManager.getAllFinishedPlans(personId));
					logger.debug("Completed plans loaded: "+plans.size());
				}
				
				if(uncompleted){
					int tmp = plans.size();
					plans.addAll(planManager.getAllUnfinishedPlans(personId));
					logger.debug("Uncompleted plans loaded: "+(plans.size()-tmp));
				}
				
				
				planTable.newData(new ListModel(plans));
				target.add(planTable);
			}
		};
		
		form.add(completedCB);
		form.add(uncompletedCB);
		form.add(asl);
		add(form);
	}
	
	public Long getPersonId() {
		return personId;
	}

	

	/**
	 * Plan table with custom column for plan detail button.
	 * @author Zdenda
	 *
	 */
	private class PlanTable extends GenericTable {


		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private long loggedId;
		
		private long personId;
		
		public PlanTable(List<? extends Serializable> values, long loggedId, long personId) {
			super("planTable", Plan.class, values, fieldNames, null);
			
			this.loggedId = loggedId;
			this.personId = personId;
		}
		
		@Override
		protected List<ComponentColumn> getComponentColumns() {
			List<ComponentColumn> componentColumns = super.getComponentColumns();
			
			componentColumns.add(new ButtonColumn("id", "planDetailButton") {
				
				@Override
				public AbstractLink getLink(IModel<Object> rowModel) {
					
					ExternalLink link = new ExternalLink(CustomButton.LINK_ID, 
							new StringResourceModel(getName()+".link",rowModel)){
						@Override
						protected void onComponentTag(ComponentTag arg0) {
							super.onComponentTag(arg0);
							arg0.put("target", "_blank");
						}
					};
					
					return link;
				}
			});
			
			return componentColumns;
		}
	}
}
