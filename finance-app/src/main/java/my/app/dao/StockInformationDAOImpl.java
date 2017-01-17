package my.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import my.app.domain.StockInformation;

@Repository
@Transactional
public class StockInformationDAOImpl implements StockInformationDAO {

	private final SessionFactory sessionFactory;
	
	@Autowired
	public StockInformationDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void saveStockInformation(StockInformation stockInformation) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(stockInformation);
	}
	
	public void saveStockInformations(List<StockInformation> stockInformations) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		for (StockInformation stockInformation : stockInformations) {
			session.save(stockInformation);
		}
		session.flush();
		session.clear();
		tx.commit();
	}

	public StockInformation getStockInformationById(int id) {
		Session session = sessionFactory.getCurrentSession();
		StockInformation stockInformation = session.get(StockInformation.class, id);
		return stockInformation;
	}

	@SuppressWarnings("unchecked")
	public List<StockInformation> getStockInformations() {
		Session session = sessionFactory.getCurrentSession();
		List<StockInformation> stockInformations = session.createCriteria(StockInformation.class).list();
		return stockInformations;
	}

	public void updateStockInformation(StockInformation stockInformation) {
		Session session = sessionFactory.getCurrentSession();
		session.update(stockInformation);
	}

	public void deleteStockInformation(StockInformation stockInformation) {
		Session session = sessionFactory.getCurrentSession();
		int id = stockInformation.getId();
		stockInformation = session.get(StockInformation.class, id);
		if (stockInformation != null) {
			session.delete(stockInformation);
		}
	}

	@SuppressWarnings("unchecked")
	public List<StockInformation> getStockInformationsByStockId(int stockId) {
		Session session = sessionFactory.getCurrentSession();
		List<StockInformation> stockInformations =
				session.createSQLQuery("SELECT * FROM stock_information WHERE stock_id =" + stockId)
				.addEntity(StockInformation.class).list();
		return stockInformations;
	}
}
