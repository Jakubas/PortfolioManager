package my.app.domain.goal;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import my.app.domain.User;
import my.app.domain.goal.Goal.Type;

public class GoalFactory {

	public Goal getGoal(User user, String goalStr, String goalTarget, String goalTarget2, String goalTarget3) {
	   Type type = getType(goalStr);
	   
	   //for some goals goalTarget2 and goalTarget3 can be null
	   //e.g. I want x% of my stock in y sector, only has two targets x and y
	   return new Goal(user, type, goalStr, goalTarget, goalTarget2, goalTarget3);
   }
	
	//compare the string to all template strings and return the type of the most similar one
	private Type getType(String goalStr) {
		
		List<String> templates = GoalTemplates.goalTemplates;
		Map<String, Type> goalTypeMapping = GoalTemplates.goalToTypeMapping;
		
		String mostSimiliar = templates.get(0);
		double highestScore = 0.0;
		for (int i = 0; i < templates.size(); i++) {
			String template = templates.get(i);
			double matchingScore = compareStrings(goalStr, template);
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
}
