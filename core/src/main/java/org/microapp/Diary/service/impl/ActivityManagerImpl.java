package org.microapp.Diary.service.impl;

import org.microapp.Diary.generic.dao.GenericDao;
import org.microapp.Diary.generic.service.impl.GenericManagerImpl;
import org.microapp.Diary.model.Activity;
import org.microapp.Diary.service.ActivityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class ActivityManagerImpl extends GenericManagerImpl<Activity, Long> implements ActivityManager{
	
	@Autowired
	public ActivityManagerImpl(@Qualifier("activityDao")GenericDao<Activity, Long> actDao) {
		super(actDao);
	}

}
