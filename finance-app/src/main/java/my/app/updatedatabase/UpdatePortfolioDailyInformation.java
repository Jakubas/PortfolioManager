package my.app.updatedatabase;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import my.app.domains.User;
import my.app.domains.portfolio.PortfolioDailyInformation;
import my.app.domains.portfolio.StockInPortfolio;
import my.app.services.PortfolioDailyInformationService;
import my.app.services.PortfolioService;
import my.app.services.PortfolioServiceImpl;
import my.app.services.UserService;

public class UpdatePortfolioDailyInformation {
	
	private final PortfolioDailyInformationService pdiService;
	private final UserService userService;
	
	public UpdatePortfolioDailyInformation(PortfolioDailyInformationService pdiService, UserService userService) {
		this.pdiService = pdiService;
		this.userService = userService;
	}
	
	public void updatePortfolioDailyInformation() {
		PortfolioService portfolioService = new PortfolioServiceImpl();
		
		List<User> users = userService.getUsers();
		int j = 0;
		for (User user : users) {
			List<PortfolioDailyInformation> pdis = new ArrayList<PortfolioDailyInformation>();
			List<StockInPortfolio> portfolio = user.getPortfolio();
			LocalDate earliestDate = portfolioService.getEarliestDateIn(portfolio);
			int daysBetween = Period.between(earliestDate, LocalDate.now()).getDays();
			for (int i = 0; i < daysBetween; i++) {
				LocalDate date = earliestDate.plusDays(i);
				PortfolioDailyInformation pdi = new PortfolioDailyInformation(user, date);
				pdis.add(pdi);
				System.out.println("Day: " + i + " / " + daysBetween);
			}
			System.out.println("User: " + j++ + " / " + users.size());
			pdiService.savePortfolioDailyInformations(pdis);
		}
	}
}
