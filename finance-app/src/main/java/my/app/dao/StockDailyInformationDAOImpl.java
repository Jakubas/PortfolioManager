package my.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import my.app.domains.StockDailyInformation;

@Repository
@Transactional
public class StockDailyInformationDAOImpl implements StockDailyInformationDAO {

	private final SessionFactory sessionFactory;
	
	@Autowired
	public StockDailyInformationDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void saveStockInformation(StockDailyInformation stockInformation) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(stockInformation);
	}
	
	public void saveStockInformations(List<StockDailyInformation> stockInformations) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		for (StockDailyInformation stockInformation : stockInformations) {
			session.save(stockInformation);
		}
		session.flush();
		session.clear();
		tx.commit();
	}

	public StockDailyInformation getStockInformationById(int id) {
		Session session = sessionFactory.getCurrentSession();
		StockDailyInformation stockInformation = session.get(StockDailyInformation.class, id);
		return stockInformation;
	}

	@SuppressWarnings("unchecked")
	public List<StockDailyInformation> getStockInformations() {
		Session session = sessionFactory.getCurrentSession();
		List<StockDailyInformation> stockInformations = session.createCriteria(StockDailyInformation.class).list();
		return stockInformations;
	}

	public void updateStockInformation(StockDailyInformation stockInformation) {
		Session session = sessionFactory.getCurrentSession();
		session.update(stockInformation);
	}
	
	public void updateStockInformations(List<StockDailyInformation> stockInformations) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		for (StockDailyInformation stockInformation : stockInformations) {
			session.update(stockInformation);
		}
		session.flush();
		session.clear();
		tx.commit();
	}

	public void deleteStockInformation(StockDailyInformation stockInformation) {
		Session session = sessionFactory.getCurrentSession();
		int id = stockInformation.getId();
		stockInformation = session.get(StockDailyInformation.class, id);
		if (stockInformation != null) {
			session.delete(stockInformation);
		}
	}

	@SuppressWarnings("unchecked")
	public List<StockDailyInformation> getStockInformationsByStockId(int stockId) {
		Session session = sessionFactory.getCurrentSession();
		List<StockDailyInformation> stockInformations =
				session.createSQLQuery("SELECT * FROM stock_information WHERE stock_id =" + stockId)
				.addEntity(StockDailyInformation.class).list();
		return stockInformations;
	}
}
