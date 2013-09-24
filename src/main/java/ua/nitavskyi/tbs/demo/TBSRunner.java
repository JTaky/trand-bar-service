package ua.nitavskyi.tbs.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ua.nitavskyi.tbs.ITrendBarService;
import ua.nitavskyi.tbs.TBSModule;
import ua.nitavskyi.tbs.domain.Quote;
import ua.nitavskyi.tbs.domain.TrendBar;
import ua.nitavskyi.tbs.domain.TrendBarType;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TBSRunner {
	
	@SuppressWarnings("serial")
	private static final List<String> SYMBOLS_LIST = new ArrayList<String>()
	{

		{
			add("EURUSD");
			add("EURUAH");
			add("EURJYP");
		}
	};
	
	private volatile boolean isStopRequested = false;
	
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	private void startDemo() throws IOException {
		Injector injector = Guice.createInjector(new TBSModule());
		for(Runnable runnable : createDemoExecutors(injector.getInstance(ITrendBarService.class))){
			executor.execute(runnable);
		}
		try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
			String userInput = "null";
			while ("null".equalsIgnoreCase(userInput)) { // =)
				userInput = in.readLine();
			}
			System.out.println("Stop requested...");
			isStopRequested = true;
			executor.shutdown();
		}
	}

	private List<Runnable> createDemoExecutors(final ITrendBarService tbs) {
		List<Runnable> generators = new ArrayList<>();
		final long maxInterval = TrendBarType.MINUTELY.TIME_PERIOD;
	    final Calendar baseTimeStamp = Calendar.getInstance();
	    
		for (final String curSymbol : SYMBOLS_LIST) {
			generators.add(new Runnable() {
				@Override
				public void run() {
					try {
						for (Quote quote : new QuoteGenerator(curSymbol, baseTimeStamp.getTimeInMillis(), maxInterval)) {
							if (isStopRequested) {
								return;
							}
							tbs.add(quote);

							Thread.sleep(1);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			});
		}
		generators.add(new Runnable() {			
			@Override
			public void run() {
				while(!isStopRequested){
					try {
						Thread.sleep(1000);
						for(final String curSymbol : SYMBOLS_LIST){
							for(TrendBarType curTrendBarType : TrendBarType.values()){
								printTb(tbs.getHistory(curSymbol, curTrendBarType, baseTimeStamp));																
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		return generators;
	}
	
	private void printTb(List<TrendBar> tbList) {
		if(tbList.isEmpty()){
			return;
		}
		System.out.println("***** " + tbList.get(0).getSymbol() + " *****");
		for(TrendBar curTb : tbList){
			System.out.println(curTb);
		}		
		System.out.println("*****");
	}	
	
	public static void main(String[] args) throws IOException {		
		new TBSRunner().startDemo();
	}	

}