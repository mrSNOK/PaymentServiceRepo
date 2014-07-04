package com.my.payment.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.payment.dao.OperationDAO;
import com.my.payment.dao.UserDAO;
import com.my.payment.exceptions.NoRolesWereFoundException;
import com.my.payment.exceptions.NoUsersWereFoundException;
import com.my.payment.exceptions.OperationNotFoundException;
import com.my.payment.exceptions.RoleNotFoundException;
import com.my.payment.exceptions.UserNotFoundException;
import com.my.payment.exceptions.NoOperationsWereFoundException;
import com.my.payment.model.Operation;

@Service
@Transactional(rollbackFor = {OperationNotFoundException.class, 
							 NoOperationsWereFoundException.class,
							 UserNotFoundException.class,  
							 NoUsersWereFoundException.class,
							 RoleNotFoundException.class, 
							 NoRolesWereFoundException.class})
public class OperationServiceImpl implements OperationService{
	@Autowired(required=true)
	private OperationDAO operationDAO;
	@Autowired(required=true)
	private UserDAO userDAO;

	@Override
	public Operation getOperation(int id) {
		return operationDAO.getOperation(id);
	}
	@Override
	public List<Operation> getOperations(Pageable pageable) {
		return operationDAO.getOperations(pageable);
	}
	@Override
	public Integer pageCountOperations(Pageable pageable) {
		return operationDAO.pageCountOperations(pageable);
	}
	@Override
	public List<Operation> getOperationsBetweenDates(Date startDate,Date endDate, Pageable pageable) {
		return operationDAO.getOperationsBetweenDates(startDate, endDate, pageable);
	}
	@Override
	public Integer pageCountOperationsBetweenDates(Date startDate,Date endDate, Pageable pageable) {
		return operationDAO.pageCountOperationsBetweenDates(startDate, endDate, pageable);
	}
	@Override
	public void setOperation(int userId,int adminId, float amount) {
		operationDAO.setOperation(new Operation(userDAO.getUser(userId),userDAO.getUser(adminId),amount)) ;
	}
	@Override
	public void deleteOperation(int id) {
		operationDAO.deleteOperation(id);
	}
}
