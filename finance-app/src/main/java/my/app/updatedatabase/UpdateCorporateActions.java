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
import my.app.utilities.DownloadUtility;

public class UpdateCorporateActions {
	
	private final DividendService dividendService;
	private final StockSplitService stockSplitService;
	private final StockService stockService;
	private final String rootDir;
	
	public UpdateCorporateActions(String rootDir, DividendService dividendService, 
			StockSplitService stockSplitService, StockService stockService) {
		this.dividendService = dividendService;
		this.stockSplitService = stockSplitService;
		this.rootDir = rootDir;
		this.stockService = stockService;
	}
	
	public void updateCorporateActions(boolean download) {
		if (download) {
			downloadCorporateActions();
		}
		List<Stock> stocks = stockService.getStocks();
		int i = 1;
		for(Stock stock : stocks) {
			String ticker = stock.getTicker();
			String filePath = rootDir + ticker + "_actions.csv";
			updateDividends(stock, filePath);
			updateStockSplits(stock, filePath);
			System.out.println(i++ + "/" + stocks.size() + " corporate actions updated");
		}
	}

	private void downloadCorporateActions() {
		List<Stock> stocks = stockService.getStocks();
		int i = 1;
		for(Stock stock : stocks) {
			String ticker = stock.getTicker();
			String filePath = rootDir + ticker + "_actions.csv";
			downloadCorporateActions(ticker, filePath);
			System.out.println(i++ + "/" + stocks.size() + " corporate actions downloaded");
		}
	}
	
	public void downloadCorporateActions(String ticker, String filePath) {
		String urlPrefix = "http://ichart.finance.yahoo.com/x?s=";
		String urlSuffix = "&a=1&b=1&c=1900&d=1&e=1&f=2099&g=v&y=0&z=30000";
		DownloadUtility.downloadFile(urlPrefix + ticker + urlSuffix, filePath);
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
