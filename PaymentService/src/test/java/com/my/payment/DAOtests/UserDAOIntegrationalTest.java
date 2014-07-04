package com.my.payment.DAOtests;

import static org.junit.Assert.*;

import org.hamcrest.Matchers;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.my.payment.controllers.PaymentController;
import com.my.payment.dao.RoleDAO;
import com.my.payment.dao.UserDAO;
import com.my.payment.exceptions.UserNotFoundException;
import com.my.payment.model.Role;
import com.my.payment.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DAOsConfig.class)
@Transactional
public class UserDAOIntegrationalTest {
	@Autowired
	UserDAO userDAO;
	@Autowired
	RoleDAO roleDAO;
	private static final double DELTA = 1e-15;
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	
	@Test
	public void userDaoTest(){
		Pageable pageable = new PageRequest(0,20);
		User testedUser;
		roleDAO.setRole(new Role("ROLE_TEST_USER"));
		userDAO.setUser(new User(
				"testUser@mail.ru",
				"testPass",
				1000f,
				roleDAO.getRole("ROLE_TEST_USER")));
		
		///Get user by name test
		testedUser = userDAO.getUser("testUser@mail.ru");
		int userID = testedUser.getId();
		assertEquals("Something is wrong wiht e-mail!", "testUser@mail.ru", testedUser.getEmail());
		assertEquals("Something is wrong with pass!", "testPass", testedUser.getPassword());
		assertEquals("Something is wrong with balance!",1000f, testedUser.getBalance(),DELTA);
		assertEquals("Something is wrong with role!", "ROLE_TEST_USER", testedUser.getRole().getRole());
		///Get user by ID test
		testedUser = userDAO.getUser(userID);
		assertEquals("Something is wrong wiht e-mail!", "testUser@mail.ru", testedUser.getEmail());
		assertEquals("Something is wrong with pass!", "testPass", testedUser.getPassword());
		assertEquals("Something is wrong with balance!",1000f, testedUser.getBalance(),DELTA);
		assertEquals("Something is wrong with role!", "ROLE_TEST_USER", testedUser.getRole().getRole());
		///Get list of users test
		List<User> users = userDAO.getUsers(pageable);
		assertNotNull("Somehow there are no users!",users);
		assertThat("Somehow there are no users!",users.size(),Matchers.greaterThan(0));
		///Get page count test
		int pages = userDAO.pageCountUsers(pageable);
		assertNotNull("Somehow there are no pages!",pages);
		assertThat("Somehow there are no pages!",pages,Matchers.greaterThan(0));
		///User balance replenish test
		userDAO.balanceReplenishment(userID, 1000f);
		testedUser = userDAO.getUser(userID);
		assertEquals("Something is wrong wiht e-mail!", "testUser@mail.ru", testedUser.getEmail());
		assertEquals("Something is wrong with pass!", "testPass", testedUser.getPassword());
		assertEquals("Something is wrong with balance!",2000f, testedUser.getBalance(),DELTA);
		assertEquals("Something is wrong with role!", "ROLE_TEST_USER", testedUser.getRole().getRole());
		///User delete test
		userDAO.deleteUser(userID);
		///User not found exception test
		try {
			testedUser = userDAO.getUser(userID);
			assertNull("somehow user was not deleted!",testedUser);
		}catch(UserNotFoundException unfe){
			logger.info("Test completed, user was successfully CRUD!");
		}
	}
}



