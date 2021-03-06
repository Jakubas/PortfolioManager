package my.app.utilities;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtility {

	private DateUtility() {
		
	}
	
	public static LocalDate stringToDate(String dateStr) throws ParseException {
		LocalDate date = LocalDate.parse(dateStr);
		return date;
	}
	
	/*
	 * Source: http://www.java2s.com/Tutorials/Java/Data_Type_How_to/Date_Convert/Convert_java_util_Date_to_LocalDate.htm
	 */
	public static LocalDate fromDate(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
	    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
	}
	
	public static Date toDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static long daysBetween(LocalDate startDate, LocalDate endDate) {
		long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
	    return daysBetween;
	}
}
