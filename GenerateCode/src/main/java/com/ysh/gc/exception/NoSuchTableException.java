package com.ysh.gc.exception;

public class NoSuchTableException extends RuntimeException{
	private static final long serialVersionUID = -299482355474132979L;

	public NoSuchTableException() {
		super();
	}

	public NoSuchTableException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchTableException(String message) {
		super(message);
	}

	
}
