package ua.nitavskyi.tbs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.mockito.Mockito;

import ua.nitavskyi.tbs.domain.Quote;
import ua.nitavskyi.tbs.test.FactoryUtil;

public class AsyncQuoteReceiverTest {
	
	//Base implementation for callback interface
	private static class FinalizatorReceiverMonitor implements IQuoteReceiver.IReceiverProcessorMonitor {

		private AtomicReference<IQuoteReceiver> quoteReceiverRef;
		
		FinalizatorReceiverMonitor(AtomicReference<IQuoteReceiver> quoteReceiverRef){
			this.quoteReceiverRef = quoteReceiverRef;
		}
		
		@Override
		public void onStarted() {
			log("on receiver " + Thread.currentThread().getName() + " started callback");
		}

		@Override
		public void onQuoteProceed() {
			log("on quote proceed callback");
			if(quoteReceiverRef != null && quoteReceiverRef.get() != null){
				quoteReceiverRef.get().stopReceiver();
			}
		}

		@Override
		public void onStoped() {
			log("on receiver " + Thread.currentThread().getName() + " stopped callback");			
		}
		
	}	
	
	private static void log(String msg){
		//System.out.println(msg);
	}	
	
	public IQuoteReceiver createInstance(){
		return new AsyncQuoteReceiver();
	}
	
	public IQuoteReceiver createInstance(IQuoteReceiver.IReceiverProcessorMonitor receiverMonitor){
		return new AsyncQuoteReceiver(receiverMonitor);
	}
	
	@Test
	public void testSendQuote() throws InterruptedException {
		final CountDownLatch sendQuoteVerified = new CountDownLatch(1);
		log("*** testSendQuote started ***");
		final Quote quote = FactoryUtil.INSTANCE.createAnyQuote();
		final ITrendBarBuilder trendBarBuilder = Mockito.mock(ITrendBarBuilder.class);
		final AtomicReference<IQuoteReceiver> quoteReceiverRef = new AtomicReference<IQuoteReceiver>();
		final IQuoteReceiver quoteReceiver = createInstance(new FinalizatorReceiverMonitor(quoteReceiverRef){
			@Override
			public void onQuoteProceed() {
				try {
					Mockito.verify(trendBarBuilder).processQuote(quote);
					log("sendQuote verified");
					sendQuoteVerified.countDown();
				} finally {
					super.onQuoteProceed();
				}
			}			
		});
		quoteReceiverRef.set(quoteReceiver);
		quoteReceiver.setTrendBarBuilder(trendBarBuilder);

		quoteReceiver.sendQuote(quote);

		sendQuoteVerified.await();
		log("*** testSendQuote finished ***\n");		
	}
	
	@Test(expected=IllegalStateException.class)
	public void testDoubleStart() {
		log("*** testDoubleStart started ***");
		IQuoteReceiver quoteReceiver = null;
		try {
			quoteReceiver = createInstance();
			quoteReceiver.startReceiver();
		} finally {
			if (quoteReceiver != null)
				quoteReceiver.stopReceiver();
			log("*** testDoubleStart finished ***\n");
		}		
	}	
	
	@Test(expected=IllegalStateException.class)
	public void testOverfullQueue() {
		final CountDownLatch sendQuoteVerified = new CountDownLatch(1);		
		log("*** testOverfullQueue started ***");			
		final ITrendBarBuilder trendBarBuilder = Mockito.mock(ITrendBarBuilder.class);		
		
		IQuoteReceiver quoteReceiver = null;		
		try {
			final AtomicReference<IQuoteReceiver> quoteReceiverRef = new AtomicReference<IQuoteReceiver>();
			quoteReceiver = createInstance(new FinalizatorReceiverMonitor(quoteReceiverRef){
				@Override
				public void onQuoteProceed() {				
					try {
						sendQuoteVerified.await();
					} catch (InterruptedException e) {
						log("Receiver Thread was interrupted");
					} finally {
						super.onQuoteProceed();
					}
				}			
			});
			quoteReceiverRef.set(quoteReceiver);
			quoteReceiver.setTrendBarBuilder(trendBarBuilder);

			Quote quote = FactoryUtil.INSTANCE.createAnyQuote();
			while (true) {
				quoteReceiver.sendQuote(quote);
			}
		} finally {
			sendQuoteVerified.countDown();
			// stop receiver
			if(quoteReceiver != null)
				quoteReceiver.stopReceiver();
			log("*** testOverfullQueue finished ***\n");
		}		
	}	

}
