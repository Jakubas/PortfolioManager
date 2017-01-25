package my.app.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import my.app.domain.User;
import my.app.service.UserService;

@RestController
public class UserController {

	private final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> getUsers() {
		List<User> users = userService.getUsers();
		return users;
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public String createUser(@RequestParam(value = "firstName") String firstName,
			@RequestParam(value = "userName") String userName, 
			@RequestParam(value = "password") String password) {
		try {
		User user = new User(firstName, userName, password);
		userService.saveUser(user);	
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "failed to create user";
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			return "failed to create user";
		}
		return "successfully created user";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public boolean authenticateUser(@RequestParam(value = "userName") String userName, 
			@RequestParam(value = "password") String password) {
		User user = userService.getUserByUserName(userName);
		if (user == null) {
			return false;
		}
		boolean result = userService.authenticateUser(user, password);
		return result;
	}
	
	@RequestMapping(value = "/users/{id}/isAdmin", method = RequestMethod.PUT)
	public String setAdminStatus(@PathVariable(value = "id") int userId, 
			@RequestParam(value = "makeAdmin") boolean makeAdmin) {
		User user = userService.getUserById(userId);
		userService.setAdmin(user, makeAdmin);
		return user.getUserName() + ", isAdmin: " + userService.getUserById(userId).isAdmin();
	}
}
