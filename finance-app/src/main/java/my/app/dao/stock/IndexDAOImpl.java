package my.app.dao.stock;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import my.app.domains.stock.Index;

@Repository
@Transactional
public class IndexDAOImpl implements IndexDAO {

	private final SessionFactory sessionFactory;
	
	@Autowired
	public IndexDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void saveIndex(Index index) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(index);
	}

	@Override
	public Index getIndexById(int id) {
		Session session = sessionFactory.getCurrentSession();
		Index index = session.get(Index.class, id);
		return index;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Index> getIndices() {
		Session session = sessionFactory.getCurrentSession();
		List<Index> index = session.createCriteria(Index.class).list();
		return index;
	}

	@Override
	public void updateIndex(Index index) {
		Session session = sessionFactory.getCurrentSession();
		session.update(index);
	}

	@Override
	public void deleteIndex(Index index) {
		Session session = sessionFactory.getCurrentSession();
		int id = index.getId();
		index = session.get(Index.class, id);
		if (index != null) {
			session.delete(index);
		}
	}

}
