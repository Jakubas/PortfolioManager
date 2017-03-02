package my.app.controllers.portfoliocontrollers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import my.app.domains.portfolio.goal.Goal;
import my.app.domains.portfolio.goal.Goal.Type;
import my.app.domains.portfolio.goal.GoalFactory;
import my.app.domains.portfolio.goal.GoalTemplates;
import my.app.domains.user.User;
import my.app.risk.RiskValues;
import my.app.services.portfolio.GoalService;
import my.app.services.stock.StockService;
import my.app.services.user.UserService;

@Controller
public class GoalController {

	private final GoalService goalService;
	private final UserService userService;
	private final StockService stockService;
	private final GoalFactory goalFactory;
	
	@Autowired
	public GoalController(GoalService goalService, UserService userService, 
			StockService stockService, GoalFactory goalFactory) {
		this.goalService = goalService;
		this.userService = userService;
		this.goalFactory = goalFactory;
		this.stockService = stockService;
	}
	
	@RequestMapping(value = "portfolio/goals", method = RequestMethod.GET)
	public String getGoals(Principal principal, Model model) {
		String username = principal.getName();
		User user = userService.getUserByUsername(username);
		List<Goal> goals = user.getGoals();
		List<Goal> otherGoals = new ArrayList<Goal>(goals);
		Collections.copy(otherGoals, goals);
		List<Goal> balanceGoals = filterBy(goals, Type.SECTOR);
		otherGoals.removeAll(balanceGoals);
		List<String> goalTemplates = GoalTemplates.GOAL_TEMPLATES;
		Map<String, Type> goalToTypeMapping = GoalTemplates.GOAL_TO_TYPE_MAPPING;
		List<String> sectors = stockService.getSectors();
		List<String> risks = RiskValues.RISKS;
		model.addAttribute("balanceGoals", balanceGoals);
		model.addAttribute("otherGoals", otherGoals);
		model.addAttribute("goalTemplates", goalTemplates);
		model.addAttribute("typeMap", goalToTypeMapping);
		model.addAttribute("sectors", sectors);
		model.addAttribute("risks", risks);
		return "portfolio/goals";
	}
	 
	private List<Goal> filterBy(List<Goal> goals, Type type) {
		goals.removeIf(o -> !o.getType().equals(type));
		return goals;
	}

	@RequestMapping(value = "portfolio/goals", method = RequestMethod.POST) 
	public String addGoal(RedirectAttributes ra, Principal principal,
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
		Goal goal;
		try {
			goal = goalFactory.getGoal(user, goalTemplate, percentage, sector1, sector2, monthlyDepositAmount, amount, length, risk, monthsOrYears);
		} catch (Exception e) {
			ra.addFlashAttribute("error", true);
			return "redirect:/portfolio/goals";
		}
		goalService.saveGoal(goal);
		return "redirect:/portfolio/goals";
	}

	@RequestMapping(value = "portfolio/goals/{goalId}", method = RequestMethod.DELETE) 
	public String deleteGoal(@PathVariable("goalId") int id) { 
		Goal goal = goalService.getGoalById(id);
		goalService.deleteGoal(goal);
		return "redirect:/portfolio/goals";
	}
}
