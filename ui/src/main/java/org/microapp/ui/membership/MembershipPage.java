package org.microapp.ui.membership;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.microapp.membernet.vo.MembershipVO;
import org.microapp.ui.HomePage;
import org.microapp.ui.base.GenericPage;
import org.microapp.ui.base.genericTable.ButtonColumn;
import org.microapp.ui.base.genericTable.ComponentColumn;
import org.microapp.ui.base.genericTable.CustomButton;
import org.microapp.ui.base.genericTable.GenericTable;


public class MembershipPage extends GenericPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public void addComponents() {
		super.addComponents();
		addMembershipTable("membershipTable");
		
	}
	
	private void addMembershipTable(String tableID) {
		
		List<String> fieldNames = Arrays.asList(new String[]{"id","name","society.id","society.name","isSocietyAdmin"});
		
		List<MembershipVO> values = new ArrayList<MembershipVO>();
		for(Integer societyId : Arrays.asList(-3,-2,-1,1,2,3)) {
			values.addAll(membernetManager.listAll(societyId));
		}
		
		MembershipTable table = new MembershipTable(tableID, MembershipVO.class, values, fieldNames, null);
		
		add(table);
	}
	
	class MembershipTable extends GenericTable {
		
		
		private static final long serialVersionUID = 1L;
		
		public MembershipTable(String id, Class<? extends Serializable> clazz,
				List<? extends Serializable> values, List<String> fieldNames,
				Map<String, String> headers) {
			super(id, clazz, values, fieldNames, headers);
		}
		
		@Override
		protected List<ComponentColumn> getComponentColumns() {
			List<ComponentColumn> columns = super.getComponentColumns();
			
			//link to diary application + authentication
			ButtonColumn diaryColumn = new ButtonColumn("id", "diaryColumn") {
				@Override
				public AbstractLink getLink( final IModel<Object> rowModel) {
					
					return new Link(CustomButton.LINK_ID) {
						
						@Override
						public void onClick() {
							String id = getDataModel(rowModel).getObject().toString();
							logDebug("Log as member with id="+id);
							
							boolean authRes = AuthenticatedWebSession.get().signIn(id, null);
							
							
							
							if(authRes) {
								setResponsePage(HomePage.class);
							}
						}
					};
				}
			};
			columns.add(diaryColumn);
			
			return columns;
		}
	}
}

