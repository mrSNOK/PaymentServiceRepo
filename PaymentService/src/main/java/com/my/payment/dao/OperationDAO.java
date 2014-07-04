package com.my.payment.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.my.payment.model.Operation;

public interface OperationDAO {
	
	public Operation getOperation(int id) ;
	public List<Operation> getOperations(Pageable pageable);
	public Integer pageCountOperations(Pageable pageable);
	public List<Operation> getOperationsBetweenDates(Date startDate, Date endDate,Pageable pageable);
	public Integer pageCountOperationsBetweenDates( Date startDate, Date endDate, Pageable pageable);
	public void setOperation(Operation operation);
	public void deleteOperation(int id);
}
