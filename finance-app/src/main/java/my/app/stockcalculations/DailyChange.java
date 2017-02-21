package my.app.stockcalculations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import my.app.domains.StockDailyInformation;

public class DailyChange {

	private LocalDate date;
	private double percentageChange;
	
	public DailyChange(LocalDate date, double percentageChange) {
		this.date = date;
		this.percentageChange = percentageChange;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public double getPercentageChange() {
		return percentageChange;
	}
	
	public void setPercentageChange(double percentageChange) {
		this.percentageChange = percentageChange;
	}
	
	private ArrayList<DailyChange> calculateDailyPercentageChanges(List<StockDailyInformation> stockInfos) {
		ArrayList<DailyChange> dailyChanges = new ArrayList<DailyChange>();
		dailyChanges.add(new DailyChange(stockInfos.get(0).getDate(), 0));
		
		for (int i = 1; i < stockInfos.size(); i++) {
			double prevPrice = stockInfos.get(i - 1).getAdjustedClose();
			double price = stockInfos.get(i).getAdjustedClose();
			double percentageChange = (price/prevPrice) - 1;
			dailyChanges.add(new DailyChange(stockInfos.get(i).getDate(), percentageChange));
		}
		return dailyChanges;
	}
	
	//removes all information that comes before the start date. So the date range is current date to startDate 
	private List<DailyChange> filterByDate(List<DailyChange> dailyChanges, LocalDate startDate) {
		
		Iterator<DailyChange> it = dailyChanges.iterator();
		while (it.hasNext()) {
			DailyChange dailyChange = it.next();
			LocalDate dailyChangeDate = dailyChange.getDate();
			//if the date is before the startDate then we remove it
			if (dailyChangeDate.isBefore(startDate)) {
				it.remove();
			}
		}
		return dailyChanges;
	}
}
