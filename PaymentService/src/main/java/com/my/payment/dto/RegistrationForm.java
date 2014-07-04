package com.my.payment.dto;


import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.my.payment.constraints.FieldEquals;
import com.my.payment.constraints.UniqueUser;
///Custom JSR 303 constraint annotation for comparison password and confirm password fields
@FieldEquals( field="password", equalsTo="confirmPassword" )
public class RegistrationForm {
	
	@NotBlank
	@Email
	///Custom JSR 303 constraint annotation for checking unique user email in DB
	@UniqueUser
	private String email;
	
	@NotBlank
	@Size(min=4)
	private String password;
	
	@NotBlank
	@Size(min=4)
	private String confirmPassword;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((confirmPassword == null) ? 0 : confirmPassword.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegistrationForm other = (RegistrationForm) obj;
		if (confirmPassword == null) {
			if (other.confirmPassword != null)
				return false;
		} else if (!confirmPassword.equals(other.confirmPassword))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}	

}
