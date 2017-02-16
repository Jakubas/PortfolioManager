package my.app.controllers.usercontrollers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import my.app.domains.User;
import my.app.services.UserService;

@Controller
public class ProfileController {

	private final UserService userService;
	
	@Autowired
	public ProfileController(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(value = "profile", method = RequestMethod.GET)
	public String getProfile(Principal principal, Model model) {
		String username = principal.getName();
		User user = userService.getUserByUsername(username);
		model.addAttribute("user", user);
		return "userProfile";
	}
}
