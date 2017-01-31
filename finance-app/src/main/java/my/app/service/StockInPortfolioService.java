package my.app.service;

import java.util.List;

import my.app.domain.StockInPortfolio;

public interface StockInPortfolioService {

	void saveStockInPortfolio(StockInPortfolio stockInPortfolio);
	StockInPortfolio getStockInPortfolioById(int id);
	List<StockInPortfolio> getStocksInPortfolio();
	void updateStockInPortfolio(StockInPortfolio stockInPortfolio);
	void deleteStockInPortfolio(StockInPortfolio stockInPortfolio);
}