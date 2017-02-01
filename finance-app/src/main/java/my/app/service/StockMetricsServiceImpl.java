package my.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import my.app.dao.StockMetricsDAOImpl;
import my.app.domain.StockMetrics;

@Service
public class StockMetricsServiceImpl implements StockMetricsService {

	private final StockMetricsDAOImpl dao;
	
	public StockMetricsServiceImpl(StockMetricsDAOImpl dao) {
		this.dao = dao;
	}
	
	public void saveStockMetrics(StockMetrics stockMetrics) {
		dao.saveStockMetrics(stockMetrics);
	}

	public StockMetrics getStockMetricsById(int id) {
		return dao.getStockMetricsById(id);
	}

	public List<StockMetrics> getStockMetrics() {
		return dao.getStockMetrics();
	}

	public void updateStockMetrics(StockMetrics stockMetrics) {
		dao.updateStockMetrics(stockMetrics);
	}

	public void deleteStockMetrics(StockMetrics stockMetrics) {
		dao.deleteStockMetrics(stockMetrics);
	}

	public void deleteStockMetricsById(int id) {
		StockMetrics stockMetrics = dao.getStockMetricsById(id);
		dao.deleteStockMetrics(stockMetrics);
	}
}
