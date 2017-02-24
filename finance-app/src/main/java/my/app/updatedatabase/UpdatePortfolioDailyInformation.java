package my.app.updatedatabase;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
		int k = 0;
		for (User user : users) {
			List<PortfolioDailyInformation> pdis = new ArrayList<PortfolioDailyInformation>();
			List<StockInPortfolio> portfolio = user.getPortfolio();
			LocalDate earliestDate = portfolioService.getEarliestDateIn(portfolio);
			long daysBetween = ChronoUnit.DAYS.between(earliestDate, LocalDate.now());
//			PortfolioDailyInformation pdi = pdiService.getPortfolioDailyInformationByDate(earliestDate, user);
//			System.out.println("final: " + (pdi != null));
			for (int i = 0; i < daysBetween; i++) {
				if (i % 50 == 0) {
					System.out.println("allah");
					pdiService.savePortfolioDailyInformations(pdis);
					System.out.println("akbar");
					pdis.clear();
				}
				LocalDate date = earliestDate.plusDays(i);
				System.out.println("stuck here");
				PortfolioDailyInformation pdi = pdiService.getPortfolioDailyInformationByDate(date, user);
				System.out.println("unstuck");
				if (pdi == null) {
					pdi = new PortfolioDailyInformation(user, date, i);
					System.out.println("Day " + i + ": "+ pdi.getValue());
					pdis.add(pdi);
				} else {
					PortfolioDailyInformation prevPdi = pdi;
					pdi.update(i);
//					System.out.println(!pdi.equals(prevPdi));
					if (!pdi.equals(prevPdi)) {
						pdiService.updatePortfolioDailyInformation(pdi);
					}
					System.out.println("Update Day " + i + ": "+ pdi.getValue());
				}
//				System.out.println("Day: " + i + " / " + daysBetween);
			}
			System.out.println("User: " + k++ + " / " + users.size());
			pdiService.savePortfolioDailyInformations(pdis);
		}
	}
}
