package my.app.domain;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import my.app.security.PasswordHasher;

@Entity
public class User {

	//For registration only the first name, user name and password is required, other fields are optional
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	private String firstName;

	private String lastName;
	
	@NotNull
	@NotEmpty
	@Column(unique=true)
	private String userName;
	
	@NotNull
	private String passwordHash;
	
	@NotNull
	private String passwordSalt;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dob;
	
	private Long cashAmount;
	
	private String goal;
	
	public User() {
		
	}
	
	public User(String firstName, String userName, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		this.firstName = firstName;
		this.userName = userName;
		this.passwordSalt = PasswordHasher.toHex(PasswordHasher.getSalt());
		this.passwordHash = PasswordHasher.hashPassword(password, passwordSalt);
	}
	
	public int getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}
	
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public String getPasswordSalt() {
		return passwordSalt;
	}
	
	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Long getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(Long cashAmount) {
		this.cashAmount = cashAmount;
	}
	
	public String getGoal() {
		return goal;
	}
	
	public void setGoal(String goal) {
		this.goal = goal;
	}
}
