package com.ysh.gc.file;

public class ClassConstants {
	public static final String clazz = 
			  "package #{package};\n"
			+ "#{imports}"
			+ "\n/**"
			+ "\n * #{comment}" 
			+ "\n * @since #{date}"
			+ "\n * @author #{user}"
			+ "\n */"
			+ "\npublic #{type} #{class} {\n"
			+ "#{field}"
			+ "#{method}"
			+ "\n}";
	
	public static final String field = 
			  "\n\t/**"
			+ "\n\t* #{comment}" 
			+ "\n\t */"
			+ "\n\tprivate #{type} #{name};\n";
	
	public static final String method =
			  "\n\t/**"
			+ "\n\t * #{comment}" 
			+ "\n\t * @since #{date}"
			+ "\n\t * @author #{user}"
			+ "\n\t */"
			+ "\n\t#{return} #{name}(#{params});\n";
	
	public static final String getter = 
			  "\n\tpublic #{type} #{methodName}() {"
			+ "\n\t\treturn this.#{name};"
			+ "\n\t}\n";
	
	public static final String setter = 
			  "\n\tpublic void #{methodName}(#{type} #{name}) {"
			+ "\n\t\tthis.#{name} = #{name};"
			+ "\n\t}\n";
}
