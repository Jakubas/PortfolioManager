package my.app.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
	
	@RequestMapping(value = "/stocks/{id}", method = RequestMethod.GET)
	public @ResponseBody Stock getStock(@PathVariable("id") int id) {
		Stock stock = stockService.getStockById(id);
		stock.setStockCalculatedData(null);
		stock.setStockDailyInformations(null);
		return stock;
	}
	
	@RequestMapping(value = "/stocks", method = RequestMethod.GET)
	public List<?> getStocks() {
		List<Stock> stocks = stockService.getStocks();
		for (int i = 0; i < stocks.size(); i++) {
			Stock stock = stocks.get(i);
			stock.setStockCalculatedData(null);
			stock.setStockDailyInformations(null);
			stocks.set(i, stock);
		}
		return stocks;
	}
}
