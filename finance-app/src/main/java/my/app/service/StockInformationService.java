package my.app.service;

import java.util.List;

import my.app.domain.StockInformation;

public interface StockInformationService {

	void saveStockInformation(StockInformation stockInformation);
	void saveStockInformations(List<StockInformation> stockInformation);
	StockInformation getStockInformationById(int id);
	List<StockInformation> getStockInformations();
	List<StockInformation> getStockInformationsByStockId(int stockId);
	void updateStockInformation(StockInformation stockInformation);
	void deleteStockInformation(StockInformation stockInformation);
}
