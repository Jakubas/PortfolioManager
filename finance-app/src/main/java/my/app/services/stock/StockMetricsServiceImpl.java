package my.app.services.stock;

import java.util.List;

import org.springframework.stereotype.Service;

import my.app.dao.stock.StockMetricsDAO;
import my.app.domains.stock.StockMetrics;

@Service
public class StockMetricsServiceImpl implements StockMetricsService {

	private final StockMetricsDAO dao;
	
	public StockMetricsServiceImpl(StockMetricsDAO dao) {
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
