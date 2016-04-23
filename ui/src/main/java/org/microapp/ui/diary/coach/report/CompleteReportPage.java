package org.microapp.ui.diary.coach.report;

import java.util.Date;
import java.util.List;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.extensions.yui.calendar.DateField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.microapp.Diary.model.MemberInfo;
import org.microapp.Diary.service.MemberInfoManager;
import org.microapp.membernet.vo.SocietyVO;
import org.microapp.ui.HomePage;
import org.microapp.ui.WicketApplication;
import org.microapp.ui.base.GenericSecuredPage;
import org.microapp.ui.base.form.MemberCheckBoxList;
import org.microapp.ui.diary.coach.CoachPage;

public class CompleteReportPage extends GenericSecuredPage {
	private static final long serialVersionUID = 1L;

	private final String REPORT_FORM_ID = "reportForm";
	
	private long societyId;
	
	@SpringBean
	private MemberInfoManager memberInfoManager;
	
	public CompleteReportPage(PageParameters parameters) {
		super(parameters);
	}
	
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
	protected void addComponents(PageParameters parameters) {
		super.addComponents(parameters);
		addReportForm();
	}
	
	private void addReportForm() {
		ReportForm rf = new ReportForm(REPORT_FORM_ID);
		add(rf);
	}
	
	private class ReportForm extends Form {
		
		private DateField actFrom, actTo, planFrom, planTo;
		private CheckBox completed, uncompleted;
		private Button cancelBtn;
		private MemberCheckBoxList members;
		
		public ReportForm(String id) {
			super(id);
			addComponents();
		}
		
		private void addComponents() {
			actFrom = new DateField("actFrom", Model.of(new Date()));
			actTo = new DateField("actTo", Model.of(new Date()));
			planFrom = new DateField("planFrom", Model.of(new Date()));
			planTo = new DateField("planTo", Model.of(new Date()));
			completed = new CheckBox("completed", Model.of(Boolean.FALSE));
			uncompleted = new CheckBox("uncompleted", Model.of(Boolean.TRUE));
			
			List<MemberInfo> values = memberInfoManager.getMemberInfos(membernetManager.listAll(societyId));
			members = new MemberCheckBoxList("members", values);
			cancelBtn = new Button("cancelBtn") {
				@Override
				public void onSubmit() {
					onCancel();
				}
			};
			cancelBtn.setDefaultFormProcessing(false);
			
			add(actFrom);
			add(actTo);
			add(planFrom);
			add(planTo);
			add(completed);
			add(uncompleted);
			add(members);
			add(cancelBtn);
		}
		
		@Override
		protected void onSubmit() {
			logger.debug("Form submitted.");
			
			java.sql.Date aFrom = new java.sql.Date(actFrom.getModelObject().getTime());
			java.sql.Date aTo = new java.sql.Date(actTo.getModelObject().getTime());
			java.sql.Date pFrom = new java.sql.Date(planFrom.getModelObject().getTime());
			java.sql.Date pTo = new java.sql.Date(planTo.getModelObject().getTime());
			boolean comp = completed.getModelObject();
			boolean uncomp = uncompleted.getModelObject();
			List<MemberInfo> selected = members.getSelected();
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for(MemberInfo mi : selected) {
				sb.append(mi.toString()+" ");
			}
			sb.append("]");
			
			logger.debug(String.format("actFrom=%s, actTo=%s, planFrom=%s, planTo=%s, completed=%b, uncompleted=%b, selected=%s", 
					aFrom, aTo, pFrom, pTo, comp, uncomp, sb.toString()));
			
		}
		
		private void onCancel() {
			logger.debug("Cancel pressed. Redirecting back to coach page.");
			setResponsePage(CoachPage.class);
		}
	}
}
