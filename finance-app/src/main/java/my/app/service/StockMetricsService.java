package my.app.service;

import java.util.List;

import my.app.domain.StockMetrics;

public interface StockMetricsService {

	void saveStockMetrics(StockMetrics stockMetrics);
	StockMetrics getStockMetricsById(int id);
	List<StockMetrics> getStockMetrics();
	void updateStockMetrics(StockMetrics stockMetrics);
	void deleteStockMetrics(StockMetrics stockMetrics);
	void deleteStockMetricsById(int id);
}
