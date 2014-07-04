package com.my.payment.exceptions.processors;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.my.payment.dto.ResponseBodyData;
import com.my.payment.exceptions.UserNotFoundException;

@ControllerAdvice
public class UserExceptionProcessor {
	@Autowired
    private MessageSource messageSource;
	private static final Logger logger = LoggerFactory.getLogger(UsersExceptionProcessor.class);
     
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseBodyData userNotFound(HttpServletRequest req, UserNotFoundException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String errorMessage;
        ///Checking with which constructor exception was created for sending back the right message
        if (ex.getUserId() != null) {
        	errorMessage = messageSource.getMessage("error.no.user.id", null, locale);
        	errorMessage += ex.getUserId();
        	logger.info("No user with id :{} {}.",ex.getUserId(),"response");
        }else{
        	errorMessage = messageSource.getMessage("error.no.user.name", null, locale);
        	errorMessage += ex.getUserName();
        	logger.info("No user with name :{} {}.",ex.getUserName(),"response");
        }
        String errorURL = req.getRequestURL().toString();
        Map<String,String> errors = new HashMap<String,String>();
        errors.put("URL", errorURL);
        errors.put("message", errorMessage);
        return new ResponseBodyData(errors,null,null,null);
    }

}
