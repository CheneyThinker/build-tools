package com.build.tools.utils;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public final class BuildUtils {
  
  private static File backfile;
  private static File frontFile;
  private static File tempFile;
  private static File resourceFile;
  private static File pomFile;
  private static String javaPath;
  
  public static void init(String projectName) {
    projectName = firstUpperCase(projectName);
    pomFile = new File(projectName.concat("/").concat(projectName));
    pomFile.mkdirs();
    frontFile = new File(projectName.concat("/").concat(projectName.concat("Front")));
    frontFile.mkdirs();
    resourceFile = new File(pomFile.getPath().concat("/src/main/resources"));
    resourceFile.mkdirs();
    List<String> strings = splitByUpperCase(projectName);
    File file = new File(pomFile.getPath().concat("/src/main/java"));
    javaPath = file.getAbsolutePath();
    projectName = pomFile.getPath().concat("/src/main/java/com");
    for (String string : strings) {
      projectName = projectName.concat("/").concat(string.toLowerCase());
    }
    backfile = new File(projectName);
    backfile.mkdirs();
  }
  
  public static void write(String content, String fileName) throws Exception {
    FileOutputStream fos = new FileOutputStream(tempFile.getPath().concat("/").concat(fileName));
    OutputStreamWriter write = new OutputStreamWriter(fos, "UTF-8");
    BufferedWriter writer = new BufferedWriter(write);
    writer.write(content);
    writer.close();
  }
  
  public static void config() {
    tempFile = new File(backfile.getPath().concat("/config"));
    tempFile.mkdirs();
  }
  
  public static void controller() {
    tempFile = new File(backfile.getPath().concat("/controller"));
    tempFile.mkdirs();
  }
  
  public static void core() {
    tempFile = new File(backfile.getPath().concat("/core"));
    tempFile.mkdirs();
  }
  
  public static void exception() {
    tempFile = new File(backfile.getPath().concat("/exception"));
    tempFile.mkdirs();
  }
  
  public static void filter() {
    tempFile = new File(backfile.getPath().concat("/filter"));
    tempFile.mkdirs();
  }
  
  public static void service() {
    tempFile = new File(backfile.getPath().concat("/service"));
    tempFile.mkdirs();
  }
  
  public static void serviceImpl() {
    tempFile = new File(backfile.getPath().concat("/service/impl"));
    tempFile.mkdirs();
  }
  
  public static void util() {
    tempFile = new File(backfile.getPath().concat("/util"));
    tempFile.mkdirs();
  }
  
  public static void application() {
    tempFile = backfile;
  }
  
  public static File resources() {
    tempFile = resourceFile;
    return resourceFile;
  }
  
  public static void pom() {
    tempFile = pomFile;
  }
  
  public static File front() {
    tempFile = frontFile;
    return frontFile;
  }
  
  public static String getJavaPath() {
    return javaPath;
  }
  
	public static String firstLowerCase(String content) {
    return content.toLowerCase().charAt(0) + content.substring(1);
  }
	
	public static String firstUpperCase(String content) {
    return content.toUpperCase().charAt(0) + content.substring(1);
  }
  
	public static List<String> splitByUpperCase(String str) {
    List<String> strings = new LinkedList<String>();
    int index = 0, len = str.length();
    for (int i = 1; i < len; i++) {
      if (Character.isUpperCase(str.charAt(i))) {
        strings.add(str.substring(index, i));
        index = i;
      }
    }
    strings.add(str.substring(index, len));
    return strings;
  }
  
	public static String splitByUpperCaseAndAddDot(String content) {
	  List<String> strings = splitByUpperCase(content);
	  String result = "";
	  int size = strings.size() - 1;
	  for (int i = 0, j = size; i < j; i++) {
	    result = result.concat(firstLowerCase(strings.get(i))).concat(".");
    }
	  return result.concat(firstLowerCase(strings.get(size)));
	}
	
	public static void initGlobalFontSetting(Font fnt) {
    FontUIResource fontRes = new FontUIResource(fnt);
    for(Enumeration<?> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
      Object key = keys.nextElement();
      Object value = UIManager.get(key);
      if(value instanceof FontUIResource) {
        UIManager.put(key, fontRes);
      }
    }
  }
  
  public static boolean isEmpty(Object str) {
    return str == null || "".equals(str);
  }
  
  public static String splitByUpperCaseAndAddLine(String content) {
    return splitByUpperCaseAndAddDot(content).replaceAll("\\.", "-");
  }
    
  public static void start(Process process) throws Exception {
    StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");
    errorGobbler.start();
    StreamGobbler outGobbler = new StreamGobbler(process.getInputStream(), "STDOUT");
    outGobbler.start();
  }

	public static void readLocal(Class<?> clazz, byte[] buffer, int readBytes, String path, String fileName) throws Exception {
	  URL url = clazz.getResource(fileName);
	  URLConnection urlConnection = url.openConnection();
		InputStream is = urlConnection.getInputStream();
		OutputStream os = new FileOutputStream(path.concat("/").concat(fileName));
		try {
			while ((readBytes = is.read(buffer)) != -1) {
				os.write(buffer, 0, readBytes);
			}
		} finally {
			os.close();
			is.close();
		}
	}

}