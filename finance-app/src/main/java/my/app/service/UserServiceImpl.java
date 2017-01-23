package my.app.service;

import java.util.List;

import my.app.dao.UserDAO;
import my.app.domain.User;

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
}
