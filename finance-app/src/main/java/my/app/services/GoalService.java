package my.app.services;

import java.util.List;

import my.app.domains.goal.Goal;

public interface GoalService {

	void saveGoal(Goal goal);
	Goal getGoalById(int id);
	List<Goal> getGoals();
	void updateGoal(Goal goal);
	void deleteGoal(Goal goal);
}
