package my.app.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import my.app.domain.User;
import my.app.domain.goal.Goal;
import my.app.domain.goal.GoalFactory;
import my.app.domain.goal.GoalTemplates;
import my.app.service.GoalService;
import my.app.service.UserService;

@Controller
public class GoalController {

	private final GoalService goalService;
	private final UserService userService;
	private final GoalFactory goalFactory;
	
	@Autowired
	public GoalController(GoalService goalService, UserService userService, GoalFactory goalFactory) {
		this.goalService = goalService;
		this.userService = userService;
		this.goalFactory = goalFactory;
	}
	
	@RequestMapping(value = "portfolio/goals", method = RequestMethod.GET)
	public String getGoals(Model model) {
		List<Goal> goals = goalService.getGoals();
		List<String> goalTemplates = GoalTemplates.goalTemplates;
		model.addAttribute("goals", goals);
		model.addAttribute("goalTemplates", goalTemplates);
		return "goals";
	}
	
	@RequestMapping(value = "portfolio/goals", method = RequestMethod.POST) 
	public String addGoal(Model model, Principal principal,
			@RequestParam("goalStr") String goalStr,
			@RequestParam("goalTarget") String goalTarget,
			@RequestParam("goalTarget2") String goalTarget2,
			@RequestParam("goalTarget3") String goalTarget3)  {
		
		String username = principal.getName();
		User user = userService.getUserByUsername(username);
		System.out.println("-----------------------------");
		System.out.println(user.getUserName());
		System.out.println(goalStr);
		System.out.println(goalTarget);
		System.out.println(goalTarget2);
		System.out.println(goalTarget3);
		System.out.println("-----------------------------");
		Goal goal = goalFactory.getGoal(user, goalStr, goalTarget, goalTarget2, goalTarget3);
		goalService.saveGoal(goal);
		return "redirect:goals";
	}
}
