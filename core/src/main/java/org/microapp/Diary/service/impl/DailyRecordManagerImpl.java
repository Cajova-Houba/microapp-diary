package org.microapp.Diary.service.impl;

import java.sql.Date;
import java.util.List;

import org.microapp.Diary.dao.DailyRecordDao;
import org.microapp.Diary.generic.service.impl.GenericAccessManagerImpl;
import org.microapp.Diary.model.DailyRecord;
import org.microapp.Diary.service.DailyRecordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class DailyRecordManagerImpl extends GenericAccessManagerImpl<DailyRecord, Long>
		implements DailyRecordManager {

	private DailyRecordDao dailyRecordDao;

	@Autowired
	public DailyRecordManagerImpl(DailyRecordDao drDao) {
		super(drDao);
		this.dailyRecordDao = drDao;
	}

	public List<DailyRecord> getDailyRecordsFrom(Date date, Long memberID) {
		return dailyRecordDao.getDailyRecordsFrom(date, memberID);
	}

	public List<DailyRecord> getDailyRecords(Long memberID, Date after, Date before) {
		return dailyRecordDao.getDailyRecords(memberID, after, before);
	}

	@Override
	public List<DailyRecord> getNonEmptyDailyRecords(Long memberID, Date after,
			Date before) {
		return dailyRecordDao.getNonEmptyDailyRecords(memberID, after, before);
	}

	@Transactional (
    		value = "transactionManagerDiary",
    		propagation = Propagation.REQUIRES_NEW
    		)
	public DailyRecord getDailyRecordFrom(Date date, Long memberID) {
		return dailyRecordDao.getDailyRecordFrom(date, memberID);
	}

	@Transactional (
    		value = "transactionManagerDiary",
    		propagation = Propagation.REQUIRES_NEW
    		)
	public DailyRecord getTodayDailyRecord(Long memberID) {
		return dailyRecordDao.getTodayDailyRecord(memberID);
	}

}
