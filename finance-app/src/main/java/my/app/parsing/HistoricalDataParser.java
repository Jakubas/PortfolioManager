package my.app.parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import my.app.domain.Stock;
import my.app.domain.StockDailyInformation;

public class HistoricalDataParser {
	
	public ArrayList<StockDailyInformation> parseCSVToStockInformation(Stock stock, String fileName) {
		
		BufferedReader reader = null;
		ArrayList<StockDailyInformation> stockInformations = new ArrayList<StockDailyInformation>();
		
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				StockDailyInformation stockInformation;
				try {
					stockInformation = parseStockInformation(stock, line);
					
				} catch (Exception e) {
					//there is some invalid data in this row so we don't add it our list of stocks
					continue;
				}
				stockInformations.add(stockInformation);
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
		return stockInformations;
	}
	
	private StockDailyInformation parseStockInformation(Stock stock, String line) throws Exception {
		CSVParser parser = new CSVParser();
		String[] lines = parser.splitLine(line);
		
		int i = 0;		
		Date date = parseDate(lines[i++]);
		double open = parseDouble(lines[i++]);
		double high = parseDouble(lines[i++]);
		double low = parseDouble(lines[i++]);
		double close = parseDouble(lines[i++]);
		int volume = parseInt(lines[i++]);
		double adjustedClose = parseDouble(lines[i++]);
		
		StockDailyInformation stockInformation = 
				new StockDailyInformation(date, open, close, high, low, volume, adjustedClose, stock);
		return stockInformation;
	}
	
	private Date parseDate(String dateStr) throws Exception {
		String datePattern = "([0-9]+)-([0-9]+)-([0-9]+)";  
		boolean match = Pattern.matches(datePattern, dateStr);
		if (match) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse(dateStr);
			return date;
		} else {
			throw new Exception();
		}
	}
	
	private boolean isDouble(String doubleStr) {
		String doublePattern = "([0-9]+)(.?)([0-9]+)?";
		return Pattern.matches(doublePattern, doubleStr);
	}
	
	private double parseDouble(String doubleStr) throws Exception {
		if (isDouble(doubleStr)) {
			return Double.valueOf(doubleStr);
		} else {
			throw new Exception();
		}
	}
	
	private int parseInt(String intStr) throws Exception {
		String intPattern = "([0-9]+)";
		boolean match = Pattern.matches(intPattern, intStr);
		if (match) {
			return Integer.valueOf(intStr);
		} else {
			throw new Exception();
		}
	}
}
