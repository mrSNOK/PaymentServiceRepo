package com.my.payment.constraints;

import java.lang.reflect.Method;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.my.payment.constraints.FieldEquals;

public class FieldEqualsValidator implements ConstraintValidator<FieldEquals, Object>{
	private String field;
	private String equalsTo;
	private String message = FieldEquals.MESSAGE;

	public void initialize(FieldEquals constraintAnnotation) {
		this.message = constraintAnnotation.message();
		this.field = constraintAnnotation.field();
		this.equalsTo = constraintAnnotation.equalsTo();
	}

	public boolean isValid(Object value, ConstraintValidatorContext context) {
		try {
			///Getting fields values
			final Object fieldObject = getProperty(value, field, null);
			final Object equalsToObject = getProperty(value, equalsTo, null);

			if (fieldObject == null && equalsToObject == null) {
				///Empty but equals
				return true;
			}
			///Checking equality
			boolean matches = (fieldObject != null)
					&& fieldObject.equals(equalsToObject);

			if (!matches) {
				///Sanding massages if not equal
				String msg = this.message;
				if( this.message == null  
						|| "".equals( this.message ) 
						|| FieldEquals.MESSAGE.equals( this.message ) ){
					msg = field + " is not equal to " + equalsTo;
				}
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate( msg )
						.addPropertyNode(equalsTo).addConstraintViolation();
			}
			///Return equality
			return matches;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private Object getProperty(Object value, String fieldName,
			Object defaultValue) {
		Class<?> clazz = value.getClass();
		///Getting getter name with reflection
		String methodName = "get" + Character.toUpperCase(fieldName.charAt(0))
				+ fieldName.substring(1);
		try {
			///Getting getter with reflection
			Method method = clazz.getDeclaredMethod(methodName, new Class[0]);
			///Returning value that returned getter 
			return method.invoke(value);
		} catch (Exception e) {
		}
		return defaultValue;
	}
}
