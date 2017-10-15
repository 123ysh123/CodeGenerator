package com.ysh.gc.core;

import com.ysh.gc.exception.UnSupportedMethodException;

public enum MethodType {
	INSERT,
	SELECT,
	UPDATE,
	DELETE;
		
	public static MethodType toMethodType(String name) {
		if (name.startsWith("insert")) {
			return MethodType.INSERT;
		}
		if (name.startsWith("select")) {
			return MethodType.SELECT;
		}
		if (name.startsWith("update")) {
			return MethodType.UPDATE;
		}
		if (name.startsWith("delete")) {
			return MethodType.DELETE;
		}
		throw new UnSupportedMethodException("unsupported method name '" + name + "'");
	}
}
