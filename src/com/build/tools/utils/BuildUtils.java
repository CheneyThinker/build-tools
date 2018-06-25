package com.build.tools.utils;

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

}
