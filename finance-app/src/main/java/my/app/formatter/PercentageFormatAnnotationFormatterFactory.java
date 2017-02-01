package my.app.formatter;

import java.util.HashSet;
import java.util.Set;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

public class PercentageFormatAnnotationFormatterFactory
implements AnnotationFormatterFactory<PercentageFormat> {

	@Override
	public Set<Class<?>> getFieldTypes() {
		Set<Class<?>> setTypes = new HashSet<Class<?>>();
	    setTypes.add(Double.class);
	    return setTypes;
	}

	@Override
	public Parser<?> getParser(PercentageFormat arg0, Class<?> arg1) {
		return new PercentageFormatFormatter();
	}

	@Override
	public Printer<?> getPrinter(PercentageFormat arg0, Class<?> arg1) {
		return new PercentageFormatFormatter();
	}
}