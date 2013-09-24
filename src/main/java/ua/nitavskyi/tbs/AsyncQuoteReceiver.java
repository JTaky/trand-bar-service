package ua.nitavskyi.tbs;

import java.util.concurrent.ArrayBlockingQueue;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ua.nitavskyi.tbs.domain.Quote;

@Singleton
class AsyncQuoteReceiver extends Thread implements IQuoteReceiver { //TODO consider ThreadPool strategy in order to vertical scaling
	
	private static class NullObjectReceiverProcessor implements IReceiverProcessorMonitor {
		/** {@inheritDoc} */
		@Override
		public void onStarted() {}

		/** {@inheritDoc} */
		@Override
		public void onQuoteProceed() {}

		@Override
		public void onStoped() {}
		
	}
	
	//TODO read from some properties storage
	private static final int MAX_QUEUE_SIZE = 10000;
		
	private ArrayBlockingQueue<Quote> quoteQueue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
	
	@Inject
	private ITrendBarBuilder trendBarBuilder;
	
	private IReceiverProcessorMonitor receiverProcessorMonitor;
	
	public AsyncQuoteReceiver(){
		this(new NullObjectReceiverProcessor());
	}
	
	public AsyncQuoteReceiver(IReceiverProcessorMonitor receiverProcessorMonitor){
		this.receiverProcessorMonitor = receiverProcessorMonitor;
		setName("AsyncQuoteReceiverThread. Creation time: " + System.currentTimeMillis() + "ms");
		startReceiver();		
	}	
	
	@Override
	public void run() {
		try {
			receiverProcessorMonitor.onStarted();
			while (!isInterrupted()) {
				processQuote(quoteQueue.take());
			}			
		} catch (InterruptedException e) {
			System.err.print(Thread.currentThread());
			e.printStackTrace();	//TODO improve LOG strategy			
		} finally {
			receiverProcessorMonitor.onStoped();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void sendQuote(Quote quote) {		
		quoteQueue.add(quote);
	}

	/** {@inheritDoc} */
	@Override
	public void startReceiver() {
		if(isAlive())
			throw new IllegalStateException("Receiver already started");
		start();		
	}

	/** {@inheritDoc} */
	@Override
	public void stopReceiver() {
		interrupt();	
	}
	
	/** {@inheritDoc} */
	@Override
	public AsyncQuoteReceiver setTrendBarBuilder(ITrendBarBuilder trendBarBuilder){
		this.trendBarBuilder = trendBarBuilder;
		return this;
	}	
	
	private void processQuote(Quote quote) {
		trendBarBuilder.processQuote(quote);
		receiverProcessorMonitor.onQuoteProceed();
	}

}