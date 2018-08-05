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
import my.app.updatedatabase.CorporateActionUpdater;
import my.app.updatedatabase.PortfolioDailyInformationUpdater;
import my.app.updatedatabase.StockInformationUpdater;
import my.app.updatedatabase.StockMetricsUpdater;

@Controller
public class AdminController {

	private final PortfolioDailyInformationUpdater portfolioDailyInformationUpdater;
	private final StockInformationUpdater stockInformationUpdater;
	private final StockMetricsUpdater stockMetricsUpdater;
	private final CorporateActionUpdater corporateActionUpdater;

	@Autowired
	public AdminController(PortfolioDailyInformationUpdater portfolioDailyInformationUpdater, StockInformationUpdater stockInformationUpdater,
						   StockMetricsUpdater stockMetricsUpdater, CorporateActionUpdater corporateActionUpdater) {
		this.portfolioDailyInformationUpdater = portfolioDailyInformationUpdater;
		this.stockInformationUpdater = stockInformationUpdater;
		this.stockMetricsUpdater = stockMetricsUpdater;
		this.corporateActionUpdater = corporateActionUpdater;
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String getAdminPage() {
		return "admin";
	}
	
	@RequestMapping(value = "/admin/updatePortfolioDailyInformation", method = RequestMethod.POST)
	public String updatePortfolioDailyInformation() {
		portfolioDailyInformationUpdater.updatePortfolioDailyInformation();
		return "redirect:/admin";
	}
	
	@RequestMapping(value = "/admin/updateStockInformation", method = RequestMethod.POST)
	public String updateStockInformation() {
		stockInformationUpdater.updateStockDailyInformation(true, true);
		return "redirect:/admin";
	}

	@RequestMapping(value = "/admin/updateCorporateActions", method = RequestMethod.POST)
	public String updateCorporateActions() {
		corporateActionUpdater.updateCorporateActions();
		return "redirect:/admin";
	}
	
	@RequestMapping(value = "/admin/updateClosingPrices", method = RequestMethod.POST)
	public String updateClosingPrices() {
		stockInformationUpdater.updateClosingPrices();
		return "redirect:/admin";
	}

	@RequestMapping(value = "/admin/updateStockMetrics", method = RequestMethod.POST)
	public String updateStockMetrics() {
		stockMetricsUpdater.updateStockMetrics();
		return "redirect:/admin";
	}
}
