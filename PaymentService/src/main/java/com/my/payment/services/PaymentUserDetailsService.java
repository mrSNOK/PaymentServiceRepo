package com.my.payment.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.payment.dao.UserDAO;
import com.my.payment.model.User;

@Service
@Transactional(readOnly = true)
public class PaymentUserDetailsService implements UserDetailsService {
	@Autowired(required=true)
	private UserDAO userDAO;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userDAO.getUser(email);
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		return new org.springframework.security.core.userdetails.User(
			user.getEmail(), 
			user.getPassword(), 
			enabled, 
			accountNonExpired, 
			credentialsNonExpired, 
			accountNonLocked,
			getAuthorities(user)
			);
	}
	public Collection<? extends GrantedAuthority> getAuthorities(User user) {
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		authList.add(new SimpleGrantedAuthority(user.getRole().getRole()));
		return authList;
	}
}
