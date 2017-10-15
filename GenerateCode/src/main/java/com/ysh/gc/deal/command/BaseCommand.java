package com.ysh.gc.deal.command;

public enum BaseCommand {
	LINK_DATABSE("link","link database <address> <user> <password> [save alias]",
			"连接数据库[按此别名保存此连接]\n example: link database 127.0.0.1:3306/tset yushaohua 123456 save as test"),
	USE_DATABASE("use","use <alias>","使用此连接"),
	SHOW_LINKS("show links","show links","展示所有保存的连接"),
	
	GEN_ENTITY("gen entity","gen entity <table> [as alias] [in column...] [not in column...] [to path]",
			"根据表生成实体(建议把path指定到项目中)\n example: gen entity student in id name score to S:/sts-workspace/GenerateCode/src/main/java/com/ysh/gc/core"),
	GEN_MAPPER("gen mapper","gen mapper <table> [as alias] [in method...] [to path]",
			"根据表生成mapper(建议把path指定到项目中)\n example: gen mapper student in selectNameById name id,selectScoreById score id to S:/sts-workspace/GenerateCode/src/main/java/com/ysh/gc/core"),
	
	SHOW_METHODS("show methods","show methods","展示mapper支持的方法"),
	
	HELP("help","help","查看命令"),
	EXIT("exit","exit","退出系统");
	
	private String mark;
	private String command;
	private String comment;
	
	public String getMark() {
		return mark;
	}
	public String getCommand() {
		return command;
	}
	public String getComment() {
		return comment;
	}
	private BaseCommand(String mark, String command, String comment) {
		this.mark = mark;
		this.command = command;
		this.comment = comment;
	}
}
