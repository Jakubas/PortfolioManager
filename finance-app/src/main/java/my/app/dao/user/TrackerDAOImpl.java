package my.app.dao.user;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import my.app.domains.user.Tracker;

@Repository
@Transactional
public class TrackerDAOImpl implements TrackerDAO {

	private final SessionFactory sessionFactory;
	
	@Autowired
	public TrackerDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void saveTracker(Tracker tracker) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(tracker);
	}

	public Tracker getTrackerById(int id) {
		Session session = sessionFactory.getCurrentSession();
		Tracker tracker = session.get(Tracker.class, id);
		return tracker;
	}

	@SuppressWarnings("unchecked")
	public List<Tracker> getTrackers() {
		Session session = sessionFactory.getCurrentSession();	
		List<Tracker> trackers = session.createCriteria(Tracker.class).list();
		return trackers;
	}

	public void updateTracker(Tracker tracker) {
		Session session = sessionFactory.getCurrentSession();
		session.update(tracker);
	}

	public void deleteTracker(Tracker tracker) {
		Session session = sessionFactory.getCurrentSession();
		int id = tracker.getId();
		tracker = session.get(Tracker.class, id);
		if (tracker != null) {
			session.delete(tracker);
		}
	}
}
