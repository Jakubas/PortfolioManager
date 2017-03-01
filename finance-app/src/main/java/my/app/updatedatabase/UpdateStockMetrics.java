package my.app.updatedatabase;

import java.util.List;

import my.app.domains.stock.Stock;
import my.app.domains.stock.StockMetrics;
import my.app.risk.Risk;
import my.app.services.stock.StockMetricsService;
import my.app.services.stock.StockService;
import my.app.stockcalculations.StockDataCalculations;

public class UpdateStockMetrics {
	
	private final StockMetricsService stockMetricsService;
	private final StockService stockService;
	
	public UpdateStockMetrics(StockMetricsService stockMetricsService, StockService stockService) {
		this.stockMetricsService = stockMetricsService;
		this.stockService = stockService;
	}
	
	public void updateStockMetrics() {
		List<Stock> stocks = stockService.getStocks();
		//runs out of heap space/becomes very slow around ~400 objects
		for (int i = 0; i < stocks.size(); i++) {
			Stock stock = stocks.get(i);
			updateStockMetricsFor(stock);
			System.out.println((i+1) + " / " + stocks.size() + " stock metrics updated");
		}
	}
	
	public void updateStockMetricsFor(Stock stock) {
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
		if (prevStockMetrics != null && !stockMetrics.equals(prevStockMetrics)) {
			prevStockMetrics.setValuesFrom(stockMetrics);
			stockMetricsService.updateStockMetrics(prevStockMetrics);
			System.out.println("updated");
		} else if(prevStockMetrics == null) {
			stockMetricsService.saveStockMetrics(stockMetrics);
			System.out.println("saved");
		}
	}
}
