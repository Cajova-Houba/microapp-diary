package org.microapp.Diary.service;

import static org.junit.Assert.assertFalse;

import java.sql.Date;
import java.util.List;

import org.junit.Test;
import org.microapp.Diary.generic.service.BaseManagerTestCase;
import org.microapp.Diary.model.Plan;
import org.springframework.beans.factory.annotation.Autowired;

public class PlanManagerTest extends BaseManagerTestCase {

	@Autowired
	private PlanManager planManager;
	
	@Test
	public void testGetPlansStartingOn() {
		//there are plans starting on this date
		Date date = Date.valueOf("2016-1-1");
		long memberID = 1l;
		List<Plan> plans = planManager.getPlansStartingOn(date, memberID);
		
		assertFalse(plans.isEmpty());
	}
	
	@Test
	public void testGetAllUnfinishedPlans() {
		//there are unfinished plans for member with id 1
		long memberID = 1l;
		List<Plan> plans = planManager.getAllUnfinishedPlans(memberID);
		
		assertFalse(plans.isEmpty());
	}
}
