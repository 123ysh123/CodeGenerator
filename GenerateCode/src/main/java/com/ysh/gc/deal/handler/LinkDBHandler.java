package com.ysh.gc.deal.handler;

import static com.ysh.gc.deal.StringUtils.*;

import com.ysh.gc.core.LinkDB;
import com.ysh.gc.deal.Response;
import com.ysh.gc.deal.Response.Status;
/**
 *  link_database("link database ${database} [save ${alias}]")
 *  127.0.0.1:3306/test yushaohua 123456
 * @author yushaohua
 *
 */
public class LinkDBHandler implements Handler{
	
	@Override
	public Response execute(String command) {
		String temp = cutHead(command, " database ");
		temp = cutTail(temp, " save ");
		
		String[] str = temp.split(" ");
		String url,user,password;
		try {
			url = str[0];
			user = str[1];
			password = str[2];
		} catch (Exception e) {
			return new  Response(Status.ERROR_PARSE, "unable to parse command");
		}
		
		Response response = LinkDB.link(url, user, password);
		if (response.getStatus() == Status.OK && command.contains("save")) {
			String alias = cutHead(command, "save").trim();
			
			UserCustomHandler handler = new UserCustomHandler();
			handler.set(alias, url + " " + user + " " + password);
		}
		
		return response;
	}

}
