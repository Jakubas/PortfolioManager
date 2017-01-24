package my.app.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

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
	
	public User getUserByUserName(String userName) {
		User user = dao.getUserByUserName(userName);
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
		String salt = user.getPasswordSalt();
		String passwordHash = PasswordHasher.hashPassword(password, salt);
		return passwordHash;
	}

	public boolean authenticateUser(User user, String password) {
		try {
			String originalPasswordHash = user.getPasswordHash();
			String checkPasswordHash = hashPassword(user, password);
			return PasswordHasher.checkPassword(originalPasswordHash, checkPasswordHash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			return false;
		}
	}  
}
