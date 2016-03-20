package org.microapp.ui.diary.goal;

import java.util.Arrays;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.microapp.Diary.model.Goal;
import org.microapp.Diary.model.Plan;
import org.microapp.Diary.model.enums.ActivityType;
import org.microapp.Diary.service.GoalManager;
import org.microapp.Diary.service.PlanManager;
import org.microapp.ui.HomePage;
import org.microapp.ui.WicketApplication;
import org.microapp.ui.base.GenericPage;
import org.microapp.ui.base.activityForm.AbstractActivityForm;
import org.microapp.ui.diary.dailyActivity.DailyActivityPage;
import org.microapp.ui.diary.plans.detail.PlanDetailPage;


public class GoalFormPage extends GenericPage {
	
	private final String GOAL_FORM_ID = "goalForm";
	
	private long goalId;
	private boolean goalIdLoaded;
	
	// if the goalId isn't loaded, then planId must be loaded so new goal can be 
	// properly assigned to a plan
	private long planId;
	private boolean planIdLoaded;
	
	private Goal goal;
	
	@SpringBean
	private GoalManager goalManager;
	
	@SpringBean
	private PlanManager planManager;
	
	
	public GoalFormPage(PageParameters parameters) {
		super(parameters);
	}
	
	@Override
	public void authenticate() {
		super.authenticate();
		
		//check if the user is logged and redirect to login page if isn't
		AuthenticatedWebApplication app = (AuthenticatedWebApplication)WicketApplication.get();
		if(!isSignedIn()) {
			logger.debug("No user logged. Redirecting to membership page.");
			app.restartResponseAtSignInPage();
		}
	}
	
	@Override
	public void loadParameters(PageParameters parameters) {
		super.loadParameters(parameters);
		
		loadGoalId(parameters);
		loadPlanId(parameters);
	}
	
	private void loadGoalId(PageParameters parameters) {
		goalIdLoaded = false;
		if (!parameters.get("goalId").isNull()) {
			try {
				goalId = Long.parseLong(parameters.get("goalId").toString());
				
				if (goalManager.exists(goalId)) {
					goalIdLoaded = true;
				} else {
					addError("Goal does not exist.");
					logger.debug("Goal does not exist.");
				}
			} catch (Exception e) {
				addError("Error while parsing 'goalId' parameter. "+e.toString());
				logger.debug("Error while parsing 'goalId' parameter. "+e.toString());
			}
		}
	}
	
	private void loadPlanId(PageParameters parameters) {
		planIdLoaded = false;
		if (!parameters.get("planId").isNull()) {
			try {
				planId = Long.parseLong(parameters.get("planId").toString());
				
				if (planManager.exists(planId)) {
					planIdLoaded = true;
				} else {
					addError("Plan does not exist.");
					logger.debug("Plan does not exist.");
				}
			} catch (Exception e) {
				addError("Error while parsing 'planId' parameter. "+e.toString());
				logger.debug("Error while parsing 'planId' parameter. "+e.toString());
			}
		}
	}
	
	@Override
	public void inic(PageParameters parameters) {
		super.inic(parameters);
		if(goalIdLoaded) {
			setTitle(new ResourceModel("title.edit"));
		} else {
			setTitle(new ResourceModel("title.new"));
		}
	}
	
	@Override
	public void addComponents(PageParameters parameters) {
		super.addComponents(parameters);
		
		addGoalForm();
	}
	
	private void addGoalForm() {
		
		if (goalIdLoaded) {
			add(new GoalForm(GOAL_FORM_ID, GOAL_FORM_ID, goalManager.get(goalId)));
		} else {
			if(planIdLoaded) {
				add(new GoalForm(GOAL_FORM_ID, GOAL_FORM_ID, planManager.get(planId)));
			} else {
				logger.error("Error: Neither goalId or planId loaded, redirecting home.");
				setResponsePage(HomePage.class);
			}
		}
	}
	
	private class GoalForm extends AbstractActivityForm<Goal, Plan> {
		
		private static final long serialVersionUID = 1L;
		private Class<? extends GenericPage> responsePage = PlanDetailPage.class;
		
		/**
		 * Use this constructor when editing existing goal.
		 * @param id
		 * @param goal
		 */
		public GoalForm(String id, String name, Goal goal) {
			super(id, name, goal);
			
			this.parent = goal.getPlan();
			
			addComponents();
		}
		
		/**
		 * Use this constructor when creating form for adding new goal.
		 * @param id
		 * @param planId
		 */
		public GoalForm(String id, String name, Plan plan) {
			super(id, name, plan);
			
			this.object = new Goal();
			this.object.setName("New Goal");
			this.object.setValue(0);
			this.object.setPlan(plan);
			
			addComponents();
		}
		@Override
		protected void onSubmit() {
			
			logger.debug("Submiting goal form.");
			
			validate();
			
			logger.debug(this.object.toString());
			
			goalManager.save(object);
			
			//redirect back to plan detail
			PageParameters params = new PageParameters();
			params.add("planId", parent.getId());
			setResponsePage(responsePage, params);
		}
		
		@Override
		protected void onCancel() {
			logger.debug("Cancel pressed. Redirecting to plan page.");
			
			//redirect back to plan detail
			PageParameters params = new PageParameters();
			params.add("planId", parent.getId());
			setResponsePage(responsePage, params);
		}
		
		@Override
		protected void onDelete() {
			
			if(newObject) {
				//nothing to delete
				logger.debug("No goal to delete.");
			} else {
				logger.debug("Deleting goal with id="+goal.getId());
				
				goalManager.remove(object.getId());
				
				//redirect back to plan detail
				PageParameters params = new PageParameters();
				params.add("planId", parent.getId());
				setResponsePage(responsePage, params);
			}
		}
		
	}
}