package my.app.service;

import java.util.List;

import my.app.domain.Stock;

public interface StockService {
	
	void saveStock(Stock stock);
	Stock getStockById(int id);
	Stock getStockByName(String name);
	Stock getStockByTicker(String ticker);
	List<Stock> getStocks();
	void updateStock(Stock stock);
	void deleteStock(Stock stock);
	List<String> getSectors();
}
