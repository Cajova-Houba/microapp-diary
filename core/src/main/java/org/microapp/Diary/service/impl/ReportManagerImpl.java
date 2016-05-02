package org.microapp.Diary.service.impl;

import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.microapp.Diary.model.DailyRecord;
import org.microapp.Diary.model.MemberInfo;
import org.microapp.Diary.model.Plan;
import org.microapp.Diary.service.DailyRecordManager;
import org.microapp.Diary.service.MemberInfoManager;
import org.microapp.Diary.service.PlanManager;
import org.microapp.Diary.service.ReportManager;
import org.springframework.beans.factory.annotation.Autowired;

public class ReportManagerImpl implements ReportManager {

	private static Logger logger = LogManager.getLogger(ReportManagerImpl.class);
	
	/**
	 * Path to the plan template.
	 */
	//use it for tests
//	private final String planTemplate = "src/main/resources/reportTemplates/PlanTemplate.xml";
	private final String planTemplate = "reportTemplates/PlanTemplate.xml";
	
	/**
	 * Path to the daily record template.
	 */
//	private final String dailyRecordTemplate = "src/main/resources/reportTemplates/DailyRecordTemplate.xml";
	private final String dailyRecordTemplate = "reportTemplates/DailyRecordTemplate.xml";
	
	/**
	 * Path to the activity template.
	 */
//	private final String activityTemplate = "src/main/resources/reportTemplates/ActivityTemplate.xml";
	private final String activityTemplate = "reportTemplates/ActivityTemplate.xml";
	
	@Autowired
	private PlanManager planManager;
	
	@Autowired
	private DailyRecordManager drManager;
	
	@Autowired
	private MemberInfoManager memberInfoManager;
	
	
	public JasperPrint makeMemberReport(long personId) {
		return null;
	}

	public JasperPrint makePlanReport(long personId, Date startsAfter, Date startsBefore, boolean completed, boolean uncompleted) {

		logger.debug(String.format("Creating plan report. PersonId=%d, startsAfter=%s, startsBefore=%s, completed=%b, uncompleted=%b.", 
				personId, startsAfter.toString(), startsBefore.toString(), completed, uncompleted));
		
		//get plans
		List<Plan> plans = new ArrayList<>();
		if (completed) {
			plans.addAll(planManager.getAllFinishedPlans(personId, startsAfter, startsBefore));
			logger.debug("Completed plans loaded: "+plans.size());
		}
		
		if (uncompleted) {
			int tmp = plans.size();
			plans.addAll(planManager.getAllUnfinishedPlans(personId, startsAfter, startsBefore));
			logger.debug("Uncompleted plans loaded: "+(plans.size()-tmp));
		}
		
		//create data source
		JRBeanCollectionDataSource plansDS = new JRBeanCollectionDataSource(plans);
		Map parameters = new HashMap();
		
		//fill the report
		try {
			//compile the template
			JasperReport rep = JasperCompileManager.compileReport(planTemplate);
			logger.debug("Template compiled.");
			JasperPrint print = JasperFillManager.fillReport(rep, parameters, plansDS);
			logger.debug("Report printed.");
			return print;
		} catch (Exception e) {
			logger.error(e);
			
			return null;
		}
	}

	public JasperPrint makeDailyRecordReport(long personId, Date after, Date before) {
		
		logger.debug(String.format("Creating daily record report. PersonId=%d, after=%s, before=%s.",
				personId, after.toString(), before.toString()));
		
		//load daily records
		List<DailyRecord> drs = drManager.getNonEmptyDailyRecords(personId, after, before);
		logger.debug(drs.size()+" daily records loaded.");
		
		//create data source
		JRBeanCollectionDataSource drDS = new JRBeanCollectionDataSource(drs);
		Map parameters = new HashMap();
		
		parameters.put("memberId", personId);
		List<MemberInfo> mi = memberInfoManager.getAllForPerson(personId);
		String name;
		if(mi.isEmpty()){
			name = "unknown";
		} else {
			parameters.put("memberName", mi.get(0).getFullName());
		}
		
		//fill the report
		try {
			//compile the activity template
			JasperReport actTemplate = JasperCompileManager.compileReport(activityTemplate);
			parameters.put("activityTemplate", actTemplate);
			logger.debug("Activity template compiled.");
			
			//compile the daily record template
			JasperReport drTemplate = JasperCompileManager.compileReport(dailyRecordTemplate);
			logger.debug("Daily record template compiled.");
			
			JasperPrint print = JasperFillManager.fillReport(drTemplate, parameters, drDS);
			logger.debug("Report printed.");
			
			return print;
		} catch (Exception e) {
			logger.error(e.toString());
			return null;
		}
	}

	public JasperPrint makeCompleteReport(long personId, Date planAfter,
			Date planBefore, boolean completed, boolean uncompleted,
			Date drAfter, Date drBefore) {
		
		logger.debug(String.format("Creating complete report. PersonId=%d, planAfter=%s, planBefore=%s, completed=%b, uncompleted=%b, drAfter=%s, drBefore=%s",
				personId, planAfter, planBefore, completed, uncompleted, drAfter, drBefore));
		
		JasperPrint planReport = makePlanReport(personId, planAfter, planBefore, completed, uncompleted);
		JasperPrint drReport = makeDailyRecordReport(personId, drAfter, drBefore);
		
		//if error occurs
		if (planReport == null || drReport == null) {
			return null;
		}
		
		//append drReport to the planReport
		for(JRPrintPage page : drReport.getPages()) {
			planReport.addPage(page);
		}
		
		return planReport;
	}
	
	

	@Override
	public JasperPrint makeCompleteReportForMembers(List<Long> personIds,
			Date planAfter, Date planBefore, boolean completed,
			boolean uncompleted, Date drAfter, Date drBefore) {

		if(personIds.isEmpty()) {
			return new JasperPrint();
		}
	
		JasperPrint completeReport = makeCompleteReport(personIds.get(0), planAfter, planBefore, completed, uncompleted, drAfter, drBefore);
		
		if(personIds.size() > 1) {
			for (Iterator iterator = personIds.subList(1, personIds.size()).iterator(); iterator.hasNext();) {
				Long personId = (Long) iterator.next();
				JasperPrint tmp = makeCompleteReport(personId, planAfter, planBefore, completed, uncompleted, drAfter, drBefore);
				
				for(JRPrintPage page : tmp.getPages()) {
					completeReport.addPage(page);
				}
			}
		}
		
		return completeReport;
	}

	@Override
	public byte[] exportToPdf(JasperPrint print) {
		try {
			return JasperExportManager.exportReportToPdf(print);
		} catch (JRException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

}
