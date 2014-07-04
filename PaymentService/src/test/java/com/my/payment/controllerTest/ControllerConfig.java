package com.my.payment.controllerTest;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.any;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.my.payment.exceptions.NoOperationsWereFoundException;
import com.my.payment.exceptions.NoUsersWereFoundException;
import com.my.payment.exceptions.OperationNotFoundException;
import com.my.payment.exceptions.UserNotFoundException;
import com.my.payment.model.Operation;
import com.my.payment.model.Role;
import com.my.payment.model.User;
import com.my.payment.services.OperationService;
import com.my.payment.services.PaymentAuthenticationSuccessHandlerService;
import com.my.payment.services.UserAutoLoginService;
import com.my.payment.services.UserAutoLoginServiceImpl;
import com.my.payment.services.UserService;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.my.payment.controllers","com.my.payment.exceptions"})
@ImportResource("classpath:security.xml")
public class ControllerConfig extends WebMvcConfigurerAdapter {
	
	private static final Timestamp REG_STAMP = new Timestamp(new Date().getTime());
	private static final Role ROLE_ADMIN = new Role(0,"ROLE_ADMIN");
	private static final Role ROLE_USER = new Role(1,"ROLE_USER");
	private static final User ADMIN = new User(0,REG_STAMP,"admin@gmail.com","admin",0f,ROLE_ADMIN);
	private static final User USER1 = new User(1,REG_STAMP,"user1@gmail.com","user1",110f,ROLE_USER);
	private static final User USER2 = new User(2,REG_STAMP,"user2@gmail.com","user2",0f,ROLE_USER);
	private static final Operation FIRST_OPERATION = new Operation(0,REG_STAMP,USER1,ADMIN,50f);
	private static final Operation SECOND_OPERATION = new Operation(1,REG_STAMP,USER1,ADMIN,60f);
	///Language interceptor for i18n
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }
    ///Pageable resolver for pagination
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
    	argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
    }
    ///Message source for i18n
	@Bean(name = "messageSource")
	public MessageSource messageSource() {
		 ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		 messageSource.setBasenames("classpath:messages");
		 messageSource.setDefaultEncoding("UTF-8");
		 return messageSource;
	}
	///Locale resolver for i18n
	@Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(StringUtils.parseLocaleString("en"));
        return cookieLocaleResolver;
    }
	///View resolver
	@Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
	///Mock user details service
	@Bean(name = "paymentUserDetailsService")
	public UserDetailsService userDetailsService(){
		UserDetailsService userDetailsService = Mockito.mock(UserDetailsService.class);
		List<GrantedAuthority> adminAuthList = new ArrayList<GrantedAuthority>();
		adminAuthList.add(new SimpleGrantedAuthority(ADMIN.getRole().getRole()));
		List<GrantedAuthority> user1AuthList = new ArrayList<GrantedAuthority>();
		user1AuthList.add(new SimpleGrantedAuthority(USER1.getRole().getRole()));
		List<GrantedAuthority> user2AuthList = new ArrayList<GrantedAuthority>();
		user2AuthList.add(new SimpleGrantedAuthority(USER2.getRole().getRole()));
		org.springframework.security.core.userdetails.User mockAdmin = new org.springframework.security.core.userdetails.User(
				ADMIN.getEmail(), 
				ADMIN.getPassword(), 
				true,true,true,true,
				adminAuthList);
		org.springframework.security.core.userdetails.User mockUser1 = new org.springframework.security.core.userdetails.User(
				USER1.getEmail(), 
				USER1.getPassword(), 
				true,true,true,true,
				user1AuthList);
		org.springframework.security.core.userdetails.User mockUser2 = new org.springframework.security.core.userdetails.User(
				USER2.getEmail(), 
				USER2.getPassword(), 
				true,true,true,true,
				user2AuthList);
		when(userDetailsService.loadUserByUsername(ADMIN.getEmail()))
				.thenReturn(mockAdmin);
		when(userDetailsService.loadUserByUsername(USER1.getEmail()))
				.thenReturn(mockUser1);
		when(userDetailsService.loadUserByUsername(USER2.getEmail()))
				.thenReturn(mockUser2);
		return userDetailsService;
	}
	///Mock user service
	@Bean(name = "userService")
	public UserService userService(){
		UserService userService = Mockito.mock(UserService.class);
		List<User> users = new ArrayList<User>();
		users.add(ADMIN);
		users.add(USER1);
		users.add(USER2);
		Pageable pageable = new PageRequest(0,20);
		when(userService.getUsers(pageable))
				.thenThrow(new NoUsersWereFoundException())
				.thenReturn(users);
		when(userService.getUser(0))
				.thenReturn(ADMIN);
		when(userService.getUser(1))
				.thenReturn(USER1);
		when(userService.getUser(2))
				.thenReturn(USER2);
		doNothing().doThrow(new UserNotFoundException(2))
				.when(userService).deleteUser(2);
		doNothing().when(userService)
				.balanceReplenishment(USER1.getId(), ADMIN.getId(), 60f);;
		when(userService.getUser(3))
				.thenThrow(new UserNotFoundException(3));
		when(userService.getUser("user3@gmail.com"))
				.thenThrow(new UserNotFoundException("user3@gmail.com"));
		when(userService.getUser(ADMIN.getEmail()))
				.thenReturn(ADMIN);
		when(userService.getUser(USER1.getEmail()))
				.thenReturn(USER1);
		when(userService.getUser(USER2.getEmail()))
				.thenThrow(new UserNotFoundException(USER2.getEmail()))
				.thenReturn(USER2);
		when(userService.pageCountUsers(pageable))
				.thenReturn(1);
		return userService;
	}
	///Mock operations service
	@Bean(name = "operationService")
	public OperationService operationService(){
		OperationService operationService = Mockito.mock(OperationService.class);
		List<Operation> operations = new ArrayList<Operation>();
		operations.add(FIRST_OPERATION);
		operations.add(SECOND_OPERATION);
		Pageable pageable = new PageRequest(0,20);
		when(operationService.getOperations(pageable))
				.thenThrow(new NoOperationsWereFoundException())
				.thenReturn(operations);
		when(operationService.getOperationsBetweenDates(any(Date.class), any(Date.class), any(Pageable.class)))
				.thenReturn(operations);
		when(operationService.pageCountOperationsBetweenDates(any(Date.class), any(Date.class), any(Pageable.class)))
				.thenReturn(1);
		when(operationService.getOperation(0))
			.thenReturn(FIRST_OPERATION);
		when(operationService.getOperation(1))
			.thenReturn(SECOND_OPERATION);
		when(operationService.getOperation(2))
			.thenThrow(new OperationNotFoundException(2));
		
		when(operationService.pageCountOperations(pageable))
			.thenReturn(1);
		return operationService;
	}
	///user Auto Login Service
	@Bean(name = "userAutoLoginService")
	public UserAutoLoginService userAutoLoginService(){
		return new UserAutoLoginServiceImpl();
	}
	///authentication Success Handler
	@Bean(name = "authenticationSuccessHandler")
	public AuthenticationSuccessHandler authenticationSuccessHandler(){
		return new PaymentAuthenticationSuccessHandlerService();
	}
}
