package my.app.domains.portfolio;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import my.app.domains.corporateactions.Dividend;
import my.app.domains.stock.Stock;
import my.app.domains.user.User;
import my.app.formatter.PercentageFormat;
import my.app.stockcalculations.StockDataCalculations;

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
	private LocalDate buyDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate sellDate;
	
	@NotNull
	@NumberFormat(style = Style.NUMBER, pattern = "#,###.00######")
	private Double buyPrice;
	
	@NumberFormat(style = Style.NUMBER, pattern = "#,###.00######")
	private Double sellPrice;
	
	@NotNull
	@PercentageFormat
	private double returnOnInvestment;
	
	@PercentageFormat
	private Double annualisedReturn;
	
	@NotNull
	private int amount;
	
	@NotNull
	@NumberFormat(style = Style.NUMBER, pattern = "#,###.##")
	private double value;
	
	public StockInPortfolio() {
		
	}
	
	public StockInPortfolio(Stock stock, User user, int amount, LocalDate buyDate) {
		this.stock = stock;
		this.user = user;
		this.amount = amount;
		this.buyDate = buyDate;
		this.buyPrice = StockDataCalculations.findStockPriceOnDate(stock, buyDate);
		calculateMetrics();
		user.minusCash(buyPrice * amount);
	}

	public StockInPortfolio(Stock stock, User user, int amount, LocalDate buyDate, LocalDate sellDate) {
		this.stock = stock;
		this.user = user;
		this.amount = amount;
		this.buyDate = buyDate;
		this.buyPrice = StockDataCalculations.findStockPriceOnDate(stock, buyDate);
		this.sellDate = sellDate;
		this.sellPrice = StockDataCalculations.findStockPriceOnDate(stock, sellDate);
		calculateMetrics();
		user.minusCash(buyPrice * amount);
	}
	
	public void calculateMetrics() {
		Double sellPrice = this.sellPrice;
		LocalDate sellDate = this.sellDate;
		if (sellPrice == null) {
			if (isStockSold()) {
				//this is most likely never reached e.g. dead code
				this.sellPrice = StockDataCalculations.findStockPriceOnDate(stock, sellDate);
				sellPrice = this.sellPrice;
			} else {
				sellPrice = stock.getLastTradePrice();
			}
		}
		double returnOnInvestment = (sellPrice - buyPrice)/buyPrice;
		Double annualisedReturn;
		if (sellDate == null) {
			annualisedReturn = StockDataCalculations.calculateAnnualisedReturn(stock, buyDate, LocalDate.now(), buyPrice, sellPrice);
		} else {
			annualisedReturn = StockDataCalculations.calculateAnnualisedReturn(stock, buyDate, sellDate, buyPrice, sellPrice);
		}
		this.returnOnInvestment = returnOnInvestment;
		this.annualisedReturn = annualisedReturn;
		this.value = sellPrice * amount;
	}
	
	public boolean isStockSold() {
		return sellDate != null;
	}
	
	public LocalDate getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(LocalDate buyDate) {
		this.buyDate = buyDate;
		calculateMetrics();
	}
	
	public void setBuyDateAndPrice(LocalDate buyDate) throws DateTimeException {
		Double buyPrice = StockDataCalculations.findStockPriceOnDate(stock, buyDate);
		if (buyPrice == null) {
			throw new DateTimeException("Date is too early");
		}
		this.buyDate = buyDate;
		this.setBuyPrice(buyPrice);
		calculateMetrics();
	}

	public LocalDate getSellDate() {
		return sellDate;
	}

	public void setSellDate(LocalDate sellDate) {
		this.sellDate = sellDate;
		calculateMetrics();
	}
	
	public void setSellDateAndPrice(LocalDate sellDate) throws DateTimeException {
		if (sellDate != null) {
			Double sellPrice = StockDataCalculations.findStockPriceOnDate(stock, sellDate);
			if (sellPrice == null) {
				throw new DateTimeException("Date is too early");
			}
			this.sellDate = sellDate;
			this.setSellPrice(sellPrice);
			calculateMetrics();
		} else {
			this.sellDate = null;
		}
	}
	
	public Double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(Double buyPrice) {
		double prevPrice;
		if (this.buyPrice == null) {
			prevPrice = 0;
		} else {
			prevPrice = this.buyPrice;
		}
		double priceChange = buyPrice - prevPrice;
		double cash = priceChange * amount;
		user.minusCash(cash);
		this.buyPrice = buyPrice;
		calculateMetrics();
	}

	public Double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Double sellPrice) {
		if (sellPrice == null) {
			this.sellPrice = null;
		} else {
			double prevPrice;
			if (this.sellPrice == null) {
				prevPrice = 0;
			} else {
				prevPrice = this.sellPrice;
			}
			double priceChange = sellPrice - prevPrice;
			double cash = priceChange * amount;
			user.plusCash(cash);
			this.sellPrice = sellPrice;
			calculateMetrics();
		}
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
		Double result = annualisedReturn != null ? annualisedReturn : 0;
		return result;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		int prevAmount = this.amount;
		double amountChange = amount - prevAmount;
		double cash = amountChange * buyPrice;
		//we update the user's cash to reflect the new amount;
		user.minusCash(cash);
		this.amount = amount;
	}
	
	public double getValue() {
		return value;
	}
	
	@NumberFormat(style = Style.NUMBER, pattern = "#,##0.00")
	public double getGain() {
		return returnOnInvestment * buyPrice * amount;
	}
	
	@NumberFormat(style = Style.NUMBER, pattern = "#,##0.00")
	//this is the absolute price difference between the buy and sell values 
	public double getDifference() {
		return Math.abs(returnOnInvestment * buyPrice * amount);
	}
	
	@NumberFormat(style = Style.NUMBER, pattern = "#,##0.00")
	public double getDividendsTotal() {
		List<Dividend> dividends = stock.getDividends();
		double dividendsTotal = 0;
		for (Dividend dividend : dividends) {
			if ((dividend.getDate().isAfter(buyDate) || dividend.getDate().isEqual(buyDate)) && 
					(sellDate == null || dividend.getDate().isBefore(sellDate))) {
				dividendsTotal +=  dividend.getAmount();
			}
		}
		return dividendsTotal;
	}
}
