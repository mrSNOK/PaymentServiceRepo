package com.my.payment.controllerTest;


import javax.servlet.http.HttpSession;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = ControllerConfig.class)
public class ControllerIntegrationalTest {
	@Autowired
    private WebApplicationContext wac;
	@Autowired
	FilterChainProxy springSecurityFilterChain;
	private MockMvc mockMvc;
	
	@Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
        		.addFilter(springSecurityFilterChain)
        		.dispatchOptions(true).build();
    }
	
	@Test
	public void unauthorizedAccessSecurityTest() {
		try {
			///Login page: allowed
			mockMvc.perform(MockMvcRequestBuilders.get("/")
					.contentType(MediaType.TEXT_HTML))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("login"))
					.andDo(MockMvcResultHandlers.print());
			///Registration page: allowed
			mockMvc.perform(MockMvcRequestBuilders.get("/register")
					.contentType(MediaType.TEXT_HTML))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("registration"))
					.andDo(MockMvcResultHandlers.print());
			///Users page: not allowed. Redirect to main
			mockMvc.perform(MockMvcRequestBuilders.get("/users")
					.contentType(MediaType.TEXT_HTML))
					.andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/"))
					.andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
					.andDo(MockMvcResultHandlers.print());
			///Operations page: not allowed. Redirect to main
			mockMvc.perform(MockMvcRequestBuilders.get("/operations")
					.contentType(MediaType.TEXT_HTML))
					.andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/"))
					.andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
					.andDo(MockMvcResultHandlers.print());
			///Balance page: not allowed. Redirect to main
			mockMvc.perform(MockMvcRequestBuilders.get("/balance")
					.contentType(MediaType.TEXT_HTML))
					.andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/"))
					.andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
					.andDo(MockMvcResultHandlers.print());
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void UserLoginTest() {
		try {
			/// Login as user and get session
			HttpSession session = mockMvc.perform(MockMvcRequestBuilders.post("/j_spring_security_check") 
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .param("j_username", "user1@gmail.com")
	                .param("j_password", "user1"))
	                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
	                .andExpect(MockMvcResultMatchers.redirectedUrl("/balance"))
					.andDo(MockMvcResultHandlers.print())
					.andReturn().getRequest().getSession();
			///User must be redirected to balance. Check username and balance to make sure that user is logged correctly
			mockMvc.perform(MockMvcRequestBuilders.get("/balance") 
					.contentType(MediaType.TEXT_HTML).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("balance"))
					.andExpect(MockMvcResultMatchers.model().attribute("username", "user1@gmail.com"))
					.andExpect(MockMvcResultMatchers.model().attribute("balance", 110f))
					.andDo(MockMvcResultHandlers.print());
			///Check access to admin resources. Users page: not allowed.
			mockMvc.perform(MockMvcRequestBuilders.get("/users")
					.contentType(MediaType.TEXT_HTML).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isForbidden())
					.andExpect(MockMvcResultMatchers.forwardedUrl("/error403"))
					.andDo(MockMvcResultHandlers.print());
			///Check access to admin resources. Operations page: not allowed.
			mockMvc.perform(MockMvcRequestBuilders.get("/operations")
					.contentType(MediaType.TEXT_HTML).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isForbidden())
					.andExpect(MockMvcResultMatchers.forwardedUrl("/error403"))
					.andDo(MockMvcResultHandlers.print());
			///Login page: Not allowed, since user is already logged
			mockMvc.perform(MockMvcRequestBuilders.get("/")
					.contentType(MediaType.TEXT_HTML).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isForbidden())
					.andExpect(MockMvcResultMatchers.forwardedUrl("/error403"))
					.andDo(MockMvcResultHandlers.print());
			///Logout.Check redirection on login page.
			mockMvc.perform(MockMvcRequestBuilders.get("/logout")
					.contentType(MediaType.TEXT_HTML).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.redirectedUrl("/"))
					.andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
					.andDo(MockMvcResultHandlers.print());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void UserRegisterTest() {
		try {
			///Get registration page
			mockMvc.perform(MockMvcRequestBuilders.get("/register") 
					.contentType(MediaType.TEXT_HTML))
	                .andExpect(MockMvcResultMatchers.status().isOk())
					.andDo(MockMvcResultHandlers.print());
			/*Checking registration. User already exists,
			password less then minimum, confirmation not equals*/
			mockMvc.perform(MockMvcRequestBuilders.post("/register") 
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .param("email", "user1@gmail.com")
	                .param("password", "123")
	                .param("confirmPassword", "1234"))
	                .andExpect(MockMvcResultMatchers.status().isOk())
	                .andExpect(MockMvcResultMatchers.model().errorCount(3))
					.andDo(MockMvcResultHandlers.print());
			///Checking registration. Empty and spaced fields
			mockMvc.perform(MockMvcRequestBuilders.post("/register") 
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .param("email", "")
	                .param("password", " ")
	                .param("confirmPassword", "  "))
	                .andExpect(MockMvcResultMatchers.status().isOk())
	                .andExpect(MockMvcResultMatchers.model().errorCount(7))
					.andDo(MockMvcResultHandlers.print());
			///Correct user registration. User must be autologged and redirected to balance page 
			HttpSession session = mockMvc.perform(MockMvcRequestBuilders.post("/register") 
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .param("email", "user2@gmail.com")
	                .param("password", "user2")
	                .param("confirmPassword", "user2"))
	                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
	                .andExpect(MockMvcResultMatchers.redirectedUrl("/balance"))
					.andDo(MockMvcResultHandlers.print()).andReturn().getRequest().getSession();
			///Check username and balance to make sure that user is logged correctly
			mockMvc.perform(MockMvcRequestBuilders.get("/balance") 
					.contentType(MediaType.TEXT_HTML).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("balance"))
					.andExpect(MockMvcResultMatchers.model().attribute("username", "user2@gmail.com"))
					.andExpect(MockMvcResultMatchers.model().attribute("balance", 0f))
					.andExpect(MockMvcResultMatchers.forwardedUrl("/WEB-INF/views/balance.jsp"))
					.andDo(MockMvcResultHandlers.print());
			///Logout.Check redirection on login page.
			mockMvc.perform(MockMvcRequestBuilders.get("/logout")
					.contentType(MediaType.TEXT_HTML).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.redirectedUrl("/"))
					.andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
					.andDo(MockMvcResultHandlers.print());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void AdminLoginTest() {
		try {
			/// Login as admin and get session. Admin must be redirected to users page
			HttpSession session = mockMvc.perform(MockMvcRequestBuilders.post("/j_spring_security_check") /// Login and get session
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .param("j_username", "admin@gmail.com")
	                .param("j_password", "admin"))
	                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
	                .andExpect(MockMvcResultMatchers.redirectedUrl("/users"))
					.andDo(MockMvcResultHandlers.print())
					.andReturn().getRequest().getSession();
			///Get users page
			mockMvc.perform(MockMvcRequestBuilders.get("/users") 
					.contentType(MediaType.TEXT_HTML).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("users"))
					.andDo(MockMvcResultHandlers.print());
			/// Check @controllerAdvice no users were found exception
			mockMvc.perform(MockMvcRequestBuilders.get("/users") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isNotFound())
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors.message", Matchers.is("There are no users")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors.URL", Matchers.is("http://localhost/users")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.i18n", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.isEmptyOrNullString()))
					.andDo(MockMvcResultHandlers.print());
			///Get users list in JSON. No errors, i18n, pages in paces
			mockMvc.perform(MockMvcRequestBuilders.get("/users") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(3)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.i18n.*", Matchers.hasSize(2)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.is(1)))
					.andDo(MockMvcResultHandlers.print());
			///Get user with ID 1 in JSON. No errors, i18n, pages in paces
			mockMvc.perform(MockMvcRequestBuilders.get("/users/1") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(1)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", Matchers.is(1)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].email", Matchers.is("user1@gmail.com")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.i18n.*", Matchers.hasSize(2)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.isEmptyOrNullString()))
					.andDo(MockMvcResultHandlers.print());
			///Delete user with ID 2.
			mockMvc.perform(MockMvcRequestBuilders.delete("/users/2") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andDo(MockMvcResultHandlers.print());
			///Trying to get deleted user with ID 2. User not found exception should be caught by @controllerAdvice
			mockMvc.perform(MockMvcRequestBuilders.delete("/users/2") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isNotFound())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors.message", Matchers.is("There is no user with id: 2")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors.URL", Matchers.is("http://localhost/users/2")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.i18n", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.isEmptyOrNullString()))
					.andDo(MockMvcResultHandlers.print());
			///Replenish balance of user with ID 1. Should be OK
			mockMvc.perform(MockMvcRequestBuilders.put("/users/1") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session)
					.content("{\"amount\":\"60\"}"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(1)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", Matchers.is(1)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].email", Matchers.is("user1@gmail.com")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.i18n.*", Matchers.hasSize(2)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.isEmptyOrNullString()))
					.andDo(MockMvcResultHandlers.print());
			///Try to replenish balance of user with ID 1. Incorrect currency format should be caught by @controllerAdvice
			mockMvc.perform(MockMvcRequestBuilders.put("/users/1") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session)
					.content("{\"amount\":\"60.123\"}"))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.
							jsonPath("$.errors.message", Matchers.is("You are trying to transact more than 9999.99 at one time, or using incorrect currency format!")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.i18n", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.isEmptyOrNullString()))
					.andDo(MockMvcResultHandlers.print());
			///Try to replenish balance of user with ID 1. More then 9999 should be caught by @controllerAdvice
			mockMvc.perform(MockMvcRequestBuilders.put("/users/1") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session)
					.content("{\"amount\":\"12345\"}"))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.
							jsonPath("$.errors.message", Matchers.is("You are trying to transact more than 9999.99 at one time, or using incorrect currency format!")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.i18n", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.isEmptyOrNullString()))
					.andDo(MockMvcResultHandlers.print());
			///Try to replenish balance of user with ID 1. No amount should be caught by @controllerAdvice
			mockMvc.perform(MockMvcRequestBuilders.put("/users/1") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session)
					.content("{\"amount\":\"\"}"))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.
							jsonPath("$.errors.message", Matchers.is("Insert amount, please!")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.i18n", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.isEmptyOrNullString()))
					.andDo(MockMvcResultHandlers.print());
			///Get existing user by username in JSON
			mockMvc.perform(MockMvcRequestBuilders.post("/users/userbyname") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session)
					.content("user1@gmail.com"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(1)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", Matchers.is(1)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].email", Matchers.is("user1@gmail.com")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.i18n.*", Matchers.hasSize(2)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.isEmptyOrNullString()))
					.andDo(MockMvcResultHandlers.print());
			///Get not existing user by username in JSON. Should be caught by @controllerAdvice
			mockMvc.perform(MockMvcRequestBuilders.post("/users/userbyname") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session)
					.content("user3@gmail.com"))
					.andExpect(MockMvcResultMatchers.status().isNotFound())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors.message", Matchers.is("There is no user with name: user3@gmail.com")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors.URL", Matchers.is("http://localhost/users/userbyname")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.i18n", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.isEmptyOrNullString()))
					.andDo(MockMvcResultHandlers.print());	
			///Getting operations page	
			mockMvc.perform(MockMvcRequestBuilders.get("/operations") 
					.contentType(MediaType.TEXT_HTML).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.view().name("operations"))
					.andDo(MockMvcResultHandlers.print());
			/// Check @controllerAdvice no operations were found exception
			mockMvc.perform(MockMvcRequestBuilders.get("/operations") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isNotFound())
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors.message", Matchers.is("There are no operations ")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors.URL", Matchers.is("http://localhost/operations")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.i18n", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.isEmptyOrNullString()))
					.andDo(MockMvcResultHandlers.print());
			///Get list of oerations in JSON
			mockMvc.perform(MockMvcRequestBuilders.get("/operations") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(2)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.i18n", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.is(1)))
					.andDo(MockMvcResultHandlers.print());
			///Get existing operation with ID 0
			mockMvc.perform(MockMvcRequestBuilders.get("/operations/0") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(1)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", Matchers.is(0)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.isEmptyOrNullString()))
					.andDo(MockMvcResultHandlers.print());
			///Get not existing operation with ID 2. Should be caught by @controllerAdvice
			mockMvc.perform(MockMvcRequestBuilders.get("/operations/2") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isNotFound())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors.message", Matchers.is("There is no operation with id: 2")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors.URL", Matchers.is("http://localhost/operations/2")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.i18n", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.isEmptyOrNullString()))
					.andDo(MockMvcResultHandlers.print());
			///Get operations between dates with correct format
			mockMvc.perform(MockMvcRequestBuilders.post("/operations/betweendates") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session)
					.content("{\"startDate\":\"20/12/2013\",\"endDate\":\"20/12/2015\"}"))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.hasSize(2)))
					.andExpect(MockMvcResultMatchers.jsonPath("$.i18n",Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.is(1)))
					.andDo(MockMvcResultHandlers.print());
			///Get operations between dates with incorrect format. Should be caught by @controllerAdvice
			mockMvc.perform(MockMvcRequestBuilders.post("/operations/betweendates") 
					.contentType(MediaType.APPLICATION_JSON).session((MockHttpSession)session)
					.content("{\"startDate\":\"2013/20/12\",\"endDate\":\"2015/20/12\"}"))
					.andExpect(MockMvcResultMatchers.status().isBadRequest())
					.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.
							jsonPath("$.errors.message", Matchers.is("use right date format :dd/MM/yyyy!")))
					.andExpect(MockMvcResultMatchers.
							jsonPath("$.errors.message", Matchers.is("use right date format :dd/MM/yyyy!")))
					.andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.i18n", Matchers.isEmptyOrNullString()))
					.andExpect(MockMvcResultMatchers.jsonPath("$.pages", Matchers.isEmptyOrNullString()))
					.andDo(MockMvcResultHandlers.print());
			///Check admin authorities. Get balance page :not allowed
			mockMvc.perform(MockMvcRequestBuilders.get("/balance")
					.contentType(MediaType.TEXT_HTML).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.status().isForbidden())
					.andExpect(MockMvcResultMatchers.forwardedUrl("/error403"))
					.andDo(MockMvcResultHandlers.print());
			///Logout
			mockMvc.perform(MockMvcRequestBuilders.get("/logout")
					.contentType(MediaType.TEXT_HTML).session((MockHttpSession)session))
					.andExpect(MockMvcResultMatchers.redirectedUrl("/"))
					.andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
					.andDo(MockMvcResultHandlers.print());
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}


