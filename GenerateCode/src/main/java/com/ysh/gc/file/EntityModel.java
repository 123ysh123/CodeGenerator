package com.ysh.gc.file;

import static com.ysh.gc.core.SqlType.getJavaType;
import static com.ysh.gc.deal.ImportUtil.getImports;
import static com.ysh.gc.deal.Utils.getCurrentDate;
import static com.ysh.gc.deal.Utils.getUser;
import static com.ysh.gc.deal.Utils.toClassName;
import static com.ysh.gc.deal.Utils.toFieldName;
import static com.ysh.gc.deal.Utils.toImportPath;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.ysh.gc.core.data.EntityData;

public class EntityModel implements Model{

	private EntityData data;
	
	public EntityModel(EntityData data) {
		this.data = data;
	}
	
	@Override
	public String parse() {
		String package_ = toImportPath(data.getPath());
		
		List<String> typeNames = data.getColumns().stream()
				.map(item -> getJavaType(item.getType()))
				.collect(toList());
		List<String> importList = getImports(typeNames);
		String imports = "";
		if (importList.size() > 0) {
			imports = importList.stream().distinct().map(item -> "import " + item + ";").collect(joining("\n", "\n", "\n"));
		}
		
		String fields = data.getColumns().stream().map(item -> ClassConstants.field.replace("#{comment}", item.getComment())
				.replace("#{type}", getJavaType(item.getType()))
				.replace("#{name}", toFieldName(item.getName()))).collect(joining());
		
		String methods = data.getColumns().stream().map(item -> ClassConstants.getter.replace("#{type}", getJavaType(item.getType()))
				.replace("#{methodName}", "get" + toClassName(item.getName()))
				.replace("#{name}", toFieldName(item.getName()))
				+
				ClassConstants.setter.replace("#{methodName}", "set" + toClassName(item.getName()))
				.replace("#{type}", getJavaType(item.getType()))
				.replaceAll("(?s)\\#\\{name\\}", toFieldName(item.getName()))).collect(joining());
		
		String clazz = ClassConstants.clazz.replace("#{package}", package_)
				.replace("#{imports}", imports)
				.replace("#{comment}", data.getTable().getComment())
				.replace("#{date}", getCurrentDate())
				.replace("#{user}", getUser())
				.replace("#{type}", "class")
				.replace("#{class}",toClassName(data.getName()))
				.replace("#{field}", fields)
				.replace("#{method}", methods);
		return clazz;
	}

}
