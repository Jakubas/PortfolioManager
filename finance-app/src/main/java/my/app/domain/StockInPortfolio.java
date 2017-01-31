package my.app.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import my.app.stock_calculations.StockDataCalculations;

@Entity
public class StockInPortfolio {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "stock_id")
	private Stock stock;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@NotNull
	private Date buyDate;
	
	private Date sellDate;
	
	@NotNull
	private Double buyPrice;
	
	private Double sellPrice;
	
	public StockInPortfolio() {
		
	}
	
	public StockInPortfolio(Stock stock, User user, Date buyDate) {
		this.stock = stock;
		this.user = user;
		this.buyDate = buyDate;
		this.buyPrice = StockDataCalculations.findStockPriceOnDate(stock, buyDate);
	}

	public StockInPortfolio(Stock stock, User user, Date buyDate, Date sellDate) {
		this.stock = stock;
		this.user = user;
		this.buyDate = buyDate;
		this.buyPrice = StockDataCalculations.findStockPriceOnDate(stock, buyDate);
		this.sellDate = sellDate;
		this.sellPrice = StockDataCalculations.findStockPriceOnDate(stock, sellDate);
	}
	
	public Date getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

	public Date getSellDate() {
		return sellDate;
	}

	public void setSellDate(Date sellDate) {
		this.sellDate = sellDate;
	}

	public Double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(Double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public Double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public int getId() {
		return id;
	}

	public Stock getStock() {
		return stock;
	}

	public User getUser() {
		return user;
	}
}