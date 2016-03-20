package org.microapp.ui.base.genericTable;

import java.util.Collection;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * This column will display buttons in membership table with links to the diary microapplication.
 * Specify the link in property file using key {@code name.link} where name is the string specified in constructor.
 * Resources:
 * <ul>
 * 	<li>{@code name.header} - Header for the column, if not specified via constructor.</li>
 * 	<li>{@code name.caption} - Button caption.</li>
 * 	<li>{@code name.link} - Link this button will redirect to.</li>
 * </ul>
 * @author Zdenda
 *
 */
public class ButtonColumn extends ComponentColumn {

	private static final long serialVersionUID = 1L;

	/**
	 * You don't have to specify the {@code displayModel}. Instead the value for key {@code name.header} will be used as header.
	 * 
	 * For button caption use {@code name.caption} property.
	 * 
	 * @param propertyExpression Property which will be used similar as in PropertyColumn(). The model class in your data table must contain this property.
	 * @param name Name of the column, used for resource names (in property files)
	 */
	public ButtonColumn(String propertyExpression, String name) {
		super( new ResourceModel(name+".header",name), propertyExpression, name);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ButtonColumn(IModel displayModel, String propertyExpression, String name) {
		super(displayModel, propertyExpression, name);
	}


	@Override
	public void populateItem(Item<ICellPopulator<Object>> item, String componentId,
			IModel<Object> rowModel) {
				
		AbstractLink link = getLink(rowModel);
		
		CustomButton button = new CustomButton(componentId, new ResourceModel(getName()+".caption", getName()), link);
		
		item.add(button);
		
	}
	
	/**
	 * Override this method to specify custom link for buttons. You can use row model to get the value of specified property.
	 * @param rowModel Row model passed from dataTable. Use {@code CustomButton.LINK_ID} for wicket id of your link.
	 * @return
	 */
	public AbstractLink getLink(IModel<Object> rowModel) {
		AbstractLink link = new ExternalLink(CustomButton.LINK_ID, 
							new StringResourceModel(getName()+".link",rowModel)){
			@Override
			protected void onComponentTag(ComponentTag arg0) {
			// TODO Auto-generated method stub
			super.onComponentTag(arg0);
			//arg0.put("target", "_blank");
			}
		};
		
		return link;
	}
	
}
