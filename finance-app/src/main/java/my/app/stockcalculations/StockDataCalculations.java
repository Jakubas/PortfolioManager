package my.app.stockcalculations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import my.app.domains.Stock;
import my.app.domains.StockDailyInformation;
import my.app.risk.RiskValues;

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
	
	public static Double calculateAnnualisedReturn(Stock stock, Date buyDate, Date sellDate) {
		Double cumulativeReturn = calculateReturnInDateRange(stock, sellDate, buyDate);
		if (cumulativeReturn == null)
			return null;
		int numberOfDays = (int) daysBetweenDates(buyDate, sellDate);
		return calculateAnnualisedReturn(cumulativeReturn, numberOfDays);
	}
	
	public static Double calculateAnnualisedReturn(Date buyDate, Date sellDate,
												   double buyPrice, double sellPrice) {
		Double cumulativeReturn = calculateReturn(buyPrice, sellPrice);
		if (cumulativeReturn == null)
			return null;
		int numberOfDays = (int) daysBetweenDates(buyDate, sellDate);
		System.out.println(numberOfDays);
		return calculateAnnualisedReturn(cumulativeReturn, numberOfDays);
	}
	
	public static long daysBetweenDates(Date startDate, Date endDate) {
	    return ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant());
	}
	
	private static Double calculateReturn(Stock stock, int numberOfDays) {
		//date is initialised with the current time
		Date date = new Date();
		Date endDate = subtractDaysFromDate(date, numberOfDays);
		return calculateReturnInDateRange(stock, date, endDate);
	}
	
	public static Double calculateReturnInDateRange(Stock stock, int numberOfDaysStart, int numberOfDaysEnd) {
		//date is initialised with the current time
		Date currentDate = new Date();
		Date startDate = subtractDaysFromDate(currentDate, numberOfDaysStart);
		Date endDate = subtractDaysFromDate(currentDate, numberOfDaysEnd);
		return calculateReturnInDateRange(stock, startDate, endDate);
	}
	
	private static Double calculateReturnInDateRange(Stock stock, Date sellDate, Date buyDate) {
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
	
	public static Double findStockPriceOnDate(Stock stock, Date date) {
		List<StockDailyInformation> stockInfos = stock.getStockDailyInformations();
		return findStockInformationForGivenDate(stockInfos, date).getAdjustedClose();
	}
	
	private static StockDailyInformation findStockInformationForGivenDate(List<StockDailyInformation> stockInfos, Date date) {
		Date earliestDate = new Date();
		for (StockDailyInformation stockInfo : stockInfos) {
			Date stockInfoDate = stockInfo.getDate();
			if (stockInfoDate.before(earliestDate)) {
				earliestDate = stockInfoDate;
			}
			if (DateUtils.isSameDay(stockInfoDate, date)) {
				return stockInfo;
			}
		}
		if (earliestDate.after(date)) {
			//stock data does not go that far back so we return null
			return null;
		}
		return findStockInformationForGivenDateHelper(stockInfos, subtractDaysFromDate(date, 1));
	}
	
	private static StockDailyInformation findStockInformationForGivenDateHelper(List<StockDailyInformation> stockInfos, Date date) {
		for (StockDailyInformation stockInfo : stockInfos) {
			Date stockInfoDate = stockInfo.getDate();
			if (DateUtils.isSameDay(stockInfoDate, date)) {
				return stockInfo;
			}
		}
		return findStockInformationForGivenDateHelper(stockInfos, subtractDaysFromDate(date, 1));
	}
	
	private static Date subtractDaysFromDate(Date date, int numberOfDays) {	
		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		LocalDateTime tenDaysAgo = ldt.minusDays(numberOfDays);
		Date adjustedDate = Date.from(tenDaysAgo.atZone(ZoneId.systemDefault()).toInstant());
		return adjustedDate;
	}
}
