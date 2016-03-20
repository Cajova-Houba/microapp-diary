package org.microapp.Diary.generic.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Base class for Model objects. Child objects should implement toString(),
 * equals() and hashCode().
 * 
 * Already contains the ID field, so it't not necessary to declare a new one in child classes.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@MappedSuperclass
public abstract class BaseObject implements Serializable {    

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
    /**
     * Returns a multi-line String with key=value pairs.
     * @return a String representation of this class.
     */
    public abstract String toString();

    /**
     * Compares object equality. When using Hibernate, the primary key should
     * not be a part of this comparison.
     * @param o object to compare to
     * @return true/false based on equality tests
     */
    public abstract boolean equals(Object o);

    /**
     * When you override equals, you should override hashCode. See "Why are
     * equals() and hashCode() importation" for more information:
     * http://www.hibernate.org/109.html
     * @return hashCode
     */
    public abstract int hashCode();
}

