package my.app.service;

import java.util.List;

import my.app.domain.Stock;
import my.app.domain.StockCalculatedData;

public interface StockCalculatedDataService {

	void saveStockCalculatedData(StockCalculatedData stockCalculatedData);
	StockCalculatedData getStockCalculatedDataById(int id);
	List<StockCalculatedData> getStockCalculatedDatas();
	void updateStockCalculatedData(StockCalculatedData stockCalculatedData);
	void deleteStockCalculatedData(StockCalculatedData stockCalculatedData);
	void deleteStockCalculatedDataById(int id);
}
