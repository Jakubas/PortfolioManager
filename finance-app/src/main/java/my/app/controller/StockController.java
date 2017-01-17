package my.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import my.app.service.StockDailyInformationService;
import my.app.service.StockService;
import my.app.update_database.UpdateStockInformation;

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
		UpdateStockInformation update = new UpdateStockInformation(stockInformationService, stockService);
		update.updateStockInformation("/home/daniel/fyp/data/");
		return "Downloaded";
	}
}
