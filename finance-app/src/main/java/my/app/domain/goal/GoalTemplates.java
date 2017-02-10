package my.app.domain.goal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import my.app.domain.goal.Goal.Type;

public class GoalTemplates {

	private GoalTemplates() {}
	
	private final static String A = "I want _x_% of my stock in _y_ sector";
	private final static String B = "I want to move _x_% from _y_ sector to _z_ sector";
	private final static String C = "I want to retire in _x_ years";
	private final static String D = "I want to grow my investments to $_x_";
	private final static String E = "I want to invest for _x_   _y_";
	private final static String F = "I want to have a _x_ risk portfolio";
	private final static String G = "I want to deposit $_x_ per month and grow my investments to $_y_";
	
	@SuppressWarnings("serial")
	public final static List<String> GOAL_TEMPLATES = new ArrayList<String>() {{
		add(A);
		add(B);
		add(C); 
		add(D);
		add(E);
		add(F);
		add(G);
	}};
	
	@SuppressWarnings("serial")
	public final static Map<String, Type> GOAL_TO_TYPE_MAPPING = new HashMap<String,Type>() {{
		put(A, Type.SECTOR);
		put(B, Type.MOVE);
		put(C, Type.RETIRE);
		put(D, Type.GROW_TO_AMOUNT);
		put(E, Type.INVEST_TIME_LENGTH);
		put(F, Type.RISK);
		put(G, Type.MONTHLY_INVESTOR);		
	}};
}
