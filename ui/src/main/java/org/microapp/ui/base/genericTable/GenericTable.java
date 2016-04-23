package org.microapp.ui.base.genericTable;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;

public class GenericTable extends Panel {

	private static final long serialVersionUID = 1L;

	/**
	 * Use this constant in constructor if you want all fields to be displayed.
	 */
	public static final List<String> ALL_FIELDS = new ArrayList<String>(0);
	
	protected String TABLE_ID = "table";
	
	private List<String> fieldNames;
	
	private Class<? extends Serializable> clazz;
	
	private IModel<List<? extends Serializable>> values;
	
	private Map<String, String> headers;
		
	private DataTable<? extends Serializable, String> table;
	
	/**
	 * Generic table.
	 * 
	 * @param id Wicket id.
	 * @param clazz	Class you want to make table for. Must extend BaseObject.
	 * @param values	Model for values to be displayed.
	 * @param fieldNames	List of names of fields you want to display in table. Use GenericTable.ALL_FIELDS for all fields. 
	 * 						If you're not sure, I recommend creating list with field names (you can save troubles with for example lazy loading). 
	 * @param headers	Specify a header for each field. Use field name as key and header as value. 
	 * 					If no header is provided for a field, then the GenericPage will try to load property with key fieldName.header from resource file (your page properties file) and if the key is not found, field name will be used as header.
	 *
	 */
	public GenericTable(String id, Class<? extends Serializable> clazz, IModel<List<? extends Serializable>> values, 
	List<String> fieldNames, Map<String, String> headers) {
		super(id);
		this.values = values;
		if (fieldNames == null) {
			this.fieldNames = new ArrayList<String>(0);
		} else {
			this.fieldNames = fieldNames;
		}
		this.clazz = clazz;
		this.headers = headers;
		
		if (this.headers == null) {
			this.headers = new HashMap<String, String>();
		} 
		
		if (this.values == null) {
			this.values = new ListModel();
		}
		
		addTable();
	}
	
	/**
	 * Generic table.
	 * 
	 * @param id Wicket id.
	 * @param clazz	Class you want to make table for. Must extend BaseObject.
	 * @param values	Values to be displayed.
	 * @param fieldNames	List of names of fields you want to display in table. Use GenericTable.ALL_FIELDS for all fields. 
	 * 						If you're not sure, I recommend creating list with field names (you can save troubles with for example lazy loading). 
	 * @param headers	Specify a header for each field. Use field name as key and header as value. 
	 * 					If no header is provided for a field, then the GenericPage will try to load property with key fieldName.header from resource file (your page properties file) and if the key is not found, field name will be used as header.
	 *
	 */
	public GenericTable(String id, Class<? extends Serializable> clazz, List<? extends Serializable> values, 
			List<String> fieldNames, Map<String, String> headers) {
		this(id,clazz,new ListModel(values),fieldNames,headers);
	}
	
	private boolean isGetter(Method method) {
		if(!method.getName().startsWith("get"))      return false;
		if(method.getParameterTypes().length != 0)   return false;
		if(void.class.equals(method.getReturnType())) return false;
		return true;
	}
	
	private List<String> getGettersNames(Class<? extends Serializable> c) {
		List<String> gettersNames = new ArrayList<String>();
		Method[] methods = c.getMethods();
		
		for(Method m : methods) {
			if(isGetter(m)) {
				gettersNames.add(m.getName());
			}
		}
		return gettersNames;
	}
	
	/**
	 * Uses reflection to get getters names from {@code clazz}. Then it return a list of field names. 
	 * Method assumes that getter name is constructed like this: get+FieldNameWithFirstLetterUpperCase and
	 * private getters are ignored.
	 * Method will also check if found fields really exists in {@code clazz}.
	 * 
	 * This method is called only if {@code fieldNames} in constructor is {@code GenericTable.ALL_FIELDS}.
	 * @return
	 */
	protected List<String> getFieldNamesFromClazz() {
		List<String> fNames = new ArrayList<>();
		
		//get all getters for clazz
		List<String> getters = getGettersNames(clazz);
		
		//remove the 'get' at the beginning of the method name and make the first letter lowercase
		for (String g : getters) {
			fNames.add(Character.toLowerCase(g.charAt(3)) + g.substring(4, g.length()));
		}
		
		return fNames;
	}
	
	/**
	 * Uses the {@code fieldNames} or {@code getFieldnamesFromClazz()} to get field names and returns an array of 
	 * {@code PropertyColumn}.
	 * @return Array of {@code PropertyColumn}
	 */
	protected List<IColumn>	getColumns() {
		
		//check if field names are empty
		if (this.fieldNames.isEmpty()) {
			this.fieldNames = getFieldNamesFromClazz();
		}
		
		//use PropertyColumn and field names to create list of columns
		List<IColumn> columns = new ArrayList<>();
		for(String fieldName : fieldNames) {
			
			//if there is no header specified, use field name as header
			IModel<String> header;
			if(headers.containsKey(fieldName)) {
				header = new Model(headers.get(fieldName));
			} else {
				header = new ResourceModel(fieldName+".header", fieldName);
			}
			
			//header and property name
			columns.add(new FormatedPropertyColumn<>(header, fieldName));
		}
		
		return columns;
	}
	
	/**
	 * Override this method and add your Components Columns here.
	 * @return
	 */
	protected List<ComponentColumn> getComponentColumns() {
		List<ComponentColumn> cColumns = new ArrayList<>(0);
		
//		cColumns.add(new ButtonColumn(new StringResourceModel("componentColumn1.header"), "id"));
		
		return cColumns;
	}
	
	/**
	 * Override thid method to specify custom wicket id for your component.
	 * @return
	 */
	protected String getTableId() {
		return this.TABLE_ID;
	}
	
	private void addTable() {
		final BaseObjectDataProvider<? extends Serializable> dataProvider = new BaseObjectDataProvider(values);
		
		//normal columns
		List<IColumn> columns = getColumns();
		
		//component columns
		columns.addAll(getComponentColumns());
		
		//create data table with header toolbar
		table = new DataTable(getTableId(), columns, dataProvider, 10);
		table.addTopToolbar(new HeadersToolbar(table, null));
		table.setOutputMarkupId(true);
		//add the table
		add(table);
	}
	
	public void newData(IModel<List<? extends Serializable>> newModel) {
		//table.getDataProvider().detach();
		((BaseObjectDataProvider)table.getDataProvider()).newData(newModel);
	}
	
	public void refresh(AjaxRequestTarget target) {
		target.add(table);
	}
}
