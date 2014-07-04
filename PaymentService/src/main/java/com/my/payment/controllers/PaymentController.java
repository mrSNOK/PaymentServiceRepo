package com.my.payment.controllers;


import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.payment.dto.RegistrationForm;
import com.my.payment.dto.RequestBodyOperations;
import com.my.payment.dto.RequestBodyUser;
import com.my.payment.dto.ResponseBodyData;
import com.my.payment.exceptions.BadRequestDataException;
import com.my.payment.model.Operation;
import com.my.payment.model.User;
import com.my.payment.services.OperationService;
import com.my.payment.services.UserAutoLoginService;
import com.my.payment.services.UserService;



@Controller
public class PaymentController {
	@Autowired(required=true)
	private UserService userService;
	@Autowired(required=true)
	OperationService operationService;
	@Autowired(required=true)
	UserAutoLoginService userAutoLoginService;
	@Autowired(required=true)
	MessageSource messageSource;
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	///Writing required user properties to map
	private Map<String,Object> fillUserProperties(User user){
		Map<String,Object> values = new HashMap<String,Object>();
		values.put("id", user.getId());
		values.put("email", user.getEmail());
		values.put("balance",user.getBalance());
		values.put("registred",user.getRegistred().toString());
		return values;
	}
	///Writing required operation properties to map
	private Map<String,Object> fillOperationProperties(Operation operation){
		Map<String,Object> values = new HashMap<String,Object>();
		values.put("id", operation.getId());
		values.put("admin",operation.getAdmin().getEmail());
		values.put("user",operation.getUser().getEmail());
		values.put("created",operation.getCreated().toString());
		values.put("amount",operation.getAmount());
		return values;
	}
	///Getting i18n user messages for current locale 
	private Map<String,String> fillUserMessges(Locale locale){
		Map<String,String> i18n = new HashMap<String,String>();
		i18n.put("replenish", messageSource.getMessage("button.replenish",null,locale));
		i18n.put("del", messageSource.getMessage("button.delete",null,locale));
		return i18n;
	}
	
