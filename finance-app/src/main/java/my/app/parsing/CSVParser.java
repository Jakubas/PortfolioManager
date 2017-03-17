package my.app.parsing;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class CSVParser {

	private CSVParser() {}
	
	//This method splits a line from a comma separated values file into an array of strings
	public static String[] splitLine(String line) {
		
		StringBuffer buffer = new StringBuffer();
		ArrayList<String> stockStrs = new ArrayList<String>();
		
		char[] chars = line.toCharArray();
		boolean inQuotes = false;
		for (char ch: chars) {
			if (inQuotes) {
				if (ch == '"') {
					inQuotes = false;
				} else {
					buffer.append(ch);
				}
			} else {
				if (ch == '"') {
					inQuotes = true;
				} else if (ch == ',') {
					stockStrs.add(buffer.toString());
					buffer.delete(0, buffer.length());
				} else {
					buffer.append(ch);
				}
			}
		}
		stockStrs.add(buffer.toString());
	
		String[] stockStrsArray = stockStrs.toArray(new String[stockStrs.size()]);
		return stockStrsArray;
	}
	
	public static LocalDate parseDate(String dateStr) throws Exception {
		String datePattern = "([0-9]+)-([0-9]+)-([0-9]+)";  
		boolean match = Pattern.matches(datePattern, dateStr);
		if (match) {
			LocalDate date =  LocalDate.parse(dateStr);
			return date;
		} else {
			throw new Exception();
		}
	}
	
	public static LocalDate parseDateWithoutDashes(String dateStr) throws Exception {
		String datePattern = "([0-9]+)";
		dateStr = dateStr.trim();
		boolean match = Pattern.matches(datePattern, dateStr);
		if (match && dateStr.length() == 8) {
			//yyyyMMdd to yyyy-MM-dd
			dateStr = new StringBuilder(dateStr).insert(4, "-").insert(7, "-").toString();
			LocalDate date =  LocalDate.parse(dateStr);
			return date;
		} else {
			throw new Exception();
		}
	}
	
	public static boolean isDouble(String doubleStr) {
		String doublePattern = "([0-9]+)(.?)([0-9]+)?";
		return Pattern.matches(doublePattern, doubleStr);
	}
	
	public static double parseDouble(String doubleStr) throws Exception {
		if (isDouble(doubleStr)) {
			return Double.valueOf(doubleStr);
		} else {
			throw new Exception();
		}
	}
	
	public static int parseInt(String intStr) throws Exception {
		String intPattern = "([0-9]+)";
		boolean match = Pattern.matches(intPattern, intStr);
		if (match) {
			return Integer.valueOf(intStr);
		} else {
			throw new Exception();
		}
	}
	
	public static long parseLong(String longStr) throws Exception {
		String intPattern = "([0-9]+)";
		boolean match = Pattern.matches(intPattern, longStr);
		if (match) {
			return Long.parseLong(longStr);
		} else {
			throw new Exception();
		}
	}
}
