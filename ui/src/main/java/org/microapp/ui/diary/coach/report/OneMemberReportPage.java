package org.microapp.ui.diary.coach.report;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

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
import org.microapp.Diary.service.ReportManager;
import org.microapp.ui.base.GenericAdminPage;
import org.microapp.ui.diary.coach.CoachPage;

public class OneMemberReportPage extends GenericAdminPage {
	private static final long serialVersionUID = 1L;

	private final String REPORT_FORM_ID = "reportForm";
	
	@SpringBean
	private ReportManager reportManager;
	
	public OneMemberReportPage(PageParameters parameters) {
		super(parameters);
	}
	
	@Override
	protected void addComponents(PageParameters parameters) {
		super.addComponents(parameters);
		addReportForm();
	}
	
	private void addReportForm() {
		ReportForm rf = new ReportForm(REPORT_FORM_ID, personId);
		add(rf);
	}
	
	private class ReportForm extends Form {
		
		private DateField actFrom, actTo, planFrom, planTo;
		private CheckBox completed, uncompleted;
		private Button cancelBtn;
		
		private long personId;
		
		public ReportForm(String id, long personId) {
			super(id);
			this.personId = personId;
			
			addComponents();
		}
		
		private void addComponents() {
			actFrom = new DateField("actFrom", Model.of(new Date()));
			actTo = new DateField("actTo", Model.of(new Date()));
			planFrom = new DateField("planFrom", Model.of(new Date()));
			planTo = new DateField("planTo", Model.of(new Date()));
			completed = new CheckBox("completed", Model.of(Boolean.FALSE));
			uncompleted = new CheckBox("uncompleted", Model.of(Boolean.TRUE));
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
			add(cancelBtn);
			add(new FeedbackPanel("feedback"));
		}
		
		@Override
		protected void onSubmit() {
			logDebug("Submitting form.");
			
			java.sql.Date aFrom = new java.sql.Date(actFrom.getModelObject().getTime());
			java.sql.Date aTo = new java.sql.Date(actTo.getModelObject().getTime());
			java.sql.Date pFrom = new java.sql.Date(planFrom.getModelObject().getTime());
			java.sql.Date pTo = new java.sql.Date(planTo.getModelObject().getTime());
			boolean comp = completed.getModelObject();
			boolean uncomp = uncompleted.getModelObject();
			
			logDebug(String.format("actFrom=%s, actTo=%s, planFrom=%s, planTo=%s, completed=%b, uncompleted=%b", 
							aFrom, aTo, pFrom, pTo, comp, uncomp));
			
			final byte[] pdf = reportManager.exportToPdf(reportManager.makeCompleteReport(personId, pFrom, pTo, comp, uncomp, aFrom, aTo));
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
			logDebug("Cancel pressed, redirecting back to coach page.");
			setResponsePage(CoachPage.class);
		}
	}
	
}
