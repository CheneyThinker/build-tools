package com.build.tools;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.build.tools.content.BuildType;
import com.build.tools.utils.BuildUtils;
import com.build.tools.utils.StreamGobbler;

/*
cmd /c dir 是执行完dir命令后关闭命令窗口。 
cmd /k dir 是执行完dir命令后不关闭命令窗口。 
cmd /c start dir 会打开一个新窗口后执行dir指令，原窗口会关闭。 
cmd /k start dir 会打开一个新窗口后执行dir指令，原窗口不会关闭。 
 */
public class BuildTools {
	
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame.setDefaultLookAndFeelDecorated(true);
				JDialog.setDefaultLookAndFeelDecorated(true);
				BuildUtils.initGlobalFontSetting(new Font("Consolas", Font.BOLD, 18));
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
					JFrame jFrame = new JFrame("BuildTools");
					Container container = jFrame.getContentPane();
					container.setLayout(null);
					
					String[] items = {"projectName:", "port:", "author:", "outerJar:", "wsdl:", "sourceOfWsdl:", "model:", "personal:", "build"};
					String[] tips = {"CheneyThinker", "9527", "CheneyThinker", "Example:cheney-thinker-1.0$tea-1.0", "Example:cheney.xml$tea.xml", "false,true", "simpliy,common", "false,true"};
					
					int width = 500;
					int height = ((items.length + 1) * 40) + ((items.length + 2) * 5);
					JLabel[] jLabels = new JLabel[items.length - 1];
					JTextField[] jTextFields = new JTextField[items.length - 4];
					for (int i = 0, j = items.length - 4; i < j; i++) {
						jLabels[i] = new JLabel(items[i]);
						jLabels[i].setBounds(5, i * 40 + 5 * (i + 1), 135, 40);
						container.add(jLabels[i]);
						jTextFields[i] = new JTextField(tips[i]);
						jTextFields[i].setBounds(140, i * 40 + 5 * (i + 1), 350, 40);
						container.add(jTextFields[i]);
					}
					int i = items.length - 4, j = items.length - 1;
					JComboBox<?>[] jComboBoxs = new JComboBox<?>[j - i];
					for (int k = 0; i < j; i++, k++) {
						jLabels[i] = new JLabel(items[i]);
						jLabels[i].setBounds(5, i * 40 + 5 * (i + 1), 135, 40);
						container.add(jLabels[i]);
						jComboBoxs[k] = new JComboBox<>(tips[i].split("\\,"));
						jComboBoxs[k].setBounds(140, i * 40 + 5 * (i + 1), 350, 40);
						container.add(jComboBoxs[k]);
					}
					JButton jButton = new JButton(items[items.length - 1]);
					jButton.setBounds(5, (items.length - 1) * 40 + 5 * (items.length + 1), 485, 40);
					jButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								String projectName = jTextFields[0].getText();
								String port = jTextFields[1].getText();
								String author = jTextFields[2].getText();
								String outerJar = jTextFields[3].getText();
								String wsdl = jTextFields[4].getText();
								String sourceOfWsdl = jComboBoxs[0].getSelectedItem().toString();
								String model = jComboBoxs[1].getSelectedItem().toString();
								String personal = jComboBoxs[2].getSelectedItem().toString();
								BuildType type = new BuildType();
								if (outerJar.equals("Example:cheney-thinker-1.0$tea-1.0") || BuildUtils.isEmpty(outerJar)) {
									outerJar = null;
								}
								if (wsdl.equals("Example:cheney.xml$tea.xml") || BuildUtils.isEmpty(wsdl)) {
									wsdl = null;
								}
								if (BuildUtils.isEmpty(projectName)) {
									projectName = "CheneyThinker";
								} else {
									projectName = type.firstUpperCase(projectName);
								}
								if (BuildUtils.isEmpty(port)) {
									port = "9527";
								} else {
									 try {
										 Integer.valueOf(port);
									} catch (Exception ex) {
										port = "9527";
									}
								}
								if (BuildUtils.isEmpty(author)) {
									author = "CheneyThinker";
								}
								File front = new File(projectName + "/" + projectName + "Front");
								front.mkdirs();
								
