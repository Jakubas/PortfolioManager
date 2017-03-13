package my.app.services.user;

import java.util.List;

import org.springframework.stereotype.Service;

import my.app.dao.user.TrackerDAO;
import my.app.domains.user.Tracker;

@Service
public class TrackerServiceImpl implements TrackerService {
	
	private final TrackerDAO dao;
	
	public TrackerServiceImpl(TrackerDAO dao) {
		this.dao = dao;
	}
	
	public void saveTracker(Tracker tracker) {
		dao.saveTracker(tracker);
	}

	public Tracker getTrackerById(int id) {
		Tracker tracker = dao.getTrackerById(id);
		return tracker;
	}

	public List<Tracker> getTrackers() {
		List<Tracker> trackers = dao.getTrackers();
		return trackers;
	}

	public void updateTracker(Tracker tracker) {
		dao.updateTracker(tracker);
	}

	public void deleteTracker(Tracker tracker) {
		dao.deleteTracker(tracker);
	}
}
