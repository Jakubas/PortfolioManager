package my.app.dao.stock;

import java.util.List;

import my.app.domains.stock.IndexDailyInformation;

public interface IndexDailyInformationDAO {
	
	void saveIndexDailyInformation(IndexDailyInformation indexDailyInformation);
	IndexDailyInformation getIndexDailyInformationById(int id);
	List<IndexDailyInformation> getIndexDailyInformations();
	void updateIndexDailyInformation(IndexDailyInformation indexDailyInformation);
	void deleteIndexDailyInformation(IndexDailyInformation indexDailyInformation);
}
