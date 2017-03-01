package my.app.domains.stock;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;

import my.app.domains.corporateactions.Dividend;
import my.app.domains.corporateactions.StockSplit;

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
	private String sector;
	
	@OneToMany(mappedBy = "stock")
	private List<StockDailyInformation> stockDailyInformations;
	
	@OneToOne(mappedBy = "stock")
	@Cascade(value=org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	private StockMetrics stockMetrics;
	
	@OneToMany(mappedBy = "stock")
	private List<StockSplit> stockSplits;
	
	@OneToMany(mappedBy = "stock")
	private List<Dividend> dividends;
	
	private String marketCap;
	
	@NotNull
	private double lastTradePrice;
	
	private Double peRatio;
	
	public Stock() {
		
	}
	
	public Stock(String name, String ticker, String sector) {
		this.name = name;
		this.ticker = ticker;
		this.sector = sector;
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
	
	public String getSector() {
		return sector;
	}
	
	public List<StockDailyInformation> getStockDailyInformations() {
		return stockDailyInformations;
	}
	
	public StockMetrics getStockMetrics() {
		return stockMetrics;
	}
	
	public List<StockSplit> getStockSplits() {
		return stockSplits;
	}

	public List<Dividend> getDividends() {
		return dividends;
	}

	public String getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(String marketCap) {
		this.marketCap = marketCap;
	}

	public double getLastTradePrice() {
		return lastTradePrice;
	}

	public void setLastTradePrice(double lastTradePrice) {
		this.lastTradePrice = lastTradePrice;
	}

	public Double getPERatio() {
		return peRatio;
	}
	
	public void setPERatio(Double peRatio) {
		// TODO Auto-generated method stub
		this.peRatio = peRatio;
	}
}
