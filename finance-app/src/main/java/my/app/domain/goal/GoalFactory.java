package my.app.domain.goal;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import my.app.domain.User;
import my.app.domain.goal.Goal.Type;

@Component
public class GoalFactory {

	public Goal getGoal(User user, String goalStr) {
	   Type type = getType(goalStr);
	   Goal goal = new Goal(user, type, goalStr);
	   
	   return goal;
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
