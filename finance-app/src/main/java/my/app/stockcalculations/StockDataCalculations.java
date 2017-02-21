package my.app.stockcalculations;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import my.app.domains.Stock;
import my.app.domains.StockDailyInformation;

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
	
	private static Double calculateAnnualisedReturn(Double cumulativeReturn, int numberOfDays) {
		if (cumulativeReturn == null || numberOfDays == 0)
			return null;
		double a = 1 + cumulativeReturn;
		double b = 365 / (double) numberOfDays;
		double annualisedReturn = Math.pow(a, b) - 1;
		return annualisedReturn;
	}
	
	private static Double calculateAnnualisedReturn(Stock stock, int numberOfDays) {
		Double cumulativeReturn = calculateReturn(stock, numberOfDays);
		return calculateAnnualisedReturn(cumulativeReturn, numberOfDays);
	}
	
	public static Double calculateAnnualisedReturn(Stock stock, LocalDate buyDate, LocalDate sellDate) {
		Double cumulativeReturn = calculateReturnInDateRange(stock, sellDate, buyDate);
		if (cumulativeReturn == null)
			return null;
		int numberOfDays = (int) daysBetweenDates(buyDate, sellDate);
		return calculateAnnualisedReturn(cumulativeReturn, numberOfDays);
	}
	
	public static Double calculateAnnualisedReturn(LocalDate buyDate, LocalDate sellDate,
												   double buyPrice, double sellPrice) {
		Double cumulativeReturn = calculateReturn(buyPrice, sellPrice);
		if (cumulativeReturn == null)
			return null;
		int numberOfDays = (int) daysBetweenDates(buyDate, sellDate);
		return calculateAnnualisedReturn(cumulativeReturn, numberOfDays);
	}
	
	public static long daysBetweenDates(LocalDate startDate, LocalDate endDate) {
		Period period = Period.between(startDate, endDate);
	    return period.getDays();
	}
	
	private static Double calculateReturn(Stock stock, int numberOfDays) {
		//date is initialised with the current time
		LocalDate today = LocalDate.now();
		LocalDate endDate = subtractDaysFromDate(today, numberOfDays);
		return calculateReturnInDateRange(stock, today, endDate);
	}
	
	public static Double calculateReturnInDateRange(Stock stock, int numberOfDaysStart, int numberOfDaysEnd) {
		//date is initialised with the current time
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
		double buyPrice = buyStockInfo.getAdjustedClose();
		double sellPrice = sellStockInfo.getAdjustedClose();
		return calculateReturn(buyPrice, sellPrice);
	}
	
	private static Double calculateReturn(double buyPrice, double sellPrice) {
		double percentageChange = (sellPrice/buyPrice) - 1;
		return percentageChange;
	}
	
	public static Double findStockPriceOnDate(Stock stock, LocalDate date) {
		List<StockDailyInformation> stockInfos = stock.getStockDailyInformations();
		StockDailyInformation stockInfo = findStockInformationForGivenDate(stockInfos, date);
		Double result = stockInfo != null ? stockInfo.getAdjustedClose() : null;
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
		if (dayCounter == 60) {
			//we went back 60 days and didn't find any price data, so the stock wasn't listed
			//anywhere near the date that we are searching for.
			return null;
		}
		return findStockInformationForGivenDateHelper(stockInfos, subtractDaysFromDate(date, 1), dayCounter++);
	}
	
	private static LocalDate subtractDaysFromDate(LocalDate date, int numberOfDays) {	
		return date.minusDays(numberOfDays);
	}
}
