package org.microapp.Diary.dao;

import java.sql.Date;
import java.util.List;

import org.microapp.Diary.generic.dao.GenericAccessDao;
import org.microapp.Diary.model.DailyRecord;

public interface DailyRecordDao extends GenericAccessDao<DailyRecord, Long> {

	/**
	 * Returns a list of all daily records from a certain date (inclusive).
	 * @param date Date from which daily records will be listed.
	 * @param memberID ID of member requested daily record belongs to.
	 * @return List of daily records. The list will be empty if there are no daily records found.
	 */
	public List<DailyRecord> getDailyRecordsFrom(Date date, Long memberID);
	
	/**
	 * Returns a list of daily records from a certain date range. (inclusive)
	 * @param after Include daily records from and after this date.
	 * @param before Include daily records from and before this date. Must be before {@code after}, if it's not, exception will be thrown.
	 * @param memberID Id of member daily records belongs to.
	 * @return List of daily records. Empty if no records are found.
	 */
	public List<DailyRecord> getDailyRecords(Long memberID, Date after, Date before);
	
	/**
	 * Returns a list of daily records from a certain date range. (inclusive)
	 * 
	 * Only the daily records containing any activities are listed.
	 * @param after Include daily records from and after this date.
	 * @param before Include daily records from and before this date. Must be before {@code after}, if it's not, exception will be thrown.
	 * @param memberID Id of member daily records belongs to.
	 * @return List of daily records. Empty if no records are found.
	 */
	public List<DailyRecord> getNonEmptyDailyRecords(Long memberID, Date after, Date before);
	
	/**
	 * Returns a daily record for a certain date.
	 * @param date Date.
	 * @param memberID ID of member daily record belongs to.
	 * @return Daily record for certain date. New record if there is no record for that date.
	 */
	public DailyRecord getDailyRecordFrom(Date date, Long memberID);

	/**
	 * Returns today daily record. If there's no record for today in database, method will create new one and return it.
	 * @param memberID ID of member requested daily record belongs to.
	 * @return Daily record for today.
	 */
	public DailyRecord getTodayDailyRecord(Long memberID);
}
