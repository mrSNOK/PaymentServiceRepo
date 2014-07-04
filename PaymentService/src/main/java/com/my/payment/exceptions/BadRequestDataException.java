package com.my.payment.exceptions;

import org.springframework.validation.BindingResult;

public class BadRequestDataException extends RuntimeException {
	private static final long serialVersionUID = -5607890118979365484L;
	private final BindingResult result;
	public BadRequestDataException(BindingResult result){
		this.result = result;
	}
	public BindingResult getResult() {
		return result;
	}
}
