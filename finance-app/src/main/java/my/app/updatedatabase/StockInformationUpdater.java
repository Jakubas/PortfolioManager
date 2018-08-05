package my.app.updatedatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import my.app.domains.corporateactions.Dividend;
import my.app.domains.corporateactions.StockSplit;
import my.app.domains.stock.Index;
import my.app.domains.stock.IndexDailyInformation;
import my.app.domains.stock.Stock;
import my.app.domains.stock.StockDailyInformation;
import my.app.parsing.HistoricalDataParser;
import my.app.parsing.IndexDataParser;
import my.app.services.stock.IndexDailyInformationService;
import my.app.services.stock.IndexService;
import my.app.services.stock.StockDailyInformationService;
import my.app.services.stock.StockService;
import my.app.updatedatabase.download.StockInformationDownloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StockInformationUpdater {
	
	private final StockDailyInformationService stockDailyInformationService;
	private final StockService stockService;
	private final IndexDailyInformationService indexDailyInformationService;
	private final IndexService indexService;
	@Value("/tmp")
	private final String targetDir;

	@Autowired
	public StockInformationUpdater(String targetDir, StockDailyInformationService stockInformationService,
								   StockService stockService, IndexService indexService, IndexDailyInformationService indexDailyInformationService) {
		this.stockDailyInformationService = stockInformationService;
		this.stockService = stockService;
		this.indexService = indexService;
		this.indexDailyInformationService = indexDailyInformationService;
		this.targetDir = targetDir;
	}
	
	//rootDir = "/home/daniel/fyp/data/"
	public void updateStocks(boolean downloadCSVs) {
		List<Stock> stocks = StockInformationReader.parseBaseStockInformation(targetDir, downloadCSVs);
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
		updateIndexDailyInformation(downloadHistoricalData);
	}
	
	public void updateIndexDailyInformation(boolean downloadHistoricalData) {
		Index index;
		if (!indexService.getIndices().isEmpty()) {
			index = indexService.getIndices().get(0);
		} else {
			index = new Index("S&P 500", "^GSPC");
			indexService.saveIndex(index);
		}
		if (downloadHistoricalData) {
			StockInformationDownloader.downloadHistoricalStockInformation(index.getTicker());
		}
		updateIndexHistoricalPrices(index);
		System.out.println("Updated index daily information");
	}
	
	private void updateIndexHistoricalPrices(Index index) {
		String indexInformationFilePath = targetDir + index.getTicker() + ".csv";
		Set<IndexDailyInformation> idisInDatabase = 
				new HashSet<IndexDailyInformation>(index.getIndexDailyInformations());
		List<IndexDailyInformation> idis = IndexDataParser.parseCSVToIndexInformation(index, indexInformationFilePath);
		ArrayList<IndexDailyInformation> idisToSave = new ArrayList<IndexDailyInformation>();
		ArrayList<IndexDailyInformation> idisToUpdate = new ArrayList<IndexDailyInformation>();
		int i = 0;
		for(IndexDailyInformation idi : idis) {
			if (idisInDatabase == null) {
				idisToSave.add(idi);
				System.out.println(i  + " S/S " + idis.size() + ", date: " + idi.getDate());
			} else if(!idisInDatabase.contains(idi)) {
				IndexDailyInformation idiInDatabase;
				if ((idiInDatabase = idisInDatabase.stream().filter(o -> 
				o.getDate().equals(idi.getDate())).findFirst().orElse(null)) != null) {
					idiInDatabase.setValuesFrom(idi);
					idisToUpdate.add(idiInDatabase);
					System.out.println(i  + " U/U " + idis.size() + ", date: " + idi.getDate());
				} else {
					idisToSave.add(idi);
					System.out.println(i  + " S/S " + idis.size() + ", date: " + idi.getDate());
				}
			}
			i++;
		}
		indexDailyInformationService.saveIndexDailyInformations(idisToSave);
		indexDailyInformationService.updateIndexDailyInformations(idisToUpdate);
	}
	
	private void updateHistoricalPrices(Stock stock) {
		//parse the CSV file containing the historical stock data into objects
		String stockInformationFilePath = targetDir + stock.getTicker() + ".csv";
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
}
