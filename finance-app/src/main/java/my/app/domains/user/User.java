package my.app.domains.user;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import my.app.domains.portfolio.PortfolioDailyInformation;
import my.app.domains.portfolio.StockInPortfolio;
import my.app.domains.portfolio.goal.Goal;
import my.app.domains.stock.Stock;
import my.app.formatter.PercentageFormat;
import my.app.risk.Risk;
import my.app.services.portfolio.PortfolioService;
import my.app.services.portfolio.PortfolioServiceImpl;

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
	private LocalDate dob;
	
	@NumberFormat(style = Style.NUMBER, pattern = "#,##0.00")
	private BigDecimal cash;
	
	@OneToMany(mappedBy = "user")
	private List<Goal> goals;
	
	@OneToMany(mappedBy = "user")
	private List<StockInPortfolio> portfolio;

	@OneToMany(mappedBy = "user")
	private List<PortfolioDailyInformation> pdis;
	
	@OneToMany(mappedBy = "user")
	private List<Tracker> trackingList;
	
	//a flag used to check if the portfolioDailyInformation needs to be updated.
	private boolean updatePortfolioInformation;
	
	//a flag that if set switches to table view for the user
	private boolean tableView;
	
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
	
	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob2) {
		this.dob = dob2;
	}
	
	public double getCashAmount() {
		return getCash();
	}
	
	public double getCash() {
		if (cash != null) {
			return cash.doubleValue();
		} else {
			return 0.0;
		}
	}

	public void setCash(Double cash) {
		this.cash = BigDecimal.valueOf(cash).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public void plusCash(double cash) {
		this.cash = this.cash.add(BigDecimal.valueOf(cash));
	}
	
	public void minusCash(double cash) {
		this.cash = this.cash.subtract(BigDecimal.valueOf(cash));
	}
	
	public List<Goal> getGoals() {
		return goals;
	}
	
	public List<StockInPortfolio> getActivePortfolio() {
		//filter sold stock
		List<StockInPortfolio> activePortfolio = new ArrayList<StockInPortfolio>();
		for (StockInPortfolio sip : portfolio) {
			if (!sip.isStockSold()) {
				activePortfolio.add(sip);
			}
		}
		return activePortfolio;
	}
	
	public List<StockInPortfolio> getPortfolio() {
		return portfolio;
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
	@NumberFormat(style = Style.NUMBER, pattern = "#,###.00")
	public double portfolioValue() {
		double value = 0;
		for (StockInPortfolio stockInPortfolio : getActivePortfolio()) {
			if (!stockInPortfolio.isStockSold()) {
				value += stockInPortfolio.getValue();
			}
		}
		value += this.getCash();
		return value;
	}
	
	//the value of all investments in a given sector from a user's portfolio
	public double sectorValue(String sector) {
		if (sector.equals("Cash")) {
			return this.getCash();
		}
		double value = 0;
		for (StockInPortfolio stockInPortfolio : getActivePortfolio()) {
			if (isInSector(stockInPortfolio, sector)) {
				value += stockInPortfolio.getValue();
			}
		}
		return value;
	}
	
	private double stockValue(Stock stock) {
		double value = 0;
		for (StockInPortfolio stockInPortfolio : getActivePortfolio()) {
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
		double portfolioValue = portfolioValue();
		double sectorValue = sectorValue(sector);
		double result = (portfolioValue != 0.0) ? sectorValue/portfolioValue*100 : 0;
		return result;
	}
	
	public double calculateSectorWeightWithoutCash(String sector) {
		double portfolioValue = portfolioValue() - this.getCash();
		double sectorValue = sectorValue(sector);
		double result = (portfolioValue != 0.0) ? sectorValue/portfolioValue*100 : 0;
		return result;
	}
	
	public double calculateStockWeight(Stock stock) {
		double portfolioValue = portfolioValue();
		double stockValue = stockValue(stock);
		double result = (portfolioValue != 0.0) ? stockValue/portfolioValue*100 : 0;
		return result;
	}
	
	public double calculateHoldingWeight(StockInPortfolio stockInPortfolio) {
		double portfolioValue = portfolioValue();
		double holdingValue = stockInPortfolio.getValue();
		double result = (portfolioValue != 0.0) ? holdingValue/portfolioValue*100 : 0;
		return result;
	}

	public String calculatePortfolioRisk() {
		return Risk.calculatePortfolioRisk(getActivePortfolio());
	}

	@PercentageFormat
	public double annualisedPortfolioPerformance() {
		PortfolioService portfolioService = new PortfolioServiceImpl();
		double annualisedPerformance = portfolioService.getAnnualisedReturn(getActivePortfolio());
		return annualisedPerformance;
	}
	
	public List<PortfolioDailyInformation> getPortfolioDailyInformations() {
		return pdis;
	}

	public List<Tracker> getTrackingList() {
		return trackingList;
	}

	public boolean isPortfolioInformationUpToDate() {
		return updatePortfolioInformation;
	}

	public void setUpdatePortfolioInformation(boolean updatePortfolioInformation) {
		this.updatePortfolioInformation = updatePortfolioInformation;
	}

	public boolean isTableView() {
		return tableView;
	}

	public void setTableView(boolean tableView) {
		this.tableView = tableView;
	}
}
