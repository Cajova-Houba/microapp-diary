package org.microapp.ui.diary.plans.detail;


import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.search.backend.AddLuceneWork;
import org.microapp.Diary.model.Goal;
import org.microapp.Diary.model.Plan;
import org.microapp.Diary.service.GoalManager;
import org.microapp.Diary.service.PlanManager;
import org.microapp.ui.WicketApplication;
import org.microapp.ui.base.GenericPage;
import org.microapp.ui.base.GenericSecuredPage;
import org.microapp.ui.base.converters.SqlDateConverter;
import org.microapp.ui.base.genericTable.ActivityValueColumn;
import org.microapp.ui.base.genericTable.ButtonColumn;
import org.microapp.ui.base.genericTable.ComponentColumn;
import org.microapp.ui.base.genericTable.GenericTable;
import org.microapp.ui.base.genericTable.ProgressColumn;
import org.microapp.ui.diary.goal.GoalFormPage;
import org.microapp.ui.diary.plans.PlansPage;
import org.microapp.ui.diary.plans.form.PlanFormPage;

public class PlanDetailPage extends GenericSecuredPage {

	private long planId;
	private boolean planIdLoaded;

	private final String PLAN_NAME_ID = "planName";
	private final String PLAN_FROM_ID = "planFrom";
	private final String PLAN_TO_ID = "planTo";
	private final String GOAL_TABLE_ID = "goalTable";
	private final String EDIT_PLAN_ID = "editPlan";
	private final String MARK_AS_COMPL_ID = "markAsCompl";
	
	private Plan plan;
	
	@SpringBean
	PlanManager planManager;
	
	@SpringBean
	GoalManager goalManager;
	
	public PlanDetailPage(PageParameters parameters) {
		super(parameters);
	}
	
	@Override
	public void loadParameters(PageParameters parameters) {
		super.loadParameters(parameters);
		loadPlanId(parameters);
		if(planIdLoaded) {
			//check if can access
			plan = planManager.get(planId);
			
			if (!canAccess(getloggedUserId(), plan.getPersonId())) {
				logger.warn("Member with id: "+getloggedUserId()+" can't access "+plan+". Redirecting back to plans page.");
				setResponsePage(PlansPage.class);
			}
			
			person = membernetManager.getMembership(plan.getPersonId());
		}
	}
	
	private void loadPlanId(PageParameters parameters) {
		planIdLoaded = false;
		if(!parameters.get("planId").isNull()) {
			try {
				planId = Long.parseLong(parameters.get("planId").toString());
				
				if (planManager.exists(planId)) {
					planIdLoaded = true;
				} else {
					addError("Plan doesn't exist.");
				}
				
			} catch (Exception e) {
				addError("Error when parsing planId parameter. "+e.toString());
			}
		} else {
			addError("Parameter 'planId' not found");
		}
	}
	
	@Override
	public void addComponents(PageParameters parameters) {
		super.addComponents(parameters);
		
		displayPlan();
		addGoalTable();
		addNewGoalButton();
		addBackButton();
		addEditPlanLink();
		addMarkAsCompleted();
	}
	
	private void addGoalTable() {
		
		if (planIdLoaded) {
			
			List<Goal> values = goalManager.getUncompletedGoalsForPlan(planId);
			logger.debug(values.size()+" goals loaded.");
			
			GenericTable goalTable = new GenericTable(GOAL_TABLE_ID, Goal.class, values, Arrays.asList("id","activityType"), null) {
				
				@Override
				protected List<ComponentColumn> getComponentColumns() {
					List<ComponentColumn> columns = super.getComponentColumns();

					//column to display value and unit
					ActivityValueColumn valCol = new ActivityValueColumn("value","activityUnit","actVal");
					columns.add(valCol);
					
					//column to display the progress
					columns.add(new ProgressColumn("progress", "progress"));
					
					//column with edit button
					columns.add(new ButtonColumn("id", "editColumn"));
					
					
					return columns;
				}
			};
			
			add(goalTable);
			
		} else {
			add(new Label(GOAL_TABLE_ID,""));
		}
	}
	
	private void addNewGoalButton() {
		Link link = new Link("btnNewGoal") {

			@Override
			public void onClick() {
				PageParameters params = new PageParameters();
				params.add("planId", planId);
				params.add("personId", getPersonId());
				
				setResponsePage(GoalFormPage.class, params);
			}
		};
		
		add(link);
	}
	
	private void addBackButton() {
		Link link = new Link("backButton") {
			
			@Override
			public void onClick() {
				PageParameters params = new PageParameters();
				params.add("personId", getPersonId());
				
				setResponsePage(PlansPage.class, params);
			};
		};
		add(link);
	}
	
	private void addEditPlanLink() {
		
		if(!planIdLoaded) {
			add(new Label(EDIT_PLAN_ID));
			return;
		}
		
		Link l = new Link(EDIT_PLAN_ID) {
			@Override
			public void onClick() {
				PageParameters params = new PageParameters();
				params.add("planId", planId);
				params.add("personId", getPersonId());
				setResponsePage(PlanFormPage.class, params);
			}
		};
		
		add(l);
	}
	
	private void addMarkAsCompleted() {
		if (!planIdLoaded) {
			add(new Label(MARK_AS_COMPL_ID));
			return;
		}
		
		Link l = new Link(MARK_AS_COMPL_ID) {
			
			@Override
			public void onClick() {
				
				//mark as completed
				planManager.completePlan(plan);
				
				//refresh page
				PageParameters params = new PageParameters();
				params.add("planId", planId);
				setResponsePage(PlanDetailPage.class, params);
			};
		};
		
		add(l);
	}
	
	private void displayPlan() {
		
		if (planIdLoaded) {
			add(new Label(PLAN_NAME_ID,new PropertyModel<String>(plan, "name")));
			add(new Label(PLAN_FROM_ID,new PropertyModel<Date>(plan, "startDate")));
			add(new Label(PLAN_TO_ID,new PropertyModel<Date>(plan, "endDate")));
		} else {
			add(new Label(PLAN_NAME_ID,"Plan name"));
			add(new Label(PLAN_FROM_ID,"date"));
			add(new Label(PLAN_TO_ID,"date"));
		}
		
	}
	
}
