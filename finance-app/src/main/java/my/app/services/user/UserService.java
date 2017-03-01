package my.app.services.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import my.app.domains.user.User;

public interface UserService {

	void saveUser(User user);
	User getUserById(int id);
	User getUserByUsername(String username);
	List<User> getUsers();
	void updateUser(User user);
	void deleteUser(User user);
	String hashPassword(User user, String password) throws NoSuchAlgorithmException, InvalidKeySpecException;
	boolean authenticateUser(User user, String password);
	void setAdmin(User user, boolean makeAdmin);
	boolean isUsernameRegistered(String username);
}
