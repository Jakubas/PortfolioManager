package my.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.app.dao.StockDAO;
import my.app.domain.Stock;

@Service
public class StockServiceImpl implements StockService {

	private final StockDAO dao;
	
	@Autowired
	public StockServiceImpl(StockDAO dao) {
		this.dao = dao;
	}
	
	public void saveStock(Stock stock) {
		dao.saveStock(stock);
	}

	public Stock getStockById(int id) {
		Stock stock = dao.getStockById(id);
		return stock;
	}

	public Stock getStockByName(String name) {
		Stock stock = dao.getStockByName(name);
		return stock;
	}

	public Stock getStockByTicker(String ticker) {
		Stock stock = dao.getStockByTicker(ticker);
		return stock;
	}

	public List<Stock> getStocks() {
		List<Stock> stocks = dao.getStocks();
		return stocks;
	}

	public void updateStock(Stock stock) {
		dao.updateStock(stock);
	}

	public void deleteStock(Stock stock) {
		dao.deleteStock(stock);
	}
}
