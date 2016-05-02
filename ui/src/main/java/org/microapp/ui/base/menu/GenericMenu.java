package org.microapp.ui.base.menu;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

public class GenericMenu extends Panel {
	private static final long serialVersionUID = 1L;

	private final static Logger logger = LogManager.getLogger(GenericMenu.class);
	
	private static final String MENU_ITEMS_ID = "genericMenu";
	
	/**
	 * Used in properties file
	 */
	private String name;
	
	public GenericMenu(String id, List<MenuItem> menuItems) {
		super(id);
		logger.trace("Creating menu:"+id+" with "+menuItems.size()+" items.");
		name = id;
		addMenuItemsByIds(menuItems);
	}
	
	public void addMenuItemsByIds(List<MenuItem> menuItems) {
		RepeatingView menu = new RepeatingView(MENU_ITEMS_ID);
		for(MenuItem menuItem : menuItems) {
			ExternalLink link = new ExternalLink(menu.newChildId(), new ResourceModel(name+"."+menuItem.getId()+".link", "#"));
			
			String captionKey = name+"."+menuItem.getId()+".caption";
			
			//simple caption
			IModel linkBody = new ResourceModel(captionKey,menuItem.getId());
			
			try {
				if(menuItem.getParamName() != null && menuItem.getParamName().length() > 0) {
					IModel tmp;
					if(menuItem.getWrapperObject() != null) {
						tmp = new StringResourceModel(captionKey+"."+menuItem.getParamName(), Model.of(menuItem.getWrapperObject()));
					} else {
						tmp = new StringResourceModel(captionKey+"."+menuItem.getParamName());	
					}
				
					//everything is ok
					linkBody = tmp;
				}
			} catch(Exception e) {
				logger.error("Error when loading model for caption of item id="+menuItem.getId()+". "+e.toString());
				e.printStackTrace();
			}
			
			link.setBody(linkBody);
			menu.add(link);
		}
		
		add(menu);
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
