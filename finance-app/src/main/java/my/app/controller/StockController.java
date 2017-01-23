package my.app.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import my.app.domain.Stock;
import my.app.service.StockService;

@RestController
public class StockController {

	private final StockService stockService;
	
	@Autowired
	public StockController(StockService stockService) {
		this.stockService = stockService;
	}
	
	@RequestMapping(value = "/stocks/{id}", method = RequestMethod.GET, produces = "application/json")
	public String getStock(@PathVariable("id") int id) {
		Stock stock = stockService.getStockById(id);
	    JSONObject json = new JSONObject();
	    json.put("id", stock.getId());
	    json.put("name", stock.getName());
	    json.put("ticker", stock.getTicker());
	    json.put("industry", stock.getIndustry());
		return json.toString();
	}
	
	@RequestMapping(value = "/stocks", method = RequestMethod.GET)
	public List<?> getStocks() {
		List<Stock> stocks = stockService.getStocks();
		
		List<Map<?,?>> jsonStrings = new ArrayList<Map<?,?>>();
		for (int i = 0; i < stocks.size(); i++) {
			Stock stock = stocks.get(i);
		    JSONObject json = new JSONObject();
		    json.put("id", stock.getId());
		    json.put("name", stock.getName());
		    json.put("ticker", stock.getTicker());
		    json.put("industry", stock.getIndustry());
			jsonStrings.add(json.toMap());
		}
		return jsonStrings;
	}
}
