package com.my.payment.DAOtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
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
import com.my.payment.dao.OperationDAO;
import com.my.payment.dao.RoleDAO;
import com.my.payment.dao.UserDAO;
import com.my.payment.exceptions.NoOperationsWereFoundException;
import com.my.payment.exceptions.OperationNotFoundException;
import com.my.payment.model.Operation;
import com.my.payment.model.Role;
import com.my.payment.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DAOsConfig.class)
@Transactional
public class OperationDAOIntegrationalTest {
	@Autowired
	UserDAO userDAO;
	@Autowired
	RoleDAO roleDAO;
	@Autowired
	OperationDAO operationDAO;
	private static final double DELTA = 1e-15;
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	
	@Test
	public void userDaoTest(){
		Pageable pageable = new PageRequest(0,20);
		Operation testedOperation;
		roleDAO.setRole(new Role("ROLE_TEST_USER"));
		roleDAO.setRole(new Role("ROLE_TEST_ADMIN"));
		userDAO.setUser(new User(
				"testUser@mail.ru",
				"testPassU",
				1000f,
				roleDAO.getRole("ROLE_TEST_USER")));
		userDAO.setUser(new User(
				"testAdmin@mail.ru",
				"testPassA",
				1000f,
				roleDAO.getRole("ROLE_TEST_ADMIN")));
		///Setting start and end time of operations creation
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -1);
		Date startDate = calendar.getTime();
		calendar.add(Calendar.MINUTE, 2);
		Date endDate = calendar.getTime();
		operationDAO.setOperation(new Operation(
				userDAO.getUser("testUser@mail.ru"),
				userDAO.getUser("testAdmin@mail.ru"),
				100f));
		operationDAO.setOperation(new Operation(
				userDAO.getUser("testUser@mail.ru"),
				userDAO.getUser("testAdmin@mail.ru"),
				200f));
		///Get list of operations test
		List<Operation> operations = operationDAO.getOperations(pageable);
		assertNotNull("Somehow there are no users!",operations);
		assertThat("Somehow there are no users!",operations.size(),Matchers.greaterThan(0));
		///Get operation by id test
		int operationID = operations.get(0).getId();
		testedOperation = operationDAO.getOperation(operationID);
		assertEquals("Something is wrong wiht operation admin!", "testAdmin@mail.ru", testedOperation.getAdmin().getEmail());
		assertEquals("Something is wrong wiht operation user!", "testUser@mail.ru", testedOperation.getUser().getEmail());
		assertEquals("Something is wrong wiht operation amount!", 100f, testedOperation.getAmount(),DELTA);
		///Get operations page count operations test
		int pages = operationDAO.pageCountOperations(pageable);
		assertNotNull("Somehow there are no pages!",pages);
		assertThat("Somehow there are no pages!",pages,Matchers.greaterThan(0));
		///Get operations page count between dates test. 
		pages = operationDAO.pageCountOperationsBetweenDates(startDate, endDate, pageable);
		assertNotNull("Somehow there are no pages!",pages);
		assertThat("Somehow there are no pages!",pages,Matchers.greaterThan(0));
		///Get operations page count between dates test. Only start date
		pages = operationDAO.pageCountOperationsBetweenDates(startDate, null, pageable);
		assertNotNull("Somehow there are no pages!",pages);
		assertThat("Somehow there are no pages!",pages,Matchers.greaterThan(0));
		///Get operations page count between dates test. Only end date
		pages = operationDAO.pageCountOperationsBetweenDates(null, endDate, pageable);
		assertNotNull("Somehow there are no pages!",pages);
		assertThat("Somehow there are no pages!",pages,Matchers.greaterThan(0));
		///Get operations page count between dates test. Incorrect dates order. No operations were found exception
		try{
			pages = operationDAO.pageCountOperationsBetweenDates(endDate, startDate, pageable);
			assertThat("Somehow there are some pages!",pages,Matchers.is(0));
		}catch(NoOperationsWereFoundException nowfe){
			logger.info("expected exception has been caught!");
		}
		///Get list of operations between dates test
		operations = operationDAO.getOperationsBetweenDates(startDate, endDate, pageable);
		assertNotNull("Somehow there are no users!",operations);
		assertThat("Somehow there are no users!",operations.size(),Matchers.is(2));
		///Get list of operations between dates test. Only end date
		operations = operationDAO.getOperationsBetweenDates(startDate, null, pageable);
		assertNotNull("Somehow there are no users!",operations);
		assertThat("Somehow there are no users!",operations.size(),Matchers.is(2));
		///Get list of operations between dates test. Only start date
		operations = operationDAO.getOperationsBetweenDates(null, endDate, pageable);
		assertNotNull("Somehow there are no users!",operations);
		assertThat("Somehow there are no users!",operations.size(),Matchers.is(2));
		///Get list of operations between dates test. Incorrect dates order. No operations were found exception
		try{
			operations = operationDAO.getOperationsBetweenDates(endDate, startDate, pageable);
			assertEquals("Somehow there are some users!",operations.size(),Matchers.greaterThan(0));
		}catch(NoOperationsWereFoundException nowfe){
			logger.info("expected exception has been caught!");
		}
		///Delete operation test
		operationDAO.deleteOperation(operationID);
		///Operation not found exception test
		try {
			testedOperation = operationDAO.getOperation(operationID);
			assertNull("somehow operation was not deleted!",testedOperation);
		}catch(OperationNotFoundException unfe){
			logger.info("Test completed, opeartion was successfully CRUD!");
		}
	}
}
