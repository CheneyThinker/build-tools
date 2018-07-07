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
		builder
		.append("(function($) {\n")
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
	
	public String getRequestJS(String projectName, String port, boolean model, boolean conversion) {
		clear();
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			ip = "localhost";
		}
		builder
		.append("$.base64.utf8encode = true\n")
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
		.append("}\n")
		.append("\n")
		.append("function uploadImage(inputTarget, imageTarget) {\n")
		.append("  var files = inputTarget.files;\n")
		.append("  var file = files[0];\n")
		.append("  var reader = new FileReader();\n")
		.append("  reader.onload = function() {\n")
		.append("    document.getElementById(imageTarget.id).src = this.result;\n")
		.append("    localStorage.setItem(imageTarget.id, this.result);\n")
		.append("  };\n")
		.append("  reader.readAsDataURL(file);\n")
		.append("  //reader.readAsBinaryString(file);\n")
		.append("  //reader.readAsText(file, 'UTF-8');\n")
		.append("  //reader.reader.readAsArrayBuffer(file);\n")
		.append("}");
		return builder.toString();
	}
	
	public String getHtml(String projectName, String author, boolean model, boolean personal, String jQuery, boolean conversion) {
		clear();
		builder
		.append("<!DOCTYPE html>\n")
		.append("<html>\n")
		.append("  <head>\n")
		.append("    <meta charset='utf-8'>\n")
		.append("    <title>").append(projectName).append("</title>\n")
		.append("    <style>\n")
		.append("      table {\n")
		.append("        border-collapse: collapse;\n")
		.append("        text-align: center;\n")
		.append("        word-wrap: break-word;\n")
		.append("        word-break: break-all;\n")
		.append("        font-size: 0.5pt;\n")
		.append("      }\n")
		.append("      thead {\n")
		.append("        background-color: #E9F3FF;\n")
		.append("      }\n")
		.append("      .key {\n")
		.append("        width: 300px;\n")
		.append("      }\n")
		.append("      .log {\n")
		.append("        width: 60px;\n")
		.append("      }\n")
		.append("    </style>\n")
		.append("  </head>\n")
		.append("  <body>\n")
		.append("    <center>\n")
		.append("      <image id=\"fileThumbnail\" />\n")
		.append("      <input type=\"file\" id=\"file\" onchange=\"uploadImage(this, fileThumbnail);\" />\n")
		.append("      <input type=\"submit\" id=\"submit\" name=\"submit\" />\n")
		.append("    </center>\n")
		.append("    <h1><center>Welcome to ").append(projectName).append("</center></h1>\n")
		.append("    <table border=\"1\" align=\"center\" id=\"project\">\n")
		.append("    </table>\n");
		if (personal) {
			builder
			.append("    <h1><center>Inter Of ").append(projectName).append("</center></h1>\n")
			.append("    <table border=\"1\" align=\"center\">\n")
			.append("      <thead>\n")
			.append("        <tr>\n")
			.append("          <th class='key'>key</th>\n")
			.append("          <th>value</th>\n")
			.append("        </tr>\n")
			.append("      </thead>\n")
			.append("      <tbody id=\"inter\">\n")
			.append("      </tbody>\n")
			.append("    </table>\n")
			.append("    <h1><center>Cons Of ").append(projectName).append("</center></h1>\n")
			.append("    <table border=\"1\" align=\"center\">\n")
			.append("      <thead>\n")
			.append("        <tr>\n")
			.append("          <th class='key'>key</th>\n")
			.append("          <th>value</th>\n")
			.append("        </tr>\n")
			.append("      </thead>\n")
			.append("      <tbody id=\"cons\">\n")
			.append("      </tbody>\n")
			.append("    </table>\n")
			.append("    <h1><center>LogInfo Of ").append(projectName).append("</center></h1>\n")
			.append("    <table border=\"1\" align=\"center\">\n")
			.append("      <thead>\n")
			.append("        <tr>\n")
			.append("          <th class='key'>fileName</th>\n")
			.append("          <th>fileSize</th>\n")
			.append("        </tr>\n")
			.append("      </thead>\n")
			.append("      <tbody id=\"logInfo\">\n")
			.append("      </tbody>\n")
			.append("    </table>\n")
			.append("    <h1><center>Logs Of ").append(projectName).append("</center></h1>\n")
			.append("    <table border=\"1\" align=\"center\">\n")
			.append("      <thead>\n")
			.append("        <tr>\n")
			.append("          <th class='log'>index</th>\n")
			.append("          <th>log</th>\n")
			.append("        </tr>\n")
			.append("      </thead>\n")
			.append("      <tbody id=\"log\">\n")
			.append("      </tbody>\n")
			.append("    </table>\n");
		}
		builder
		.append("  </body>\n")
		.append("  <!--<script src=\"http://code.jquery.com/jquery-latest.min.js\"></script>-->\n")
		.append("  <script src=\"jquery-").append(jQuery).append(".min.js\"></script>\n")
		.append("  <script src=\"jquery.base64.js\"></script>\n")
		.append("  <script src=\"md5.js\"></script>\n")
		.append("  <script src=\"request.js\"></script>\n")
		.append("  <script>\n")
		.append("    $(\n")
		.append("      function() {\n")
		.append("        $('#submit').click(function() {\n")
		.append("          var content = localStorage.getItem('fileThumbnail');\n")
		.append("          postJson\n")
		.append("          (\n")
		.append("            {\n")
		.append("              methodOf").append(projectName).append(": 'upload',\n")
		.append("              content: content\n")
		.append("            },\n")
		.append("            function(res) {\n")
		.append("              localStorage.clear();\n")
		.append("            }\n")
		.append("          )\n")
		.append("        })\n")
		.append("\n")
		.append("        //postBase64Json\n")
		.append("        postJson\n")
		.append("        (\n");
		if (model) {
			builder
			.append("          {\n")
			.append("            methodOf").append(projectName).append(": 'index',\n");
		} else {
			builder
			.append("          'index',\n")
			.append("          {\n");
		}
		builder
		.append("            sign: hex_md5('").append(projectName).append(" By ").append(author).append("'),\n")
		.append("            author: '").append(author).append("',\n")
		.append("            fileName: '").append(projectName).append(".log'\n")
		.append("          },\n")
		.append("          function(data) {\n")
		.append("            var projectKey = '', projectValue = ''\n")
		.append("            for(var key in data) {\n")
		.append("              var value = data[key]\n");
		if (personal) {
			builder
			.append("              if (typeof value == 'object') {\n")
			.append("                for(var itemKey in value) {\n")
			.append("                  var item = '<td>' + itemKey + '</td>'\n")
			.append("                           + '<td>' + value[itemKey] + '</td>'\n")
			.append("                  $('#' + key).prepend('<tr>' + item + '</tr>')\n")
			.append("                }\n")
			.append("              } else {\n")
			.append("  ");
		}
		builder
		.append("              projectKey = projectKey\n")
		.append("                         + '<th>' + key + '</th>'\n")
		.append("              projectValue = projectValue\n")
		.append("                           + '<td>' + value + '</td>'\n")
		.append(personal ? "              }\n" : "")
		.append("            }\n")
		.append("             $('#project').append('<thead><tr>' + projectKey + '</tr></thead').append('<tbody><tr>' + projectValue + '</tr></tbody>')\n")
		.append("          }\n")
		.append("        )\n")
		.append("      }\n")
		.append("    )\n")
		.append("  </script>\n")
		.append("</html>");
		return builder.toString();
	}
	
	public String getPom(String projectName, String packageName, String[] outerJar, String[] wsdlJars, boolean sourceOfWsdl, boolean personal, boolean lombok, boolean xml) {
		clear();
		builder
		.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
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
		if (personal) {
			builder
			.append("    <dependency>\n")
		   	.append("      <groupId>org.springframework.boot</groupId>\n")
		   	.append("      <artifactId>spring-boot-configuration-processor</artifactId>\n")
		   	.append("      <optional>true</optional>\n")
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
		if (!BuildUtils.isEmpty(wsdlJars) && !sourceOfWsdl) {
			for (String string : wsdlJars) {
				builder
				.append("    <dependency>\n")
				.append("      <groupId>").append(string).append("</groupId>\n")
				.append("      <artifactId>").append(string.replaceAll("\\.", "-")).append("</artifactId>\n")
				.append("      <version>1.0</version>\n")
				.append("      <scope>system</scope>\n")
				.append("      <systemPath>${pom.basedir}/src/main/resources/lib/").append(string.replaceAll("\\.", "-")).append("-1.0.jar</systemPath>\n")
				.append("    </dependency>\n");
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
	
	public String getConfig(String port, String projectName, boolean model, boolean personal) {
		clear();
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			ip = "localhost";
		}
		builder
		.append("server:\n")
		.append("  port: ").append(port);
		if (personal) {
			builder
			.append("\n")
			.append(splitByUpperCaseAndAddLine(projectName)).append(":\n")
			.append("  cons: #常量定义\n")
			.append("    projectName: ").append(projectName).append("\n")
			.append("    version: PRO\n")
			.append("    major: 1.0\n")
			.append("  inter: #第三方接口定义\n")
			.append("    index: http://").append(ip).append(":").append(port).append("/").append(projectName).append(model ? "/invoke" : "/");
			//.append("\n")
			//.append("logging:\n")
			//.append("  level:\n")
			//.append("    com.").append(packageName).append(".filter: error\n")
			//.append("  file: logs\\").append(projectName).append(".log");
		}
		return builder.toString();
	}
	
	public String getApplication(String projectName, String packageName, String author) {
		clear();
		builder
		.append("package com.").append(packageName).append(";\n")
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
	
	public String getController(String projectName, String packageName, String author, boolean model) {
		clear();
		builder
		.append("package com.").append(packageName).append(".controller;\n")
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
			builder
			.append("  @RequestMapping(\"/invoke\")\n")
			.append("  public Response invoke(HttpServletRequest request) {\n")
			.append("    try {\n")
			.append("      return ResponseGenerator.genYes(").append(projectName).append("Utils.invoke(request.getAttribute(\"data\"), service));\n")
			.append("    } catch (Exception e) {\n")
			.append("      return ResponseGenerator.genNo(logger, e, ").append(projectName).append("Mapping.INVOKE);\n")
			.append("    }\n")
			.append("  }\n");
		} else {
			builder
			.append("  @RequestMapping(\"/index\")\n")
			.append("  public Response index(HttpServletRequest request) {\n")
			.append("    try {\n")
			.append("      return ResponseGenerator.genYes(service.index(request.getAttribute(\"data\")));\n")
			.append("    } catch (Exception e) {\n")
			.append("      return ResponseGenerator.genNo(logger, e, ").append(projectName).append("Mapping.INDEX);\n")
			.append("    }\n")
			.append("  }\n");
		}
		builder
		.append("\n")
		.append("}");
		return builder.toString();
	}
	
	public String getResponse(String projectName, String packageName, String author) {
		clear();
		builder
		.append("package com.").append(packageName).append(".core;\n")
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
		builder
		.append("package com.").append(packageName).append(".core;\n")
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
		builder
		.append("package com.").append(packageName).append(".core;\n")
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
		.append("  private static final String format = \"class:{}\\tmethod:{}\\tline:{}\";\n")
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
		builder
		.append("package com.").append(packageName).append(".mapping;\n")
		.append("\n")
		.append("/**\n")
		.append(" * @description\n")
		.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
		.append(" * @date ").append(date).append("\n")
		.append(" */\n")
		.append("public enum ").append(projectName).append("Mapping {\n")
		.append("\n")
		.append(model ? "  INVOKE(\"Invocation invoke fail!\"),\n" : "  INDEX(\"Invocation index fail!\"),\n")
		.append("  FILTER(\"Invocation filter fail!\");\n")
		.append("\n")
		//.append("  MODEL(\"application/x-www-form-urlencoded\"),\n")
		//.append("  JSON(\"application/json\");\n")
		//.append("\n")
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
		.append("}");
		return builder.toString();
	}
	
	public String getService(String projectName, String packageName, String author, boolean model, boolean personal, boolean conversion) {
		clear();
		builder
		.append("package com.").append(packageName).append(".service;\n")
		.append("\n");
		if (conversion) {
			builder
			.append("import com.").append(packageName).append(".entity.").append(projectName).append(";\n")
			.append("import com.").append(packageName).append(".entity.").append(projectName).append("Entity;\n");
		}
		builder
		.append("import com.").append(packageName).append(".utils.").append(projectName).append("Utils;\n");
		if (model) {
			if (personal) {
				builder
				.append("import com.").append(packageName).append(".config.").append(projectName).append("YMLConfig;\n");
			}
			builder
			.append("import org.springframework.stereotype.Service;\n")
			.append("\n")
			.append(conversion ? "import java.util.HashMap;\n" : "")
			.append("//import java.io.BufferedOutputStream;\n")
			.append("//import java.io.DataOutputStream;\n")
			.append("//import java.io.FileOutputStream;\n");
		}
		builder
		.append("import java.util.Map;\n")
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
			String firstLowerCase = firstLowerCase(projectName);
			builder
			.append("  public Map<String, Object> upload(Map<String, Object> map) throws Exception {\n")
			.append("    String path = System.getProperty(\"user.dir\").concat(\"\\\\upload\\\\temp.png\"));\n")
			.append("    String content = (String) map.get(\"content\");\n")
			.append("    int len = content.indexOf(\",\");\n")
			.append("    ").append(projectName).append("Utils.base64ToImage(content.substring(len + 1), path);\n")
			.append("    /*String binaryString = (String) map.get(\"content\");\n")
			.append("    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path)));\n")
			.append("    out.writeBytes(binaryString);\n")
			.append("    out.close();*/\n")
			.append("    return map;\n")
			.append("  }\n")
			.append("\n")
			.append("  public Map<String, Object> index(").append(conversion ? projectName.concat(" ").concat(firstLowerCase) : "Map<String, Object> map").append(") throws Exception {\n")
			.append(conversion ? "    ".concat(projectName).concat("Entity ").concat(firstLowerCase).concat("Entity = ").concat(projectName).concat("Utils.conversion(").concat(firstLowerCase).concat(");\n") : "")
			.append("    if (").append(conversion ? firstLowerCase.concat("Entity.getSign()") : "map.get(\"sign\")").append(".equals(").append(projectName).append("Utils.md5(\"").append(projectName).append(" By ").append(author).append("\"))) {\n");
			if (personal) {
				builder
				.append(conversion ? "      Map<String, Object> map = new HashMap<>();\n" : "")
				.append("      ").append(projectName).append("YMLConfig ").append(firstLowerCase).append("YMLConfig = ").append(projectName).append("Utils.get").append(projectName).append("YMLConfig();\n")
				.append("      ").append(projectName).append("YMLConfig.Cons cons = ").append(firstLowerCase).append("YMLConfig.getCons();\n")
				.append("      map.put(\"projectName\", cons.getProjectName());\n")
				.append("      map.put(\"version\", cons.getVersion());\n")
				.append("      map.put(\"major\", cons.getMajor());\n")
				.append("      map.put(\"cons\", cons);\n")
				.append("      map.put(\"inter\", ").append(firstLowerCase).append("YMLConfig.getInter());\n");
			} else {
				builder
				.append("      map.put(\"projectName\", \"").append(projectName).append("\");\n")
				.append("      map.put(\"version\", \"PRO\");\n")
				.append("      map.put(\"major\", \"1.0\");\n");
			}
			builder
			.append("      map.put(\"log\", ").append(projectName).append("Utils.readLog(").append(conversion ? firstLowerCase.concat("Entity.getFileName()") : "map.remove(\"fileName\")").append("));\n")
			.append("      map.put(\"logInfo\", ").append(projectName).append("Utils.getLogInfo());\n");
			if (conversion) {
				builder
				.append("      map.put(\"methodOf").append(projectName).append("\", ").append(firstLowerCase).append("Entity.getMethodOf").append(projectName).append("());\n")
				.append("      map.put(\"sign\", ").append(firstLowerCase).append("Entity.getSign());\n")
				.append("      map.put(\"author\", ").append(firstLowerCase).append("Entity.getAuthor());\n")
				.append("      map.put(\"fileName\", ").append(firstLowerCase).append("Entity.getFileName());\n");
			}
			builder
			.append("      return map;\n")
			.append("    } else {\n")
			.append("      return null;\n")
			.append("    }\n")
			.append("  }\n");
		} else {
			builder
			.append("  Map<String, Object> index(Object object) throws Exception;\n");
		}
		builder
		.append("\n")
		.append("}");
		return builder.toString();
	}
	
	public String getServiceImpl(String projectName, String packageName, String author, boolean personal) {
		clear();
		builder
		.append("package com.").append(packageName).append(".service.impl;\n")
		.append("\n")
		.append("import com.").append(packageName).append(".service.").append(projectName).append("Service;\n");
		if (personal) {
			builder
			.append("import com.").append(packageName).append(".config.").append(projectName).append("YMLConfig;\n");
		}
		builder
		.append("import com.").append(packageName).append(".utils.").append(projectName).append("Utils;\n")
		.append("import org.springframework.stereotype.Service;\n")
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
		.append("  public Map<String, Object> index(Object object) throws Exception {\n")
		.append("    Map<String, Object> map = ").append(projectName).append("Utils.conversion(object);\n")
		.append("    if (map.get(\"sign\").equals(").append(projectName).append("Utils.md5(\"").append(projectName).append(" By ").append(author).append("\"))) {\n");
		if (personal) {
			String firstLowerCase = firstLowerCase(projectName);
			builder
			.append("      ").append(projectName).append("YMLConfig ").append(firstLowerCase).append("YMLConfig = ").append(projectName).append("Utils.get").append(projectName).append("YMLConfig();\n")
			.append("      ").append(projectName).append("YMLConfig.Cons cons = ").append(firstLowerCase).append("YMLConfig.getCons();\n")
			.append("      map.put(\"projectName\", cons.getProjectName());\n")
			.append("      map.put(\"version\", cons.getVersion());\n")
			.append("      map.put(\"major\", cons.getMajor());\n")
			.append("      map.put(\"cons\", cons);\n")
			.append("      map.put(\"inter\", ").append(firstLowerCase).append("YMLConfig.getInter());\n");
		} else {
			builder
			.append("      map.put(\"projectName\", \"").append(projectName).append("\");\n")
			.append("      map.put(\"version\", \"PRO\");\n")
			.append("      map.put(\"major\", \"1.0\");\n");
		}
		builder
		.append("      map.put(\"log\", ").append(projectName).append("Utils.readLog(map.remove(\"fileName\")));\n")
		.append("      map.put(\"logInfo\", ").append(projectName).append("Utils.getLogInfo());\n")
		.append("      return map;\n")
		.append("    } else {\n")
		.append("      return null;\n")
		.append("    }\n")
		.append("  }\n")
		.append("\n")
		.append("}");
		return builder.toString();
	}
	
	public String getFilter(String projectName, String packageName, String author, boolean personal, boolean xml, boolean conversion) {
		clear();
		builder
		.append("package com.").append(packageName).append(".filter;\n")
		.append("\n");
		if (personal) {
			builder
			.append("import com.").append(packageName).append(".config.").append(projectName).append("YMLConfig;\n");
		}
		builder
		.append("import com.").append(packageName).append(".core.ResponseGenerator;\n")
		.append("import com.").append(packageName).append(".mapping.").append(projectName).append("Mapping;\n")
		.append("import com.").append(packageName).append(".utils.").append(projectName).append("Utils;\n")
		.append("import org.slf4j.Logger;\n")
		.append("import org.slf4j.LoggerFactory;\n");
		if (personal) {
			builder
			.append("import org.springframework.beans.factory.annotation.Autowired;\n")
			.append("import org.springframework.web.client.RestTemplate;\n");
		}
		builder
		.append("import org.springframework.util.StringUtils;\n")
		.append("\n")
		.append("import javax.servlet.*;\n")
		.append("import javax.servlet.annotation.WebFilter;\n")
		.append("import javax.servlet.http.HttpServletRequest;\n")
		.append("import javax.servlet.http.HttpServletResponse;\n")
		.append("import java.io.IOException;\n")
		.append(conversion ? "" : "import java.util.Map;\n")
		.append("\n")
		.append("/**\n")
		.append(" * @description\n")
		.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
		.append(" * @date ").append(date).append("\n")
		.append(" */\n")
		.append("@WebFilter(\"/").append(projectName).append("/*\")\n")
		.append("public class ").append(projectName).append("Filter implements Filter {\n")
		.append("\n");
		if (personal) {
			builder
			.append("  @Autowired\n")
			.append("  private ").append(projectName).append("YMLConfig ").append(firstLowerCase(projectName)).append("YMLConfig;\n")
			.append("  @Autowired\n")
			.append("  private RestTemplate restTemplate;\n");
		}
		builder
		.append("  private Logger logger = LoggerFactory.getLogger(").append(projectName).append("Filter.class);\n")
		.append("\n")
		.append("  public void init(FilterConfig filterConfig) {\n")
		.append("  }\n")
		.append("\n")
		.append("  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {\n")
		.append("    HttpServletRequest request = (HttpServletRequest) servletRequest;\n")
		.append("    HttpServletResponse response = (HttpServletResponse) servletResponse;\n")
		.append("    response.setHeader(\"Access-Control-Allow-Origin\", \"*\");\n")
		.append("    response.setCharacterEncoding(\"UTF-8\");\n")
		.append("    response.setContentType(\"application/json; charset=UTF-8\");\n")
		.append("    try {\n");
		if (personal) {
			builder
			.append("      ").append(projectName).append("Utils.install").append(projectName).append("YMLConfig(").append(firstLowerCase(projectName)).append("YMLConfig);\n")
			.append("      ").append(projectName).append("Utils.installRestTemplate(restTemplate);\n");
		}
		builder
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
		.append("      String parameter = request.getParameter(\"data\");\n")
		.append("      logger.error(parameter);\n");
		if (conversion) {
			builder
			.append("      request.setAttribute(\"data\", ").append(projectName).append("Utils.getEntity(parameter, Class.forName(\"com.").append(packageName).append(".entity.\".concat(request.getParameter(\"model\"))), false));\n");
		} else {
			builder
			.append("      //Map<String, Object> map = ").append(projectName).append("Utils.getMapFromBase64(parameter").append(xml ? ", false" : "").append(");\n")
			.append("      Map<String, Object> map = ").append(projectName).append("Utils.getMap(parameter").append(xml ? ", false" : "").append(");\n")
			.append("      /*if (map.containsKey(\"authToken\") && map.containsKey(\"systemId\")) {\n")
			.append("        String authToken = (String) map.remove(\"authToken\");\n")
			.append("        String systemId = (String) map.remove(\"systemId\");\n")
			.append("      }*/\n")
			.append("      request.setAttribute(\"data\", map);\n");
		}
		builder
		.append("      filterChain.doFilter(request, response);\n")
		.append("    } catch (Exception e) {\n")
		.append("      ServletOutputStream out = null;\n")
		.append("      try {\n")
		.append("        out = response.getOutputStream();\n")
		.append("        String result = ").append(projectName).append("Utils.toJson(ResponseGenerator.genNo(logger, e, ").append(projectName).append("Mapping.FILTER));\n")
		.append("        logger.error(result);\n")
		.append("        out.write(result.getBytes(\"UTF-8\"));\n")
		.append("        out.flush();\n")
		.append("      } catch (IOException ex) {\n")
		.append("        ex.printStackTrace();\n")
		.append("      } finally {\n")
		.append("        if (!StringUtils.isEmpty(out)) {\n")
		.append("          try {\n")
		.append("            out.close();\n")
		.append("          } catch (IOException exe) {\n")
		.append("            exe.printStackTrace();\n")
		.append("          }\n")
		.append("        }\n")
		.append("      }\n")
		.append("    }\n")
		.append("  }\n")
		.append("\n")
		.append("  public void destroy() {\n")
		.append("  }\n")
		.append("\n")
		.append("}");
		return builder.toString();
	}
	
	public String getUtils(String projectName, String packageName, String author, boolean model, boolean personal, boolean xml, boolean conversion) {
		clear();
		builder
		.append("package com.").append(packageName).append(".utils;\n")
		.append("\n");
		if (conversion) {
			builder
			.append("import com.").append(packageName).append(".entity.").append(projectName).append(";\n");
		}
		builder
		.append("import com.fasterxml.jackson.databind.ObjectMapper;\n")
		.append("import com.fasterxml.jackson.databind.SerializationFeature;\n")
		.append(xml ? "import com.fasterxml.jackson.dataformat.xml.XmlMapper;\n" : "");
		if (personal) {
			builder
			.append("import com.").append(packageName).append(".config.").append(projectName).append("YMLConfig;\n")
			.append("import org.springframework.http.HttpEntity;\n")
			.append("import org.springframework.http.HttpHeaders;\n")
			.append("import org.springframework.http.MediaType;\n")
			.append("import org.springframework.util.LinkedMultiValueMap;\n")
			.append("import org.springframework.util.MultiValueMap;\n");
		}
		builder
		.append("import org.springframework.util.StringUtils;\n");
		if (personal) {
			builder
			.append("import org.springframework.web.client.RestTemplate;\n");
		}
		builder
		.append("import sun.misc.BASE64Decoder;\n")
		.append("\n")
		.append("import java.io.*;\n")
		.append(model ? "import java.lang.reflect.Method;\n" : "")
		.append("import java.security.MessageDigest;\n")
		.append("import java.security.NoSuchAlgorithmException;\n")
		.append("import java.util.Base64;\n")
		.append("import java.util.HashMap;\n")
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
		.append("\n");
		String firstLowerCase = firstLowerCase(projectName);
		if (personal) {
			builder
			.append("  private static ").append(projectName).append("YMLConfig ").append(firstLowerCase).append("YMLConfig;\n")
			.append("  private static RestTemplate restTemplate;\n");
		}
		builder
		.append("  private static ObjectMapper mapper = new ObjectMapper();\n")
		.append(xml ? "  private static XmlMapper xmlMapper = new XmlMapper();\n" : "")
		.append("\n");
		if (personal) {
			builder
			.append("  public static void installRestTemplate(RestTemplate _restTemplate) {\n")
			.append("    if (StringUtils.isEmpty(restTemplate)) {\n")
			.append("      restTemplate = _restTemplate;\n")
			.append("    }\n")
			.append("  }\n")
			.append("\n")
			.append("  public static void install").append(projectName).append("YMLConfig(").append(projectName).append("YMLConfig _").append(firstLowerCase).append("YMLConfig) {\n")
			.append("    if (StringUtils.isEmpty(").append(firstLowerCase).append("YMLConfig)) {\n")
			.append("      ").append(firstLowerCase).append("YMLConfig = _").append(firstLowerCase).append("YMLConfig;\n")
			.append("    }\n")
			.append("  }\n")
			.append("\n")
			.append("  public static ").append(projectName).append("YMLConfig get").append(projectName).append("YMLConfig() {\n")
			.append("    return ").append(firstLowerCase).append("YMLConfig;\n")
			.append("  }\n")
			.append("\n");
		}
		if (model) {
			if (conversion) {
				builder
				.append("  public static Object invoke(Object data, Object service) throws Exception {\n")
				.append("    ").append(projectName).append(" ").append(firstLowerCase(projectName)).append(" = conversion(data);\n")
				.append("    Method method = service.getClass().getMethod(").append(firstLowerCase(projectName)).append(".getMethodOf").append(projectName).append("(), ").append(projectName).append(".class);\n")
				.append("    return method.invoke(service, ").append(firstLowerCase(projectName)).append(");\n")
				.append("  }\n")
				.append("\n");
			} else {
				builder
				.append("  public static Object invoke(Object data, Object service) throws Exception {\n")
				.append("    Map<String, Object> map = conversion(data);\n")
				.append("    Method method = service.getClass().getMethod((String) map.").append(personal ? "get" : "remove").append("(\"methodOf").append(projectName).append("\"), Map.class);\n")
				.append("    return method.invoke(service, map);\n")
				.append("  }\n")
				.append("\n");
			}
		}
		builder
		.append("  public static void base64ToImage(String base64, String filePath) {\n")
		.append("    if (!StringUtils.isEmpty(base64)) {\n")
		.append("      BASE64Decoder decoder = new BASE64Decoder();\n")
		.append("      OutputStream out = null;\n")
		.append("      try {\n")
		.append("        byte[] bytes = decoder.decodeBuffer(base64);\n")
		.append("        for (int i = 0, j = bytes.length; i < j; ++i) {\n")
		.append("          if (bytes[i] < 0) {\n")
		.append("            bytes[i] += 256;\n")
		.append("          }\n")
		.append("        }\n")
		.append("        out = new FileOutputStream(filePath);\n")
		.append("        out.write(bytes);\n")
		.append("        out.flush();\n")
		.append("      } catch (IOException e) {\n")
		.append("      } finally {\n")
		.append("        try {\n")
		.append("          out.close();\n")
		.append("        } catch (IOException e) {\n")
		.append("        }\n")
		.append("      }\n")
		.append("    }\n")
		.append("  }\n")
		.append("\n")
		.append("  public static <T> T getEntity(String content, Class<T> clazz").append(xml ? ", boolean xml" : "").append(") throws Exception {\n")
		.append("    return getEntity(content, clazz, false").append(xml ? ", xml" : "").append(");\n")
		.append("  }\n")
		.append("\n")
		.append("  public static <T> T getEntityFromBase64(String content, Class<T> clazz").append(xml ? ", boolean xml" : "").append(") throws Exception {\n")
		.append("    return getEntity(content, clazz, true").append(xml ? ", xml" : "").append(");\n")
		.append("  }\n")
		.append("\n")
		.append("  public static Map<String, Object> getMap(String content").append(xml ? ", boolean xml" : "").append(") throws Exception {\n")
		.append("    return getEntity(content, Map.class").append(xml ? ", xml" : "").append(");\n")
		.append("  }\n")
		.append("\n")
		.append("  public static Map<String, Object> getMapFromBase64(String content").append(xml ? ", boolean xml" : "").append(") throws Exception {\n")
		.append("    return getEntityFromBase64(content, Map.class").append(xml ? ", xml" : "").append(");\n")
		.append("  }\n")
		.append("\n")
		.append("  private static <T> T getEntity(String content, Class<T> clazz, boolean fromBase64").append(xml ? ", boolean xml" : "").append(") throws Exception {\n")
		.append("    if (StringUtils.isEmpty(content)) {\n")
		.append("      throw new Exception(\"Argument Not Allowed Empty!\");\n")
		.append("    }\n")
		.append("    if (fromBase64) {\n")
		.append("      byte[] bytes = Base64.getDecoder().decode(content);\n")
		.append("      content = new String(bytes, \"UTF-8\");\n")
		.append("    }\n");
		if (xml) {
			builder
			.append("    if (xml) {\n")
			.append("      try {\n")
			.append("        return xmlMapper.readValue(content, clazz);\n")
			.append("      } catch (Exception e) {\n")
			.append("        return null;\n")
			.append("      }\n")
			.append("    }\n");
		}
		builder
		.append("    return mapper.readValue(content, clazz);\n")
		.append("  }\n")
		.append("\n");
		if (personal) {
			builder
			.append("  public static <T> T get(String url, Class<T> clazz) {\n")
			.append("    return restTemplate.getForObject(url, clazz);\n")
			.append("  }\n")
			.append("\n")
			.append("  public static <T> T post(").append(model ? "" : "String url, ").append("Map<String, Object> params, Class<T> clazz) {\n")
			.append("    HttpHeaders headers = new HttpHeaders();\n")
			.append("    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);\n")
			.append("    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();\n")
			.append(model ? "    String url = ".concat(firstLowerCase).concat("YMLConfig.getInter().get(params.remove(\"methodOf").concat(projectName).concat("\"));\n") : "")
			.append("    map.setAll(params);\n")
			.append("    HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(map, headers);\n")
			.append("    return restTemplate.postForObject(url, formEntity, clazz);\n")
			.append("  }\n")
			.append("\n")
			.append("  public static <T> T postJson(").append(model ? "" : "String url, ").append("Map<String, Object> params, Class<T> clazz) {\n")
			.append("    HttpHeaders headers = new HttpHeaders();\n")
			.append("    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);\n")
			.append("    return restTemplate.postForObject(").append(model ? firstLowerCase.concat("YMLConfig.getInter().get(params.remove(\"methodOf").concat(projectName).concat("\"))") : "url").append(", new HttpEntity<String>(toJson(params), headers), clazz);\n")
			.append("  }\n")
			.append("\n");
		}
		builder
		.append("  public static String toJson(Object value) {\n")
		.append("    try {\n")
		.append("      mapper.enable(SerializationFeature.INDENT_OUTPUT);\n")
		.append("      return mapper.writeValueAsString(value);\n")
		.append("    } catch (Exception e) {\n")
		.append("      //It's impossible to happen\n")
		.append("      return null;\n")
		.append("    }\n")
		.append("  }\n")
		.append("\n");
		if (xml) {
			builder
			.append("  public static String toXml(Object value) {\n")
			.append("    try {\n")
			.append("      xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);\n")
			.append("      return xmlMapper.writeValueAsString(value);\n")
			.append("    } catch (Exception e) {\n")
			.append("      //It's impossible to happen\n")
			.append("      return null;\n")
			.append("    }\n")
			.append("  }\n")
			.append("\n");
		}
		builder
		.append("  public static <T> T conversion(Object value) {\n")
		.append("    return (T) value;\n")
		.append("  }\n")
		.append("\n")
		.append("  public static String md5(String content) {\n")
		.append("    try {\n")
		.append("      MessageDigest messageDigest = MessageDigest.getInstance(\"MD5\");\n")
		.append("      byte[] bytes = messageDigest.digest(content.getBytes());\n")
		.append("      StringBuffer buffer = new StringBuffer();\n")
		.append("      for (byte b : bytes) {\n")
		.append("        int number = b & 0xff;\n")
		.append("        String str = Integer.toHexString(number);\n")
		.append("        if (str.length() == 1) {\n")
		.append("          buffer.append(\"0\");\n")
		.append("        }\n")
		.append("        buffer.append(str);\n")
		.append("      }\n")
		.append("      return buffer.toString();\n")
		.append("    } catch (NoSuchAlgorithmException e) {\n")
		.append("      return \"\";\n")
		.append("    }\n")
		.append("  }\n")
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
		.append("\n")
		.append("  public static Map<Short, String> readLog(Object fileName) {\n")
		.append("    try {\n")
		.append("      BufferedReader reader = new BufferedReader(new FileReader(new File(\"logs/\".concat(fileName.toString()))));\n")
		.append("      String temp;\n")
		.append("      Map<Short, String> map = new HashMap<>();\n")
		.append("      short i = 0;\n")
		.append("      while (!StringUtils.isEmpty((temp = reader.readLine()))) {\n")
		.append("        map.put(++i, temp);\n")
		.append("      }\n")
		.append("      return map;\n")
		.append("    } catch (IOException e) {\n")
		.append("      return null;\n")
		.append("    }\n")
		.append("  }\n")
		.append("\n")
		.append("  public static Map<String, Long> getLogInfo() {\n")
		.append("    File file = new File(\"logs\");\n")
		.append("    File[] files = file.listFiles();\n")
		.append("    Map<String, Long> logInfo = new HashMap<>();\n")
		.append("    for (File f : files) {\n")
		.append("      logInfo.put(f.getName(), f.length());\n")
		.append("    }\n")
		.append("    return logInfo;\n")
		.append("  }\n")
		.append("\n")
		.append("}");
		return builder.toString();
	}
	
	public String getRestTemplate(String projectName, String packageName, String author) {
		clear();
		builder
		.append("package com.").append(packageName).append(".config;\n")
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

	public String getYMLConfig(String projectName, String packageName, String author) {
		clear();
		builder
		.append("package com.").append(packageName).append(".config;\n")
		.append("\n")
		.append("import org.springframework.beans.factory.annotation.Autowired;\n")
		.append("import org.springframework.boot.context.properties.ConfigurationProperties;\n")
		.append("import org.springframework.stereotype.Component;\n")
		.append("import org.springframework.stereotype.Service;\n")
		.append("\n")
		.append("import java.util.Map;\n")
		.append("\n")
		.append("/**\n")
		.append(" * @description\n")
		.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
		.append(" * @date ").append(date).append("\n")
		.append(" */\n")
		.append("@Component\n")
		.append("@ConfigurationProperties(prefix = \"").append(splitByUpperCaseAndAddLine(projectName)).append("\")\n")
		.append("public class ").append(projectName).append("YMLConfig {\n")
		.append("\n")
		.append("  @Autowired\n")
		.append("  private Cons cons;\n")
		.append("  private Map<String, String> inter;\n")
		.append("\n")
		.append("  public Cons getCons() {\n")
		.append("    return cons;\n")
		.append("  }\n")
		.append("\n")
		.append("  public void setCons(Cons cons) {\n")
		.append("    this.cons = cons;\n")
		.append("  }\n")
		.append("\n")
		.append("  public Map<String, String> getInter() {\n")
		.append("    return inter;\n")
		.append("  }\n")
		.append("\n")
		.append("  public void setInter(Map<String, String> inter) {\n")
		.append("    this.inter = inter;\n")
		.append("  }\n")
		.append("\n")
		.append("  @Service\n")
		.append("  public class Cons {\n")
		.append("\n")
		.append("    private String projectName;\n")
		.append("    private String version;\n")
		.append("    private String major;\n")
		.append("\n")
		.append("    public String getProjectName() {\n")
		.append("      return projectName;\n")
		.append("    }\n")
		.append("\n")
		.append("    public void setProjectName(String projectName) {\n")
		.append("      this.projectName = projectName;\n")
		.append("    }\n")
		.append("\n")
		.append("    public String getVersion() {\n")
		.append("      return version;\n")
		.append("    }\n")
		.append("\n")
		.append("    public void setVersion(String version) {\n")
		.append("      this.version = version;\n")
		.append("    }\n")
		.append("\n")
		.append("    public String getMajor() {\n")
		.append("      return major;\n")
		.append("    }\n")
		.append("\n")
		.append("    public void setMajor(String major) {\n")
		.append("      this.major = major;\n")
		.append("    }\n")
		.append("\n")
		.append("  }\n")
		.append("\n")
		.append("}");
		return builder.toString();
	}
	
	public String getLog(String projectName, String packageName) {
		clear();
		return
		builder
		.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
		.append("<configuration>\n")
		.append("  <conversionRule conversionWord=\"clr\" converterClass=\"org.springframework.boot.logging.logback.ColorConverter\" />\n")
		.append("  <conversionRule conversionWord=\"wex\" converterClass=\"org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter\" />\n")
		.append("  <conversionRule conversionWord=\"wEx\" converterClass=\"org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter\" />\n")
		.append("  <property name=\"CONSOLE_LOG_PATTERN\" value=\"${CONSOLE_LOG_PATTERN:-%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}\" />\n")
		.append("  <appender name=\"CONSOLE_LOG\" class=\"ch.qos.logback.core.ConsoleAppender\">\n")
		.append("    <encoder>\n")
		//.append("      <!-- %d日期,%p日志级别,%file文件名,%line所在行数,%m输出的信息,%n换行 -->\n")
		.append("      <pattern>${CONSOLE_LOG_PATTERN}</pattern>\n")
		.append("      <charset>UTF-8</charset>\n")
		.append("    </encoder>\n")
		.append("  </appender>\n")
		.append("  <root level=\"info\">\n")
		.append("    <appender-ref ref=\"CONSOLE_LOG\" />\n")
		.append("  </root>\n")
		.append("  <appender name=\"").append(projectName).append("Log\" class=\"ch.qos.logback.core.rolling.RollingFileAppender\">\n")
		.append("    <File>logs/").append(projectName).append(".log</File>\n")
		.append("    <rollingPolicy class=\"ch.qos.logback.core.rolling.TimeBasedRollingPolicy\">\n")
		.append("      <fileNamePattern>logs/").append(projectName).append(".%d.%i.log</fileNamePattern>\n")
		.append("      <timeBasedFileNamingAndTriggeringPolicy class=\"ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP\">\n")
		.append("        <maxFileSize>64 KB</maxFileSize>\n")
		.append("      </timeBasedFileNamingAndTriggeringPolicy>\n")
		.append("    </rollingPolicy>\n")
		.append("    <encoder>\n")
		.append("      <pattern>\n")
		.append("        %d{MM-dd HH:mm:ss} %file %line %m%n\n")
		.append("      </pattern>\n")
		.append("      <charset>UTF-8</charset>\n")
		.append("    </encoder>\n")
		.append("  </appender>\n")
		.append("  <logger name=\"com.").append(packageName).append("\" level=\"ERROR\">\n")
		.append("    <appender-ref ref=\"").append(projectName).append("Log\" />\n")
		.append("  </logger>\n")
		.append("</configuration>").toString();
	}
	
	public String getRootEntity(String projectName, String packageName, String author) {
		clear();
		builder
		.append("package com.").append(packageName).append(".entity;\n")
		.append("\n")
		.append("/**\n")
		.append(" * @description\n")
		.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
		.append(" * @date ").append(date).append("\n")
		.append(" */\n")
		.append("public interface ").append(projectName).append(" {\n")
		.append("\n")
		.append("  String getMethodOf").append(projectName).append("();\n")
		.append("\n")
		.append("}");
		return builder.toString();
	}
	
	public String getProjectEntity(String projectName, String packageName, String author, boolean lombok) {
		clear();
		builder
		.append("package com.").append(packageName).append(".entity;\n")
		.append("\n")
		.append(lombok ? "import lombok.Data;\n\n" : "")
		.append("/**\n")
		.append(" * @description\n")
		.append(" * @author ").append(BuildUtils.isEmpty(author) ? "admin" : author).append("\n")
		.append(" * @date ").append(date).append("\n")
		.append(" */\n")
		.append(lombok ? "@Data\n" : "")
		.append("public class ").append(projectName).append("Entity implements ").append(projectName).append(" {\n")
		.append("\n")
		.append("  private String methodOf").append(projectName).append(";\n")
		.append("  private String sign;\n")
		.append("  private String author;\n")
		.append("  private String fileName;\n");
		if (!lombok) {
			builder
			.append("\n")
			.append("  public String getMethodOf").append(projectName).append("() {\n")
			.append("    return methodOf").append(projectName).append(";\n")
			.append("  }\n")
			.append("\n")
			.append("  public void setMethodOf").append(projectName).append("(String methodOf").append(projectName).append(") {\n")
			.append("    this.methodOf").append(projectName).append(" = methodOf").append(projectName).append(";\n")
			.append("  }\n")
			.append("\n")
			.append("  public String getSign() {\n")
			.append("    return sign;\n")
			.append("  }\n")
			.append("\n")
			.append("  public void setSign(String sign) {\n")
			.append("    this.sign = sign;\n")
			.append("  }\n")
			.append("\n")
			.append("  public String getMethodOfCivilAffairs() {\n")
			.append("    return author;\n")
			.append("  }\n")
			.append("\n")
			.append("  public void setAuthor(String author) {\n")
			.append("    this.author = author;\n")
			.append("  }\n")
			.append("\n")
			.append("  public String getFileName() {\n")
			.append("    return fileName;\n")
			.append("  }\n")
			.append("\n")
			.append("  public void setFileName(String fileName) {\n")
			.append("    this.fileName = fileName;\n")
			.append("  }\n")
			.append("\n");
		}
		builder
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
	
	public String splitByUpperCaseAndAddLine(String content) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0, j = content.length(); i < j; i++) {
			char ch = content.charAt(i);
			if (ch >= 'A' && ch <= 'Z') {
				builder.append(i == 0 ? "" : "-").append((char)(ch + 32));
			} else {
				builder.append(ch);
			}
		}
		return builder.toString();
	}
	
	public String firstLowerCase(String content) {
		return content.toLowerCase().charAt(0) + content.substring(1);
	}
	
}