package my.app.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import my.app.domain.StockCalculatedData;
import my.app.service.StockCalculatedDataService;

@RestController
public class StockCalculatedDataRestController {
	
	private final StockCalculatedDataService stockCalculatedDataService;
	
	@Autowired
	public StockCalculatedDataRestController(StockCalculatedDataService stockCalculatedDataService) {
		this.stockCalculatedDataService = stockCalculatedDataService;
	}
	
	@RequestMapping(value = "api/stocks/{stock_id}/calculatedData", method = RequestMethod.GET, produces = "application/json")
	public String getStockCalculatedData(@PathVariable("stock_id") int stockId) {
		StockCalculatedData stockCalculatedData = stockCalculatedDataService.getStockCalculatedDataById(stockId);
	    JSONObject json = new JSONObject();
	    json.put("stockId", stockCalculatedData.getStockId());
	    json.put("quarterlyAnnualisedReturn", stockCalculatedData.getQuarterlyAnnualisedReturn());
	    json.put("oneYearAnnualisedReturn", stockCalculatedData.getOneYearAnnualisedReturn());
	    json.put("fiveYearAnnualisedReturn", stockCalculatedData.getFiveYearAnnualisedReturn());
	    json.put("tenYearAnnualisedReturn", stockCalculatedData.getTenYearAnnualisedReturn());
	    json.put("variance", stockCalculatedData.getVariance());
	    return json.toString();
	}
}
