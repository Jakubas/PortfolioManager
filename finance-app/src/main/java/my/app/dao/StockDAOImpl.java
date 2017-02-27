package my.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import my.app.domains.Stock;

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
		Criteria cr = session.createCriteria(Stock.class, name);
		cr.add(Restrictions.eq("ticker", name));
		Stock stock = (Stock) cr.list().get(0);
		return stock;
	}

	public Stock getStockByTicker(String ticker) {
		Session session = sessionFactory.getCurrentSession();
		Criteria cr = session.createCriteria(Stock.class, ticker);
		cr.add(Restrictions.eq("ticker", ticker));
		if (!cr.list().isEmpty()) {
			Stock stock = (Stock) cr.list().get(0);
			return stock;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Stock> getStocks() {
		Session session = sessionFactory.getCurrentSession();
		List<Stock> stocks = session.createCriteria(Stock.class).list();
		return stocks;
	}

	public void updateStock(Stock stock) {
		Session session = sessionFactory.getCurrentSession();
		String ticker = stock.getTicker();
		Criteria cr = session.createCriteria(Stock.class, ticker);
		cr.add(Restrictions.eq("ticker", ticker));
		Stock stock2 = (Stock) cr.list().get(0);
		if (stock2 != null) {
			stock2.setMarketCap(stock.getMarketCap());
			stock2.setLastTradePrice(stock.getLastTradePrice());
			stock2.setPERatio(stock.getPERatio());
			session.update(stock2);
		}
	}

	public void deleteStock(Stock stock) {
		Session session = sessionFactory.getCurrentSession();
		int id = stock.getId();
		stock = session.get(Stock.class, id);
		if (stock != null) {
			session.delete(stock);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getSectors() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Stock.class);
		criteria.setProjection(Projections.distinct(Projections.property("sector")));
		List<String> sectors = criteria.list();
		return sectors;
	}
}