								File src = new File(projectName + "/" + projectName + "/src/main/java");
								src.mkdirs();
								
								File resources = new File(projectName + "/" + projectName + "/src/main/resources");
								resources.mkdirs();
								
								List<String> strings = splitByUpperCase(projectName);
								String javaPath = projectName + "/" + projectName + "/src/main/java/com";
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
								File service = new File(project.getPath() + "/service");
								service.mkdirs();
								File config = null;
								if (personal.equals("false")) {
									config = new File(project.getPath() + "/config");
									config.mkdirs();
								}
								File filter = new File(project.getPath() + "/filter");
								filter.mkdirs();
							
								String packageName = BuildUtils.splitByUpperCaseAndAddDot(projectName);
								
								write(type.getConfig(port, projectName, model.equals("simpliy"), personal.equals("false")), resources.getPath() + "/application-default.yml");
								write(type.getLog(projectName, packageName), resources.getPath() + "/logback-spring.xml");
							
								write(type.getApplication(projectName, packageName, author), project.getPath() + "/" + projectName + "Application.java");
							
								write(type.getUtils(projectName, packageName, author, model.equals("simpliy"), personal.equals("false")), utils.getPath() + "/" + projectName + "Utils.java");
							
								write(type.getService(projectName, packageName, author, model.equals("simpliy"), personal.equals("false")), service.getPath() + "/" + projectName + "Service.java");
								if (!model.equals("simpliy")) {
									File impl = new File(service.getPath() + "/impl");
									impl.mkdirs();
									write(type.getServiceImpl(projectName, packageName, author, personal.equals("false")), impl.getPath() + "/" + projectName + "ServiceImpl.java");
								}
								
								write(type.getFilter(projectName, packageName, author, personal.equals("false")), filter.getPath() + "/" + projectName + "Filter.java");
							
								write(type.getMapping(projectName, packageName, author, model.equals("simpliy")), mapping.getPath() + "/" + projectName + "Mapping.java");
							
								write(type.getResponse(projectName, packageName, author), core.getPath() + "/Response.java");
								write(type.getResponseCode(projectName, packageName, author), core.getPath() + "/ResponseCode.java");
								write(type.getResponseGenerator(projectName, packageName, author), core.getPath() + "/ResponseGenerator.java");
							
								write(type.getController(projectName, packageName, author, model.equals("simpliy")), controller.getPath() + "/" + projectName + "Controller.java");
								
								if (!BuildUtils.isEmpty(config)) {
									write(type.getRestTemplate(projectName, packageName, author), config.getPath() + "/RestTemplateConfig.java");
									write(type.getYMLConfig(projectName, packageName, author), config.getPath() + "/" + projectName + "YMLConfig.java");
								}

								StringBuffer command = new StringBuffer();
								byte[] buffer = new byte[1024];
						        int readBytes = -1;
						        String jQuery = null;
								try {
									URL url = new URL("http://code.jquery.com/jquery-latest.min.js");
									URLConnection urlConnection = url.openConnection();
									InputStream is = urlConnection.getInputStream();
									OutputStream os = new FileOutputStream(front.getPath() + "/jquery-1.11.1.min.js");
									try {
										int i = 0;
										while ((readBytes = is.read(buffer)) != -1) {
											os.write(buffer, 0, readBytes);
											if (i == 0) {
												jQuery = new String(buffer);
												jQuery = jQuery.substring(jQuery.indexOf("v") + 1, jQuery.indexOf("|") - 1);
											}
											i++;
										}
									} finally {
										os.close();
										is.close();
									}
									if (!jQuery.equals("1.11.1")) {
										command.append("rename ").append(front.getAbsolutePath()).append("\\jquery-1.11.1.min.js jquery-").append(jQuery).append(".min.js");
										command.append("\n");
									}
								} catch (Exception ex) {
									BuildUtils.readLocal(getClass(), buffer, readBytes, front.getPath(), "jquery-1.11.1.min.js");
									jQuery = "1.11.1";
								}
								BuildUtils.readLocal(getClass(), buffer, readBytes, front.getPath(), "md5.js");
								String[] outerJars = null;
								if (!BuildUtils.isEmpty(outerJar)) {
									File libs = new File(resources.getPath() + "/lib");
									if (!libs.exists()) {
										libs.mkdirs();
									}
									outerJars = outerJar.contains("$") ? outerJar.split("\\$") : new String[] {outerJar};
									for (String string : outerJars) {
										if (!BuildUtils.isEmpty(string)) {
											command.append("move ").append(string).append(".jar ").append(libs.getAbsolutePath());
											command.append("\n");
										}
									}
								}
								
