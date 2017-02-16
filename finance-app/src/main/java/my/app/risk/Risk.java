package my.app.risk;

import java.util.List;

import my.app.domains.Stock;
import my.app.domains.StockInPortfolio;
import my.app.stockcalculations.StockDataCalculations;

public class Risk {

	private Risk() {
		
	}
	
	//The risk is based on the last 10 years of historical data
	//it is on a scale of 0 - 100, where 0 is very low risk and 100 is very high risk
	public static String calculateRisk(Stock stock) {
		double variance = Risk.calculateVariance(stock);
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
	public static double calculateVariance(Stock stock) {
		//Calculate the yearly returns for the last 10 years
		int yearlyReturnsLength = 10;
		double[] yearlyReturns = new double[yearlyReturnsLength];
		for(int i = 0; i < yearlyReturnsLength; i++) {
			Double yearlyReturn = StockDataCalculations.calculateReturnInDateRange(stock, i*365, (i+1)*365);
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
	
	//calculate the risk factor of a portfolio
	public static String calculatePortfolioRisk(List<StockInPortfolio> portfolio) {
		double variance = 0;
		for (StockInPortfolio sip : portfolio) {
			Stock stock = sip.getStock();
			double stockVariance = Risk.calculateVariance(stock);
			double stockVarianceWeighted = stockVariance * sip.getUser().calculateHoldingWeight(sip);
			variance += stockVarianceWeighted;
		}
		return calculateRisk(variance);
	}
}
