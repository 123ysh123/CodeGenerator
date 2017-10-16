package com.ysh.gc.core.data;

import static com.ysh.gc.core.MethodType.toMethodType;
import static com.ysh.gc.core.SqlType.getJavaType;
import static com.ysh.gc.deal.ImportUtil.getImport;
import static com.ysh.gc.deal.StringUtils.cutHead;
import static com.ysh.gc.deal.StringUtils.cutTail;
import static com.ysh.gc.deal.StringUtils.matches;
import static com.ysh.gc.deal.Utils.containsIgnoreCase;
import static com.ysh.gc.deal.Utils.getIgnoreCase;
import static com.ysh.gc.deal.Utils.splitCamel;
import static com.ysh.gc.deal.Utils.toFieldName;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ysh.gc.core.MethodType;
import com.ysh.gc.deal.ImportUtil;
import com.ysh.gc.exception.NoSuchColumnException;
import com.ysh.gc.exception.UnSupportedMethodException;

public class MapperData extends EntityData{
	
	private String fullClssName;
	private List<String> imports;
	private List<MethodData> methodDatas;

	public String getFullClssName() {
		return fullClssName;
	}
	public void setFullClssName(String fullClssName) {
		this.fullClssName = fullClssName;
	}
	public List<String> getImports() {
		return imports;
	}
	public List<MethodData> getMethodDatas() {
		return methodDatas;
	}
	public MapperData init(List<String> methods) {
		if (methods.size() == 0) {
			
			methods.add("insert");
			methods.add("selectById id");
			
			parse(methods);
			return this;
		} 
		
		parse(methods);
		return this;
	}
	/**
	 * selectNameById name id
	 * @param methods
	 */
	private void parse(List<String> methods) {
		imports = new ArrayList<>();
		methodDatas = new ArrayList<>();
		
		for (String methodStr : methods) {
			MethodData data = new MethodData();
			String[] strs = methodStr.split(" ");
			String methodName = strs[0];
			data.setName(methodName);
			
			MethodType type = toMethodType(methodName);
			data.setType(type);
			
			data.setFullEntityName(fullClssName);
			data.setTableName(table.getName());
			
			if (methodName.equals("insert") || methodName.equals("insertReturnKey") || methodName.equals("insertBatch")
					|| methodName.equals("select") || methodName.equals("update")) {
				data.setAllColomn(true);
				
				Map<String, Column> paramItems = columns.stream()
						.collect(toMap(item -> toFieldName(item.getName()), item -> item));
				data.setParams(paramItems);
				data.setReturns(new HashMap<>());
				
				imports.add(fullClssName);
				if (methodName.equals("insertBatch") || methodName.equals("select")) {
					imports.add(getImport("List"));
				}
				methodDatas.add(data);
				continue;
			}
			
			List<String> columnNames = columns.stream().map(Column::getName).collect(toList());
			Map<String,Column> nameColumnMap = columns.stream().collect(toMap(Column::getName, item -> item));
			
			if (matches(methodName, "selectBy\\w+") || matches(methodName, "selectCountBy\\w+") || matches(methodName, "deleteBy\\w+")) {
				Map<String, Column> paramItems = new HashMap<>();
				String temp = cutHead(methodName, "By");
				List<String> inputParams = splitCamel(temp);
				
				if (inputParams.size() != strs.length - 1) {
					throw new UnSupportedMethodException("column does not math param '" + methodStr + "'");
				}
				
				for(int i=1; i<strs.length; i++) {
					String param = inputParams.get(i - 1);
					if (!containsIgnoreCase(columnNames, strs[i])) {
						throw new NoSuchColumnException("no such column '" + strs[i] + "'");
					}
					
					paramItems.put(param, getIgnoreCase(nameColumnMap, strs[i]));
					
					if (param.contains("list")) {
						imports.add(getImport("List"));
					}
					if (matches(methodName, "selectBy\\w+")) {
						imports.add(fullClssName);
						if (!methodName.equals("selectById")) {
							imports.add(getImport("List"));
						}
					}
				}
				
				if (paramItems.size() > 1) {
					imports.add(getImport("Param"));
				}
				data.setParams(paramItems);
				data.setReturns(new HashMap<>());
				methodDatas.add(data);
				continue;
			}
			
			if (matches(methodName, "select\\w+By\\w+") || matches(methodName, "update\\w+By\\w+")) {
				Map<String, Column> paramItems = new HashMap<>();
				Map<String, Column> returnItems = new HashMap<>();
				
				String temp = methodName.substring(6);
				List<String> returns = splitCamel(cutTail(temp, "By"));
				List<String> params = splitCamel(cutHead(temp, "By"));
				
				if (returns.size() + params.size() != strs.length - 1) {
					throw new UnSupportedMethodException("column does not math param in '" + methodStr + "'");
				}
				if (returns.size() > 2) {
					throw new UnSupportedMethodException("to much return in '" + methodStr + "'");
				}
				
				for (int i=0; i<returns.size(); i++) {
					if (!containsIgnoreCase(columnNames, strs[i + 1])) {
						throw new NoSuchColumnException("no such column '" + strs[i + 1] + "'");
					}
					returnItems.put(returns.get(i), getIgnoreCase(nameColumnMap, strs[i + 1]));
				}
				
				if (!matches(methodName, "select\\w+ById") && !matches(methodName, "update\\w+By\\w+")) {
					imports.add(getImport("List"));
				}
				if (returns.size() == 2 && !matches(methodName, "update\\w+By\\w+")) {
					imports.add(getImport("Map"));
				}
				
				for (int i=0; i<params.size(); i++) {
					if (!containsIgnoreCase(columnNames, strs[returns.size() + 1 + i])) {
						throw new NoSuchColumnException("no such column '" + strs[returns.size() + 1 + i] + "'");
					}
					paramItems.put(params.get(i), getIgnoreCase(nameColumnMap, strs[returns.size() + i + 1]));
					if (params.get(i).contains("list")) {
						imports.add(getImport("List"));
					}
				}
				
				if (params.size() > 1) {
					imports.add(getImport("Param"));
				}
				data.setReturns(returnItems);
				data.setParams(paramItems);
				methodDatas.add(data);
				continue;
			}
			
			throw new UnSupportedMethodException("method name '"+ methodName +"' invalid");
		}
		
		for (MethodData methodData : methodDatas) {
			List<String> types = new ArrayList<>();
			if (!methodData.isAllColomn()) {
				types = methodData.getParams().values().stream()
						.map(column -> getJavaType(column.getType())).collect(toList());
			}
				
			if (methodData.getReturns().size() == 1) {
				String returnType = methodData.getReturns().values().stream().findFirst().get().getType();
				returnType = getJavaType(returnType);
				types.add(returnType);
			}
			imports.addAll(ImportUtil.getImports(types));
		}
	}
	
}
