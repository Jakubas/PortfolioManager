package my.app.update_database;

import java.util.List;

import my.app.domain.Stock;
import my.app.domain.StockMetrics;
import my.app.service.StockMetricsService;
import my.app.service.StockService;
import my.app.stock_calculations.StockDataCalculations;

public class UpdateStockCalculations {
	
	private final StockMetricsService stockMetricsService;
	private final StockService stockService;
	
	public UpdateStockCalculations(StockMetricsService stockMetricsService, StockService stockService) {
		this.stockMetricsService = stockMetricsService;
		this.stockService = stockService;
	}
	
	public void updateStockCalculations() {
		List<Stock> stocks = stockService.getStocks();
		for (Stock stock : stocks) {
			System.out.println(stock.getId());
			Double quarterlyAnnualisedReturn = StockDataCalculations.calculateQuarterlyAnnualisedReturn(stock);
			Double oneYearAnnualisedReturn = StockDataCalculations.calculate1YrAnnualisedReturn(stock);
			Double fiveYearAnnualisedReturn = StockDataCalculations.calculate5YrAnnualisedReturn(stock);
			Double tenYearAnnualisedReturn = StockDataCalculations.calculate10YrAnnualisedReturn(stock);
			Double variance = StockDataCalculations.calculateVariance(stock);
			
			StockMetrics stockMetrics = 
					new StockMetrics(stock, quarterlyAnnualisedReturn, oneYearAnnualisedReturn, 
							fiveYearAnnualisedReturn, tenYearAnnualisedReturn, variance);
			
			StockMetrics prevStockMetrics = 
					stockMetricsService.getStockMetricsById(stock.getId());
			if (prevStockMetrics != null &&
				!stockMetrics.getThreeMonthAnnualisedReturn().equals(prevStockMetrics.getThreeMonthAnnualisedReturn())) {
				stockMetricsService.deleteStockMetrics(prevStockMetrics);
				stockMetricsService.saveStockMetrics(stockMetrics);
			} else if(prevStockMetrics == null) {
				stockMetricsService.saveStockMetrics(stockMetrics);
			}
		}
	}
}
