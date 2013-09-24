package ua.nitavskyi.tbs.domain;

import java.util.Calendar;

import org.junit.Test;

import junit.framework.Assert;

import ua.nitavskyi.tbs.test.FactoryUtil;
import ua.nitavskyi.tbs.util.CalendarFactory;

public class TrendBarTest {
	
	private CalendarFactory calendarFactory = new CalendarFactory();
	
	private TrendBarType tbType = TrendBarType.DAILY;
	
	@Test
	public void testCreate(){
		Quote singleQuote 
			= FactoryUtil.INSTANCE.createSomeQuote(calendarFactory.now().getTimeInMillis(), 1.0);
		
		TrendBar trendBar = new TrendBar(singleQuote, tbType);
		
		Assert.assertEquals(singleQuote.PRICE, trendBar.getOpenPrice());
		Assert.assertEquals(singleQuote.PRICE, trendBar.getHighPrice());
		Assert.assertEquals(singleQuote.PRICE, trendBar.getLowPrice());
		Assert.assertEquals(tbType, trendBar.getTrendBarType());
		
		Assert.assertTrue(singleQuote.TIMESTAMP >= trendBar.getTimestamp()
				&& singleQuote.TIMESTAMP < trendBar.getCloseTime());
		
		Assert.assertNull(trendBar.getClosePrice());		
	}
	
	@Test
	public void testHighLowPrice(){
		Calendar now = calendarFactory.now();
		Calendar inAnHour = (Calendar)now.clone();
		inAnHour.add(Calendar.HOUR, 1);
		Calendar inAHalfHour = (Calendar)now.clone();
		inAHalfHour.add(Calendar.MINUTE, 30);		
		
		Quote nowQuote 
			= FactoryUtil.INSTANCE.createSomeQuote(now.getTimeInMillis(), 1.0);
		Quote inAHalfHourQuote 
		= FactoryUtil.INSTANCE.createSomeQuote(inAHalfHour.getTimeInMillis(), 0.9);		
		Quote inAnHourQuote 
			= FactoryUtil.INSTANCE.createSomeQuote(inAnHour.getTimeInMillis(), 1.1);		
	
		TrendBar trendBar = new TrendBar(nowQuote, tbType);
		
		trendBar.processQuote(inAHalfHourQuote);
		trendBar.processQuote(inAnHourQuote);
		
		Assert.assertEquals(Math.max(nowQuote.PRICE, Math.max(inAHalfHourQuote.PRICE, inAnHourQuote.PRICE)), trendBar.getHighPrice());
		Assert.assertEquals(Math.min(nowQuote.PRICE, Math.min(inAHalfHourQuote.PRICE, inAnHourQuote.PRICE)), trendBar.getLowPrice());
	}
	
	@Test
	public void testClosePrice(){
		Calendar now = calendarFactory.now();
		Calendar inAnHour = (Calendar)now.clone();
		inAnHour.add(Calendar.HOUR, 1);
		
		Quote nowQuote 
			= FactoryUtil.INSTANCE.createSomeQuote(now.getTimeInMillis(), 1.0);
		Quote inAnHourQuote 
			= FactoryUtil.INSTANCE.createSomeQuote(inAnHour.getTimeInMillis(), 1.1);
		
		TrendBar trendBar = new TrendBar(nowQuote, tbType);

		trendBar.processQuote(inAnHourQuote);
		trendBar.closeTrendBar();
		
		Assert.assertEquals(inAnHourQuote.PRICE, trendBar.getClosePrice());
	}
	
	@Test
	public void testUnClosedTrendBarPrice(){
		Calendar now = calendarFactory.now();
		Calendar inAnHour = (Calendar)now.clone();
		inAnHour.add(Calendar.HOUR, 1);
		
		Quote nowQuote 
			= FactoryUtil.INSTANCE.createSomeQuote(now.getTimeInMillis(), 1.0);
		Quote inAnHourQuote 
			= FactoryUtil.INSTANCE.createSomeQuote(inAnHour.getTimeInMillis(), 1.1);
		
		TrendBar trendBar = new TrendBar(nowQuote, tbType);

		trendBar.processQuote(inAnHourQuote);
		
		Assert.assertNull(trendBar.getClosePrice());
	}	
	
	@Test
	public void testCloseDate(){
		Calendar now = calendarFactory.now();
		now.set(Calendar.MINUTE, 1);
		Calendar inAnDay = (Calendar)now.clone();
		inAnDay.add(Calendar.DATE, 1);
		
		Quote nowQuote 
			= FactoryUtil.INSTANCE.createSomeQuote(now.getTimeInMillis(), 1.0);
		
		TrendBar trendBar = new TrendBar(nowQuote, tbType);
		tbType.truncate(inAnDay);
		Assert.assertEquals(inAnDay.getTimeInMillis(), trendBar.getCloseTime());
	}
	
	@Test
	public void testSingleQuote(){
		Calendar now = calendarFactory.now();
		
		Quote singleQuote 
			= FactoryUtil.INSTANCE.createSomeQuote(now.getTimeInMillis(), 1.0);
		
		TrendBar trendBar = new TrendBar(singleQuote, tbType);
		tbType.truncate(now);		
		Assert.assertEquals(now.getTimeInMillis() + tbType.TIME_PERIOD, trendBar.getCloseTime());
	}	

}