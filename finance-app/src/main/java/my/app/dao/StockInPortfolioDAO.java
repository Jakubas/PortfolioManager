package my.app.dao;

import java.util.List;

import my.app.domain.StockInPortfolio;

public interface StockInPortfolioDAO {

	void saveStockInPortfolio(StockInPortfolio stockInPortfolio);
	StockInPortfolio getStockInPortfolioById(int id);
	List<StockInPortfolio> getStocksInPortfolio();
	void updateStockInPortfolio(StockInPortfolio stockInPortfolio);
	void deleteStockInPortfolio(StockInPortfolio stockInPortfolio);
}
