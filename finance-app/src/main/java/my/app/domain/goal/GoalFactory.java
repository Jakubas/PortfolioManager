package my.app.domain.goal;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.media.sound.InvalidDataException;

import my.app.domain.User;
import my.app.domain.goal.Goal.Type;
import my.app.service.StockService;

@Component
public class GoalFactory {
	
	private final StockService stockService;
	
	@Autowired
	public GoalFactory(StockService stockService) {
		this.stockService = stockService;
	}
	
	public Goal getGoal(User user, String goalTemplate, Double percentage, 
			String sector1, String sector2, Integer monthlyDepositAmount, 
			Integer amount, Double length, String risk, String monthsOrYears) throws InvalidDataException {
		
		if(!areSectorsValid(sector1, sector2)) {
			throw new InvalidDataException(sector1 + " or " + sector2 + " is not a sector");
		}
        Type type = getType(goalTemplate);
	    String goalStr = generateGoalText(goalTemplate, type, percentage, sector1, sector2, 
			   monthlyDepositAmount, amount, length, risk, monthsOrYears);
	   
	    Goal goal = new Goal(user, type, goalStr);
	    setGoalTargets(goal, percentage, sector1, sector2, 
			   monthlyDepositAmount, amount, length, risk, monthsOrYears);
        return goal;
   }

	//checks if the sectors provided as arguments are in the list of valid sectors
	private boolean areSectorsValid(String sector1, String sector2) {
		List<String> sectors = stockService.getSectors();
		if (sector1 != null) {
			if (!sectors.contains(sector1)) {
				return false;
			}
		}
		if (sector2 != null) {
			return sectors.contains(sector2);
		}
		return true;
	}

	private String generateGoalText(String goalTemplate, Type type, Double percentage, String sector1, String sector2,
			Integer monthlyDepositAmount, Integer amount, Double length, String risk, String monthsOrYears) {
		String result = null;
		switch(type) {
		case GROW_TO_AMOUNT:
			result = fillHoles(goalTemplate, amount.toString());
			break;
		case INVEST_TIME_LENGTH:
			result = fillHoles(goalTemplate, length.toString(), monthsOrYears);
			break;
		case MONTHLY_INVESTOR:
			result = fillHoles(goalTemplate, monthlyDepositAmount.toString(), amount.toString());
			break;
		case MOVE:
			result = fillHoles(goalTemplate, percentage.toString(), sector1, sector2);
			break;
		case RETIRE:
			result = fillHoles(goalTemplate, length.toString());
			break;
		case RISK:
			result = fillHoles(goalTemplate, risk);
			break;
		case SECTOR:
			result = fillHoles(goalTemplate, percentage.toString(), sector1);
			break;
		}
		return result;
	}
	
	private String fillHole(String template, String textForHole) {
		return template.replaceFirst("_(x|y|z)_", textForHole);
	}
	
	private String fillHoles(String template, String textForHole1) {
		return fillHole(template, textForHole1);
	}
	
	private String fillHoles(String template, String textForHole1, String textForHole2) {
		template = fillHoles(template, textForHole1);
		template = fillHoles(template, textForHole2);
		return template;
	}
	
	private String fillHoles(String template, String textForHole1, String textForHole2, String textForHole3) {
		template = fillHoles(template, textForHole1, textForHole2);
		template = fillHoles(template, textForHole3);
		return template;
	}

	//compare the string to all template strings and return the type of the most similar one
	private Type getType(String goalTemplate) {
		List<String> templates = GoalTemplates.GOAL_TEMPLATES;
		Map<String, Type> goalTypeMapping = GoalTemplates.GOAL_TO_TYPE_MAPPING;
		
		String mostSimiliar = templates.get(0);
		double highestScore = 0.0;
		for (int i = 0; i < templates.size(); i++) {
			String template = templates.get(i);
			double matchingScore = compareStrings(goalTemplate, template);
			if (highestScore < matchingScore) {
				highestScore = matchingScore;
				mostSimiliar = template;
			}
		}
		return goalTypeMapping.get(mostSimiliar);
	}
	
	private double compareStrings(String str1, String str2) {
	    return StringUtils.getJaroWinklerDistance(str1, str2);
	}
	
	private void setGoalTargets(Goal goal, Double percentage, String sector1, String sector2,
			Integer monthlyDepositAmount, Integer amount, Double length, String risk, String monthsOrYears) {
		
		if (monthsOrYears != null && monthsOrYears.equals("Months")) {
			length = length/12;
		}
		
		switch(goal.getType()) {
		case GROW_TO_AMOUNT:
			goal.setAmount(amount);
			break;
		case INVEST_TIME_LENGTH:
			goal.setLength(length);
			break;
		case MONTHLY_INVESTOR:
			goal.setMonthlyDepositAmount(monthlyDepositAmount);
			goal.setAmount(amount);
			break;
		case MOVE:
			goal.setPercentage(percentage);
			goal.setSector1(sector1);
			break;
		case RETIRE:
			goal.setLength(length);
			break;
		case RISK:
			goal.setRisk(risk);
			break;
		case SECTOR:
			goal.setPercentage(percentage);
			goal.setSector1(sector1);
			goal.setSector2(sector2);
			break;
		}
	}
}
