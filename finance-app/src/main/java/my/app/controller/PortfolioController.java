package my.app.controller;

import java.security.Principal;
import java.text.ParseException;
import java.time.DateTimeException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
			@RequestParam("amount") int amount,
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
		StockInPortfolio sip = new StockInPortfolio(stock, user, amount, buyDate);
		stockInPortfolioService.saveStockInPortfolio(sip);
		return "redirect:stocks";
	}
	
	@RequestMapping(value = "portfolio/{stockInPortfolioId}", method = RequestMethod.GET)
	public String updateStockInPortfolio(Model model,
			@PathVariable("stockInPortfolioId") int id) {
		StockInPortfolio sip = stockInPortfolioService.getStockInPortfolioById(id);
		model.addAttribute("stockInPortfolio", sip);
		return "updatePortfolio";
	}
			
	@RequestMapping(value = "portfolio/{stockInPortfolioId}", method = RequestMethod.PUT)
	public String updateStockInPortfolio(RedirectAttributes ra, Principal principal,
			@PathVariable("stockInPortfolioId") int id,
			@RequestParam("amount") int amount,
			@RequestParam("buyDate") String buyDateStr,
			@RequestParam(value = "sellDate", required = false) String sellDateStr,
			@RequestParam("buyPrice") Double buyPrice,
			@RequestParam(value = "sellPrice", required = false) Double sellPrice) {
		StockInPortfolio sip = stockInPortfolioService.getStockInPortfolioById(id);
		Date buyDate;
		Date sellDate = null;
		try {
			buyDate = Utility.stringToDate(buyDateStr);
			if (!sellDateStr.isEmpty())
				sellDate = Utility.stringToDate(sellDateStr);
		} catch (ParseException e) {
			ra.addAttribute("dateError", true);
			return "redirect:/portfolio/{stockInPortfolioId}";
		}
		
		Double prevBuyPrice = sip.getBuyPrice();
		try {
			sip.setBuyDate(buyDate);
			sip.setSellDate(sellDate);
		} catch (DateTimeException e) {
			ra.addFlashAttribute("dateError2", true);
			return "redirect:/portfolio/{stockInPortfolioId}";
		}
		sip.setAmount(amount);
		if (!buyPrice.equals(prevBuyPrice)) {
			sip.setBuyPrice(buyPrice);
		}
		if (sellPrice == null || !sellPrice.equals(sip.getSellPrice())) {
			sip.setSellPrice(sellPrice);
		}
		stockInPortfolioService.updateStockInPortfolio(sip);
		return "redirect:/portfolio";
	}
	
	@RequestMapping(value = "portfolio/{stockInPortfolioId}", method = RequestMethod.DELETE)
	public String deleteStockInPortfolio(Model model, Principal principal,
			@PathVariable("stockInPortfolioId") int id) {
		
		StockInPortfolio sip = stockInPortfolioService.getStockInPortfolioById(id);
		stockInPortfolioService.deleteStockInPortfolio(sip);
		
		return getPortfolio(model, principal);
	}
}
