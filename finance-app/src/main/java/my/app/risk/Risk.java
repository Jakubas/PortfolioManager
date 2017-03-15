package my.app.risk;

import java.util.List;

import my.app.domains.portfolio.StockInPortfolio;
import my.app.domains.stock.Stock;
import my.app.stockcalculations.StockDataCalculations;

public class Risk {

	private Risk() {}
	
	//The risk is based on the last 10 years of historical data
	//it is on a scale of 0 - 100, where 0 is very low risk and 100 is very high risk
	public static String calculateRisk(Stock stock) {
		double variance = Risk.calculateVariance(stock, 10);
		return calculateRisk(variance);
	}
	
	public static String calculateRisk(Double variance) {
		double standardDeviation = Math.sqrt(variance)*100;
		if (standardDeviation == 0) {
			return RiskValues.UNKNOWN;
		} else if (standardDeviation < 10) {
			return RiskValues.VERY_LOW;
		} else if (standardDeviation < 20) {
			return RiskValues.LOW;
		} else if (standardDeviation < 30) {
			return RiskValues.MEDIUM;
		} else if (standardDeviation < 40) {
			return RiskValues.MEDIUM_HIGH;
		} else if (standardDeviation < 50) {
			return RiskValues.HIGH;
		} else {
			return RiskValues.VERY_HIGH;
		}
	}
	
	//refactor this monster to be easier to understand
	public static double calculateVariance(Stock stock, int years) {
		//Calculate the yearly returns for the last 10 years
		int yearlyReturnsLength = years;
		double[] yearlyReturns = new double[yearlyReturnsLength];
		for(int i = 0; i < yearlyReturnsLength; i++) {
			Double yearlyReturn = StockDataCalculations.calculateReturnInDateRange(stock, i*365, (i+1)*365);
			if (yearlyReturn == null) {
				yearlyReturnsLength = i;
				break;
			}
			yearlyReturns[i] = yearlyReturn;
		}
		return calculateVariance(yearlyReturnsLength, yearlyReturns);
	}
	
	//refactor this monster to be easier to understand
	public static double calculateMonthlyVariance(Stock stock, int months) {
		//Calculate the yearly returns for the last 10 years
		int monthlyReturnsLength = months;
		double[] monthlyReturns = new double[monthlyReturnsLength];
		for(int i = 0; i < monthlyReturnsLength; i++) {
			Double monthlyReturn = StockDataCalculations.calculateReturnInDateRange(stock, i*30, (i+1)*30);
			if (monthlyReturn == null) {
				monthlyReturnsLength = i;
				break;
			}
			monthlyReturns[i] = monthlyReturn;
		}
		return calculateVariance(monthlyReturnsLength, monthlyReturns);
	}
	
	public static double calculateVariance(int returnsLength, double[] returns) {
		//Calculate the average return.
		double averageReturn = 0.0;
		double cumulativeReturn = 0.0;
		for (int i = 0; i < returnsLength; i++) {
			cumulativeReturn += returns[i];
		}
		averageReturn = cumulativeReturn/returnsLength;
		
		//calculate the sum of the squared differences
		double sumOfSquaredDifferences = 0.0;
		for (int i = 0; i < returnsLength; i++) {
			sumOfSquaredDifferences += Math.pow((returns[i] - averageReturn), 2);
		}
	
		//calculate the variance
		if (sumOfSquaredDifferences != 0 && returnsLength != 0)
			return sumOfSquaredDifferences/(returnsLength - 1);
		else
			return 0;
	}
	
	//calculate the risk factor of a portfolio
	public static String calculatePortfolioRisk(List<StockInPortfolio> portfolio) {
		double variance = 0;
		for (StockInPortfolio sip : portfolio) {
			Stock stock = sip.getStock();
			double stockVariance = Risk.calculateVariance(stock, 10);
			double stockVarianceWeighted = stockVariance * sip.getUser().calculateHoldingWeight(sip);
			variance += stockVarianceWeighted;
		}
		return calculateRisk(variance);
	}
	
	public static double calculate3MonthVariance(Stock stock) {
		return Risk.calculateMonthlyVariance(stock, 3);
	}
	
	public static double calculate1YearVariance(Stock stock) {
		return Risk.calculateVariance(stock, 1);
	}
	
	public static double calculate3YearVariance(Stock stock) {
		return Risk.calculateVariance(stock, 3);
	}
	
	public static double calculate5YearVariance(Stock stock) {
		return Risk.calculateVariance(stock, 5);
	}
	
	public static double calculate10YearVariance(Stock stock) {
		return Risk.calculateVariance(stock, 10);
	}
}
