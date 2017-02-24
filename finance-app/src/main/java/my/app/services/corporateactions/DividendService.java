package my.app.services.corporateactions;

import java.util.List;

import my.app.domains.corporateactions.Dividend;

public interface DividendService {

	void saveDividend(Dividend dividend);
	Dividend getDividendById(int id);
	List<Dividend> getDividends();
	void updateDividend(Dividend dividend);
	void deleteDividend(Dividend dividend);
}
