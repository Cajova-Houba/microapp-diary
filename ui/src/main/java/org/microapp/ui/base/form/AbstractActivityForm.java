package org.microapp.ui.base.form;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.microapp.Diary.generic.model.BaseObject;
import org.microapp.Diary.model.Activity;
import org.microapp.Diary.model.BaseActivityObject;
import org.microapp.Diary.model.enums.ActivityType;
import org.microapp.Diary.model.enums.ActivityUnit;



/**
 * A form component for activity based objects - Activity and Goal.
 * 
 * When using this, be sure to have the 'confirmationMessage' property specified. This will be displayed when deleting
 * the object.
 * 
 * @param <T> Object to be displayed by form.
 * @param <P> Parent of displayed object.
 * @author Zdenda
 *
 */
public abstract class AbstractActivityForm<T extends BaseActivityObject, P extends BaseObject> extends Form{

	protected boolean newObject;
	protected P parent;
	protected T object;
	
	protected FeedbackPanel feedbackPanel;
	protected Button cancelButton;
	protected Button deleteButton;
	protected TextField nameTf;
	protected NumberTextField<Double> valueNtf;
	protected DropDownChoice activityType;
	protected DropDownChoice activityUnit;
	protected Label header;
	
	protected final String name;
	
	public AbstractActivityForm(String id) {
		super(id);
		name = "form";
	}
	
	/**
	 * Use this constructor for displaying existing object.
	 * @param id Wicket id.
	 * @param name Name of the form. Used for accessing properties.
	 * @param object Object to be displayed in form.
	 */
	public AbstractActivityForm(String id, String name, T object) {
		super(id);
		
		this.name = name;
		newObject = false;
		
		this.object = object;
	}
	
	/**
	 * Use this constructor for displaying new object.
	 * @param id Wicket id.
	 * @param name Name of the form. Used for accessing properties.
	 * @param parent Parent of displayed object. For activity, the parent is daily record. For goal, the parent is plan.
	 */
	public AbstractActivityForm(String id, String name, P parent) {
		super(id);
		
		this.name = name;
		newObject = true;

		this.parent = parent;
	}
	
	/**
	 * This method needs to be called manually extending class.
	 */
	protected void addComponents() {
		setDefaultModel(new CompoundPropertyModel(this));
		
		feedbackPanel =  new FeedbackPanel("feedbackPanel");
		
		if (newObject) {
			header = new Label("header", new ResourceModel(name+".header.new", "Form header"));
		} else {
			header = new Label("header", new ResourceModel(name+".header.existing", "Form header")); 
		}
		
		cancelButton = new Button("cancelButton") {
			@Override
			public void onSubmit() {
				onCancel();
			}
		};
		cancelButton.setDefaultFormProcessing(false);
		
		deleteButton = new Button("deleteButton") {

			@Override
			public void onSubmit() {
				onDelete();
			}
		};
		deleteButton.setDefaultFormProcessing(false);
		
		if(!newObject) {
			deleteButton.add(new AttributeAppender("onClick", "return confirm('"+new ResourceModel("confirmationMessage","Are you sure?").getObject()+"');"));
		}
		
		
		nameTf = new TextField("name", new PropertyModel<Activity>(this.object,"name"));
		nameTf.setRequired(true);
		
		valueNtf = new NumberTextField("value", new PropertyModel<Activity>(this.object, "value"));
		valueNtf.setStep(0.01);
		valueNtf.setRequired(true);
		
		activityType = new DropDownChoice<ActivityType>("activityType",new PropertyModel(this.object,"activityType"), Arrays.asList(ActivityType.values()));
		activityType.setRequired(true);
		
		ChoiceRenderer choiceRenderer = new ChoiceRenderer<T>("value", "key");
		activityUnit = new DropDownChoice<ActivityUnit>("activityUnit",new PropertyModel(this.object,"activityUnit"), Arrays.asList(ActivityUnit.values()), choiceRenderer);
		activityUnit.setRequired(true);
		
		
		add(nameTf);
		add(valueNtf);
		add(activityType);
		add(activityUnit);
		
		add(cancelButton);
		add(deleteButton);
		
		add(feedbackPanel);
		add(header);
	}
	
	/**
	 * Reaction to click on the cancel button.
	 */
	protected abstract void onCancel();
	
	/**
	 * Reaction to click on the delete button.
	 */
	protected abstract void onDelete();
	
}
