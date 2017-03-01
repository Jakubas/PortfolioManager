package my.app.controllers.usercontrollers;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import my.app.domains.user.User;
import my.app.services.user.UserService;
import my.app.utilities.DateUtility;

@Controller
public class RegistrationController {

	private final UserService userService;
	
	public RegistrationController(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET) 
	public String getRegistrationPage() {
		return "/register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerUser(Model model, @RequestParam("username") String username, 
			@RequestParam("password") String password,
			@RequestParam("firstName") String firstName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam(value = "dob", required = false) String dob) {
		try {
			User user = new User(firstName, username, password);
			//check if user is registered
			if (!userService.isUsernameRegistered(username)) {
				if (!lastName.isEmpty()) {
					user.setLastName(lastName);
				}
				if (!dob.isEmpty()) {
					user.setDob(DateUtility.stringToDate(dob));
				}
				userService.saveUser(user);
				return "login";
			} else {
				return registrationFailed(model, "usernameError");
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | ParseException e) {
			return registrationFailed(model, "registerError");
		}
	}
	
	public String registrationFailed(Model model, String error) {
		model.addAttribute(error, true);
		return "register";
	}
}
