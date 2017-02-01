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
	private Double threeMonthVariance;
	@PercentageFormat
	private Double oneYearVariance;
	@PercentageFormat
	private Double threeYearVariance;
	@PercentageFormat
	private Double fiveYearVariance;
	@PercentageFormat
	private Double tenYearVariance;
	@PercentageFormat
	private Double oneYearTargetPriceEst;
	@PercentageFormat
	private Double oneYearROIEst;
	
	public StockMetrics() {
		
	}
	
	public StockMetrics(Stock stock, Double quarterlyAnnualisedReturn, Double oneYearAnnualisedReturn,
			Double fiveYearAnnualisedReturn, Double tenYearAnnualisedReturn, Double tenYearVariance) {
		this.stock = stock;
		this.threeMonthAnnualisedReturn = quarterlyAnnualisedReturn;
		this.oneYearAnnualisedReturn = oneYearAnnualisedReturn;
		this.setFiveYearAnnualisedReturn(fiveYearAnnualisedReturn);
		this.setTenYearAnnualisedReturn(tenYearAnnualisedReturn);
		this.tenYearVariance = tenYearVariance;
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

	public Double getThreeMonthVariance() {
		return threeMonthVariance;
	}

	public void setThreeMonthVariance(Double threeMonthVariance) {
		this.threeMonthVariance = threeMonthVariance;
	}

	public Double getOneYearVariance() {
		return oneYearVariance;
	}

	public void setOneYearVariance(Double oneYearVariance) {
		this.oneYearVariance = oneYearVariance;
	}

	public Double getThreeYearVariance() {
		return threeYearVariance;
	}

	public void setThreeYearVariance(Double threeYearVariance) {
		this.threeYearVariance = threeYearVariance;
	}

	public Double getFiveYearVariance() {
		return fiveYearVariance;
	}

	public void setFiveYearVariance(Double fiveYearVariance) {
		this.fiveYearVariance = fiveYearVariance;
	}

	public Double getTenYearVariance() {
		return tenYearVariance;
	}

	public void setTenYearVariance(Double tenYearVariance) {
		this.tenYearVariance = tenYearVariance;
	}

	public Double getOneYearTargetPriceEst() {
		return oneYearTargetPriceEst;
	}

	public void setOneYearTargetPriceEst(Double oneYearTargetPriceEst) {
		this.oneYearTargetPriceEst = oneYearTargetPriceEst;
	}

	public Double getOneYearROIEst() {
		return oneYearROIEst;
	}

	public void setOneYearROIEst(Double oneYearROIEst) {
		this.oneYearROIEst = oneYearROIEst;
	}
}
