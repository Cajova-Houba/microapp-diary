package org.microapp.ui.base.form;

import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;

/**
 * Ajax button with confiramtion message. Taken from
 * 
 * <a href="https://cwiki.apache.org/confluence/display/WICKET/Getting+user+confirmation">here</a>.
 * 
 * @author Zdenda
 */
public abstract class ConfirmationButton<T> extends AjaxLink<T> {
	private static final long serialVersionUID = 1L;

	private IModel<String> text;
	
	public ConfirmationButton(String id, IModel<String> text) {
		super(id);
		this.text = text;
	}
	
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		
		AjaxCallListener ajaxCallListener = new AjaxCallListener();
		ajaxCallListener.onPrecondition("return confirm('"+text.getObject()+"');");
		attributes.getAjaxCallListeners().add(ajaxCallListener);
	}
}
