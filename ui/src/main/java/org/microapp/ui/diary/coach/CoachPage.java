package org.microapp.ui.diary.coach;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.microapp.membernet.vo.MembershipVO;
import org.microapp.membernet.vo.SocietyVO;
import org.microapp.ui.base.GenericAdminPage;
import org.microapp.ui.base.genericTable.ButtonColumn;
import org.microapp.ui.base.genericTable.ComponentColumn;
import org.microapp.ui.base.genericTable.GenericTable;
import org.microapp.ui.diary.coach.plans.MultiplePlansPage;
import org.microapp.ui.diary.coach.report.CompleteReportPage;

public class CoachPage extends GenericAdminPage{
	private static final long serialVersionUID = 1L;
	private final String HEADER_ID = "socName";
	private final String TABLE_ID = "membersTable";
	private final String COMPLETE_REPORT_ID = "complReportButton";
	private final String MULTIPLE_PLANS_ID = "multiplePlansButton";
	
	private SocietyVO society;
	
	
	@Override
	protected void authenticate() {
		super.authenticate();
		
		//admin status is already verified in GenericAdminPage
		society = logged.getSociety();
		logger.debug("User is admin of society with id: "+society.getId());
	}
	
	@Override
	public void addComponents() {
		super.addComponents();
		addHeader();
		addMembersTable();
		addCompleteReportButton();
		addMultiplePLansButton();
	}
	
	private void addHeader() {
		add(new Label(HEADER_ID, society == null ? "Society name" : society.getName()));
	}
	
	
	private void addMembersTable() {
		if (society == null) {
			add(new Label(TABLE_ID,""));
		} else {
			List<String> fieldNames = Arrays.asList(new String[] {"id", "name"});
			List<MembershipVO> values = membernetManager.listAll(society.getId());
			
			MembersTable membersTable = new MembersTable(TABLE_ID, MembershipVO.class, values, fieldNames, null);
			add(membersTable);
		}
	}
	
	private void addCompleteReportButton() {
		Link l = new Link(COMPLETE_REPORT_ID) {
			@Override
			public void onClick() {
				setResponsePage(CompleteReportPage.class);
			}
		};
		add(l);
	}
	
	private void addMultiplePLansButton() {
		Link l = new Link(MULTIPLE_PLANS_ID) {
			@Override
			public void onClick() {
				setResponsePage(MultiplePlansPage.class);
			}
		};
		
		add(l);
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
