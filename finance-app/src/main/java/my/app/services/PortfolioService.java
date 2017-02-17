package my.app.services;

import java.util.Date;
import java.util.List;

import my.app.domains.StockInPortfolio;

public interface PortfolioService {
	
	List<List<StockInPortfolio>> groupPortfolio(List<StockInPortfolio> portfolio);
	List<Date> getBuyDates(List<StockInPortfolio> portfolio);
	List<Date> getSellDates(List<StockInPortfolio> portfolio);
	int getStockAmount(List<StockInPortfolio> portfolio);
	double getGain(List<StockInPortfolio> portfolio);
	double getDifference(List<StockInPortfolio> portfolio);
	double getAnnualisedReturn(List<StockInPortfolio> portfolio);
	double getValue(List<StockInPortfolio> portfolio);
}
