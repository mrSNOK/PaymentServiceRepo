package com.my.payment.services;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class UserAutoLoginServiceImpl implements UserAutoLoginService{
	@Autowired(required=true)
	private UserDetailsService userDetailsService;
	@Autowired(required=true)
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;
	private static final Logger logger = LoggerFactory.getLogger(UserAutoLoginServiceImpl.class);
	
	@Override
	public boolean logIN(String userName, HttpServletRequest request) {
		try{
			UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken
					(userName, userDetails.getPassword(), userDetails.getAuthorities());
			// generate session if one doesn't exist
			request.getSession();
			//save details as WebAuthenticationDetails records the remote address and will also set the session Id if a session already exists (it won't create one)
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication authentication = authenticationManager.authenticate(token);
			//Need to set this as thread locale as available throughout
			SecurityContextHolder.getContext().setAuthentication(authentication);
			//Set SPRING_SECURITY_CONTEXT attribute in session as Spring identifies context through this attribute
			request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
											  SecurityContextHolder.getContext());
		} catch (Exception e) {
			logger.warn("User {}{}",userName," faild with autologin!",e);
			SecurityContextHolder.getContext().setAuthentication(null);
			return false;
		}
		return true;		
	}
}
