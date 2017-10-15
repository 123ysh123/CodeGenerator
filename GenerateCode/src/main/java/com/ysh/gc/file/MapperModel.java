package com.ysh.gc.file;

import static com.ysh.gc.deal.Utils.getCurrentDate;
import static com.ysh.gc.deal.Utils.getUser;
import static com.ysh.gc.deal.Utils.toImportPath;
import static com.ysh.gc.file.ClassConstants.clazz;
import static com.ysh.gc.file.ClassConstants.method;
import static java.util.stream.Collectors.joining;

import java.io.File;

import com.ysh.gc.core.MapperMethod;
import com.ysh.gc.core.data.MapperData;
import com.ysh.gc.core.data.MethodData;

public class MapperModel implements Model{
	
	private MapperData data;
	public MapperModel(MapperData data) {
		this.data = data;
	}

	public String parse() {
		String imports = data.getImports().stream()
				.map(item -> "import " + item + ";")
				.collect(joining("\n", "\n", "\n"));
		
		String filepath = data.getPath() + "\\" + data.getName() + ".java";
		File file = new File(filepath);
		if (file.exists()) {
			return getMethods();
		}
		
		String mapper = clazz.replace("#{package}", toImportPath(data.getPath()))
				.replace("#{imports}", imports)
				.replace("#{comment}", data.getTable().getComment())
				.replace("#{date}", getCurrentDate())
				.replace("#{user}", getUser())
				.replace("#{type}", "interface")
				.replace("#{class}", data.getName())
				.replace("#{field}", "")
				.replace("#{method}", getMethods());
		return mapper;
	}
	
	private String getMethods() {
		StringBuilder builder = new StringBuilder();
		for (MethodData methodData : data.getMethodDatas()) {
			MapperMethod mapperMethod = new MapperMethod(methodData); 
			
			String methodStr = method.replace("#{comment}", mapperMethod.getCommont())
					.replace("#{date}", getCurrentDate())
					.replace("#{user}", getUser())
					.replace("#{return}", mapperMethod.getReturn_())
					.replace("#{name}", mapperMethod.getName())
					.replace("#{params}", mapperMethod.getParam());
			
			builder.append(methodStr);
		}
		return builder.toString();
	}
}
