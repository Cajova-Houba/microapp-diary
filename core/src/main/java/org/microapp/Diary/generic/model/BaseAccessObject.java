package org.microapp.Diary.generic.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Use this class for entities which are bound to a member - entities which should have some sort of "member_id" field.
 * 
 * Right now, the personId field reassembles the ID column in the membership table. I will add that connection into 
 * this generic module later.
 * 
 * @author Zdenda
 *
 */
@MappedSuperclass
public abstract class BaseAccessObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Name for queries in generic access DAO.
	 */
	public static final String ACCESS_ID_COLUMN_NAME = "personId";
	
	@Column(name="person_id")
	protected long personId;
	
	
	
	public long getPersonId() {
		return personId;
	}

	public void setPersonId(long personId) {
		this.personId = personId;
	}

	
}
