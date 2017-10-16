package com.ysh.gc.file;

public class MapperConstants {
	
	public static final String MAPPER = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
			+ "\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">"
			+ "\n<mapper namespace=\"#{namespace}\">"
			
			+ "\n\t<resultMap id=\"baseResultMap\" type=\"#{type}\" >"
			+ "\n\t\t<id property=\"id\" column=\"id\"/>"
			+ "#{result}"
    		+ "\n\t</resultMap>\n"
			
    		+ "\n\t<sql id=\"baseColumn\">#{baseColumn}</sql>\n"
    		
    		+ "\n\t<sql id=\"columnExcludeId\">#{columnExcludeId}</sql>\n"
    		
    		+ "#{statement}"
			
			+ "\n</mapper>";
	
	public static final String RESULT = "\n\t\t<result property=\"#{property}\" column=\"#{column}\"/>";
	
	public static final String INSERT = "\n\t<insert id=\"#{id}\" #{returnId}>"
			+ "\n\t\tINSERT INTO #{table}(<include refid=\"columnExcludeId\"/>)"
			+ "\n\t\tVALUES"
			+ "#{values}"
			+ "\n\t</insert>\n";
	
	public static final String RETURN_ID = "useGeneratedKeys=\"true\" keyProperty=\"id\"";
	
	public static final String INSERT_FOREACH = "\n\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">"
			+ "\n\t\t\t(#{param})"
			+ "\n\t\t</foreach>";
	
	public static final String SELECT = "\n\t<select id=\"#{id}\" #{resultType}>"
			+ "\n\t\tSELECT #{column} FROM #{table}"
			+ "\n\t\tWHERE #{where}"
			+ "\n\t</select>\n";
	
	public static final String FOREACH = "\n\t\t<foreach collection=\"#{collection}\" item=\"item\" separator=\",\" open=\"(\" close=\")\">"
			+ "\n\t\t\t#{item}"
			+ "\n\t\t</foreach>";
	
	public static final String CONDITION = "\n\t\t<if test=\"#{test} != null\">"
			+ "\n\t\t\t#{operate}"
			+ "\n\t\t</if>";
	
	public static final String UPDATE = "\n\t<update id=\"#{id}\">"
			+ "\n\t\tUPDATE #{table}"
			+ "\n\t\tSET #{set}"
			+ "\n\t\tWHERE #{where}"
			+ "\n\t</update>\n";
	
	public static final String DELETE = "\n\t<delete id=\"#{id}\">"
			+ "\n\t\tDELETE FROM #{table}"
			+ "\n\t\tWHERE #{where}"
			+ "\n\t</delete>\n";
}
