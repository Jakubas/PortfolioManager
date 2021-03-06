package my.app.services.portfolio;

import java.util.List;

import my.app.domains.portfolio.StockInPortfolio;

public interface StockInPortfolioService {

	void saveStockInPortfolio(StockInPortfolio stockInPortfolio);
	StockInPortfolio getStockInPortfolioById(int id);
	List<StockInPortfolio> getStocksInPortfolio();
	void updateStockInPortfolio(StockInPortfolio stockInPortfolio);
	void deleteStockInPortfolio(StockInPortfolio stockInPortfolio);
}
