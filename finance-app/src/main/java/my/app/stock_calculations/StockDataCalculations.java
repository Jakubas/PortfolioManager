package my.app.stock_calculations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import my.app.domain.Stock;
import my.app.domain.StockDailyInformation;

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
	
	private static Double calculateAnnualisedReturn(Stock stock, int numberOfDays) {
		Double cumulativeReturn = calculateReturn(stock, numberOfDays);
		if (cumulativeReturn == null)
			return null;
		
		double a = 1 + cumulativeReturn;
		double b = 365 / (double) numberOfDays;
		double annualisedReturn = Math.pow(a, b) - 1;
		return annualisedReturn;
	}
	
	private static Double calculateReturn(Stock stock, int numberOfDays) {
		List<StockDailyInformation> stockInfos = stock.getStockDailyInformations();

		//date is initialised with the current time
		Date date = new Date();
		StockDailyInformation currentStockInfo = findStockInformationForGivenDate(stockInfos, date);
		
		Date date2 = subtractDaysFromDate(date, numberOfDays);
		StockDailyInformation prevStockInfo = findStockInformationForGivenDate(stockInfos, date2);
		if (currentStockInfo == null || prevStockInfo == null) {
			return null;
		}
		double currentPrice = currentStockInfo.getAdjustedClose();
		double prevPrice = prevStockInfo.getAdjustedClose();
		double percentageChange = (currentPrice/prevPrice) - 1;
		return percentageChange;
	}
	
	private static Double calculateReturnInDateRange(Stock stock, int numberOfDaysStart, int numberOfDaysEnd) {
		List<StockDailyInformation> stockInfos = stock.getStockDailyInformations();
		//date is initialised with the current time
		Date currentDate = new Date();
		Date startDate = subtractDaysFromDate(currentDate, numberOfDaysStart);
		StockDailyInformation startStockInfo = findStockInformationForGivenDate(stockInfos, startDate);
		
		Date endDate = subtractDaysFromDate(currentDate, numberOfDaysEnd);
		StockDailyInformation endStockInfo = findStockInformationForGivenDate(stockInfos, endDate);
		if (startStockInfo == null || endStockInfo == null) {
			return null;
		}
		double currentPrice = startStockInfo.getAdjustedClose();
		double prevPrice = endStockInfo.getAdjustedClose();
		double percentageChange = (currentPrice/prevPrice) - 1;
		return percentageChange;
	}
	
	//The risk is based on the last 10 years of historical data
	//it is on a scale of 0 - 100, where 0 is very low risk and 100 is very high risk
	public static String calculateRisk(Stock stock) {
		double variance = calculateVariance(stock);
		return calculateRisk(variance);
	}
	
	public static String calculateRisk(Double variance) {
		double standardDeviation = Math.sqrt(variance)*100;
		if (standardDeviation == 0) {
			return "N/A";
		} else if (standardDeviation < 10) {
			return "very low risk";
		} else if (standardDeviation < 20) {
			return "low risk";
		} else if (standardDeviation < 30) {
			return "medium risk";
		} else if (standardDeviation < 40) {
			return "medium-high risk";
		} else if (standardDeviation < 50) {
			return "high risk";
		} else {
			return "very high risk";
		}
	}
	
	public static double calculateVariance(Stock stock) {
		//Calculate the yearly returns for the last 10 years
		int yearlyReturnsLength = 10;
		double[] yearlyReturns = new double[yearlyReturnsLength];
		for(int i = 0; i < yearlyReturnsLength; i++) {
			Double yearlyReturn = calculateReturnInDateRange(stock, i*365, (i+1)*365);
			if (yearlyReturn == null) {
				yearlyReturnsLength = i;
				break;
			}
			yearlyReturns[i] = yearlyReturn;
		}

		//Calculate the average return.
		double averageReturn = 0.0;
		double cumulativeReturn = 0.0;
		for (int i = 0; i < yearlyReturnsLength; i++) {
			cumulativeReturn += yearlyReturns[i];
		}
		averageReturn = cumulativeReturn/yearlyReturnsLength;
		
		//calculate the sum of the squared differences
		double sumOfSquaredDifferences = 0.0;
		for (int i = 0; i < yearlyReturnsLength; i++) {
			sumOfSquaredDifferences += Math.pow((yearlyReturns[i] - averageReturn), 2);
		}

		//calculate the variance
		if (sumOfSquaredDifferences != 0 && yearlyReturnsLength != 0)
			return sumOfSquaredDifferences/(yearlyReturnsLength - 1);
		else
			return 0;
	}
	
	//bugs need to fix like in calculateVariance()
	public static double calculateAverageReturn(Stock stock) {
		//Calculate the yearly returns for the last 10 years
		double[] yearlyReturns = new double[10];
		for(int i = 0; i < 10; i++) {
			yearlyReturns[i] = calculateReturnInDateRange(stock, i*365, (i+1)*365);
		}
		//Calculate the average return.
		double averageReturn = 0.0;
		double cumulativeReturn = 0.0;
		for (int i = 0; i < yearlyReturns.length; i++) {
			cumulativeReturn += yearlyReturns[i];
		}
		averageReturn = cumulativeReturn/yearlyReturns.length;
		return averageReturn;
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
			return null;
		}
		//check if date we are searching for is >= Jan 1 1970
		return findStockInformationForGivenDate(stockInfos, subtractDaysFromDate(date, 1));
	}
	
	private static Date subtractDaysFromDate(Date date, int numberOfDays) {	
		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		LocalDateTime tenDaysAgo = ldt.minusDays(numberOfDays);
		Date adjustedDate = Date.from(tenDaysAgo.atZone(ZoneId.systemDefault()).toInstant());
		return adjustedDate;
	}
	
	public static Double findStockPriceOnDate(Stock stock, Date date) {
		List<StockDailyInformation> stockInfos = stock.getStockDailyInformations();
		Date earliestDate = new Date();
		for (StockDailyInformation stockInfo : stockInfos) {
			Date stockInfoDate = stockInfo.getDate();
			if (stockInfoDate.before(earliestDate)) {
				earliestDate = stockInfoDate;
			}
			if (DateUtils.isSameDay(stockInfoDate, date)) {
				return stockInfo.getAdjustedClose();
			}
		}
		if (earliestDate.after(date)) {
			//stock data does not go that far back so we return null
			return null;
		}
		//check if date we are searching for is >= Jan 1 1970
		return findStockInformationForGivenDate(stockInfos, subtractDaysFromDate(date, 1)).getAdjustedClose();
	}
}
