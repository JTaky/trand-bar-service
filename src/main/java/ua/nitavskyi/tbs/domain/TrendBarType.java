package ua.nitavskyi.tbs.domain;

import java.util.Calendar;

public enum TrendBarType {
	MINUTELY(60*1000){
		@Override
		public void truncate(Calendar datetime) {
			truncateByMinute(datetime);
		}		
	},
	HOURLY(60*60*1000){
		@Override
		public void truncate(Calendar datetime) {
			truncateByHour(datetime);
		}
	},
	DAILY(24*60*60*1000){
		@Override
		public void truncate(Calendar datetime) {
			truncateByDay(datetime);
		}		
	};
	
	public final long TIME_PERIOD;
	
	TrendBarType(long timePeriod){
		this.TIME_PERIOD = timePeriod;
	}

	public abstract void truncate(Calendar datetime);
	
	public long truncate(Long timestamp) {
		Calendar timestampCalendar = Calendar.getInstance();	//performance expensive
		timestampCalendar.setTimeInMillis(timestamp);
		truncate(timestampCalendar);
		return timestampCalendar.getTimeInMillis();
	}	
	
	private static void truncateByMinute(Calendar datetime){
		datetime.set(Calendar.SECOND, 0);
		datetime.set(Calendar.MILLISECOND, 0);		
	}
	
	private static void truncateByHour(Calendar datetime){
		truncateByMinute(datetime);
		datetime.set(Calendar.MINUTE, 0);		
	}
	
	private static void truncateByDay(Calendar datetime){
		truncateByHour(datetime);
		datetime.set(Calendar.HOUR, 0);		
	}

}
