package org.microapp.Diary.dao;

import java.sql.Date;
import java.util.List;

import org.microapp.Diary.generic.dao.GenericAccessDao;
import org.microapp.Diary.model.Plan;

public interface PlanDao extends GenericAccessDao<Plan, Long> {

	/**
	 * Return a list of plans which started after certain date (inclusive).
	 * @param date Starting date.
	 * @return List of plans. The list will be empty if no plans are found.
	 */
	public List<Plan> getPlansStartingOn(Date date, long memberID);
	
	
	/**
	 * Returns a list of all unfinished plans of certain member.
	 * @param memberID	ID of member requested plans belong to.
	 * @return	List of plans. Empty if no plans are found.
	 */
	public List<Plan> getAllUnfinishedPlans(long memberID);
	
	/**
	 * Returns a list of all unfinished plans of certain member.
	 * @param memberID	ID of member requested plans belong to.
	 * @param startsAfter Plans starting after this date (inclusive).
	 * @param startsBefore Plans starting before this date (inclusive). Must be before {@code startsAfter}.
	 * @return	List of plans. Empty if no plans are found.
	 */
	public List<Plan> getAllUnfinishedPlans(long memberId, Date startsAfter, Date startsBefore);
	
	/**
	 * Returns a list of all finished plans.
	 * @param memberId Id of member plans belong to.
	 * @return List of plans. Empty if no plans are found.
	 */
	public List<Plan> getAllFinishedPlans(long memberId);
	
	/**
	 * Returns a list of all finished plans of a certain member.
	 * @param memberID	ID of member requested plans belong to.
	 * @param startsAfter Plans starting after this date (inclusive).
	 * @param startsBefore Plans starting before this date (inclusive). Must be before {@code startsAfter}, if it's not, exception will be thrown.
	 * @return	List of plans. Empty if no plans are found.
	 */
	public List<Plan> getAllFinishedPlans(long memberId, Date startsAfter, Date startsBefore);
}
