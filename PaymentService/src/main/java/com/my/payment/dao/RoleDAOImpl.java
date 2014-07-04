package com.my.payment.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.my.payment.exceptions.NoUsersWereFoundException;
import com.my.payment.exceptions.RoleNotFoundException;
import com.my.payment.model.Role;


@Repository
public class RoleDAOImpl implements RoleDAO{
	@Autowired(required=true)
	private SessionFactory sessionFactory;
	private static final Logger logger = LoggerFactory.getLogger(RoleDAOImpl.class);
	
	private Session getCurrentSession() {
		logger.debug("Getting current session...");
		return sessionFactory.getCurrentSession();
	}

	@Override
	public Role getRole(int id) {
		logger.debug("Getting role with id: {}",id);
		Role role = (Role) getCurrentSession().get(Role.class, id);
		if (role != null) 
			return role;
		throw new RoleNotFoundException(id);
	}
	@Override
	public Role getRole(String role) {
		logger.debug("Getting role by name: {}",role);
		Query query = getCurrentSession().createQuery("from Role r where r.role = :role");
		query.setParameter("role", role);
		List<Role> roleList = query.list();
		if(roleList != null)
			if (roleList.size() > 0)
				return roleList.get(0);
		throw new RoleNotFoundException(role);
	}

	@Override
	public void setRole(Role role) {
		logger.debug("Setting role with name: {}",role.getRole());
		getCurrentSession().saveOrUpdate(role);
	}

	@Override
	public List<Role> getRoles() {
		logger.debug("Getting list of roles...");
		List<Role> roleList = getCurrentSession().createQuery("from Role").list();
		if (roleList != null)
			if(roleList.size() > 0)
				return roleList;
		throw new NoUsersWereFoundException();
	}

}
