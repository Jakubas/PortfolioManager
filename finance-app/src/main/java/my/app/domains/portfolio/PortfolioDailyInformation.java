package my.app.domains.portfolio;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import my.app.domains.User;
import my.app.services.PortfolioService;
import my.app.services.PortfolioServiceImpl;

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
	@Column(unique = true)
	private LocalDate date;
	
	private double cashAmount;
	
	private double value;
	
	public PortfolioDailyInformation(User user, LocalDate date) {
		this.user = user;
		this.date = date;
		if (date.equals(LocalDate.now())) {
			this.cashAmount = user.getCashAmount();
		} else {
			this.cashAmount = 0;
		}
		PortfolioService portfolioService = new PortfolioServiceImpl();
		value = portfolioService.getValueOnDate(user.getPortfolio(), date) + cashAmount;
	}
	
	public PortfolioDailyInformation() { }

	public User getUser() {
		return user;
	}

	public LocalDate getDate() {
		return date;
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
}
