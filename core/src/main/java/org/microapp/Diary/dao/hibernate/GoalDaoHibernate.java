package org.microapp.Diary.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.microapp.Diary.dao.GoalDao;
import org.microapp.Diary.generic.dao.hibernate.GenericDaoHibernate;
import org.microapp.Diary.model.Activity;
import org.microapp.Diary.model.DailyRecord;
import org.microapp.Diary.model.Goal;

public class GoalDaoHibernate extends GenericDaoHibernate<Goal, Long> implements
		GoalDao {

	public GoalDaoHibernate() {
		super(Goal.class);
	}
	
	public List<Goal> getUncompletedGoalsForPlan(long planID) {
		String query = "SELECT g FROM "+Goal.class.getSimpleName()+" g WHERE g.plan.id=? AND g.completed=false";
		Map<Object, Object> parameters = new HashMap<Object, Object>(1);
		parameters.put(0, planID);
		return find(query, parameters);
	}

	public List<Activity> getActivitiesForGoal(Goal goal) {
		String query = "SELECT a FROM "+Activity.class.getSimpleName()+" a, "+DailyRecord.class.getSimpleName()+" dr WHERE "
				// from plan's begin date, same person, same daily record and same activity type
				+ "DATE(dr.date)>=DATE(?) AND dr.personId=? AND a.dailyRecord=dr AND a.activityType=?";
		Map<Object, Object> parameters = new HashMap<Object, Object>(3);
		parameters.put(0, goal.getPlan().getStartDate());
		parameters.put(1, goal.getPlan().getPersonId());
		parameters.put(2, goal.getActivityType());
		return find(query, parameters);
	}
	
	

}
