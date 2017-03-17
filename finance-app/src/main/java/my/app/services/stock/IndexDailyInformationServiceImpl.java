package my.app.services.stock;

import java.util.List;

import org.springframework.stereotype.Service;

import my.app.dao.stock.IndexDailyInformationDAO;
import my.app.domains.stock.IndexDailyInformation;

@Service
public class IndexDailyInformationServiceImpl implements IndexDailyInformationService {

	private final IndexDailyInformationDAO dao;
	
	public IndexDailyInformationServiceImpl(IndexDailyInformationDAO dao) {
		this.dao = dao;
	}
	
	public void saveIndexDailyInformation(IndexDailyInformation indexDailyInformation) {
		dao.saveIndexDailyInformation(indexDailyInformation);
	}

	public IndexDailyInformation getIndexDailyInformationById(int id) {
		return dao.getIndexDailyInformationById(id);
	}

	public List<IndexDailyInformation> getIndexDailyInformations() {
		return dao.getIndexDailyInformations();
	}

	public void updateIndexDailyInformation(IndexDailyInformation indexDailyInformation) {
		dao.updateIndexDailyInformation(indexDailyInformation);
	}

	public void deleteIndexDailyInformation(IndexDailyInformation indexDailyInformation) {
		dao.deleteIndexDailyInformation(indexDailyInformation);
	}

	public void deleteIndexDailyInformationById(int id) {
		IndexDailyInformation indexDailyInformation = dao.getIndexDailyInformationById(id);
		dao.deleteIndexDailyInformation(indexDailyInformation);
	}

	@Override
	public void saveIndexDailyInformations(List<IndexDailyInformation> idisToSave) {
		dao.saveIndexDailyInformations(idisToSave);
	}

	@Override
	public void updateIndexDailyInformations(List<IndexDailyInformation> idisToUpdate) {
		dao.updateIndexDailyInformations(idisToUpdate);
	}
}
