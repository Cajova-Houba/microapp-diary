package org.microapp.ui.diary.activity;

import java.sql.Date;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.microapp.ui.base.GenericSecuredPage;
import org.microapp.ui.diary.activity.displayer.DailyRecordDisplayer;

public class DailyActivityPage extends GenericSecuredPage {

	private final String CONTENT_ID = "content";
	
	/**
	 * For which date activities should be displayed.
	 */
	private Date date;
	private boolean dateLoaded;
	
	
	public DailyActivityPage(PageParameters parameters) {
		super(parameters);
	}
	
	@Override
	public void loadParameters(PageParameters parameters) {
		super.loadParameters(parameters);
		
		loadDate(parameters);
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
