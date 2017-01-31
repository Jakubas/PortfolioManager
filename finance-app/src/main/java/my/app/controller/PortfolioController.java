package my.app.controller;

import java.security.Principal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import my.app.domain.Stock;
import my.app.domain.StockInPortfolio;
import my.app.domain.User;
import my.app.service.StockInPortfolioService;
import my.app.service.StockService;
import my.app.service.UserService;

@Controller
public class PortfolioController {

	private final StockInPortfolioService stockInPortfolioService;
	private final UserService userService;
	private final StockService stockService;
	
	@Autowired
	public PortfolioController(StockInPortfolioService stockInPortfolioService, UserService userService, StockService stockService) {
		this.stockInPortfolioService = stockInPortfolioService;
		this.userService = userService;
		this.stockService = stockService;
	}
	
	@RequestMapping(value = "portfolio", method = RequestMethod.GET)
	public String getPortfolio(Model model, Principal principal) {
		String username = principal.getName();
		User user = userService.getUserByUsername(username);
		List<StockInPortfolio> portfolio = user.getPortfolio();
		model.addAttribute("portfolio", portfolio);
		return "portfolio";
	}
	
	@RequestMapping(value = "portfolio", method = RequestMethod.POST)
	public String addToPortfolio(Model model, Principal principal,
			@RequestParam("stockId") int stockId,
			@RequestParam(value = "buyDate", required = false) String buyDateStr) {
		
		String username = principal.getName();
		User user = userService.getUserByUsername(username); 
		Stock stock = stockService.getStockById(stockId);
		Date buyDate;
		if (buyDateStr != null) {
			try {
				buyDate = Utility.stringToDate(buyDateStr);
			} catch (ParseException e) {
				model.addAttribute("dateError", true);
				return "stocks";
			}
		} else {
			buyDate = new Date();
		}
		StockInPortfolio sip = new StockInPortfolio(stock, user, buyDate);
		stockInPortfolioService.saveStockInPortfolio(sip);
		return "redirect:/stocks";
	}
	
	@RequestMapping(value = "portfolio/{stockInPortfolioId}", method = RequestMethod.PUT)
	public String updateStockInPortfolio(Model model, Principal principal,
			@PathVariable("stockInPortfolioId") int id,
			@RequestParam("buyDate") String buyDateStr,
			@RequestParam("sellDate") String sellDateStr,
			@RequestParam("buyPrice") Double buyPrice,
			@RequestParam("sellPrice") Double sellPrice) {
		
		StockInPortfolio sip = stockInPortfolioService.getStockInPortfolioById(id);
		Date buyDate;
		Date sellDate;
		try {
			buyDate = Utility.stringToDate(buyDateStr);
			sellDate = Utility.stringToDate(sellDateStr);
		} catch (ParseException e) {
			model.addAttribute("dateError", true);
			return "portfolio/" + id;
		}
		sip.setBuyDate(buyDate);
		sip.setSellDate(sellDate);
		sip.setBuyPrice(buyPrice);
		sip.setSellPrice(sellPrice);
		stockInPortfolioService.updateStockInPortfolio(sip);
		return "redirect:/portfolio";
	}
}
