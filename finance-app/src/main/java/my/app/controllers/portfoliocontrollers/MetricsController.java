package my.app.controllers.portfoliocontrollers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import my.app.cashflow.HoldingTransaction;
import my.app.domains.portfolio.PortfolioDailyInformation;
import my.app.domains.portfolio.StockInPortfolio;
import my.app.domains.stock.IndexDailyInformation;
import my.app.domains.user.User;
import my.app.services.portfolio.PortfolioDailyInformationService;
import my.app.services.portfolio.PortfolioService;
import my.app.services.portfolio.StockInPortfolioService;
import my.app.services.stock.IndexDailyInformationService;
import my.app.services.stock.StockService;
import my.app.services.user.UserService;
import my.app.updatedatabase.UpdatePortfolioDailyInformation;
import my.app.utilities.DateUtility;

//Portfolio metrics
@Controller
public class MetricsController {

	private final StockInPortfolioService stockInPortfolioService;
	private final UserService userService;
	private final StockService stockService;
	private final PortfolioService portfolioService;
	private final IndexDailyInformationService indexDailyInformationService;
	private final PortfolioDailyInformationService portfolioDailyInformationService;
	
	@Autowired
	public MetricsController(StockInPortfolioService stockInPortfolioService, 
			UserService userService, StockService stockService, PortfolioService portfolioService,
			IndexDailyInformationService indexDailyInformationService, 
			PortfolioDailyInformationService portfolioDailyInformationService) {
		this.stockInPortfolioService = stockInPortfolioService;
		this.userService = userService;
		this.stockService = stockService;
		this.portfolioService = portfolioService;
		this.indexDailyInformationService = indexDailyInformationService;
		this.portfolioDailyInformationService = portfolioDailyInformationService;
	}
	
	@RequestMapping(value = "portfolio/metrics", method = RequestMethod.GET)
	public String getPortfolioMetrics(Model model, Principal principal) {
		String username = principal.getName();
		User user = userService.getUserByUsername(username);
		List<String> sectors = stockService.getSectors();
		boolean ignoreCash = false;
		if (user.getCash() < 0) {
			sectors.remove("Cash");
			ignoreCash = true;
		}
		double[] sectorValues =  new double[sectors.size()];
		for (int i = 0; i < sectors.size(); i++) {
			if (!ignoreCash) {
				sectorValues[i] = user.calculateSectorWeight(sectors.get(i));
			} else {
				sectorValues[i] = user.calculateSectorWeightWithoutCash(sectors.get(i));
			}
		}
		List<HoldingTransaction> transactions = getTransactions(user);
		model.addAttribute("sectors", sectors);
		model.addAttribute("sectorValues", sectorValues);
		model.addAttribute("dayValues", getDayValues(user));
		model.addAttribute("dates", getDates(user));
		model.addAttribute("transactions", transactions);
		model.addAttribute("indexValues", getIndexValues(user, transactions));
		model.addAttribute("isData", !user.getPortfolio().isEmpty());
		UpdatePortfolioDailyInformation updi = new UpdatePortfolioDailyInformation(portfolioDailyInformationService, userService);
		updi.updatePortfolioDailyInformationFor(user);
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
	
	private double[] getIndexValues(User user, List<HoldingTransaction> transactions) {
		List<IndexDailyInformation> idis = indexDailyInformationService.getIndexDailyInformations();
		List<PortfolioDailyInformation> pdis = user.getPortfolioDailyInformations();
		pdis.sort(Comparator.comparing(PortfolioDailyInformation::getDate));
		idis.sort(Comparator.comparing(IndexDailyInformation::getDate));
		if (pdis == null || pdis.size() < 2) {
			return null;
		}
		LocalDate earliestDate = pdis.get(0).getDate();
		LocalDate latestDate = pdis.get(pdis.size()-1).getDate();
		idis.removeIf(o -> o.getDate().isBefore(earliestDate));
		idis.removeIf(o -> o.getDate().isAfter(latestDate));
		double[] indexValues = new double[pdis.size()];
		transactions.sort(Comparator.comparing(HoldingTransaction::getDate));
		//fill the gaps inbetween dates;
		List<IndexDailyInformation> idisToAdd = new ArrayList<IndexDailyInformation>();
		for (int i = 0; i < idis.size()-1; i++) {
			IndexDailyInformation idi = idis.get(i);
			IndexDailyInformation idi2 = idis.get(i+1);
			LocalDate date = idi.getDate();
			LocalDate date2 = idi2.getDate();
			while (DateUtility.daysBetween(date, date2) > 1) {
				IndexDailyInformation idiToAdd = new IndexDailyInformation();
				idiToAdd.setValuesFrom(idi);
				date = date.plusDays(1);
				idiToAdd.setDate(date);
				idisToAdd.add(idiToAdd);
			}
		}
		while (idis.size() + idisToAdd.size() < pdis.size()) {
			IndexDailyInformation idi = idis.get(idis.size()-1);
			LocalDate date = idi.getDate();
			IndexDailyInformation idiToAdd = new IndexDailyInformation();
			idiToAdd.setValuesFrom(idi);
			date = date.plusDays(1);
			idiToAdd.setDate(date);
			idisToAdd.add(idiToAdd);
		}
		idis.addAll(idisToAdd);
		idis.sort(Comparator.comparing(IndexDailyInformation::getDate));
		double shareAmount = 0;
		int j = 0;
		for (int i = 0; i < indexValues.length; i++) {
			IndexDailyInformation idi = idis.get(i);
			if (transactions != null && j < transactions.size()) {
				HoldingTransaction transaction = transactions.get(j);
				while (j < transactions.size() && 
						(transaction.getDate().isBefore(idi.getDate()) || 
						 transaction.getDate().isEqual(idi.getDate()))) {
					if (transaction.isBuy()) {
						shareAmount += (transaction.getValue() / idi.getClose());
					} else {
						shareAmount -= (transaction.getValue() / idi.getClose());
					}
					j++;
					if (j >= transactions.size()) {
						break;
					}
					transaction = transactions.get(j);
				}
			}
			indexValues[i] = shareAmount * idi.getClose();
		} 
		return indexValues;
	}
	
	private List<HoldingTransaction> getTransactions(User user) {
		List<HoldingTransaction> transactions = new ArrayList<HoldingTransaction>();
		List<StockInPortfolio> portfolio = user.getPortfolio();
		for (StockInPortfolio holding : portfolio) {
			if (holding.getAmount() > 0) {
				transactions.add(new HoldingTransaction(holding, true));
				if (holding.isStockSold()) {
					transactions.add(new HoldingTransaction(holding, false));
				}
			}
		}
		transactions.sort(Comparator.comparing(HoldingTransaction::getDate));
		return transactions;
	}
}
