package my.app.updatedatabase;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import my.app.domains.corporateactions.Dividend;
import my.app.domains.corporateactions.StockSplit;
import my.app.domains.stock.Stock;
import my.app.parsing.CorporateActionParser;
import my.app.services.corporateactions.DividendService;
import my.app.services.corporateactions.StockSplitService;
import my.app.services.stock.StockService;
import my.app.updatedatabase.download.CorporateActionDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CorporateActionUpdater {
	
	private final DividendService dividendService;
	private final StockSplitService stockSplitService;
	private final StockService stockService;
	@Value("/tmp")
	private final String targetDir;

	@Autowired
	public CorporateActionUpdater(String targetDir, DividendService dividendService,
								  StockSplitService stockSplitService, StockService stockService) {
		this.targetDir = targetDir;
		this.dividendService = dividendService;
		this.stockSplitService = stockSplitService;
		this.stockService = stockService;
	}

	public void updateCorporateActions() {
		//TODO: The dir should be passed in as an arg and the file path of the downloaded files should be resolved by a
		//TODO: method, so that we can re-use it
		List<Stock> stocks = stockService.getStocks();
		CorporateActionDownloader.downloadCorporateActions(stocks, targetDir);
		int i = 1;
		for(Stock stock : stocks) {
			String ticker = stock.getTicker();
			String filePath = targetDir + ticker + "_actions.csv";
			updateDividends(stock, filePath);
			updateStockSplits(stock, filePath);
			System.out.println(i++ + "/" + stocks.size() + " corporate actions updated");
		}
	}

	private void updateDividends(Stock stock, String filePath) {
		List<Dividend> dividends = CorporateActionParser.parseCSVToDividends(stock, filePath);
		dividends.sort(Comparator.comparing(Dividend::getDate));
		
		List<Dividend> dividendsInDatabase = dividendService.getDividends();
		double totalToDate = 0;
		for (Dividend dividend : dividends) {
			LocalDate date = dividend.getDate();
			double amount = dividend.getAmount();
			totalToDate += amount;
			if (dividendsInDatabase.stream().anyMatch(o -> 
			o.getStock().getId() == stock.getId() && o.getDate().equals(date) && o.getAmount() == amount)) {
				continue;
			} else {
				dividend.setTotalToDate(totalToDate);
				dividendService.saveDividend(dividend);
			}
		}
	}
	
	private void updateStockSplits(Stock stock, String filePath) {
		List<StockSplit> stockSplits = CorporateActionParser.parseCSVToStockSplits(stock, filePath);
		
		stockSplits.sort(Comparator.comparing(StockSplit::getDate).reversed());
		List<StockSplit> stockSplitsInDatabase = stock.getStockSplits();
		double splitRatioToDateReversed = 1;
		for (StockSplit stockSplit : stockSplits) {
			splitRatioToDateReversed *= stockSplit.getSplitRatio();
			stockSplit.setSplitRatioToDateReversed(splitRatioToDateReversed);
		}
		
		stockSplits.sort(Comparator.comparing(StockSplit::getDate));
		double splitRatioToDate = 1;
		for (StockSplit stockSplit : stockSplits) {
			splitRatioToDate *= stockSplit.getSplitRatio();
			stockSplit.setSplitRatioToDate(splitRatioToDate);
			
			StockSplit stockSplitInDatabase;
			if ((stockSplitInDatabase = getMostSimiliar(stockSplit, stockSplitsInDatabase)) != null) {
				if (stockSplitInDatabase.getSplitRatioToDate() == stockSplit.getSplitRatioToDate() &&
						stockSplitInDatabase.getSplitRatioToDateReversed() == stockSplit.getSplitRatioToDateReversed()) {
					continue;
				} else {
					stockSplitInDatabase.setSplitRatioToDate(stockSplit.getSplitRatioToDate());
					stockSplitInDatabase.setSplitRatioToDateReversed(stockSplit.getSplitRatioToDateReversed());
					stockSplitService.updateStockSplit(stockSplitInDatabase);
				}
			} else {
				stockSplitService.saveStockSplit(stockSplit);
			}
		}
	}
	
	private StockSplit getMostSimiliar(StockSplit stockSplit, List<StockSplit> stockSplits) {
		int stockId = stockSplit.getStock().getId();
		LocalDate date = stockSplit.getDate();
		String split = stockSplit.getSplit();
		for (StockSplit stockSplit2 : stockSplits) {
			int stockId2 = stockSplit2.getStock().getId();
			LocalDate date2 = stockSplit2.getDate();
			String split2 = stockSplit2.getSplit();
			if (stockId == stockId2 && date.equals(date2) && split.equals(split2)) {
				return stockSplit2;
			}
		}
		return null;
	}
}
