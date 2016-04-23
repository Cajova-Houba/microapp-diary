package org.microapp.membernet.vo;

import java.io.Serializable;

/**
 * Simplified version of SocietyVO class from membernet.
 * @author Zdenda
 *
 */
public class SocietyVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	
	
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
	
	@Override
	public String toString() {
		return "SocietyVO [id=" + id + ", name=" + name + "]";
	}
	
	
}
