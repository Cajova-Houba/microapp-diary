package org.microapp.Diary.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.microapp.Diary.generic.model.BaseAccessObject;

@Entity
public class Plan extends BaseAccessObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date startDate;
	
	private Date endDate;
	
	@OneToMany(fetch=FetchType.EAGER,mappedBy="plan")
	private List<Goal> goals = new ArrayList<Goal>(0);
	
	private String name;
	
	private boolean completed;
	
	
	/**
	 * Marks the plan as completed.
	 */
	public void complete() {
		setCompleted(true);
	}
	
	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Goal> getGoals() {
		return goals;
	}

	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return Plan.class.getSimpleName() + String.format(" [id=%d, personId=%d, name=%s, start=%s, end=%s, goalsCount=%d, completed=%s]",
															getId(),
															getPersonId(),
															getName(),
															getStartDate(),
															getEndDate(),
															getGoals() == null ? null : getGoals().size(),
															isCompleted());
	}

	@Override
	public boolean equals(Object o) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

}
