package my.app.formatter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

public class PercentageFormatFormatter implements Formatter<Double> {

	@Override
	public String print(Double arg0, Locale arg1) {
		arg0 = arg0 * 100;
		DecimalFormat formatter = new DecimalFormat("######.##");
		return formatter.format(arg0) + "%";
	}

	@Override
	public Double parse(String arg0, Locale arg1) throws ParseException {
		return Double.parseDouble(arg0);
	}
}
