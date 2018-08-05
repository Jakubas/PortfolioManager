package my.app.updatedatabase;

import java.util.List;

import my.app.domains.stock.Stock;
import my.app.domains.stock.StockMetrics;
import my.app.risk.Risk;
import my.app.services.stock.StockMetricsService;
import my.app.services.stock.StockService;
import my.app.stockcalculations.StockDataCalculations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class StockMetricsUpdater {
	
	private final StockMetricsService stockMetricsService;
	private final StockService stockService;

	@Autowired
	public StockMetricsUpdater(StockMetricsService stockMetricsService, StockService stockService) {
		this.stockMetricsService = stockMetricsService;
		this.stockService = stockService;
	}
	
	public void updateStockMetrics() {
		List<Stock> stocks = stockService.getStocks();
		//runs out of heap space/becomes very slow after ~400 iterations
		for (int i = 0; i < stocks.size(); i++) {
			Stock stock = stocks.get(i);
			updateStockMetricsFor(stock);
			System.out.println((i+1) + " / " + stocks.size() + " stock metrics updated");
		}
	}
	
	private void updateStockMetricsFor(Stock stock) {
		Double threeMonthAnnualisedReturn = StockDataCalculations.calculateQuarterlyAnnualisedReturn(stock);
		Double oneYearAnnualisedReturn = StockDataCalculations.calculate1YrAnnualisedReturn(stock);
		Double threeYearAnnualisedReturn = StockDataCalculations.calculate3YrAnnualisedReturn(stock);
		Double fiveYearAnnualisedReturn = StockDataCalculations.calculate5YrAnnualisedReturn(stock);
		Double tenYearAnnualisedReturn = StockDataCalculations.calculate10YrAnnualisedReturn(stock);
		Double threeMonthVariance = Risk.calculate3MonthVariance(stock);
		Double oneYearVariance = Risk.calculate1YearVariance(stock);
		Double threeYearVariance = Risk.calculate3YearVariance(stock);
		Double fiveYearVariance = Risk.calculate5YearVariance(stock);
		Double tenYearVariance = Risk.calculate10YearVariance(stock);
		StockMetrics stockMetrics = 
				new StockMetrics(stock, threeMonthAnnualisedReturn, oneYearAnnualisedReturn, threeYearAnnualisedReturn, 
						fiveYearAnnualisedReturn, tenYearAnnualisedReturn, threeMonthVariance, oneYearVariance, 
						threeYearVariance, fiveYearVariance, tenYearVariance);
		
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
