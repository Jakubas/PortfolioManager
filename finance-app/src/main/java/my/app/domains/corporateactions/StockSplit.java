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
	@ManyToOne
	@JoinColumn(name = "stock_id")
	private Stock stock;
	
	public StockSplit() {}
	
	public StockSplit(LocalDate date, String split) {
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
	
	public double getTotalToDate() {
		return splitRatioToDate;
	}

	public void setTotalToDate(double splitRatioToDate) {
		this.splitRatioToDate = splitRatioToDate;
	}

	public Stock getStock() {
		return stock;
	}
}