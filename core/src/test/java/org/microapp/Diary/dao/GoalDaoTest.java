package org.microapp.Diary.dao;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.List;

import org.junit.Test;
import org.microapp.Diary.generic.dao.BaseDaoTestCase;
import org.microapp.Diary.model.Activity;
import org.microapp.Diary.model.Goal;
import org.springframework.beans.factory.annotation.Autowired;

public class GoalDaoTest extends BaseDaoTestCase {

	@Autowired
	private GoalDao goalDao;
	
	
	@Test
	public void testGetAllUncompletedGoalsForPlan() {
		//there are some uncompleted plans for plan with id 3
		long planId = 1l;
		List<Goal> goals = goalDao.getUncompletedGoalsForPlan(planId);
		
		assertFalse(goals.isEmpty());
	}
	
	@Test
	public void testGetActivitiesForGoal() {
		//there are activities and goals with the same activity type for the same member in test data
		long goalId = 1;
		Goal g = goalDao.get(goalId);
		Date gDate = g.getPlan().getStartDate();
		List<Activity> activities = goalDao.getActivitiesForGoal(g);
		
		assertFalse(activities.isEmpty());
		for(Activity a : activities) {
			Date aDate = a.getDailyRecord().getDate();
			assertEquals(a+" doesn't belong to the same member!", g.getPlan().getPersonId(), a.getDailyRecord().getPersonId());
			assertEquals(a+" has bad activity type!", a.getActivityType(), g.getActivityType());
			assertTrue(a+" is outdated!", gDate.before(aDate) || gDate.equals(aDate));
		}
	}
}
