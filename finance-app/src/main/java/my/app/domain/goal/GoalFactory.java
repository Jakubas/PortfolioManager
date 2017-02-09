package my.app.domain.goal;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import my.app.domain.User;
import my.app.domain.goal.Goal.Type;

@Component
public class GoalFactory {
	
	public Goal getGoal(User user, String goalTemplate, Double percentage, 
			String sector1, String sector2, Integer monthlyDepositAmount, 
			Integer amount, Double length, String risk, String monthsOrYears) {
		
	   Type type = getType(goalTemplate);
	   String goalStr = generateGoalText(goalTemplate, type, percentage, sector1, sector2, 
			   monthlyDepositAmount, amount, length, risk, monthsOrYears);
	   
	   Goal goal = new Goal(user, type, goalStr);
	   setGoalTargets(goal, percentage, sector1, sector2, 
			   monthlyDepositAmount, amount, length, risk, monthsOrYears);
	   return goal;
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
		List<String> templates = GoalTemplates.goalTemplates;
		Map<String, Type> goalTypeMapping = GoalTemplates.goalToTypeMapping;
		
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
		goal.setPercentage(percentage);
		goal.setSector1(sector1);
		goal.setSector2(sector2);
		goal.setMonthlyDepositAmount(monthlyDepositAmount);
		goal.setAmount(amount);
		goal.setLength(length);
		goal.setRisk(risk);
	}
}
