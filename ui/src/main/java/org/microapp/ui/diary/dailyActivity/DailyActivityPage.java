package org.microapp.ui.diary.dailyActivity;

import java.sql.Date;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.microapp.ui.WicketApplication;
import org.microapp.ui.base.GenericPage;
import org.microapp.ui.diary.displayer.dailyRecord.DailyRecordDisplayer;

public class DailyActivityPage extends GenericPage {

	private final String CONTENT_ID = "content";
	
	/**
	 * For which date activities should be displayed.
	 */
	private Date date;
	private boolean dateLoaded;
	
	/**
	 * For which person daily activities should be displayed.
	 */
	private long personId;
	private boolean personIdLoaded;
	
	public DailyActivityPage(PageParameters parameters) {
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
		loadDate(parameters);
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
	
	private void loadDate(PageParameters parameters) {
		dateLoaded = false;
		Date tmp = loadDateParameter(parameters, "date", false);
		if(tmp != null) {
			date = tmp;
			dateLoaded = true;
		}
	}
	
	@Override
	public void addComponents(PageParameters parameters) {
		super.addComponents(parameters);
		
		addDailyRecordDisplayer();
	}
	
	private void addDailyRecordDisplayer() {
		DailyRecordDisplayer drd = new DailyRecordDisplayer(CONTENT_ID, getloggedUserId(), personId);
		add(drd);
	}
}
