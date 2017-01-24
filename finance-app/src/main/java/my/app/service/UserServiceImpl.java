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
	
	public String hashPassword(User user, String password) {
		return null;
	}

	public boolean authenticateUser(User user, String password) {
		return false;
	}
	
//	   private static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
//	    {
//	        String[] parts = storedPassword.split(":");
//	        int iterations = Integer.parseInt(parts[0]);
//	        byte[] salt = fromHex(parts[1]);
//	        byte[] hash = fromHex(parts[2]);
//	         
//	        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
//	        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//	        byte[] testHash = skf.generateSecret(spec).getEncoded();
//	         
//	        int diff = hash.length ^ testHash.length;
//	        for(int i = 0; i < hash.length && i < testHash.length; i++)
//	        {
//	            diff |= hash[i] ^ testHash[i];
//	        }
//	        return diff == 0;
//	    }
	   
}
