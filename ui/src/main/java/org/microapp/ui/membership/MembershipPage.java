package org.microapp.ui.membership;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.types.resources.StringResource;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.microapp.membernet.MembernetManager;
import org.microapp.ui.HomePage;
import org.microapp.ui.base.GenericPage;
import org.microapp.ui.base.genericTable.ButtonColumn;
import org.microapp.ui.base.genericTable.ComponentColumn;
import org.microapp.ui.base.genericTable.CustomButton;
import org.microapp.ui.base.genericTable.GenericTable;

import com.yoso.dev.membernet.member.domain.Member;
import com.yoso.dev.membernet.membership.domain.Membership;
import com.yoso.dev.membernet.society.domain.Society;


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
		
		List<String> fieldNames = Arrays.asList(new String[] {"id", "lower.id", 
								"upper.societyId", "lower.firstName", "lower.lastName",
								"upper.name","societyAdmin","id"});
		
		MembershipTable table = new MembershipTable(tableID, Membership.class, membernetManager.listAll(), fieldNames, null);
		
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
							logger.debug("Log as member with id="+id);
							
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
