package my.app.updatedatabase.download;

import java.util.List;

import my.app.domains.stock.Stock;
import my.app.utilities.DownloadUtility;

public class StockInformationDownloader {
    //TODO: this data source is throwing a 403
    static final String INDEX_CONSTITUENTS_DATA_SOURCE = "http://data.okfn.org/data/core/s-and-p-500-companies/r/constituents.csv";
    static final String HISTORICAL_STOCK_DATA_SOURCE_PREFIX = "http://ichart.finance.yahoo.com/table.csv?d=1&e=1&f=2099&g=d&a=1&b=1&c=1800&ignore=.csv&s=";

    public static void downloadStockDailyInformation(List<Stock> stocks) {
		int i = 1;
		for(Stock stock : stocks) {
			downloadHistoricalStockInformation(stock.getTicker());
			System.out.println(i++ + "/" + stocks.size() + " historical data CSVs downloaded");
		}
	}
	
	//downloads a file that contains the stock ticker symbol, stock name and sector
	public static void downloadBaseStockInformation(String filePath) {
		String url = INDEX_CONSTITUENTS_DATA_SOURCE;
		DownloadUtility.downloadFile(url, filePath);
	}
	
	public static void downloadHistoricalStockInformation(String ticker) {
		String urlPrefix = HISTORICAL_STOCK_DATA_SOURCE_PREFIX;
		//TODO: replace with /tmp, also make it an argument that is passed in e.g. targetDir
		DownloadUtility.downloadFile(urlPrefix + ticker, "/home/daniel/fyp/data/" + ticker + ".csv");
	}
}
