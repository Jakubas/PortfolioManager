package my.app.domain;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import my.app.domain.goal.Goal;

@Entity
public class User {

	//For registration only the first name, user name and password is required, other fields are optional
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	private String firstName;

	private String lastName;
	
	@NotNull
	@NotEmpty
	@Column(unique=true)
	private String username;
	
	@NotNull
	private String passwordHash;
	
	@NotNull
	private boolean isAdmin;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dob;
	
	private double cashAmount;
	
	@OneToMany(mappedBy = "user")
	private List<Goal> goals;
	
	
	@OneToMany(mappedBy = "user")
	private List<StockInPortfolio> portfolio;

	public User() {
		
	}
	
	public User(String firstName, String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		this.firstName = firstName;
		this.username = username;
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		this.passwordHash = passwordEncoder.encode(password);
		this.isAdmin = false;
	}
	
	public int getId() {
		return id;
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
		return username;
	}
	
	public String getPasswordHash() {
		return passwordHash;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}
	
	public void setAdminPrivileges(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public double getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(Long cashAmount) {
		this.cashAmount = cashAmount;
	}
	
	public List<Goal> getGoals() {
		return goals;
	}
	
	public List<StockInPortfolio> getPortfolio() {
		return portfolio;
	}
	
	//the value of all investments in the user's portfolio
	public double portfolioValue() {
		double value = 0;
		for (StockInPortfolio stockInPortfolio : portfolio) {
			value += stockInPortfolio.getValue();
		}
		return value + cashAmount;
	}
	
	//the value of all investments in a given sector from a user's portfolio
	public double sectorValue(String sector) {
		double value = 0;
		for (StockInPortfolio stockInPortfolio : portfolio) {
			if (isInSector(stockInPortfolio, sector)) {
				value += stockInPortfolio.getValue();
			}
		}
		return value + cashAmount;
	}

	private boolean isInSector(StockInPortfolio stockInPortfolio, String sector) {
		String stockSector = stockInPortfolio.getStock().getSector();
		return stockSector.equals(sector);
	}

	public double calculateSectorWeight(String sector) {
		double porfolioValue = portfolioValue();
		double sectorValue = sectorValue(sector);
		return sectorValue/porfolioValue;
	}
}
