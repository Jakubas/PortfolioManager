package my.app.formatter;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class CustomWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

	@Override
	public void addFormatters(FormatterRegistry registry) {
	    super.addFormatters(registry);
	    registry.addFormatterForFieldAnnotation(new 
	    		PercentageFormatAnnotationFormatterFactory());
	}
}
