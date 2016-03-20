package org.microapp.Diary.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.microapp.Diary.generic.model.BaseAccessObject;

@Entity
public class DailyRecord extends BaseAccessObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date date;

	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "dailyRecord")
	private List<Activity> activities = new ArrayList<Activity>(0);
	
	/**
	 * Set date to today.
	 */
	public DailyRecord() {
		this.setDate(new Date(Calendar.getInstance().getTime().getTime()));
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return DailyRecord.class.getSimpleName()+String.format(" [id=%d, personId=%d, date=%s, activitiesCount=%d]",
																	getId(),
																	getPersonId(),
																	getDate() == null ? null : getDate().toString(),
																	getActivities() == null ? null : getActivities().size());
	}

	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		
		if (o instanceof DailyRecord) {
			DailyRecord other = (DailyRecord)o;
			
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
