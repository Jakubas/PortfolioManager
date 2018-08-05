package my.app.domains.stock;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.apache.commons.lang3.builder.HashCodeBuilder;
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
	
	public StockMetrics() {
		
	}
	
	public StockMetrics(Stock stock, Double threeMonthAnnualisedReturn, Double oneYearAnnualisedReturn, 
			Double threeYearAnnualisedReturn, Double fiveYearAnnualisedReturn, Double tenYearAnnualisedReturn,
			Double threeMonthVariance, Double oneYearVariance, Double threeYearVariance, 
			Double fiveYearVariance, Double tenYearVariance) {
		this.stock = stock;
		this.threeMonthAnnualisedReturn = threeMonthAnnualisedReturn;
		this.oneYearAnnualisedReturn = oneYearAnnualisedReturn;
		this.threeYearAnnualisedReturn = threeYearAnnualisedReturn;
		this.fiveYearAnnualisedReturn = fiveYearAnnualisedReturn;
		this.tenYearAnnualisedReturn = tenYearAnnualisedReturn;
		this.threeMonthVariance = threeMonthVariance;
		this.oneYearVariance = oneYearVariance;
		this.threeYearVariance = threeYearVariance;
		this.fiveYearVariance = fiveYearVariance;
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
	
	public void setValuesFrom(StockMetrics metrics) {
		this.threeMonthAnnualisedReturn = metrics.getThreeMonthAnnualisedReturn();
		this.oneYearAnnualisedReturn = metrics.getOneYearAnnualisedReturn();
		this.threeYearAnnualisedReturn = metrics.getThreeYearAnnualisedReturn();
		this.fiveYearAnnualisedReturn = metrics.getFiveYearAnnualisedReturn();
		this.tenYearAnnualisedReturn = metrics.getTenYearAnnualisedReturn();
		this.threeMonthVariance = metrics.getThreeMonthVariance();
		this.oneYearVariance = metrics.getOneYearVariance();
		this.threeYearVariance = metrics.getThreeYearVariance();
		this.fiveYearVariance = metrics.getFiveYearVariance();
		this.tenYearVariance = metrics.getTenYearVariance();
	}
	
	public boolean isEqual(Double a, Double b) {
		if (a == null) {
			return b == null;
		} else {
			return a.equals(b);
		}
	}
	
	@Override
    public boolean equals(Object obj) {
       if (!(obj instanceof StockMetrics))
            return false;
        if (obj == this)
            return true;

        StockMetrics metrics = (StockMetrics) obj;
        if (isEqual(threeMonthAnnualisedReturn, metrics.getThreeMonthAnnualisedReturn()) &&
    		isEqual(oneYearAnnualisedReturn, metrics.getOneYearAnnualisedReturn()) &&
    		isEqual(threeYearAnnualisedReturn, metrics.getThreeYearAnnualisedReturn()) &&	
    		isEqual(fiveYearAnnualisedReturn, metrics.getFiveYearAnnualisedReturn()) &&	
    		isEqual(tenYearAnnualisedReturn, metrics.getTenYearAnnualisedReturn()) &&
    		isEqual(threeMonthVariance, metrics.getThreeMonthVariance()) &&
    		isEqual(oneYearVariance, metrics.getOneYearVariance()) &&
    		isEqual(threeYearVariance, metrics.getThreeYearVariance()) &&
    		isEqual(fiveYearVariance, metrics.getFiveYearVariance()) &&
    		isEqual(tenYearVariance, metrics.getTenYearVariance())
        	) {
        	return true;
        } else {
        	return false;
        }
    }
    
	@Override
	public int hashCode() {
    	HashCodeBuilder builder = new HashCodeBuilder();
    	builder.append(threeMonthAnnualisedReturn);
    	builder.append(oneYearAnnualisedReturn);
    	builder.append(threeYearAnnualisedReturn);
    	builder.append(fiveYearAnnualisedReturn);
    	builder.append(tenYearAnnualisedReturn);
    	builder.append(threeMonthVariance);
    	builder.append(oneYearVariance);
    	builder.append(threeYearVariance);
    	builder.append(fiveYearVariance);
    	builder.append(tenYearVariance);
    	return builder.toHashCode();   
    }
}
