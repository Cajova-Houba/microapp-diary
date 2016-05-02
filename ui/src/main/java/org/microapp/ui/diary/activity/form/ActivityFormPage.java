package org.microapp.ui.diary.activity.form;

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
import org.microapp.ui.base.GenericSecuredPage;
import org.microapp.ui.base.form.AbstractActivityForm;
import org.microapp.ui.diary.activity.DailyActivityPage;

/**
 * Form for creating, editing or deleting activity. 
 * Provide the 'activityId' parameter, if you want to edit/delte existing activity.
 * Provide the 'date' (yyy-mm-dd) and 'memberId' parameters if you want to add a new activity.
 * @author Zdenda
 *
 */
public class ActivityFormPage extends GenericSecuredPage {
	
	private long activityId;
	private boolean activityIdLoaded;
	
	private Date date;
	private boolean dateLoaded;
	
	@SpringBean
	private DailyRecordManager dailyRecordManager;
	
	@SpringBean
	private ActivityManager activityManager;
	
	
	public ActivityFormPage(PageParameters parameters) {
		super(parameters);
	}
	
	@Override
	public void loadParameters(PageParameters parameters) {
		super.loadParameters(parameters);
		
		loadActivityId(parameters);
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
		} else if (dateLoaded && personIdLoaded) {
			//add new activity
			add(new ActivityForm(formId, formId, dailyRecordManager.getDailyRecordFrom(date, personId)));
		} else {
			//error back to homepage
			logError("Error: Neither activityId or date and memberId loaded, redirecting home.");
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
		protected void addComponents() {
			super.addComponents();
			add(new Label("forMember", getPersonName()));
		}
		
		
		@Override
		protected void onSubmit() {
			
			logDebug("Submiting activity form");
			
			validate();
			
			logDebug(this.object.toString());
			
			activityManager.save(object);
			
			//redirect to diary page
			PageParameters params = new PageParameters();
			params.add("selected", "drd");
			params.add("date", parent.getDate());
			
			params.add("personId", personId);
			
			setResponsePage(responsePage,params);
		}
		
		 /** 
		  * Cancel button pressed
		  */
		@Override
		protected void onCancel() {
			
			logDebug("Cancel pressed. Redirecting to diary page.");
			
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
				logDebug("No activity to delete.");
			} else {
				logDebug("Deleting activity: "+object.toString());
				
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

