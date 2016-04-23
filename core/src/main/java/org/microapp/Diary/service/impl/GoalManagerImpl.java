package org.microapp.Diary.service.impl;

import java.util.List;

import org.microapp.Diary.dao.GoalDao;
import org.microapp.Diary.generic.service.impl.GenericManagerImpl;
import org.microapp.Diary.model.Activity;
import org.microapp.Diary.model.Goal;
import org.microapp.Diary.service.GoalManager;
import org.springframework.beans.factory.annotation.Autowired;

public class GoalManagerImpl extends GenericManagerImpl<Goal, Long> implements
		GoalManager {

	private GoalDao	 goalDao;
	
	@Autowired
	public GoalManagerImpl(GoalDao goalDao) {
		super(goalDao);
		this.goalDao = goalDao;
	}
	
	public List<Goal> getUncompletedGoalsForPlan(long planID) {
		List<Goal> goals = goalDao.getUncompletedGoalsForPlan(planID);
		
		for (Goal g : goals) {
			g = computeProgress(g);
		}
		
		return goals;
	}

	public Goal computeProgress(Goal g) {
		List<Activity> activities = goalDao.getActivitiesForGoal(g);
		double sum = 0;
		for (Activity a : activities) {
			sum += a.valueInDefaultUnit();
		}
		
		double progress = sum / g.valueInDefaultUnit(); 
		
		
		if(g.valueInDefaultUnit() == 0) {
			g.setProgress(0);
		} else {
			g.setProgress(sum / g.valueInDefaultUnit());
		}
		
		return g;
	}
	
	/**
	 * Returns the goal is it exists and also computes the goal progress.
	 */
	@Override
	public Goal get(Long id) {
		Goal g = super.get(id);
		
		//compute the progress
		g = computeProgress(g);
		
		return g;
	}

}
