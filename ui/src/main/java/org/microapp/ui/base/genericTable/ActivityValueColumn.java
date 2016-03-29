package org.microapp.ui.base.genericTable;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.microapp.Diary.model.enums.ActivityUnit;


public class ActivityValueColumn extends ComponentColumn {

	private String unitField;
	private String valueField;
	
	public ActivityValueColumn(String valueField, String unitField, String name) {
		super(new ResourceModel(name+".header", name), valueField, name);
		this.valueField = valueField;
		this.unitField = unitField;
	}
	
	@Override
	public void populateItem(Item<ICellPopulator<Object>> item, String componentId,
			IModel<Object> rowModel) {
		
		Double value = (Double)getDataModel(rowModel).getObject();
		ActivityUnit unit = (ActivityUnit)(new PropertyModel(rowModel, unitField)).getObject();
		
		Label l = new Label(componentId, Model.of(value+" "+unit.getTextRepresentation()));
		item.add(l);
	}

}
