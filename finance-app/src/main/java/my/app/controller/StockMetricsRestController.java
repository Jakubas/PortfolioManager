package my.app.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import my.app.domain.StockMetrics;
import my.app.service.StockMetricsService;

@RestController
public class StockMetricsRestController {
	
	private final StockMetricsService stockMetricsService;
	
	@Autowired
	public StockMetricsRestController(StockMetricsService stockMetricsService) {
		this.stockMetricsService = stockMetricsService;
	}
	
	@RequestMapping(value = "api/stocks/{stock_id}/calculatedData", method = RequestMethod.GET, produces = "application/json")
	public String getstockMetrics(@PathVariable("stock_id") int stockId) {
		StockMetrics stockMetrics = stockMetricsService.getStockMetricsById(stockId);
	    JSONObject json = new JSONObject();
	    json.put("stockId", stockMetrics.getStockId());
	    json.put("quarterlyAnnualisedReturn", stockMetrics.getQuarterlyAnnualisedReturn());
	    json.put("oneYearAnnualisedReturn", stockMetrics.getOneYearAnnualisedReturn());
	    json.put("fiveYearAnnualisedReturn", stockMetrics.getFiveYearAnnualisedReturn());
	    json.put("tenYearAnnualisedReturn", stockMetrics.getTenYearAnnualisedReturn());
	    json.put("variance", stockMetrics.getVariance());
	    return json.toString();
	}
}
