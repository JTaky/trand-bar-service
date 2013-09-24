package ua.nitavskyi.tbs;

import ua.nitavskyi.tbs.persistent.IQuoteStore;
import ua.nitavskyi.tbs.persistent.MemoryQuoteStore;
import ua.nitavskyi.tbs.util.CalendarFactory;

import com.google.inject.AbstractModule;

public class TBSModule extends AbstractModule {

	/** {@inheritDoc} */
	@Override
	protected void configure() {
		bind(ITrendBarService.class).to(TrendBarService.class);
		bind(IQuoteReceiver.class).to(AsyncQuoteReceiver.class);
		bind(ITrendBarBuilder.class).to(TrendBarBuilder.class);
		
		bind(IQuoteStore.class).to(MemoryQuoteStore.class);
		
		bind(CalendarFactory.class);
	}

}