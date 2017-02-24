package my.app.domains.corporateactions;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import my.app.domains.Stock;

@Entity
public class Dividend {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	private LocalDate date;
	
	@NotNull
	private double amount;
	
	@NotNull
	private double totalToDate;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "stock_id")
	private Stock stock;
	
	public Dividend() {}
	
	public Dividend(Stock stock, LocalDate date, double amount) {
		this.stock = stock;
		this.date = date;
		this.amount = amount;
	}
	
	public int getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
	}

	public double getAmount() {
		return amount;
	}
	
	public double getTotalToDate() {
		return totalToDate;
	}

	public void setTotalToDate(double totalToDate) {
		this.totalToDate = totalToDate;
	}

	public Stock getStock() {
		return stock;
	}
}
