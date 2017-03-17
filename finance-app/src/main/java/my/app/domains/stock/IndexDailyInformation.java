package my.app.domains.stock;

import java.time.LocalDate;

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
public class IndexDailyInformation {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;
	
	@NotNull
	private double open;
	
	@NotNull
	private double close;
	
	@NotNull
	private double high;
	
	@NotNull
	private double low;
	
	@NotNull
	private long volume;
	
	//the closing price adjusted by dividends (reinvested back into the stock) and stock splits
	@NotNull
	private double adjustedClose;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "index_id")
	private Index index;

	public IndexDailyInformation() {
		
	}
	
	public IndexDailyInformation(LocalDate date, double open, double close, double high,
			double low, long volume, double adjustedClose, Index index) {
		this.date = date;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
		this.volume = volume;
		this.adjustedClose = adjustedClose;
		this.index = index;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
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
	
	public long getVolume() {
		return volume;
	}
	
	public double getAdjustedClose() {
		return adjustedClose;
	}
	
	public Index getIndex() {
		return index;
	}
	
	public void setValuesFrom(IndexDailyInformation idi) {
		this.date = idi.getDate();
		this.open = idi.getOpen();
		this.close = idi.getClose();
		this.high = idi.getHigh();
		this.low = idi.getLow();
		this.volume = idi.getVolume();
		this.adjustedClose = idi.getAdjustedClose();
	}
	
	@Override
    public boolean equals(Object obj) {
       if (!(obj instanceof IndexDailyInformation))
            return false;
        if (obj == this)
            return true;

        IndexDailyInformation indexInfo = (IndexDailyInformation) obj;
        if (date.equals(indexInfo.getDate()) && open == indexInfo.getOpen() &&
        	close == indexInfo.getClose() && high == indexInfo.getHigh() &&
        	low == indexInfo.getLow() && volume == indexInfo.getVolume()) {
        	return true;
        } else {
        	return false;
        }
    }
	
    @Override
    public int hashCode() {
    	HashCodeBuilder builder = new HashCodeBuilder();
    	builder.append(date);
    	builder.append(open);
    	builder.append(close);
    	builder.append(high);
    	builder.append(low);
    	builder.append(volume);
    	return builder.toHashCode();   
    }
}
