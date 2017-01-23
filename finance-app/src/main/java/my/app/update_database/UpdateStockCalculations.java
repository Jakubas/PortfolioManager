package my.app.update_database;

import java.util.List;

import my.app.domain.Stock;
import my.app.domain.StockCalculatedData;
import my.app.service.StockCalculatedDataService;
import my.app.service.StockService;
import my.app.stock_calculations.StockDataCalculations;

public class UpdateStockCalculations {
	
	private final StockCalculatedDataService stockCalculatedDataService;
	private final StockService stockService;
	
	public UpdateStockCalculations(StockCalculatedDataService stockCalculatedDataService, StockService stockService) {
		this.stockCalculatedDataService = stockCalculatedDataService;
		this.stockService = stockService;
	}
	
	public void updateStockCalculations() {
		StockDataCalculations stockDataCalculations = new StockDataCalculations();
		
		List<Stock> stocks = stockService.getStocks();
		for (Stock stock : stocks) {
			System.out.println(stock.getId());
			Double quarterlyAnnualisedReturn = stockDataCalculations.calculateQuarterlyAnnualisedReturn(stock);
			Double oneYearAnnualisedReturn = stockDataCalculations.calculate1YrAnnualisedReturn(stock);
			Double fiveYearAnnualisedReturn = stockDataCalculations.calculate5YrAnnualisedReturn(stock);
			Double tenYearAnnualisedReturn = stockDataCalculations.calculate10YrAnnualisedReturn(stock);
			Double variance = stockDataCalculations.calculateVariance(stock);
			
			StockCalculatedData stockCalculatedData = 
					new StockCalculatedData(stock, quarterlyAnnualisedReturn, oneYearAnnualisedReturn, 
							fiveYearAnnualisedReturn, tenYearAnnualisedReturn, variance);
			
			StockCalculatedData prevStockCalculatedData = 
					stockCalculatedDataService.getStockCalculatedDataById(stock.getId());
			if (prevStockCalculatedData != null &&
				!stockCalculatedData.getVariance().equals(prevStockCalculatedData.getVariance())) {
				stockCalculatedDataService.deleteStockCalculatedData(prevStockCalculatedData);
				stockCalculatedDataService.saveStockCalculatedData(stockCalculatedData);
			} else if(prevStockCalculatedData == null) {
				stockCalculatedDataService.saveStockCalculatedData(stockCalculatedData);
			}
		}
	}
}
