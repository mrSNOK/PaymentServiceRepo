package com.my.payment.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.my.payment.exceptions.NoOperationsWereFoundException;
import com.my.payment.exceptions.OperationNotFoundException;
import com.my.payment.model.Operation;


@Repository
public class OperationDAOImpl implements OperationDAO{
	@Autowired(required=true)
	private SessionFactory sessionFactory;
	private static final Logger logger = LoggerFactory.getLogger(OperationDAOImpl.class);
	
	private Session getCurrentSession() {
		logger.debug("Getting current session...");
		return sessionFactory.getCurrentSession();
	}
	
	private Criteria createOperationCriteria(){
		logger.debug("Creating operation criteria...");
		return getCurrentSession().createCriteria(Operation.class);
	}
	
	private void addOrderCriteria(Criteria criteria){
		logger.debug("Adding operation order criteria...");
		if(criteria != null)
			criteria.addOrder(Order.asc("created"));
	}
	
	private Integer addCountCriteria(Criteria criteria){
		logger.debug("Adding operation count criteria...");
		if(criteria != null)
			return	((Long)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		return 0;
	}
	
	private void addPagination(Criteria criteria, Pageable pageable){
		logger.debug("Adding operation pagination criteria. Page number :{} {} {}",pageable.getPageNumber(),
				". Page size",pageable.getPageSize());
		if(pageable != null && criteria != null){
			int page = pageable.getPageNumber();
			int pageSize = pageable.getPageSize();
			criteria.setFirstResult(page*pageSize);
			criteria.setMaxResults(pageSize);
		}
	}
	
	private void addDatesCriteria(Criteria criteria, Date startDate, Date endDate){
		logger.debug("Adding date criteria...");
		if(criteria != null){
			if(startDate!=null){
				logger.debug("Adding start date :", startDate);
				criteria.add(Restrictions.ge("created",startDate));
			}
			if(endDate != null){
				logger.debug("Adding end date :", endDate);
				criteria.add(Restrictions.le("created",endDate));
			}
		}
	}
	
	private Integer countPages(Pageable pageable, Criteria criteria){
		logger.debug("Counting operations pages...");
		if (pageable != null){
			int operations = addCountCriteria(criteria);
			logger.debug("{} {}",operations," operations were found...");
			if(operations > 0){
				int pagesize = pageable.getPageSize();
				logger.debug("Pagesize :",pagesize);
				if (operations >= pagesize){
					///Count of fully filled pages
					int pages = (int) Math.floor(operations/pagesize);
					///Count of residual operations
					int residual = operations % pagesize;
					///If we have residual operations- increase page count
					if (residual > 0)
						pages++;
					return pages;
				}
				return 1;
			}
			throw new NoOperationsWereFoundException();
		}
		return null;
	}

	@Override
	public Operation getOperation(int id) {
		logger.debug("Getting operation with id :{}",id);
		Operation operation = (Operation) getCurrentSession().get(Operation.class, id);
		if (operation != null)
			return operation;
		throw new OperationNotFoundException(id);
	}

	@Override
	public List<Operation> getOperations(Pageable pageable) {
		logger.debug("Getting list of opertions...");
		Criteria criteria = createOperationCriteria();
		addPagination(criteria, pageable);
		addOrderCriteria(criteria);
		List<Operation> operationsList = criteria.list();
		if(operationsList != null)
			if(operationsList.size() > 0)
				return operationsList;
		throw new NoOperationsWereFoundException();
	}
	
	@Override
	public Integer pageCountOperations(Pageable pageable) {
		logger.debug("Getting operations count...");
		Criteria criteria = createOperationCriteria();
		return countPages(pageable, criteria);
	}
	
	@Override
	public List<Operation> getOperationsBetweenDates(Date startDate ,Date endDate, Pageable pageable) {
		logger.debug("Getting list of opertions between dates...");
		Criteria criteria = createOperationCriteria();
		addDatesCriteria(criteria, startDate, endDate);
		addPagination(criteria, pageable);
		addOrderCriteria(criteria);
		List<Operation> operationsList = criteria.list();
		if(operationsList != null)
			if(operationsList.size() > 0)
				return operationsList;
		throw new NoOperationsWereFoundException();
	}
	
	@Override
	public Integer pageCountOperationsBetweenDates( Date startDate, Date endDate, Pageable pageable) {
		logger.debug("Getting page count of opertions between dates...");
		Criteria criteria = createOperationCriteria();
		addDatesCriteria(criteria, startDate, endDate);
		return countPages(pageable, criteria);
	}
	
	@Override
	public void setOperation(Operation operation) {
		logger.debug("Saving operation... Admin: {} {} {} {} {}",operation.getAdmin().getEmail(),
				"User: ", operation.getUser().getEmail(),
				"Amount: ", operation.getAmount());
		getCurrentSession().saveOrUpdate(operation);
	}

	@Override
	public void deleteOperation(int id) {
		logger.debug("Deleting operation with id: {}",id);
		Operation operation = getOperation(id);
		getCurrentSession().delete(operation);
	}
}
