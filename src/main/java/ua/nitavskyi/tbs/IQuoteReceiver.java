package ua.nitavskyi.tbs;

import ua.nitavskyi.tbs.domain.Quote;

interface IQuoteReceiver {
	
	interface IReceiverProcessorMonitor {	//better to use more powerful pattern in real system EventAggregator
		
		void onStarted();
		
		void onQuoteProceed();

		void onStoped();
	}	

	void sendQuote(Quote quote);	
	
	void startReceiver();
	
	void stopReceiver();
	
	IQuoteReceiver setTrendBarBuilder(ITrendBarBuilder trendBarBuilder);

}
