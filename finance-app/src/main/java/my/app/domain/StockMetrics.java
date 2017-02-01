package my.app.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import my.app.formatter.PercentageFormat;

@Entity
public class StockMetrics {
	
	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "foreign",
	parameters = {@Parameter(name = "property", value = "stock")})
	private int stockId;
	
	@OneToOne
	@PrimaryKeyJoinColumn
	private Stock stock;
	
	@PercentageFormat
	private Double threeMonthAnnualisedReturn;
	@PercentageFormat
	private Double oneYearAnnualisedReturn;
	@PercentageFormat
	private Double threeYearAnnualisedReturn;
	@PercentageFormat
	private Double fiveYearAnnualisedReturn;
	@PercentageFormat
	private Double tenYearAnnualisedReturn;
	@PercentageFormat
	private Double variance;
	
	public StockMetrics() {
		
	}
	
	public StockMetrics(Stock stock, Double quarterlyAnnualisedReturn, Double oneYearAnnualisedReturn,
			Double fiveYearAnnualisedReturn, Double tenYearAnnualisedReturn, Double variance) {
		this.stock = stock;
		this.threeMonthAnnualisedReturn = quarterlyAnnualisedReturn;
		this.oneYearAnnualisedReturn = oneYearAnnualisedReturn;
		this.setFiveYearAnnualisedReturn(fiveYearAnnualisedReturn);
		this.setTenYearAnnualisedReturn(tenYearAnnualisedReturn);
		this.variance = variance;
	}
	
	public int getStockId() {
		return stockId;
	}
	
	public Stock getStock() {
		return stock;
	}
	
	public Double getThreeMonthAnnualisedReturn() {
		return threeMonthAnnualisedReturn;
	}
	
	public void setThreeMonthAnnualisedReturn(Double threeMonthAnnualisedReturn) {
		this.threeMonthAnnualisedReturn = threeMonthAnnualisedReturn;
	}
	
	public Double getOneYearAnnualisedReturn() {
		return oneYearAnnualisedReturn;
	}
	
	public void setOneYearAnnualisedReturn(Double oneYearAnnualisedReturn) {
		this.oneYearAnnualisedReturn = oneYearAnnualisedReturn;
	}
	
	public Double getThreeYearAnnualisedReturn() {
		return fiveYearAnnualisedReturn;
	}
	
	public void setThreeYearAnnualisedReturn(Double threeYearAnnualisedReturn) {
		this.threeYearAnnualisedReturn = threeYearAnnualisedReturn;
	}
	
	public Double getFiveYearAnnualisedReturn() {
		return fiveYearAnnualisedReturn;
	}

	public void setFiveYearAnnualisedReturn(Double fiveYearAnnualisedReturn) {
		this.fiveYearAnnualisedReturn = fiveYearAnnualisedReturn;
	}

	public Double getTenYearAnnualisedReturn() {
		return tenYearAnnualisedReturn;
	}

	public void setTenYearAnnualisedReturn(Double tenYearAnnualisedReturn) {
		this.tenYearAnnualisedReturn = tenYearAnnualisedReturn;
	}
}
