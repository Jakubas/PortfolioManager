package my.app.dao;

import java.util.List;

import my.app.domain.StockDailyInformation;

public interface StockDailyInformationDAO {
	
	void saveStockInformation(StockDailyInformation stockInformation);
	void saveStockInformations(List<StockDailyInformation> stockInformation);
	StockDailyInformation getStockInformationById(int id);
	List<StockDailyInformation> getStockInformations();
	List<StockDailyInformation> getStockInformationsByStockId(int stockId);
	void updateStockInformation(StockDailyInformation stockInformation);
	void deleteStockInformation(StockDailyInformation stockInformation);
	
}
