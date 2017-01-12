package my.app.dao;

import java.util.List;

import my.app.domain.StockInformation;

public interface StockInformationDAO {
	
	void saveStockInformation(StockInformation stockInformation);
	StockInformation getStockInformationById(int id);
	List<StockInformation> getStockInformations();
	void updateStockInformation(StockInformation stockInformation);
	void deleteStockInformation(StockInformation stockInformation);
}
