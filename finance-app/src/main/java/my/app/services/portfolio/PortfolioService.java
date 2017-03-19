package my.app.services.portfolio;

import java.time.LocalDate;
import java.util.List;

import my.app.domains.portfolio.StockInPortfolio;

public interface PortfolioService {
	
	List<List<StockInPortfolio>> groupPortfolio(List<StockInPortfolio> portfolio);
	List<LocalDate> getBuyDates(List<StockInPortfolio> portfolio);
	List<LocalDate> getSellDates(List<StockInPortfolio> portfolio);
	int getStockAmount(List<StockInPortfolio> portfolio);
	double getGain(List<StockInPortfolio> portfolio);
	double getDifference(List<StockInPortfolio> portfolio);
	double getAnnualisedReturn(List<StockInPortfolio> portfolio);
	double getValue(List<StockInPortfolio> portfolio);
	double getValueOnDate(List<StockInPortfolio> portfolio, LocalDate date);
	LocalDate getEarliestDateIn(List<StockInPortfolio> portfolio);
	LocalDate getLatestDateIn(List<StockInPortfolio> portfolio);
	StockInPortfolio getWorstPerformer(List<StockInPortfolio> portfolio);
	StockInPortfolio getBestPerformer(List<StockInPortfolio> portfolio);
}
