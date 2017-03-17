package my.app.services.portfolio;

import java.time.LocalDate;
import java.util.List;

import my.app.domains.portfolio.PortfolioDailyInformation;
import my.app.domains.user.User;

public interface PortfolioDailyInformationService {

	void savePortfolioDailyInformation(PortfolioDailyInformation pdi);
	void savePortfolioDailyInformations(List<PortfolioDailyInformation> pdis);
	PortfolioDailyInformation getPortfolioDailyInformationById(int id);
	PortfolioDailyInformation getPortfolioDailyInformationByDate(LocalDate date, User user);
	List<PortfolioDailyInformation> getPortfolioDailyInformations();
	void updatePortfolioDailyInformation(PortfolioDailyInformation pdi);
	void updatePortfolioDailyInformations(List<PortfolioDailyInformation> pdis);
	void deletePortfolioDailyInformation(PortfolioDailyInformation pdi);
	void deletePortfolioDailyInformations(List<PortfolioDailyInformation> pdis);
}
