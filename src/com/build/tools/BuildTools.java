package com.build.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.build.tools.content.BuildType;

public class BuildTools {
	
	public static void main(String[] args) throws Exception {
		{
			String projectName = null;
			String port = null;
			String author = null;
			BuildType type = new BuildType();
			try {
				projectName = args[0];
				projectName = type.firstUpperCase(projectName);
			} catch (Exception e) {
				projectName = "CheneyThinker";
			}
			try {
				port = args[1];
			} catch (Exception e) {
				port = "9527";
			}
			try {
				author = args[2];
			} catch (Exception e) {
				author = "CheneyThinker";
			}
			System.out.println("Config Info From Input:\n");
			System.out.println("ProjectName: "+ projectName);
			System.out.println("port: " + port);
			System.out.println("author: " + author);
			
			System.out.println("\nBuildTools is Running!");
			
				System.out.println("\tPreparing to build Directory!");
				
					File front = new File(projectName + "/" + projectName + "Front");
					front.mkdirs();
					
					File resources = new File(projectName + "/" + projectName + "/src/main/resources");
					resources.mkdirs();
					List<String> strings = splitByUpperCase(projectName);
					String javaPath = projectName + "/" + projectName + "/src/main/java/com/";
					for (String string : strings) {
						javaPath = javaPath + "/" + string.toLowerCase();
					}
					File project = new File(javaPath);
					project.mkdirs();
					File utils = new File(project.getPath() + "/utils");
					utils.mkdirs();
					File mapping = new File(project.getPath() + "/mapping");
					mapping.mkdirs();
					File core = new File(project.getPath() + "/core");
					core.mkdirs();
					File controller = new File(project.getPath() + "/controller");
					controller.mkdirs();
					File service = new File(project.getPath() + "/service/impl");
					service.mkdirs();
					File config = new File(project.getPath() + "/config");
					config.mkdirs();
					
				System.out.println("\tDirectory build Finished!");

				System.out.println();
				
				System.out.println("\tPreparing to write file!");
					
					write(type.getBase64JS(), front + "/jquery.base64.js");
					write(type.getRequestJS(projectName, port), front + "/request.js");
					write(type.getHtml(projectName, author), front + "/index.html");
				
					write(type.getPom(projectName), projectName + "/" + projectName + "/pom.xml");
				
					write(type.getConfig(port), resources.getPath() + "/application-default.yml");
				
					write(type.getApplication(projectName, author), project.getPath() + "/" + projectName + "Application.java");
				
					write(type.getUtils(projectName, author), utils.getPath() + "/" + projectName + "Utils.java");
				
					write(type.getService(projectName, author), service.getParentFile().getPath() + "/" + projectName + "Service.java");
					write(type.getServiceImpl(projectName, author), service.getPath() + "/" + projectName + "ServiceImpl.java");
				
					write(type.getMapping(projectName, author), mapping.getPath() + "/" + projectName + "Mapping.java");
				
					write(type.getResponse(projectName, author), core.getPath() + "/Response.java");
					write(type.getResponseCode(projectName, author), core.getPath() + "/ResponseCode.java");
					write(type.getResponseGenerator(projectName, author), core.getPath() + "/ResponseGenerator.java");
				
					write(type.getController(projectName, author), controller.getPath() + "/" + projectName + "Controller.java");
					
					write(type.getRestTemplate(projectName, author), config.getPath() + "/RestTemplateConfig.java");
					
				System.out.println("\tFiles was Finished!");
					
			System.out.println("BuildTools is Finished!");
		}
	}
	
	private static void write(String content, String filePath) throws Exception {
		FileOutputStream fos = new FileOutputStream(filePath);
		OutputStreamWriter write = new OutputStreamWriter(fos, "UTF-8");
		BufferedWriter writer = new BufferedWriter(write);
		writer.write(content);
		writer.close();  
	}
	
	private static List<String> splitByUpperCase(String str) {
		List<String> strings = new ArrayList<String>();
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
	
}
