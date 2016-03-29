package org.microapp.ui.diary.plans.report;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;














import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.extensions.yui.calendar.DateField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.microapp.Diary.service.ReportManager;
import org.microapp.ui.WicketApplication;
import org.microapp.ui.base.GenericPage;
import org.microapp.ui.base.GenericSecuredPage;
import org.microapp.ui.diary.plans.PlansPage;

public class PlanReportPage extends GenericSecuredPage {

	private final String REPORT_FROM_ID = "reportForm";
	
	@SpringBean
	private ReportManager reportManager;
	
	public PlanReportPage(PageParameters parameters) {
		super(parameters);
	}
	
	@Override
	public void addComponents(PageParameters parameters) {
		super.addComponents(parameters);
		
		addReportForm();
	}
	
	private void addReportForm() {
		ReportForm rf = new ReportForm(REPORT_FROM_ID, personId);
		add(rf);
	}
	
	private void onCancel() {
		logger.debug("Cancel pressed, redirecting back to plans page.");
		
		setResponsePage(PlansPage.class);
	}
	
	private class ReportForm extends Form {

		private DateField from,to;
		private CheckBox completed,uncompleted;
		private Button cancelBtn;
		
		//to be used later
		private long personId;
		
		public ReportForm(String id, long personId) {
			super(id);
			this.personId = personId;
			
			addComponents();
		}
		
		private void addComponents() {
			from = new DateField("from", Model.of(new Date()));
			to = new DateField("to", Model.of(new Date()));
			completed = new CheckBox("completed", Model.of(Boolean.FALSE));
			uncompleted = new CheckBox("uncompleted", Model.of(Boolean.TRUE));
			cancelBtn = new Button("cancelBtn") {
				@Override
				public void onSubmit() {
					onCancel();
				}
			};
			cancelBtn.setDefaultFormProcessing(false);
			
			
			add(from);
			add(to);
			add(completed);
			add(uncompleted);
			add(cancelBtn);
		}
		
		@Override
		protected void onSubmit() {
			logger.debug("Submitting report form.");
			final java.sql.Date fromDate = new java.sql.Date(from.getDate().getTime());
			final java.sql.Date toDate = new java.sql.Date(to.getDate().getTime());
			final boolean comp = completed.getModelObject();
			final boolean uncomp = uncompleted.getModelObject();
			
			logger.debug(String.format("from=%s, to=%s, completed=%b, uncompleted=%b", fromDate, toDate, comp, uncomp));
			
			final byte[] pdf = reportManager.exportToPdf(reportManager.makePlanReport(personId, fromDate, toDate, comp, uncomp));
			logger.debug("Pdf generated");
			
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
			logger.debug("Cancel pressed, redirecting back to plans page.");
			
			setResponsePage(PlansPage.class);
		}
	}
}
