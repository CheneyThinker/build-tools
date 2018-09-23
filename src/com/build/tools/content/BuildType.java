package com.build.tools.content;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.build.tools.utils.BuildUtils;

public class BuildType {
	
  private String projectName;
  private String author;
  private String packageName;
  private String firstLowerCase;
  private String addSplitLine;
  private String date;
  private String jQuery;
  private String port;
  private StringBuilder builder;
  
  public BuildType(String _projectName, String _author, String _port) {
    projectName = _projectName;
    author = _author;
    packageName = BuildUtils.splitByUpperCaseAndAddDot(projectName);
    firstLowerCase = BuildUtils.firstLowerCase(projectName);
    addSplitLine = BuildUtils.splitByUpperCaseAndAddLine(projectName);
    date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    port = _port;
    builder = new StringBuilder();
  }
  
  public void setjQuery(String _jQuery) {
    jQuery = _jQuery;
  }
  
	public String getPom(String[] outerJar, String[] wsdlJars, boolean sourceOfWsdl, boolean lombok, boolean xml, String broadcast) {
    builder.delete(0, builder.length())
    .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
    .append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n")
    .append("     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n")
    .append("     xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n")
    .append("  <modelVersion>4.0.0</modelVersion>\n")
    .append("\n")
    .append("  <groupId>com.").append(packageName).append("</groupId>\n")
    .append("  <artifactId>").append(projectName).append("</artifactId>\n")
    .append("  <version>1.0.0-PRO</version>\n")
    .append("\n")
    .append("  <parent>\n")
    .append("    <groupId>org.springframework.boot</groupId>\n")
    .append("    <artifactId>spring-boot-starter-parent</artifactId>\n")
    .append("    <version>2.0.1.RELEASE</version>\n")
    .append("    <relativePath/>\n")
    .append("  </parent>\n")
    .append("\n")
    .append("  <properties>\n")
    .append("    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n")
    .append("    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>\n")
    .append("    <java.version>1.8</java.version>\n")
    .append("  </properties>\n")
    .append("\n")
    .append("  <dependencies>\n")
    .append("    <dependency>\n")
    .append("      <groupId>org.springframework.boot</groupId>\n")
    .append("      <artifactId>spring-boot-starter-web</artifactId>\n")
    .append("    </dependency>\n")
    .append("    <dependency>\n")
    .append("      <groupId>org.springframework.boot</groupId>\n")
    .append("      <artifactId>spring-boot-configuration-processor</artifactId>\n")
    .append("      <optional>true</optional>\n")
    .append("    </dependency>\n");
    if (!broadcast.equals("none")) {
        builder
        .append("    <dependency>\n")
        .append("      <groupId>org.springframework.boot</groupId>\n")
        .append("      <artifactId>spring-boot-starter-websocket</artifactId>\n")
        .append("    </dependency>\n");
  	}
    if (lombok) {
      builder
      .append("    <dependency>\n")
      .append("      <groupId>org.projectlombok</groupId>\n")
      .append("      <artifactId>lombok</artifactId>\n")
      .append("      <version>RELEASE</version>\n")
      .append("    </dependency>\n");
    }
    if (xml) {
      builder
      .append("    <dependency>\n")
      .append("      <groupId>com.fasterxml.jackson.dataformat</groupId>\n")
      .append("      <artifactId>jackson-dataformat-xml</artifactId>\n")
      .append("      <version>RELEASE</version>\n")
      .append("    </dependency>\n");
    }
    if (!BuildUtils.isEmpty(outerJar)) {
      for (String string : outerJar) {
        if (!BuildUtils.isEmpty(string)) {
          builder
          .append("    <dependency>\n")
          .append("      <groupId>").append(string.substring(0, string.lastIndexOf("-")).replaceAll("-", ".")).append("</groupId>\n")
          .append("      <artifactId>").append(string.substring(0, string.lastIndexOf("-"))).append("</artifactId>\n")
          .append("      <version>").append(string.substring(string.lastIndexOf("-") + 1)).append("</version>\n")
          .append("      <scope>system</scope>\n")
          .append("      <systemPath>${pom.basedir}/src/main/resources/lib/").append(string).append(".jar</systemPath>\n")
          .append("    </dependency>\n");
        }
      }
    }
    if (!BuildUtils.isEmpty(wsdlJars) && !sourceOfWsdl) {
      for (String string : wsdlJars) {
        if (!BuildUtils.isEmpty(string)) {
          builder
          .append("    <dependency>\n")
          .append("      <groupId>").append(string).append("</groupId>\n")
          .append("      <artifactId>").append(string.replaceAll("\\.", "-")).append("</artifactId>\n")
          .append("      <version>1.0</version>\n")
          .append("      <scope>system</scope>\n")
          .append("      <systemPath>${pom.basedir}/src/main/resources/lib/").append(string.replaceAll("\\.", "-")).append("-1.0.0.jar</systemPath>\n")
          .append("    </dependency>\n");
        }
      }
    }
    builder
    .append("  </dependencies>\n")
    .append("\n")
    .append("  <build>\n")
    .append("    <plugins>\n")
    .append("      <plugin>\n")
    .append("        <groupId>org.springframework.boot</groupId>\n")
    .append("        <artifactId>spring-boot-maven-plugin</artifactId>\n");
    if (!BuildUtils.isEmpty(outerJar) || !BuildUtils.isEmpty(wsdlJars) && !sourceOfWsdl) {
      builder
      .append("        <configuration>\n")
      .append("          <fork>true</fork>\n")
      .append("          <includeSystemScope>true</includeSystemScope>\n")
      .append("        </configuration>\n");
    }
    builder
    .append("      </plugin>\n")
    .append("    </plugins>\n")
    .append("  </build>\n")
    .append("\n")
    .append("</project>");
    return builder.toString();
  }
	
