package org.microapp.Diary.service;

import static org.junit.Assert.*;

import java.sql.Date;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.junit.Test;
import org.microapp.Diary.generic.service.BaseManagerTestCase;
import org.springframework.beans.factory.annotation.Autowired;

public class ReportManagerTest extends BaseManagerTestCase {

	@Autowired
	private ReportManager reportManager;
	
	@Test
	public void testMakePlanReport() {
		long personId = 1;
		Date startsAfter = Date.valueOf("2016-1-1");
		Date startsBefore = Date.valueOf("2017-1-1");
		boolean completed = true;
		boolean uncompleted = true;
		JasperPrint print = reportManager.makePlanReport(personId, startsAfter, startsBefore, completed, uncompleted);
		
		//if error occurs, print will be null
		assertNotNull(print);
		
		//test print
		try {
			JasperExportManager.exportReportToPdfFile(print, "testMakePlanReport.pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMakeDailyRecordReport() {
		long personId = 1;
		Date after = Date.valueOf("2016-1-1");
		Date before = Date.valueOf("2017-1-1");
		JasperPrint print = reportManager.makeDailyRecordReport(personId, after, before);
		
		assertNotNull(print);
		
		//test print
		try {
			JasperExportManager.exportReportToPdfFile(print, "testMakeDailyRecordReport.pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMakeCompleteReport() {
		long personId = 1;
		Date planAfter = Date.valueOf("2016-1-1");
		Date planBefore = Date.valueOf("2017-1-1");
		boolean completed = true;
		boolean uncompleted = true;
		Date drAfter = Date.valueOf("2016-1-1");
		Date drBefore = Date.valueOf("2017-1-1");
		
		JasperPrint print = reportManager.makeCompleteReport(personId, planAfter, planBefore, completed, uncompleted, drAfter, drBefore);
		
		assertNotNull(print);
		
		//test print
		try {
			JasperExportManager.exportReportToPdfFile(print, "testMakeCompleteReport.pdf");
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
