package com.my.payment.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.payment.dao.RoleDAO;
import com.my.payment.dao.RoleDAOImpl;
import com.my.payment.exceptions.NoOperationsWereFoundException;
import com.my.payment.exceptions.NoRolesWereFoundException;
import com.my.payment.exceptions.NoUsersWereFoundException;
import com.my.payment.exceptions.OperationNotFoundException;
import com.my.payment.exceptions.RoleNotFoundException;
import com.my.payment.exceptions.UserNotFoundException;
import com.my.payment.model.Role;

@Service
@Transactional(rollbackFor ={OperationNotFoundException.class, 
		 					NoOperationsWereFoundException.class,
		 					UserNotFoundException.class,  
		 					NoUsersWereFoundException.class,
		 					RoleNotFoundException.class, 
		 					NoRolesWereFoundException.class})
public class RoleServiceImpl implements RoleService{
	@Autowired(required=true)
	private RoleDAO roleDAO;
	@Override
	public Role getRole(int id) {
		return roleDAO.getRole(id);
	}
	@Override
	public Role getRole(String role) {
		return roleDAO.getRole(role);
	}
	@Override
	public List<Role> getRoles() {
		return roleDAO.getRoles();
	}
	@Override
	public void setRole(Role role) {
		roleDAO.setRole(role);
	}
}
