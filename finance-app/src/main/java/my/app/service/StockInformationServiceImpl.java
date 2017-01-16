package my.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.app.dao.StockInformationDAO;
import my.app.domain.StockInformation;

@Service
public class StockInformationServiceImpl implements StockInformationService {

	private final StockInformationDAO dao;
	
	@Autowired
	public StockInformationServiceImpl(StockInformationDAO dao) {
		this.dao = dao;
	}
	
	public void saveStockInformation(StockInformation stockInformation) {
		dao.saveStockInformation(stockInformation);
	}

	public StockInformation getStockInformationById(int id) {
		StockInformation stockInformation = dao.getStockInformationById(id);
		return stockInformation;
	}

	public List<StockInformation> getStockInformations() {
		List<StockInformation> stockInformations = dao.getStockInformations();
		return stockInformations;
	}

	public void updateStockInformation(StockInformation stockInformation) {
		dao.updateStockInformation(stockInformation);
	}

	public void deleteStockInformation(StockInformation stockInformation) {
		dao.deleteStockInformation(stockInformation);
	}

	public List<StockInformation> getStockInformationsByStockId(int stockId) {
		List<StockInformation> stockInformations = dao.getStockInformationsByStockId(stockId);
		return stockInformations;
	}
}
