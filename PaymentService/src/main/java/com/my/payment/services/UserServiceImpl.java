package com.my.payment.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.payment.dao.OperationDAO;
import com.my.payment.dao.RoleDAO;
import com.my.payment.dao.UserDAO;
import com.my.payment.exceptions.NoOperationsWereFoundException;
import com.my.payment.exceptions.NoRolesWereFoundException;
import com.my.payment.exceptions.NoUsersWereFoundException;
import com.my.payment.exceptions.OperationNotFoundException;
import com.my.payment.exceptions.RoleNotFoundException;
import com.my.payment.exceptions.UserNotFoundException;
import com.my.payment.model.Operation;
import com.my.payment.model.User;

@Service
@Transactional(rollbackFor ={OperationNotFoundException.class, 
							NoOperationsWereFoundException.class,
							UserNotFoundException.class,  
							NoUsersWereFoundException.class,
							RoleNotFoundException.class, 
							NoRolesWereFoundException.class})
public class UserServiceImpl implements UserService{
	@Autowired(required=true)
	private UserDAO userDAO;
	@Autowired(required=true)
	private OperationDAO operationDAO;
	@Autowired(required=true)
	private RoleDAO roleDAO;

	@Override
	public User getUser(String email) {
		return userDAO.getUser(email);
	}
	@Override
	public void registerUser(String email, String password,float amount, String role) {
		userDAO.setUser(new User(email,password,amount,roleDAO.getRole(role)));
	}
	@Override
	public User getUser(int id) {
		return userDAO.getUser(id);
	}
	@Override
	public List<User> getUsers(Pageable pageable) {
		return userDAO.getUsers(pageable);
	}
	@Override
	public Integer pageCountUsers(Pageable pageable) {
		return userDAO.pageCountUsers(pageable);
	}
	@Override
	public void deleteUser(int id) {
		userDAO.deleteUser(id);
	}
	@Override
	public void balanceReplenishment(int userId,int adminId, float amount) {
		userDAO.balanceReplenishment(userId, amount);
		operationDAO.setOperation(new Operation(userDAO.getUser(userId),userDAO.getUser(adminId),amount)) ;
	}
}
