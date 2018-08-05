package my.app.updatedatabase;

import java.util.List;

import my.app.domains.stock.Stock;
import my.app.parsing.StockParser;
import my.app.parsing.StockParserTwo;
import my.app.updatedatabase.download.StockInformationDownloader;

public class StockInformationReader {

	public static List<Stock> parseBaseStockInformation(String targetDir, boolean downloadCSVs) {
		String filePath = targetDir + "stock_base_info.csv";
		if (downloadCSVs) {
			StockInformationDownloader.downloadBaseStockInformation(filePath);
		}
		StockParser stockParser = new StockParser();
		StockParserTwo stockParserTwo = new StockParserTwo();
		List<Stock> stocks = stockParser.parseCSVToStocks(filePath);
		stocks = stockParserTwo.parseAdditionalStockData(stocks, targetDir, downloadCSVs);
		return stocks;
	}
}
