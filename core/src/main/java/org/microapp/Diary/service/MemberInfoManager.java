package org.microapp.Diary.service;

import java.util.List;

import org.microapp.Diary.generic.service.GenericAccessManager;
import org.microapp.Diary.model.MemberInfo;

import com.yoso.dev.membernet.membership.domain.Membership;

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
	
	/**
	 * Creates a member info object from membership and returns it. If the
	 * member info object already exists for the membership it will be returned.
	 * @param membership Membership.
	 * @return Member info object for membership.
	 */
	public MemberInfo getMemberInfo(Membership membership);
	
	/**
	 * Creates a list of member info objects from memberships and returns it. If the
	 * member info object already exists for the membership it will be returned.
	 * @param memberships Memberships.
	 * @return List of member info objects for memberships.
	 */
	public List<MemberInfo> getMemberInfos(List<Membership> memberships);

}
