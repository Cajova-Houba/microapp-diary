package org.microapp.ui.diary.plans;

import java.util.List;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.microapp.Diary.model.Plan;
import org.microapp.Diary.service.PlanManager;
import org.microapp.ui.WicketApplication;
import org.microapp.ui.base.GenericPage;
import org.microapp.ui.base.GenericSecuredPage;
import org.microapp.ui.diary.plans.displayer.PlanDisplayer;

public class PlansPage extends GenericSecuredPage {

	private final String CONTENT_ID = "content";
	
	public PlansPage(PageParameters parameters) {
		super(parameters);
	}
	
	@Override
	public void addComponents(PageParameters parameters) {
		super.addComponents(parameters);
		
		addPlansDisplayer();
	}
	
	private void addPlansDisplayer() {
		PlanDisplayer pd = new PlanDisplayer(CONTENT_ID, getloggedUserId(), personId);
		add(pd);
	}
}
