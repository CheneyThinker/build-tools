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
          
          String[] jTextFieldItems = { "projectName:", "port:", "author:", "outerJar:", "wsdl:" };
          String[] jTextFieldTips = { "lemon", "9527", "CheneyThinker", "Example:cheney-thinker-1.0.0$tea-1.0.0", "Example:cheney.xml$tea.xml" };
          
          String[] jComboBoxItems = { "sourceOfWsdl:", "lombok:", "mediaType:", "model:", "reloadYmlAs:", "broadcast:", "drivenModel:" };
          String[] jComboBoxTips = { "false,true", "false,true", "json,both", "simpliy,common", "map,properties", "none,stomp,websocket", "Event Driven,Timing Task Driven" };
          
          int itemHeight = 35;
          int width = 500;
          int totalLength = jTextFieldItems.length + jComboBoxItems.length + 1;
          int height = ((totalLength + 1) * itemHeight) + ((totalLength + 2) * 5);
          JLabel[] jLabels = new JLabel[totalLength - 1];
          JTextField[] jTextFields = new JTextField[jTextFieldItems.length];
          int i = 0;
          for (int j = jTextFields.length; i < j; i++) {
            jLabels[i] = new JLabel(jTextFieldItems[i]);
            jLabels[i].setBounds(5, i * itemHeight + 5 * (i + 1), 135, itemHeight);
            container.add(jLabels[i]);
            jTextFields[i] = new JTextField(jTextFieldTips[i]);
            jTextFields[i].setBounds(140, i * itemHeight + 5 * (i + 1), 350, itemHeight);
            container.add(jTextFields[i]);
          }
          JComboBox<?>[] jComboBoxs = new JComboBox<?>[jComboBoxItems.length];
          for (int k = 0, j = jComboBoxs.length; k < j; i++, k++) {
            jLabels[i] = new JLabel(jComboBoxItems[k]);
            jLabels[i].setBounds(5, i * itemHeight + 5 * (i + 1), 135, itemHeight);
            container.add(jLabels[i]);
            jComboBoxs[k] = new JComboBox<>(jComboBoxTips[k].split("\\,"));
            jComboBoxs[k].setBounds(140, i * itemHeight + 5 * (i + 1), 350, itemHeight);
            container.add(jComboBoxs[k]);
          }
          JButton jButton = new JButton("Makefile");
          jButton.setBounds(5, (totalLength - 1) * itemHeight + 5 * (totalLength + 1), 485, itemHeight);
          jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              try {
                String projectName = jTextFields[0].getText();
                String port = jTextFields[1].getText();
                String author = jTextFields[2].getText();
                String outerJar = jTextFields[3].getText();
                String wsdl = jTextFields[4].getText();
                String sourceOfWsdl = jComboBoxs[0].getSelectedItem().toString();
                String lombok = jComboBoxs[1].getSelectedItem().toString();
                String xml = jComboBoxs[2].getSelectedItem().toString();
                String model = jComboBoxs[3].getSelectedItem().toString();
                String reloadYmlAs = jComboBoxs[4].getSelectedItem().toString();
                String broadcast = jComboBoxs[5].getSelectedItem().toString();
                boolean eventDrivenModel = jComboBoxs[6].getSelectedItem().toString().equals("Event Driven");
                
                BuildUtils.init(projectName);
                
                if (outerJar.startsWith("Example:") || outerJar.equals("Example:cheney-thinker-1.0.0$tea-1.0.0") || BuildUtils.isEmpty(outerJar)) {
                  outerJar = null;
                }
                if (wsdl.startsWith("Example:") || wsdl.equals("Example:cheney.xml$tea.xml") || BuildUtils.isEmpty(wsdl)) {
                  wsdl = null;
                }
                if (BuildUtils.isEmpty(projectName)) {
                  projectName = "CheneyThinker";
                } else {
                  projectName = BuildUtils.firstUpperCase(projectName);
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
                BuildType type = new BuildType(projectName, author.equals("CheneyThinkerImage") ? "CheneyThinker" : author, port);
                Template template = new Template(type);
                
                BuildUtils.config();
                template.writeCzm("YMLConfig-".concat(lombok), type.getProjectName().concat("YMLConfig.java"));
                template.writeCzm("RestTemplateConfig", "RestTemplateConfig.java");
                if (broadcast.equals("stomp")) {
                  template.writeCzm("StompWebSocketMessageBrokerConfigurer", "StompWebSocketMessageBrokerConfigurer.java");
				} else if (broadcast.equals("websocket")) {
					template.writeCzm("ServerEndpointExporterConfig", "ServerEndpointExporterConfig.java");
				}
                BuildUtils.controller();
                template.writeCzm("Controller-".concat(model).concat("-").concat(broadcast.equals("websocket") ? "none" : broadcast), type.getProjectName().concat("Controller.java"));
                BuildUtils.core();
                template.writeCzm("Response-".concat(lombok), "Response.java");
                template.writeCzm("ResponseCode", "ResponseCode.java");
                template.writeCzm("ResponseGenerator", "ResponseGenerator.java");
                if (broadcast.equals("stomp")) {
                  if (eventDrivenModel == false) {
                	template.writeCzm("BroadcastWrapper-".concat(lombok), "BroadcastWrapper.java");
                	template.writeCzm("BroadcastDispatch", "BroadcastDispatch.java");
                  }
                } else if (broadcast.equals("websocket")) {
                  template.writeCzm("WebSocket", type.getProjectName().concat("WebSocket.java"));
                }
                BuildUtils.exception();
                template.writeCzm("Exception", type.getProjectName().concat("Exception.java"));
                BuildUtils.filter();
                template.writeCzm("Filter-".concat(model).concat("-").concat(broadcast.equals("websocket") ? "none" : broadcast), type.getProjectName().concat("Filter.java"));
                BuildUtils.service();
                if (model.equals("simpliy")) {
                  template.writeCzm("Service-".concat(model).concat("-").concat(reloadYmlAs), type.getProjectName().concat("Service.java"));
                } else {
                  template.writeCzm("Service-".concat(model), type.getProjectName().concat("Service.java"));
                  BuildUtils.serviceImpl();
                  template.writeCzm("ServiceImpl-".concat(reloadYmlAs), type.getProjectName().concat("ServiceImpl.java"));
                }
                BuildUtils.utils();
                template.writeCzm("Utils-".concat(xml).concat("-").concat(model), type.getProjectName().concat("Utils.java"));
                template.writeCzm("BeanUtils", "BeanUtils.java");
                template.writeCzm("ReflectUtils", "ReflectUtils.java");
                if (broadcast.equals("stomp")) {
				  template.writeCzm("BroadcastUtils-".concat(String.valueOf(eventDrivenModel)), "BroadcastUtils.java");
				} else if (broadcast.equals("websocket")) {
				  template.writeCzm("WebSocketUtils", "BroadcastUtils.java");
				}
                BuildUtils.application();
                BuildUtils.write(type.getApplication(), type.getProjectName().concat("Application.java"));
                
                StringBuffer command = new StringBuffer();
                byte[] buffer = new byte[1024];
                int readBytes = -1;
                String jQuery = null;
                File front = BuildUtils.front();
                try {
                  URL url = new URL("http://code.jquery.com/jquery-latest.min.js");
                  URLConnection urlConnection = url.openConnection();
                  InputStream is = urlConnection.getInputStream();
                  OutputStream os = new FileOutputStream(front.getPath().concat("/jquery-1.11.1.min.js"));
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
                type.setjQuery(jQuery);
                if (author.equals("CheneyThinkerImage")) {
				  BuildUtils.readLocal(getClass(), buffer, readBytes, front.getPath(), "image-base64.js");
				  BuildUtils.readLocal(getClass(), buffer, readBytes, front.getPath(), "image-index.js");
				  template.writeCzm("image-index", "image-index.html");
				}
                BuildUtils.readLocal(getClass(), buffer, readBytes, front.getPath(), "md5.js");
                if (broadcast.equals("stomp")) {
                  BuildUtils.readLocal(getClass(), buffer, readBytes, front.getPath(), "stomp.min.js");
                  BuildUtils.readLocal(getClass(), buffer, readBytes, front.getPath(), "sockjs.min.js");
                  template.writeCzm("stompJS", "stomp-broker.js");
                  template.writeCzm("stompHtml", "StompBroker.html");
				} else if (broadcast.equals("websocket")) {
				  template.writeCzm("websocketJS", "stomp-broker.js");
				  template.writeCzm("websocketHtml", "StompBroker.html");
				}
                template.writeCzm("validator-pluginJS-".concat(model), "validator-plugin.js");
                template.writeCzm("configJS-".concat(model), "config.js");
                template.writeCzm("html-".concat(model).concat("-").concat(broadcast), type.getProjectName().concat(".html"));
                template.writeCzm("pluginHtml-".concat(model), type.getProjectName().concat("Plugin.html"));
                template.writeCzm("jquery.base64", "jquery.base64.js");
                template.writeCzm("request-".concat(model), "request.js");
                
                String[] outerJars = null;
                if (!BuildUtils.isEmpty(outerJar)) {
                  File libs = new File(BuildUtils.resources().getPath().concat("/lib"));
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
                    libs = new File(BuildUtils.resources().getPath().concat("/lib"));
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
                        command.append("wsimport -Xnocompile -s ").append(BuildUtils.getJavaPath()).append(" -p ").append(wsdlJars[i]).append(" -encoding UTF-8 -keep ").append(target);
                      } else {
                        command.append("wsimport -p ").append(wsdlJars[i]).append(" -encoding UTF-8 -keep ").append(target);
                        command.append("\n");
                        command.append("jar cvf ").append(wsdlJars[i].replaceAll("\\.", "-")).append("-1.0.0.jar org");
                        command.append("\n");
                        command.append("rd/s/q org");
                        command.append("\n");
                        command.append("move ").append(wsdlJars[i].replaceAll("\\.", "-")).append("-1.0.0.jar ").append(libs.getAbsolutePath());
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
                    BuildUtils.start(process);
                    process.waitFor();
                    process.destroy();
                    process = null;
                  } catch(Exception ex) {
                  }
                }
                BuildUtils.resources();
                template.writeCzm("application-default-".concat(model), "application-default.yml");
                template.writeCzm("logback-spring", "logback-spring.xml");
                BuildUtils.pom();
                BuildUtils.write(type.getPom(outerJars, wsdlJars, sourceOfWsdl.equals("true"), lombok.equals("true"), xml.equals("xml") || xml.equals("both"), broadcast), "pom.xml");
                
                System.exit(0);
              } catch (Exception ex) {
              }
            }
          });
          container.add(jButton);
          jFrame.setIconImage(new ImageIcon(this.getClass().getResource("v11.png")).getImage());
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

}