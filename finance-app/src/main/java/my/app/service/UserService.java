package my.app.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import my.app.domain.User;

public interface UserService {

	void saveUser(User user);
	User getUserById(int id);
	User getUserByUserName(String userName);
	List<User> getUsers();
	void updateUser(User user);
	void deleteUser(User user);
	String hashPassword(User user, String password) throws NoSuchAlgorithmException, InvalidKeySpecException;
	boolean authenticateUser(User user, String password);
}
