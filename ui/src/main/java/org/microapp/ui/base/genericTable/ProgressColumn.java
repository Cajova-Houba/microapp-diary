package org.microapp.ui.base.genericTable;


import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Column for displaying progress. The input value from the row model is expected to be double.
 * In that case, color formating will be applied. If the input value from the row model isn't double,
 * plain text will be displayed.
 * 
 * Formating:
 * 	output will be formated as (d)d% with bold font. 
 * 	The color will depend on the input value:
 * 		[0%;15%): red
 * 		[15%;85%): yellow
 * 		[85%;100%): something between yellow and green
 * 		100%: green 
 * 
 * @author Zdenda
 *
 */
public class ProgressColumn extends ComponentColumn {

	private final String redColor = "#FF0000";
	private final String yellowColor = "#FF8800";
	private final String ygreenColor = "#B0BC00";
	private final String greenColor = "#66EE00";
	
	/**
	 * Specify the property expression which will be used to get the value from the row model and name of the column.
	 * The name will be used to load resources from properties file. 
	 * 
	 * If the name.header key is specified in property file, its value will be used as header. Otherwise value of name will be used.
	 * 
	 * @param propertyExpression Property which will be used similar as in PropertyColumn(). The model class in your data table must contain this property.
	 * @param name Name of the column, used for resource names (in property files)
	 */
	public ProgressColumn(String propertyExpression, String name) {
		super( new ResourceModel(name+".header",name), propertyExpression, name);
	}
	
	@Override
	public void populateItem(Item<ICellPopulator<Object>> item, String componentId,
			IModel<Object> rowModel) {
		
		//get property model
		IModel propModel = getDataModel(rowModel);
		
		//if it's double, then format it
		if(propModel.getObject() instanceof Double) {
			double val = ((Double)propModel.getObject()).doubleValue();
			if (val == Double.NaN || val < 0) {
				val = 0;
			}
			
			Label l = new Label(componentId, (int)Math.floor(val*100)+"%");
			
			//bold
			l.add(new AttributeAppender("style", "font-weight:bold; "));
			
			//color
			String color = "";
			if (val < 0.15) {
				color = redColor;
			} else if (val < 0.85) {
				color = yellowColor;
			} else if (val < 1) {
				color = ygreenColor;
			} else {
				color = greenColor;
			}
			l.add(new AttributeAppender("style","color:"+color+"; "));
			
			item.add(l);
		} else {
			item.add(new Label(componentId, getDataModel(rowModel)));
		}
	}
	

}
