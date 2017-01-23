package my.app.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class User {

	//For registration only the first name, user name and password is required, other fields are optional
	@NotNull
	private String firstName;

	private String lastName;
	
	@NotNull
	private String userName;
	
	@NotNull
	private String passwordHash;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dob;
	
	private Long cashAmount;
	
	private String goal;
	
	public User() {
		
	}
	
	public User(String firstName, String userName, String passwordHash) {
		this.firstName = firstName;
		this.userName = userName;
		this.passwordHash = passwordHash;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}
	
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Long getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(Long cashAmount) {
		this.cashAmount = cashAmount;
	}
	
	public String getGoal() {
		return goal;
	}
	
	public void setGoal(String goal) {
		this.goal = goal;
	}
}
