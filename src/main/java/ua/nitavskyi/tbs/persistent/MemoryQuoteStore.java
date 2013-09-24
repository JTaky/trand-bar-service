package ua.nitavskyi.tbs.persistent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.inject.Singleton;

import ua.nitavskyi.tbs.domain.TrendBar;
import ua.nitavskyi.tbs.domain.TrendBarType;

@Singleton
public class MemoryQuoteStore implements IQuoteStore {
	
	private static int SIZE = 100;	//TODO should be moved to property
	
	private final Lock[] locks = new Lock[SIZE];  
    {  
        for (int i = 0 ; i < locks.length; i++){  
            locks[i] = new ReentrantLock();  
        }  
    }  
	
	private final Map<String, Map<TrendBarType, TreeMap<Long, TrendBar>>> tbBySymbol = new HashMap<>();

	/** {@inheritDoc} */
	@Override
	public List<TrendBar> retrieveTrendBars(String symbol, TrendBarType tbType,
			Calendar from, Calendar to) {
		return new ArrayList<>(findTbByTimestampMap(symbol, tbType).subMap(
				from.getTimeInMillis(), true, to.getTimeInMillis(), true).values());
	}

	@Override
	public void storeTrendBar(TrendBar trendBar) {
		findTbByTimestampMap(trendBar.getSymbol(), trendBar.getTrendBarType())
				.put(trendBar.getTimestamp(), trendBar);
	}
	
	private TreeMap<Long, TrendBar> findTbByTimestampMap(String symbol,
			TrendBarType tbType) {
		Lock modifyLock = locks[Math.abs(symbol.hashCode() % SIZE)];
		try {
			modifyLock.lock();

			Map<TrendBarType, TreeMap<Long, TrendBar>> tbByType = tbBySymbol
					.get(symbol);
			if (tbByType == null) {
				tbByType = new IdentityHashMap<>();
				tbBySymbol.put(symbol, tbByType);
			}
			TreeMap<Long, TrendBar> tbByOpenTime = tbByType.get(tbType);
			if (tbByOpenTime == null) {
				tbByOpenTime = new TreeMap<>();
				tbByType.put(tbType, tbByOpenTime);
			}
			return tbByOpenTime;
		} finally {
			modifyLock.unlock();
		}
	}	

}
