package my.app.updatedatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import my.app.domains.Stock;
import my.app.domains.StockDailyInformation;
import my.app.domains.corporateactions.Dividend;
import my.app.domains.corporateactions.StockSplit;
import my.app.parsing.HistoricalDataParser;
import my.app.services.StockDailyInformationService;
import my.app.services.StockService;


public class UpdateStockInformation {
	
	private final StockDailyInformationService stockDailyInformationService;
	private final StockService stockService;
	private final String rootDir;
	
	public UpdateStockInformation(String rootDir, StockDailyInformationService stockInformationService, StockService stockService) {
		this.stockDailyInformationService = stockInformationService;
		this.stockService = stockService;
		this.rootDir = rootDir;
	}
	
	//rootDir = "/home/daniel/fyp/data/"
	public void updateStocks(boolean downloadCSVs) {
		List<Stock> stocks = StockInformationReader.parseBaseStockInformation(rootDir, downloadCSVs);
		List<Stock> stocksInDatabase = stockService.getStocks();
		ArrayList<String> tickersInDatabase = stocksToTickers(stocksInDatabase);
		
		int i = 0;
		for(Stock stock : stocks) {
			System.out.println(i++);
			Stock currentStock = stockService.getStockByTicker(stock.getTicker());
			
			
			if (currentStock == null || tickersInDatabase == null ||
				!tickersInDatabase.contains(stock.getTicker())) {
				stockService.saveStock(stock);
			} else if (!stock.getMarketCap().equals(currentStock.getMarketCap()) ||
					   stock.getLastTradePrice() != currentStock.getLastTradePrice() ||
					   stock.getPERatio() == null ||
					   !stock.getPERatio().equals(currentStock.getPERatio())) {
				//hacky implementation of StockService.updateStock(Stock stock)
				stockService.updateStock(stock);
			}
		}
	}

	//currentData is data such as current price & market cap
	//historicalData is parsed to StockDailyInformation
	public void updateStockDailyInformation(boolean downloadCurrentData, boolean downloadHistoricalData) {	
		updateStocks(downloadCurrentData);
		List<Stock> stocks = stockService.getStocks();
		if (downloadHistoricalData) {
			StockInformationDownloader.downloadStockDailyInformation(stocks);
		}
	
		for (int i = 0; i < stocks.size(); i++) {
			updateHistoricalPrices(stocks.get(i));
			System.out.println((i+1) + "/" + stocks.size() + " historical data CSVs parsed into database");
		}
	}
	
	private void updateHistoricalPrices(Stock stock) {
		//parse the CSV file containing the historical stock data into objects
		String stockInformationFilePath = rootDir + stock.getTicker() + ".csv";
		HistoricalDataParser hdp = new HistoricalDataParser();
		Set<StockDailyInformation> sdisInDatabase = 
				new HashSet<StockDailyInformation>(stock.getStockDailyInformations());
		List<StockDailyInformation> sdis = 
				hdp.parseCSVToStockInformation(stock, stockInformationFilePath);
		sdis = addClosingPrices(sdis);
		//retrieve daily information that is currently in the database and store it in a hashset since we use it for lookups
		
		ArrayList<StockDailyInformation> sdisToSave = new ArrayList<StockDailyInformation>();
		ArrayList<StockDailyInformation> sdisToUpdate = new ArrayList<StockDailyInformation>();
		int i = 0;
		for(StockDailyInformation sdi : sdis) {
			if (sdisInDatabase == null || 
					!sdisInDatabase.contains(sdi)) {
				StockDailyInformation sdiInDatabase;
				if ((sdiInDatabase = sdisInDatabase.stream().filter(o -> 
				o.getDate().equals(sdi.getDate())).findFirst().orElse(null)) != null) {
					sdiInDatabase.setValuesFrom(sdi);
					sdisToUpdate.add(sdiInDatabase);
					System.out.println(i  + " U/U " + sdis.size() + ", date: " + sdi.getDate());
				} else {
					sdisToSave.add(sdi);
					System.out.println(i  + " S/S " + sdis.size() + ", date: " + sdi.getDate());
				}
			}
			i++;
		}
		stockDailyInformationService.saveStockInformations(sdisToSave);
		stockDailyInformationService.updateStockInformations(sdisToUpdate);
	}
	
	public void updateClosingPrices() {
		int i = 1;
		List<Stock> stocks = stockService.getStocks();
		int stockSize = stocks.size();
		for (Stock stock : stocks) {
			updateClosingPrices(stock);
			System.out.println(i++ + " / "+ stockSize + " stock closing prices updated");
		}
	}
	
