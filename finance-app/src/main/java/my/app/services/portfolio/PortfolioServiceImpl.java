package my.app.services.portfolio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;
import org.springframework.stereotype.Service;

import my.app.domains.portfolio.StockInPortfolio;
import my.app.formatter.PercentageFormat;
import my.app.stockcalculations.StockDataCalculations;

@Service
public class PortfolioServiceImpl implements PortfolioService {

	@Override
	public List<List<StockInPortfolio>> groupPortfolio(List<StockInPortfolio> portfolio) {
		List<List<StockInPortfolio>> groupedPortfolio = groupPortfolioByStocks(portfolio);
		return groupedPortfolio;
	}
	
	/*
	 * group a portfolio by their underlying holdings. Each unique holding has a list of 
	 * all stockInPortfolio which represent that holding. 
	 * The list of the lists of these holdings is what is returned.
	 */
	private List<List<StockInPortfolio>> groupPortfolioByStocks(List<StockInPortfolio> portfolio) {
		List<List<StockInPortfolio>> groupedPortfolio = new ArrayList<List<StockInPortfolio>>();
		List<String> traversed = new ArrayList<String>();
		for (int i = 0; i < portfolio.size(); i++) {
			StockInPortfolio holding = portfolio.get(i);
			String ticker = holding.getStock().getTicker();
			if (!traversed.contains(ticker)) {
				List<StockInPortfolio> subPortfolio = new ArrayList<StockInPortfolio>();
				subPortfolio.add(holding);
				traversed.add(ticker);
				for (int j = i + 1; j < portfolio.size(); j++) {
					StockInPortfolio holding2 = portfolio.get(j);
					if (ticker.equals(holding2.getStock().getTicker())) {
						subPortfolio.add(holding2);
					}
				}
				groupedPortfolio.add(subPortfolio);
			}
		}
		return groupedPortfolio;
	}

	@Override
	public List<LocalDate> getBuyDates(List<StockInPortfolio> portfolio) {
		List<LocalDate> buyDates = new ArrayList<LocalDate>();
		for (StockInPortfolio holding : portfolio) {
			buyDates.add(holding.getBuyDate());
		}
		return buyDates;
	}

	@Override
	public List<LocalDate> getSellDates(List<StockInPortfolio> portfolio) {
		List<LocalDate> sellDates = new ArrayList<LocalDate>();
		for (StockInPortfolio holding : portfolio) {
			sellDates.add(holding.getSellDate());
		}
		return sellDates;
	}

	@Override
	public int getStockAmount(List<StockInPortfolio> portfolio) {
		int amount = 0;
		for (StockInPortfolio holding : portfolio) {
			amount += holding.getAmount();
		}
		return amount;
	}
	
	@Override
	@NumberFormat(style = Style.NUMBER, pattern = "#,##0.00")
	public double getGain(List<StockInPortfolio> portfolio) {
		double gain = 0;
		for (StockInPortfolio holding : portfolio) {
			gain += holding.getReturnOnInvestment() * holding.getBuyPrice() * holding.getAmount();
		}
		return gain;
	}
	
	@Override
	@NumberFormat(style = Style.NUMBER, pattern = "#,##0.00")
	public double getDifference(List<StockInPortfolio> portfolio) {
		double difference = Math.abs(getGain(portfolio));
		return difference;
	}
	
	@Override
	@PercentageFormat
	public double getAnnualisedReturn(List<StockInPortfolio> portfolio) {
		double totalValue = getValue(portfolio);
		double annualReturn = 0;
		for (StockInPortfolio holding : portfolio) {
			double holdingAnnualReturn = holding.getAnnualisedReturn();
			double holdingValue = holding.getValue();
			double shareWeight = holdingValue/totalValue;
			annualReturn += holdingAnnualReturn * shareWeight;
		}
		return annualReturn;
	}

	@Override
	@NumberFormat(style = Style.NUMBER, pattern = "#,##0.00")
	public double getValue(List<StockInPortfolio> portfolio) {
		double value = 0;
		for (StockInPortfolio holding : portfolio) {
			value += holding.getValue();
		}
		return value;
	}
	
	@Override
	public double getValueOnDate(List<StockInPortfolio> portfolio, LocalDate date) {
		double value = 0;
		for (StockInPortfolio holding : portfolio) {
			value += getHoldingValueOnDate(holding, date);
		}
		return value;
	}
	
	private double getHoldingValueOnDate(StockInPortfolio holding, LocalDate date) {
		if (isHeldOn(holding, date)) {
			Double price = StockDataCalculations.findStockPriceOnDate(holding.getStock(), date);
			double value = price != null ? price * holding.getAmount() : 0;
			return value;
		}
		return 0;
	}
	
	//returns true if the holding was held (owned) on the date provided, otherwise returns false
	private boolean isHeldOn(StockInPortfolio holding, LocalDate date) {
		return (holding.getBuyDate().isBefore(date) || holding.getBuyDate().equals(date)) && 
			   (holding.getSellDate() == null || holding.getSellDate().isAfter(date));
	}

	@Override
	public LocalDate getEarliestDateIn(List<StockInPortfolio> portfolio) {
		LocalDate earliestDate = LocalDate.now();
		for (StockInPortfolio holding : portfolio) {
			if (holding.getBuyDate().isBefore(earliestDate)) {
				earliestDate = holding.getBuyDate();
			}
		}
		return earliestDate;
	}
	
	@Override
	public LocalDate getLatestDateIn(List<StockInPortfolio> portfolio) {
		if (portfolio == null || portfolio.isEmpty()) 
			return LocalDate.now();
		portfolio.sort(Comparator.comparing(StockInPortfolio::getBuyDate).reversed());
		return portfolio.get(0).getBuyDate();
	}
	@Override
	public StockInPortfolio getWorstPerformer(List<StockInPortfolio> portfolio) {
		if (portfolio.isEmpty()) {
			return null;
		}
		StockInPortfolio worstPerformingHolding = portfolio.get(0); 
		for (StockInPortfolio holding : portfolio) {
			if (holding.getAnnualisedReturn() < worstPerformingHolding.getAnnualisedReturn()) {
				worstPerformingHolding = holding;
			}
		}
		return worstPerformingHolding;
	}
	
	@Override
	public StockInPortfolio getBestPerformer(List<StockInPortfolio> portfolio) {
		if (portfolio.isEmpty()) {
			return null;
		}
		StockInPortfolio bestPerformingHolding = portfolio.get(0); 
		for (StockInPortfolio holding : portfolio) {
			System.out.println(holding.getStock().getSector() + ": " + holding.getStock().getName() + ":" + holding.getAnnualisedReturn());
			if (holding.getAnnualisedReturn() > bestPerformingHolding.getAnnualisedReturn()) {
				bestPerformingHolding = holding;
			}
		}
		return bestPerformingHolding;
	}
}
