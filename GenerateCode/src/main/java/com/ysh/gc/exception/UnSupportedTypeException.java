package com.ysh.gc.exception;

public class UnSupportedTypeException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public UnSupportedTypeException() {
		super();
	}

	public UnSupportedTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnSupportedTypeException(String message) {
		super(message);
	}

	
}
