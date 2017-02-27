package my.app.updatedatabase;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		List<User> users = userService.getUsers();
		int k = 0;
		for (User user : users) {
			updatePortfolioDailyInformationFor(user);
			System.out.println("User: " + k++ + " / " + users.size());
		}
	}
	
	public void updatePortfolioDailyInformationFor(User user) {
		PortfolioService portfolioService = new PortfolioServiceImpl();
		List<PortfolioDailyInformation> pdisToSave = new ArrayList<PortfolioDailyInformation>();
		List<PortfolioDailyInformation> pdisToUpdate = new ArrayList<PortfolioDailyInformation>();
		List<StockInPortfolio> portfolio = user.getPortfolio();
		List<PortfolioDailyInformation> pdisInDatabase = user.getPortfolioDailyInformations();
		LocalDate earliestDate = portfolioService.getEarliestDateIn(portfolio);
		long daysBetween = ChronoUnit.DAYS.between(earliestDate, LocalDate.now());
		for (int i = 0; i < daysBetween; i++) {
			LocalDate date = earliestDate.plusDays(i);
			PortfolioDailyInformation pdi = 
					pdisInDatabase.stream().filter(o -> o.getDate().equals(date)).findFirst().orElse(null);
			if (pdi == null) {
				pdi = new PortfolioDailyInformation(user, date, i);
				pdisToSave.add(pdi);
			} else {
				double prevDay = pdi.getDay();
				double prevValue = pdi.getValue();
				pdi.update(i);
				if (prevDay != pdi.getDay() || prevValue !=  pdi.getValue()) {
					pdisToUpdate.add(pdi);
				}
			}	
		}
		pdiService.savePortfolioDailyInformations(pdisToSave);
		pdiService.updatePortfolioDailyInformations(pdisToUpdate);
	}
}
