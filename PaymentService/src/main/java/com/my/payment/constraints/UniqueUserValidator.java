package com.my.payment.constraints;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.my.payment.exceptions.UserNotFoundException;
import com.my.payment.model.User;
import com.my.payment.services.UserService;

public class UniqueUserValidator implements ConstraintValidator<UniqueUser, String>{
	///Injecting user service for checking if username already exists
	@Autowired(required = true)
	UserService userService;
	public void initialize(UniqueUser user) {}
	public boolean isValid(String value, ConstraintValidatorContext context) {
		try {
			///Checking username in db
			User user = userService.getUser(value);
			///If there were no exception, then username is not unique
			return false;
		}catch (UserNotFoundException unfe){
			///Catching exception. Username unique
			return true; 
		}  
	}
}
