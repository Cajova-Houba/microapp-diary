package org.microapp.ui.base.genericTable;

import java.sql.Date;
import java.util.Locale;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;
import org.microapp.ui.base.converters.SqlDateConverter;

/**
 * Property column with converter for better value displaying.
 * @author Zdenda
 *
 * @param <T>
 */
public class FormatedPropertyColumn<T> extends PropertyColumn<T, String>{

	private static final long serialVersionUID = 1L;

	private IConverter converter;
	
	/**
	 * Use this constructor to specify custom converter.
	 * @param displayModel
	 * @param propertyExpression
	 * @param converter
	 */
	public FormatedPropertyColumn(IModel<String> displayModel, String propertyExpression, IConverter converter) {
		super(displayModel, propertyExpression);
		
		this.converter = converter;
	}
	
	/**
	 * Converter will be chosen by ConverterLocator from application.
	 * @param displayModel
	 * @param propertyExpression
	 */
	public FormatedPropertyColumn(IModel<String> displayModel, String propertyExpression) {
		 super(displayModel, propertyExpression);
		 
		 this.converter = null;
	}

	@Override
	public void populateItem(Item<ICellPopulator<T>> item, String componentId,
			IModel<T> rowModel) {
		
		IModel propModel = getPropertyModel(rowModel);
		
		if (converter == null) {
			super.populateItem(item, componentId, rowModel);
			return;
		} 
		
		item.add(new Label(componentId, converter.convertToString(propModel.getObject(), Locale.getDefault())));
	}
	
	private IModel getPropertyModel(IModel<T> rowModel) {
		return new PropertyModel<T>(rowModel, getPropertyExpression());
	}
	

}
