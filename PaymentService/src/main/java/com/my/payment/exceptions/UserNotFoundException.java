package com.my.payment.exceptions;

public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 6091263914046814309L;
	private final Integer userId;
	private final String userName;
		///Constructor for getting by ID exception
	    public UserNotFoundException(Integer userId) {
	    	this.userId = userId;
	    	userName = null;
	    }
	    ///Constructor for getting by name exception
	    public UserNotFoundException(String userName) {
	    	this.userName = userName;
	    	userId = null;
	    }
	    public Integer getUserId() {
	        return userId;
	    }
	    public String getUserName() {
	        return userName;
	    }
}
