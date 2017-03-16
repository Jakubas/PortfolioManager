package my.app.cashflow;

import java.time.LocalDate;

import my.app.domains.portfolio.StockInPortfolio;

public class CashFlow {

	private double cashAmount;
	
	private String event;
	
	private LocalDate date;
	
	//if it's not isBuy then its a sale
	public CashFlow(StockInPortfolio holding, boolean isBuy) {
		this.setEvent(holding, isBuy);
	}

	public double getCashAmount() {
		return cashAmount;
	}

	public String getEvent() {
		return event;
	}
	
	private void setEvent(StockInPortfolio holding, boolean isBuy) {
		if (isBuy) {
			int amount = holding.getAmount();
			String ticker = holding.getStock().getTicker();
			double price = holding.getBuyPrice();
			this.event = "Bought " + amount + " shares of " + ticker  + " @ $" + price + " each"; 
			this.cashAmount = price * amount;
			this.date = holding.getBuyDate();
		} else {
			int amount = holding.getAmount();
			String ticker = holding.getStock().getTicker();
			double price = holding.getSellPrice();
			this.event = "Sold " + amount + " shares of " + ticker + " @ $" + price + " each"; 
			this.cashAmount = -(price * amount);
			this.date = holding.getSellDate();
		}
	}
	
	public LocalDate getDate() {
		return date;
	}
}
