package org.microapp.ui.base;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.microapp.membernet.MembernetManager;
import org.microapp.membernet.vo.MembershipVO;
import org.microapp.ui.HomePage;
import org.microapp.ui.base.menu.GenericMenu;
import org.microapp.ui.diary.activity.DailyActivityPage;
import org.microapp.ui.diary.coach.CoachPage;
import org.microapp.ui.diary.plans.PlansPage;
import org.microapp.ui.membership.MembershipPage;
import org.microapp.ui.security.DiarySession;


/**
 * Generic page. To change stuff, override init() method, use setters and do your own inicialization.
 * To add components, override addComponents() methods (with super call).
 * 
 * Override constructor just in case you need to use page Parameters. If you use constructor with page parameters
 * you need to override init() and addComponents() methods with page parameters too.
 * 
 * To set the title and header of page, you can either use setTitleModel(), setHeaderModel() methods or just add property file for your page
 * with {@code title} and {@code header} keys.
 * 
 * @author Zdenda
 *
 */
public class GenericPage extends WebPage {

	private static final String CSS_FILE_NAME = "GenericPage.css";
	
	private final String NO_ERROR = "No errors.";
	private final String DEF_TITLE = "Wicket - Title";
	private final String DEF_HEADER = "Header";
	private final Class<?> DEF_BACK_LINK = HomePage.class;
	
	private final String TITLE_ID = "title";
	private final String HEADER_ID = "header";
	private final String ERROR_MSG_ID = "errorMsg";
	private final String MAIN_MENU_ID = "mainMenu";
	private final String LOGGED_NAME_ID = "loggedName";
	private final String LOGGED_NAME_LABEL_ID = "loggedName.label";
	
	protected final transient Logger logger = LogManager.getLogger(getClass());
	
	protected StringBuilder errorMsgB;
	
	protected IModel<String> titleModel;
	protected IModel<String> headerModel;
	
	protected String title;
	
	protected String header;
	
	/**
	 * Logged member. Null if no member is logged in.
	 */
	protected MembershipVO logged;
	
	/**
	 * By default used as home page button in main menu.
	 */
	protected Class<?> backLink;
	
	@SpringBean(name = "membernetManager")
	protected MembernetManager membernetManager;
	
	public GenericPage() {
		errorMsgB = new StringBuilder();
		
		authenticate();
		
		//assign values to fields
		inic();
		
		//actually add components
		addComponents();
		
		//add error message label
		showErrorMsg();
	}
	
	public GenericPage(PageParameters parameters) {
		// TODO Auto-generated constructor stub
		errorMsgB = new StringBuilder();
		
		authenticate();
		
		//load parameters
		loadParameters(parameters);
		
		//assign values to fields
		inic(parameters);
		
		//actually add components
		addComponents(parameters);
		
		//add error message label
		showErrorMsg(parameters);
	}
	
	/**
	 * Check if there is a usser logged in current session and checks if he's a couch.
	 */
	protected void authenticate() {
		DiarySession session = getAuthSession();
		if (session.isSignedIn()) {
			logged = membernetManager.getMembership(session.getLoggedMemberId());
		} else {
			logged = null;
		}
	}
	
	/**
	 * Basic initialization is being done here. Override this method to set the title and header or back link.
	 * You can also do your authentication here.
	 */
	protected void inic() {
		this.inic(null);
	}
	
	/**
	 * Basic initialization is being done here. Override this method to set the title and header or back link.
	 * You can also do your authentication here.
	 * @param parameters Page parameters to be used.
	 */
	protected void inic(PageParameters parameters) {
		setBackLink(DEF_BACK_LINK);
		setTitle(new ResourceModel("title", DEF_TITLE));
		setHeader(new ResourceModel("header", DEF_HEADER));
	}
	
	protected void addComponents() {
		this.addComponents(null);
	}
	
	protected void addComponents(PageParameters parameters) {
		add(new Label(TITLE_ID,getTitleModel()));
		add(new Label(HEADER_ID,getHeaderModel()));
		addMainMenu(MAIN_MENU_ID);
		addLoggedName();
	}
	
	private void addLoggedName() {
		if(getLoggedName().length() == 0) {
			add(new Label(LOGGED_NAME_LABEL_ID, ""));
			add(new Label(LOGGED_NAME_ID, ""));
		} else {
			add(new Label(LOGGED_NAME_LABEL_ID, new ResourceModel(LOGGED_NAME_LABEL_ID, LOGGED_NAME_LABEL_ID)));
			add(new Label(LOGGED_NAME_ID, getLoggedName()));
		}
	}
	
	private void addMainMenu(String mainMenuId) {
		
		List<String> linkNames = new ArrayList<String>();
		linkNames.add("home");
		linkNames.add("login");
		if(isSignedIn()) {
			linkNames.add("dailyRecords");
			linkNames.add("plans");
		}
		if(isCoach()) {
			linkNames.add("coach");
		}
		
		GenericMenu mainMenu = new GenericMenu(MAIN_MENU_ID, linkNames);
		add(mainMenu);
	}
	
	
	/**
	 * Override this method to load your parameters.
	 * @param parameters
	 */
	protected void loadParameters(PageParameters parameters) {
		
	}
	
