package my.app.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
public class StockCalculatedData {
	
	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "foreign",
	parameters = {@Parameter(name = "property", value = "stock")})
	private int stockId;
	
	@OneToOne
	@PrimaryKeyJoinColumn
	private Stock stock;

	private Double quarterlyAnnualisedReturn;
	private Double oneYearAnnualisedReturn;
	private Double fiveYearAnnualisedReturn;
	private Double tenYearAnnualisedReturn;
	private Double variance;
	
	public StockCalculatedData() {
		
	}
	
	public StockCalculatedData(Stock stock, Double quarterlyAnnualisedReturn, Double oneYearAnnualisedReturn,
			Double fiveYearAnnualisedReturn, Double tenYearAnnualisedReturn, Double variance) {
		this.stock = stock;
		this.quarterlyAnnualisedReturn = quarterlyAnnualisedReturn;
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
	
	public Double getQuarterlyAnnualisedReturn() {
		return quarterlyAnnualisedReturn;
	}
	
	public void setQuarterlyAnnualisedReturn(Double quarterlyAnnualisedReturn) {
		this.quarterlyAnnualisedReturn = quarterlyAnnualisedReturn;
	}
	
	public Double getOneYearAnnualisedReturn() {
		return oneYearAnnualisedReturn;
	}
	
	public void setOneYearAnnualisedReturn(Double oneYearAnnualisedReturn) {
		this.oneYearAnnualisedReturn = oneYearAnnualisedReturn;
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

	public Double getVariance() {
		return variance;
	}

	public void setVariance(Double variance) {
		this.variance = variance;
	}
	
	
	

}
