package org.microapp.Diary.dao;

import java.sql.Date;
import java.util.List;

import org.microapp.Diary.generic.dao.GenericDao;
import org.microapp.Diary.model.Activity;
import org.microapp.Diary.model.Goal;

public interface GoalDao extends GenericDao<Goal, Long> {

	/**
	 * Returns a list of uncompleted goals for a certain plan.
	 * @param planID
	 * @return
	 */
	public List<Goal> getUncompletedGoalsForPlan(long planID);
	
	/**
	 * Returns list of activities which are assigned to the same owner as the goal and also
	 * have the same activity type as the goal. Only the activities which happend after the start
	 * date of goal's plan are listed.
	 *  
	 * @param goal Goal for which activities will be returned.
	 * @return List of activities, empty if no activities are found.
	 */
	public List<Activity> getActivitiesForGoal(Goal goal);
}
