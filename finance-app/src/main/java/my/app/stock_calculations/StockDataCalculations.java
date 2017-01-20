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

	public double calculateQuarterlyAnnualisedReturn(Stock stock) {
		int numberOfDays = 365/4;
		double annualisedReturn = calculateAnnualisedReturn(stock, numberOfDays);
		return annualisedReturn;
	}

	public double calculate1YrAnnualisedReturn(Stock stock) {
		return calculateReturn(stock, 365);
	}
	
	public double calculate5YrAnnualisedReturn(Stock stock) {
		int numberOfDays = 365*5;
		double annualisedReturn = calculateAnnualisedReturn(stock, numberOfDays);
		return annualisedReturn;
	}
	
	public double calculate10YrAnnualisedReturn(Stock stock) {
		int numberOfDays = 365*10;
		double annualisedReturn = calculateAnnualisedReturn(stock, numberOfDays);
		return annualisedReturn;
	}
	
	private double calculateAnnualisedReturn(Stock stock, int numberOfDays) {
		double cumulativeReturn = calculateReturn(stock, numberOfDays);
		
		double a = 1 + cumulativeReturn;
		double b = 365 / (double) numberOfDays;
		double annualisedReturn = Math.pow(a, b) - 1;
		return annualisedReturn;
	}
	
	private double calculateReturn(Stock stock, int numberOfDays) {
		List<StockDailyInformation> stockInfos = stock.getStockDailyInformations();

		//date is initialised with the current time
		Date date = new Date();
		StockDailyInformation currentStockInfo = findStockInformationForGivenDate(stockInfos, date);
		
		Date date2 = subtractDaysFromDate(date, numberOfDays);
		StockDailyInformation prevStockInfo = findStockInformationForGivenDate(stockInfos, date2);
		double currentPrice = currentStockInfo.getAdjustedClose();
		double prevPrice = prevStockInfo.getAdjustedClose();
		double percentageChange = (currentPrice/prevPrice) - 1;
		return percentageChange;
	}
	
	private double calculateReturnInDateRange(Stock stock, int numberOfDaysStart, int numberOfDaysEnd) {
		List<StockDailyInformation> stockInfos = stock.getStockDailyInformations();

		//date is initialised with the current time
		Date currentDate = new Date();
		Date startDate = subtractDaysFromDate(currentDate, numberOfDaysStart);
		StockDailyInformation currentStockInfo = findStockInformationForGivenDate(stockInfos, startDate);
		
		Date endDate = subtractDaysFromDate(currentDate, numberOfDaysEnd);
		StockDailyInformation prevStockInfo = findStockInformationForGivenDate(stockInfos, endDate);
		double currentPrice = currentStockInfo.getAdjustedClose();
		double prevPrice = prevStockInfo.getAdjustedClose();
		double percentageChange = (currentPrice/prevPrice) - 1;
		return percentageChange;
	}
	
	//The risk is based on the last 10 years of historical data
	//it is on a scale of 0 - 100, where 0 is very low risk and 100 is very high risk
	public String calculateRisk(Stock stock) {
		double variance = calculateVariance(stock);
		double standardDeviation = Math.sqrt(variance)*100;
		if (standardDeviation < 10) {
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
	
	private double calculateVariance(Stock stock) {
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
		//calculate the sum of the squared differences
		double sumOfSquaredDifferences = 0.0;
		for (int i = 0; i < yearlyReturns.length; i++) {
			sumOfSquaredDifferences += Math.pow((yearlyReturns[i] - averageReturn), 2);
		}
		//calculate the variance
		return sumOfSquaredDifferences/(yearlyReturns.length - 1);
	}
	
	public double calculateAverageReturn(Stock stock) {
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
	
	private StockDailyInformation findStockInformationForGivenDate(List<StockDailyInformation> stockInfos, Date date) {
		for (StockDailyInformation stockInfo : stockInfos) {
			Date stockInfoDate = stockInfo.getDate();
			if (DateUtils.isSameDay(stockInfoDate, date)) {
				return stockInfo;
			}
		}
		//check if date we are searching for is >= Jan 1 1970
		if (date.getTime() > 0) {
			return findStockInformationForGivenDate(stockInfos, subtractDaysFromDate(date, 1));
		} else {
			return null;
		}
	}
	
	private Date subtractDaysFromDate(Date date, int numberOfDays) {	
		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		LocalDateTime tenDaysAgo = ldt.minusDays(numberOfDays);
		Date adjustedDate = Date.from(tenDaysAgo.atZone(ZoneId.systemDefault()).toInstant());
		return adjustedDate;
	}
}
