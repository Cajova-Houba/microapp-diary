package org.microapp.ui.base.menu;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.ResourceModel;

public class GenericMenu extends Panel {
	private static final long serialVersionUID = 1L;

	private static final String MENU_ITEMS_ID = "genericMenu";
	
	/**
	 * Used in properties file
	 */
	private String name;
	
	public GenericMenu(String id, List<String> linkNames) {
		super(id);
		name=id;
		addMenuItemsByIds(linkNames);
	}

	public void addMenuItemsByIds(List<String> linkNames) {
		RepeatingView menu = new RepeatingView(MENU_ITEMS_ID);
		for(String linkName : linkNames) {
			ExternalLink link = new ExternalLink(menu.newChildId(), new ResourceModel(name+"."+linkName+".link", "#"));
			link.setBody(new ResourceModel(name+"."+linkName+".caption",linkName));
			menu.add(link);
		}
		
		add(menu);
	}
	
	public void addMenuItems(List<? extends Component> menuItems) {
		RepeatingView menu = new RepeatingView(MENU_ITEMS_ID);
		for(Component c : menuItems) {
			c.setOutputMarkupId(true);
			c.setMarkupId(menu.newChildId());
			menu.add(c);
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
