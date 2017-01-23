package my.app.dao;

import java.util.List;

import my.app.domain.StockCalculatedData;

public interface StockCalculatedDataDAO {

	void saveStockCalculatedData(StockCalculatedData stockCalculatedData);
	StockCalculatedData getStockCalculatedDataById(int id);
	List<StockCalculatedData> getStockCalculatedDatas();
	void updateStockCalculatedData(StockCalculatedData stockCalculatedData);
	void deleteStockCalculatedData(StockCalculatedData stockCalculatedData);
}
