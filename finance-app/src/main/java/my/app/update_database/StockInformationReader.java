package my.app.update_database;

import java.util.List;

import my.app.domain.Stock;
import my.app.parsing.StockParser;
import my.app.parsing.StockParserTwo;

public class StockInformationReader {

	public static List<Stock> retrieveBaseStockInformation(String rootDir) {
		String filePath = rootDir + "stock_base_info.csv";
		StockParser stockParser = new StockParser();
		StockParserTwo stockParserTwo = new StockParserTwo();
		List<Stock> stocks = stockParser.parseCSVToStocks(filePath);
		stocks = stockParserTwo.parseAdditionalStockData(stocks);
		return stocks;
	}
}
