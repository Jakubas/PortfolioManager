package my.app.updatedatabase;

import java.util.List;

import my.app.domains.stock.Stock;
import my.app.parsing.StockParser;
import my.app.parsing.StockParserTwo;

public class StockInformationReader {

	public static List<Stock> parseBaseStockInformation(String rootDir, boolean downloadCSVs) {
		String filePath = rootDir + "stock_base_info.csv";
		if (downloadCSVs) {
			StockInformationDownloader.downloadBaseStockInformation(filePath);
		}
		StockParser stockParser = new StockParser();
		StockParserTwo stockParserTwo = new StockParserTwo();
		List<Stock> stocks = stockParser.parseCSVToStocks(filePath);
		stocks = stockParserTwo.parseAdditionalStockData(stocks, rootDir, downloadCSVs);
		return stocks;
	}
}