	private void updateClosingPrices(Stock stock) {
		List<StockDailyInformation> sdis = stock.getStockDailyInformations();
		Set<StockDailyInformation> sdiSet = new HashSet<StockDailyInformation>(stock.getStockDailyInformations());
		sdis = addClosingPrices(sdis);
		List<StockDailyInformation> sdis2 = new ArrayList<StockDailyInformation>(); 
		for (int i = 0; i < sdis.size(); i++) {
			StockDailyInformation sdi = sdis.get(i);
			if (!sdiSet.contains(sdi)) {
				sdis2.add(sdi);
				if (sdis2.size() >= 1500) {
					stockDailyInformationService.updateStockInformations(sdis2);
					sdis2.clear();
				}
			}
		}
		stockDailyInformationService.updateStockInformations(sdis2);
	}

	private List<StockDailyInformation> addClosingPrices(List<StockDailyInformation> sdis) {
		for (StockDailyInformation sdi : sdis) {
			double adjCloseStockSplits = sdi.getClose()/getSplitRatioFor(sdi);
			sdi.setAdjCloseStockSplits(adjCloseStockSplits);
			//divTotalToToday dividend total from IPO date to {Today}
			double divTotalToToday = getDividendTotalFor(LocalDate.now(), sdi.getStock().getDividends());
			//divTotalToDate dividend total from IPO date to sdi date
			double divTotalToDate = getDividendTotalFor(sdi);
			double adjCloseDivNotReinvested = adjCloseStockSplits 
					- (divTotalToToday - divTotalToDate);
			sdi.setAdjCloseDivNotReinvested(adjCloseDivNotReinvested);
		}
		return sdis;
	}
	
	//get the split ratio that is applicable to the stock daily information date
	private double getSplitRatioFor(StockDailyInformation sdi) {
		LocalDate date = sdi.getDate();
		List<StockSplit> stockSplits = sdi.getStock().getStockSplits();
		stockSplits.sort(Comparator.comparing(StockSplit::getDate));
		for (StockSplit stockSplit : stockSplits) {
			//since stockSplits is sorted by date we return the first split ratio where split date is before the sdi date
			if (date.isBefore(stockSplit.getDate())) {
				return stockSplit.getSplitRatioToDateReversed();
			}
		}
		return 1;
	}

	//get the dividend total that is applicable to the stock daily information date
	private double getDividendTotalFor(StockDailyInformation sdi) {
		LocalDate date = sdi.getDate();
		List<Dividend> dividends = sdi.getStock().getDividends();
		return getDividendTotalFor(date, dividends);
	}
	
	//get the dividend total that is nearest to the date
	private double getDividendTotalFor(LocalDate date, List<Dividend> dividends) {
		dividends.sort(Comparator.comparing(Dividend::getDate));
		
		if (dividends.isEmpty()) {
			return 0;
		}
		LocalDate latestDividendDate = dividends.get(dividends.size()-1).getDate();
		if (date.isAfter(latestDividendDate) || date.isEqual(latestDividendDate)) {
			return dividends.get(dividends.size()-1).getTotalToDate();
		}
		//TODO use search algorithm optimised for sorted arrays to make this faster
		for (int i = 0; i < dividends.size(); i++) {
			Dividend dividend = dividends.get(i);
			if (date.isBefore(dividend.getDate())) {
				if (i > 0) {
					return dividends.get(i-1).getTotalToDate();
				} else {
					return 0;
				}
			}
		}
		return 0;
	}
		
	//takes a list of stocks and returns a list of stock tickers corresponding to the stocks
	private ArrayList<String> stocksToTickers(List<Stock> stocks) {
		ArrayList<String> tickers = new ArrayList<String>();
		for (Stock stock: stocks) {
			tickers.add(stock.getTicker());
		}
		return tickers;
	}
	
	/*
	 * due to a bug I had entries with the same {date, stock} combo in stock_daily_information, so I made this method 
	 * to remove the entries so that only one entry corresponds to one date and stock. Afterwards, I added a unique 
	 * constraint to the database to ensure that this doesn't occur again.
	*/
	public void removeDuplicateDateEntries() {
		List<Stock> stocks = stockService.getStocks();
		for (int i = 0; i < stocks.size(); i++) {
			Stock stock = stocks.get(i);
			List<StockDailyInformation> sdis = stock.getStockDailyInformations();
			sdis.sort(Comparator.comparing(StockDailyInformation::getDate));
			List<StockDailyInformation> sdisToRemove = new ArrayList<StockDailyInformation>();
			Iterator<StockDailyInformation> it = sdis.iterator();
			System.out.println((i+1) + " / " + stocks.size());
			
			int k = 0;
			while (it.hasNext()) {
				StockDailyInformation sdi = it.next();
				StockDailyInformation sdi2 = (sdis.subList(k+1, sdis.size()).stream()
						.filter(o ->  o.getDate().equals(sdi.getDate()) && 
								o.getId() != sdi.getId())
						.findFirst().orElse(null));
				if (sdi2 != null) {
					System.out.println(k+1 + " / " + sdis.size() + " deleted");
					sdisToRemove.add(sdi);
				}
				k++;
			}
			stockDailyInformationService.deleteStockInformations(sdisToRemove);
		}
	}
}
