package my.app.services.corporateactions;

import java.util.List;

import org.springframework.stereotype.Service;

import my.app.dao.corporateactions.StockSplitDAO;
import my.app.domains.corporateactions.StockSplit;

@Service
public class StockSplitServiceImpl implements StockSplitService {
	
	private final StockSplitDAO dao;
	
	public StockSplitServiceImpl(StockSplitDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public void saveStockSplit(StockSplit stockSplit) {
		dao.saveStockSplit(stockSplit);
	}

	@Override
	public StockSplit getStockSplitById(int id) {
		StockSplit stockSplit = dao.getStockSplitById(id);
		return stockSplit;
	}

	@Override
	public List<StockSplit> getStockSplits() {
		List<StockSplit> stockSplits = dao.getStockSplits();
		return stockSplits;
	}

	@Override
	public void updateStockSplit(StockSplit stockSplit) {
		dao.updateStockSplit(stockSplit);
	}

	@Override
	public void deleteStockSplit(StockSplit stockSplit) {
		dao.deleteStockSplit(stockSplit);
	}
}
