package my.app.domains.goal;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import my.app.domains.User;
import my.app.risk.RiskValues;

@Entity
public class Goal {
	
	public enum Type {
		SECTOR,
		MOVE,
		RETIRE,
		GROW_TO_AMOUNT,
		INVEST_TIME_LENGTH,
		RISK,
		MONTHLY_INVESTOR
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
    
	@NotNull
	private Type type;
	
    @NotNull
    private String goalStr;
    
    //Since each goal has different targets most of the fields listed below here will be null
	private Double percentage;
	
    private String sector1;

    private String sector2;
    
    private Integer monthlyDepositAmount;
    
	private Integer amount;
	
    private Double length;
    
    private String risk;
    
    private String startRisk;
    
    private LocalDate startDate;
    
    private Double sector1StartWeight;
    
    private Double sector2StartWeight;
    
	public Goal() {
		
	}
	
	public Goal(User user, Type type, String goalStr) {
		this.user = user;
		this.type = type;
		this.goalStr = goalStr;
	}

	public void setTypeSpecificFields() {
		if (type == Type.MOVE) {
			sector1StartWeight = user.calculateSectorWeight(sector1);
			sector2StartWeight = user.calculateSectorWeight(sector2);
		} else if (type == Type.RETIRE || type == Type.INVEST_TIME_LENGTH) {
			startDate = LocalDate.now();
		} else if (type == Type.RISK) {
			startRisk = user.calculatePortfolioRisk();
		}
	}

	public int getId() {
		return id;
	}
	
	public Type getType() {
		return type;
	}

	public User getUser() {
		return user;
	}
	
	public String getGoalStr() {
		return goalStr;
	}
	
