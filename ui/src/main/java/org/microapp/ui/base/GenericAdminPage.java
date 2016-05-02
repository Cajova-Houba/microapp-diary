package org.microapp.ui.base;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.microapp.ui.HomePage;

/**
 * Base page accessible only to administrators.
 * @author Zdenda
 *
 */
public class GenericAdminPage extends GenericSecuredPage {

	public GenericAdminPage() {
		super();
	}

	public GenericAdminPage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected void authenticate() {
		super.authenticate();
		if(!isCoach()) {
			logDebug("Access denied for user id="+getloggedUserId()+". Redirecting home.");
			setResponsePage(HomePage.class);
		}
	}
	
}
