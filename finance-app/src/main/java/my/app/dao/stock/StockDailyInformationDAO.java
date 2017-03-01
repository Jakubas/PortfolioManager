package my.app.dao.stock;

import java.util.List;

import my.app.domains.stock.StockDailyInformation;

public interface StockDailyInformationDAO {
	
	void saveStockInformation(StockDailyInformation stockInformation);
	void saveStockInformations(List<StockDailyInformation> stockInformation);
	StockDailyInformation getStockInformationById(int id);
	List<StockDailyInformation> getStockInformations();
	List<StockDailyInformation> getStockInformationsByStockId(int stockId);
	void updateStockInformation(StockDailyInformation stockInformation);
	void updateStockInformations(List<StockDailyInformation> stockInformations);
	void deleteStockInformation(StockDailyInformation stockInformation);
	void deleteStockInformations(List<StockDailyInformation> stockInformations);
}
