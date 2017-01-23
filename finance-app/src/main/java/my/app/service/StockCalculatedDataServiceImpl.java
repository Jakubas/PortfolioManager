package my.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import my.app.dao.StockCalculatedDataDAOImpl;
import my.app.domain.StockCalculatedData;

@Service
public class StockCalculatedDataServiceImpl implements StockCalculatedDataService {

	private final StockCalculatedDataDAOImpl dao;
	
	public StockCalculatedDataServiceImpl(StockCalculatedDataDAOImpl dao) {
		this.dao = dao;
	}
	
	public void saveStockCalculatedData(StockCalculatedData stockCalculatedData) {
		dao.saveStockCalculatedData(stockCalculatedData);
	}

	public StockCalculatedData getStockCalculatedDataById(int id) {
		return dao.getStockCalculatedDataById(id);
	}

	public List<StockCalculatedData> getStockCalculatedDatas() {
		return dao.getStockCalculatedDatas();
	}

	public void updateStockCalculatedData(StockCalculatedData stockCalculatedData) {
		dao.updateStockCalculatedData(stockCalculatedData);
	}

	public void deleteStockCalculatedData(StockCalculatedData stockCalculatedData) {
		dao.deleteStockCalculatedData(stockCalculatedData);
	}

	public void deleteStockCalculatedDataById(int id) {
		StockCalculatedData stockCalculatedData = dao.getStockCalculatedDataById(id);
		dao.deleteStockCalculatedData(stockCalculatedData);
	}
}
