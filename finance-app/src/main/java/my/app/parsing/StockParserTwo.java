package my.app.parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import my.app.domain.Stock;

public class StockParserTwo {

	public List<Stock> parseAdditionalStockData(List<Stock> stocks) {
		List<Stock> stocks2 = new ArrayList<Stock>();
		for (Stock stock : stocks) {
			String ticker = stock.getTicker();
			String filePath = "/home/daniel/fyp/data/" + ticker + "_current.csv";
			downloadCSV(stock.getTicker(), filePath);
			
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(filePath));
				String line;
				if ((line = reader.readLine()) != null) {
					try {
						stock = addStockInformation(line, stock);
						stocks2.add(stock);
					} catch (Exception e) {
						System.out.println("could not parse additional information about stock");
					}
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
		}	
		return stocks2;
	}

	private Stock addStockInformation(String line, Stock stock) throws Exception {
		int i = 0;
		CSVParser parser = new CSVParser();
		String[] stockStrs = parser.splitLine(line);
		stock.setLastTradePrice(parser.parseDouble(stockStrs[i++]));
		stock.setPERatio(parser.parseDouble(stockStrs[i++]));
		stock.setMarketCap(stockStrs[i++]);
		return stock;
	}

	//used to download additional information about a stock,
	//currently only downloading market cap, last trade price and P/E ratio
	private void downloadCSV(String ticker, String filePath) {
		String urlPrefix = "http://download.finance.yahoo.com/d/quotes.csv?s=";
		String urlSuffix = "&f=l1rj1&e=.csv";
		try {
			URL url = new URL(urlPrefix + ticker + urlSuffix);
			File file = new File(filePath);
			FileUtils.copyURLToFile(url, file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
