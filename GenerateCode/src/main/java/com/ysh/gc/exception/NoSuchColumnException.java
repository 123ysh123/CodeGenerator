package com.ysh.gc.exception;

public class NoSuchColumnException extends RuntimeException{
	private static final long serialVersionUID = 7923552224044688192L;

	public NoSuchColumnException() {
		super();
	}

	public NoSuchColumnException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchColumnException(String message) {
		super(message);
	}

	
}
