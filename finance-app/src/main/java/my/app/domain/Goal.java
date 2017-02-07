package my.app.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
	private Type type;
	
    @NotNull
    private Double goalTarget; 
    
    private Double goalTarget2;
    
	public Goal() {
		
	}
	
	public Goal(User user, Type type, Double goalTarget) {
		this.user = user;
		this.type = type;
		this.goalTarget = goalTarget;
	}
	
	public Goal(User user, Type type, Double goalTarget, Double goalTarget2) {
		this.user = user;
		this.type = type;
		this.goalTarget = goalTarget;
		this.setGoalTarget2(goalTarget2);
	}

	public int getId() {
		return id;
	}

	public User getUser() {
		return user;
	}
	
	public Type getGoal() {
		return type;
	}
	
	public Double getGoalTarget() {
		return goalTarget;
	}
	
	public void setGoalTarget(Double goalTarget) {
		this.goalTarget = goalTarget;
	}
	
	public Double getGoalTarget2() {
		return goalTarget2;
	}
	
	public void setGoalTarget2(Double goalTarget2) {
		if (this.type.equals(Type.MONTHLY_INVESTOR)) {
			this.goalTarget2 = goalTarget2;
		} else {
			//this goal type only has one target so the second target is set to null
			this.goalTarget2 = null;
		}
	}
}
