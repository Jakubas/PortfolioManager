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
		
		Date dateOneQuarterAgo = subtractDaysFromDate(date, numberOfDays);
		StockDailyInformation prevStockInfo = findStockInformationForGivenDate(stockInfos, dateOneQuarterAgo);
		double currentPrice = currentStockInfo.getAdjustedClose();
		double prevPrice = prevStockInfo.getAdjustedClose();
		double percentageChange = (currentPrice/prevPrice) - 1;
		return percentageChange;
	}
	
	//to be implemented
	public void calculateRisk() {
		
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
