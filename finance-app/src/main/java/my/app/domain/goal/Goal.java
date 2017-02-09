package my.app.domain.goal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import my.app.domain.User;

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
    
	public Goal() {
		
	}
	
	public Goal(User user, Type type, String goalStr) {
		this.user = user;
		this.type = type;
		this.goalStr = goalStr;
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
	
}
