package org.microapp.ui.base.genericTable;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Column used for displaying components in table. Extend and use the {@code populateItem()} method to 
 * populate your components.
 * @author Zdenda
 *
 */
public abstract class ComponentColumn implements IColumn<Object, Object> {

	private static final long serialVersionUID = 1L;
	
	private IModel displayModel;
	
	private String propertyExpression;
	
	private String name;
	
	/**
	 * Component column.
	 * @param displayModel Model for the header of this column.
	 * @param propertyExpression Property which will be use similary to PropertyColumn(). The model class in your data table must contain this property.
	 * @param name Name of the column, used for resource names (in property files)
	 */
	public ComponentColumn(IModel displayModel, final String propertyExpression, String name) {
		this.displayModel = displayModel;
		this.propertyExpression = propertyExpression;
		this.name = name;
	}
	
	/**
	 * Same implementation as in PropertyColumn. So if the property expression is specified, it's possible to get the value of certain property
	 * from row model and return it.
	 * @param rowModel
	 * @return
	 */
	protected IModel<Object> getDataModel(IModel<Object> rowModel) {
		PropertyModel<Object> propertyModel = new PropertyModel<>(rowModel, getPropertyExpression());
		
		return propertyModel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IModel getDisplayModel() {
		return displayModel;
	}

	public void setDisplayModel(IModel displayModel) {
		this.displayModel = displayModel;
	}

	public String getPropertyExpression() {
		return propertyExpression;
	}

	public void setPropertyExpression(String propertyExpression) {
		this.propertyExpression = propertyExpression;
	}
	
	@Override
	public void detach() {
		
		//same implementation as in AbstractColumn
		if (displayModel != null) {
			displayModel.detach();
		}
	}

	@Override
	public Component getHeader(String componentId) {
		return new Label(componentId, displayModel);
	}

	@Override
	public String getSortProperty() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isSortable() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
