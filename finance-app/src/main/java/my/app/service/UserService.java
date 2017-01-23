package my.app.service;

import java.util.List;

import my.app.domain.User;

public interface UserService {

	void saveUser(User user);
	User getUserById(int id);
	List<User> getUsers();
	void updateUser(User user);
	void deleteUser(User user);
}
