package my.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import my.app.domain.StockCalculatedData;

@Repository
@Transactional
public class StockCalculatedDataDAOImpl implements StockCalculatedDataDAO {

	private final SessionFactory sessionFactory;
	
	@Autowired
	public StockCalculatedDataDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void saveStockCalculatedData(StockCalculatedData stockCalculatedData) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(stockCalculatedData);
	}

	public StockCalculatedData getStockCalculatedDataById(int id) {
		Session session = sessionFactory.getCurrentSession();
		StockCalculatedData stockCalculatedData = session.get(StockCalculatedData.class, id);
		return stockCalculatedData;
	}

	@SuppressWarnings("unchecked")
	public List<StockCalculatedData> getStockCalculatedDatas() {
		Session session = sessionFactory.getCurrentSession();
		List<StockCalculatedData> stockCalculatedDatas = session.createCriteria(StockCalculatedData.class).list();
		return stockCalculatedDatas;
	}

	public void updateStockCalculatedData(StockCalculatedData stockCalculatedData) {
		Session session = sessionFactory.getCurrentSession();
		session.update(stockCalculatedData);
	}

	public void deleteStockCalculatedData(StockCalculatedData stockCalculatedData) {
		Session session = sessionFactory.getCurrentSession();
		int id = stockCalculatedData.getStockId();
		stockCalculatedData = session.get(StockCalculatedData.class, id);
		if (stockCalculatedData != null) {
			session.delete(stockCalculatedData);
		}
	}

}
