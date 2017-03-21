package my.app.risk;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.stat.correlation.Covariance;

import my.app.domains.portfolio.StockInPortfolio;
import my.app.domains.stock.Index;
import my.app.domains.stock.IndexDailyInformation;
import my.app.domains.stock.Stock;
import my.app.domains.stock.StockDailyInformation;
import my.app.stockcalculations.StockDataCalculations;

public class Risk {

	private Risk() {}
	
	//The risk is based on the last 10 years of historical data
	//it is on a scale of 0 - 100, where 0 is very low risk and 100 is very high risk
	public static String calculateRisk(Stock stock, Index index) {
		double variance = Risk.calculateVariance(stock, 10);
		Double beta = Risk.calculateBeta(stock, index, variance);
		return calculateRisk(beta);
	}
	
	public static String calculateRisk(Double beta) {
		if (beta == null) {
			return RiskValues.UNKNOWN;
		} else if (beta < 0.4) {
			return RiskValues.VERY_LOW;
		} else if (beta < 0.75) {
			return RiskValues.LOW;
		} else if (beta >= 0.75 && beta <= 1.25) {
			return RiskValues.MEDIUM;
		} else if (beta < 1.45) {
			return RiskValues.MEDIUM_HIGH;
		} else if (beta < 1.85) {
			return RiskValues.HIGH;
		} else {
			return RiskValues.VERY_HIGH;
		}
	}
	
	public static Double calculateBeta(Stock stock, Index index, double variance) {
		List<StockDailyInformation> sdis = stock.getStockDailyInformations();
		if (sdis == null || sdis.isEmpty()) {
			return null;
		}
		sdis.sort(Comparator.comparing(StockDailyInformation::getDate));
		LocalDate earliestDate = LocalDate.now().minusYears(10);
		sdis.removeIf(o -> o.getDate().isBefore(earliestDate));
		Set<LocalDate> dates = extractDates(sdis);
		List<IndexDailyInformation> idis = index.getIndexDailyInformations();
		idis.sort(Comparator.comparing(IndexDailyInformation::getDate));
		idis.removeIf(o -> !dates.contains(o.getDate()));
		
		List<Double> sdiPrices = extractPrices(sdis);
		List<Double> idiPrices = extractPrices2(idis);
		
		int sizeDiff = sdiPrices.size() - idiPrices.size();
		while (sizeDiff > 0) {
			sdiPrices.remove(0);
			sizeDiff--;
		}
		
		double[] sdiPercentageChanges = calculatePercentageChangesBetweenDays(sdiPrices);
		double[] idiPercentageChanges = calculatePercentageChangesBetweenDays(idiPrices);
		
		if (sdiPercentageChanges.length < 2) {
			return null;
		}
		
		Covariance covarianceMath = new Covariance();
		double covariance = covarianceMath.covariance(sdiPercentageChanges, idiPercentageChanges);
		double beta = covariance / variance;
		return beta;
	}
	
	
	private static double[] calculatePercentageChangesBetweenDays(List<Double> prices) {
		double[] percentageChanges = new double[prices.size() - 1];
		if (prices.size() == 1) {
			percentageChanges[0] = 0.0;
			return percentageChanges;
		}
		for (int i = 0; i < prices.size() - 1; i++) {
			Double price = prices.get(i);
			Double price2 = prices.get(i + 1);
			percentageChanges[i] = ((price2 - price) / price);
		}
		return percentageChanges;
	}

	private static List<Double> extractPrices(List<StockDailyInformation> sdis) {
		List<Double> prices = new ArrayList<Double>();
		for (StockDailyInformation sdi : sdis) {
			prices.add(sdi.getAdjCloseStockSplits());
		}
		return prices;
	}
	
	private static List<Double> extractPrices2(List<IndexDailyInformation> idis) {
		List<Double> prices = new ArrayList<Double>();
		for (IndexDailyInformation idi : idis) {
			prices.add(idi.getAdjustedClose());
		}
		return prices;
	}

	private static Set<LocalDate> extractDates(List<StockDailyInformation> sdis) {
		Set<LocalDate> dates = new HashSet<LocalDate>();
		for (StockDailyInformation sdi : sdis) {
			dates.add(sdi.getDate());
		}
		return dates;
	}

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
	public static String calculatePortfolioRisk(List<StockInPortfolio> portfolio, Index index) {
		double beta = 0;
		for (StockInPortfolio sip : portfolio) {
			Stock stock = sip.getStock();
			double stockVariance = Risk.calculateVariance(stock, 10);
			double stockBeta = Risk.calculateBeta(stock, index, stockVariance);
			double stockBetaWeighted = stockBeta * sip.getUser().calculateHoldingWeight(sip);
			beta += stockBetaWeighted;
		}
		return calculateRisk(beta);
	}
	
	public static double calculate3MonthVariance(Stock stock) {
		return Risk.calculateMonthlyVariance(stock, 3);
	}
	
	public static double calculate1YearVariance(Stock stock) {
		return Risk.calculateMonthlyVariance(stock, 12);
	}
	
	public static double calculate3YearVariance(Stock stock) {
		return Risk.calculateMonthlyVariance(stock, 36);
	}
	
	public static double calculate5YearVariance(Stock stock) {
		return Risk.calculateMonthlyVariance(stock, 60);
	}
	
	public static double calculate10YearVariance(Stock stock) {
		return Risk.calculateMonthlyVariance(stock, 120);
	}
}
