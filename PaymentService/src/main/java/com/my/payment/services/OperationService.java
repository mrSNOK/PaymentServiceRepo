package com.my.payment.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.my.payment.model.Operation;

public interface OperationService {
	
	public Operation getOperation(int id) ;
	public List<Operation> getOperations(Pageable pageable);
	public Integer pageCountOperations(Pageable pageable);
	public List<Operation> getOperationsBetweenDates(Date startDate, Date endDate,Pageable pageable);
	public Integer pageCountOperationsBetweenDates( Date startDate, Date endDate, Pageable pageable);
	public void setOperation(int userId,int adminId, float amount);
	public void deleteOperation(int id);
}
