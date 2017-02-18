package my.app.controllers.portfoliocontrollers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import my.app.domains.User;
import my.app.services.PortfolioService;
import my.app.services.StockInPortfolioService;
import my.app.services.StockService;
import my.app.services.UserService;

//Portfolio metrics
@Controller
public class MetricsController {

	private final StockInPortfolioService stockInPortfolioService;
	private final UserService userService;
	private final StockService stockService;
	private final PortfolioService portfolioService;
	
	@Autowired
	public MetricsController(StockInPortfolioService stockInPortfolioService, 
			UserService userService, StockService stockService, PortfolioService portfolioService) {
		this.stockInPortfolioService = stockInPortfolioService;
		this.userService = userService;
		this.stockService = stockService;
		this.portfolioService = portfolioService;
	}
	
	@RequestMapping(value = "portfolio/metrics", method = RequestMethod.GET)
	public String getPortfolioMetrics(Model model, Principal principal) {
		String username = principal.getName();
		User user = userService.getUserByUsername(username);
//		List<StockInPortfolio> portfolio = user.getPortfolio();
		List<String> sectors = stockService.getSectors();
		double[] weights =  new double[sectors.size()];
		for (int i = 0; i < sectors.size(); i++) {
			weights[i] = user.calculateSectorWeight(sectors.get(i));
		}
		model.addAttribute("sectors", sectors);
		model.addAttribute("weights", weights);
		System.out.println("ss");
		return "portfolio/metrics";
	}
}
