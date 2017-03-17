package my.app.services.stock;

import java.util.List;

import my.app.domains.stock.IndexDailyInformation;

public interface IndexDailyInformationService {

	void saveIndexDailyInformation(IndexDailyInformation indexDailyInformation);
	void saveIndexDailyInformations(List<IndexDailyInformation> idisToSave);
	IndexDailyInformation getIndexDailyInformationById(int id);
	List<IndexDailyInformation> getIndexDailyInformations();
	void updateIndexDailyInformation(IndexDailyInformation indexDailyInformation);
	void updateIndexDailyInformations(List<IndexDailyInformation> idisToUpdate);
	void deleteIndexDailyInformation(IndexDailyInformation indexDailyInformation);
}
