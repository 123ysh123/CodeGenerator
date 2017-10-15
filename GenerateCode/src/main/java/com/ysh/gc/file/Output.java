package com.ysh.gc.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.ysh.gc.core.data.EntityData;

public class Output {
	
	public static <T extends Model> void output(OutputFile<T> output) throws IOException {
		String path = output.getFilePath() + "/" + output.getFileName();
		File file = new File(path);
		
		if (file.exists()) {
			file.delete();
			file.createNewFile();
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		String content = output.getData().parse();
		writer.write(content);
		
		writer.flush();
		writer.close();
	}
	
	public static <T extends Model> void append(OutputFile<T> output, String end) throws IOException {
		String path = output.getFilePath() + "/" + output.getFileName();
		File file = new File(path);
		
		RandomAccessFile accessFile = null;
		byte[] bytes = output.getData().parse().getBytes("utf-8");
		if (file.exists()) {
			accessFile = new RandomAccessFile(file, "rw");
			long filelength = accessFile.length();
			long length = end.getBytes().length;
			accessFile.seek(filelength - length);
			accessFile.write(bytes);
			accessFile.write(end.getBytes());
		} else {
			accessFile = new RandomAccessFile(file, "rw");
			accessFile.write(bytes);
		}
		
		accessFile.close();
	}
	
}
