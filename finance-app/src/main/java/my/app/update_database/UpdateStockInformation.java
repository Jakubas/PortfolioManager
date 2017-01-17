package my.app.update_database;

import java.util.ArrayList;
import java.util.List;

import my.app.domain.Stock;
import my.app.domain.StockInformation;
import my.app.parsing.HistoricalDataParser;
import my.app.service.StockInformationService;
import my.app.service.StockService;


public class UpdateStockInformation {
	
	private final StockInformationService stockInformationService;
	private final StockService stockService;
	
	public UpdateStockInformation(StockInformationService stockInformationService, StockService stockService) {
		this.stockInformationService = stockInformationService;
		this.stockService = stockService;
	}
	
	public void updateStocks(String rootDir) {
		List<Stock> stocks = StockInformationReader.retrieveBaseStockInformation(rootDir);
		List<Stock> stocksInDatabase = stockService.getStocks();
		ArrayList<String> tickersInDatabase = stocksToTickers(stocksInDatabase);
		
		for(Stock stock : stocks) {
			//the stock ticker is not in the database, therefore we add the stock to the database
			if (tickersInDatabase == null ||
				!tickersInDatabase.contains(stock.getTicker())) {
				stockService.saveStock(stock);
			}
		}
	}

	public void updateStockInformation(String rootDir) {
		updateStocks(rootDir);
		
		//parse CSV into stockInformation
		List<Stock> stocks = stockService.getStocks();
		
		for (int i = 0; i < stocks.size(); i++) {
			updateHistoricalPrices(stocks.get(i), rootDir);
			System.out.println(i + "/" + stocks.size() + " CSVs parsed into database");
		}
	}
	
	private void updateHistoricalPrices(Stock stock, String rootDir) {
		
		//parse the CSV file containing the historical stock data into objects
		String stockInformationFilePath = rootDir + stock.getTicker() + ".csv";
		HistoricalDataParser hdp = new HistoricalDataParser();
		List<StockInformation> stockInformations = 
				hdp.parseCSVToStockInformation(stock, stockInformationFilePath);
		
		//retrieve historical data on the stock that is currently in the database
		List<StockInformation> stockInformationsInDatabase = 
				stockInformationService.getStockInformationsByStockId(stock.getId());
		//if the data is not in the database, add it
		ArrayList<StockInformation> stockInformations2 = new ArrayList<StockInformation>();
		for(StockInformation stockInformation: stockInformations) {
			if (stockInformationsInDatabase == null || 
				!stockInformationsInDatabase.contains(stockInformation)) {
				stockInformations2.add(stockInformation);
				if (stockInformations2.size() >= 50) {
					stockInformationService.saveStockInformations(stockInformations2);
					stockInformations2.clear();
				}
			}
		}
		stockInformationService.saveStockInformations(stockInformations2);
	}
	
	//takes a list of stocks and returns a list of stock tickers corresponding to the stocks
	private ArrayList<String> stocksToTickers(List<Stock> stocks) {
		ArrayList<String> tickers = new ArrayList<String>();
		for (Stock stock: stocks) {
			tickers.add(stock.getTicker());
		}
		return tickers;
	}
}
