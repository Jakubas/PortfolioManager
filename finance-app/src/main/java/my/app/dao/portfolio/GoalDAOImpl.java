package my.app.dao.portfolio;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import my.app.domains.portfolio.goal.Goal;

@Repository
@Transactional
public class GoalDAOImpl implements GoalDAO {

	private final SessionFactory sessionFactory;
	
	@Autowired
	public GoalDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void saveGoal(Goal goal) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(goal);
	}

	@Override
	public Goal getGoalById(int id) {
		Session session = sessionFactory.getCurrentSession();
		Goal goal = session.get(Goal.class, id);
		return goal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Goal> getGoals() {
		Session session = sessionFactory.getCurrentSession();
		List<Goal> goals = session.createCriteria(Goal.class).list();
		return goals;
	}

	@Override
	public void updateGoal(Goal goal) {
		Session session = sessionFactory.getCurrentSession();
		session.update(goal);
	}

	@Override
	public void deleteGoal(Goal goal) {
		Session session = sessionFactory.getCurrentSession();
		int id = goal.getId();
		goal = session.get(Goal.class, id);
		if (goal != null) {
			session.delete(goal);
		}
	}
}
