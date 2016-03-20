package org.microapp.ui.diary.plans;

import java.util.List;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.microapp.Diary.model.Plan;
import org.microapp.Diary.service.PlanManager;
import org.microapp.ui.WicketApplication;
import org.microapp.ui.base.GenericPage;
import org.microapp.ui.diary.plans.displayer.PlanDisplayer;

public class PlansPage extends GenericPage {

	private final String CONTENT_ID = "content";
	
	private long personId;
	private boolean personIdLoaded;
	
	public PlansPage(PageParameters parameters) {
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
		
		loadPersonId(parameters);
	}
	
	private void loadPersonId(PageParameters parameters) {
		personIdLoaded = false;
		Long tmp = loadLongParameter(parameters, "personId", false);
		if (tmp != null) {
			personId = tmp.longValue();
			personIdLoaded = membernetManager.exists(personId);
		} 
		
		if(!personIdLoaded) {
			logger.debug("No personId loaded, use loggedId instead.");
			personId = getloggedUserId();
			personIdLoaded = true;
		}
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
