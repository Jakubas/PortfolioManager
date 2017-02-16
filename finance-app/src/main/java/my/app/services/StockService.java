package my.app.services;

import java.util.List;

import my.app.domains.Stock;

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
