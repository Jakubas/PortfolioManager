package my.app.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Stock {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String name;
	
	private String ticker;
	
	@OneToMany
	private List<StockInformation> stockInformation;
	
	public Stock(String name, String ticker) {
		this.name = name;
		this.ticker = ticker;
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
	
	public List<StockInformation> getStockInformation() {
		return stockInformation;
	}
}
