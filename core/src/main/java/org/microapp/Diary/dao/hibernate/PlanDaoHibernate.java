package org.microapp.Diary.dao.hibernate;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.microapp.Diary.dao.PlanDao;
import org.microapp.Diary.generic.dao.hibernate.GenericAccessDaoHibernate;
import org.microapp.Diary.model.Plan;

public class PlanDaoHibernate extends GenericAccessDaoHibernate<Plan, Long>
		implements PlanDao {

	public PlanDaoHibernate() {
		super(Plan.class);
	}
	
	public List<Plan> getPlansStartingOn(Date date, long memberID) {
		String query = "SELECT p FROM Plan p WHERE DATE(p.startDate)=DATE(:date) AND person_id=:memberId";
		Map<Object, Object> parameters = new HashMap<Object, Object>(2);
		parameters.put("date", date);
		parameters.put("memberId", memberID);
		return find(query, parameters);
	}

	public List<Plan> getAllUnfinishedPlans(long memberID) {
		String query = "SELECT p FROM Plan p WHERE p.completed=false AND person_id=?";
		Map<Object, Object> parameters = new HashMap<Object, Object>(1);
		parameters.put(0, memberID);
		return find(query, parameters);
	}

	public List<Plan> getAllUnfinishedPlans(long memberId, Date startsAfter,
			Date startsBefore) {
		String query = "SELECT p FROM Plan p WHERE "
				+ "p.completed=false "
				+ "AND DATE(p.startDate)>=DATE(:startsAfter) "
				+ "AND DATE(p.startDate)<=DATE(:startsBefore) "
				+ "AND p.personId=:personId";
		Map<Object, Object> parameters = new HashMap<Object, Object>(3);
		parameters.put("startsAfter", startsAfter);
		parameters.put("startsBefore", startsBefore);
		parameters.put("personId", memberId);
		return find(query, parameters);
	}

	public List<Plan> getAllFinishedPlans(long memberId) {
		String query = "SELECT p FROM Plan p WHERE p.completed=true AND person_id=?";
		Map<Object, Object> parameters = new HashMap<Object, Object>(1);
		parameters.put(0, memberId);
		return find(query, parameters);
	}

	public List<Plan> getAllFinishedPlans(long memberId, Date startsAfter,
			Date startsBefore) {
		String query = "SELECT p FROM Plan p WHERE "
				+ "p.completed=true "
				+ "AND DATE(p.startDate)>=DATE(:startsAfter) "
				+ "AND DATE(p.startDate)<=DATE(:startsBefore) "
				+ "AND p.personId=:personId";
		Map<Object, Object> parameters = new HashMap<Object, Object>(3);
		parameters.put("startsAfter", startsAfter);
		parameters.put("startsBefore", startsBefore);
		parameters.put("personId", memberId);
		return find(query, parameters);
	}
	
	

}
