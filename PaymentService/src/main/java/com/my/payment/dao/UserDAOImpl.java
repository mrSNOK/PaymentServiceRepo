package com.my.payment.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.my.payment.exceptions.NoUsersWereFoundException;
import com.my.payment.exceptions.UserNotFoundException;
import com.my.payment.model.User;


@Repository
public class UserDAOImpl implements UserDAO{
	@Autowired(required=true)
	private SessionFactory sessionFactory;
	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
	
	private Session getCurrentSession() {
		logger.debug("Getting current session...");
		return sessionFactory.getCurrentSession();
	}
	
	private Criteria createUsersCriteria(){
		logger.debug("Creating user criteria...");
		return getCurrentSession().createCriteria(User.class);
	}
	
	private void addOrderCriteria(Criteria criteria){
		logger.debug("Adding user order criteria...");
		if(criteria != null)
			criteria.addOrder(Order.asc("registred"));
	}
	
	private Integer addCountCriteria(Criteria criteria){
		logger.debug("Adding user count criteria...");
		if(criteria != null)
			return	((Long)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		return 0;
	}
	
	private void addPagination(Criteria criteria, Pageable pageable){
		logger.debug("Adding user pagination criteria. Page number :{} .{} {}. {} {}",pageable.getPageNumber(),
				"Page size :",pageable.getPageSize(),
				"Page number :",pageable.getPageNumber());
		if(pageable != null && criteria != null){
			int page = pageable.getPageNumber();
			int pageSize = pageable.getPageSize();
			criteria.setFirstResult(page*pageSize);
			criteria.setMaxResults(pageSize);
		}
	}
	
	private Integer countPages(Pageable pageable, Criteria criteria){
		logger.debug("Counting operations pages...");
		if (pageable != null){
			int users = addCountCriteria(criteria);
			logger.debug("{} {}",users," users were found...");
			if(users > 0){
				int pagesize = pageable.getPageSize();
				logger.debug("Pagesize :{}",pagesize);
				if (users >= pagesize){
					///Count of fully filled pages
					int pages = (int) Math.floor(users/pagesize);
					///Count of residual users
					int residual = users % pagesize;
					///If we have residual users- increase page count
					if (residual > 0)
						pages++;
					return pages;
				}
				return 1;
			}
			throw new NoUsersWereFoundException();
		}
		return null;
	}

	@Override
	public User getUser(String email) {
		logger.debug("Getting user by name :{}",email);
		Query query = getCurrentSession().createQuery("from User u where u.email = :email");
		query.setParameter("email", email);
		List<User> userList = query.list();
		if(userList != null)
			if (userList.size() > 0)
				return userList.get(0);
		throw new UserNotFoundException(email);
	}

	@Override
	public User getUser(int id) {
		logger.debug("Getting user by id :{}",id);
		User user =(User) getCurrentSession().get(User.class, id);
		if (user != null)
			return  user;
		throw new UserNotFoundException(id);
	}

	@Override
	public List<User> getUsers(Pageable pageable) {
		logger.debug("Getting list of users...");
		Criteria criteria = createUsersCriteria();
		addPagination(criteria, pageable);
		addOrderCriteria(criteria);
		List<User> usersList = criteria.list();
		if(usersList != null)
			if(usersList.size() > 0)
				return usersList;
		throw new NoUsersWereFoundException();
	}
	
	@Override
	public Integer pageCountUsers(Pageable pageable) {
		logger.debug("Getting count of pages...");
		Criteria criteria = createUsersCriteria();
		return countPages(pageable, criteria);
	}

	@Override
	public void setUser(User user) {
		logger.debug("Saving user... Email: {} .{} {}. {} {}.",user.getEmail(),
				"Password: ", user.getPassword(),
				"Balance: ", user.getBalance());
		getCurrentSession().saveOrUpdate(user);
	}

	@Override
	public void deleteUser(int id) {
		logger.debug("Deleting user with id: {}",id);
		User user = getUser(id);
		getCurrentSession().delete(user);	
	}

	@Override
	public void balanceReplenishment(int id,float amount) {
		logger.debug("Replanishing balance user with id: {}. {} {}.",id,"Amount: ", amount);
		User user = getUser(id);
		user.setbalance(user.getBalance()+amount);
		setUser(user);
	}
}
