package com.ysh.gc.deal.handler;

import static com.ysh.gc.deal.StringUtils.cutHead;
import static com.ysh.gc.deal.StringUtils.cutTail;
import static com.ysh.gc.deal.Utils.containsIgnoreCase;
import static com.ysh.gc.deal.Utils.getDesktopPath;
import static com.ysh.gc.deal.Utils.toClassName;
import static com.ysh.gc.deal.Utils.toImportPath;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ysh.gc.core.LinkDB;
import com.ysh.gc.core.data.Column;
import com.ysh.gc.core.data.EntityData;
import com.ysh.gc.deal.PropertyUtil;
import com.ysh.gc.deal.Response;
import com.ysh.gc.deal.Response.Status;
import com.ysh.gc.file.EntityModel;
import com.ysh.gc.file.Output;
import com.ysh.gc.file.OutputFile;

/**
 * gen entity <table> [as alias] [in column...] [not in column...] [to path]
 * @author yushaohua
 *
 */
public class GenEntityHandler implements Handler {
	private String command;
	
	public static final Map<String, List<String>> GENERATED_ITEMS = new HashMap<>();

	@Override
	public Response execute(String command) {
		this.command = command;
		
		String table = getTable();
		String path = getPath();
		Optional<String> aliasOpt = getAlias();
		
		try {
			EntityData data = getEntityData(table);
			OutputFile<EntityModel> entityFile = new OutputFile<>(new EntityModel(data));
			data.setPath(path);
			entityFile.setFilePath(path);
			
			if (aliasOpt.isPresent()) {
				data.setName(aliasOpt.get());
				entityFile.setFileName(toClassName(aliasOpt.get()) + ".java");
			} else {
				data.setName(data.getTable().getName());
				entityFile.setFileName(toClassName(data.getTable().getName()) + ".java");
			}
			
			Output.output(entityFile);
			saveEntity(table.toLowerCase(), toImportPath(path) + "." + entityFile.getFileName().replace(".java", ""));
			
			List<String> savedColumns = data.getColumns().stream()
					.map(column -> column.getName())
					.collect(toList());
			GENERATED_ITEMS.put(table.toLowerCase(), savedColumns);
			
		} catch (SQLException | IOException e) {
			return new Response(Status.ERROR_EXCUTE, e.getMessage());
		}
		return new Response(Status.OK);
	}
	
	private String getTable() {
		String table = cutHead(command, " entity ");
		table = cutTail(table, " as ");
		table = cutTail(table, " in ");
		table = cutTail(table, " not ");
		table = cutTail(table, " to ");
		
		return table;
	}
	
	private Optional<String> getAlias() {
		if (command.contains(" as ")) {
			String alias = cutHead(command, " as ");
			alias = cutTail(alias, " in ");
			alias = cutTail(alias, " not ");
			alias = cutTail(alias, " to ");
			
			return Optional.of(alias);
		}
		return Optional.empty();
	}
	
	private EntityData getEntityData(String table) throws SQLException {
		EntityData data =LinkDB.getEntity(table);
		
		String cutCommand = cutTail(command, " to ");
		if (cutCommand.contains(" not in ")) {
			String temp = cutHead(cutCommand, " not in ");
			temp = cutTail(temp, " to ");
			
			List<String> columns = Arrays.asList(temp.split(" "));
			List<Column> columnList = data.getColumns().stream()
					.filter(item -> !containsIgnoreCase(columns, item.getName())).collect(toList());
			data.setColumns(columnList);
			return data;
		}
		
		if (cutCommand.contains(" in ")) {
			String temp = cutHead(cutCommand, " in ");
			temp = cutTail(temp, " to ");
			
			List<String> columns = Arrays.asList(temp.split(" "));
			List<Column> columnList = data.getColumns().stream()
					.filter(item -> containsIgnoreCase(columns, item.getName())).collect(toList());
			data.setColumns(columnList);
			return data;
		}
		
		return data;
	}
	
	private String getPath() {
		if (command.contains(" to ")) {
			return cutHead(command, " to ");
		}
		return getDesktopPath();
	}
	
	private void saveEntity(String key, String value) {
		PropertyUtil property = new PropertyUtil(PropertyUtil.GEN_ENTITY);
		property.set(key, value);
	}
}
