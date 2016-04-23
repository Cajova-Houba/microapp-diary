package org.microapp.ui.diary.coach.plans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.yui.calendar.DateField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.microapp.Diary.model.Goal;
import org.microapp.Diary.model.MemberInfo;
import org.microapp.Diary.model.Plan;
import org.microapp.Diary.model.enums.ActivityType;
import org.microapp.Diary.model.enums.ActivityUnit;
import org.microapp.Diary.service.MemberInfoManager;
import org.microapp.Diary.service.PlanManager;
import org.microapp.membernet.vo.SocietyVO;
import org.microapp.ui.base.GenericAdminPage;
import org.microapp.ui.base.form.MemberCheckBoxList;
import org.microapp.ui.base.genericTable.GenericTable;
import org.microapp.ui.diary.coach.CoachPage;

public class MultiplePlansPage extends GenericAdminPage {
	private static final long serialVersionUID = 1L;

	private long societyId;
	
	private final String PLANS_FORM_ID = "plansForm";
	private final String GOAL_FORM_ID = "goalForm";
	
	
	@SpringBean
	private MemberInfoManager memberInfoManager;
	
	@SpringBean
	private PlanManager planManager;
	
	@Override
	protected void authenticate() {
		super.authenticate();
		
		//admin status is already verified in GenericAdminPage
		SocietyVO society = logged.getSociety();
		this.societyId = society.getId();
		logger.debug("User is admin of society with id: "+societyId);
	}
	
	@Override
	protected void addComponents() {
		super.addComponents();
		addPlansForm();
	}
	
	private void addPlansForm() {
		PlansForm pf = new PlansForm(PLANS_FORM_ID);
		add(pf);
	}
	
	private class PlansForm extends Form {
		
		private MemberCheckBoxList members;
		private TextField planName;
		private DateField starts, ends;
		private Button cancelBtn;
		private GenericTable goalsTable;
		private GoalSimpleForm goalForm;
		
		protected List<Goal> goals;
		
		public PlansForm(String id) {
			super(id);
			goals = new ArrayList<Goal>();
			addComponents();
		}
		
		private void addComponents() {
			List<MemberInfo> memberInfos = memberInfoManager.getMemberInfos(membernetManager.listAll(societyId));
			members = new MemberCheckBoxList("members", memberInfos);
			planName = new TextField("planName", Model.of("New plan"));
			planName.setRequired(true);
			starts = new DateField("starts", Model.of(new Date()));
			starts.setRequired(true);
			ends = new DateField("ends", Model.of(new Date()));
			ends.setRequired(true);
			cancelBtn = new Button("cancelBtn") {
				@Override
				public void onSubmit() {
					onCancel();
				}
			};
			cancelBtn.setDefaultFormProcessing(false);
			goalsTable = new GenericTable("goalsTable", Goal.class, new ListModel(goals), Arrays.asList("name", "activityType", "value", "activityUnit"), null);
			goalsTable.setOutputMarkupId(true);
			goalForm = new GoalSimpleForm(goalsTable);
			
			add(members);
			add(planName);
			add(starts);
			add(ends);
			add(cancelBtn);
			add(goalsTable);
			add(goalForm);
		}
		
		@Override
		protected void onSubmit() {
			logger.debug("Form submitted.");
			
			java.sql.Date startsDate = new java.sql.Date(starts.getModelObject().getTime());
			java.sql.Date endsDate = new java.sql.Date(ends.getModelObject().getTime());
			String name = planName.getModelObject().toString();
			List<MemberInfo> selected = members.getSelected();
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for(MemberInfo mi : selected) {
				sb.append(mi.toString()+" ");
			}
			sb.append("]");
			
			StringBuilder sb2 = new StringBuilder();
			sb2.append("[");
			for(Goal g : goals) {
				sb2.append(g.toString()+" ");
			}
			sb2.append("]");
			
			logger.debug(String.format("name=%s, starts=%s, ends=%s, goals=%s, selected=%s", name, startsDate, endsDate, sb2.toString(), sb.toString()));
			
			//create and save plans
//			List<Plan> plans = new ArrayList<Plan>();
			for(MemberInfo mi : selected) {
				Plan p = new Plan();
				p.setPersonId(mi.getPersonId());
				p.setEndDate(endsDate);
				p.setStartDate(startsDate);
				p.setName(name);
				
				//add goals
				p.setGoals(goals);
				
				
				logger.debug("Saving plan: "+p);
				planManager.save(p);
				logger.debug("Saved.");
			}
			
			logger.debug("Plans saved, redirecting back to the coach page.");
			setResponsePage(CoachPage.class);
		}
		
		private void onCancel() {
			logger.debug("Cancel pressed. Redirecting back to coach page.");
			setResponsePage(CoachPage.class);
		}
		
		public List<Goal> getGoals() {
			return goals;
		}

		public void setGoals(List<Goal> goals) {
			this.goals = goals;
		}



		private class GoalSimpleForm extends Form<Goal> {
			
			private GenericTable displayTable;
			
			public GoalSimpleForm(GenericTable displayTable) {
				super(GOAL_FORM_ID, new Model(new Goal()));
				this.displayTable = displayTable;
				addComponents();
			}
			
			private void addComponents() {
				TextField<String> nameTf = new TextField("goalName", new PropertyModel(this.getDefaultModel(), "name"));
				nameTf.setRequired(true);
				nameTf.add(new AjaxFormComponentUpdatingBehavior("onChange") {
					
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						
					}
					
				});
				
				NumberTextField<Double> valueTf = new NumberTextField<Double>("goalValue", new PropertyModel(this.getDefaultModel(), "value"));
				valueTf.setStep(0.01);
				valueTf.setRequired(true);
				valueTf.add(new AjaxFormComponentUpdatingBehavior("onChange") {
					
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						
					}
					
				});
				
				DropDownChoice<ActivityType> actType = new DropDownChoice<ActivityType>("goalActType",new PropertyModel(this.getDefaultModel(),"activityType"), Arrays.asList(ActivityType.values()));
				actType.setRequired(true);
				actType.add(new AjaxFormComponentUpdatingBehavior("onChange") {
					
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						
					}
					
				});
				
				ChoiceRenderer choiceRenderer = new ChoiceRenderer("value", "key");
				DropDownChoice<ActivityUnit> actUnit = new DropDownChoice<ActivityUnit>("goalActUnit",new PropertyModel(this.getDefaultModel(),"activityUnit"), Arrays.asList(ActivityUnit.values()), choiceRenderer);
				actUnit.setRequired(true);
				actUnit.add(new AjaxFormComponentUpdatingBehavior("onChange") {
					
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						
					}
					
				});
				
				AjaxSubmitLink addGoalBtn = new AjaxSubmitLink("addGoalBtn", GoalSimpleForm.this) {
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
						Goal g = ((Goal)form.getModelObject()).clone();
						logger.debug("Adding goal: "+g);
						goals.add(g);
						target.add(displayTable);
					}
				};
				addGoalBtn.setDefaultFormProcessing(false);
				
				add(nameTf);
				add(valueTf);
				add(actType);
				add(actUnit);
				add(addGoalBtn);
			}
			
		}
		
	}
	
	
}
