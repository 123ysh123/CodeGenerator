package com.ysh.gc.exception;

public class EntityNotGeneratedException extends RuntimeException{

	private static final long serialVersionUID = 4912459963888564500L;

	public EntityNotGeneratedException() {
		super();
	}

	public EntityNotGeneratedException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNotGeneratedException(String message) {
		super(message);
	}
	

}
