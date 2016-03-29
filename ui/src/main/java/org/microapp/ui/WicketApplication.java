package org.microapp.ui;

import java.sql.Date;

import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.microapp.ui.base.converters.SqlDateConverter;
import org.microapp.ui.diary.activity.DailyActivityPage;
import org.microapp.ui.diary.activity.form.ActivityFormPage;
import org.microapp.ui.diary.activity.report.DailyRecordReportPage;
import org.microapp.ui.diary.coach.CoachPage;
import org.microapp.ui.diary.coach.plans.MultiplePlansPage;
import org.microapp.ui.diary.coach.report.CompleteReportPage;
import org.microapp.ui.diary.coach.report.OneMemberReportPage;
import org.microapp.ui.diary.goal.GoalFormPage;
import org.microapp.ui.diary.plans.PlansPage;
import org.microapp.ui.diary.plans.detail.PlanDetailPage;
import org.microapp.ui.diary.plans.form.PlanFormPage;
import org.microapp.ui.diary.plans.report.PlanReportPage;
import org.microapp.ui.membership.MembershipPage;
import org.microapp.ui.security.DiarySession;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see org.microapp.ui.Start#main(String[])
 */
public class WicketApplication extends AuthenticatedWebApplication
{
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();

		// add your configuration here
		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
		
		mountPage("/home", HomePage.class);
		mountPage("/membership", MembershipPage.class);
		mountPage("/diary", DailyActivityPage.class);
		mountPage("/diary/activity/daily", DailyActivityPage.class);
		mountPage("/diary/activity", ActivityFormPage.class);
		mountPage("/diary/activity/report", DailyRecordReportPage.class);
		
		mountPage("/diary/plans", PlansPage.class);
		mountPage("/diary/plans/detail", PlanDetailPage.class);
		mountPage("/diary/plans/form",PlanFormPage.class);
		mountPage("/diary/plans/report",PlanReportPage.class);
		mountPage("/diary/goal", GoalFormPage.class);

		mountPage("/diary/coach", CoachPage.class);
		mountPage("/diary/coach/memberReport", OneMemberReportPage.class);
		mountPage("/diary/coach/completeReport", CompleteReportPage.class);
		mountPage("/diary/coach/plans", MultiplePlansPage.class);
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return MembershipPage.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return DiarySession.class;
	}
	
	@Override
	protected IConverterLocator newConverterLocator() {
		ConverterLocator cl = (ConverterLocator)super.newConverterLocator();
		
		cl.set(Date.class, new SqlDateConverter());
		
		return cl;
	}
}
