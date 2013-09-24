package ua.nitavskyi.tbs;

import java.util.Calendar;

import org.junit.Test;
import org.mockito.Mockito;

import ua.nitavskyi.tbs.domain.Quote;
import ua.nitavskyi.tbs.domain.TrendBarType;
import ua.nitavskyi.tbs.persistent.IQuoteStore;
import ua.nitavskyi.tbs.test.FactoryUtil;
import ua.nitavskyi.tbs.util.CalendarFactory;

public class TrendBarServiceTest {	
	
	private ITrendBarService createTestObject(){
		return new TrendBarService();
	}
	
	@Test
	public void testAddQuote(){
		ITrendBarService trendBarService = createTestObject();		
		
		IQuoteReceiver quoteReceiver = Mockito.mock(IQuoteReceiver.class);
		trendBarService.setQuoteReceiver(quoteReceiver);
		
		Quote quote = FactoryUtil.INSTANCE.createAnyQuote();
		trendBarService.add(quote);
		
		Mockito.verify(quoteReceiver).sendQuote(quote);		
	}
	
	@Test
	public void testGetHistory(){
		ITrendBarService trendBarService = createTestObject();
		Calendar from = Calendar.getInstance();
		from.set(Calendar.MONTH, 10);
		Calendar to = Calendar.getInstance();
		to.set(Calendar.MONTH, 11);
		TrendBarType trendBarType = TrendBarType.HOURLY;
		String symbol = "SIMPLE_SYMBOL";		
		
		IQuoteStore quoteStore = Mockito.mock(IQuoteStore.class);
		trendBarService.setQuoteStore(quoteStore);
		
		trendBarService.getHistory(symbol, trendBarType, from, to);
		
		Mockito.verify(quoteStore).retrieveTrendBars(symbol, trendBarType, from, to);
	}
	
	@Test
	public void testGetHistoryDefaultEndDate(){
		ITrendBarService trendBarService = createTestObject();
		Calendar from = Calendar.getInstance();
		from.set(Calendar.MONTH, 10);
		final Calendar NOW = Calendar.getInstance();
		TrendBarType trendBarType = TrendBarType.HOURLY;
		String symbol = "SIMPLE_SYMBOL";		
		
		IQuoteStore quoteStore = Mockito.mock(IQuoteStore.class);
		trendBarService.setQuoteStore(quoteStore);
		CalendarFactory calendarFactory = Mockito.mock(CalendarFactory.class);
		Mockito.when(calendarFactory.now()).thenReturn(NOW);
		trendBarService.setCalendarFactory(calendarFactory);	
		
		trendBarService.getHistory(symbol, trendBarType, from);
		
		Mockito.verify(quoteStore).retrieveTrendBars(symbol, trendBarType, from, NOW);
	}	

}
