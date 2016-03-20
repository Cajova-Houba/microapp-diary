package org.microapp.ui.diary.activity;

import java.sql.Date;
import java.util.Arrays;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.microapp.Diary.model.Activity;
import org.microapp.Diary.model.DailyRecord;
import org.microapp.Diary.model.enums.ActivityType;
import org.microapp.Diary.service.ActivityManager;
import org.microapp.Diary.service.DailyRecordManager;
import org.microapp.ui.HomePage;
import org.microapp.ui.WicketApplication;
import org.microapp.ui.base.GenericPage;
import org.microapp.ui.base.activityForm.AbstractActivityForm;
import org.microapp.ui.diary.dailyActivity.DailyActivityPage;

/**
 * Form for creating, editing or deleting activity. 
 * Provide the 'activityId' parameter, if you want to edit/delte existing activity.
 * Provide the 'date' (yyy-mm-dd) and 'memberId' parameters if you want to add a new activity.
 * @author Zdenda
 *
 */
public class ActivityFormPage extends GenericPage {
	
	private long activityId;
	private boolean activityIdLoaded;
	
	private Date date;
	private boolean dateLoaded;
	
	private long memberId;
	private boolean memberIdLoaded;
	
	@SpringBean
	private DailyRecordManager dailyRecordManager;
	
	@SpringBean
	private ActivityManager activityManager;
	
	
	public ActivityFormPage(PageParameters parameters) {
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
		
		loadActivityId(parameters);
		loadMemberId(parameters);
		loadDate(parameters);
	}
	
	private void loadDate(PageParameters parameters) {
		Date tmp = loadDateParameter(parameters, "date", false);
		dateLoaded = false;
		if (tmp != null) {
			date = tmp;
			dateLoaded = true;
		}
	}
	
	private void loadMemberId(PageParameters parameters) {
		Long tmp = loadLongParameter(parameters, "memberId", false);
		memberIdLoaded = false;
		if (tmp != null) {
			memberId = tmp.longValue();
			memberIdLoaded = true;
		}
	}
	
	private void loadActivityId(PageParameters parameters) {
		Long tmp = loadLongParameter(parameters, "activityId", false);
		activityIdLoaded = false;
		if (tmp != null) {
			activityId = tmp.longValue();
			activityIdLoaded = activityManager.exists(activityId);
		}
	}
	
	@Override
	public void inic(PageParameters parameters) {
		super.inic(parameters);
		
		if (activityIdLoaded) {
			setTitle(new ResourceModel("title.edit"));
		} else {
			setTitle(new ResourceModel("title.new"));
		}
	}
	
	@Override
	public void addComponents(PageParameters parameters) {
		super.addComponents(parameters);
		addForm("actForm");
	}
	
	private void addForm(String formId) {
		
		if (activityIdLoaded) {
			//edit activity
			add(new ActivityForm(formId, formId, activityManager.get(activityId)));
		} else if (dateLoaded && memberIdLoaded) {
			//add new activity
			add(new ActivityForm(formId, formId, dailyRecordManager.getDailyRecordFrom(date, memberId)));
		} else {
			//error back to homepage
			logger.error("Error: Neither activityId or date and memberId loaded, redirecting home.");
			setResponsePage(HomePage.class);
		}
	}
	
	private class ActivityForm extends AbstractActivityForm<Activity, DailyRecord> {
		
		private static final long serialVersionUID = 1L;
		
		private Class<? extends GenericPage> responsePage = DailyActivityPage.class;

		public ActivityForm(String id, String name, DailyRecord dr) {
			super(id, name, dr);
			
			this.object = new Activity();
			object.setName("New activity");
			object.setValue(0);
			object.setDailyRecord(dr);
			
			
			addComponents();
		}
		
		public ActivityForm(String id, String name, Activity activity) {
			super(id, name, activity);
			
			this.parent = activity.getDailyRecord();
			
			addComponents();
		}
		
		
		@Override
		protected void onSubmit() {
			
			logger.debug("Submiting activity form");
			
			validate();
			
			logger.debug(this.object.toString());
			
			activityManager.save(object);
			
			//redirect to diary page
			PageParameters params = new PageParameters();
			params.add("selected", "drd");
			params.add("date", parent.getDate());
			
			params.add("personId", memberId);
			
			setResponsePage(responsePage,params);
		}
		
		 /** 
		  * Cancel button pressed
		  */
		@Override
		protected void onCancel() {
			
			logger.debug("Cancel pressed. Redirecting to diary page.");
			
			//redirect to diary page
			PageParameters params = new PageParameters();
			params.add("selected", "drd");
			params.add("date", parent.getDate());
			
			params.add("personId", parent.getPersonId());
			
			setResponsePage(responsePage,params);
		}
		
		/**
		 * Delete button pressed
		 */
		@Override
		protected void onDelete() {
			
			//nothing to delete
			if (newObject) {
				logger.debug("No activity to delete.");
			} else {
				logger.debug("Deleting activity: "+object.toString());
				
				activityManager.remove(object.getId());
				
				//redirect to diary page
				PageParameters params = new PageParameters();
				params.add("selected", "drd");
				params.add("date", parent.getDate());
				
				params.add("personId", parent.getPersonId());
				
				setResponsePage(responsePage,params);
			}
		}
	}
}

