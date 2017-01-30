package my.app.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import my.app.dao.UserDAO;
import my.app.domain.User;
import my.app.security.PasswordHasher;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserDAO dao;
	
	public UserServiceImpl(UserDAO dao) {
		this.dao = dao;
	}
	
	public void saveUser(User user) {
		dao.saveUser(user);
	}

	public User getUserById(int id) {
		User user = dao.getUserById(id);
		return user;
	}
	
	public User getUserByUsername(String username) {
		User user = dao.getUserByUsername(username);
		return user;
	}

	public List<User> getUsers() {
		List<User> users = dao.getUsers();
		return users;
	}

	public void updateUser(User user) {
		dao.updateUser(user);
	}

	public void deleteUser(User user) {
		dao.deleteUser(user);
	}
	
	public String hashPassword(User user, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String passwordHash = passwordEncoder.encode(password);
		return passwordHash;
	}

	public boolean authenticateUser(User user, String password) {
		String passwordHash = user.getPasswordHash();
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(password, passwordHash);
	}
	
	public void setAdmin(User user, boolean makeAdmin) {
		if (makeAdmin) {
			user.setAdminPrivileges(true);
		} else {
			user.setAdminPrivileges(false);
		}
		updateUser(user);
	}

	public boolean isUsernameRegistered(String username) {
		return false;
	}
}
