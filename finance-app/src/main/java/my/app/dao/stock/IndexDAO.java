package my.app.dao.stock;

import java.util.List;

import my.app.domains.stock.Index;

public interface IndexDAO {
	
	void saveIndex(Index index);
	Index getIndexById(int id);
	List<Index> getIndices();
	void updateIndex(Index index);
	void deleteIndex(Index index);
}
