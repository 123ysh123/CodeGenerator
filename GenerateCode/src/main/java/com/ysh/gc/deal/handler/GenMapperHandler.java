package com.ysh.gc.deal.handler;

import static com.ysh.gc.deal.StringUtils.cutHead;
import static com.ysh.gc.deal.StringUtils.cutTail;
import static com.ysh.gc.deal.Utils.getDesktopPath;
import static com.ysh.gc.deal.Utils.toClassName;
import static com.ysh.gc.deal.Utils.containsIgnoreCase;
import static com.ysh.gc.deal.handler.GenEntityHandler.GENERATED_ITEMS;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ysh.gc.core.LinkDB;
import com.ysh.gc.core.data.Column;
import com.ysh.gc.core.data.EntityData;
import com.ysh.gc.core.data.MapperData;
import com.ysh.gc.deal.PropertyUtil;
import com.ysh.gc.deal.Response;
import com.ysh.gc.deal.Response.Status;
import com.ysh.gc.exception.EntityNotGeneratedException;
import com.ysh.gc.file.MapperModel;
import com.ysh.gc.file.MapperXmlModel;
import com.ysh.gc.file.Output;
import com.ysh.gc.file.OutputFile;
/**
 * gen mapper <table> [as alias] [in method...] [to path]
 * @author yushaohua
 *
 */
public class GenMapperHandler implements Handler {
	private String command;

	@Override
	public Response execute(String command) {
		this.command = command;
		String tableName = getTable();
		try {
			EntityData entityData = LinkDB.getEntity(tableName);
			String fullEntityName = getSavedEntity(tableName);
			Optional<String> aliasOpt = getAlias();
			List<String> methodStrs = getMethods();
			
			List<String> savedItems = GENERATED_ITEMS.get(tableName.toLowerCase());
			if (savedItems != null) {
				List<Column> columns = entityData.getColumns().stream()
						.filter(column -> containsIgnoreCase(savedItems, column.getName()))
						.collect(toList());
				entityData.setColumns(columns);
			}
			
			MapperData mapperData = new MapperData();
			mapperData.setTable(entityData.getTable());
			mapperData.setColumns(entityData.getColumns());
			mapperData.setFullClssName(fullEntityName);
			mapperData.setPath(getPath());
			mapperData.init(methodStrs);
			
			OutputFile<MapperModel> mapperFile = new OutputFile<MapperModel>(new MapperModel(mapperData));
			OutputFile<MapperXmlModel> mapperXmlFile = new OutputFile<MapperXmlModel>(new MapperXmlModel(mapperData));
			mapperFile.setFilePath(getPath());
			mapperXmlFile.setFilePath(getPath());
			
			if (aliasOpt.isPresent()) {
				mapperData.setName(toClassName(aliasOpt.get()));
				mapperFile.setFileName(toClassName(aliasOpt.get()) + ".java");
				mapperXmlFile.setFileName(toClassName(aliasOpt.get()) + ".xml");
			}else {
				mapperData.setName(toClassName(entityData.getTable().getName()) + "Mapper");
				mapperFile.setFileName(toClassName(entityData.getTable().getName()) + "Mapper.java");
				mapperXmlFile.setFileName(toClassName(entityData.getTable().getName()) + "Mapper.xml");
			}
			
			Output.append(mapperFile, "}");
			Output.append(mapperXmlFile, "</mapper>");
			
		} catch (Exception e) {
			return new Response(Status.ERROR_EXCUTE, e.getMessage());
		}
		return new Response(Status.OK);
	}
	
	private String getTable() {
		String table = cutHead(command, "mapper");
		table = cutTail(table, "as");
		table = cutTail(table, "in");
		table = cutTail(table, "to");
		
		return table;
	}
	
	private Optional<String> getAlias() {
		if (command.contains("as")) {
			String alias = cutHead(command, "as");
			alias = cutTail(alias, "in");
			alias = cutTail(alias, "to");
			
			return Optional.of(alias);
		}
		return Optional.empty();
	}
	
	private List<String> getMethods() {
		String cutCommand = cutTail(command, "to");
		if (cutCommand.contains("in")) {
			String methods = cutHead(cutCommand, "in");
			methods = cutTail(methods, "to");
			
			String[] strs = methods.split(",");
			return Arrays.asList(strs);
		}
		return new ArrayList<>();
	}
	
	private String getPath() {
		if (command.contains("to")) {
			return cutHead(command, "to");
		}
		return getDesktopPath();
	}
	
	private String getSavedEntity(String table) {
		PropertyUtil property = new PropertyUtil(PropertyUtil.GEN_ENTITY);
		Optional<String> entityOpt = property.get(table.toLowerCase());
		return entityOpt.orElseThrow(() -> new EntityNotGeneratedException("entity '" + table + "' not generated"));
	}
}
