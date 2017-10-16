package com.ysh.gc;

import java.util.Scanner;

import com.ysh.gc.deal.Response;
import com.ysh.gc.deal.Response.Status;
import com.ysh.gc.deal.command.BaseCommand;
import com.ysh.gc.deal.command.MethodCommand;
import com.ysh.gc.deal.handler.GenEntityHandler;
import com.ysh.gc.deal.handler.GenMapperHandler;
import com.ysh.gc.deal.handler.LinkDBHandler;
import com.ysh.gc.deal.handler.UserCustomHandler;

public class MainEntrance {

	private static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) {
		welcome();
		String command = null;
		while (!(command = scan.nextLine()).equals("exit") && command.length() > 0) {
			if (command.equals("help")) {
				printCommand();
				continue;
			}
			if (command.equals("show methods")) {
				printMethod();
				continue;
			}
			
			Response response = null;
			if (command.startsWith("link database")) {
				response = new LinkDBHandler().execute(command);
				dealResponse(response);
				continue;
			}
			if (command.startsWith("use") || command.equals("show links")) {
				response = new UserCustomHandler().execute(command);
				dealResponse(response);
				continue;
			}
			if (command.startsWith("gen entity")) {
				response = new GenEntityHandler().execute(command);
				dealResponse(response);
				continue;
			}
			if (command.startsWith("gen mapper")) {
				response = new GenMapperHandler().execute(command);
				dealResponse(response);
				continue;
			}
			System.out.println("error command!");
		}
	}

	private static void dealResponse(Response response) {
		if (response.getStatus() == Status.OK) {
			System.out.println("ok");
		} else {
			System.out.println(response.getMsg());
		}
	}
	
	private static void welcome() {
		System.out.println("*************************************************");
		System.out.println("*               ^_^欢迎使用代码生成器^_^              *");
		System.out.println("* help 查看帮助                                                                                                           *");
		System.out.println("* exit 推出系统                                                                                                           *");
		System.out.println("*************************************************");
	}
	private static void printCommand() {
		for (BaseCommand command : BaseCommand.values()) {
			System.out.println(command.getCommand());
			System.out.println("  " + command.getComment());
		}
	}
	
	private static void printMethod() {
		for (MethodCommand command : MethodCommand.values()) {
			System.out.println(command.getCommand());
			System.out.println("  " + command.getComment());
		}
	}
}
