package my.app.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import my.app.domains.stock.Stock;
import my.app.domains.stock.StockDailyInformation;
import my.app.services.stock.StockService;

@Controller
public class StockController {

	private final StockService stockService;
	
	@Autowired
	public StockController(StockService stockService) {
		this.stockService = stockService;
	}
	
	@RequestMapping(value = "stocks", method=RequestMethod.GET)
	public String getStocks(Model model) {
		List<Stock> stocks = stockService.getStocks();
		model.addAttribute("stocks", stocks);
		return "stocks";
	}
	
	@RequestMapping(value = "stocks/{id}", method=RequestMethod.GET)
	public String getStockInformation(@PathVariable("id") int id, Model model) {
		Stock stock = stockService.getStockById(id);
		List<StockDailyInformation> sdis = stock.getStockDailyInformations();
		sdis.sort(Comparator.comparing(StockDailyInformation::getDate));
		List<String> dates = getDates(sdis);
		List<Double> prices = getPrices(sdis);
		model.addAttribute("stock", stock);
		model.addAttribute("dates", dates);
		model.addAttribute("prices", prices);
		model.addAttribute("stockMetrics", stock.getStockMetrics());
		return "stockInformation";
	}
	
	private List<String> getDates(List<StockDailyInformation> sdis) {
		List<String> dates = new ArrayList<String>();
		for (StockDailyInformation sdi : sdis) {
			dates.add(sdi.getDate().toString());
		}
		return dates;
	}
	
	private List<Double> getPrices(List<StockDailyInformation> sdis) {
		List<Double> prices = new ArrayList<Double>();
		for (StockDailyInformation sdi : sdis) {
			prices.add(sdi.getAdjCloseStockSplits());
		}
		return prices;
	}
}
