package com.my.payment.exceptions.processors;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.my.payment.dto.ResponseBodyData;
import com.my.payment.exceptions.BadRequestDataException;

@ControllerAdvice
public class BadRequestDataExceptionProcessor {
	@Autowired
    private MessageSource messageSource;
	private static final Logger logger = LoggerFactory.getLogger(BadRequestDataExceptionProcessor.class);
     
    @ExceptionHandler(BadRequestDataException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseBodyData noUsersWereFound(BadRequestDataException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        logger.info("Bad request data response!");
        BindingResult result = ex.getResult();
        Map<String,String> errors = new HashMap<String,String>();
        for (FieldError error : result.getFieldErrors()) {
			errors.put("message", messageSource.getMessage(error,locale)) ;
		}
        return new ResponseBodyData(errors,null,null,null);
    }
}
