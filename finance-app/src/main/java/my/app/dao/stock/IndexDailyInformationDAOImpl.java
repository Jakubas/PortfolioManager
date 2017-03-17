package my.app.dao.stock;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import my.app.domains.stock.IndexDailyInformation;

@Repository
@Transactional
public class IndexDailyInformationDAOImpl implements IndexDailyInformationDAO {

	private final SessionFactory sessionFactory;
	
	@Autowired
	public IndexDailyInformationDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void saveIndexDailyInformation(IndexDailyInformation indexDailyInformation) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(indexDailyInformation);
	}
	
	@Override
	public void saveIndexDailyInformations(List<IndexDailyInformation> idisToSave) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		for (IndexDailyInformation indexDailyInformation : idisToSave) {
			session.save(indexDailyInformation);
		}
		session.flush();
		session.clear();
		tx.commit();
	}

	@Override
	public IndexDailyInformation getIndexDailyInformationById(int id) {
		Session session = sessionFactory.getCurrentSession();
		IndexDailyInformation indexDailyInformation = session.get(IndexDailyInformation.class, id);
		return indexDailyInformation;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<IndexDailyInformation> getIndexDailyInformations() {
		Session session = sessionFactory.getCurrentSession();
		List<IndexDailyInformation> indexDailyInformation = session.createCriteria(IndexDailyInformation.class).list();
		return indexDailyInformation;
	}

	@Override
	public void updateIndexDailyInformation(IndexDailyInformation indexDailyInformation) {
		Session session = sessionFactory.getCurrentSession();
		session.update(indexDailyInformation);
	}
	
	@Override
	public void updateIndexDailyInformations(List<IndexDailyInformation> idisToUpdate) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		for (IndexDailyInformation indexDailyInformation : idisToUpdate) {
			session.update(indexDailyInformation);
		}
		session.flush();
		session.clear();
		tx.commit();
	}

	@Override
	public void deleteIndexDailyInformation(IndexDailyInformation indexDailyInformation) {
		Session session = sessionFactory.getCurrentSession();
		int id = indexDailyInformation.getId();
		indexDailyInformation = session.get(IndexDailyInformation.class, id);
		if (indexDailyInformation != null) {
			session.delete(indexDailyInformation);
		}
	}
}
