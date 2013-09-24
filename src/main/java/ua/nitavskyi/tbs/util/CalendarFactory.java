package ua.nitavskyi.tbs.util;

import java.util.Calendar;

import com.google.inject.Singleton;

@Singleton
public class CalendarFactory {
	
	private static int DEFAULT_HOUR_OF_DAY = 12;
	private static int DEFAULT_MINUTE = 0;
	private static int DEFAULT_SECOND = 0;
	
	public Calendar now(){
		return Calendar.getInstance();
	}
	
	public Calendar fromDate(int year, int month, int dayOfMonth){
		return fromDate(year, month, dayOfMonth, DEFAULT_HOUR_OF_DAY, DEFAULT_MINUTE);
	}
	
	public Calendar fromDate(int year, int month, int dayOfMonth, int hourOfDay, int minute){
		return fromDate(year, month, dayOfMonth, hourOfDay, minute, DEFAULT_SECOND);
	}
	
	public Calendar fromDate(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second){
		Calendar timestamp = Calendar.getInstance();
		timestamp.set(year, month, dayOfMonth, hourOfDay, minute, second);
		return timestamp;
	}	

}
