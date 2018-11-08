package com.build.tools;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import com.build.tools.content.BuildType;
import com.build.tools.utils.BuildUtils;

public class Template {
  
  private BuildType type;
  
  public Template(BuildType _type) {
    type = _type;
  }
  
  public void writeCzm(String czmFile, String fileName) {
    try {
      String ip;
      try {
    	ip = InetAddress.getLocalHost().getHostAddress();
      } catch (UnknownHostException e) {
    	ip = "127.0.0.1";
      }
      URL url = Template.class.getResource(czmFile.concat(".czm"));
      URLConnection urlConnection = url.openConnection();
      InputStream in = urlConnection.getInputStream();
      byte[] content = new byte[in.available()];
      in.read(content);
      in.close();
      String result = new String(content, "UTF-8");
      result = result.replaceAll("%package%", type.getPackageName());
      result = result.replaceAll("%author%", type.getAuthor());
      result = result.replaceAll("%date%", type.getDate());
      result = result.replaceAll("%project%", type.getProjectName());
      result = result.replaceAll("%prefix%", type.getAddSplitLine());
      result = result.replaceAll("%small%", type.getFirstLowerCase());
      result = result.replaceAll("%port%", type.getPort());
      result = result.replaceAll("%jQuery%", type.getjQuery());
      result = result.replaceAll("%ip%", ip);
      BuildUtils.write(result, fileName);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public BuildType getType() {
    return type;
  }
  
}