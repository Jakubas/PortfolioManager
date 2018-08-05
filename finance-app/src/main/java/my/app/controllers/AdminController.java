package my.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import my.app.services.corporateactions.DividendService;
import my.app.services.corporateactions.StockSplitService;
import my.app.services.portfolio.PortfolioDailyInformationService;
import my.app.services.stock.IndexDailyInformationService;
import my.app.services.stock.IndexService;
import my.app.services.stock.StockDailyInformationService;
import my.app.services.stock.StockService;
import my.app.services.user.UserService;
import my.app.updatedatabase.UpdateCorporateActions;
import my.app.updatedatabase.UpdatePortfolioDailyInformation;
import my.app.updatedatabase.UpdateStockInformation;
import my.app.updatedatabase.StockMetricsUpdater;

@Controller
public class AdminController {

	private final PortfolioDailyInformationService pdiService;
	private final UserService userService;
	private final DividendService dividendService;
	private final StockSplitService stockSplitService;
	private final StockService stockService;
	private final StockDailyInformationService stockDailyInformationService;
	private final IndexService indexService;
	private final IndexDailyInformationService indexDailyInformationService;
	private final StockMetricsUpdater stockMetricsUpdater;

	@Autowired
	public AdminController(PortfolioDailyInformationService pdiService, UserService userService,
			DividendService dividendService, StockSplitService stockSplitService, StockService stockService,
			StockDailyInformationService stockDailyInformationService, IndexService indexService,
		    IndexDailyInformationService indexDailyInformationService, StockMetricsUpdater stockMetricsUpdater) {
		this.pdiService = pdiService;
		this.userService = userService;
		this.dividendService = dividendService;
		this.stockSplitService = stockSplitService;
		this.stockService = stockService;
		this.stockDailyInformationService = stockDailyInformationService;
		this.indexService = indexService;
		this.indexDailyInformationService = indexDailyInformationService;
		this.stockMetricsUpdater = stockMetricsUpdater;
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String getAdminPage() {
		return "admin";
	}
	
	@RequestMapping(value = "/admin/updatePortfolioDailyInformation", method = RequestMethod.POST)
	public String updatePortfolioDailyInformation() {
		UpdatePortfolioDailyInformation updi = new UpdatePortfolioDailyInformation(pdiService, userService);
		updi.updatePortfolioDailyInformation();
		return "redirect:/admin";
	}
	
	@RequestMapping(value = "/admin/updateStockInformation", method = RequestMethod.POST)
	public String updateStockInformation() {
		String rootDir = "/home/daniel/fyp/data/";
		UpdateStockInformation usi = 
				new UpdateStockInformation(rootDir, stockDailyInformationService, stockService, indexService, indexDailyInformationService);
		usi.updateStockDailyInformation(true, true);
		return "redirect:/admin";
	}
	
	@RequestMapping(value = "/admin/updateCorporateActions", method = RequestMethod.POST)
	public String updateCorporateActions() {
		String rootDir = "/home/daniel/fyp/data/";
		UpdateCorporateActions uca = 
				new UpdateCorporateActions(rootDir, dividendService, stockSplitService, stockService);
		uca.updateCorporateActions(true);
		return "redirect:/admin";
	}
	
	@RequestMapping(value = "/admin/updateClosingPrices", method = RequestMethod.POST)
	public String updateClosingPrices() {
		String rootDir = "/home/daniel/fyp/data/";
		UpdateStockInformation usi = 
				new UpdateStockInformation(rootDir, stockDailyInformationService, stockService, indexService, indexDailyInformationService);
		usi.updateClosingPrices();
		return "redirect:/admin";
	}

	@RequestMapping(value = "/admin/updateStockMetrics", method = RequestMethod.POST)
	public String updateStockMetrics() {
		stockMetricsUpdater.updateStockMetrics();
		return "redirect:/admin";
	}
}
