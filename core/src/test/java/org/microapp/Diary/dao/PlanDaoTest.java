package org.microapp.Diary.dao;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.List;

import org.junit.Test;
import org.microapp.Diary.generic.dao.BaseDaoTestCase;
import org.microapp.Diary.model.Plan;
import org.springframework.beans.factory.annotation.Autowired;

public class PlanDaoTest extends BaseDaoTestCase {

	@Autowired
	private PlanDao planDao;
	
	@Test
	public void testGetPlansStartingOn() {
		//there are plans starting on this date
		Date date = Date.valueOf("2016-1-1");
		long memberID = 1l;
		List<Plan> plans = planDao.getPlansStartingOn(date, memberID);
		
		assertFalse(plans.isEmpty());
	}
	
	@Test
	public void testGetAllUnfinishedPlans() {
		//there are unfinished plans for member with id 1
		long memberID = 1l;
		List<Plan> plans = planDao.getAllUnfinishedPlans(memberID);
		
		assertFalse(plans.isEmpty());
	}
	
	@Test
	public void testGetAllUnfinishedPlansAfterBefore() {
		//there two plans starting between 2016-1-1 and 2016-4-30
		//there is one plan which start on 2016-5-1 for person 1
		Date after = Date.valueOf("2016-1-1");
		Date before = Date.valueOf("2016-4-30");
		long personId = 1;
		
		List<Plan> plans = planDao.getAllUnfinishedPlans(personId, after, before);
		
		assertEquals(2, plans.size());
	}
}
