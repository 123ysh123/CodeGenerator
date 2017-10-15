package com.ysh.gc.deal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static String cutHead(String source, String content) {
		if (source.contains(content)) {
			return source.substring(source.indexOf(content) + content.length()).trim();
		}
		return source.trim();
	}
	
	public static String cutTail(String source, String content) {
		if (source.contains(content)) {
			return source.substring(0, source.indexOf(content)).trim();
		}
		return source.trim();
	} 
	
	public static boolean matches(String source, String reg) {
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(source);
		return matcher.matches();
	} 
	
}
