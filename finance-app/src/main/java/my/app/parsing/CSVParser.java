package my.app.parsing;

public class CSVParser {

	//This method splits a line from a comma separated values file into an array of strings
	public String[] splitLine(String line) {
		
		StringBuffer buffer = new StringBuffer();
		String[] stockStrs = new String[3];
		
		char[] chars = line.toCharArray();
		int i = 0;
		boolean inQuotes = false;
		for (char ch: chars) {
			if (inQuotes) {
				if (ch == '"') {
					inQuotes = false;
				} else {
					buffer.append(ch);
				}
			} else {
				if (ch == '"') {
					inQuotes = true;
				} else if (ch == ',') {
					stockStrs[i++] = buffer.toString();
					buffer.delete(0, buffer.length());
				} else {
					buffer.append(ch);
				}
			}
		}
		stockStrs[i++] = buffer.toString();
		return stockStrs;
	}
}
