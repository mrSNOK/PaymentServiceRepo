package com.my.payment.services;

import javax.servlet.http.HttpServletRequest;

public interface UserAutoLoginService {
	
	public boolean logIN(String	userName, HttpServletRequest request);

}
