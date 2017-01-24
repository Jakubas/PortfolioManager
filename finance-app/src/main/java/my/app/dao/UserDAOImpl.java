package my.app.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import my.app.domain.User;

public class UserDAOImpl implements UserDAO {

	private final SessionFactory sessionFactory;
	
	@Autowired
	public UserDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void saveUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.persist(user);
	}

	public User getUserById(int id) {
		Session session = sessionFactory.getCurrentSession();
		User user = session.get(User.class, id);
		return user;
	}
	
	public User getUserByUserName(String userName) {
		Session session = sessionFactory.getCurrentSession();
		User user = session.get(User.class, userName);
		return user;
	}

	@SuppressWarnings("unchecked")
	public List<User> getUsers() {
		Session session = sessionFactory.getCurrentSession();	
		List<User> users = session.createCriteria(User.class).list();
		return users;
	}

	public void updateUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.update(user);
	}

	public void deleteUser(User user) {
		Session session = sessionFactory.getCurrentSession();
		int id = user.getId();
		user = session.get(User.class, id);
		if (user != null) {
			session.delete(user);
		}
	}
}
