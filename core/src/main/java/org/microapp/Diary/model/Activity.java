package org.microapp.Diary.model;

import javassist.expr.Instanceof;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Activity extends BaseActivityObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="dailyRecordId")
	private DailyRecord dailyRecord;
	
	public DailyRecord getDailyRecord() {
		return dailyRecord;
	}

	public void setDailyRecord(DailyRecord dailyRecord) {
		this.dailyRecord = dailyRecord;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Activity.class.getSimpleName()+String.format(" [id=%d, type=%s, value=%f, unit=%s, dailyRecordId=%d]",
																getId(),
																getActivityType() == null ? null : getActivityType().name(),
																getValue(),
																getActivityUnit() == null ? null : getActivityUnit().name(),
																getDailyRecord() == null ? null : getDailyRecord().getId());
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		
		if (o instanceof Activity) {
			Activity other = (Activity)o;
			
			return getId() == other.getId();
			
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
