package org.microapp.ui.diary.coach.plans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.extensions.yui.calendar.DateField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.microapp.Diary.model.Goal;
import org.microapp.Diary.model.MemberInfo;
import org.microapp.Diary.service.MemberInfoManager;
import org.microapp.membernet.vo.SocietyVO;
import org.microapp.ui.HomePage;
import org.microapp.ui.WicketApplication;
import org.microapp.ui.base.GenericSecuredPage;
import org.microapp.ui.base.form.MemberCheckBoxList;
import org.microapp.ui.base.genericTable.GenericTable;
import org.microapp.ui.diary.coach.CoachPage;

public class MultiplePlansPage extends GenericSecuredPage {
	private static final long serialVersionUID = 1L;

	private long societyId;
	
	private final String PLANS_FORM_ID = "plansForm";
	
	@SpringBean
	private MemberInfoManager memberInfoManager;
	
	@Override
	protected void authenticate() {
		super.authenticate();
		
		AuthenticatedWebApplication app = (AuthenticatedWebApplication)WicketApplication.get();
		long loggedId = getloggedUserId();
		
		//check if exists
		if(!membernetManager.exists(loggedId)){
			logger.debug("User doesn't exist. Redirecting to membership page.");
			app.restartResponseAtSignInPage();
			
			//check if is admin
		} else if (membernetManager.getMembership(loggedId).isIsSocietyAdmin()) {
			
			//load society
			SocietyVO society = membernetManager.getMembership(loggedId).getSociety();
			if (society == null) {
				logger.debug("No society");
			} else {
				this.societyId = society.getId();
				logger.debug("User is admin of society with id: "+societyId);
			}
		} else {
			logger.debug("User is not admin of society. Redirecting home");
			setResponsePage(HomePage.class);
		}
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
		
		public PlansForm(String id) {
			super(id);
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
			goalsTable = new GenericTable("goalsTable", Goal.class, new ArrayList(), Arrays.asList("name", "activityType", "value", "activityUnit"), null);
			
			
			add(members);
			add(planName);
			add(starts);
			add(ends);
			add(cancelBtn);
			add(goalsTable);
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
			
			logger.debug(String.format("name=%s, starts=%s, ends=%s, selected=%s", name, startsDate, endsDate, sb.toString()));
		}
		
		private void onCancel() {
			logger.debug("Cancel pressed. Redirecting back to coach page.");
			setResponsePage(CoachPage.class);
		}
		
	}
	
}
