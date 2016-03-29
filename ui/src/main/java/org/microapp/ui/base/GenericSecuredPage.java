package org.microapp.ui.base;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.microapp.ui.WicketApplication;

/**
 * Generic page accessible only by logged members.
 * Also checks if the logged member can access other member (specified by a personId parameter)
 * @author Zdenda
 *
 */
public class GenericSecuredPage extends GenericPage {

	/**
	 * Id of member whose data is going to be displayed. So if the logged member is couch,
	 * he can specify the id of person to see his diary.
	 */
	protected long personId;
	protected boolean personIdLoaded;
	
	public GenericSecuredPage(PageParameters parameters) {
		super(parameters);
	}
	
	public GenericSecuredPage() {
		super();
	}
	
	/**
	 * Checks if some member is logged. If not, restarts the application at sign in page. 
	 */
	@Override
	protected void authenticate() {
		super.authenticate();
		
		//check if the user is logged and redirect to login page if isn't
		AuthenticatedWebApplication app = (AuthenticatedWebApplication)WicketApplication.get();
		if(!isSignedIn()) {
			logger.debug("No user logged. Redirecting to membership page.");
			app.restartResponseAtSignInPage();
		}
	}
	
	@Override
	protected void loadParameters(PageParameters parameters) {
		super.loadParameters(parameters);
		
		loadPersonId(parameters);
	}
	
	/**
	 * Will try to load the 'personId' parameter. If the parameter exists and is correctly loaded, then
	 * the method will check if the logged member can access member with this id.
	 * 
	 * If the parameter isn't loaded correctly, or the member can't access the other member, then id of logged member is
	 * used as the personId parameter.
	 * @param parameters
	 */
	protected void loadPersonId(PageParameters parameters) {
		personIdLoaded = false;
		Long tmp = loadLongParameter(parameters, "personId", false);
		if (tmp != null) {
			personId = tmp.longValue();
			boolean canAccess = canAccess(getloggedUserId(), personId);
			boolean exists = membernetManager.exists(personId);
			
			if (!exists) {
				logger.warn("Member with id: "+personId+" doesn't exist.");
			} else if(!canAccess) {
				logger.warn("Logged member with id: "+getloggedUserId()+" can't access member with id: "+personId);
			}
			
			personIdLoaded = exists && canAccess;
		} 
		
		if(!personIdLoaded) {
			logger.debug("No personId loaded, use loggedId instead.");
			personId = getloggedUserId();
			personIdLoaded = true;
		}
	}
	
	/**
	 * Decides if the member with 'requesterId' can access member with 'personId'.
	 * @param requesterId Id of requester (typically id of logged member)
	 * @param targetId Id of person to be accessed (typically loaded personId parameter).
	 * @return True if requester can access other member.
	 */
	protected boolean canAccess(long requesterId, long targetId) {
		return membernetManager.canAccess(requesterId, targetId);
	}
	
}
