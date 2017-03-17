package my.app.services.stock;

import java.util.List;

import org.springframework.stereotype.Service;

import my.app.dao.stock.IndexDAO;
import my.app.domains.stock.Index;

@Service
public class IndexServiceImpl implements IndexService {

	private final IndexDAO dao;
	
	public IndexServiceImpl(IndexDAO dao) {
		this.dao = dao;
	}
	
	public void saveIndex(Index index) {
		dao.saveIndex(index);
	}

	public Index getIndexById(int id) {
		return dao.getIndexById(id);
	}

	public List<Index> getIndices() {
		return dao.getIndices();
	}

	public void updateIndex(Index index) {
		dao.updateIndex(index);
	}

	public void deleteIndex(Index index) {
		dao.deleteIndex(index);
	}

	public void deleteIndexById(int id) {
		Index index = dao.getIndexById(id);
		dao.deleteIndex(index);
	}
}
