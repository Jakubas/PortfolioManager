package my.app.services.corporateactions;

import java.util.List;

import org.springframework.stereotype.Service;

import my.app.dao.corporateactions.DividendDAO;
import my.app.domains.corporateactions.Dividend;

@Service
public class DividendServiceImpl implements DividendService {
	
	private final DividendDAO dao;
	
	public DividendServiceImpl(DividendDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public void saveDividend(Dividend dividend) {
		dao.saveDividend(dividend);
	}

	@Override
	public Dividend getDividendById(int id) {
		Dividend dividend = dao.getDividendById(id);
		return dividend;
	}

	@Override
	public List<Dividend> getDividends() {
		List<Dividend> dividends = dao.getDividends();
		return dividends;
	}

	@Override
	public void updateDividend(Dividend dividend) {
		dao.updateDividend(dividend);
	}

	@Override
	public void deleteDividend(Dividend dividend) {
		dao.deleteDividend(dividend);
	}
}
