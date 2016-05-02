package org.microapp.ui.diary.activity.report;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.extensions.yui.calendar.DateField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
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
import org.microapp.ui.WicketApplication;
import org.microapp.ui.base.GenericPage;
import org.microapp.ui.base.GenericSecuredPage;
import org.microapp.ui.diary.plans.PlansPage;

public class DailyRecordReportPage extends GenericSecuredPage {
	
	private final String REPORT_FORM_ID = "reportForm";
	
	@SpringBean
	private ReportManager reportManager;
	
	public DailyRecordReportPage(PageParameters parameters) {
		super(parameters);
	}
	
	@Override
	public void addComponents(PageParameters parameters) {
		super.addComponents(parameters);
		addReportForm();
	}
	
	private void addReportForm() {
		ReportForm rf = new ReportForm(REPORT_FORM_ID, personId);
		add(rf);
	}
	
	private class ReportForm extends Form {

		private DateField from;
		private DateField to;
		private Button cancelButton;
		private long personId;
		
		public ReportForm(String id, long personId) {
			super(id);
			this.personId = personId;
			addComponents();
		}
		
		private void addComponents() {
			from = new DateField("from", Model.of(new Date()));
			to = new DateField("to", Model.of(new Date())); 
			cancelButton = new Button("cancelBtn") {
				@Override
				public void onSubmit() {
					onCancel();
				}
			};
			cancelButton.setDefaultFormProcessing(false);
			
			add(new Label("memberName", getPersonName()));
			add(from);
			add(to);
			add(cancelButton);
			add(new FeedbackPanel("feedback"));
		}
		
		@Override
		protected void onSubmit() {
			logDebug("Submitting form");
			
			java.sql.Date fromDate = new java.sql.Date(from.getModelObject().getTime());
			java.sql.Date toDate = new java.sql.Date(to.getModelObject().getTime());
			logDebug(String.format("from=%s, to=%s", fromDate, toDate));
			
			final byte[] pdf = reportManager.exportToPdf(reportManager.makeDailyRecordReport(personId, fromDate, toDate));
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
			logDebug("Cancel pressed, redirecting back daily activities page.");
			
			setResponsePage(PlansPage.class);
		}
	}
}
