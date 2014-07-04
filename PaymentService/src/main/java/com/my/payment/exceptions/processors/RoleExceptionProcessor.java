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
import com.my.payment.exceptions.RoleNotFoundException;

@ControllerAdvice
public class RoleExceptionProcessor {
	@Autowired
    private MessageSource messageSource;
	private static final Logger logger = LoggerFactory.getLogger(RoleExceptionProcessor.class);
     
    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseBodyData roleNotFound(HttpServletRequest req, RoleNotFoundException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String errorMessage;
      ///Checking with which constructor exception was created for sending back the right message
        if (ex.getRoleId() != null) {
        	errorMessage = messageSource.getMessage("error.no.role.id", null, locale);
        	errorMessage += ex.getRoleId();
        	logger.info("No role with id :{} {}.", ex.getRoleId(),"response");
        }else{
        	errorMessage = messageSource.getMessage("error.no.role.name", null, locale);
        	errorMessage += ex.getRoleName();
        	logger.info("No role with name :{} {}.", ex.getRoleName(),"response");
        }
        String errorURL = req.getRequestURL().toString();
        Map<String,String> errors = new HashMap<String,String>();
        errors.put("URL", errorURL);
        errors.put("message", errorMessage);
        return new ResponseBodyData(errors,null,null,null);
    }

}
