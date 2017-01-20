package my.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import my.app.domain.Stock;
import my.app.service.StockDailyInformationService;
import my.app.service.StockService;
import my.app.stock_calculations.StockDataCalculations;

@RestController
public class StockController {

	private final StockDailyInformationService stockInformationService;
	private final StockService stockService;
	
	@Autowired
	public StockController(StockDailyInformationService stockInformationService, StockService stockService) {
		this.stockInformationService = stockInformationService;
		this.stockService = stockService;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getBaseStockInformation() {
//		UpdateStockInformation update = new UpdateStockInformation(stockInformationService, stockService);
//		update.updateStockInformation("/home/daniel/fyp/data/");
		StockDataCalculations stockDataCalculations = new StockDataCalculations();
		Stock stock = stockService.getStockById(66);
//		Double ar1 = stockDataCalculations.calculateQuarterlyAnnualisedReturn(stockService.getStockById(1))*100;
//		Double ar2 = stockDataCalculations.calculate1YrAnnualisedReturn(stockService.getStockById(1))*100;
//		Double ar3 = stockDataCalculations.calculate5YrAnnualisedReturn(stockService.getStockById(1))*100;
//		Double ar4 = stockDataCalculations.calculate10YrAnnualisedReturn(stockService.getStockById(1))*100;
//		return ar1.toString() + "%<br />" + ar2.toString() + "%<br />" + ar3.toString() + "%<br />" + ar4.toString() + "%<br />";
		return stockDataCalculations.calculateRisk(stock);
	}
}
