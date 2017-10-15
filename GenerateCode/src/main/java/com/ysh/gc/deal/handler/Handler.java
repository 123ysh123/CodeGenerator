package com.ysh.gc.deal.handler;

import com.ysh.gc.deal.Response;

public interface Handler {
	
	Response execute(String command);
}
