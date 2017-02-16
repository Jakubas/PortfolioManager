package my.app.services;

import java.util.List;

import my.app.domains.StockDailyInformation;

public interface StockDailyInformationService {

	void saveStockInformation(StockDailyInformation stockInformation);
	void saveStockInformations(List<StockDailyInformation> stockInformation);
	StockDailyInformation getStockInformationById(int id);
	List<StockDailyInformation> getStockInformations();
	List<StockDailyInformation> getStockInformationsByStockId(int stockId);
	void updateStockInformation(StockDailyInformation stockInformation);
	void deleteStockInformation(StockDailyInformation stockInformation);
}
