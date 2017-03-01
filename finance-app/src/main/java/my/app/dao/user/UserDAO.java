package my.app.dao.user;

import java.util.List;

import my.app.domains.user.User;

public interface UserDAO {

	void saveUser(User user);
	User getUserById(int id);
	List<User> getUsers();
	void updateUser(User user);
	void deleteUser(User user);
	User getUserByUsername(String userName);
}
