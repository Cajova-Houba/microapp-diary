package org.microapp.ui.diary.plans.form;

import java.sql.Date;
import java.util.Calendar;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.extensions.yui.calendar.DateField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.microapp.Diary.model.Plan;
import org.microapp.Diary.service.PlanManager;
import org.microapp.ui.HomePage;
import org.microapp.ui.WicketApplication;
import org.microapp.ui.base.GenericPage;
import org.microapp.ui.base.GenericSecuredPage;
import org.microapp.ui.diary.plans.PlansPage;
import org.microapp.ui.diary.plans.detail.PlanDetailPage;

public class PlanFormPage extends GenericSecuredPage {

	private static final long serialVersionUID = 1L;

	private final String PLAN_FORM_ID = "planForm";
	
	/**
	 * If no planId is loaded, assume that new plan should be created
	 */
	private long planId;
	private boolean planIdLoaded;
	
	@SpringBean
	private PlanManager planManager;
	
	public PlanFormPage(PageParameters parameters) {
		super(parameters);
	}
	
	@Override
	public void loadParameters(PageParameters parameters) {
		super.loadParameters(parameters);
		
		loadPlanId(parameters);
		
		//check if can access
		if (planIdLoaded) {
			long pId = planManager.get(planId).getPersonId();
			if (!canAccess(getloggedUserId(), pId)) {
				logger.warn("Member with id: "+getloggedUserId()+" can't access "+planManager.get(planId)+". Redirecting back to plans page.");
				setResponsePage(PlansPage.class);
			}
		}
	}
	
	private void loadPlanId(PageParameters parameters) {
		planIdLoaded = false;
		Long tmp = loadLongParameter(parameters, "planId", false);
		if (tmp != null) {
			planId = tmp.longValue();
			planIdLoaded = planManager.exists(planId);
			
			if (planIdLoaded) {
				logger.debug("PlanId = "+planId+" loaded.");
			}
		}
	}
	
	@Override
	public void inic(PageParameters parameters) {
		super.inic(parameters);
		
		if (planIdLoaded) {
			setTitle(new ResourceModel("title.edit"));
		} else {
			setTitle(new ResourceModel("title.new"));
		}
	}
	
	@Override
	public void addComponents(PageParameters parameters) {
		super.addComponents(parameters);
		
		addPlanForm();
	}
	
	private void addPlanForm() {
		if (planIdLoaded) {
			add(new PlanForm(PLAN_FORM_ID, planManager.get(planId)));
		} else if (personIdLoaded) {
			add(new PlanForm(PLAN_FORM_ID, personId));
		} else {
			logger.error("Error: Neither planId or personId loaded, redirecting home.");
			setResponsePage(HomePage.class);
		}
	}
	
	public class PlanForm extends Form {
		
		private static final long serialVersionUID = 1L;
		
		private Plan plan;
		
		/**
		 * See properties file.
		 */
		private String headerKey;
		private long personId;
		private boolean newPlan;
		
		/**
		 * Use this constructor when editing existing plan.
		 * @param id Wicket id.
		 * @param plan Plan to be displayed in form.
		 */
		public PlanForm(String id, Plan plan) {
			super(id);
			
			logger.debug("Editing plan with id="+plan.getId());
			
			this.newPlan = false;
			this.plan = plan;
			this.personId = plan.getPersonId();
			this.headerKey = "edit";
			
			addComponents();
		}
		
		/**
		 * Use this constructor when creating a new plan.
		 * @param id Wicket id.
		 * @param personId Id of a person new plan will belong to.c
		 */
		public PlanForm(String id, long personId) {
			super(id);
			
			logger.debug("Creating new plan for person with id="+personId);
			
			this.newPlan = true;
			this.headerKey = "new";
			this.personId = personId;
			this.plan = new Plan();
			this.plan.setPersonId(personId);
			this.plan.setName("New plan");
			this.plan.setStartDate(new Date(Calendar.getInstance().getTimeInMillis()));
			this.plan.setEndDate(new Date(Calendar.getInstance().getTimeInMillis()));
			
			addComponents();
		}
		
		private void addComponents() {
			
			TextField planName;
			DateField startDate, endDate;
			Button cancelButton, deleteButton;
			
			planName = new TextField("name", new PropertyModel(this.plan, "name"));
			planName.setRequired(true);
			startDate = new DateField("startDate", new PropertyModel(this.plan, "startDate"));
			startDate.setRequired(true);
			endDate = new DateField("endDate", new PropertyModel(this.plan, "endDate"));
			endDate.setRequired(true);
			
			cancelButton = new Button("cancelButton") {
				public void onSubmit() {
					onCancel();
				}
			};
			cancelButton.setDefaultFormProcessing(false);
			
			deleteButton = new Button("deleteButton") {
				@Override
				public void onSubmit() {
					onDelete();
				}
			};
			deleteButton.setDefaultFormProcessing(false);
			
			add(new Label("header", new StringResourceModel("header.${headerKey}", Model.of(this))));
			add(planName);
			add(startDate);
			add(endDate);
			add(cancelButton);
			add(deleteButton);
		}

		@Override
		protected void onSubmit() {
			
			logger.debug("Submitting form.");
			
			validate();
			
			logger.debug("Plan: id="+plan.getId()+" name="+plan.getName());
			
			planManager.save(plan);
			
			//redirect to plans page
			PageParameters params = new PageParameters();
			params.add("personId",personId);
			setResponsePage(PlansPage.class,params);
			
		}
		
		private void onCancel() {
			
			logger.debug("Cancel pressed. Redirecting to plans page.");
			//redirect to plans page
			PageParameters params = new PageParameters();
			params.add("personId",personId);
			setResponsePage(PlansPage.class,params);
		}
		
		private void onDelete() {
			
			if(newPlan) {
				//nothing to delete
				logger.debug("No plan to delete.");
			} else {
				logger.debug("Deleting plan with id="+plan.getId());
				
				planManager.remove(plan.getId());
				
				//redirect back to plan detail
				PageParameters params = new PageParameters();
				params.add("personId", personId);
				setResponsePage(PlansPage.class, params);
			}
		}
		
		public String getHeaderKey() {
			return headerKey;
		}

		public void setHeaderKey(String headerKey) {
			this.headerKey = headerKey;
		}
	}
}
