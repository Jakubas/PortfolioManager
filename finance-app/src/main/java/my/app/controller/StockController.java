package my.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import my.app.domain.Stock;
import my.app.service.StockCalculatedDataService;
import my.app.service.StockService;
import my.app.update_database.UpdateStockInformation;

@Controller
public class StockController {

	private final StockService stockService;
	private final StockCalculatedDataService scdService;
	
	@Autowired
	public StockController(StockService stockService, StockCalculatedDataService scdService) {
		this.stockService = stockService;
		this.scdService = scdService;
	}
	
	@RequestMapping(value = "stocks", method=RequestMethod.GET)
	public String getStocks(Model model) {
		List<Stock> stocks = stockService.getStocks();
		model.addAttribute("stocks", stocks);
		return "stocks";
	}
	
	@RequestMapping(value = "stocks/{id}", method=RequestMethod.GET)
	public String getStockGraph(@PathVariable("id") int id, Model model) {
		Stock stock = stockService.getStockById(id);
		model.addAttribute("stock", stock);
		model.addAttribute("stockCalculatedData", stock.getStockCalculatedData());
		return "stockInformation";
	}
}
