package my.app.cashflow;

import java.time.LocalDate;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import my.app.domains.portfolio.StockInPortfolio;

public class HoldingTransaction {

	@NumberFormat(style = Style.NUMBER, pattern = "#,###.00")
	private double value;
	
	private LocalDate date;
	
	private boolean isBuy;
	
	private String ticker;

	private int amount;
	
	@NumberFormat(style = Style.NUMBER, pattern = "#,###.00")
	private double stockPrice;

	private String event;
	
	//if it's not isBuy then its a sale
	public HoldingTransaction(StockInPortfolio holding, boolean isBuy) {
		this.isBuy = isBuy;
		this.ticker = holding.getStock().getTicker();
		this.amount = holding.getAmount();
		setValues(holding);
		this.value = amount * stockPrice;
		this.setEvent();
	}

	private void setValues(StockInPortfolio holding) {
		if (this.isBuy) {
			this.date = holding.getBuyDate();
			this.stockPrice = holding.getBuyPrice();
		} else {
			this.date = holding.getSellDate();
			this.stockPrice = holding.getSellPrice();
		}
	}

	public double getCashAmount() {
		return value;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public boolean isBuy() {
		return isBuy;
	}
	
	public double getValue() {
		return value;
	}

	public String getTicker() {
		return ticker;
	}

	public int getAmount() {
		return amount;
	}
	
	public String getEvent() {
		return event;
	}
	
	public double getStockPrice() {
		return stockPrice;
	}
	
	private void setEvent() {
		if (isBuy) {
			this.event = "Bought " + amount + " shares of " + ticker  + " @ $" + stockPrice + " each"; 
		} else {
			this.event = "Sold " + amount + " shares of " + ticker + " @ $" + stockPrice + " each"; 
		}
	}
}
