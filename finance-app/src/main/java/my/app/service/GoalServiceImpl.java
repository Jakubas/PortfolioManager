package my.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import my.app.dao.GoalDAO;
import my.app.domain.goal.Goal;

@Service
public class GoalServiceImpl implements GoalService {
	
	private final GoalDAO dao;
	
	public GoalServiceImpl(GoalDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public void saveGoal(Goal goal) {
		dao.saveGoal(goal);
	}

	@Override
	public Goal getGoalById(int id) {
		Goal goal = dao.getGoalById(id);
		return goal;
	}

	@Override
	public List<Goal> getGoals() {
		List<Goal> goals = dao.getGoals();
		return goals;
	}

	@Override
	public void updateGoal(Goal goal) {
		dao.updateGoal(goal);
	}

	@Override
	public void deleteGoal(Goal goal) {
		dao.deleteGoal(goal);
	}
}
