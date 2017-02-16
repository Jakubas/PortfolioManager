package my.app.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import my.app.domains.StockInPortfolio;

@Repository
@Transactional
public class StockInPortfolioDAOImpl implements StockInPortfolioDAO {

	
	private final SessionFactory sessionFactory;
	
	@Autowired
	public StockInPortfolioDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void saveStockInPortfolio(StockInPortfolio stockInPortfolio) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(stockInPortfolio);
	}

	@Override
	public StockInPortfolio getStockInPortfolioById(int id) {
		Session session = sessionFactory.getCurrentSession();
		StockInPortfolio stockInPortfolio = session.get(StockInPortfolio.class, id);
		return stockInPortfolio;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<StockInPortfolio> getStocksInPortfolio() {
		Session session = sessionFactory.getCurrentSession();
		List<StockInPortfolio> stocksInPortfolio = session.createCriteria(StockInPortfolio.class).list();
		return stocksInPortfolio;
	}

	@Override
	public void updateStockInPortfolio(StockInPortfolio stockInPortfolio) {
		Session session = sessionFactory.getCurrentSession();
		session.update(stockInPortfolio);
	}

	@Override
	public void deleteStockInPortfolio(StockInPortfolio stockInPortfolio) {
		Session session = sessionFactory.getCurrentSession();
		int id = stockInPortfolio.getId();
		stockInPortfolio = session.get(StockInPortfolio.class, id);
		if (stockInPortfolio != null) {
			session.delete(stockInPortfolio);
		}
	}
	
}
