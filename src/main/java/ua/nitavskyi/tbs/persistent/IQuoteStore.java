package ua.nitavskyi.tbs.persistent;

import java.util.Calendar;
import java.util.List;

import ua.nitavskyi.tbs.domain.TrendBar;
import ua.nitavskyi.tbs.domain.TrendBarType;

public interface IQuoteStore {

	List<TrendBar> retrieveTrendBars(String symbol, TrendBarType trendBarType,
			Calendar from, Calendar to);

	void storeTrendBar(TrendBar any);

}
