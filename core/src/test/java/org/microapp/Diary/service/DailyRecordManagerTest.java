package org.microapp.Diary.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.microapp.Diary.generic.service.BaseManagerTestCase;
import org.microapp.Diary.model.DailyRecord;
import org.springframework.beans.factory.annotation.Autowired;

public class DailyRecordManagerTest extends BaseManagerTestCase {

	@Autowired
	private DailyRecordManager dailyRecordManager;
	
	
	@Test
	public void testGetAll() {
		//there are daily records
		List<DailyRecord> drs = dailyRecordManager.getAll();
		
		assertFalse(drs.isEmpty());
	}
	
	@Test
	public void testGetDailyRecordsFrom() {
		//there are daily records for member with id from 2016-1-1
		long memberID = 1l;
		Date date = Date.valueOf("2016-1-1");
		List<DailyRecord> drs = dailyRecordManager.getDailyRecordsFrom(date, memberID);
		
		assertFalse(drs.isEmpty());
	}
	
	@Test
	public void testGetTodayDailyRecord() {
		long memberID = 1l;
		DailyRecord dr = dailyRecordManager.getTodayDailyRecord(memberID);
		Date today = new Date(Calendar.getInstance().getTime().getTime());
		
		assertNotNull(dr);
		assertEquals(today.toString(), dr.getDate().toString());
	}
	
	@Test
	public void testGetDailyRecordFrom() {
		//there are daily record for member with id 1  from 2016-1-1
		long memberID = 1l;
		Date date = Date.valueOf("2016-1-1");
		DailyRecord dr = dailyRecordManager.getDailyRecordFrom(date, memberID);
		
		assertNotNull(dr);
	}
}
