package my.app.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Stock {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	private String name;
	
	@NotNull
	private String ticker;
	
	@OneToMany
	private List<StockInformation> stockInformation;

	@NotNull
	private String industry;
	
	public Stock(String name, String ticker, String industry) {
		this.name = name;
		this.ticker = ticker;
		this.industry = industry;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTicker() {
		return ticker;
	}
	
	public String getIndustry() {
		return industry;
	}
	
	public List<StockInformation> getStockInformation() {
		return stockInformation;
	}
}
