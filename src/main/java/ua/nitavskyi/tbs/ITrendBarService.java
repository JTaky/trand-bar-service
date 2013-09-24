package ua.nitavskyi.tbs;

import java.util.Calendar;
import java.util.List;

import ua.nitavskyi.tbs.domain.Quote;
import ua.nitavskyi.tbs.domain.TrendBar;
import ua.nitavskyi.tbs.domain.TrendBarType;
import ua.nitavskyi.tbs.persistent.IQuoteStore;
import ua.nitavskyi.tbs.util.CalendarFactory;

/**
 * Facade for business logic implementation.
 */
public interface ITrendBarService {

	/**
	 * Add new quote to bar service in order to save bar history
	 * 
	 * @param quote new quote for bar
	 */
	void add(Quote quote);
	
	/**
	 * Retrieve bar history for concrete period.
	 * End period set as current time.
	 * 
	 * @param symbol quote symbol for history
	 * @param trendBarType type of quote
	 * @param from start history period
	 * 
	 * @return Bar history 
	 */
	List<TrendBar> getHistory(String symbol, TrendBarType trendBarType,
			Calendar from);	

	/**
	 * Retrieve bar history for concrete period
	 * 
	 * @param symbol quote symbol for history
	 * @param trendBarType type of quote
	 * @param from start history period
	 * @param to end history period
	 * 
	 * @return Bar history
	 */
	List<TrendBar> getHistory(String symbol, TrendBarType trendBarType, Calendar from,
			Calendar to);
	
	/**
	 * Setter for quote receiver strategy
	 * 
	 * @param quoteReceiver quote receiver
	 */
	ITrendBarService setQuoteReceiver(IQuoteReceiver quoteReceiver);
	
	/**
	 * Setter for quote store to process with history
	 * 
	 * @param quoteStore quote store
	 */
	ITrendBarService setQuoteStore(IQuoteStore quoteStore);

	/**
	 * Setter for Calendar factory to create date more easily
	 * 
	 * @param calendarFactory calendar factory
	 */
	ITrendBarService setCalendarFactory(CalendarFactory calendarFactory);		

}
