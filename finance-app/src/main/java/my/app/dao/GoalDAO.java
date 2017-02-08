package my.app.dao;

import java.util.List;

import my.app.domain.goal.Goal;

public interface GoalDAO {

	void saveGoal(Goal goal);
	Goal getGoalById(int id);
	List<Goal> getGoals();
	void updateGoal(Goal goal);
	void deleteGoal(Goal goal);
}
