package my.app.parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import my.app.domains.stock.Stock;
import my.app.domains.stock.StockDailyInformation;

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
		String[] lines = CSVParser.splitLine(line);
		
		int i = 0;		
		LocalDate date = CSVParser.parseDate(lines[i++]);
		double open = CSVParser.parseDouble(lines[i++]);
		double high = CSVParser.parseDouble(lines[i++]);
		double low = CSVParser.parseDouble(lines[i++]);
		double close = CSVParser.parseDouble(lines[i++]);
		int volume = CSVParser.parseInt(lines[i++]);
		double adjustedClose = CSVParser.parseDouble(lines[i++]);
		
		StockDailyInformation stockInformation = 
				new StockDailyInformation(date, open, close, high, low, volume, adjustedClose, stock);
		return stockInformation;
	}
}
