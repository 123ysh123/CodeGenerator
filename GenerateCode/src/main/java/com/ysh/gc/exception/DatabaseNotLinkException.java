package com.ysh.gc.exception;

public class DatabaseNotLinkException extends RuntimeException{

	private static final long serialVersionUID = -509057322796715874L;

	public DatabaseNotLinkException() {
		super();
	}

	public DatabaseNotLinkException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseNotLinkException(String message) {
		super(message);
	}

}
