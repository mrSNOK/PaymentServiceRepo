package com.my.payment.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.Pattern;

public class RequestBodyOperations {
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	///Regular expression for date format
	private final static String REGEXP = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
	@Pattern(regexp = REGEXP)
	private String startDate;
	@Pattern(regexp = REGEXP)
	private String endDate;
	///We setting date as string, but getting as date
	public Date getStartDate() {
		if(startDate != null){
			try{
				return formatter.parse(startDate);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}else return null;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	///We setting date as string, but getting as date
	public Date getEndDate() {
		if(endDate != null){
			try{
				return formatter.parse(endDate);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else return null;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
