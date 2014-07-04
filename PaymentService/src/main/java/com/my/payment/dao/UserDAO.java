package com.my.payment.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.my.payment.model.User;

public interface UserDAO {
	
	public User getUser(int id);
	public User getUser(String email);
	public List<User> getUsers(Pageable pageable);
	public Integer pageCountUsers(Pageable pageable);
	public void setUser(User user);
	public void deleteUser(int id);
	public void balanceReplenishment(int id,float amount);
}
