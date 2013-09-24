package ua.nitavskyi.tbs;

import java.util.Calendar;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ua.nitavskyi.tbs.domain.Quote;
import ua.nitavskyi.tbs.domain.TrendBar;
import ua.nitavskyi.tbs.domain.TrendBarType;
import ua.nitavskyi.tbs.persistent.IQuoteStore;
import ua.nitavskyi.tbs.util.CalendarFactory;

@Singleton
public class TrendBarService implements ITrendBarService {
	
	@Inject
	private IQuoteReceiver quoteReceiver;
	
	@Inject
	private IQuoteStore quoteStore;
	
	@Inject
	private CalendarFactory calendarFactory;

	/** {@inheritDoc} */
	@Override
	public void add(Quote quote) {
		quoteReceiver.sendQuote(quote);		
	}
	
	/** {@inheritDoc} */
	@Override
	public List<TrendBar> getHistory(String symbol, TrendBarType trendBarType,
			Calendar from) {
		return getHistory(symbol, trendBarType, from, calendarFactory.now());	
	}	
	
	/** {@inheritDoc} */
	@Override
	public List<TrendBar> getHistory(String symbol, TrendBarType trendBarType,
			Calendar from, Calendar to) {
		return quoteStore.retrieveTrendBars(symbol, trendBarType, from, to);
	}
	
	/** {@inheritDoc} */
	//@Inject
	@Override
	public TrendBarService setQuoteReceiver(IQuoteReceiver quoteReceiver) {
		this.quoteReceiver = quoteReceiver;
		return this;
	}
	
	/** {@inheritDoc} */
	//@Inject
	@Override
	public TrendBarService setQuoteStore(IQuoteStore quoteStore) {
		this.quoteStore = quoteStore;	
		return this;
	}	
	
	/** {@inheritDoc} */
	//@Inject
	@Override
	public TrendBarService setCalendarFactory(CalendarFactory calendarFactory){
		this.calendarFactory = calendarFactory;
		return this;
	}

}