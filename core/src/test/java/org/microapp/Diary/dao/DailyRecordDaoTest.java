package org.microapp.Diary.dao;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.microapp.Diary.generic.dao.BaseDaoTestCase;
import org.microapp.Diary.model.DailyRecord;
import org.springframework.beans.factory.annotation.Autowired;

public class DailyRecordDaoTest extends BaseDaoTestCase {

	@Autowired
	private DailyRecordDao dailyRecordDao;
	
	@Test
	public void testGetAll() {
		//there are daily records
		List<DailyRecord> drs = dailyRecordDao.getAll();
		
		assertFalse(drs.isEmpty());
	}
	
	@Test
	public void testGetDailyRecordsFrom() {
		//there are daily records for member with id from 2016-1-1
		long memberID = 1l;
		Date date = Date.valueOf("2016-1-1");
		List<DailyRecord> drs = dailyRecordDao.getDailyRecordsFrom(date, memberID);
		
		assertFalse(drs.isEmpty());
	}
	
	@Test
	public void testGetDailyRecordsAfterBefore() {
		//there are daily records for member with id after 2016-1-1 and before 2017-1-1
		long memberID = 1l;
		Date after = Date.valueOf("2016-1-1");
		Date before = Date.valueOf("2017-1-1");
		List<DailyRecord> drs = dailyRecordDao.getDailyRecords(memberID, after, before);
		
		assertFalse(drs.isEmpty());
	}
	
	@Test
	public void testGetTodayDailyRecord() {
		long memberID = 1l;
		DailyRecord dr = dailyRecordDao.getTodayDailyRecord(memberID);
		Date today = new Date(Calendar.getInstance().getTime().getTime());
		
		assertNotNull(dr);
		assertEquals(today.toString(), dr.getDate().toString());
		
		//now there is a daily record for today, so the method should return the same daily record on next call
		DailyRecord nextDr = dailyRecordDao.getTodayDailyRecord(memberID);
		assertEquals(dr.getId(), nextDr.getId());
	}
	
	@Test
	public void testGetDailyRecordFrom() {
		//there are daily record for member with id 1  from 2016-1-1
		long memberID = 1l;
		Date date = Date.valueOf("2016-1-1");
		DailyRecord dr = dailyRecordDao.getDailyRecordFrom(date, memberID);
		
		assertNotNull(dr);
	}
}
