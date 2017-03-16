package my.app.controllers.portfoliocontrollers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import my.app.cashflow.CashFlow;
import my.app.domains.portfolio.PortfolioDailyInformation;
import my.app.domains.portfolio.StockInPortfolio;
import my.app.domains.user.User;
import my.app.services.portfolio.PortfolioService;
import my.app.services.portfolio.StockInPortfolioService;
import my.app.services.stock.StockService;
import my.app.services.user.UserService;

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
		double[] sectorValues =  new double[sectors.size()];
		for (int i = 0; i < sectors.size(); i++) {
			sectorValues[i] = user.sectorValue(sectors.get(i));
		}
		model.addAttribute("sectors", sectors);
		model.addAttribute("sectorValues", sectorValues);
		model.addAttribute("dayValues", getDayValues(user));
		model.addAttribute("dates", getDates(user));
		model.addAttribute("cashFlow", getCashFlow(user));
		return "portfolio/metrics";
	}

	public double[] getDayValues(User user) {
		List<PortfolioDailyInformation> pids = user.getPortfolioDailyInformations();
		pids.sort(Comparator.comparing(PortfolioDailyInformation::getDate));
		double[] values = new double[pids.size()];
		for (int i = 0; i < pids.size(); i++) {
			PortfolioDailyInformation pid = pids.get(i);
			double value = pid.getValue();
			values[i] = value;
		}
		return values;
	}
	
	public String[] getDates(User user) {
		List<PortfolioDailyInformation> pids = user.getPortfolioDailyInformations();
		pids.sort(Comparator.comparing(PortfolioDailyInformation::getDate));
		String[] dates = new String[pids.size()];
		for (int i = 0; i < pids.size(); i++) {
			PortfolioDailyInformation pid = pids.get(i);
			String value = pid.getDate().toString();
			dates[i] = value;
		}
		return dates;
	}
	
	private List<CashFlow> getCashFlow(User user) {
		List<CashFlow> cashFlows = new ArrayList<CashFlow>();
		List<StockInPortfolio> portfolio = user.getActivePortfolio();
		for (StockInPortfolio holding : portfolio) {
			cashFlows.add(new CashFlow(holding, true));
			cashFlows.add(new CashFlow(holding, false));
		}
		cashFlows.sort(Comparator.comparing(CashFlow::getDate));
		return cashFlows;
	}
}
