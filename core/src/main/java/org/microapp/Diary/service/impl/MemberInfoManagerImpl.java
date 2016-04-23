package org.microapp.Diary.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.microapp.Diary.generic.dao.GenericAccessDao;
import org.microapp.Diary.generic.service.impl.GenericAccessManagerImpl;
import org.microapp.Diary.model.MemberInfo;
import org.microapp.Diary.service.MemberInfoManager;
import org.microapp.membernet.vo.MembershipVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public class MemberInfoManagerImpl extends GenericAccessManagerImpl<MemberInfo, Long>
		implements MemberInfoManager {

	private GenericAccessDao<MemberInfo, Long> miDao;
	
	@Autowired
	public MemberInfoManagerImpl(@Qualifier("memberInfoDao") GenericAccessDao<MemberInfo, Long> miDao) {
		super(miDao);
		this.miDao = miDao;
	}

	@Transactional (
    		value = "transactionManagerDiary",
    		propagation = Propagation.REQUIRES_NEW
    		)
	public MemberInfo enableDiary(long personId) {
		if (exists(personId)) {
			MemberInfo mi = get(personId);
			mi.enableDiary();
			return save(mi);
		} else {
			return null;
		}
	}

	@Transactional (
    		value = "transactionManagerDiary",
    		propagation = Propagation.REQUIRES_NEW
    		)
	public MemberInfo disableDiary(long personId) {
		if (exists(personId)) {
			MemberInfo mi = get(personId);
			mi.disableDiary();
			return save(mi);
		} else {
			return null;
		}
	}

	@Override
	public MemberInfo getMemberInfo(MembershipVO membership) {

		long personId = membership.getId();

		List<MemberInfo> mi = getAllForPerson(personId);
		if (!mi.isEmpty()) {
			return mi.get(0);
		} 
		
		MemberInfo mInfo = new MemberInfo(membership);
		return save(mInfo);
	}

	@Override
	public List<MemberInfo> getMemberInfos(List<MembershipVO> memberships) {
		
		List<MemberInfo> infos = new ArrayList<MemberInfo>(memberships.size());
		for(MembershipVO m : memberships) {
			infos.add(getMemberInfo(m));
		}
		
		return infos;
	}
	
	

}
