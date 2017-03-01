package my.app.stockcalculations;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import my.app.domains.stock.Stock;
import my.app.domains.stock.StockDailyInformation;

//this class is used for various calculations regarding a stock, such as risk, annualised returns, etc.
//annualisedReturns are estimates and may be off by a couple days
public class StockDataCalculations {

	private StockDataCalculations() {
		
	}
	
	public static Double calculateQuarterlyAnnualisedReturn(Stock stock) {
		int numberOfDays = 365/4;
		Double annualisedReturn = calculateAnnualisedReturn(stock, numberOfDays);
		return annualisedReturn;
	}

	public static Double calculate1YrAnnualisedReturn(Stock stock) {
		int numberOfDays = 365;
		Double annualisedReturn = calculateAnnualisedReturn(stock, numberOfDays);
		return annualisedReturn;
	}
	
	public static Double calculate5YrAnnualisedReturn(Stock stock) {
		int numberOfDays = 365*5;
		Double annualisedReturn = calculateAnnualisedReturn(stock, numberOfDays);
		return annualisedReturn;
	}
	
	public static Double calculate10YrAnnualisedReturn(Stock stock) {
		int numberOfDays = 365*10;
		Double annualisedReturn = calculateAnnualisedReturn(stock, numberOfDays);
		return annualisedReturn;
	}
	
	public static Double calculateAnnualisedReturn(Stock stock, LocalDate buyDate, LocalDate sellDate) {
		Double totalReturn = calculateReturnInDateRange(stock, sellDate, buyDate);
		return calculateAnnualisedReturn(totalReturn, buyDate, sellDate);
	}
	
	public static Double calculateAnnualisedReturn(Stock stock, LocalDate buyDate, LocalDate sellDate, 
			Double buyPrice, Double sellPrice) {
		Double totalReturn = calculateReturnInDateRange(stock, sellDate, buyDate, buyPrice, sellPrice);
		return calculateAnnualisedReturn(totalReturn, buyDate, sellDate);
	}
	
	private static Double calculateAnnualisedReturn(Double totalReturn, LocalDate buyDate, LocalDate sellDate) {
		if (totalReturn == null)
			return null;
		int numberOfDays = (int) daysBetweenDates(buyDate, sellDate);
		return calculateAnnualisedReturn(totalReturn, numberOfDays);
	}
	
	private static Double calculateAnnualisedReturn(Stock stock, int numberOfDays) {
		Double totalReturn = calculateReturn(stock, numberOfDays);
		return calculateAnnualisedReturn(totalReturn, numberOfDays);
	}
	
	private static Double calculateAnnualisedReturn(Double totalReturn, int numberOfDays) {
		if (totalReturn == null || numberOfDays == 0)
			return null;
		double a = 1 + totalReturn;
		double b = 365 / (double) numberOfDays;
		if (numberOfDays == 365/4) b = 4;
		
		double annualisedReturn = Math.pow(a, b) - 1;
		return annualisedReturn;
	}
	
	public static long daysBetweenDates(LocalDate startDate, LocalDate endDate) {
		long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
	    return daysBetween;
	}
	
	private static Double calculateReturn(Stock stock, int numberOfDays) {
		LocalDate today = LocalDate.now();
		LocalDate endDate = subtractDaysFromDate(today, numberOfDays);
		return calculateReturnInDateRange(stock, today, endDate);
	}
	
	public static Double calculateReturnInDateRange(Stock stock, int numberOfDaysStart, int numberOfDaysEnd) {
		LocalDate today = LocalDate.now();
		LocalDate startDate = subtractDaysFromDate(today, numberOfDaysStart);
		LocalDate endDate = subtractDaysFromDate(today, numberOfDaysEnd);
		return calculateReturnInDateRange(stock, startDate, endDate);
	}
	
