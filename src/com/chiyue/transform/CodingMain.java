package com.chiyue.transform;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodingMain {

	public static String ORIGIN_DIRECTORY;
	public static String DEST_DIRECTORY;

	public static void main(String[] args) throws IOException {

		if (args == null || args.length < 2) {
			System.out.println("Please enter a origin directory and dest directory. v_v");
		}

//		String url = "C:\\Users\\Administrator\\Documents\\1tele\\1727mu\\www.1727mu.com";
//		DEST_DIRECTORY = "C:\\Users\\Administrator\\Documents\\1tele\\1727mu\\www.1727mu.com1";
		
		String url = args[0];
		DEST_DIRECTORY = args[1];
		ORIGIN_DIRECTORY = url;
		if (ORIGIN_DIRECTORY.equals(DEST_DIRECTORY)) {
			System.out.println("orgin directory and dest directory can't be equals.");
			return;
		}

		tranformCoding(new File(url));

		System.out.println("Figure out. ^_^");
	}

	/**
	 * 
	 * @param inputFileUrl
	 * @param outputFileUrl
	 * @throws IOException
	 */
	public static void saveAsUTF8(String inputFileUrl, String outputFileUrl) throws IOException {

		File outputFile = new File(outputFileUrl);
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}

		String inputFileEncode = EncodingDetect.getJavaEncode(inputFileUrl);
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(inputFileUrl), inputFileEncode));
		BufferedWriter bufferedWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(outputFileUrl), "UTF-8"));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			
			if(line.indexOf("</title>") > -1) {
				String keywords = "<meta name=\"keywords\" content=\"关键字\"/>";
				String description = "<meta name=\"description\" content=\"描述\"/>";
				String charset = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">";
				line += "\r\n\t" + keywords + "\r\n\t" + description + "\r\n\t" + charset;
			} else if(line.indexOf("<meta name=\"keywords\"") > -1) {
				continue;
			} else if(line.indexOf("<meta name=\"description\"") > -1) {
				continue;
			} else if (line.indexOf("charset=") > -1) {
				continue;
			} else if(line.indexOf("javascript:if(confirm(%27") > -1) {
				line = line.replaceAll("%22", "\"");
				Pattern p = Pattern.compile("href=\".*?\"");
				Matcher m = p.matcher(line); 
				
				Pattern pHref = Pattern.compile("%27http.*?%27");
				Matcher mHref = pHref.matcher(line);
				
				String replaceStr = "";
				while(mHref.find()) {
					replaceStr = mHref.group().replaceAll("%27http://.*?/", "/").replaceAll("%27", "");
				}
				replaceStr = "href=\"" + replaceStr + "\" ";
				line = m.replaceAll(replaceStr);
			}
			bufferedWriter.write(line + "\r\n");
		}
		bufferedWriter.close();
		bufferedReader.close();
		String outputFileEncode = EncodingDetect.getJavaEncode(outputFileUrl);
		System.out.println(inputFileEncode + " ==> " + outputFileEncode + " --> " + inputFileUrl);
	}

	public static void tranformCoding(File file) throws IOException {
		if (file.isDirectory()) {
			File[] listFiles = file.listFiles();
			for (File childFile : listFiles) {
				tranformCoding(childFile);
			}
		} else {
			String url = file.getAbsolutePath();
			String outputFile = url.replace(ORIGIN_DIRECTORY, DEST_DIRECTORY);
			
			String checkUrl = url.toLowerCase();//统一转为小写，防止后缀中带有大写时没有转换
			if (checkUrl.endsWith(".html") || checkUrl.endsWith(".htm") || checkUrl.endsWith(".css") || checkUrl.endsWith(".js")) {
				saveAsUTF8(url, outputFile);
			} else {
				copyFile(new File(url), new File(outputFile));
				System.out.println("copy a file: " + url + " -> " + outputFile);
			}
		}
	}

	// 复制文件
	public static void copyFile(File sourceFile, File targetFile) throws FileNotFoundException {
		
		if(!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}
		
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		try {
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				// 刷新此缓冲的输出流
				outBuff.flush();
				// 关闭流
				inBuff.close();
				outBuff.close();
				output.close();
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 复制文件夹
	public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
				copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}

	@Deprecated
	public static void contentWriter(String url, String content) throws FileNotFoundException {

		FileOutputStream fos = new FileOutputStream(new File(url));

		try {
			fos.write(content.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Deprecated
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
