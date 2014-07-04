package com.my.payment.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.my.payment.model.User;

public interface UserService {
	
	public User getUser(int id);
	public User getUser(String email);
	public List<User> getUsers(Pageable pageable);
	public Integer pageCountUsers(Pageable pageable);
	public void registerUser(String email, String password,float amount, String role);
	public void deleteUser(int id);
	public void balanceReplenishment(int userId,int adminId, float amount);
}
