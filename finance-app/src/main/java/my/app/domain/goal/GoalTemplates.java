package my.app.domain.goal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import my.app.domain.goal.Goal.Type;

public class GoalTemplates {

	private GoalTemplates() {
		
	}
	
	@SuppressWarnings("serial")
	public final static List<String> goalTemplates = new ArrayList<String>() {{
		add("I want x% of my stock in y sector");
		add("I want to move x% from y sector to z sector");
		add("I want to retire in x years");
		add("I want to grow my investments to $x");
		add("I want to invest for x y");
		add("I want to have a x risk portfolio");
		add("I want to deposit $x per month and grow my investments to $y");
	}};
	
	@SuppressWarnings("serial")
	public final static Map<String, Type> goalToTypeMapping = new HashMap<String,Type>() {{
		put("I want x% of my stock in y sector", Type.SECTOR);
		put("I want to move x% from y sector to z sector", Type.MOVE);
		put("I want to retire in x years", Type.RETIRE);
		put("I want to grow my investments to $x", Type.GROW_TO_AMOUNT);
		put("I want to invest for x y", Type.INVEST_TIME_LENGTH);
		put("I want to have a x risk portfolio", Type.RISK);
		put("I want to deposit $x per month and grow my investments to $y", Type.MONTHLY_INVESTOR);		
	}};
}
