package my.app.dao;

import java.util.List;

import my.app.domain.StockMetrics;

public interface StockMetricsDAO {

	void saveStockMetrics(StockMetrics stockMetrics);
	StockMetrics getStockMetricsById(int id);
	List<StockMetrics> getStockMetrics();
	void updateStockMetrics(StockMetrics stockMetrics);
	void deleteStockMetrics(StockMetrics stockMetrics);
}
