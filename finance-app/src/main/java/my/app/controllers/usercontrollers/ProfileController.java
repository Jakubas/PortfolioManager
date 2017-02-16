package my.app.controllers.usercontrollers;

import java.security.Principal;
import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import my.app.controllers.Utility;
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
	
	@RequestMapping(value = "profile", method = RequestMethod.POST)
	public String updateProfile(RedirectAttributes ra, @RequestParam("userId") int id,
			@RequestParam("firstName") String firstName,
			@RequestParam(value = "lastName", required=false) String lastName,
			@RequestParam(value = "dob", required=false) String dobStr,
			@RequestParam(value = "cashAmount", required=false) Double cashAmount) {
		
		User user = userService.getUserById(id);
		user.setFirstName(firstName);
		if (!lastName.isEmpty()) {
			user.setLastName(lastName);
		}
		if (!dobStr.isEmpty()) {
			Date dob;
			try {
				dob = Utility.stringToDate(dobStr);
			} catch (ParseException e) {
				ra.addFlashAttribute("dateError", true);
				return "redirect:/profile";
			}
			user.setDob(dob);
		}
		if (cashAmount != null) {
			user.setCashAmount(cashAmount);
		}
		userService.updateUser(user);
		return "redirect:/profile";
	}
}
