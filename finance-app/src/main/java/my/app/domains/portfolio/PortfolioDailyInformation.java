package my.app.domains.portfolio;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import my.app.domains.user.User;
import my.app.services.portfolio.PortfolioService;
import my.app.services.portfolio.PortfolioServiceImpl;

@Entity
public class PortfolioDailyInformation {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	
	private double cashAmount;
	
	private double value;
	
	private int day;
	
	public PortfolioDailyInformation(User user, LocalDate date, int day) {
		this.user = user;
		this.date = date;
		update(day);
	}
	
	public void update(int day) {
		this.day = day;
		if (date.equals(LocalDate.now())) {
			cashAmount = user.getCash();
		}
		PortfolioService portfolioService = new PortfolioServiceImpl();
		value = portfolioService.getValueOnDate(user.getPortfolio(), date);
	}
	
	public PortfolioDailyInformation() { }

	public int getId() {
		return id;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getCashAmount() {
		return cashAmount;
	}
	
	public void setCashAmount(double cashAmount) {
		this.value -= this.cashAmount;
		this.value += cashAmount;
		this.cashAmount = cashAmount;
	}

	public double getValue() {
		return value;
	}
	
	public int getDay() {
		return day;
	}
}
