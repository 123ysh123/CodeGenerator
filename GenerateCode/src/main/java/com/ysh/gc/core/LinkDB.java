package com.ysh.gc.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.ysh.gc.core.data.Column;
import com.ysh.gc.core.data.EntityData;
import com.ysh.gc.core.data.Table;
import com.ysh.gc.deal.Response;
import com.ysh.gc.deal.Response.Status;
import com.ysh.gc.exception.DatabaseNotLinkException;
import com.ysh.gc.exception.NoSuchTableException;

public class LinkDB {
	
//	private final static String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF8";
//    private final static String user = "yushaohua"; 
//    private final static String password = "ysh123";
    
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String protocol = "jdbc:mysql://" ;
	private static final String encoding = "utf-8";
    private static DatabaseMetaData metadata= null;
    private static Connection conn = null;
    
	public static Response link(String url, String user, String password) {
    	try {
    		Class.forName(driver);
    		
    		Properties props =new Properties();
    		props.setProperty("user", user);
    		props.setProperty("password", password);
    		props.setProperty("remarks", "true");
    		props.setProperty("characterEncoding", encoding);
    		props.setProperty("useInformationSchema", "true");
    		props.setProperty("useSSL", "true");

    		conn = DriverManager.getConnection(protocol + url, props);
			metadata = conn.getMetaData();
		} catch (ClassNotFoundException | SQLException e) {
			return new Response(Status.ERROR_EXCUTE, e.getMessage());
		}
    	return new Response(Status.OK);
    }
    
    static public List<Table> getAllTable() throws SQLException {
    	if (metadata == null) {
			throw new DatabaseNotLinkException("database is not linked");
		}
    	ResultSet resultSet = metadata.getTables(conn.getCatalog(), null, null, new String[]{"TABLE"});
    	List<Table> tables = new ArrayList<>();
    	while(resultSet.next()) {
    		Table table = new Table();
    		table.setComment(resultSet.getString("REMARKS"));
    		table.setName(resultSet.getString("TABLE_NAME"));
    		tables.add(table);
    		
    	}
    	return tables;
    }
    
    static public EntityData getEntity(String tableName) throws SQLException {
    	if (metadata == null) {
    		throw new DatabaseNotLinkException("database is not linked");
		}
    	ResultSet tableSet = metadata.getTables(conn.getCatalog(), null, tableName, new String[]{"TABLE"});
    	Table table = null;
    	while(tableSet.next()) {
    		table = new Table();
    		table.setComment(tableSet.getString("REMARKS"));
    		table.setName(tableSet.getString("TABLE_NAME"));
    	}
    	
    	if (table == null) {
			throw new NoSuchTableException("no such table '" + tableName + "' exist");
		}
    	
    	ResultSet columnSet = metadata.getColumns(conn.getCatalog(), null, tableName, null);
    	List<Column> columns = new ArrayList<>();
    	while (columnSet.next()) {
    		Column column = new Column();
    		column.setComment(columnSet.getString("REMARKS"));
    		column.setName(columnSet.getString("COLUMN_NAME"));
			column.setType(columnSet.getString("TYPE_NAME"));
    		columns.add(column);
		}
    	
    	EntityData entityData = new EntityData();
    	entityData.setTable(table);
    	entityData.setColumns(columns);
    	return entityData;
    }
    
    
}
