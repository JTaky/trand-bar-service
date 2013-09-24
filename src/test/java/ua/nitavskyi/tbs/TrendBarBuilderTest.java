package ua.nitavskyi.tbs;

import java.util.Calendar;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import ua.nitavskyi.tbs.domain.Quote;
import ua.nitavskyi.tbs.domain.TrendBar;
import ua.nitavskyi.tbs.domain.TrendBarType;
import ua.nitavskyi.tbs.persistent.IQuoteStore;
import ua.nitavskyi.tbs.test.FactoryUtil;
import ua.nitavskyi.tbs.util.CalendarFactory;

public class TrendBarBuilderTest {
	
	private static final int CALENDAR_TYPE = Calendar.DATE;

	private CalendarFactory calendarFactory = new CalendarFactory();

	private ITrendBarBuilder createTBBuilder() {
		return new TrendBarBuilder();
	}

	@Test
	public void testOpenNewBar() {
		ITrendBarBuilder tbBuilder = createTBBuilder();
		ITrendBarBuilder.ITrendBarFactory tbFactory = Mockito.mock(ITrendBarBuilder.ITrendBarFactory.class);
		for(TrendBarType curTbType : TrendBarType.values()){
			Mockito.when(tbFactory.createTrendBar(Matchers.any(Quote.class), Matchers.eq(curTbType))).thenReturn(FactoryUtil.INSTANCE.createAnyTrendBar());			
		}
		tbBuilder.setTrendBarFactory(tbFactory);

		Quote quote = FactoryUtil.INSTANCE.createAnyQuote();

		tbBuilder.processQuote(quote);
		for(TrendBarType curTbType : TrendBarType.values()){
			Mockito.verify(tbFactory, Mockito.atLeastOnce()).createTrendBar(Mockito.<Quote>any(), Matchers.eq(curTbType));
		}		
	}
	
	@Test
	public void testStoreOldBar() {
		ITrendBarBuilder tbBuilder = createTBBuilder();
		IQuoteStore quoteStore = Mockito.mock(IQuoteStore.class);
		tbBuilder.setQuoteStore(quoteStore);
		
		Calendar now = calendarFactory.now();
		Calendar inAnHour = (Calendar) now.clone();
		inAnHour.add(CALENDAR_TYPE, 1);
		Calendar inAnTwoHour = (Calendar) now.clone();
		inAnTwoHour.add(CALENDAR_TYPE, 2);

		Quote nowQuote = FactoryUtil.INSTANCE.createSomeQuote(
				now.getTimeInMillis(), 1.0);
		Quote inAnHourQuote = FactoryUtil.INSTANCE.createSomeQuote(
				inAnHour.getTimeInMillis(), 1.1);
		Quote inATwoHourQuote = FactoryUtil.INSTANCE.createSomeQuote(
				inAnTwoHour.getTimeInMillis(), 0.9);	

		tbBuilder.processQuote(nowQuote);	
		tbBuilder.processQuote(inAnHourQuote);
		tbBuilder.processQuote(inATwoHourQuote);
		Mockito.verify(quoteStore, Mockito.atLeastOnce()).storeTrendBar(Mockito.<TrendBar>any());
	}	

}
