package my.app.updatedatabase;

import java.util.List;

import my.app.domains.Stock;
import my.app.parsing.StockParser;
import my.app.parsing.StockParserTwo;

public class StockInformationReader {

	public static List<Stock> retrieveBaseStockInformation(String rootDir, boolean downloadCSVs) {
		String filePath = rootDir + "stock_base_info.csv";
		StockParser stockParser = new StockParser();
		StockParserTwo stockParserTwo = new StockParserTwo();
		List<Stock> stocks = stockParser.parseCSVToStocks(filePath);
		stocks = stockParserTwo.parseAdditionalStockData(stocks, rootDir, downloadCSVs);
		return stocks;
	}
}
