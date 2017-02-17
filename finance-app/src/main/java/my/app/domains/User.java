package my.app.domains;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
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

import my.app.domains.goal.Goal;
import my.app.risk.Risk;

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
	
	private Double cashAmount;
	
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

	public String getUsername() {
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

	public Double getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(Double cashAmount) {
		this.cashAmount = cashAmount;
	}
	
	public List<Goal> getGoals() {
		return goals;
	}
	
	public List<StockInPortfolio> getPortfolio() {
		//filter sold stock
		List<StockInPortfolio> activePortfolio = new ArrayList<StockInPortfolio>();
		for (StockInPortfolio sip : portfolio) {
			if (!sip.isStockSold()) {
				activePortfolio.add(sip);
			}
		}
		return activePortfolio;
	}
	
	public List<StockInPortfolio> getHistoricalPortfolio() {
		List<StockInPortfolio> historicalPortfolio = new ArrayList<StockInPortfolio>();
		for (StockInPortfolio sip : portfolio) {
			if (sip.isStockSold()) {
				historicalPortfolio.add(sip);
			}
		}
		return historicalPortfolio;
	}
	
	//the value of all investments in the user's portfolio
	public double portfolioValue() {
		double value = 0;
		for (StockInPortfolio stockInPortfolio : getPortfolio()) {
			if (!stockInPortfolio.isStockSold()) {
				value += stockInPortfolio.getValue();
			}
		}
		value = (cashAmount != null) ? value + cashAmount : value;
		return value;
	}
	
	//the value of all investments in a given sector from a user's portfolio
	public double sectorValue(String sector) {
		if (sector == "Cash") {
			return cashAmount;
		}
		double value = 0;
		for (StockInPortfolio stockInPortfolio : getPortfolio()) {
			if (isInSector(stockInPortfolio, sector)) {
				value += stockInPortfolio.getValue();
			}
		}
		return value;
	}
	
	private double stockValue(Stock stock) {
		double value = 0;
		for (StockInPortfolio stockInPortfolio : getPortfolio()) {
			if (isStock(stockInPortfolio, stock)) {
				value += stockInPortfolio.getValue();
			}
		}
		return value;
	}

	private boolean isStock(StockInPortfolio stockInPortfolio, Stock stock) {
		return stockInPortfolio.getStock() == stock;
	}

	private boolean isInSector(StockInPortfolio stockInPortfolio, String sector) {
		String stockSector = stockInPortfolio.getStock().getSector();
		return stockSector.equals(sector);
	}

	public double calculateSectorWeight(String sector) {
		double porfolioValue = portfolioValue();
		double sectorValue = sectorValue(sector);
		return sectorValue/porfolioValue*100;
	}
	
	public double calculateStockWeight(Stock stock) {
		double porfolioValue = portfolioValue();
		double stockValue = stockValue(stock);
		return stockValue/porfolioValue*100;
	}
	
	public double calculateHoldingWeight(StockInPortfolio stockInPortfolio) {
		double porfolioValue = portfolioValue();
		double holdingValue = stockInPortfolio.getValue();
		return holdingValue/porfolioValue*100;
	}

	public String calculatePortfolioRisk() {
		return Risk.calculatePortfolioRisk(getPortfolio());
	}

	//based on current portfolio performance
	public double annualisedPortfolioPerformance() {
		double annualisedPerformance = 0;
		for (StockInPortfolio stockInPortfolio : getPortfolio()) {
			Double annualisedReturn = stockInPortfolio.getAnnualisedReturn();
			if (annualisedReturn == null) {
				annualisedReturn = 0.0;
			}
			Double annualisedReturnWeighted = annualisedReturn * (calculateHoldingWeight(stockInPortfolio)/100); 
			annualisedPerformance += annualisedReturnWeighted;
		}
		return annualisedPerformance;
	}
}
