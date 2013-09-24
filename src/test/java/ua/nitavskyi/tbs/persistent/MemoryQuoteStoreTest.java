package ua.nitavskyi.tbs.persistent;

import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ua.nitavskyi.tbs.domain.Quote;
import ua.nitavskyi.tbs.domain.TrendBar;
import ua.nitavskyi.tbs.domain.TrendBarType;
import ua.nitavskyi.tbs.test.FactoryUtil;
import ua.nitavskyi.tbs.util.CalendarFactory;

public class MemoryQuoteStoreTest {
	
	private static final String SYMBOL = "SYMBOL";
	private static final TrendBarType TB_TYPE = TrendBarType.HOURLY;
	private static final int CALENDAR_TYPE = Calendar.HOUR;

	private CalendarFactory calendarFactory = new CalendarFactory();

	private IQuoteStore createQuoteStore() {
		return new MemoryQuoteStore();
	}
	
	@Test
	public void testStoreAndSearch(){
		IQuoteStore quoteStore = createQuoteStore();
		
		Calendar now = calendarFactory.now();
		Calendar atStartPeriod = (Calendar) now.clone();
		TB_TYPE.truncate(atStartPeriod);
		Calendar inAnHour = (Calendar) now.clone();
		inAnHour.add(CALENDAR_TYPE, 1);
		Calendar inAnTwoHour = (Calendar) now.clone();
		inAnTwoHour.add(CALENDAR_TYPE, 2);
		
		Quote nowQuote = FactoryUtil.INSTANCE.createSomeQuote(
				SYMBOL, now.getTimeInMillis(), 1.0);
		Quote inAnHourQuote = FactoryUtil.INSTANCE.createSomeQuote(
				SYMBOL, inAnHour.getTimeInMillis(), 1.1);
		Quote inATwoHourQuote = FactoryUtil.INSTANCE.createSomeQuote(
				SYMBOL, inAnTwoHour.getTimeInMillis(), 0.9);
		
		quoteStore.storeTrendBar(new TrendBar(nowQuote, TB_TYPE));
		quoteStore.storeTrendBar(new TrendBar(inAnHourQuote, TB_TYPE));
		quoteStore.storeTrendBar(new TrendBar(inATwoHourQuote, TB_TYPE));
		List<TrendBar> tbList = quoteStore.retrieveTrendBars(SYMBOL, TB_TYPE, atStartPeriod, inAnHour);
		Assert.assertEquals(2, tbList.size());
		int index = 0;
		Assert.assertEquals(nowQuote.PRICE, tbList.get(index++).getHighPrice());
		Assert.assertEquals(inAnHourQuote.PRICE, tbList.get(index).getHighPrice());
	}

}
