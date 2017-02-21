package my.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import my.app.dao.StockInPortfolioDAO;
import my.app.domains.portfolio.StockInPortfolio;

@Service
public class StockInPortfolioServiceImpl implements StockInPortfolioService {

	private final StockInPortfolioDAO dao;
	
	@Autowired
	public StockInPortfolioServiceImpl(StockInPortfolioDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public void saveStockInPortfolio(StockInPortfolio stockInPortfolio) {
		dao.saveStockInPortfolio(stockInPortfolio);
	}

	@Override
	public StockInPortfolio getStockInPortfolioById(int id) {
		StockInPortfolio stockInPortfolio = dao.getStockInPortfolioById(id);
		return stockInPortfolio;
	}

	@Override
	public List<StockInPortfolio> getStocksInPortfolio() {
		List<StockInPortfolio> stocksInPortfolio = dao.getStocksInPortfolio();
		return stocksInPortfolio;
	}

	@Override
	public void updateStockInPortfolio(StockInPortfolio stockInPortfolio) {
		dao.updateStockInPortfolio(stockInPortfolio);
	}

	@Override
	public void deleteStockInPortfolio(StockInPortfolio stockInPortfolio) {
		dao.deleteStockInPortfolio(stockInPortfolio);
	}
}
