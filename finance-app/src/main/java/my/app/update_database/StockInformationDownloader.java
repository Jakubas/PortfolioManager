package my.app.update_database;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;

import my.app.domain.Stock;
import my.app.parsing.StockParser;

public class StockInformationDownloader {
	
	public static void downloadStockInformation(String rootDir) {
		//"/home/daniel/fyp/data/stock_base_info.csv"
		String filePath = rootDir + "stock_base_info.csv";
		downloadBaseStockInformation(filePath);
		
		StockParser stockParser = new StockParser();
		List<Stock> stocks = stockParser.parseCSVToStocks(filePath);
		int i = 1;
		for(Stock stock : stocks) {
			downloadHistoricalStockInformation(stock.getTicker());
			System.out.println(i++ + "/" + stocks.size() + " CSVs downloaded");
		}
	}
	
	//downloads a file that contains the stock ticker symbol, stock name and industry
	public static void downloadBaseStockInformation(String filePath) {
		try {
			URL url = new URL("http://data.okfn.org/data/core/s-and-p-500-companies/r/constituents.csv");
			File file = new File(filePath);
			FileUtils.copyURLToFile(url, file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void downloadHistoricalStockInformation(String stockTicker) {
		String urlPrefix = "http://ichart.finance.yahoo.com/table.csv?d=1&e=1&f=2099&g=d&a=1&b=1&c=1800&ignore=.csv&s=";
		try {
			URL url = new URL(urlPrefix + stockTicker);
			File file = new File("/home/daniel/fyp/data/" + stockTicker + ".csv");
			FileUtils.copyURLToFile(url, file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
