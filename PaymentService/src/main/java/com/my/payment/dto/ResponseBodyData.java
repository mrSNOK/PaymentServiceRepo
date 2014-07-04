package com.my.payment.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ResponseBodyData implements Serializable{
	private static final long serialVersionUID = -6731137496289531715L;
	private Map<String,String> errors;
	private Map<String,String> i18n;
	private List<Map<String,Object>> data;
	private Integer pages;
	
	public ResponseBodyData(){}
	
	public ResponseBodyData(Map<String,String> errors, Map<String,String> i18n, List<Map<String,Object>> data, Integer pages){
		this.errors = errors;
		this.i18n = i18n;
		this.data = data;
		this.pages = pages;
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((errors == null) ? 0 : errors.hashCode());
		result = prime * result + ((i18n == null) ? 0 : i18n.hashCode());
		result = prime * result + ((pages == null) ? 0 : pages.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResponseBodyData other = (ResponseBodyData) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (errors == null) {
			if (other.errors != null)
				return false;
		} else if (!errors.equals(other.errors))
			return false;
		if (i18n == null) {
			if (other.i18n != null)
				return false;
		} else if (!i18n.equals(other.i18n))
			return false;
		if (pages == null) {
			if (other.pages != null)
				return false;
		} else if (!pages.equals(other.pages))
			return false;
		return true;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

	public Map<String, String> getI18n() {
		return i18n;
	}

	public void setI18n(Map<String, String> i18n) {
		this.i18n = i18n;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
		
	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
