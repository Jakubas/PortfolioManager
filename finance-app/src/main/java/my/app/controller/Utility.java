package my.app.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Utility {

	private Utility() {
		
	}
	
	public static Date stringToDate(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateStr);
		return date;
	}
	

	/*
	 * Source: http://www.java2s.com/Tutorials/Java/Data_Type_How_to/Date_Convert/Convert_java_util_Date_to_LocalDate.htm
	 */
	public static LocalDate fromDate(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
	    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
	}
}
