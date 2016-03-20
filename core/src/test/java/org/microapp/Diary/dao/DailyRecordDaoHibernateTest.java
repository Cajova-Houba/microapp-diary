package org.microapp.Diary.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.microapp.Diary.generic.dao.BaseDaoTestCase;
import org.microapp.Diary.model.DailyRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class DailyRecordDaoHibernateTest extends BaseDaoTestCase {

//	@Autowired
//	@Qualifier("dailyRecordDaoH")
//	DailyRecordDao dailyRecordDao;
//	
//	@Test
//	public void testGetAll() {
//		List<DailyRecord> drs = dailyRecordDao.getAll();
//		
//		assertFalse(drs.isEmpty());
//	}
	
	
	@Test
	public void ok() {
		assertTrue(true);
	}
}
