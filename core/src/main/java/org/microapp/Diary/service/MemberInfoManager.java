package org.microapp.Diary.service;

import org.microapp.Diary.generic.service.GenericAccessManager;
import org.microapp.Diary.model.MemberInfo;

public interface MemberInfoManager extends GenericAccessManager<MemberInfo, Long>{
	
	/**
	 * Enables the diary for specified person.
	 * @param personId Id of person which diary will be enabled.
	 * @return Updated member info. If the memberinfo object doesn't exist yet, null will be returned.
	 */
	public MemberInfo enableDiary(long personId);
	
	/**
	 * Disables the diary for specified person.
	 * @param personId Id of person which diary will be disabled.
	 * @return Updated member info. If the memberinfo object doesn't exist yet, null will be returned.
	 */
	public MemberInfo disableDiary(long personId);

}
