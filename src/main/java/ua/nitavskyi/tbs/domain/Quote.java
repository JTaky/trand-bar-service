package ua.nitavskyi.tbs.domain;

public final class Quote {
	
	public static Quote newInstance
		(	String symbol
		,	Long timestamp
		, 	Double price
		){
		return new Quote(symbol, timestamp, price);
	}
	
	public final String SYMBOL;
	
	public final Long TIMESTAMP;
	
	public final Double PRICE;
	
	private Quote(String symbol, Long timestamp, Double price){
		this.SYMBOL = symbol;
		this.TIMESTAMP = timestamp;
		this.PRICE = price;
	}

}
