package org.microapp.Diary.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import org.microapp.Diary.generic.model.BaseObject;
import org.microapp.Diary.model.enums.ActivityType;
import org.microapp.Diary.model.enums.ActivityUnit;

/**
 * This class serves as base class for all activity-related classes (Activity, Goal..).
 * 
 * @author Zdenda
 *
 */
@MappedSuperclass
public class BaseActivityObject extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Enumerated(EnumType.STRING)
	protected ActivityType activityType;
	
	@Enumerated(EnumType.STRING)
	protected ActivityUnit activityUnit;
	
	protected double value;
	
	protected String name;
	
	/**
	 * Default constructor.
	 */
	public BaseActivityObject() {
		this.activityType = ActivityType.unknown;
		this.activityUnit = ActivityUnit.NULL;
		this.value = 0;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityType activitytype) {
		this.activityType = activitytype;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public ActivityUnit getActivityUnit() {
		return activityUnit;
	}

	public void setActivityUnit(ActivityUnit activityUnit) {
		this.activityUnit = activityUnit;
	}
	
	/**
	 * Returns the value converted to default unit.
	 * That means the {@code value*activityUnit.ratio} will be returned.
	 * @return Value converted to default unit.
	 */
	public double valueInDefaultUnit() {
		if (getActivityUnit() == null || getActivityUnit() == ActivityUnit.NULL) {
			return getValue();
		} else {
			return getValue() * getActivityUnit().getRatio();
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

}
