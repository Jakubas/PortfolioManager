package my.app.dao.corporateactions;

import java.util.List;

import my.app.domains.corporateactions.StockSplit;

public interface StockSplitDAO {

	void saveStockSplit(StockSplit StockSplit);
	StockSplit getStockSplitById(int id);
	List<StockSplit> getStockSplits();
	void updateStockSplit(StockSplit StockSplit);
	void deleteStockSplit(StockSplit StockSplit);
}