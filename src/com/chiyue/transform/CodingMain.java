package com.chiyue.transform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class CodingMain {

	public static void main(String[] args) throws FileNotFoundException {
		
		if(args == null || args.length == 0) {
			System.out.println("Please enter a directory. v_v");
			return;
		}
		
		String url = args[0];//"C:\\Users\\Administrator\\Documents\\tele\\1727mu\\www.1727mu.com";
		tranformCoding(new File(url));
		
		System.out.println("Figure out. ^_^");
	}
	
	public static void tranformCoding(File file) throws FileNotFoundException {
		if(file.isDirectory()) {
			File[] listFiles = file.listFiles();
			for (File childFile : listFiles) {
				tranformCoding(childFile);
			}
		} else {
			String url = file.getAbsolutePath();
			if(url.endsWith(".html") || url.endsWith(".htm")) {
				String content = readToString(url);
				System.out.println("ok --> "+ url);
				contentWriter(url, content);
			}
		}
	}

	public static void contentWriter(String url, String content) throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(new File(url));
		
		try {
			fos.write(content.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String readToString(String fileName) {
		String encoding = "gb2312";
		File file = new File(fileName);
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(filecontent, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
			return null;
		}
	}
}
