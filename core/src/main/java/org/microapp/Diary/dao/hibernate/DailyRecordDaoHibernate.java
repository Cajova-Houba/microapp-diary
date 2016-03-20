package org.microapp.Diary.dao.hibernate;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.microapp.Diary.dao.DailyRecordDao;
import org.microapp.Diary.generic.dao.hibernate.GenericAccessDaoHibernate;
import org.microapp.Diary.model.DailyRecord;

public class DailyRecordDaoHibernate extends GenericAccessDaoHibernate<DailyRecord, Long>
		implements DailyRecordDao {
	
	public DailyRecordDaoHibernate() {
		super(DailyRecord.class);
	}

	public List<DailyRecord> getDailyRecordsFrom(Date date, Long memberID) {
		String query = "SELECT dr FROM DailyRecord dr WHERE dr.date>=:date AND dr.personId=:memberId";
		Map<Object, Object> parameters = new HashMap<Object, Object>(2);
		parameters.put("date", date);
		parameters.put("memberId", memberID);
		return find(query, parameters);
	}
	
	@Override
	public List<DailyRecord> getDailyRecords(Long memberID, Date after, Date before) {
		String query = "SELECT dr FROM DailyRecord dr WHERE "
				+ "dr.date>=:after "
				+ "AND dr.date<=:before "
				+ "AND dr.personId=:memberId";
		Map<Object, Object> parameters = new HashMap<Object, Object>(3);
		parameters.put("after", after);
		parameters.put("before", before);
		parameters.put("memberId", memberID);
		return find(query, parameters);
	}

	public DailyRecord getDailyRecordFrom(Date date, Long memberID) {
		String query = "SELECT dr FROM DailyRecord dr WHERE DATE(dr.date)=DATE(?) AND dr.personId=?";
		Map<Object, Object> parameters = new HashMap<Object, Object>(2);
		parameters.put(0, date);
		parameters.put(1, memberID);
		
		List<DailyRecord> drs = find(query, parameters);
		if(drs.isEmpty()) {
			DailyRecord tmp = new DailyRecord();
			tmp.setDate(date);
			tmp.setPersonId(memberID); 
			
			return save(tmp);
		} else {
			return drs.get(0);
		}
	}

	public DailyRecord getTodayDailyRecord(Long memberID) {
		Date today = new Date(Calendar.getInstance().getTime().getTime());
		return getDailyRecordFrom(today, memberID);
	}

}
