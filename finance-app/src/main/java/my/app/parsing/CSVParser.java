package my.app.parsing;

import java.util.ArrayList;

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
}
