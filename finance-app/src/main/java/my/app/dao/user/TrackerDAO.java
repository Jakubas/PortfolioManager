package my.app.dao.user;

import java.util.List;

import my.app.domains.user.Tracker;

public interface TrackerDAO {

	void saveTracker(Tracker tracker);
	Tracker getTrackerById(int id);
	List<Tracker> getTrackers();
	void updateTracker(Tracker tracker);
	void deleteTracker(Tracker tracker);
}
