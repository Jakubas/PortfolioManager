package my.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LandingController {
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String getLanding() {
		return "index";
	}
}
