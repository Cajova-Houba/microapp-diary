package org.microapp.Diary.service.impl;

import java.util.List;

import org.microapp.Diary.generic.dao.GenericAccessDao;
import org.microapp.Diary.generic.service.impl.GenericAccessManagerImpl;
import org.microapp.Diary.model.MemberInfo;
import org.microapp.Diary.service.MemberInfoManager;
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

	@Override
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

	@Override
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

}
