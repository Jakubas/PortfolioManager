package my.app.domain.goal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import my.app.domain.goal.Goal.Type;

public class GoalTemplates {

	private final static String a = "I want X% of my stock in y sector";
	private final static String b = "I want to move x% from y sector to z sector";
	private final static String c = "I want to retire in x years";
	private final static String d = "I want to grow my investments to $x";
	private final static String e = "I want to invest for x y";
	private final static String f = "I want to have a x risk portfolio";
	private final static String g = "I want to deposit $x per month and grow my investments to $y";

	private GoalTemplates() {
		
	}
	
	@SuppressWarnings("serial")
	public final static List<String> goalTemplates = new ArrayList<String>() {{
		add(a);
		add(b);
		add(c); 
		add(d);
		add(e);
		add(f);
		add(g);
	}};
	
	@SuppressWarnings("serial")
	public final static Map<String, Type> goalToTypeMapping = new HashMap<String,Type>() {{
		put(a, Type.SECTOR);
		put(b, Type.MOVE);
		put(c, Type.RETIRE);
		put(d, Type.GROW_TO_AMOUNT);
		put(e, Type.INVEST_TIME_LENGTH);
		put(f, Type.RISK);
		put(g, Type.MONTHLY_INVESTOR);		
	}};
}
