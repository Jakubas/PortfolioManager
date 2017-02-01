package my.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import my.app.domain.StockMetrics;

@Repository
@Transactional
public class StockMetricsDAOImpl implements StockMetricsDAO {

	private final SessionFactory sessionFactory;
	
	@Autowired
	public StockMetricsDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void saveStockMetrics(StockMetrics stockMetrics) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(stockMetrics);
	}

	public StockMetrics getStockMetricsById(int id) {
		Session session = sessionFactory.getCurrentSession();
		StockMetrics stockMetrics = session.get(StockMetrics.class, id);
		return stockMetrics;
	}

	@SuppressWarnings("unchecked")
	public List<StockMetrics> getStockMetrics() {
		Session session = sessionFactory.getCurrentSession();
		List<StockMetrics> stockMetrics = session.createCriteria(StockMetrics.class).list();
		return stockMetrics;
	}

	public void updateStockMetrics(StockMetrics stockMetrics) {
		Session session = sessionFactory.getCurrentSession();
		session.update(stockMetrics);
	}

	public void deleteStockMetrics(StockMetrics stockMetrics) {
		Session session = sessionFactory.getCurrentSession();
		int id = stockMetrics.getStockId();
		stockMetrics = session.get(StockMetrics.class, id);
		if (stockMetrics != null) {
			session.delete(stockMetrics);
		}
	}

}
