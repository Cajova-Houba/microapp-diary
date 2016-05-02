package org.microapp.ui.diary.coach.report;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javassist.bytecode.Mnemonic;

import org.apache.wicket.extensions.yui.calendar.DateField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.validation.validator.DateValidator;
import org.microapp.Diary.model.MemberInfo;
import org.microapp.Diary.service.MemberInfoManager;
import org.microapp.Diary.service.ReportManager;
import org.microapp.membernet.vo.SocietyVO;
import org.microapp.ui.base.GenericAdminPage;
import org.microapp.ui.base.form.MemberCheckBoxList;
import org.microapp.ui.diary.coach.CoachPage;

public class CompleteReportPage extends GenericAdminPage {
	private static final long serialVersionUID = 1L;

	private final String REPORT_FORM_ID = "reportForm";
	
	private long societyId;
	
	@SpringBean
	private MemberInfoManager memberInfoManager;
	
	@SpringBean
	private ReportManager reportManager;
	
	public CompleteReportPage(PageParameters parameters) {
		super(parameters);
	}
	
	@Override
	protected void authenticate() {
		super.authenticate();
		
		//admin status is already verified in GenericAdminPage
		SocietyVO society = logged.getSociety();
		this.societyId = society.getId();
		logDebug("User is admin of society with id: "+societyId);
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
			add(new FeedbackPanel("feedback"));
		}
		
		@Override
		protected void onSubmit() {
			logDebug("Form submitted.");
			
			java.sql.Date aFrom = new java.sql.Date(actFrom.getModelObject().getTime());
			java.sql.Date aTo = new java.sql.Date(actTo.getModelObject().getTime());
			java.sql.Date pFrom = new java.sql.Date(planFrom.getModelObject().getTime());
			java.sql.Date pTo = new java.sql.Date(planTo.getModelObject().getTime());
			
			//check dates
			if(!aFrom.before(aTo)) {
				error("Activity-from must be before activity-to");
				return;
			}
			
			if(!pFrom.before(pTo)) {
				error("Plan-from must be before plan-to");
				return;
			}
			
			boolean comp = completed.getModelObject();
			boolean uncomp = uncompleted.getModelObject();
			
			List<MemberInfo> selected = members.getSelected();
			
			//check that someone has been selected
			if(selected.isEmpty()) {
				error("No members selected.");
				return;
			}
			
			List<Long> personIds = new ArrayList<Long>();
			for (MemberInfo mi : selected) {
				if(membernetManager.exists(mi.getPersonId())) {
					personIds.add(mi.getPersonId());
				}
			}
			
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for(MemberInfo mi : selected) {
				sb.append(mi.toString()+" ");
			}
			sb.append("]");
			
			logDebug(String.format("actFrom=%s, actTo=%s, planFrom=%s, planTo=%s, completed=%b, uncompleted=%b, selected=%s", 
					aFrom, aTo, pFrom, pTo, comp, uncomp, sb.toString()));
			
			final byte[] pdf = reportManager.exportToPdf(reportManager.makeCompleteReportForMembers(personIds, pFrom, pTo, comp, uncomp, aFrom, aTo));
			logDebug("Pdf generated");
			
			IResourceStream resStream = new AbstractResourceStreamWriter() {
				
				@Override
				public void write(OutputStream output) throws IOException {
					output.write(pdf);
				}
			};
			
			IRequestHandler handler = new ResourceStreamRequestHandler(resStream);
			
			getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
		}
		
		private void onCancel() {
			logDebug("Cancel pressed. Redirecting back to coach page.");
			setResponsePage(CoachPage.class);
		}
	}
}