	private static Double calculateReturnInDateRange(Stock stock, LocalDate sellDate, LocalDate buyDate) {
		List<StockDailyInformation> stockInfos = stock.getStockDailyInformations();
		StockDailyInformation sellStockInfo = findStockInformationForGivenDate(stockInfos, sellDate);
		StockDailyInformation buyStockInfo = findStockInformationForGivenDate(stockInfos, buyDate);
		if (sellStockInfo == null || buyStockInfo == null) {
			return null;
		}
		double buyPrice = buyStockInfo.getAdjCloseStockSplits();
		double sellPrice = sellStockInfo.getAdjCloseStockSplits();
		return calculateReturn(buyStockInfo, sellStockInfo, buyPrice, sellPrice);
	}
	
	private static Double calculateReturnInDateRange(Stock stock, LocalDate sellDate, LocalDate buyDate, double buyPrice, double sellPrice) {
		List<StockDailyInformation> stockInfos = stock.getStockDailyInformations();
		StockDailyInformation sellStockInfo = findStockInformationForGivenDate(stockInfos, sellDate);
		StockDailyInformation buyStockInfo = findStockInformationForGivenDate(stockInfos, buyDate);
		if (sellStockInfo == null || buyStockInfo == null) {
			return null;
		}
		return calculateReturn(buyStockInfo, sellStockInfo, buyPrice, sellPrice);
	}
	
	private static Double calculateReturn(StockDailyInformation buyStockInfo, StockDailyInformation sellStockInfo, double buyPrice, double sellPrice) {
		double dividendTotal = (buyStockInfo.getAdjCloseStockSplits() - buyStockInfo.getAdjCloseDivNotReinvested()) - 
				(sellStockInfo.getAdjCloseStockSplits() - sellStockInfo.getAdjCloseDivNotReinvested());
		return calculateReturn(buyPrice, sellPrice, dividendTotal);
	}
	
	private static Double calculateReturn(double buyPrice, double sellPrice, double dividendTotal) {
		// (x - y) / y = x / y - 1
		double percentageChange = (sellPrice + dividendTotal - buyPrice) / buyPrice;
		return percentageChange;
	}
	
	public static Double findStockPriceOnDate(Stock stock, LocalDate date) {
		List<StockDailyInformation> stockInfos = stock.getStockDailyInformations();
		StockDailyInformation stockInfo = findStockInformationForGivenDate(stockInfos, date);
		Double result = stockInfo != null ? stockInfo.getAdjCloseStockSplits() : null;
		return result;
	}
	
	private static StockDailyInformation findStockInformationForGivenDate(List<StockDailyInformation> stockInfos, LocalDate date) {
		LocalDate earliestDate = LocalDate.now();
		for (StockDailyInformation stockInfo : stockInfos) {
			LocalDate stockInfoDate = stockInfo.getDate();
			if (stockInfoDate.isBefore(earliestDate)) {
				earliestDate = stockInfoDate;
			}
			if (stockInfoDate.equals(date)) {
				return stockInfo;
			}
		}
		if (earliestDate.isAfter(date)) {
			//stock data does not go that far back so we return null
			return null;
		}
		return findStockInformationForGivenDateHelper(stockInfos, subtractDaysFromDate(date, 1), 1);
	}
	
	private static StockDailyInformation findStockInformationForGivenDateHelper(
			List<StockDailyInformation> stockInfos, LocalDate date, int dayCounter) {
		for (StockDailyInformation stockInfo : stockInfos) {
			LocalDate stockInfoDate = stockInfo.getDate();
			if (stockInfoDate.equals(date)) {
				return stockInfo;
			}
		}
		if (dayCounter == 14) {
			//we went back 14 days and didn't find any price data, so the stock wasn't listed
			//anywhere near the date that we are searching for.
			return null;
		}
		return findStockInformationForGivenDateHelper(stockInfos, subtractDaysFromDate(date, 1), dayCounter++);
	}
	
	private static LocalDate subtractDaysFromDate(LocalDate date, int numberOfDays) {	
		return date.minusDays(numberOfDays);
	}
}
