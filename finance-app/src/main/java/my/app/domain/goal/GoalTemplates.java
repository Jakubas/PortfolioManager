package my.app.domain.goal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import my.app.domain.goal.Goal.Type;

public class GoalTemplates {

	private final static String a = "I want _x_% of my stock in _y_ sector";
	private final static String b = "I want to move _x_% from _y_ sector to _z_ sector";
	private final static String c = "I want to retire in _x_ years";
	private final static String d = "I want to grow my investments to $_x_";
	private final static String e = "I want to invest for _x_   _y_";
	private final static String f = "I want to have a _x_ risk portfolio";
	private final static String g = "I want to deposit $_x_ per month and grow my investments to $_y_";

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
