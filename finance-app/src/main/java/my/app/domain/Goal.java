package my.app.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

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
    
    @NotNull
    private String goalTarget;
    
    @NotNull
    private String goalTarget2;
    
    @NotNull
    private String goalTarget3;
    
	public Goal() {
		
	}
	
	public Goal(User user, Type type, String goalStr, String goalTarget) {
		this.user = user;
		this.type = type;
		this.goalStr = goalStr;
		this.goalTarget = goalTarget;
	}
	
	public Goal(User user, Type type, String goalStr, String goalTarget, String goalTarget2) {
		this.user = user;
		this.type = type;
		this.goalStr = goalStr;
		this.goalTarget = goalTarget;
		this.goalTarget2 = goalTarget2;
	}
	
	public Goal(User user, Type type, String goalStr, String goalTarget, String goalTarget2, String goalTarget3) {
		this.user = user;
		this.type = type;
		this.goalStr = goalStr;
		this.goalTarget = goalTarget;
		this.goalTarget2 = goalTarget2;
		this.goalTarget3 = goalTarget3;
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

	public String getGoalTarget() {
		return goalTarget;
	}

	public void setGoalTarget(String goalTarget) {
		this.goalTarget = goalTarget;
	}

	public String getGoalTarget2() {
		return goalTarget2;
	}

	public void setGoalTarget2(String goalTarget2) {
		this.goalTarget2 = goalTarget2;
	}

	public String getGoalTarget3() {
		return goalTarget3;
	}

	public void setGoalTarget3(String goalTarget3) {
		this.goalTarget3 = goalTarget3;
	}
}