	/**
	 * Method will try to load a long parameter from page parameters.
	 * If the parameter is successfully loaded, then it's returned. 
	 * Otherwise null is returned and error is added using addError() method. 
	 * @param parameters Page parameters.
	 * @param paramName Name of the parameter.
	 * @param required Set to true if you want to also display error if the parameter is not found.
	 * @return Long value if parameter is successfully loaded, otherwise null.
	 */
	protected Long loadLongParameter(PageParameters parameters, String paramName, boolean required) {
		Long paramValue = null;
		
		if(!parameters.get(paramName).isNull()) {
			try {
				paramValue = Long.parseLong(parameters.get(paramName).toString());
				logger.debug("Parameter '"+paramName+"' loaded with value: "+paramValue);
			} catch (Exception e) {
				String msg = "Error when parsing parameter '"+paramName+"'. "+e.toString();
				addError(msg);
				logger.warn(msg);
			}
			
		} else if(required) {
			String msg = "Parameter '"+paramName+"' not found.";
			addError(msg);
			logger.warn(msg);
		} else {
			logger.debug("Parameter '"+paramName+"' is null.");
		}
		
		
		return paramValue;
		
	}
	
	/**
	 * Method will try to load a string parameter from page parameters.
	 * If the parameter is successfully loaded, then it's returned. 
	 * Otherwise null is returned and error is added using addError() method. 
	 * @param parameters Page parameters.
	 * @param paramName Name of the parameter.
	 * @param required Set to true if you want to also display error if the parameter is not found.
	 * @return String representing the parameter value if parameter is successfully loaded, otherwise null.
	 */
	protected String loadStringParameter(PageParameters parameters, String paramName, boolean required) {
		String paramValue = null;
		
		if(!parameters.get(paramName).isNull()) {
			paramValue = parameters.get(paramName).toString();
			
		} else if(required) {
			String msg = "Parameter '"+paramName+"' not found.";
			addError(msg);
			logger.warn(msg);
		}
		
		return paramValue;
	}
	
	/**
	 * Method will try to load a date parameter from page parameters.
	 * If the parameter is successfully loaded, then it's returned. 
	 * Otherwise null is returned and error is added using addError() method. 
	 * @param parameters Page parameters.
	 * @param paramName Name of the parameter.
	 * @param required Set to true if you want to also display error if the parameter is not found.
	 * @return Date (sql one) if parameter is successfully loaded, otherwise null.
	 */
	protected Date loadDateParameter(PageParameters parameters, String paramName, boolean required) {
		Date paramValue = null;
		
		if(!parameters.get(paramName).isNull()) {
			try {
				paramValue = Date.valueOf(parameters.get(paramName).toString());
			} catch (Exception e) {
				String msg = "Error when parsing parameter '"+paramName+"'. "+e.toString();
				addError(msg);
				logger.warn(msg);
			}
		} else if (required) {
			String msg = "Parameter '"+paramName+"' not found.";
			addError(msg);
			logger.warn(msg);
		}
		
		return paramValue;
	}

	/**
	 * Method will try to load a boolean parameter from page parameters.
	 * If the parameter is successfully loaded, then it's returned. 
	 * Otherwise null is returned and error is added using addError() method. 
	 * @param parameters Page parameters.
	 * @param paramName Name of the parameter.
	 * @param required Set to true if you want to also display error if the parameter is not found.
	 * @return Boolean value of parameter, or null if error occurs.
	 */
	protected Boolean loadBooleanParamaeter(PageParameters parameters, String paramName, boolean required) {
		Boolean paramValue = null;
		
		if(!parameters.get(paramName).isNull()) {
			try {
				paramValue = Boolean.parseBoolean(parameters.get(paramName).toString());
			} catch (Exception e) {
				String msg = "Error when parsing parameter '"+paramName+"'. "+e.toString();
				addError(msg);
				logger.warn(msg);
			}
		} else if (required) {
			String msg = "Parameter '"+paramName+"' not found.";
			addError(msg);
			logger.warn(msg);
		}
		
		return paramValue;
	}
	
	public void showErrorMsg() {
		this.showErrorMsg(null);
	}
	
	public void showErrorMsg(PageParameters parameters) {
		add(new Label(ERROR_MSG_ID,getErrorMsg()));
	}
	
	public String getTitle() {
		return titleModel.getObject();
	}

	public void setTitle(String title) {
		this.title = title;
		this.titleModel = Model.of(title);
	}

	public void setTitle(IModel<String> titleModel) {
		this.titleModel = titleModel;
	}
	
	public String getHeader() {
		return headerModel.getObject();
	}

	public void setHeader(String header) {
		this.header = header;
		this.headerModel = Model.of(header);
	}
	
	
	public void setHeader(IModel<String> headerModel) {
		this.headerModel = headerModel;
	}
	public String getLoggedName() {
		return logged == null ? "" : logged.getName();
	}

	public IModel<String> getTitleModel() {
		return titleModel;
	}
	
	public IModel<String> getHeaderModel() {
		return headerModel;
	}
	
	public Class getBackLink() {
		return backLink;
	}

	public void setBackLink(Class<?> backLink) {
		this.backLink = backLink;
	}
	
	public String getErrorMsg() {
		return errorMsgB.toString();
	}
	
	public void addError(String error) {
		errorMsgB.append(error+"\n");
	}

	/**
	 * returns the authenticated web session.
	 * @return Active DiarySession.
	 */
	public DiarySession getAuthSession() {
		return (DiarySession)AuthenticatedWebSession.get();
	}
	
	/**
	 * Returns true if anybody is signed in current session.
	 * @return
	 */
	public boolean isSignedIn() {
		return logged != null;
	}

	public Long getloggedUserId() {
		return logged == null ? null : logged.getId();
	}

	public boolean isCoach() {
		return logged == null ? false : logged.isIsSocietyAdmin();
	}
	
	
	
}
