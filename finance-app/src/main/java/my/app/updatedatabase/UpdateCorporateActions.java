package my.app.updatedatabase;

import java.util.List;

import my.app.domains.Stock;
import my.app.domains.corporateactions.Dividend;
import my.app.domains.corporateactions.StockSplit;
import my.app.parsing.CorporateActionParser;
import my.app.services.StockService;
import my.app.services.corporateactions.DividendService;
import my.app.services.corporateactions.StockSplitService;

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
		String urlSuffix = "&a=00&b=2&c=1962&d=04&e=25&f=2011&g=v&y=0&z=30000";
		DownloadUtility.downloadFile(urlPrefix + ticker + urlSuffix, filePath);
	}

	private void updateDividends(Stock stock, String filePath) {
		List<Dividend> dividends = CorporateActionParser.parseCSVToDividends(stock, filePath);
		for (Dividend dividend : dividends) {
			dividendService.saveDividend(dividend);
		}
	}
	
	private void updateStockSplits(Stock stock, String filePath) {
		List<StockSplit> stockSplits = CorporateActionParser.parseCSVToStockSplits(stock, filePath);
		for (StockSplit stockSplit : stockSplits) {
			stockSplitService.saveStockSplit(stockSplit);
		}
	}
}
