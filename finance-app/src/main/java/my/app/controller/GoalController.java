package my.app.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import my.app.domain.User;
import my.app.domain.goal.Goal;
import my.app.domain.goal.Goal.Type;
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
		Map<String, Type> goalToTypeMapping = GoalTemplates.goalToTypeMapping;
		model.addAttribute("goals", goals);
		model.addAttribute("goalTemplates", goalTemplates);
		model.addAttribute("typeMap", goalToTypeMapping);
		return "goals";
	}
	 
	@RequestMapping(value = "portfolio/goals", method = RequestMethod.POST) 
	public String addGoal(Model model, Principal principal,
			@RequestParam(value="goalTemplate") String goalTemplate,
			@RequestParam(value="percentage", required=false) Double percentage,
			@RequestParam(value="sector1", required=false) String sector1,
			@RequestParam(value="sector2", required=false) String sector2,
			@RequestParam(value="monthlyDepositAmount", required=false) Integer monthlyDepositAmount,
			@RequestParam(value="amount", required=false) Integer amount,
			@RequestParam(value="length", required=false) Double length,
			@RequestParam(value="risk", required=false) String risk,
			@RequestParam(value="monthsOrYears", required=false) String monthsOrYears)  {
		
		String username = principal.getName();
		User user = userService.getUserByUsername(username);
		System.out.println("-----------------------------");
		System.out.println(user.getUserName());
		System.out.println(goalTemplate);
		System.out.println(percentage);
		System.out.println(sector1);
		System.out.println(sector2);
		System.out.println(monthlyDepositAmount);
		System.out.println(amount);
		System.out.println(length);
		System.out.println(risk);
		System.out.println(monthsOrYears);
		System.out.println("-----------------------------");
		Goal goal = goalFactory.getGoal(user, goalTemplate, percentage, sector1, sector2, monthlyDepositAmount, amount, length, risk, monthsOrYears);
		goalService.saveGoal(goal);
		return "redirect:goals";
	}
}
