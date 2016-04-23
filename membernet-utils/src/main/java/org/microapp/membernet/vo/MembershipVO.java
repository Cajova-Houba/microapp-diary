package org.microapp.membernet.vo;

import java.io.Serializable;

/**
 * Simplified version of MembershipVO class from membernet.
 * @author Zdenda
 *
 */
public class MembershipVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private boolean isSocietyAdmin;
	private SocietyVO society;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isIsSocietyAdmin() {
		return isSocietyAdmin;
	}
	public void setIsSocietyAdmin(boolean isSocietyAdmin) {
		this.isSocietyAdmin = isSocietyAdmin;
	}
	public SocietyVO getSociety() {
		return society;
	}
	public void setSociety(SocietyVO society) {
		this.society = society;
	}
	
	@Override
	public String toString() {
		return "MembershipVO [id=" + id + ", name=" + name
				+ ", isSocietyAdmin=" + isSocietyAdmin + ", society=" + society
				+ "]";
	}
	
	
}
