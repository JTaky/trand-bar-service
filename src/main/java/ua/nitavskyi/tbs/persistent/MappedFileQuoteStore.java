package ua.nitavskyi.tbs.persistent;

import java.util.Calendar;
import java.util.List;

import com.google.inject.Singleton;

import ua.nitavskyi.tbs.domain.TrendBar;
import ua.nitavskyi.tbs.domain.TrendBarType;

@Singleton
public class MappedFileQuoteStore implements IQuoteStore {

	@Override
	public List<TrendBar> retrieveTrendBars(String symbol,
			TrendBarType trendBarType, Calendar from, Calendar to) {
		return null;
	}

	@Override
	public void storeTrendBar(TrendBar any) {
		// TODO Auto-generated method stub
		
	}

}
