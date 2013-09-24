package ua.nitavskyi.tbs.test;

import ua.nitavskyi.tbs.domain.Quote;
import ua.nitavskyi.tbs.domain.TrendBar;
import ua.nitavskyi.tbs.domain.TrendBarType;

public enum FactoryUtil {
	
	INSTANCE;
	
	static final String DEFAULT_SYMBOL = "";
	static final Long DEFAULT_TIME_STAMP = 0L;
	static final TrendBarType DEFAULT_TB_TYPE = TrendBarType.DAILY;
	static final Double DEFAULT_PRICE = 0.0;	
	
	public Quote createAnyQuote(){
		return createSomeQuote(DEFAULT_SYMBOL, DEFAULT_TIME_STAMP, DEFAULT_PRICE);
	}
	
	public Quote createAnyQuoteWithTime(long timeStamp){
		return createSomeQuote(DEFAULT_SYMBOL, timeStamp, DEFAULT_PRICE);
	}
	
	public Quote createSomeQuote(long timeStamp, Double price){
		return createSomeQuote(DEFAULT_SYMBOL, timeStamp, price);
	}
	
	public Quote createSomeQuote(String symbol, long timeStamp, Double price){
		return Quote.newInstance(symbol, timeStamp, price);
	}	
	
	public TrendBar createAnyTrendBar(){
		return new TrendBar(createAnyQuote(), DEFAULT_TB_TYPE);
	}

}