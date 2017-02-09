package my.app.dao;

import java.util.List;

import my.app.domain.Stock;

public interface StockDAO {

	void saveStock(Stock stock);
	Stock getStockById(int id);
	Stock getStockByName(String name);
	Stock getStockByTicker(String ticker);
	List<Stock> getStocks();
	void updateStock(Stock stock);
	void deleteStock(Stock stock);
	List<String> getSectors();
}
