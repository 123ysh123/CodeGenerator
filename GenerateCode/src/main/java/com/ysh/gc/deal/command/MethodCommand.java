package com.ysh.gc.deal.command;

public enum MethodCommand {
	INSERT("insert","example:insert"),
	INSER_RETURN_KEY("insertReturnKey","example:inserReturnKey"),
	INSERT_BATCH("insertBatch","example:insertBatch"),
	
	SELECT("select","example:select"),
	SELECT_BY_COLUMN("selectBy<?..>","example:selectByName name,selectById id"),
	SELECT_COLUMN_BY_COLUMN("select<?..>By<?..>","example:selectNameById name id,selectNamesByIdlist name id"),
	SELECT_COUNT_BY_COLUMN("selectCountBy<?..>","example:selectCountByName name"),
	
	UPDATE("update","example:update"),
	UPDATE_COLUMN_BY_COLUMN("update<?..>By<?..>","example:updateNameById name id"),
	
	DELETE_BY_COLUMN("deleteBy<?..>","exampl:deleteById id");
	
	private String command;
	private String comment;
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	private MethodCommand(String command, String comment) {
		this.command = command;
		this.comment = comment;
	}
	
}
