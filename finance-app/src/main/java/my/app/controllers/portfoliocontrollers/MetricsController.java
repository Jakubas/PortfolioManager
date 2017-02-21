package my.app.controllers.portfoliocontrollers;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import my.app.controllers.Utility;
import my.app.domains.StockInPortfolio;
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
		List<String> sectors = stockService.getSectors();
		double[] weights =  new double[sectors.size()];
		for (int i = 0; i < sectors.size(); i++) {
			weights[i] = user.calculateSectorWeight(sectors.get(i));
		}
		double[] dayValues = getDayValues(user);
		model.addAttribute("sectors", sectors);
		model.addAttribute("weights", weights);
		model.addAttribute("dayValues", dayValues);
		return "portfolio/metrics";
	}
	
	public double[] getDayValues(User user) {
		List<StockInPortfolio> portfolio = user.getPortfolio(); 
		LocalDate earliestDate = portfolioService.getEarliestDateIn(portfolio);
		int daysBetween = Period.between(earliestDate, LocalDate.now()).getDays();
		double[] values = new double[daysBetween/28];
		for (int i = 0; i < values.length; i++) {
			System.out.println(i + " / " + values.length);
			double value = portfolioService.getValueOnDate(portfolio, earliestDate.plusDays(i*28));
			values[i] = value;
		}
		return values;
	}
}
