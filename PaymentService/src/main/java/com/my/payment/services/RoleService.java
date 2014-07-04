package com.my.payment.services;

import java.util.List;

import com.my.payment.model.Role;

public interface RoleService {
	
	public Role getRole(int id);
	public Role getRole(String role);
	public List<Role> getRoles();
	public void setRole(Role role);

}
