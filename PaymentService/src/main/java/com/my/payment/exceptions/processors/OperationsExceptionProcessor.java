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
import com.my.payment.exceptions.NoOperationsWereFoundException;

@ControllerAdvice
public class OperationsExceptionProcessor {
	@Autowired
    private MessageSource messageSource;
	private static final Logger logger = LoggerFactory.getLogger(OperationsExceptionProcessor.class);
     
    @ExceptionHandler(NoOperationsWereFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseBodyData noOperationsWereFound(HttpServletRequest req, NoOperationsWereFoundException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String errorMessage = messageSource.getMessage("error.no.operations", null, locale);
        logger.info("No operations were found response!");
        String errorURL = req.getRequestURL().toString();
        Map<String,String> errors = new HashMap<String,String>();
        errors.put("URL", errorURL);
        errors.put("message", errorMessage);
        return new ResponseBodyData(errors,null,null,null);
    }

}
