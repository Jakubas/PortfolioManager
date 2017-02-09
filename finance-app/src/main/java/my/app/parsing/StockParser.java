package my.app.parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import my.app.domain.Stock;

public class StockParser {
	
	public ArrayList<Stock> parseCSVToStocks(String fileName) {
		
		BufferedReader reader = null;
		ArrayList<Stock> stocks = new ArrayList<Stock>();
		
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				Stock stock;
				try {
					stock = parseStock(line);
				} catch (Exception e) {
					//there is some invalid data in this row so we don't add it our list of stocks
					continue;
				}
				stocks.add(stock);
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
		return stocks;
	}

	private Stock parseStock(String line) throws Exception {
		int i = 0;
//		String[] stockStr = line.split(",");
		CSVParser parser = new CSVParser();
		String[] stockStrs = parser.splitLine(line);
		String ticker = parseTicker(stockStrs[i++]);
		String name = stockStrs[i++];
		String sector = stockStrs[i];
		Stock stock = new Stock(name, ticker, sector);
		return stock;
	}
	
	private String parseTicker(String ticker) throws Exception {
		String tickerPattern = "([A-Z]+)";  
		boolean match = Pattern.matches(tickerPattern, ticker);
		if (match) {
			return ticker;
		} else {
			throw new Exception();
		}
	}
}
