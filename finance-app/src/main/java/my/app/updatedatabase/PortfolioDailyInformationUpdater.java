package my.app.updatedatabase;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import my.app.domains.portfolio.PortfolioDailyInformation;
import my.app.domains.portfolio.StockInPortfolio;
import my.app.domains.user.User;
import my.app.services.portfolio.PortfolioDailyInformationService;
import my.app.services.portfolio.PortfolioService;
import my.app.services.portfolio.PortfolioServiceImpl;
import my.app.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PortfolioDailyInformationUpdater {
	
	private final PortfolioDailyInformationService pdiService;
	private final UserService userService;

	@Autowired
	public PortfolioDailyInformationUpdater(PortfolioDailyInformationService pdiService, UserService userService) {
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
		if (!user.isPortfolioInformationUpToDate() && pdisInDatabase != null && !pdisInDatabase.isEmpty()) {
			pdisInDatabase.sort(Comparator.comparing(PortfolioDailyInformation::getDate).reversed());
			LocalDate latestDate = pdisInDatabase.get(0).getDate();
			if (latestDate.isEqual(LocalDate.now())) {
				return;
			}
		}
		LocalDate earliestDate = portfolioService.getEarliestDateIn(portfolio);
		long daysBetween = ChronoUnit.DAYS.between(earliestDate, LocalDate.now());
		deleteOldPortfolioDailyInformation(pdisInDatabase, earliestDate);
		for (int i = 0; i <= daysBetween; i++) {
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
		user.setUpdatePortfolioInformation(false);
		userService.updateUser(user);
	}

	private void deleteOldPortfolioDailyInformation(List<PortfolioDailyInformation> pdisInDatabase,
			LocalDate earliestDate) {
		pdisInDatabase.sort(Comparator.comparing(PortfolioDailyInformation::getDate));
		List<PortfolioDailyInformation> pdisToDelete = new ArrayList<PortfolioDailyInformation>();
		for (PortfolioDailyInformation pdi : pdisInDatabase) {
			if (pdi.getDate().isBefore(earliestDate) ) {
				pdisToDelete.add(pdi);
			}
		}
		pdiService.deletePortfolioDailyInformations(pdisToDelete);
	}
}
