package com.ysh.gc.deal;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

public class PropertyUtil {
	public static final String DATABASE_ALIAS = "database-alias.properties";
	public static final String GEN_ENTITY = "gen-entity.properties";
	public static final String IMPORT = "import.properties";
	
	private Properties properties = null;
	private String file;
	
	public PropertyUtil(String propertyFile) {
		this.file = propertyFile;
		Properties pro = new Properties();
		InputStream in = PropertyUtil.class.getClassLoader().getResourceAsStream(propertyFile);
		try {
			pro.load(in);
			in.close();
		} catch (IOException e) {
			System.out.println("error:" + e.getMessage());
		}
		this.properties = pro;
	}
	
	public Map<String, String> getProperties() {
		return properties.stringPropertyNames().stream()
				.collect(Collectors.toMap(item -> item, item -> properties.getProperty(item)));
	}
	
	public Optional<String> get(String key) {
		String value = properties.getProperty(key);
		if (value != null) {
			return Optional.of(value);
		}
		return Optional.empty();
	}
	
	public void set(String key, String value) {
		OutputStream outputStream;
		try {
			String classpathFile = PropertyUtil.class.getClassLoader().getResource(file).getPath();
			String sourceFile = classpathFile.replace("target/classes", "src\\main\\resources");
			outputStream = new FileOutputStream(sourceFile);
			properties.setProperty(key, value);
			properties.store(outputStream, Utils.getCurrentDate());
		} catch (IOException e) {
			System.out.println("error:" + e.getMessage());
		}
	}
	
}
