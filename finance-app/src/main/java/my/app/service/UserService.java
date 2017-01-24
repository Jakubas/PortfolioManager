package my.app.service;

import java.util.List;

import my.app.domain.User;

public interface UserService {

	void saveUser(User user);
	User getUserById(int id);
	User getUserByUserName(String userName);
	List<User> getUsers();
	void updateUser(User user);
	void deleteUser(User user);
	String hashPassword(User user, String password);
	boolean authenticateUser(User user, String password);
}
