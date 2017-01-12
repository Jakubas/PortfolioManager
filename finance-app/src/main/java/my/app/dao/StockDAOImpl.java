package my.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import my.app.domain.Stock;

@Repository
@Transactional
public class StockDAOImpl implements StockDAO {

	private final SessionFactory sessionFactory;
	
	@Autowired
	public StockDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void saveStock(Stock stock) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(stock);
	}

	public Stock getStockById(int id) {
		Session session = sessionFactory.getCurrentSession();
		Stock stock = session.get(Stock.class, id);
		return stock;
	}

	public Stock getStockByName(String name) {
		Session session = sessionFactory.getCurrentSession();
		Stock stock = session.get(Stock.class, name);
		return stock;
	}

	public Stock getStockByTicker(String ticker) {
		Session session = sessionFactory.getCurrentSession();
		Stock stock = session.get(Stock.class, ticker);
		return stock;
	}

	@SuppressWarnings("unchecked")
	public List<Stock> getStocks() {
		Session session = sessionFactory.getCurrentSession();
		List<Stock> stocks = session.createCriteria(Stock.class).list();
		return stocks;
	}

	public void updateStock(Stock stock) {
		Session session = sessionFactory.getCurrentSession();
		session.update(stock);
	}

	public void deleteStock(Stock stock) {
		Session session = sessionFactory.getCurrentSession();
		int id = stock.getId();
		stock = session.get(Stock.class, id);
		if (stock != null) {
			session.delete(stock);
		}
	}
}
