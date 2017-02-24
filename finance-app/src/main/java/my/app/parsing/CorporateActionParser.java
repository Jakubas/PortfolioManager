package my.app.parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import my.app.domains.Stock;
import my.app.domains.corporateactions.Dividend;
import my.app.domains.corporateactions.StockSplit;

public class CorporateActionParser {

	private CorporateActionParser() {}
	
	public List<Dividend> parseCSVToDividends(Stock stock, String filePath) {
		BufferedReader reader = null;
		List<Dividend> dividends = new ArrayList<Dividend>();
		
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = reader.readLine()) != null) {
				Dividend dividend;
				try {
					dividend = parseDividend(stock, line);
					
				} catch (Exception e) {
					//there is some invalid data in this row so we don't add it our list of stocks
					continue;
				}
				if (dividend == null) {
					continue;
				}
				dividends.add(dividend);
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
		return dividends;
	}

	public List<StockSplit> parseCSVToStockSplits(Stock stock, String filePath) {
		BufferedReader reader = null;
		List<StockSplit> stockSplits = new ArrayList<StockSplit>();
		
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = reader.readLine()) != null) {
				StockSplit stockSplit;
				try {
					stockSplit = parseStockSplit(stock, line);
					
				} catch (Exception e) {
					//there is some invalid data in this row so we don't add it our list of stocks
					continue;
				}
				if (stockSplit == null) {
					continue;
				}
				stockSplits.add(stockSplit);
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
		return stockSplits;
	}
	
	private Dividend parseDividend(Stock stock, String line) throws Exception {
		String[] lines = CSVParser.splitLine(line);
		
		int i = 0;	
		String type = lines[i++];
		if (type.equals("DIVIDEND")) {
			LocalDate date = CSVParser.parseDateWithoutDashes(lines[i++]);
			double amount = CSVParser.parseDouble(lines[i++]);
			Dividend dividend = new Dividend(stock, date, amount);
			return dividend;
		}
		return null;
	}
	
	private StockSplit parseStockSplit(Stock stock, String line) throws Exception {
		String[] lines = CSVParser.splitLine(line);
		
		int i = 0;	
		String type = lines[i++];
		if (type.equals("SPLIT")) {
			LocalDate date = CSVParser.parseDateWithoutDashes(lines[i++]);
			String split = lines[i++];
			StockSplit stockSplit = new StockSplit(stock, date, split);
			return stockSplit;
		}
		return null;
	}
}
