package ua.nitavskyi.tbs.demo;

import java.util.Iterator;

import ua.nitavskyi.tbs.domain.Quote;

public class QuoteGenerator implements Iterable<Quote> {	
	
	private String symbol;
	
	private long baseTimeStamp;
	
	private long maxInterval;
	
	public QuoteGenerator(String symbol, long baseTimeStamp, long maxInterval){
		this.symbol = symbol;
		this.baseTimeStamp = baseTimeStamp;
		this.maxInterval = maxInterval;
	}

	@Override
	public Iterator<Quote> iterator() {
		return new Iterator<Quote>(){

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public Quote next() {
				return Quote.newInstance(symbol, nextTimeStamp(), nextPrice());
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			};		
		};
	}
	
	private double nextPrice(){
		return Math.random();
	}
	
	private long nextTimeStamp(){
		return baseTimeStamp += Math.round(maxInterval*Math.random()/100.0);
	}	
	
}
