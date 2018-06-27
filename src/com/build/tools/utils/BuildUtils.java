package com.build.tools.utils;

import java.awt.Font;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public final class BuildUtils {
	
	public static boolean isEmpty(Object str) {
		return str == null || "".equals(str);
	}
	
	public static String splitByUpperCaseAndAddDot(String content) {
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

	public static void readLocal(Class<?> clazz, byte[] buffer, int readBytes, String path, String fileName) throws Exception {
		URL url = clazz.getResource(fileName);
        URLConnection urlConnection = url.openConnection();
		InputStream is = urlConnection.getInputStream();
		OutputStream os = new FileOutputStream(path + "/" + fileName);
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
