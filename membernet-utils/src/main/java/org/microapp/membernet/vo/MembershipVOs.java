package org.microapp.membernet.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wrapper object for REST service returning society members.
 * @author Zdenda
 *
 */
public class MembershipVOs implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int totalCount;
	private boolean empty;
	private List<MembershipVO> items;
	
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public boolean isEmpty() {
		return empty;
	}
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
	public List<MembershipVO> getItems() {
		return items;
	}
	public void setItems(List<MembershipVO> items) {
		this.items = items;
	}
	
	@Override
	public String toString() {
		return "MembershipVOs [totalCount=" + totalCount + ", empty=" + empty
				+ ", items=" + items + "]";
	}
	
	
}
