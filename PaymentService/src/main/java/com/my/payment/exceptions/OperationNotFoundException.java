package com.my.payment.exceptions;

public class OperationNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 4754804477318775627L;
	private final int operationId;
	
    public OperationNotFoundException(int id) {
    	operationId = id;
    }
    public int getOperationId() {
        return operationId;
    }
}
