package com.ysh.gc.deal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ysh.gc.exception.UnSupportedTypeException;

public class ImportUtil {
	
	private static PropertyUtil property;
	
	static{
		property = new PropertyUtil(PropertyUtil.IMPORT);
	}
	
	public static List<String> getImports(List<String> name) {
		Map<String, String> map = property.getProperties();
		
		List<String> imports = new ArrayList<>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (name.contains(entry.getKey())) {
				imports.add(entry.getValue());
			}
		}
		return imports;
	} 
	
	public static String getImport(String className) {
		return property.get(className).orElseThrow(() -> new UnSupportedTypeException("unsupported type '" + className + "'"));
	}
	
}
