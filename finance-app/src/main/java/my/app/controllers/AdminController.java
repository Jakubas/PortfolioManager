package my.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import my.app.services.PortfolioDailyInformationService;
import my.app.services.StockDailyInformationService;
import my.app.services.StockService;
import my.app.services.UserService;
import my.app.services.corporateactions.DividendService;
import my.app.services.corporateactions.StockSplitService;
import my.app.updatedatabase.UpdateCorporateActions;
import my.app.updatedatabase.UpdatePortfolioDailyInformation;
import my.app.updatedatabase.UpdateStockInformation;

@Controller
public class AdminController {

	PortfolioDailyInformationService pdiService;
	UserService userService;
	DividendService dividendService;
	StockSplitService stockSplitService;
	StockService stockService;
	StockDailyInformationService stockDailyInformationService;
	
	@Autowired
	public AdminController(PortfolioDailyInformationService pdiService, UserService userService,
			DividendService dividendService, StockSplitService stockSplitService, StockService stockService,
			StockDailyInformationService stockDailyInformationService) {
		this.pdiService = pdiService;
		this.userService = userService;
		this.dividendService = dividendService;
		this.stockSplitService = stockSplitService;
		this.stockService = stockService;
		this.stockDailyInformationService = stockDailyInformationService;
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
				new UpdateStockInformation(rootDir, stockDailyInformationService, stockService);
		usi.updateStockDailyInformation(true, true);
		return "redirect:/admin";
	}
	
	@RequestMapping(value = "/admin/updateCorporateActions", method = RequestMethod.POST)
	public String updateCorporateActions() {
		String rootDir = "/home/daniel/fyp/data/";
		UpdateCorporateActions uca = 
				new UpdateCorporateActions(rootDir, dividendService, stockSplitService, stockService);
		uca.updateCorporateActions(false);
		return "redirect:/admin";
	}
}
