package my.app.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import my.app.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserService userService;
	
	@Autowired
	public UserDetailsServiceImpl(UserService userService) {
		this.userService = userService;
	}
	
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		System.out.println("Username: " + userName);
		my.app.domain.User user = userService.getUserByUserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Username " + userName + " not found");
		}
		return new User(user.getUserName(), user.getPasswordHash(), getGrantedAuthorities(user));
	}
	
	public List<GrantedAuthority> getGrantedAuthorities(my.app.domain.User user) {
		List<GrantedAuthority> roleNames = new ArrayList<GrantedAuthority>();
		if (user.isAdmin()) {
			roleNames.add(new SimpleGrantedAuthority("ROLE_USER"));
			roleNames.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else {
			roleNames.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
        return roleNames;
	}
}