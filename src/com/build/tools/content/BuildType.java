package com.build.tools.content;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.build.tools.utils.BuildUtils;

public class BuildType {
	
	private StringBuilder builder = new StringBuilder("BuildType");
	private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	
	public String getBase64JS() {
		clear();
		builder	.append("(function($) {\n")
				.append("    var b64 = \"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/\",\n")
				.append("        a256 = '',\n")
				.append("        r64 = [256],\n")
				.append("        r256 = [256],\n")
				.append("        i = 0;\n")
				.append("    var UTF8 = {\n")
				.append("        encode: function(strUni) {\n")
				.append("			var strUtf = strUni.replace(/[\\u0080-\\u07ff]/g,\n")
				.append("			function(c) {\n")
				.append("				var cc = c.charCodeAt(0);\n")
				.append("                return String.fromCharCode(0xc0 | cc >> 6, 0x80 | cc & 0x3f);\n")
				.append("            })\n")
				.append("            .replace(/[\\u0800-\\uffff]/g,\n")
				.append("			function(c) {\n")
				.append("                var cc = c.charCodeAt(0);\n")
				.append("                return String.fromCharCode(0xe0 | cc >> 12, 0x80 | cc >> 6 & 0x3F, 0x80 | cc & 0x3f);\n")
				.append("            });\n")
				.append("			return strUtf;\n")
				.append("		},\n")
				.append("        decode: function(strUtf) {\n")
				.append("			var strUni = strUtf.replace(/[\\u00e0-\\u00ef][\\u0080-\\u00bf][\\u0080-\\u00bf]/g,\n")
				.append("			function(c) {\n")
				.append("                var cc = ((c.charCodeAt(0) & 0x0f) << 12) | ((c.charCodeAt(1) & 0x3f) << 6) | (c.charCodeAt(2) & 0x3f);\n")
				.append("                return String.fromCharCode(cc);\n")
				.append("            })\n")
				.append("            .replace(/[\\u00c0-\\u00df][\\u0080-\\u00bf]/g,\n")
				.append("            function(c) {\n")
				.append("                var cc = (c.charCodeAt(0) & 0x1f) << 6 | c.charCodeAt(1) & 0x3f;\n")
				.append("                return String.fromCharCode(cc);\n")
				.append("            });\n")
				.append("            return strUni;\n")
				.append("        }\n")
				.append("    };\n")
				.append("    while(i < 256) {\n")
				.append("        var c = String.fromCharCode(i);\n")
				.append("        a256 += c;\n")
				.append("        r256[i] = i;\n")
				.append("        r64[i] = b64.indexOf(c);\n")
				.append("        ++i;\n")
				.append("    }\n")
				.append("    function code(s, discard, alpha, beta, w1, w2) {\n")
				.append("        s = String(s);\n")
				.append("        var buffer = 0,\n")
				.append("            i = 0,\n")
				.append("            length = s.length,\n")
				.append("            result = '',\n")
				.append("            bitsInBuffer = 0;\n")
				.append("        while(i < length) {\n")
				.append("            var c = s.charCodeAt(i);\n")
				.append("            c = c < 256 ? alpha[c] : -1;\n")
				.append("            buffer = (buffer << w1) + c;\n")
				.append("            bitsInBuffer += w1;\n")
				.append("            while(bitsInBuffer >= w2) {\n")
				.append("                bitsInBuffer -= w2;\n")
				.append("                var tmp = buffer >> bitsInBuffer;\n")
				.append("                result += beta.charAt(tmp);\n")
				.append("                buffer ^= tmp << bitsInBuffer;\n")
				.append("            }\n")
				.append("            ++i;\n")
				.append("        }\n")
				.append("        if(!discard && bitsInBuffer > 0) result += beta.charAt(buffer << (w2 - bitsInBuffer));\n")
				.append("        return result;\n")
				.append("    }\n")
				.append("    var Plugin = $.base64 = function(dir, input, encode) {\n")
				.append("		return input ? Plugin[dir](input, encode) : dir ? null : this;\n")
				.append("    };\n")
				.append("    Plugin.btoa = Plugin.encode = function(plain, utf8encode) {\n")
				.append("        plain = Plugin.raw === false || Plugin.utf8encode || utf8encode ? UTF8.encode(plain) : plain;\n")
				.append("        plain = code(plain, false, r256, b64, 8, 6);\n")
				.append("        return plain + '===='.slice((plain.length % 4) || 4);\n")
				.append("    };\n")
				.append("    Plugin.atob = Plugin.decode = function(coded, utf8decode) {\n")
				.append("        coded = String(coded).split('=');\n")
				.append("        var i = coded.length;\n")
				.append("        do {\n")
				.append("			--i;\n")
				.append("            coded[i] = code(coded[i], true, r64, a256, 6, 8);\n")
				.append("        } while (i > 0);\n")
				.append("        coded = coded.join('');\n")
				.append("        return Plugin.raw === false || Plugin.utf8decode || utf8decode ? UTF8.decode(coded) : coded;\n")
				.append("    };\n")
				.append("}(jQuery));");
		return builder.toString();
	}
	
