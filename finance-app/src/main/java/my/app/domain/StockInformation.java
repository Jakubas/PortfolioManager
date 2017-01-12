package my.app.domain;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class StockInformation {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Calendar date;
	
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
	
	@ManyToOne
	@JoinColumn(name = "stock_id")
	private Stock stock;

	public int getId() {
		return id;
	}
	
	public Calendar getDate() {
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
}
