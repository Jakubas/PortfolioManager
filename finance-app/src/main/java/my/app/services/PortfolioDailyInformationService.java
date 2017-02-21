package my.app.services;

import java.time.LocalDate;
import java.util.List;

import my.app.domains.portfolio.PortfolioDailyInformation;

public interface PortfolioDailyInformationService {

	void savePortfolioDailyInformation(PortfolioDailyInformation pdi);
	PortfolioDailyInformation getPortfolioDailyInformationById(int id);
	PortfolioDailyInformation getPortfolioDailyInformationByDate(LocalDate date);
	List<PortfolioDailyInformation> getPortfolioDailyInformations();
	void updatePortfolioDailyInformation(PortfolioDailyInformation pdi);
	void deletePortfolioDailyInformation(PortfolioDailyInformation pdi);
}
