package com.ysh.gc.deal;

public class Response {
	public static enum Status {
		OK,ERROR_PARSE,ERROR_EXCUTE
	}
	
	private Status status;
	private String msg;
	public Response(Status status, String msg) {
		this.status = status;
		this.msg = msg;
	}
	public Response(Status status) {
		super();
		this.status = status;
	}
	public Status getStatus() {
		return status;
	}
	public String getMsg() {
		return msg;
	}
}
