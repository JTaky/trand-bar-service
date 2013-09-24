package ua.nitavskyi.tbs.domain;

import java.util.Calendar;

import junit.framework.Assert;

import org.junit.Test;

import ua.nitavskyi.tbs.util.CalendarFactory;

public class TrendBarTypeTest {
	
	private CalendarFactory calendarFactory = new CalendarFactory();
	
	@Test
	public void testPeriodValue(){
		Calendar now = calendarFactory.now();
		Calendar inAMinute = (Calendar)now.clone();
		inAMinute.add(Calendar.MINUTE, 1);
		Calendar inAnHour = (Calendar)now.clone();
		inAnHour.add(Calendar.HOUR, 1);
		Calendar inADay = (Calendar)now.clone();
		inADay.add(Calendar.DATE, 1);

		Assert.assertEquals
			(	inAMinute.getTimeInMillis() - now.getTimeInMillis()
			, 	TrendBarType.MINUTELY.TIME_PERIOD
			);
		Assert.assertEquals
			(	inAnHour.getTimeInMillis() - now.getTimeInMillis()
			, 	TrendBarType.HOURLY.TIME_PERIOD
			);
		Assert.assertEquals
			(	inADay.getTimeInMillis() - now.getTimeInMillis()
			,	TrendBarType.DAILY.TIME_PERIOD
			);
	}

}
