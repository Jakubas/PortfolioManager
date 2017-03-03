package my.app.controllers.portfoliocontrollers;

import java.security.Principal;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import my.app.domains.portfolio.StockInPortfolio;
import my.app.domains.stock.Stock;
import my.app.domains.user.User;
import my.app.services.portfolio.PortfolioService;
import my.app.services.portfolio.StockInPortfolioService;
import my.app.services.stock.StockService;
import my.app.services.user.UserService;
import my.app.utilities.DateUtility;

@Controller
public class PortfolioController {

	private final StockInPortfolioService stockInPortfolioService;
	private final UserService userService;
	private final StockService stockService;
	private final PortfolioService portfolioService;
	
	@Autowired
	public PortfolioController(StockInPortfolioService stockInPortfolioService, 
			UserService userService, StockService stockService, PortfolioService portfolioService) {
		this.stockInPortfolioService = stockInPortfolioService;
		this.userService = userService;
		this.stockService = stockService;
		this.portfolioService = portfolioService;
	}
	
	@RequestMapping(value = "portfolio", method = RequestMethod.GET)
	public String getPortfolio(Model model, Principal principal) {
		String username = principal.getName();
		User user = userService.getUserByUsername(username);
		List<StockInPortfolio> portfolio = user.getActivePortfolio();
		List<List<StockInPortfolio>> groupedPortfolio = portfolioService.groupPortfolio(portfolio);
		List<String> sectors = stockService.getSectors();
		double[] weights =  new double[sectors.size()];
		for (int i = 0; i < sectors.size(); i++) {
			weights[i] = user.calculateSectorWeight(sectors.get(i));
		}
		model.addAttribute("user", user);
		model.addAttribute("groupedPortfolio", groupedPortfolio);
		model.addAttribute("portfolioService", portfolioService);
		model.addAttribute("sectors", sectors);
		model.addAttribute("weights", weights);
		return "portfolio/portfolio";
	}
	
	@RequestMapping(value = "portfolio", method = RequestMethod.POST)
	public String addToPortfolio(RedirectAttributes ra, Principal principal,
			@RequestParam("stockId") int stockId,
			@RequestParam("amount") int amount,
			@RequestParam(value = "buyDate", required = false) String buyDateStr) {
		
		String username = principal.getName();
		User user = userService.getUserByUsername(username); 
		Stock stock = stockService.getStockById(stockId);
		LocalDate buyDate;
		if (buyDateStr != "") {
			try {
				buyDate = DateUtility.stringToDate(buyDateStr);
			} catch (ParseException e) {
				ra.addFlashAttribute("dateError", true);
				return "redirect:/stocks";
			}
		} else {
			buyDate = LocalDate.now();
		}
		StockInPortfolio sip = new StockInPortfolio(stock, user, amount, buyDate);
		stockInPortfolioService.saveStockInPortfolio(sip);
		userService.updateUser(sip.getUser());
		return "redirect:/stocks";
	}
	
	@RequestMapping(value = "portfolio/{stockInPortfolioId}", method = RequestMethod.GET)
	public String updateStockInPortfolio(Model model,
			@PathVariable("stockInPortfolioId") int id) {
		StockInPortfolio sip = stockInPortfolioService.getStockInPortfolioById(id);
		model.addAttribute("stockInPortfolio", sip);
		return "portfolio/updatePortfolio";
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
		LocalDate buyDate;
		LocalDate sellDate = null;
		try {
			buyDate = DateUtility.stringToDate(buyDateStr);
			if (!sellDateStr.isEmpty())
				sellDate = DateUtility.stringToDate(sellDateStr);
		} catch (ParseException e) {
			ra.addAttribute("dateError", true);
			return "redirect:/portfolio/{stockInPortfolioId}";
		}
		
		try {
			sip.setAmount(amount);
			if (sip.getBuyDate().equals(buyDate)) {
				if (Math.abs(buyPrice - sip.getBuyPrice()) >= 0.01) {
					sip.setBuyPrice(buyPrice);
				}
			} else if (Math.abs(buyPrice - sip.getBuyPrice()) >= 0.01) {
				sip.setBuyDate(buyDate);
				sip.setBuyPrice(buyPrice);
			} else {
				//remove this when price is autofilled on client side
				sip.setBuyDateAndPrice(buyDate);
			}
			if (sellDate == null) {
				sip.setSellDate(null);
			} else if (sip.getSellDate() != null && sip.getSellDate().equals(sellDate)) {
				if (sellPrice != null && sip.getSellPrice() != null && 
				    Math.abs(sellPrice - sip.getSellPrice()) >= 0.01) {
					sip.setSellPrice(sellPrice);
				}
			} else if (sellPrice != null && sip.getSellPrice() != null && 
					   Math.abs(sellPrice - sip.getSellPrice()) >= 0.01) {
				sip.setSellDate(buyDate);
				sip.setSellPrice(buyPrice);
			} else {
				//remove this when price is autofilled on client side
				sip.setSellDateAndPrice(buyDate);
			}
		//remove this catch when price is autofilled on client side
		} catch (DateTimeException e) {
			ra.addFlashAttribute("dateError2", true);
			return "redirect:/portfolio/{stockInPortfolioId}";
		}
		stockInPortfolioService.updateStockInPortfolio(sip);
		//updates the user's cash
		userService.updateUser(sip.getUser());
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