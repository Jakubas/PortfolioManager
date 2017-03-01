package my.app.dao.portfolio;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import my.app.domains.portfolio.PortfolioDailyInformation;
import my.app.domains.user.User;

@Repository
@Transactional
public class PortfolioDailyInformationDAOImpl implements PortfolioDailyInformationDAO {

	private final SessionFactory sessionFactory;
	
	@Autowired
	public PortfolioDailyInformationDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void savePortfolioDailyInformation(PortfolioDailyInformation pdi) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(pdi);
	}
	
	public void savePortfolioDailyInformations(List<PortfolioDailyInformation> pdis) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		for (PortfolioDailyInformation pdi : pdis) {
			session.save(pdi);
		}
		session.flush();
		session.clear();
		tx.commit();
	}

	@Override
	public PortfolioDailyInformation getPortfolioDailyInformationById(int id) {
		Session session = sessionFactory.getCurrentSession();
		PortfolioDailyInformation pdi = session.get(PortfolioDailyInformation.class, id);
		return pdi;
	}
	
	@Override
	public PortfolioDailyInformation getPortfolioDailyInformationByDate(LocalDate date, User user) {
		Session session = sessionFactory.getCurrentSession();
		
		Criteria cr = session.createCriteria(PortfolioDailyInformation.class);
		PortfolioDailyInformation pdi = new PortfolioDailyInformation();
		pdi.setDate(date);
		pdi.setUser(user);
		cr.add(Restrictions.eq("date", date));
		cr.add(Restrictions.eq("user", user));
		pdi = (PortfolioDailyInformation) cr.uniqueResult();
		return pdi;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PortfolioDailyInformation> getPortfolioDailyInformations() {
		Session session = sessionFactory.getCurrentSession();
		List<PortfolioDailyInformation> pdis = session.createCriteria(PortfolioDailyInformation.class).list();
		return pdis;
	}

	@Override
	public void updatePortfolioDailyInformation(PortfolioDailyInformation pdi) {
		Session session = sessionFactory.getCurrentSession();
		session.update(pdi);
	}
	
	@Override
	public void updatePortfolioDailyInformations(List<PortfolioDailyInformation> pdis) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		for (PortfolioDailyInformation pdi : pdis) {
			session.update(pdi);
		}
		session.flush();
		session.clear();
		tx.commit();
	}

	@Override
	public void deletePortfolioDailyInformation(PortfolioDailyInformation pdi) {
		Session session = sessionFactory.getCurrentSession();
		int id = pdi.getId();
		pdi = session.get(PortfolioDailyInformation.class, id);
		if (pdi != null) {
			session.delete(pdi);
		}
	}
}
