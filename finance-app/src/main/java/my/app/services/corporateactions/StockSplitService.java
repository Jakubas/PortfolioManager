package my.app.services.corporateactions;

import java.util.List;

import my.app.domains.corporateactions.StockSplit;

public interface StockSplitService {

	void saveStockSplit(StockSplit stockSplit);
	StockSplit getStockSplitById(int id);
	List<StockSplit> getStockSplits();
	void updateStockSplit(StockSplit stockSplit);
	void deleteStockSplit(StockSplit stockSplit);
}
