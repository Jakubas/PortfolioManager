package my.app.parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import my.app.domains.stock.Index;
import my.app.domains.stock.IndexDailyInformation;
import my.app.domains.stock.StockDailyInformation;

public class IndexDataParser {
	
	private IndexDataParser() {}
	
	public static List<IndexDailyInformation> parseCSVToIndexInformation(Index index, String fileName) {
		
		BufferedReader reader = null;
		List<IndexDailyInformation> idis = new ArrayList<IndexDailyInformation>();
		
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				IndexDailyInformation indexDailyInformation;
				try {
					indexDailyInformation = parseIndexDailyInformation(index, line);
					
				} catch (Exception e) {
					//there is some invalid data in this row so we don't add it our list of stocks
					continue;
				}
				idis.add(indexDailyInformation);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Could not parse due to input/output error");	
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.out.println("Could not close reader");
				}
			}
		}
		return idis;
	}
	
	private static IndexDailyInformation parseIndexDailyInformation(Index index, String line) throws Exception {
		String[] lines = CSVParser.splitLine(line);
		
		int i = 0;		
		LocalDate date = CSVParser.parseDate(lines[i++]);
		double open = CSVParser.parseDouble(lines[i++]);
		double high = CSVParser.parseDouble(lines[i++]);
		double low = CSVParser.parseDouble(lines[i++]);
		double close = CSVParser.parseDouble(lines[i++]);
		long volume = CSVParser.parseLong(lines[i++]);
		double adjustedClose = CSVParser.parseDouble(lines[i++]);
		
		IndexDailyInformation indexDailyInformation = 
				new IndexDailyInformation(date, open, close, high, low, volume, adjustedClose, index);
		return indexDailyInformation;
	}
}

