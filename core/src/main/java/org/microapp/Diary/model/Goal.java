package org.microapp.Diary.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Goal extends BaseActivityObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="planId")
	private Plan plan;
	
	private boolean completed;
	
	private double progress;
	
	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	@Override
	public String toString() {
		return Goal.class.getSimpleName()+String.format(" [id=%d, type=%s, value=%f, unit=%s, progress=%f, planId=%d]",
															getId(),
															getActivityType() == null ? null : getActivityType().name(),
															getValue(),
															getActivityUnit() == null ? null : getActivityUnit().name(),
															getProgress(),
															getPlan() == null ? null : getPlan().getId());
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		
		if (o instanceof Goal) {
			Goal other = (Goal)o;
			
			return getId() == other.getId();
			
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return 0;
	}

}