								String[] wsdlJars = null;
								if (!BuildUtils.isEmpty(wsdl)) {
									File libs = null;
									if (sourceOfWsdl.equals("false")) {
										libs = new File(resources.getPath() + "/lib");
										if (!libs.exists()) {
											libs.mkdirs();
										}
									}
									String[] wsdls = wsdl.contains("$") ? wsdl.split("\\$") : new String[] {wsdl};
									wsdlJars = new String[wsdls.length];
									for (int i = 0; i < wsdlJars.length; i++) {
										if (!BuildUtils.isEmpty(wsdls[i])) {
											String target = wsdls[i];
											if (wsdls[i].startsWith("http") || wsdls[i].endsWith("?wsdl")) {
												wsdls[i] = wsdls[i].substring(wsdls[i].lastIndexOf("/") + 1, wsdls[i].length() - 5);
												if (wsdls[i].contains(".")) {
													wsdlJars[i] = "org.".concat(BuildUtils.splitByUpperCaseAndAddDot(wsdls[i].substring(0, wsdls[i].indexOf("."))));
												} else {
													wsdlJars[i] = "org.".concat(BuildUtils.splitByUpperCaseAndAddDot(wsdls[i]));
												}
											} else {
												wsdlJars[i] = "org.".concat(BuildUtils.splitByUpperCaseAndAddDot(wsdls[i].substring(0, wsdls[i].length() - 4)));
											}
											if (sourceOfWsdl.equals("true")) {
												command.append("wsimport -Xnocompile -s ").append(src.getAbsolutePath()).append(" -p ").append(wsdlJars[i]).append(" -encoding UTF-8 -keep ").append(target);
											} else {
												command.append("wsimport -p ").append(wsdlJars[i]).append(" -encoding UTF-8 -keep ").append(target);
												command.append("\n");
												command.append("jar cvf ").append(wsdlJars[i].replaceAll("\\.", "-")).append("-1.0.jar org");
												command.append("\n");
												command.append("rd/s/q org");
												command.append("\n");
												command.append("move ").append(wsdlJars[i].replaceAll("\\.", "-")).append("-1.0.jar ").append(libs.getAbsolutePath());
											}
											command.append("\n");
										}
									}
								}
								if (!BuildUtils.isEmpty(command.toString())) {
									try {
										//command.append("\n");
										//command.append("exit");
										write(command.toString(), "command.bat");
										Runtime runtime = Runtime.getRuntime();
										Process process = runtime.exec("cmd /c command.bat & del command.bat");//rd/f/s/q command.bat
										start(process);
										process.waitFor();
										process.destroy();
										process = null;
									} catch(Exception ex) {
									}
								}
								write(type.getPom(projectName, packageName, outerJars, wsdlJars, sourceOfWsdl.equals("true"), personal.equals("false")), projectName + "/" + projectName + "/pom.xml");
								
								write(type.getBase64JS(), front + "/jquery.base64.js");
								write(type.getRequestJS(projectName, port, model.equals("simpliy")), front + "/request.js");
								write(type.getHtml(projectName, author, model.equals("simpliy"), personal.equals("false"), jQuery), front + "/" + projectName + ".html");
								
								System.exit(0);
							} catch (Exception ex) {
							}
						}
					});
					container.add(jButton);
					jFrame.setIconImage(new ImageIcon(getClass().getResource("v11.png")).getImage());
					jFrame.setVisible(true);
					jFrame.setSize(width, height);
					jFrame.setLocationRelativeTo(null);
					jFrame.setResizable(false);
					jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} catch (Exception e) {
				}
			};
		});
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
	
	private static void start(Process process) throws Exception {
		StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");
		errorGobbler.start();
		StreamGobbler outGobbler = new StreamGobbler(process.getInputStream(), "STDOUT");
		outGobbler.start();
	}
	
}
