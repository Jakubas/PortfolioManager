package my.app.dao;

import java.util.List;

import my.app.domain.User;

public interface UserDAO {

	void saveUser(User user);
	User getUserById(int id);
	List<User> getUsers();
	void updateUser(User user);
	void deleteUser(User user);
	User getUserByUserName(String userName);
}
