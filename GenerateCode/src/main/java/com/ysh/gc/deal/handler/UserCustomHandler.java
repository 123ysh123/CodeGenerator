package com.ysh.gc.deal.handler;

import java.util.Map;
import java.util.Optional;

import com.ysh.gc.core.LinkDB;
import com.ysh.gc.deal.PropertyUtil;
import com.ysh.gc.deal.Response;
import com.ysh.gc.deal.Response.Status;
/**
 *  show_link("show links"),
 *	use_database("use ${alias}"),
 *  link_database("link database ${database} [save ${alias}]")
 * @author yushaohua
 *
 */
public class UserCustomHandler implements Handler{
	
	private PropertyUtil util;
	
	public UserCustomHandler() {
		PropertyUtil propertyUtil = new PropertyUtil(PropertyUtil.DATABASE_ALIAS);
		this.util = propertyUtil;
	}

	@Override
	public Response execute(String command) {
		if ("show links".equals(command)) {
			printAlias();
			return new Response(Status.OK);
		}
		
		String database = "";
		if (command.startsWith("use")) {
			String key = command.replace("use", "").trim();
			Optional<String> optional = util.get(key);
			if (!optional.isPresent()) {
				return new Response(Status.ERROR_PARSE, "no such link alias: '" + key + "'");
			}
			
			database = optional.get();
			String[] str = database.split(" ");
			String url,user,password;
			url = str[0];
			user = str[1];
			password = str[2];
			return LinkDB.link(url, user, password);
		}
		return new Response(Status.OK);
	}
	
	public void set(String key, String value) {
		util.set(key, value);
	}

	private void printAlias() {
		Map<String, String> alias = util.getProperties();
		for (Map.Entry<String, String> entry : alias.entrySet()) {
			System.out.println(entry.getKey() + "  " + entry.getValue());
		}
	}
}
