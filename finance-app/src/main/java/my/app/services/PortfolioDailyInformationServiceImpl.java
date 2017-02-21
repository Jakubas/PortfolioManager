package my.app.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.app.dao.PortfolioDailyInformationDAO;
import my.app.domains.portfolio.PortfolioDailyInformation;

@Service
public class PortfolioDailyInformationServiceImpl implements PortfolioDailyInformationService {
	
	private final PortfolioDailyInformationDAO dao;
	
	@Autowired
	public PortfolioDailyInformationServiceImpl(PortfolioDailyInformationDAO dao) {
		this.dao = dao;
	}

	@Override
	public void savePortfolioDailyInformation(PortfolioDailyInformation pdi) {
		dao.savePortfolioDailyInformation(pdi);
	}

	@Override
	public void savePortfolioDailyInformations(List<PortfolioDailyInformation> pdis) {
		dao.savePortfolioDailyInformations(pdis);
	}
	
	@Override
	public PortfolioDailyInformation getPortfolioDailyInformationById(int id) {
		return dao.getPortfolioDailyInformationById(id);
	}

	@Override
	public PortfolioDailyInformation getPortfolioDailyInformationByDate(LocalDate date) {
		return dao.getPortfolioDailyInformationByDate(date);
	}

	@Override
	public List<PortfolioDailyInformation> getPortfolioDailyInformations() {
		return dao.getPortfolioDailyInformations();
	}

	@Override
	public void updatePortfolioDailyInformation(PortfolioDailyInformation pdi) {
		dao.updatePortfolioDailyInformation(pdi);
	}

	@Override
	public void deletePortfolioDailyInformation(PortfolioDailyInformation pdi) {
		dao.deletePortfolioDailyInformation(pdi);
	}
}
