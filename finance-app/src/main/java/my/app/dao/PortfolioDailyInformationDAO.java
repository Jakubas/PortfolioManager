package my.app.dao;

import java.time.LocalDate;
import java.util.List;

import my.app.domains.portfolio.PortfolioDailyInformation;

public interface PortfolioDailyInformationDAO {

	void savePortfolioDailyInformation(PortfolioDailyInformation pdi);
	void savePortfolioDailyInformations(List<PortfolioDailyInformation> pdis);
	PortfolioDailyInformation getPortfolioDailyInformationById(int id);
	PortfolioDailyInformation getPortfolioDailyInformationByDate(LocalDate date);
	List<PortfolioDailyInformation> getPortfolioDailyInformations();
	void updatePortfolioDailyInformation(PortfolioDailyInformation pdi);
	void deletePortfolioDailyInformation(PortfolioDailyInformation pdi);
}
