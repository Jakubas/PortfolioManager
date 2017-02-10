package my.app.risk;

import java.util.ArrayList;
import java.util.List;

public class RiskValues {

	private RiskValues() {}
	
	public final static String UNKNOWN = "N/A";
	public final static String VERY_LOW = "very low";
	public final static String LOW = "low";
	public final static String MEDIUM = "medium";
	public final static String MEDIUM_HIGH = "medium high";
	public final static String HIGH = "high";
	public final static String VERY_HIGH = "very high";
	
	@SuppressWarnings("serial")
	public final static List<String> RISKS = new ArrayList<String>() {{
		add(UNKNOWN);
		add(VERY_LOW);
		add(LOW); 
		add(MEDIUM);
		add(MEDIUM_HIGH);
		add(HIGH);
		add(VERY_HIGH);
	}};
}
