package org.microapp.ui.base.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a mennu object. It can be either just one string containing the component id/ resource key, or
 * wrapper object for parameter can be specified.
 * @author Zdenda
 *
 */
public class MenuItem implements Serializable {
	private static final long serialVersionUID = 1L;

	
	/**
	 * Wicket id and resource key. Mandatory.
	 */
	private String id;
	
	/**
	 * Parameter specifying which caption from resource bundle will be used. Not mandatory.
	 * 
	 * Example:
	 * menuName.id.caption.1=Caption 1
	 * menuName.id.caption.2=Caption 2
	 * menuName.id.caption.3=Caption 3
	 * 
	 * If the paramName is '2', then the 'Caption 2' will be used as a caption.
	 */
	private String paramName;
	
	/**
	 * Wrapper object for parameter in resource values.
	 * 
	 * Example:
	 * menuName.id.caption.${val1}=Caption ${val2}
	 * menuName.id.caption.${val1}=Caption ${val2}
	 * menuName.id.caption.${val1}=Caption ${val2}
	 * 
	 * If the paramName is '${val1}' then the wrapper object is expected to have fields (and getters/setters) named 
	 * 'val1' and 'val2'.
	 */
	private Serializable wrapperObject;
	
	/**
	 * Constructor for specifying all parameters.
	 * @param id Wicket id.
	 * @param paramName Parameter specifying which caption from resource bundle will be used. Not mandatory.
	 * @param wrapperObject Wrapper object for parameter in resource values.
	 */
	public MenuItem(String id, String paramName, Serializable wrapperObject) {
		super();
		this.id = id;
		this.paramName = paramName;
		this.wrapperObject = wrapperObject;
	}
	
	/**
	 * Use this constructor when you don't need to pass any values from wrapping object.
	 * @param id Wicket id.
	 * @param paramName Parameter specifying which caption from resource bundle will be used. Not mandatory.
	 */
	public MenuItem(String id, String paramName) {
		this(id, paramName, null);
	}
	
	/**
	 * Use this constructor when you just want a god damned piece of text as a caption.
	 * @param id Wicket id.
	 */
	public MenuItem(String id) {
		this(id, null, null);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public Serializable getWrapperObject() {
		return wrapperObject;
	}

	public void setWrapperObject(Serializable wrapperObject) {
		this.wrapperObject = wrapperObject;
	}
	
	/**
	 * Use this method when you want to create a simple menu items from the list of wicket ids.
	 * @param names Wicket ids / names of the item in te resources bundle.
	 * @return
	 */
	public static List<MenuItem> fromStringList(List<String> names) {
		List<MenuItem> menuItems = new ArrayList<MenuItem>(names.size());
		
		for(String s : names) {
			menuItems.add(new MenuItem(s));
		}
		
		return menuItems;
	}
}
