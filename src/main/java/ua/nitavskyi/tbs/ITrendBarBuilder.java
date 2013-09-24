package ua.nitavskyi.tbs;

import ua.nitavskyi.tbs.domain.Quote;
import ua.nitavskyi.tbs.domain.TrendBar;
import ua.nitavskyi.tbs.domain.TrendBarType;
import ua.nitavskyi.tbs.persistent.IQuoteStore;

interface ITrendBarBuilder {
	
	interface ITrendBarFactory {
		TrendBar createTrendBar(Quote quote, TrendBarType trendBarType);
	}

	void processQuote(Quote quote);

	ITrendBarBuilder setQuoteStore(IQuoteStore quoteStore);
	
	ITrendBarBuilder setTrendBarFactory(ITrendBarFactory tbFactory);

}
