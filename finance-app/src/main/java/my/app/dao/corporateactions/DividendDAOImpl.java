package my.app.dao.corporateactions;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import my.app.domains.corporateactions.Dividend;

@Repository
@Transactional
public class DividendDAOImpl implements DividendDAO {

	private final SessionFactory sessionFactory;
	
	@Autowired
	public DividendDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void saveDividend(Dividend dividend) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(dividend);
	}

	@Override
	public Dividend getDividendById(int id) {
		Session session = sessionFactory.getCurrentSession();
		Dividend dividend = session.get(Dividend.class, id);
		return dividend;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Dividend> getDividends() {
		Session session = sessionFactory.getCurrentSession();
		List<Dividend> dividends = session.createCriteria(Dividend.class).list();
		return dividends;
	}

	@Override
	public void updateDividend(Dividend dividend) {
		Session session = sessionFactory.getCurrentSession();
		session.update(dividend);
	}

	@Override
	public void deleteDividend(Dividend dividend) {
		Session session = sessionFactory.getCurrentSession();
		int id = dividend.getId();
		dividend = session.get(Dividend.class, id);
		if (dividend != null) {
			session.delete(dividend);
		}
	}
}
