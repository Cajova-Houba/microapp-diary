package org.microapp.Diary.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.microapp.Diary.dao.PlanDao;
import org.microapp.Diary.generic.service.impl.GenericAccessManagerImpl;
import org.microapp.Diary.model.Goal;
import org.microapp.Diary.model.Plan;
import org.microapp.Diary.service.GoalManager;
import org.microapp.Diary.service.PlanManager;
import org.springframework.beans.factory.annotation.Autowired;

public class PlanManagerImpl extends GenericAccessManagerImpl<Plan, Long> implements
		PlanManager {

	private PlanDao planDao;
	
	@Autowired
	private GoalManager goalManager;
	
	@Autowired
	public PlanManagerImpl(PlanDao planDao) {
		super(planDao);
		this.planDao = planDao;
	}

	public List<Plan> getPlansStartingOn(Date date, long memberID) {
		return planDao.getPlansStartingOn(date, memberID);
	}

	public List<Plan> getAllUnfinishedPlans(long memberID) {
		return planDao.getAllUnfinishedPlans(memberID);
	}
	
	public List<Plan> getAllUnfinishedPlans(long memberId, Date startsAfter,
			Date startsBefore) {
		
		//check that startsBefore is before startsAfter
		if (startsAfter.getTime() >= startsBefore.getTime()) {
			throw new IllegalArgumentException("startsAfter  ("+startsAfter.toString()+") must be before startsBefore ("+startsBefore.toString()+").");
		}
		
		return planDao.getAllUnfinishedPlans(memberId, startsAfter, startsBefore);
	}

	public List<Plan> getPlans(long memberId, boolean completed,
			boolean uncompleted, Date from, Date to) {
		return null;
	}

	public List<Plan> getAllFinishedPlans(long memberId) {
		return planDao.getAllFinishedPlans(memberId);
	}

	public List<Plan> getAllFinishedPlans(long memberId, Date startsAfter,
			Date startsBefore) {
		//check that startsBefore is before startsAfter
		if (startsAfter.getTime() >= startsBefore.getTime()) {
			throw new IllegalArgumentException("startsAfter  ("+startsAfter.toString()+") must be before startsBefore ("+startsBefore.toString()+").");
		}
		return planDao.getAllFinishedPlans(memberId, startsAfter, startsBefore);
	}

	public Plan completePlan(Plan plan) {
		plan.complete();
		return save(plan);
	}
	
	@Override
	public Plan save(Plan object) {
		
		//save the plan
		Plan savedPlan = super.save(object);
		
		List<Goal> goals = object.getGoals(); 
		List<Goal> savedGoals = new ArrayList<Goal>(goals.size());
		
		//save goals 
		for(Goal g : goals) {
			g.setPlan(savedPlan);
			savedGoals.add(goalManager.save(g));
		}
		
		savedPlan.setGoals(savedGoals);
		return savedPlan;
	}
}
