package my.app.domain.goal;

import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import my.app.controller.Utility;
import my.app.domain.User;
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
    
    private Date startDate;
    
    private Double sector1StartWeight;
    
    private Double sector2StartWeight;
    
	public Goal() {
		
	}
	
	public Goal(User user, Type type, String goalStr) {
		this.user = user;
		this.type = type;
		this.goalStr = goalStr;
		setTypeSpecificFields();
	}

	private void setTypeSpecificFields() {
		if (type == Type.MOVE) {
			sector1StartWeight = user.calculateSectorWeight(sector1);
			sector2StartWeight = user.calculateSectorWeight(sector2);
		} else if (type == Type.RETIRE || type == Type.INVEST_TIME_LENGTH) {
			startDate = new Date();
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
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
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
	
	public Double[] getProgress() {
		
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
			progressValue = user.calculateSectorWeight(sector1);
			progressGoal = percentage;
			break;
		case RISK:
			break;
		}
		
		Double progressPercentage = null;
		if (progressValue != null && progressGoal != null) {
			progressPercentage = Math.abs((progressValue/progressGoal) * 100); 
		}		
		Double[] progress = new Double[3];
		progress[0] = progressValue;
		progress[1] = progressGoal;
		progress[2] = progressPercentage;
		return progress;
	}
	
	public List<String> getTips() {
		List<String> tips = new ArrayList<String>();
		switch(type) {
		case GROW_TO_AMOUNT:
			user.annualisedPortfolioPerformance();
			tips.add("Based on your portfolio's historical performance you should reach this goal in x years.");
			tips.add("Disclaimer: past performance does not guarantee future results.");
			break;
		case INVEST_TIME_LENGTH:
			tips.add("x (years/months) have passed so far");
			break;
		case MONTHLY_INVESTOR:
			tips.add("Based on your monthly deposit amount and portfolio's historical performance you should reach this goal in x years.");
			tips.add("Due to market volatility this is only an estimate. The lower the risk of your portfolio than the more accurate the estimate.");
			break;
		case MOVE:
			break;
		case RETIRE:
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
			double currentPercentage = user.calculateSectorWeight(sector1);
			double diff = Math.abs(percentage - currentPercentage);
			double diffValue = (diff/100) * user.portfolioValue();
			double percentageDiff = 1 - (percentage/currentPercentage);
			if (currentPercentage < percentage) {
				tips.add("You need to increase holdings of " + sector1 + " sector by " + diff + "%");
				
				tips.add("This can be accomplished by buying " + percentageDiff + "% ($" + diffValue + ") more of stock in " + sector1 + " sector");
			} else {
				tips.add("You need to decrease holdings of " + sector1 + " sector by " + diff + "%");
				tips.add("This can be accomplished by selling " + percentageDiff + "% ($" + diffValue + ") of your stock holdings in " + sector1 + " sector");
			}
			break;
		}
		return tips;
	}

	//calculate the amount of time elapsed (in years) from the start date to today's date
	private double calculateElapsedTime() {
		Date currentDate = new Date();
		Period period = Period.between(Utility.fromDate(startDate), Utility.fromDate(currentDate)); 
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
