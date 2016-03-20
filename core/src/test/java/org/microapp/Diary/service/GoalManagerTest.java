package org.microapp.Diary.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.microapp.Diary.generic.service.BaseManagerTestCase;
import org.microapp.Diary.model.Goal;
import org.springframework.beans.factory.annotation.Autowired;

public class GoalManagerTest extends BaseManagerTestCase {

	@Autowired
	private GoalManager goalManager;
	
	
	@Test
	public void testGetAllUncompletedGoalsForPlan() {
		//there are some uncompleted goals for plan with id 1
		long planId = 1l;
		List<Goal> goals = goalManager.getUncompletedGoalsForPlan(planId);
		
		assertFalse(goals.isEmpty());
	}
	
	@Test
	public void testGet() {
		//goal with id 1 should have related activities in test data, so the progress should be 
		long goalId = 1l;
		Goal g = goalManager.get(goalId);
		assertEquals(0.05, g.getProgress(), 0.0001);
	}
}
