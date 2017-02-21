package my.app.parsing;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class CSVParser {

	//This method splits a line from a comma separated values file into an array of strings
	public String[] splitLine(String line) {
		
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
	
	public LocalDate parseDate(String dateStr) throws Exception {
		String datePattern = "([0-9]+)-([0-9]+)-([0-9]+)";  
		boolean match = Pattern.matches(datePattern, dateStr);
		if (match) {
			LocalDate date =  LocalDate.parse(dateStr);
			return date;
		} else {
			throw new Exception();
		}
	}
	
	public boolean isDouble(String doubleStr) {
		String doublePattern = "([0-9]+)(.?)([0-9]+)?";
		return Pattern.matches(doublePattern, doubleStr);
	}
	
	public double parseDouble(String doubleStr) throws Exception {
		if (isDouble(doubleStr)) {
			return Double.valueOf(doubleStr);
		} else {
			throw new Exception();
		}
	}
	
	public int parseInt(String intStr) throws Exception {
		String intPattern = "([0-9]+)";
		boolean match = Pattern.matches(intPattern, intStr);
		if (match) {
			return Integer.valueOf(intStr);
		} else {
			throw new Exception();
		}
	}
}
