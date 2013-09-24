package ua.nitavskyi.tbs.domain;

public final class TrendBar {
	
	private String symbol;
	
	private Double openPrice;
	
	private Double closePrice;
	
	private Double highPrice;
	
	private Double lowPrice;
	
	private TrendBarType trendBarType;
	
	private long timestamp;
	
	private Quote lastQuote;
		
	public TrendBar(Quote openQuote, TrendBarType trendBarType){
		this.symbol = openQuote.SYMBOL;
		this.openPrice = openQuote.PRICE;	
		this.highPrice = openQuote.PRICE;
		this.lowPrice = openQuote.PRICE;
		this.trendBarType = trendBarType;
		this.timestamp = trendBarType.truncate(openQuote.TIMESTAMP);
		
		this.lastQuote = openQuote;
	}
	
	public void processQuote(Quote quote){
		assert symbol.equals(quote.SYMBOL);
		assert timestamp < quote.TIMESTAMP;
		assert getCloseTime() > quote.TIMESTAMP;
		
		if(this.getHighPrice() < quote.PRICE){
			this.highPrice = quote.PRICE;
		}
		if(this.getLowPrice() > quote.PRICE){
			this.lowPrice = quote.PRICE;
		}	
		
		this.lastQuote = quote;
	}	
	
	public void closeTrendBar(){
		this.closePrice = getLastQuote().PRICE;
	}
	
	public String getSymbol(){
		return symbol;
	}

	public Double getOpenPrice() {
		return openPrice;
	}

	public Double getClosePrice() {
		return closePrice;
	}

	public Double getHighPrice() {
		return highPrice;
	}
	public Double getLowPrice() {
		return lowPrice;
	}

	public TrendBarType getTrendBarType() {
		return trendBarType;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public Quote getLastQuote() {
		return lastQuote;
	}
	
	public long getCloseTime(){
		return this.timestamp + trendBarType.TIME_PERIOD;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() >= getCloseTime();
	}
	
	@Override
	public String toString() {
		return "TrendBar [symbol=" + symbol + ", openPrice=" + openPrice
				+ ", closePrice=" + closePrice + ", highPrice=" + highPrice
				+ ", lowPrice=" + lowPrice + ", trendBarType=" + trendBarType
				+ ", timestamp=" + timestamp + ", lastQuote=" + lastQuote + "]";
	}
}
