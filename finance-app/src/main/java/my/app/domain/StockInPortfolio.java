package my.app.domain;

import java.time.DateTimeException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import my.app.formatter.PercentageFormat;
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
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date buyDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date sellDate;
	
	@NotNull
	private Double buyPrice;
	
	private Double sellPrice;
	
	@NotNull
	private double returnOnInvestment;
	
	@PercentageFormat
	private Double annualisedReturn;
	
	public StockInPortfolio() {
		
	}
	
	public StockInPortfolio(Stock stock, User user, Date buyDate) {
		this.stock = stock;
		this.user = user;
		this.buyDate = buyDate;
		this.buyPrice = StockDataCalculations.findStockPriceOnDate(stock, buyDate);
		calculateMetrics();
	}

	public StockInPortfolio(Stock stock, User user, Date buyDate, Date sellDate) {
		this.stock = stock;
		this.user = user;
		this.buyDate = buyDate;
		this.buyPrice = StockDataCalculations.findStockPriceOnDate(stock, buyDate);
		this.sellDate = sellDate;
		this.sellPrice = StockDataCalculations.findStockPriceOnDate(stock, sellDate);
		calculateMetrics();
	}
	
	private void calculateMetrics() {
		Double sellPrice = this.sellPrice;
		Date sellDate = this.sellDate;
		if (sellPrice == null) {
			if (sellDate == null) {
				sellPrice = stock.getLastTradePrice();
			} else {
				this.sellPrice = StockDataCalculations.findStockPriceOnDate(stock, sellDate);
				sellPrice = this.sellPrice;
			}
		}
		double returnOnInvestment = (sellPrice - buyPrice)/buyPrice;
		Double annualisedReturn;
		if (sellDate == null) {
			annualisedReturn = StockDataCalculations.calculateAnnualisedReturn(buyDate, new Date(), buyPrice, sellPrice);
		} else {
			annualisedReturn = StockDataCalculations.calculateAnnualisedReturn(stock, buyDate, sellDate);
		}
		this.returnOnInvestment = returnOnInvestment;
		this.annualisedReturn = annualisedReturn;
	}
	
	public Date getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(Date buyDate) throws DateTimeException {
		Double buyPrice = StockDataCalculations.findStockPriceOnDate(stock, buyDate);
		if (buyPrice == null) {
			throw new DateTimeException("Date is too early");
		}
		this.buyDate = buyDate;
		this.buyPrice = buyPrice;
		calculateMetrics();
	}

	public Date getSellDate() {
		return sellDate;
	}

	public void setSellDate(Date sellDate) throws DateTimeException {
		if (sellDate != null) {
			Double sellPrice = StockDataCalculations.findStockPriceOnDate(stock, buyDate);
			if (sellPrice == null) {
				throw new DateTimeException("Date is too early");
			}
			this.sellDate = sellDate;
			this.sellPrice = sellPrice;
			calculateMetrics();
		} else {
			this.sellDate = null;
		}
	}

	public Double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(Double buyPrice) {
		this.buyPrice = buyPrice;
		calculateMetrics();
	}

	public Double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Double sellPrice) {
		this.sellPrice = sellPrice;
		calculateMetrics();
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
	
	public double getReturnOnInvestment() {
		return returnOnInvestment;
	}
	
	public Double getAnnualisedReturn() {
		return annualisedReturn;
	}
}
