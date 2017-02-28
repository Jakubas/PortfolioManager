package my.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.app.dao.StockDailyInformationDAO;
import my.app.domains.StockDailyInformation;

@Service
public class StockDailyInformationServiceImpl implements StockDailyInformationService {

	private final StockDailyInformationDAO dao;
	
	@Autowired
	public StockDailyInformationServiceImpl(StockDailyInformationDAO dao) {
		this.dao = dao;
	}
	
	public void saveStockInformation(StockDailyInformation stockInformation) {
		dao.saveStockInformation(stockInformation);
	}
	
	public void saveStockInformations(List<StockDailyInformation> stockInformations) {
		dao.saveStockInformations(stockInformations);
	}

	public StockDailyInformation getStockInformationById(int id) {
		StockDailyInformation stockInformation = dao.getStockInformationById(id);
		return stockInformation;
	}

	public List<StockDailyInformation> getStockInformations() {
		List<StockDailyInformation> stockInformations = dao.getStockInformations();
		return stockInformations;
	}

	public void updateStockInformation(StockDailyInformation stockInformation) {
		dao.updateStockInformation(stockInformation);
	}
	
	public void updateStockInformations(List<StockDailyInformation> stockInformations) {
		dao.updateStockInformations(stockInformations);
	}

	public void deleteStockInformation(StockDailyInformation stockInformation) {
		dao.deleteStockInformation(stockInformation);
	}

	public List<StockDailyInformation> getStockInformationsByStockId(int stockId) {
		List<StockDailyInformation> stockInformations = dao.getStockInformationsByStockId(stockId);
		return stockInformations;
	}

	@Override
	public void deleteStockInformations(List<StockDailyInformation> stockInformations) {
		dao.deleteStockInformations(stockInformations);
	}
}
