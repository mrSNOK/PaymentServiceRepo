package com.my.payment.DAOtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.my.payment.dao.RoleDAO;
import com.my.payment.model.Role;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DAOsConfig.class)
@Transactional
public class RoleDAOIntegrationalTest {
	@Autowired
	RoleDAO roleDAO;
	
	@Test
	public void userDaoTest(){
		roleDAO.setRole(new Role("ROLE_TEST_USER"));
		///Get role by name test
		Role testedRole = roleDAO.getRole("ROLE_TEST_USER");
		assertNotNull("Somehow there is no role!", testedRole);
		int roeID =  testedRole.getId();
		///Get role by ID test
		testedRole = roleDAO.getRole(roeID);
		assertNotNull("Somehow there is no role!", testedRole);
		assertEquals("Something is wrong with role!", "ROLE_TEST_USER", testedRole.getRole());
		///Get list of roles test
		List<Role> roles = roleDAO.getRoles();
		assertNotNull("Somehow there are no roles!", roles);
		assertThat("Somehow there are no roles!",roles.size(),Matchers.greaterThan(0));
		
	}
}
