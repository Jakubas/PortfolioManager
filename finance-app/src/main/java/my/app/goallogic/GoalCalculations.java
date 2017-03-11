package my.app.goallogic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import my.app.domains.portfolio.StockInPortfolio;
import my.app.domains.portfolio.goal.Goal;
import my.app.domains.portfolio.goal.Goal.Type;
import my.app.domains.stock.Stock;
import my.app.domains.user.User;

public class GoalCalculations {
	
	
	public static List<String> getBalancingTips(User user) {
		List<String> balancingTips = new ArrayList<String>();
		double totalCashNeeded = getCashNeededForBalancing(user);
		double cash = user.getCash();
		double cashNeeded = totalCashNeeded - cash;
		List<Stock> stockToSell = stockToSell(user, cashNeeded);
		
		//You need $x to rebalance your portfolio.
		balancingTips.add("You need $" + totalCashNeeded + " to rebalance your portfolio.");
		//You have $x in cash, you need $y more.
		balancingTips.add("You have $" + cash + " in cash, you need $" + cashNeeded + " more.");
		//I suggest you sell: 
		String stockToSellString = "";
		for (Stock stock : stockToSell) {
			stockToSellString += "<br /> - " + stock.getTicker();
		}
		balancingTips.add("Based on poor performance, I suggest you sell: " + stockToSellString);
		// - list of stocks you suggest to sell
		return balancingTips;
	}
	
	public static double getCashNeededForBalancing(User user) {
		List<Goal> goals = user.getGoals();
		goals.removeIf(o -> !o.getType().equals(Type.SECTOR));
		double cashNeeded = 0.0;
		for (Goal goal : goals) {
			double currentPercentage = user.calculateSectorWeight(goal.getSector1());
			double diff = goal.getPercentage() - currentPercentage;
			double diffValue = (diff/100) * user.portfolioValue();
			cashNeeded += diffValue;
		}
		return cashNeeded;
	}
	
	//returns a list of sectors that are in active sector goals
	public static List<String> getSectorsBeingBalanced(User user) {
		List<Goal> goals = user.getGoals();
		List<String> sectors = new ArrayList<String>();
		for(Goal goal : goals) {
			if (goal.getType().equals(Type.SECTOR)) {
				sectors.add(goal.getSector1());
			}
		}
		return sectors;
	}
	
	public static List<String> getSectorsNotBeingBalanced(User user) {
		List<Goal> goals = user.getGoals();
		List<String> sectors = new ArrayList<String>();
		for(Goal goal : goals) {
			if (!goal.getType().equals(Type.SECTOR)) {
				sectors.add(goal.getSector1());
			}
		}
		return sectors;
	}
	
	private static double calculateSectorsWeight(User user, List<String> sectors) {
		double weight = 0.0;
		for(String sector : sectors) {
			weight += user.calculateSectorWeight(sector);
		}
		return weight;
	}
	
	public static double targetPercentage(User user) {
		List<String> activeSectors = getSectorsBeingBalanced(user);
		List<String> inactiveSectors = getSectorsNotBeingBalanced(user);
		//(100 -  total weight of active goal sectors) / number of inactive sectors
		return (100 - calculateSectorsWeight(user, activeSectors)) / inactiveSectors.size();
	}
	
	public static List<String> sectorsToSellFor(User user) {
		List<String> inactiveSectors = getSectorsNotBeingBalanced(user);
		double inactiveTargetPercentage = targetPercentage(user);
		List<String> sectorsToSell = new ArrayList<String>();
		for (String sector : inactiveSectors) {
			double sectorWeight = user.calculateSectorWeight(sector);
			if (sectorWeight > inactiveTargetPercentage) {
				sectorsToSell.add(sector);
			}
		}
		return sectorsToSell;
	}
	
	public static List<Stock> stockToSell(User user, double cashNeeded) {
		List<String> sectorsToSell = getSectorsNotBeingBalanced(user);
		List<Stock> stockToSell = new ArrayList<Stock>();
		List<StockInPortfolio> portfolio = user.getActivePortfolio();
		portfolio.removeIf(o -> !sectorsToSell.contains(o.getStock().getSector()));
		portfolio.sort(Comparator.comparing(StockInPortfolio::getAnnualisedReturn));
		for (StockInPortfolio holding : portfolio) {
			if (cashNeeded <= 0) {
				break;
			} 
			stockToSell.add(holding.getStock());
			cashNeeded -= holding.getValue();
		}
		return stockToSell;
	}
}
