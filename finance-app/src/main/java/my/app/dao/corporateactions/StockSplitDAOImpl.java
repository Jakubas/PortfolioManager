package my.app.dao.corporateactions;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import my.app.domains.corporateactions.StockSplit;

@Repository
@Transactional
public class StockSplitDAOImpl implements StockSplitDAO {

	private final SessionFactory sessionFactory;
	
	@Autowired
	public StockSplitDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void saveStockSplit(StockSplit stockSplit) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(stockSplit);
	}

	@Override
	public StockSplit getStockSplitById(int id) {
		Session session = sessionFactory.getCurrentSession();
		StockSplit stockSplit = session.get(StockSplit.class, id);
		return stockSplit;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StockSplit> getStockSplits() {
		Session session = sessionFactory.getCurrentSession();
		List<StockSplit> stockSplits = session.createCriteria(StockSplit.class).list();
		return stockSplits;
	}

	@Override
	public void updateStockSplit(StockSplit stockSplit) {
		Session session = sessionFactory.getCurrentSession();
		session.update(stockSplit);
	}

	@Override
	public void deleteStockSplit(StockSplit stockSplit) {
		Session session = sessionFactory.getCurrentSession();
		int id = stockSplit.getId();
		stockSplit = session.get(StockSplit.class, id);
		if (stockSplit != null) {
			session.delete(stockSplit);
		}
	}
}
