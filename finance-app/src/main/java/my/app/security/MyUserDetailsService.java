package my.app.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import my.app.service.UserService;

@Service
public class MyUserDetailsService implements UserDetailsService {

	private final UserService userService;
	
	@Autowired
	public MyUserDetailsService(UserService userService) {
		this.userService = userService;
	}
	
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		my.app.domain.User user = userService.getUserByUserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Username " + userName + " not found");
		}
		return new User(userName, user.getPasswordHash(), getGrantedAuthorities(user));
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