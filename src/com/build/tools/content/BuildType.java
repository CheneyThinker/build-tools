package com.build.tools.content;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	public String getRequestJS(String projectName, String port) {
		clear();
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			ip = "localhost";
		}
		builder	.append("$.base64.utf8encode = true\n")
				.append("\n")
				.append("function postAndGet(type, handler, data, success, error) {\n")
				.append("  $.ajax({\n")
				.append("    url : 'http://").append(ip).append(":").append(port).append("/").append(projectName).append("/' + handler,\n")
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
				.append("function post(handler, data, success, error) {\n")
				.append("  postAndGet('POST', handler, data, success, error)\n")
				.append("}\n")
				.append("\n")
				.append("function postJson(handler, data, success, error) {\n")
				.append("  post(handler, {data: JSON.stringify(data)}, success, error)\n")
				.append("}\n")
				.append("\n")
				.append("function postBase64Json(handler, data, success, error) {\n")
				.append("  post(handler, {data: $.base64.btoa(JSON.stringify(data))}, success, error)\n")
				.append("}\n")
				.append("\n")
				.append("function get(handler, data, success, error) {\n")
				.append("  postAndGet('GET', handler, data, success, error)\n")
				.append("}\n")
				.append("\n")
				.append("function getJson(handler, data, success, error) {\n")
				.append("  get(handler, {data: JSON.stringify(data)}, success, error)\n")
				.append("}\n")
				.append("\n")
				.append("function getBase64Json(handler, data, success, error) {\n")
				.append("  get(handler, {data: $.base64.btoa(JSON.stringify(data))}, success, error)\n")
				.append("}\n");
		return builder.toString();
	}
	
	public String getHtml(String projectName, String author) {
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
				.append("        (\n")
				.append("          'index',\n")
				.append("          {\n")
				.append("            author: '").append(author).append("'\n")
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
	
	public String getPom(String projectName) {
		clear();
		builder	.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
			   	.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n")
			   	.append("     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n")
			   	.append("     xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n")
			   	.append("  <modelVersion>4.0.0</modelVersion>\n")
			   	.append("\n")
			   	.append("  <groupId>com.").append(splitByUpperCaseAndAddDot(projectName)).append("</groupId>\n")
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
			   	.append("    </dependency>\n")
			   	.append("  </dependencies>\n")
			   	.append("\n")
			   	.append("  <build>\n")
			   	.append("    <plugins>\n")
			   	.append("      <plugin>\n")
			   	.append("        <groupId>org.springframework.boot</groupId>\n")
			   	.append("        <artifactId>spring-boot-maven-plugin</artifactId>\n")
			   	.append("      </plugin>\n")
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
	
	public String getApplication(String projectName, String author) {
		clear();
		builder	.append("package com.").append(splitByUpperCaseAndAddDot(projectName)).append(";\n")
				.append("\n")
				.append("import org.springframework.boot.SpringApplication;\n")
				.append("import org.springframework.boot.autoconfigure.SpringBootApplication;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description\n")
				.append(" * @author ").append(isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append("@SpringBootApplication\n")
				.append("public class ").append(firstUpperCase(projectName)).append("Application {\n")
				.append("\n")
				.append("  public static void main(String[] args) {\n")
				.append("    SpringApplication.run(").append(firstUpperCase(projectName)).append("Application.class, args);\n")
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
				.append("    builder.append(\"|   ").append(firstUpperCase(projectName)).append("Application is Running!   |\\n\");\n")
				.append("    builder.append(\"|").append(space).append("|\\n\");\n")
				.append("    builder.append(\"|").append(space).append("|\\n\");\n")
				.append("    builder.append(\"").append(dotLine).append("\\n\");\n")
				.append("    System.out.println(builder.toString());\n")
				.append("  }\n")
				.append("\n")
				.append("}");
		return builder.toString();
	}
	
	public String getController(String projectName, String author) {
		clear();
		builder	.append("package com.").append(splitByUpperCaseAndAddDot(projectName)).append(".controller;\n")
				.append("\n")
				.append("import com.").append(splitByUpperCaseAndAddDot(projectName)).append(".core.Response;\n")
				.append("import com.").append(splitByUpperCaseAndAddDot(projectName)).append(".core.ResponseGenerator;\n")
				.append("import com.").append(splitByUpperCaseAndAddDot(projectName)).append(".mapping.").append(firstUpperCase(projectName)).append("Mapping;\n")
				.append("import com.").append(splitByUpperCaseAndAddDot(projectName)).append(".service.").append(firstUpperCase(projectName)).append("Service;\n")
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
				.append(" * @author ").append(isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append("@RestController\n")
				.append("@RequestMapping(\"/").append(firstUpperCase(projectName)).append("\")\n")
				.append("@CrossOrigin(maxAge = 3600, origins = \"*\")\n")
				.append("public class ").append(firstUpperCase(projectName)).append("Controller {\n")
				.append("\n")
				.append("  @Autowired\n")
				.append("  private ").append(firstUpperCase(projectName)).append("Service service;\n")
				.append("  private Logger logger = LoggerFactory.getLogger(").append(firstUpperCase(projectName)).append("Controller.class);\n")
				.append("\n")
				.append("  @RequestMapping(\"/index\")\n")
				.append("  public Response index(HttpServletRequest request) {\n")
				.append("    try {\n")
				.append("      return ResponseGenerator.genYes(service.index(request.getParameter(\"data\")));\n")
				.append("    } catch (Exception e) {\n")
				.append("      return ResponseGenerator.genNo(logger, e, ").append(firstUpperCase(projectName)).append("Mapping.INDEX);\n")
				.append("    }\n")
				.append("  }\n")
				.append("\n")
				.append("}");
		return builder.toString();
	}
	
	public String getResponse(String projectName, String author) {
		clear();
		builder	.append("package com.").append(splitByUpperCaseAndAddDot(projectName)).append(".core;\n")
				.append("\n")
				.append("import com.fasterxml.jackson.annotation.JsonInclude;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description Entity of Response\n")
				.append(" * @author ").append(isEmpty(author) ? "admin" : author).append("\n")
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
	
	public String getResponseCode(String projectName, String author) {
		clear();
		builder	.append("package com.").append(splitByUpperCaseAndAddDot(projectName)).append(".core;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description Code of Response\n")
				.append(" * @author ").append(isEmpty(author) ? "admin" : author).append("\n")
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

	public String getResponseGenerator(String projectName, String author) {
		clear();
		builder	.append("package com.").append(splitByUpperCaseAndAddDot(projectName)).append(".core;\n")
				.append("\n")
				.append("import com.").append(splitByUpperCaseAndAddDot(projectName)).append(".mapping.").append(firstUpperCase(projectName)).append("Mapping;\n")
				.append("import org.slf4j.Logger;\n")
				.append("import org.springframework.util.StringUtils;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description Generator of Response\n")
				.append(" * @author ").append(isEmpty(author) ? "admin" : author).append("\n")
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
				.append("  public static Response genNo(Logger logger, Exception e, ").append(firstUpperCase(projectName)).append("Mapping mapping) {\n")
				.append("    StackTraceElement[] elements = e.getStackTrace();\n")
				.append("    for (int i = elements.length - 1; i >= 0; i--) {\n")
				.append("      StackTraceElement element = elements[i];\n")
				.append("      logger.error(format, element.getClassName(), element.getMethodName(), element.getLineNumber());\n")
				.append("    }\n")
				.append("    return new Response()\n")
				.append("            .setCode(ResponseCode.NO)\n")
				.append("            .setMsg(StringUtils.isEmpty(e.getMessage()) ? mapping.getContent() : e.getMessage());\n")
				.append("  }\n")
				.append("\n")
				.append("  public static Response genNo(Logger logger, Object data, Exception e, ").append(firstUpperCase(projectName)).append("Mapping mapping) {\n")
				.append("    return genNo(logger, e, mapping)\n")
				.append("            .setData(data);\n")
				.append("  }\n")
				.append("\n")
				.append("}");
		return builder.toString();
	}
	
	public String getMapping(String projectName, String author) {
		clear();
		clear();
		builder	.append("package com.").append(splitByUpperCaseAndAddDot(projectName)).append(".mapping;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description\n")
				.append(" * @author ").append(isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append("public enum ").append(firstUpperCase(projectName)).append("Mapping {\n")
				.append("\n")
				.append("  INDEX(\"Call index fail!\");\n")
				.append("\n")
				.append("  final String content;\n")
				.append("\n")
				.append("  ").append(firstUpperCase(projectName)).append("Mapping(final String content) {\n")
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
	
	public String getService(String projectName, String author) {
		clear();
		builder	.append("package com.").append(splitByUpperCaseAndAddDot(projectName)).append(".service;\n")
				.append("\n")
				.append("import java.util.Map;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description\n")
				.append(" * @author ").append(isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append("public interface ").append(firstUpperCase(projectName)).append("Service {\n")
				.append("\n")
				.append("  Map<String, Object> index(String json) throws Exception;\n")
				.append("\n")
				.append("}");
		return builder.toString();
	}
	
	public String getServiceImpl(String projectName, String author) {
		clear();
		builder	.append("package com.").append(splitByUpperCaseAndAddDot(projectName)).append(".service.impl;\n")
				.append("\n")
				.append("import com.").append(splitByUpperCaseAndAddDot(projectName)).append(".service.").append(firstUpperCase(projectName)).append("Service;\n")
				.append("import com.").append(splitByUpperCaseAndAddDot(projectName)).append(".utils.").append(firstUpperCase(projectName)).append("Utils;\n")
				.append("import org.springframework.stereotype.Service;\n")
				.append("\n")
				.append("import java.util.Map;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description\n")
				.append(" * @author ").append(isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append("@Service\n")
				.append("public class ").append(firstUpperCase(projectName)).append("ServiceImpl implements ").append(firstUpperCase(projectName)).append("Service {\n")
				.append("\n")
				.append("  public Map<String, Object> index(String json) throws Exception {\n")
				.append("    Map<String, Object> map = ").append(firstUpperCase(projectName)).append("Utils.getMapFromBase64(json);\n")
				.append("    map.put(\"projectName\", \"").append(firstUpperCase(projectName)).append("\");\n")
				.append("    map.put(\"version\", \"PRO\");\n")
				.append("    map.put(\"major\", \"1.0\");\n")
				.append("    return map;\n")
				.append("  }\n")
				.append("\n")
				.append("}");
		return builder.toString();
	}
	
	public String getUtils(String projectName, String author) {
		clear();
		builder	.append("package com.").append(splitByUpperCaseAndAddDot(projectName)).append(".utils;\n")
				.append("\n")
				.append("import com.fasterxml.jackson.databind.ObjectMapper;\n")
				.append("import org.springframework.util.StringUtils;\n")
				.append("\n")
				.append("import java.util.Base64;\n")
				.append("import java.util.Map;\n")
				.append("\n")
				.append("/**\n")
				.append(" * @description\n")
				.append(" * @author ").append(isEmpty(author) ? "admin" : author).append("\n")
				.append(" * @date ").append(date).append("\n")
				.append(" */\n")
				.append("public final class ").append(firstUpperCase(projectName)).append("Utils {\n")
				.append("\n")
				.append("  private static ObjectMapper mapper = new ObjectMapper();\n")
				.append("\n")
				.append("  public static <T> T getEntity(String json, Class<T> clazz) throws Exception {\n")
				.append("    return getEntity(json, clazz, false);\n")
				.append("  }\n")
				.append("\n")
				.append("  public static <T> T getEntityFromBase64(String json, Class<T> clazz) throws Exception {\n")
				.append("    return getEntity(json, clazz, true);\n")
				.append("  }\n")
				.append("\n")
				.append("  public static Map<String, Object> getMap(String json) throws Exception {\n")
				.append("    return getEntity(json, Map.class, false);\n")
				.append("  }\n")
				.append("\n")
				.append("  public static Map<String, Object> getMapFromBase64(String json) throws Exception {\n")
				.append("    return getEntity(json, Map.class, true);\n")
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
				.append("}");
		return builder.toString();
	}
	
	private void clear() {
		builder.delete(0, builder.length());
	}
	
	public String firstUpperCase(String content) {
		return content.toUpperCase().charAt(0) + content.substring(1);
	}
	
	private String splitByUpperCaseAndAddDot(String content) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0, j = content.length(); i < j; i++) {
			char ch = content.charAt(i);
			if (ch >= 'A' && ch <= 'Z') {
				builder.append(i == 0 ? "" : ".").append((char)(ch + 32));
			} else {
				builder.append(ch);
			}
		}
		return builder.toString();
	}

	private boolean isEmpty(Object str) {
		return str == null || "".equals(str);
	}
	
}