  public String getApplication() {
    builder.delete(0, builder.length())
    .append("package com.").append(packageName).append(";\n")
    .append("\n")
    .append("import org.springframework.boot.SpringApplication;\n")
    .append("import org.springframework.boot.autoconfigure.SpringBootApplication;\n")
    .append("import org.springframework.boot.web.servlet.ServletComponentScan;\n")
    .append("\n")
    .append("/**\n")
    .append(" * @description\n")
    .append(" * @author ").append(author).append("\n")
    .append(" * @date ").append(date).append("\n")
    .append(" */\n")
    .append("@ServletComponentScan\n")
    .append("@SpringBootApplication\n")
    .append("public class ").append(projectName).append("Application {\n")
    .append("\n")
    .append("  public static void main(String[] args) {\n")
    .append("    SpringApplication.run(").append(projectName).append("Application.class, args);\n")
    .append("    StringBuilder builder = new StringBuilder(\"\\n\");\n");
    int len = projectName.length() + 6 + "Application is Running!".length();
    StringBuilder temp = new StringBuilder();
    for (int i = 0; i < len; i++) {
      temp.append("-");
    }
    temp.append("--");
    String dotLine = temp.toString();
    builder.append("    builder.append(\"").append(dotLine).append("\\n\");\n");
    temp.delete(0, temp.length());
    for (int i = 0; i < len; i++) {
      temp.append(" ");
    }
    String space = temp.toString();
    builder
    .append("    builder.append(\"|").append(space).append("|\\n\");\n")
    .append("    builder.append(\"|").append(space).append("|\\n\");\n")
    .append("    builder.append(\"|   ").append(projectName).append("Application is Running!   |\\n\");\n")
    .append("    builder.append(\"|").append(space).append("|\\n\");\n")
    .append("    builder.append(\"|").append(space).append("|\\n\");\n")
    .append("    builder.append(\"").append(dotLine).append("\\n\");\n")
    .append("    System.out.println(builder.toString());\n")
    .append("  }\n")
    .append("\n")
    .append("}");
    return builder.toString();
  }
	  
  public String getPackageName() {
    return packageName;
  }
  
  public String getProjectName() {
    return projectName;
  }
  
  public String getDate() {
    return date;
  }
  
  public String getAuthor() {
    return author;
  }
  
  public String getAddSplitLine() {
    return addSplitLine;
  }
  
  public String getFirstLowerCase() {
    return firstLowerCase;
  }
  
  public String getjQuery() {
    return jQuery;
  }
  
  public String getPort() {
    return port;
  }
  
  //.append("/*Call WebService(\n")
	//.append("                  wsimport -Xnocompile -encoding UTF-8 wsdl-Address\n")
	//.append("                  保存wsdl文件到xml文件之后查找删除<s:element ref=\"s:schema\" />之后可修改可不修改<s:any />为<s:any minOccurs=\"2\"maxOccurs=\"2\"/>可执行以上语句得到JAVA源文件\n")
	//.append("                 )*/\n")
	//.append("\n")
	//.append("  public static String md5(String content) {\n")
	//.append("    char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };\n")
	//.append("    try {\n")
	//.append("      byte[] bytes = content.getBytes();\n")
	//.append("      MessageDigest messageDigest = MessageDigest.getInstance("MD5");\n")
	//.append("      messageDigest.update(bytes);\n")
	//.append("      bytes = messageDigest.digest();\n")
	//.append("      int j = bytes.length;\n")
	//.append("      char str[] = new char[j << 1];\n")
	//.append("      int k = 0;\n")
	//.append("      for (int i = 0; i < j; i++) {\n")
	//.append("        byte b = bytes[i];\n")
	//.append("        str[k++] = hexDigits[b >>> 4 & 0xf];\n")
	//.append("        str[k++] = hexDigits[b & 0xf];\n")
	//.append("      }\n")
	//.append("      return new String(str);\n")
	//.append("    } catch (NoSuchAlgorithmException e) {\n")
	//.append("      return \"\";\n")
	//.append("    }\n")
	//.append("  }\n")

  //.append("      String data = \"\";\n")
  //.append("      if (request.getContentType().startsWith(").append(projectName).append("Mapping.MODEL.getContent())) {\n")
  //.append("        data = request.getParameter(\"data\");\n")
  //.append("      } else if (request.getContentType().startsWith(").append(projectName).append("Mapping.JSON.getContent())) {\n")
  //.append("        BufferedReader bufferedReader = request.getReader();\n")
  //.append("        String temp;\n")
  //.append("        while (null != (temp = bufferedReader.readLine())) {\n")
  //.append("          data = data.concat(temp);\n")
  //.append("        }\n")
  //.append("        bufferedReader.close();\n")
  //.append("      }\n")
  
}