	///Get main page
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		logger.info("Requesting login page.");
		return "login";
	}
	
	///Get registration page
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register( ModelMap model) {
		logger.info("Requesting register page.");
		RegistrationForm registrationForm =  new RegistrationForm();
		model.put("registrationForm",registrationForm);
		return "registration";
	}
	
	///Posting registration data
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerMe(@Valid @ModelAttribute("registrationForm") RegistrationForm registrationForm 
			,BindingResult result ,HttpServletRequest request, Model model) {
		logger.info("Posting registration data.");
		if (result.hasErrors()) {
			logger.info("Registration data contains errors.");
			return "registration";
		}else {
			///If registration data contains no errors then register new user with role "ROLE_USER"
			userService.registerUser(registrationForm.getEmail(),registrationForm.getPassword(),0f,"ROLE_USER");
			///After user successfully registered, trying to log him in.
			if(userAutoLoginService.logIN(registrationForm.getEmail(), request))
				return "redirect:/balance";
			else
				return "redirect:/";
		}		
	}
	
	///Get balance page
	@RequestMapping(value = "/balance", method = RequestMethod.GET)
	public String balance( Model model, Principal principal) {
		logger.info("User {} {}",principal.getName(),"requested balance page");
		///Getting user by name with logged users principal name
		User loggedUser = userService.getUser(principal.getName());
		model.addAttribute("username",loggedUser.getEmail());
		model.addAttribute("balance",loggedUser.getBalance());
		return "balance";
	}
	
	///Get users page
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String users( Model model, Principal principal) {
		logger.info("Admin {} {}",principal.getName(),"requested users page");
		return "users";
	}
	
	///Get users list in JSON
	@RequestMapping(value = "/users", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE 
			, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseBodyData usersJson(Pageable pageable, Principal principal) {
		logger.info("Admin {} {}. {} {}",principal.getName(),"requested users list","page:",pageable.getPageNumber());
		List<User> users = userService.getUsers(pageable);
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		Locale locale = LocaleContextHolder.getLocale();
		Map<String,String> i18n = fillUserMessges(locale);
		for (User user : users){
			data.add(fillUserProperties(user));
		}
		Integer pages = userService.pageCountUsers(pageable);
		return new ResponseBodyData(null,i18n,data,pages);
	}
	
	///Post username and return user with such username.
	@RequestMapping(value = "users/userbyname", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE 
			, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseBodyData JsonUserByName(@RequestBody String email , Principal principal) {
		logger.info("Admin {} {} {}",principal.getName(),"requested user by name :",email);
		User user = userService.getUser(email);
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		Locale locale = LocaleContextHolder.getLocale();
		Map<String,String> i18n = fillUserMessges(locale);
		data.add(fillUserProperties(user));	
		return new ResponseBodyData(null,i18n,data,null);		
	}
	
	///Get user by ID in JSON	
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE
			, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseBodyData userJson(@PathVariable("id") Integer id, Principal principal) {
		logger.info("Admin {} {} {}",principal.getName(),"requested user with id :",id);
		User user = userService.getUser(id);
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		Locale locale = LocaleContextHolder.getLocale();
		Map<String,String> i18n = fillUserMessges(locale);
		data.add(fillUserProperties(user));
		return new ResponseBodyData(null,i18n,data,null);
	}
	
	///Delete user by ID
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE
			, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void delUser(@PathVariable("id") Integer id, Principal principal) {
		logger.info("Admin {} {} {}",principal.getName()," trying to delete user with id :", id);
		userService.deleteUser(id);
	}
	
	///Put amount of money to replenish users balance
	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE
			, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseBodyData modifyUser(@PathVariable("id") Integer id, @Valid @RequestBody RequestBodyUser requestBodyUser
			, BindingResult result, Principal principal) {
		logger.info("Admin {} {} {}",principal.getName()," trying to replanish user with id :", id);
		ResponseBodyData responseBodyData = new ResponseBodyData();
		if (result.hasErrors()) {
			logger.info("Replanish user data contains errors!");
			throw new BadRequestDataException(result);
		}else {
			///If replenish data contains no errors then get logged admins principal
			org.springframework.security.core.userdetails.User adminPrincipal = (org.springframework.security.core.userdetails.User) SecurityContextHolder
							.getContext().getAuthentication().getPrincipal();
			///Get logged admin by name
			User admin = userService.getUser(adminPrincipal.getUsername());
			///Replenish users balance, putting users ID, admins ID and amount of money
			userService.balanceReplenishment(id, admin.getId(), requestBodyUser.getAmount());
			///Get user with new balance
			ResponseBodyData userData = userJson(id,principal);
			responseBodyData.setData(userData.getData());
			responseBodyData.setI18n(userData.getI18n());
		}
		return responseBodyData;
	}
	
	///Get operations page
	@RequestMapping(value = "/operations", method = RequestMethod.GET)
	public String operations( Model model, Principal principal) {
		logger.info("Admin {}{}",principal.getName(),"requested oerations page");
		return "operations";
	}
	
	///Get operations list in JSON
	@RequestMapping(value = "/operations", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE
			, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseBodyData opertionsJson(Pageable pageable, Principal principal) {
		logger.info("Admin {} {}. {} {}",principal.getName(),"requested oerations list","page:",pageable.getPageNumber());
		List<Operation> operations = operationService.getOperations(pageable);
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		for (Operation operation : operations){
			data.add(fillOperationProperties(operation));
		}
		Integer pages = operationService.pageCountOperations(pageable);
		return new ResponseBodyData(null,null,data,pages);
	}
	
	///Post dates to get operations list between them
	@RequestMapping(value = "/operations/betweendates", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE
			, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseBodyData opertionsBetweenDatesJson(Pageable pageable, @Valid @RequestBody RequestBodyOperations requestBodyOperations
			, BindingResult result, Principal principal) {
		logger.info("Admin {} {}. {} {}",principal.getName(),"requested oerations list between dates","page:",pageable.getPageNumber());
		ResponseBodyData responseBodyData = new ResponseBodyData();
		if (result.hasErrors()) {
			logger.info("Operations between dates data conatains errors!");
			throw new BadRequestDataException(result);
		}else {
			List<Operation> operations = operationService.getOperationsBetweenDates(requestBodyOperations.getStartDate()
					, requestBodyOperations.getEndDate(), pageable);
			Integer pages = operationService.pageCountOperationsBetweenDates(requestBodyOperations.getStartDate()
					, requestBodyOperations.getEndDate(), pageable);
			List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
			for (Operation operation : operations){
				data.add(fillOperationProperties(operation));
			}
			responseBodyData.setData(data);
			responseBodyData.setPages(pages);
		}
		return responseBodyData;
	}
	
	///Get operation by ID in JSON
	@RequestMapping(value = "/operations/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE
			, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseBodyData operationJson(@PathVariable("id") Integer id, Principal principal) {
		logger.info("Admin {} {} {}",principal.getName(),"requested operation with id :",id);
		Operation operation = operationService.getOperation(id);
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		data.add(fillOperationProperties(operation));		
		return new ResponseBodyData(null,null,data,null);
	}
	
	///Get access denied page
	@RequestMapping(value = "/error403", method = RequestMethod.GET)
	public String error403( Model model) {	
		return "error403";
	}
	
}
