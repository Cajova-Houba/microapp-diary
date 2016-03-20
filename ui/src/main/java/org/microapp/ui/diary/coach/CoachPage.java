package org.microapp.ui.diary.coach;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.xml.JRPenFactory.Bottom;

import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.microapp.membernet.MembernetManager;
import org.microapp.ui.HomePage;
import org.microapp.ui.WicketApplication;
import org.microapp.ui.base.GenericPage;
import org.microapp.ui.base.genericTable.ButtonColumn;
import org.microapp.ui.base.genericTable.ComponentColumn;
import org.microapp.ui.base.genericTable.GenericTable;

import com.yoso.dev.membernet.membership.domain.Membership;
import com.yoso.dev.membernet.society.domain.Society;

public class CoachPage extends GenericPage{
	
	private final String HEADER_ID = "socName";
	private final String TABLE_ID = "membersTable";
	
	@SpringBean
	private MembernetManager membernetManager;
	
	private Society society;
	
	
	@Override
	public void authenticate() {
		super.authenticate();
		
		//check if the logged member is coach and get the society in which he is
		AuthenticatedWebApplication app = (AuthenticatedWebApplication)WicketApplication.get();
		if(!isSignedIn()) {
			logger.debug("No user logged. Redirecting to membership page.");
			app.restartResponseAtSignInPage();
		} else {
			long loggedId = getloggedUserId();
			
			//check if exists
			if(!membernetManager.exists(loggedId)){
				logger.debug("User doesn't exist. Redirecting to membership page.");
				app.restartResponseAtSignInPage();
				
				//check if is admin
			} else if (membernetManager.getMembership(loggedId).isSocietyAdmin()) {
				
				//load society
				this.society = membernetManager.getMembership(loggedId).getUpper();
				if (society == null) {
					logger.debug("No society");
				}
			} else {
				logger.debug("User is not admin of society. Redirecting home");
				setResponsePage(HomePage.class);
			}
		}
	}
	
	@Override
	public void addComponents() {
		super.addComponents();
		addHeader();
		addMembersTable();
	}
	
	private void addHeader() {
		add(new Label(HEADER_ID, society == null ? "Society name" : society.getName()));
	}
	
	
	private void addMembersTable() {
		if (society == null) {
			add(new Label(TABLE_ID,""));
		} else {
			List<String> fieldNames = Arrays.asList(new String[] {"id", "fullName"});
			List<Membership> values = membernetManager.listAll(society.getSocietyId());
			
			MembersTable membersTable = new MembersTable(TABLE_ID, Membership.class, values, fieldNames, null);
			add(membersTable);
		}
	}
	
	public class MembersTable extends GenericTable {

		public MembersTable(String id, Class<? extends Serializable> clazz,
				List<? extends Serializable> values,
				List<String> fieldNames, Map<String, String> headers) {
			super(id, clazz, values, fieldNames, headers);
		}
		
		@Override
		protected List<ComponentColumn> getComponentColumns() {
			List<ComponentColumn> columns =  super.getComponentColumns();
			
			// link to members plans
			ButtonColumn plans = new ButtonColumn("id", "plans");
			columns.add(plans);
			
			//link to members daily records
			ButtonColumn dailyActs = new ButtonColumn("id", "dailyAct");
			columns.add(dailyActs);
			
			//link to report page - todo
			ButtonColumn reports = new ButtonColumn("id", "report");
			columns.add(reports);
			
			
			return columns;
		}
		
	}

}
