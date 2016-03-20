package org.microapp.Diary.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.microapp.Diary.generic.model.BaseAccessObject;

/**
 * Basic informations about member.
 * @author Zdenda
 *
 */
@Entity
@Table(name="member_info")
public class MemberInfo extends BaseAccessObject {

	/**
	 * Full name of the member.
	 */
	@Column(
			length=50,
			name="full_name"
			)
	private String fullName;
	
	/**
	 * If the member has the microapplication enabled.
	 */
	@Column(name="diary_enabled")
	private boolean diaryEnabled;
	
	/**
	 * Marks the diary as enabled.
	 */
	public void enableDiary() {
		setDiaryEnabled(true);
	}
	
	public void disableDiary() {
		setDiaryEnabled(false);
	}
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean isDiaryEnabled() {
		return diaryEnabled;
	}

	public void setDiaryEnabled(boolean diaryEnabled) {
		this.diaryEnabled = diaryEnabled;
	}

	@Override
	public String toString() {
		return String.format("%s: [id=%d, fullName=%s, diaryEnabled=%b]", 
				this.getClass().getSimpleName(),
				this.getId(),
				this.getFullName(),
				this.isDiaryEnabled());
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof MemberInfo)) {
			return false;
		}
		
		MemberInfo other = (MemberInfo)o;
		return getId() == other.getId() && getPersonId() == other.getPersonId();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = hash*17 + Objects.hashCode(getId());
		hash = hash*31 + Objects.hashCode(getPersonId());
		return hash;
	}

}
