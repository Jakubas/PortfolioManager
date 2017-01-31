package my.app.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

	private Utility() {
		
	}
	
	public static Date stringToDate(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateStr);
		return date;
	}
}
