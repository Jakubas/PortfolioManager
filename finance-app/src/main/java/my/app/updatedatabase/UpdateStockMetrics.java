package my.app.updatedatabase;

import java.util.List;

import my.app.domains.Stock;
import my.app.domains.StockMetrics;
import my.app.risk.Risk;
import my.app.services.StockMetricsService;
import my.app.services.StockService;
import my.app.stockcalculations.StockDataCalculations;

public class UpdateStockMetrics {
	
	private final StockMetricsService stockMetricsService;
	private final StockService stockService;
	
	public UpdateStockMetrics(StockMetricsService stockMetricsService, StockService stockService) {
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
			Double variance = Risk.calculateVariance(stock);
			
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
