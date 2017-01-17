package my.app.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class StockDailyInformation {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	
	@NotNull
	private double open;
	
	@NotNull
	private double close;
	
	@NotNull
	private double high;
	
	@NotNull
	private double low;
	
	@NotNull
	private int volume;
	
	@NotNull
	private double adjustedClose;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "stock_id")
	private Stock stock;

	public StockDailyInformation() {
		
	}
	
	public StockDailyInformation(Date date, double open, double close, double high,
			double low, int volume, double adjustedClose, Stock stock) {
		this.date = date;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
		this.volume = volume;
		this.adjustedClose = adjustedClose;
		this.stock = stock;
	}
	
	public int getId() {
		return id;
	}
	
	public Date getDate() {
		return date;
	}
	
	public double getOpen() {
		return open;
	}

	public double getClose() {
		return close;
	}
	
	public double getHigh() {
		return high;
	}
	
	public double getLow() {
		return low;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public double getAdjustedClose() {
		return adjustedClose;
	}
	
	public Stock getStock() {
		return stock;
	}
	
    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof StockDailyInformation))
            return false;
        if (obj == this)
            return true;

        StockDailyInformation stockInfo = (StockDailyInformation) obj;
        if (date.getTime() == stockInfo.getDate().getTime() && open == stockInfo.getOpen() &&
        	close == stockInfo.getClose() && high == stockInfo.getHigh() &&
        	low == stockInfo.getLow() && volume == stockInfo.getVolume()) {
        	return true;
        } else {
        	return false;
        }
    }
    
    @Override
    public int hashCode() {
    	HashCodeBuilder builder = new HashCodeBuilder();
    	builder.append(date.getTime());
    	builder.append(open);
    	builder.append(close);
    	builder.append(high);
    	builder.append(low);
    	builder.append(volume);
    	return builder.toHashCode();   
    }
}
