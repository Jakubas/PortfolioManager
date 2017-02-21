package my.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import my.app.services.PortfolioDailyInformationService;
import my.app.services.UserService;
import my.app.updatedatabase.UpdatePortfolioDailyInformation;

@Controller
public class AdminController {

	PortfolioDailyInformationService pdiService;
	UserService userService;
	@Autowired
	public AdminController(PortfolioDailyInformationService pdiService, UserService userService) {
		this.pdiService = pdiService;
		this.userService = userService;
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
}
