package com.my.payment.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class RequestBodyUser {
	@NotNull
	///currency format 
	@Digits(integer=4, fraction=2)
	@Min(value=1)
	private Float amount;
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
}
