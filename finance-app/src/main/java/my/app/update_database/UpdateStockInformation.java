package my.app.update_database;

import java.util.ArrayList;
import java.util.List;

import my.app.domain.Stock;
import my.app.domain.StockDailyInformation;
import my.app.parsing.HistoricalDataParser;
import my.app.service.StockDailyInformationService;
import my.app.service.StockService;


public class UpdateStockInformation {
	
	private final StockDailyInformationService stockInformationService;
	private final StockService stockService;
	
	public UpdateStockInformation(StockDailyInformationService stockInformationService, StockService stockService) {
		this.stockInformationService = stockInformationService;
		this.stockService = stockService;
	}
	
	//rootDir = "/home/daniel/fyp/data/"
	public void updateStocks(String rootDir, boolean downloadCSVs) {
		List<Stock> stocks = StockInformationReader.retrieveBaseStockInformation(rootDir, downloadCSVs);
		List<Stock> stocksInDatabase = stockService.getStocks();
		ArrayList<String> tickersInDatabase = stocksToTickers(stocksInDatabase);
		
		int i = 0;
		for(Stock stock : stocks) {
			System.out.println(i++);
			Stock currentStock = stockService.getStockByTicker(stock.getTicker());
			
			if (tickersInDatabase == null ||
				!tickersInDatabase.contains(stock.getTicker())) {
				stockService.saveStock(stock);
			} else if (!stock.getMarketCap().equals(currentStock.getMarketCap()) ||
					   stock.getLastTradePrice() != currentStock.getLastTradePrice()) {
				stockService.updateStock(stock);
			}
		}
	}

	public void updateStockInformation(String rootDir, boolean downloadCSVs) {
		updateStocks(rootDir, downloadCSVs);
		
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
		List<StockDailyInformation> stockInformations = 
				hdp.parseCSVToStockInformation(stock, stockInformationFilePath);
		
		//retrieve historical data on the stock that is currently in the database
		List<StockDailyInformation> stockInformationsInDatabase = 
				stockInformationService.getStockInformationsByStockId(stock.getId());
		//if the data is not in the database, add it
		ArrayList<StockDailyInformation> stockInformations2 = new ArrayList<StockDailyInformation>();
		for(StockDailyInformation stockInformation: stockInformations) {
			
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
