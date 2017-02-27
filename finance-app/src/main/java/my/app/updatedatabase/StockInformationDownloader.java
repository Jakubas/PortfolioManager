package my.app.updatedatabase;

import java.util.List;

import my.app.domains.Stock;

public class StockInformationDownloader {
	
	public static void downloadStockDailyInformation(List<Stock> stocks) {
		int i = 1;
		for(Stock stock : stocks) {
			downloadHistoricalStockInformation(stock.getTicker());
			System.out.println(i++ + "/" + stocks.size() + " historical data CSVs downloaded");
		}
	}
	
	//downloads a file that contains the stock ticker symbol, stock name and sector
	public static void downloadBaseStockInformation(String filePath) {
		String url = "http://data.okfn.org/data/core/s-and-p-500-companies/r/constituents.csv";
		DownloadUtility.downloadFile(url, filePath);
	}
	
	public static void downloadHistoricalStockInformation(String stockTicker) {
		String urlPrefix = "http://ichart.finance.yahoo.com/table.csv?d=1&e=1&f=2099&g=d&a=1&b=1&c=1800&ignore=.csv&s=";
		DownloadUtility.downloadFile(urlPrefix + stockTicker, "/home/daniel/fyp/data/" + stockTicker + ".csv");
	}
}
