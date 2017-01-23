package my.app.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;

@Entity
public class Stock {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	private String name;
	
	@NotNull
	private String ticker;

	@NotNull
	private String industry;
	
	@OneToMany(mappedBy = "stock")
	private List<StockDailyInformation> stockDailyInformations;
	
	@OneToOne(mappedBy = "stock")
	@Cascade(value=org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private StockCalculatedData stockCalculatedData;
	
	public Stock() {
		
	}
	
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
	
	public List<StockDailyInformation> getStockDailyInformations() {
		return stockDailyInformations;
	}
	
	public StockCalculatedData getStockCalculatedData() {
		return stockCalculatedData;
	}
}