	public String getRequestJS(String projectName, String port, boolean model) {
		clear();
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			ip = "localhost";
		}
		builder	.append("$.base64.utf8encode = true\n")
				.append("\n")
				.append("function postAndGet(type").append(model ? "" : ", handler").append(", data, success, error) {\n")
				.append("  $.ajax({\n")
				.append("    url : 'http://").append(ip).append(":").append(port).append("/").append(projectName).append(model ? "/invoke',\n" : "/' + handler,\n")
				.append("    type : type,\n")
				.append("    data : data,\n")
				.append("    dataType : 'json'\n")
				.append("  }).then(function(res) {\n")
				.append("    console.log(res)\n")
				.append("    if (200 == res.code) {\n")
				.append("      success(res.data)\n")
				.append("    } else {\n")
				.append("      alert(res.msg)\n")
				.append("      if (undefined != res.data) {\n")
				.append("        error(res.data)\n")
				.append("      }\n")
				.append("    }\n")
				.append("  })\n")
				.append("}\n")
				.append("\n")
				.append("function post(").append(model ? "" : "handler, ").append("data, success, error) {\n")
				.append("  postAndGet('POST', ").append(model ? "" : "handler, ").append("data, success, error)\n")
				.append("}\n")
				.append("\n")
				.append("function postJson(").append(model ? "" : "handler, ").append("data, success, error) {\n")
				.append("  post(").append(model ? "" : "handler, ").append("{data: JSON.stringify(data)}, success, error)\n")
				.append("}\n")
				.append("\n")
				.append("function postBase64Json(").append(model ? "" : "handler, ").append("data, success, error) {\n")
				.append("  post(").append(model ? "" : "handler, ").append("{data: $.base64.btoa(JSON.stringify(data))}, success, error)\n")
				.append("}\n")
				.append("\n")
				.append("function get(").append(model ? "" : "handler, ").append("data, success, error) {\n")
				.append("  postAndGet('GET', ").append(model ? "" : "handler, ").append("data, success, error)\n")
				.append("}\n")
				.append("\n")
				.append("function getJson(").append(model ? "" : "handler, ").append("data, success, error) {\n")
				.append("  get(").append(model ? "" : "handler, ").append("{data: JSON.stringify(data)}, success, error)\n")
				.append("}\n")
				.append("\n")
				.append("function getBase64Json(").append(model ? "" : "handler, ").append("data, success, error) {\n")
				.append("  get(").append(model ? "" : "handler, ").append("{data: $.base64.btoa(JSON.stringify(data))}, success, error)\n")
				.append("}\n");
		return builder.toString();
	}
	
	public String getHtml(String projectName, String author, boolean model) {
		clear();
		builder	.append("<html>\n")
				.append("  <head>\n")
				.append("    <meta charset='utf-8'>\n")
				.append("    <title>").append(projectName).append("</title>\n")
				.append("    <style>\n")
				.append("      td {\n")
				.append("        text-align: center\n")
				.append("      }\n")
				.append("    </style>\n")
				.append("  </head>\n")
				.append("  <body>\n")
				.append("    <h1><center>Demo Of ").append(projectName).append("</center></h1>\n")
				.append("    <table border=\"1\" align=\"center\">\n")
				.append("      <tr>\n")
				.append("        <th>ProjectName</th>\n")
				.append("        <th>Author</th>\n")
				.append("        <th>Version</th>\n")
				.append("        <th>Major</th>\n")
				.append("      </tr>\n")
				.append("      <tr>\n")
				.append("        <td id=\"projectName\"></td>\n")
				.append("        <td id=\"author\"></td>\n")
				.append("        <td id=\"version\"></td>\n")
				.append("        <td id=\"major\"></td>\n")
				.append("      </tr>\n")
				.append("    </table>\n")
				.append("  </body>\n")
				.append("  <script src=\"http://code.jquery.com/jquery-latest.min.js\"></script>\n")
				.append("  <script src=\"jquery.base64.js\"></script>\n")
				.append("  <script src=\"request.js\"></script>\n")
				.append("  <script>\n")
				.append("    $(\n")
				.append("      function() {\n")
				.append("        postBase64Json\n")
				.append("        (\n");
		if (model) {
			builder	.append("          {\n")
					.append("            method: 'index',\n");
		} else {
			builder	.append("          'index',\n")
					.append("          {\n");
		}
		builder	.append("            author: '").append(author).append("'\n")
				.append("          },\n")
				.append("          function(data) {\n")
				.append("            for(var key in data) {\n")
				.append("              $('#' + key).html(data[key]);\n")
				.append("            }\n")
				.append("          }\n")
				.append("        )\n")
				.append("      }\n")
				.append("    )\n")
				.append("  </script>\n")
				.append("</html>");
		return builder.toString();
	}
	
	public String getPom(String projectName, String packageName, String[] outerJar, String[] wsdlJars, boolean sourceOfWsdl) {
		clear();
		builder	.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
			   	.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n")
			   	.append("     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n")
			   	.append("     xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n")
			   	.append("  <modelVersion>4.0.0</modelVersion>\n")
			   	.append("\n")
			   	.append("  <groupId>com.").append(packageName).append("</groupId>\n")
			   	.append("  <artifactId>").append(projectName).append("</artifactId>\n")
			   	.append("  <version>1.0-PRO</version>\n")
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
			   	.append("    </dependency>\n");
		if (!BuildUtils.isEmpty(outerJar)) {
			for (String string : outerJar) {
				builder	.append("    <dependency>\n")
						.append("      <groupId>").append(string.substring(0, string.lastIndexOf("-")).replaceAll("-", ".")).append("</groupId>\n")
						.append("      <artifactId>").append(string.substring(0, string.lastIndexOf("-"))).append("</artifactId>\n")
						.append("      <version>").append(string.substring(string.lastIndexOf("-") + 1)).append("</version>\n")
						.append("      <scope>system</scope>\n")
						.append("      <systemPath>${pom.basedir}/src/main/resources/lib/").append(string).append(".jar</systemPath>\n")
						.append("    </dependency>\n");
			}
		}
		if (!BuildUtils.isEmpty(wsdlJars) && !sourceOfWsdl) {
			for (String string : wsdlJars) {
				builder	.append("    <dependency>\n")
						.append("      <groupId>").append(string).append("</groupId>\n")
						.append("      <artifactId>").append(string.replaceAll("\\.", "-")).append("</artifactId>\n")
						.append("      <version>1.0</version>\n")
						.append("      <scope>system</scope>\n")
						.append("      <systemPath>${pom.basedir}/src/main/resources/lib/").append(string.replaceAll("\\.", "-")).append("-1.0.jar</systemPath>\n")
						.append("    </dependency>\n");
			}
		}
		builder	.append("  </dependencies>\n")
			   	.append("\n")
			   	.append("  <build>\n")
			   	.append("    <plugins>\n")
			   	.append("      <plugin>\n")
			   	.append("        <groupId>org.springframework.boot</groupId>\n")
			   	.append("        <artifactId>spring-boot-maven-plugin</artifactId>\n");
		if (!BuildUtils.isEmpty(outerJar) || !BuildUtils.isEmpty(wsdlJars) && !sourceOfWsdl) {
			builder	.append("        <configuration>\n")
					.append("          <fork>true</fork>\n")
					.append("          <includeSystemScope>true</includeSystemScope>\n")
					.append("        </configuration>\n");
		}
		builder	.append("      </plugin>\n")
			   	.append("    </plugins>\n")
			   	.append("  </build>\n")
			   	.append("\n")
			   	.append("</project>\n");
		return builder.toString();
	}
	
	public String getConfig(String port) {
		clear();
		builder	.append("server:\n")
				.append("  port: ")
				.append(port);
		return builder.toString();
	}
	
	public String getApplication(String projectName, String packageName, String author) {
		clear();
		builder	.append("package com.").append(packageName).append(";\n")
				.append("\n")
				.append("import org.springframework.boot.SpringApplication;\n")
				.append("import org.springframework.boot.autoconfigure.SpringBootApplication;\n")
				.append("import org.springframework.boot.web.servlet.ServletComponentScan;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description\n")
				.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
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
		builder	.append("    builder.append(\"|").append(space).append("|\\n\");\n")
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
	
	public String getController(String projectName, String packageName, String author, boolean model) {
		clear();
		builder	.append("package com.").append(packageName).append(".controller;\n")
				.append("\n")
				.append("import com.").append(packageName).append(".core.Response;\n")
				.append("import com.").append(packageName).append(".core.ResponseGenerator;\n")
				.append("import com.").append(packageName).append(".mapping.").append(projectName).append("Mapping;\n")
				.append("import com.").append(packageName).append(".service.").append(projectName).append("Service;\n")
				.append(model ? "import com.".concat(packageName).concat(".utils.").concat(projectName).concat("Utils;\n") : "")
				.append("import org.slf4j.Logger;\n")
				.append("import org.slf4j.LoggerFactory;\n")
				.append("\n")
				.append("import org.springframework.beans.factory.annotation.Autowired;\n")
				.append("import org.springframework.web.bind.annotation.CrossOrigin;\n")
				.append("import org.springframework.web.bind.annotation.RequestMapping;\n")
				.append("import org.springframework.web.bind.annotation.RestController;\n")
				.append("\n")
				.append("import javax.servlet.http.HttpServletRequest;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description\n")
				.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append("@RestController\n")
				.append("@RequestMapping(\"/").append(projectName).append("\")\n")
				.append("@CrossOrigin(maxAge = 3600, origins = \"*\")\n")
				.append("public class ").append(projectName).append("Controller {\n")
				.append("\n")
				.append("  @Autowired\n")
				.append("  private ").append(projectName).append("Service service;\n")
				.append("  private Logger logger = LoggerFactory.getLogger(").append(projectName).append("Controller.class);\n")
				.append("\n");
				if (model) {
					builder	.append("  @RequestMapping(\"/invoke\")\n")
							.append("  public Response invoke(HttpServletRequest request) {\n")
							.append("    try {\n")
							.append("      return ResponseGenerator.genYes(").append(projectName).append("Utils.invoke(request.getAttribute(\"data\"), service));\n")
							.append("    } catch (Exception e) {\n")
							.append("      return ResponseGenerator.genNo(logger, e, ").append(projectName).append("Mapping.INVOKE);\n")
							.append("    }\n")
							.append("  }\n");
				} else {
					builder	.append("  @RequestMapping(\"/index\")\n")
							.append("  public Response index(HttpServletRequest request) {\n")
							.append("    try {\n")
							.append("      return ResponseGenerator.genYes(service.index(request.getParameter(\"data\")));\n")
							.append("    } catch (Exception e) {\n")
							.append("      return ResponseGenerator.genNo(logger, e, ").append(projectName).append("Mapping.INDEX);\n")
							.append("    }\n")
							.append("  }\n");
				}
				builder	.append("\n")
						.append("}");
		return builder.toString();
	}
	
	public String getResponse(String projectName, String packageName, String author) {
		clear();
		builder	.append("package com.").append(packageName).append(".core;\n")
				.append("\n")
				.append("import com.fasterxml.jackson.annotation.JsonInclude;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description Entity of Response\n")
				.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append("@JsonInclude(JsonInclude.Include.NON_NULL)\n")
				.append("public class Response {\n")
				.append("\n")
				.append("  private Integer code;\n")
				.append("  private String msg;\n")
				.append("  private Object data;\n")
				.append("\n")
				.append("  public Integer getCode() {\n")
				.append("    return code;\n")
				.append("  }\n")
				.append("\n")
				.append("  public Response setCode(ResponseCode code) {\n")
				.append("    this.code = code.getCode();\n")
				.append("    return this;\n")
				.append("  }\n")
				.append("\n")
				.append("  public String getMsg() {\n")
				.append("    return msg;\n")
				.append("  }\n")
				.append("\n")
				.append("  public Response setMsg(String msg) {\n")
				.append("    this.msg = msg;\n")
				.append("    return this;\n")
				.append("  }\n")
				.append("\n")
				.append("  public Object getData() {\n")
				.append("    return data;\n")
				.append("  }\n")
				.append("\n")
				.append("  public Response setData(Object data) {\n")
				.append("    this.data = data;\n")
				.append("    return this;\n")
				.append("  }\n")
				.append("\n")
				.append("}");
		return builder.toString();
	}
	
	public String getResponseCode(String projectName, String packageName, String author) {
		clear();
		builder	.append("package com.").append(packageName).append(".core;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description Code of Response\n")
				.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append("public enum ResponseCode {\n")
				.append("\n")
				.append("  YES(200),\n")
				.append("  NO(404);\n")
				.append("\n")
				.append("  private final Integer code;\n")
				.append("\n")
				.append("  ResponseCode(final Integer code) {\n")
				.append("    this.code = code;\n")
				.append("  }\n")
				.append("\n")
				.append("  public Integer getCode() {\n")
				.append("    return code;\n")
				.append("  }\n")
				.append("\n")
				.append("}");
		return builder.toString();
	}

	public String getResponseGenerator(String projectName, String packageName, String author) {
		clear();
		builder	.append("package com.").append(packageName).append(".core;\n")
				.append("\n")
				.append("import com.").append(packageName).append(".mapping.").append(projectName).append("Mapping;\n")
				.append("import org.slf4j.Logger;\n")
				.append("import org.springframework.util.StringUtils;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description Generator of Response\n")
				.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append("public final class ResponseGenerator {\n")
				.append("\n")
				.append("  private static final String DEFAULT_YES = \"YES\";\n")
				.append("  private static final String format = \"\\nclass:\\t{}\\nmethod:\\t{}\\nline:\\t{}\\n\";\n")
				.append("\n")
				.append("  public static Response genYes() {\n")
				.append("    return new Response()\n")
				.append("            .setCode(ResponseCode.YES)\n")
				.append("            .setMsg(DEFAULT_YES);\n")
				.append("  }\n")
				.append("\n")
				.append("  public static Response genYes(Object data) {\n")
				.append("    return genYes()\n")
				.append("            .setData(data);\n")
				.append("  }\n")
				.append("\n")
				.append("  public static Response genNo(Logger logger, Exception e, ").append(projectName).append("Mapping mapping) {\n")
				.append("    StackTraceElement[] elements = e.getCause().getStackTrace();\n")
				.append("    for (int i = elements.length - 1; i >= 0; i--) {\n")
				.append("      StackTraceElement element = elements[i];\n")
				.append("      if (element.toString().startsWith(\"com.").append(packageName).append("\"))\n")
				.append("        logger.error(format, element.getClassName(), element.getMethodName(), element.getLineNumber());\n")
				.append("    }\n")
				.append("    return new Response()\n")
				.append("            .setCode(ResponseCode.NO)\n")
				.append("            .setMsg(StringUtils.isEmpty(e.getCause()) ? mapping.getContent() : StringUtils.isEmpty(e.getCause().getMessage()) ? mapping.getContent() : e.getCause().getMessage());\n")
				.append("  }\n")
				.append("\n")
				.append("  public static Response genNo(Logger logger, Object data, Exception e, ").append(projectName).append("Mapping mapping) {\n")
				.append("    return genNo(logger, e, mapping)\n")
				.append("            .setData(data);\n")
				.append("  }\n")
				.append("\n")
				.append("}");
		return builder.toString();
	}
	
	public String getMapping(String projectName, String packageName, String author, boolean model) {
		clear();
		builder	.append("package com.").append(packageName).append(".mapping;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description\n")
				.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append("public enum ").append(projectName).append("Mapping {\n")
				.append("\n")
				.append(model ? "  INVOKE(\"Invocation invoke fail!\");\n" : "  INDEX(\"Invocation index fail!\");\n")
				.append("\n")
				.append("  final String content;\n")
				.append("\n")
				.append("  ").append(projectName).append("Mapping(final String content) {\n")
				.append("    this.content = content;\n")
				.append("  }\n")
				.append("\n")
				.append("  public String getContent() {\n")
				.append("    return content;\n")
				.append("  }\n")
				.append("\n")
				.append("}\n");
		return builder.toString();
	}
	
	public String getService(String projectName, String packageName, String author, boolean model) {
		clear();
		builder	.append("package com.").append(packageName).append(".service;\n")
				.append("\n");
		if (model) {
			builder	.append("import org.springframework.beans.factory.annotation.Autowired;\n")
					.append("import org.springframework.stereotype.Service;\n")
					.append("import org.springframework.web.client.RestTemplate;\n")
					.append("\n");
		}
		builder	.append("import java.util.Map;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description\n")
				.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append(model ? "@Service\n" : "")
				.append("public ").append(model ? "class" : "interface").append(" ").append(projectName).append("Service {\n")
				.append("\n");
		if (model) {
			builder	.append("  @Autowired\n")
					.append("  private RestTemplate restTemplate;\n")
					.append("\n")
					.append("  public Map<String, Object> index(Map<String, Object> map) throws Exception {\n")
					.append("    map.put(\"projectName\", \"").append(projectName).append("\");\n")
					.append("    map.put(\"version\", \"PRO\");\n")
					.append("    map.put(\"major\", \"1.0\");\n")
					.append("    return map;\n")
					.append("  }\n");
		} else {
			builder	.append("  Map<String, Object> index(String json) throws Exception;\n");
		}
		builder	.append("\n")
				.append("}");
		return builder.toString();
	}
	
	public String getServiceImpl(String projectName, String packageName, String author) {
		clear();
		builder	.append("package com.").append(packageName).append(".service.impl;\n")
				.append("\n")
				.append("import com.").append(packageName).append(".service.").append(projectName).append("Service;\n")
				.append("import com.").append(packageName).append(".utils.").append(projectName).append("Utils;\n")
				.append("import org.springframework.beans.factory.annotation.Autowired;\n")
				.append("import org.springframework.stereotype.Service;\n")
				.append("import org.springframework.web.client.RestTemplate;\n")
				.append("\n")
				.append("import java.util.Map;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description\n")
				.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append("@Service\n")
				.append("public class ").append(projectName).append("ServiceImpl implements ").append(projectName).append("Service {\n")
				.append("\n")
				.append("  @Autowired\n")
				.append("  private RestTemplate restTemplate;\n")
				.append("\n")
				.append("  public Map<String, Object> index(String json) throws Exception {\n")
				.append("    Map<String, Object> map = ").append(projectName).append("Utils.getMapFromBase64(json);\n")
				.append("    map.put(\"projectName\", \"").append(projectName).append("\");\n")
				.append("    map.put(\"version\", \"PRO\");\n")
				.append("    map.put(\"major\", \"1.0\");\n")
				.append("    return map;\n")
				.append("  }\n")
				.append("\n")
				.append("}");
		return builder.toString();
	}
	
	public String getFilter(String projectName, String packageName, String author) {
		clear();
		builder	.append("package com.").append(packageName).append(".filter;\n")
				.append("\n")
				.append("import com.").append(packageName).append(".utils.").append(projectName).append("Utils;\n")
				.append("\n")
				.append("import javax.servlet.*;\n")
				.append("import javax.servlet.annotation.WebFilter;\n")
				.append("import javax.servlet.http.HttpServletRequest;\n")
				.append("import javax.servlet.http.HttpServletResponse;\n")
				.append("import java.util.Map;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description\n")
				.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append("@WebFilter(\"/").append(projectName).append("/*\")\n")
				.append("public class ").append(projectName).append("Filter implements Filter {\n")
				.append("\n")
				.append("  public void init(FilterConfig filterConfig) {\n")
				.append("  }\n")
				.append("\n")
				.append("  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {\n")
				.append("    try {\n")
				.append("      HttpServletRequest request = (HttpServletRequest) servletRequest;\n")
				.append("      HttpServletResponse response = (HttpServletResponse) servletResponse;\n")
				.append("      Map<String, Object> map = ").append(projectName).append("Utils.getMapFromBase64(request.getParameter(\"data\"));\n")
				.append("      request.setAttribute(\"data\", map);\n")
				.append("      filterChain.doFilter(request, response);\n")
				.append("    } catch (Exception e) {\n")
				.append("    }\n")
				.append("  }\n")
				.append("\n")
				.append("  public void destroy() {\n")
				.append("  }\n")
				.append("\n")
				.append("}");
		return builder.toString();
	}
	
	public String getUtils(String projectName, String packageName, String author, boolean model) {
		clear();
		builder	.append("package com.").append(packageName).append(".utils;\n")
				.append("\n")
				.append("import com.fasterxml.jackson.databind.ObjectMapper;\n")
				.append("import org.springframework.http.HttpEntity;\n")
				.append("import org.springframework.http.HttpHeaders;\n")
				.append("import org.springframework.http.MediaType;\n")
				.append("import org.springframework.util.LinkedMultiValueMap;\n")
				.append("import org.springframework.util.MultiValueMap;\n")
				.append("import org.springframework.util.StringUtils;\n")
				.append("import org.springframework.web.client.RestTemplate;\n")
				.append("\n")
				.append("import java.lang.reflect.Method;\n")
				.append("import java.util.Base64;\n")
				.append("import java.util.Map;\n")
				.append("\n")
				//.append("/*Call WebService(\n")
				//.append("                  wsimport -Xnocompile -encoding UTF-8 wsdl-Address\n")
				//.append("                  保存wsdl文件到xml文件之后查找删除<s:element ref=\"s:schema\" />之后可修改可不修改<s:any />为<s:any minOccurs=\"2\"maxOccurs=\"2\"/>可执行以上语句得到JAVA源文件\n")
				//.append("                 )*/\n")
				.append("/**\n")
				.append(" * @description\n")
				.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append("public final class ").append(projectName).append("Utils {\n")
				.append("\n")
				.append("  private static ObjectMapper mapper = new ObjectMapper();\n")
				.append("\n");
		if (model) {
			builder .append("  public static Object invoke(Object data, Object service) throws Exception {\n")
					.append("    Map<String, Object> map = (Map<String, Object>) data;\n")
					.append("    Method method = service.getClass().getMethod((String) map.get(\"method\"), Map.class);\n")
					.append("    return method.invoke(service, map);\n")
					.append("  }\n")
					.append("\n");
		}
		builder	.append("  public static <T> T getEntity(String json, Class<T> clazz) throws Exception {\n")
				.append("    return getEntity(json, clazz, false);\n")
				.append("  }\n")
				.append("\n")
				.append("  public static <T> T getEntityFromBase64(String json, Class<T> clazz) throws Exception {\n")
				.append("    return getEntity(json, clazz, true);\n")
				.append("  }\n")
				.append("\n")
				.append("  public static Map<String, Object> getMap(String json) throws Exception {\n")
				.append("    return getEntity(json, Map.class);\n")
				.append("  }\n")
				.append("\n")
				.append("  public static Map<String, Object> getMapFromBase64(String json) throws Exception {\n")
				.append("    return getEntityFromBase64(json, Map.class);\n")
				.append("  }\n")
				.append("\n")
				.append("  private static <T> T getEntity(String json, Class<T> clazz, boolean fromBase64) throws Exception {\n")
				.append("    if (StringUtils.isEmpty(json)) {\n")
				.append("      throw new Exception(\"Argument Not Allowed Empty!\");\n")
				.append("    }\n")
				.append("    if (fromBase64) {\n")
				.append("      byte[] bytes = Base64.getDecoder().decode(json);\n")
				.append("      json = new String(bytes, \"UTF-8\");\n")
				.append("    }\n")
				.append("    return mapper.readValue(json, clazz);\n")
				.append("  }\n")
				.append("\n")
				.append("  public static <T> T get(String url, RestTemplate restTemplate, Class<T> clazz) {\n")
				.append("    return restTemplate.getForObject(url, clazz);\n")
				.append("  }\n")
				.append("\n")
				.append("  public static <T> T post(String url, Map<String, Object> params, RestTemplate restTemplate, Class<T> clazz) {\n")
				.append("    HttpHeaders headers = new HttpHeaders();\n")
				.append("    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);\n")
				.append("    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();\n")
				.append("    map.setAll(params);\n")
				.append("    HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(map, headers);\n")
				.append("    return restTemplate.postForObject(url, formEntity , clazz);\n")
				.append("  }\n")
				.append("\n")
				.append("  public static <T> T postJson(String url, Map<String, Object> params, RestTemplate restTemplate, Class<T> clazz) {\n")
				.append("    HttpHeaders headers = new HttpHeaders();\n")
				.append("    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);\n")
				.append("    try {\n")
				.append("      return restTemplate.postForObject(url, new HttpEntity<String>(mapper.writeValueAsString(params), headers), clazz);\n")
				.append("    } catch (Exception e) {\n")
				.append("      //It's impossible to happen\n")
				.append("      return null;\n")
				.append("    }\n")
				.append("  }\n")
				.append("\n")
				.append("}");
		return builder.toString();
	}
	
	public String getRestTemplate(String projectName, String packageName, String author) {
		clear();
		builder	.append("package com.").append(packageName).append(".config;\n")
				.append("\n")
				.append("import com.fasterxml.jackson.databind.ObjectMapper;\n")
				.append("import org.springframework.context.annotation.Bean;\n")
				.append("import org.springframework.context.annotation.Configuration;\n")
				.append("import org.springframework.http.MediaType;\n")
				.append("import org.springframework.http.client.ClientHttpRequestFactory;\n")
				.append("import org.springframework.http.client.SimpleClientHttpRequestFactory;\n")
				.append("import org.springframework.http.converter.HttpMessageConverter;\n")
				.append("import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;\n")
				.append("import org.springframework.web.client.RestTemplate;\n")
				.append("\n")
				.append("import java.util.ArrayList;\n")
				.append("import java.util.List;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description\n")
				.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append("@Configuration\n")
				.append("public class RestTemplateConfig {\n")
				.append("\n")
				.append("  @Bean\n")
				.append("  public RestTemplate restTemplate(ClientHttpRequestFactory factory) {\n")
				.append("    RestTemplate restTemplate = new RestTemplate(factory);\n")
				.append("    List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();\n")
				.append("    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();\n")
				.append("    jsonConverter.setObjectMapper(new ObjectMapper());\n")
				.append("    List<MediaType> mediaTypes = new ArrayList<>();\n")
				.append("    mediaTypes.add(MediaType.TEXT_PLAIN);\n")
				.append("    mediaTypes.add(MediaType.TEXT_HTML);\n")
				.append("    mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);\n")
				.append("    jsonConverter.setSupportedMediaTypes(mediaTypes);\n")
				.append("    converters.add(jsonConverter);\n")
				.append("    return restTemplate;\n")
				.append("  }\n")
				.append("\n")
				.append("  @Bean\n")
				.append("  public ClientHttpRequestFactory clientHttpRequestFactory() {\n")
				.append("    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();\n")
				.append("    factory.setReadTimeout(30000);\n")
				.append("    factory.setConnectTimeout(30000);\n")
				.append("    return factory;\n")
				.append("  }\n")
				.append("\n")
				.append("}");
		return builder.toString();
	}
	
	private void clear() {
		builder.delete(0, builder.length());
	}
	
	public String firstUpperCase(String content) {
		return content.toUpperCase().charAt(0) + content.substring(1);
	}

}