	public void setGoalStr(String goalStr) {
		this.goalStr = goalStr;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public String getSector1() {
		return sector1;
	}

	public void setSector1(String sector1) {
		this.sector1 = sector1;
	}

	public String getSector2() {
		return sector2;
	}

	public void setSector2(String sector2) {
		this.sector2 = sector2;
	}

	public Integer getMonthlyDepositAmount() {
		return monthlyDepositAmount;
	}

	public void setMonthlyDepositAmount(Integer monthlyDepositAmount) {
		this.monthlyDepositAmount = monthlyDepositAmount;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}
	
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	public Double getSector1StartWeight() {
		return sector1StartWeight;
	}
	
	public Double getSector2StartWeight() {
		return sector2StartWeight;
	}
	
	public String startRisk() {
		return startRisk;
	}
	
	public String[] getProgress() {
		
		String[] progress = new String[3];
		//the current progress
		Double progressValue = null; 
		//the goal that is to be achieved
		Double progressGoal = null;
		switch(type) {
		case GROW_TO_AMOUNT:
			progressValue = user.portfolioValue();
			progressGoal = Double.valueOf(amount);
			break;
		case INVEST_TIME_LENGTH:
			progressValue = calculateElapsedTime();
			progressGoal = length;
			break;
		case MONTHLY_INVESTOR:
			progressValue = user.portfolioValue();
			progressGoal = Double.valueOf(amount);
			break;
		case MOVE:
			break;
		case RETIRE:
			progressValue = calculateElapsedTime();
			progressGoal = length;
			break;
		case SECTOR:
			if (user.calculateSectorWeight(sector1) < percentage * 1.05) {
				progressValue = user.calculateSectorWeight(sector1);
				progressGoal = percentage; 
			} else {
				//progressGoal = user.calculateSectorWeight(sector1);
				//progressValue = percentage; 
				progress[0] = String.valueOf(user.calculateSectorWeight(sector1));
				progress[1] = percentage.toString();
				progress[2] = String.valueOf(Math.abs(percentage/user.calculateSectorWeight(sector1) * 100)); 
			}
			break;
		case RISK:
			break;
		}
		
		
		Double progressPercentage = null;
		if (progressValue != null && progressGoal != null) {
			progressPercentage = Math.abs(progressValue/progressGoal * 100); 
		
			String progressPercentageStr = null;
			if (progressPercentage != null) {
				if(progressPercentage >= 100) {
					progressPercentageStr = "100";
				} else {
					progressPercentageStr = String.valueOf(progressPercentage);
				}
			}
			progress[0] = progressValue.toString();
			progress[1] = progressGoal.toString();
			progress[2] = progressPercentageStr;
		}
		return progress;
	}
	
	public List<String> getTips() {
		List<String> tips = new ArrayList<String>();
		switch(type) {
		case GROW_TO_AMOUNT:
			double annualReturn = user.annualisedPortfolioPerformance();
			double monthlyReturn = Math.pow((1 + annualReturn), (1.0/12.0)) - 1; 
			double portfolioValue = user.portfolioValue();
			Double yearsToReach = growthTime(portfolioValue, amount, monthlyReturn);
			if (yearsToReach != null) {
				String yearsToReachStr = String.format("%.1f", yearsToReach);
				tips.add("Based on your portfolio's historical performance you should reach this goal in  " + yearsToReachStr + "  years.");
				tips.add("Disclaimer: past performance does not guarantee future results.");
			} else {
				tips.add("This goal will take over 200 years to reach");
			}
			break;
		case INVEST_TIME_LENGTH:
			if (length < 1) {
				String elapsedMonths = String.format("%.2f", calculateElapsedTime()*12);
				tips.add(elapsedMonths + " months have passed so far");
			} else {
				String elapsedYears = String.format("%.1f", calculateElapsedTime());
				tips.add(elapsedYears + " years have passed so far");
			}
			break;
		case MONTHLY_INVESTOR:
			annualReturn = user.annualisedPortfolioPerformance();
			monthlyReturn = Math.pow((1 + annualReturn), (1/12)) - 1; 
			portfolioValue = user.portfolioValue();
			String yearsToReachStr = String.format("%.1f", growthTime(portfolioValue, amount, monthlyReturn, monthlyDepositAmount));
			tips.add("Based on your monthly deposit amount and portfolio's historical performance you should reach this goal in " + yearsToReachStr + " years.");
			tips.add("Due to market volatility this is only an estimate. The lower the risk of your portfolio than the more accurate the estimate.");
			break;
		case MOVE:
			double currentPercentage = user.calculateSectorWeight(sector1);
			double goalPercentage = sector1StartWeight - percentage;
			double diff = Math.abs(goalPercentage - currentPercentage);
			String diffValue = String.format("%.2f", (diff/100) * user.portfolioValue());
			String percentageDiff = String.format("%.2f", (1.0 - (goalPercentage/currentPercentage))*100);
			if (currentPercentage < goalPercentage) {
				tips.add("You need to increase your portfolio weight in " + sector1 + " by " + diff + "%");
				tips.add("This can be accomplished by buying $" + diffValue + " (" + percentageDiff + "%) of stock in the " + sector1 + " sector");
			} else {
				tips.add("You need to decrease your portfolio weight in " + sector1 + " by " + diff + "%");
				tips.add("This can be accomplished by selling $" + diffValue + " (" + percentageDiff + "%) of your stock holdings in the " + sector1 + " sector");
			}
			currentPercentage = user.calculateSectorWeight(sector2);
			goalPercentage = sector2StartWeight + percentage;
			diff = Math.abs(goalPercentage - currentPercentage);
			diffValue = String.format("%.2f", (diff/100) * user.portfolioValue());
			percentageDiff = String.format("%.2f", (1.0 - (goalPercentage/currentPercentage))*100);
			if (currentPercentage < goalPercentage) {
				tips.add("You need to increase your portfolio weight in " + sector2 + " by " + diff + "%");
				tips.add("This can be accomplished by buying $" + diffValue + " (" + percentageDiff + "%) of stock in the " + sector2 + " sector");
			} else {
				tips.add("You need to decrease your portfolio weight in " + sector2 + " sector by " + diff + "%");
				tips.add("This can be accomplished by selling $" + diffValue + " (" + percentageDiff + "%) of your stock holdings in the " + sector2 + " sector");
			}
			break;
		case RETIRE:
			double elapsedYears = calculateElapsedTime();
			String elapsedYearsStr = String.format("%.0f", elapsedYears);
			tips.add(elapsedYearsStr + " years have passed so far");
			if (user.getDob() != null) {
				long monthsToRetirement = Math.round((length - elapsedYears) * 12.0);
				LocalDate retirementDay = LocalDate.now().plusMonths(monthsToRetirement);
				LocalDate birthday = user.getDob();
				int ageOfRetirement = Period.between(birthday, retirementDay).getYears();
				tips.add("You plan to retire at the age of " + ageOfRetirement);
			} else {
				long monthsToRetirement = Math.round((length - elapsedYears) * 12.0);
				LocalDate retirementDay = LocalDate.now().plusMonths(monthsToRetirement);
				int yearOfRetirement = retirementDay.getYear();
				tips.add("You plan to retire in " + yearOfRetirement);
			}
			break;
		case RISK:
			String currentRisk = user.calculatePortfolioRisk();
			tips.add("Current risk is: " + currentRisk);
			if (riskToNumber(currentRisk) < riskToNumber(risk)) {
				tips.add("To achieve this goal you need to buy riskier stock and sell some of your less risky stock holdings");
			} else {
				tips.add("To achieve this goal you need to sell some of your riskier stock holdings and buy less risky stock");
			}
			break;
		case SECTOR:
			currentPercentage = user.calculateSectorWeight(sector1);
			diff = Math.abs(percentage - currentPercentage);
			diffValue = String.format("%.2f", (diff/100) * user.portfolioValue());
			percentageDiff = String.format("%.2f", (1.0 - (percentage/currentPercentage))*100);
			if (currentPercentage < percentage) {
				tips.add("You need to increase your portfolio weight in " + sector1 + " by " + diff + "%");
				
				tips.add("This can be accomplished by buying $" + diffValue + " (" + percentageDiff + "%) of stock in the " + sector1 + " sector");
			} else {
				tips.add("You need to decrease your portfolio weight in " + sector1 + " by " + diff + "%");
				tips.add("This can be accomplished by selling $" + diffValue + " (" + percentageDiff + "%) of your stock holdings in the " + sector1 + " sector");
			}
			break;
		}
		return tips;
	}

	//calculates the years required to reach the goal based on compounding monthly returns
	private Double growthTime(double startAmount, double goalAmount, double monthlyReturn) {
		return growthTime(startAmount, goalAmount, monthlyReturn, 0);
	}
	
	//same as above but with an additional parameter for monthly deposits
	private Double growthTime(double startAmount, double goalAmount, double monthlyReturn, double monthlyDeposit) {
		monthlyReturn = monthlyReturn + 1;
		//checks if return rate is negative
		if (startAmount * monthlyReturn < startAmount) {
			return null;
		}
		
		//a maximum amount of 2400 months/200 years
		int maxLength = 2400;
		int i = 0;
		for (; startAmount < goalAmount && i < maxLength; i++) {
			startAmount = startAmount * monthlyReturn;
			startAmount += monthlyDeposit;
		}
		Double result = (i < maxLength) ? (double) i/12 : null;
		return result;
	}

	//calculate the amount of time elapsed (in years) from the start date to today's date
	private double calculateElapsedTime() {
		LocalDate today = LocalDate.now();
		Period period = Period.between(startDate, today); 
		int months = period.getMonths();
		return ((double) months/12.0);
	}
	
	private int riskToNumber(String risk) {
		List<String> risks = RiskValues.RISKS;
		int i = 1;
		for(String risk2 : risks) {
			if (risk.equals(risk2)) {
				return i;
			}
			i++;
		}
		return 0;
	}
}
