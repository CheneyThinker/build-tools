package com.build.tools.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public final class StreamGobbler extends Thread {
  
  private InputStream is;
  private String type;
  private OutputStream os;
  
  public StreamGobbler(InputStream is, String type) {
    this(is, type, null);
	}
	
	public StreamGobbler(InputStream is, String type, OutputStream redirect) {
		this.is = is;
		this.type = type;
		this.os = redirect;
	}
	
	public void run() {
		InputStreamReader isr = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			if (os != null)
				pw = new PrintWriter(os);
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line = null;
			while ( (line = br.readLine()) != null) {
				if (pw != null)
					pw.println(line);
				System.out.println(type.concat(":").concat(line));
			}
			if (pw != null)
				pw.flush();
		} catch (IOException ioe) {
		} finally {
			try {
				if (pw != null) {
					pw.close();
				}
				if (br != null) {
					br.close();
				}
				if (isr != null) {
					isr.close();
				}
			} catch (IOException e) {
			}
		}
	}
	
}