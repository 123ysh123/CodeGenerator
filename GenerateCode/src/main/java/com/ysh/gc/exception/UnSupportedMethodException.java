package com.ysh.gc.exception;

public class UnSupportedMethodException extends RuntimeException{
	private static final long serialVersionUID = -3019342883958922659L;

	public UnSupportedMethodException() {
		super();
	}

	public UnSupportedMethodException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnSupportedMethodException(String message) {
		super(message);
	}
	
}
