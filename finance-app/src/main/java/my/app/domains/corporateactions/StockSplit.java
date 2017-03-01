package my.app.domains.corporateactions;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import my.app.domains.stock.Stock;

@Entity
public class StockSplit {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	private LocalDate date;
	
	@NotNull
	private String split;
	
	@NotNull
	private double splitRatio;
	
	@NotNull
	private double splitRatioToDate;
	
	@NotNull
	private double splitRatioToDateReversed;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "stock_id")
	private Stock stock;
	
	public StockSplit() {}
	
	public StockSplit(Stock stock, LocalDate date, String split) {
		this.stock = stock;
		this.date = date;
		this.splitRatio = splitToRatio(split);
		this.split = split;
	}
	
	private double splitToRatio(String split) {
		String[] numStrings = split.split(":");
		double numerator = Double.valueOf(numStrings[0]);
		double denominator = Double.valueOf(numStrings[1]);
		return numerator/denominator;
	}

	public int getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getSplit() {
		return split;
	}
	
	public double getSplitRatio() {
		return splitRatio;
	}
	
	public double getSplitRatioToDate() {
		return splitRatioToDate;
	}

	public void setSplitRatioToDate(double splitRatioToDate) {
		this.splitRatioToDate = splitRatioToDate;
	}
	
	public double getSplitRatioToDateReversed() {
		return splitRatioToDateReversed;
	}

	public void setSplitRatioToDateReversed(double splitRatioToDateReversed) {
		this.splitRatioToDateReversed = splitRatioToDateReversed;
	}

	public Stock getStock() {
		return stock;
	}
}