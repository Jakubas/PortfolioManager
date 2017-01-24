package my.app.security;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHasher {
	
	private static final int ITERATIONS = 2000;
	
	private PasswordHasher() { }
	
	public static byte[] getSalt() {
		Random random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
	    return salt;
	}

	public static String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] passwordChars = password.toCharArray();
        
        PBEKeySpec spec = new PBEKeySpec(passwordChars, salt, ITERATIONS, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return toHex(hash);
	}
	
	public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		return hashPassword(password, salt.getBytes());
	}
	
    public static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
    
    public static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++) {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

	public static boolean checkPassword(String originalPasswordHash, String checkPasswordHash) throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] checkHash =  fromHex(checkPasswordHash);
      	byte[] originalHash = fromHex(originalPasswordHash);
      	
      	boolean result = Arrays.equals(checkHash, originalHash);
		return result;
	}
}
