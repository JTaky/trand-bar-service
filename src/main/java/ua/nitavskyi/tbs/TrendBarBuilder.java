package ua.nitavskyi.tbs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import ua.nitavskyi.tbs.domain.Quote;
import ua.nitavskyi.tbs.domain.TrendBar;
import ua.nitavskyi.tbs.domain.TrendBarType;
import ua.nitavskyi.tbs.persistent.IQuoteStore;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
class TrendBarBuilder implements ITrendBarBuilder {
	//TODO develop smart thread safe strategy
	
	private IQuoteStore quoteStore;
	
	private ITrendBarFactory tbFactory = new ITrendBarFactory() {

		public TrendBar createTrendBar(Quote quote, TrendBarType trendBarType) {
			return new TrendBar(quote, trendBarType);
		}

	};
	
	private Map<String, Map<TrendBarType, TrendBar>> currentTrendBars = new HashMap<>();

	@Override
	public void processQuote(Quote quote) {
		List<TrendBar> trendBarsList = findTrendBars(quote);
		for(TrendBar curTrendBar : trendBarsList){
			curTrendBar.processQuote(quote);
		}		
	}

	@Inject
	@Override
	public ITrendBarBuilder setQuoteStore(IQuoteStore quoteStore){
		this.quoteStore = quoteStore;
		return this;
	}
	
	@Override
	public ITrendBarBuilder setTrendBarFactory(ITrendBarFactory tbFactory){
		this.tbFactory = tbFactory;
		return this;
	}

	private List<TrendBar> findTrendBars(Quote quote) {
		Map<TrendBarType, TrendBar> trendBarsByType = currentTrendBars
				.get(quote.SYMBOL);
		if (trendBarsByType == null) {
			trendBarsByType = new IdentityHashMap<TrendBarType, TrendBar>();
			currentTrendBars.put(quote.SYMBOL, trendBarsByType);
		}
		List<TrendBar> trendBarsList = new ArrayList<>(
				TrendBarType.values().length);
		for (TrendBarType curTrendBarType : TrendBarType.values()) {
			TrendBar trendBar = trendBarsByType.get(curTrendBarType);
			if (trendBar != null && quote.TIMESTAMP >= trendBar.getCloseTime()) { //store old tb if need
				quoteStore.storeTrendBar(trendBar);
				trendBar = null;
			}
			if (trendBar == null) {
				trendBar = tbFactory.createTrendBar(quote, curTrendBarType);
				trendBarsByType.put(curTrendBarType, trendBar);
			}
			trendBarsList.add(trendBar);
		}
		return trendBarsList;
	}	
	
}
