package com.ysh.gc.deal;

import static com.ysh.gc.deal.StringUtils.cutHead;
import static com.ysh.gc.deal.StringUtils.cutTail;
import static com.ysh.gc.deal.StringUtils.matches;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.filechooser.FileSystemView;

import com.ysh.gc.exception.UnSupportedMethodException;

public class Utils {
	public static String getDesktopPath() {
		FileSystemView fsv = FileSystemView.getFileSystemView();
		return fsv.getHomeDirectory().getPath(); 
	}
	
	public static String getUser() {
		return System.getenv().get("USERNAME");
	}
	
	public static String toImportPath(String path) {
		path = cutHead(path, "src\\main\\java\\");
		path = path.replaceAll("\\\\", "\\.");
		if (path.endsWith(".")) {
			return path.substring(0, path.length() - 1);
		}
		return path;
	}
	
	public static String toClassName(String source) {
		source = toCamel(source);
		return source.substring(0, 1).toUpperCase() + source.substring(1);
	}
	
	public static String toFieldName(String source) {
		source = toCamel(source);
		return source.substring(0, 1).toLowerCase() + source.substring(1);
	}
	
	public static String toShortClassName(String fullClassName) {
		return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
	}
	public static String getCurrentDate() {
		LocalDate date = LocalDate.now();
		return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
	}
	
	private static String toCamel(String source) {
		if (source.contains("_")) {
			String[] str = source.split("_");
			source = Arrays.asList(str).stream()
					.map(item -> item.substring(0, 1).toUpperCase() + item.substring(1)).collect(Collectors.joining());
		}
		if (source.contains("-")) {
			String[] str = source.split("-");
			source = Arrays.asList(str).stream()
					.map(item -> item.substring(0, 1).toUpperCase() + item.substring(1)).collect(Collectors.joining());
		}
		return source;
	}
	
	public static List<String> splitCamel(String source) {
		char[] chars = source.toCharArray();
		List<String> list = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		
		for (Character character : chars) {
			if (Character.isUpperCase(character)) {
				if (builder.toString().length() != 0) {
					list.add(builder.toString());
				}
				builder = new StringBuilder(character.toString().toLowerCase());
			} else {
				builder.append(character);
			}
		}
		list.add(builder.toString());
		
		return list;
	}
	
	
}
