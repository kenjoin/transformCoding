package com.chiyue.transform;

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

public class CodingMain {

	public static String ORIGIN_DIRECTORY;
	public static String DEST_DIRECTORY;
	
	public static void main(String[] args) throws IOException {
		
		if(args == null || args.length < 2) {
			System.out.println("Please enter a origin directory and dest directory. v_v");
			return;
		}
		
		
		String url = args[0];
		ORIGIN_DIRECTORY = url;
		DEST_DIRECTORY = args[1];
		if(ORIGIN_DIRECTORY.equals(DEST_DIRECTORY)) {
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
    	if(!outputFile.getParentFile().exists()) {
    		outputFile.getParentFile().mkdirs();
    	}
    	
        String inputFileEncode = EncodingDetect.getJavaEncode(inputFileUrl);
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputFileUrl), inputFileEncode));
        BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFileUrl), "UTF-8"));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            bufferedWriter.write(line + "\r\n");
        }
        bufferedWriter.close();
        bufferedReader.close();
        String outputFileEncode = EncodingDetect.getJavaEncode(outputFileUrl);
		System.out.println(inputFileEncode + " ==> " + outputFileEncode + " --> " + inputFileUrl);
    }
	
	public static void tranformCoding(File file) throws IOException {
		if(file.isDirectory()) {
			File[] listFiles = file.listFiles();
			for (File childFile : listFiles) {
				tranformCoding(childFile);
			}
		} else {
			String url = file.getAbsolutePath();
			if(url.endsWith(".html") || url.endsWith(".htm")) {
				String outputDirectory = url.replace(ORIGIN_DIRECTORY, DEST_DIRECTORY);
				saveAsUTF8(url, outputDirectory);
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
		}finally {
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
