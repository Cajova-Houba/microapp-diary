package org.microapp.ui.base.genericTable;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Creates a button with link and a caption.
 * @author Zdenda
 *
 */
public class CustomButton extends Panel {

	private static final long serialVersionUID = 1L;

	private final String CAPTION_ID = "caption";
	
	public static final String LINK_ID = "button";
	
	private AbstractLink link;
	private IModel<?> caption;
	
	/**
	 * Contructor
	 * @param id Wicket id of the component-
	 * @param caption Model of desired caption.
	 * @param link Link with wicket id {@code CustomButton.LINK_ID}.
	 */
	public CustomButton(String id, IModel<?> caption, AbstractLink link) {
		super(id, caption);
		
		this.link = link;
		this.caption = caption;
		
		addComponents();
	}
	
	private void addComponents() {
		
		//caption
		Label caption = new Label(CAPTION_ID, this.caption);

		//link
		link.add(caption);
		
		add(link);
	}


}
