package org.microapp.Diary.service;

import java.util.List;

import org.microapp.Diary.generic.service.GenericManager;
import org.microapp.Diary.model.Goal;

public interface GoalManager extends GenericManager<Goal, Long> {


	/**
	 * Returns a list of uncompleted goals for a certain plan. Also computes the progress for every plan.
	 * @param planID
	 * @return
	 */
	public List<Goal> getUncompletedGoalsForPlan(long planID);
	
	/**
	 * Computes the progress of goal and returns it. Doesn't save the goal.
	 * @param g Goal for which progress will be computed.
	 * @return Goal with computed progress.
	 */
	public Goal computeProgress(Goal g);
}
