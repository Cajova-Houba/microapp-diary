package org.microapp.Diary.service;

import java.sql.Date;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * Methods for reports.
 * @author Zdenda
 *
 */
public interface ReportManager {

	
	/**
	 * Will create a short report about the member diary.
	 * @param personId Id of person which report is to be created.
	 * @return Jasper print object which can be then exported to various formats. If error occurs during reporting, null is returned.
	 */
	public JasperPrint makeMemberReport(long personId);
	
	/**
	 * Will create a jasper report of plans matching the specified criteria.
	 * @param personId Id of person which plans are going to be in the report.
	 * @param startsAfter Only include plans starting after this date (inclusive).
	 * @param startsBefore Only include plans starting before this date (inclusive). Must be after {@code startsAfter}.  
	 * @param completed Include completed plans.
	 * @param uncompleted Include uncompleted plans.
	 * @return Jasper print object which can be then exported to various formats. If error occurs during reporting, null is returned. 
	 */
	public JasperPrint makePlanReport(long personId, Date startsAfter, Date startsBefore, boolean completed, boolean uncompleted);
	
	/**
	 * Will create a jasper report of daily records and activities matching specified criteria.
	 * @param personId Id of person which daily records are going to be in the report.
	 * @param after Only include daily records after this date (inclusive).
	 * @param before Only include daily records before this date (inclusive). Must be after {@code after}.
	 * @return Jasper print object which can then be exported to varius formats. If error occurs during reporting, null is returned.
	 */
	public JasperPrint makeDailyRecordReport(long personId, Date after, Date before);
	
	/**
	 * Will create a complete report of members activities and plans.
	 * @param personId Id of person for which the report will be created.
	 * @param planAfter Include plans starting after this date (inclusive).
	 * @param planBefore Include plans starting before this date (inclusive). Must be after {@code planAfter}.
	 * @param completed Include completed plans.
	 * @param uncompleted Include uncompleted plans.
	 * @param drAfter Include daily records after this date (inclusive).
	 * @param drBefore Include daily records before this date (inclusive). Must be after {@code drAfter}.
	 * @return Jasper print object which can then be exported to varius formats. If error occurs during reporting, null is returned.
	 */
	public JasperPrint makeCompleteReport(long personId, Date planAfter, Date planBefore, boolean completed, boolean uncompleted, Date drAfter, Date drBefore);
}
