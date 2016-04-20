package jetty.starter;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.bsFilter;
import it.classhidra.core.tool.util.util_classes;
import neohort.universal.output.creator_iHort;

public class JettyClasshidraStarter {

	private static String rootPath = null;

	public static void main(String[] args) {



		boolean mainIntoJar = false;
		if(args.length>0){
			rootPath=args[0];
			File root = new File(rootPath);
			if(!root.exists()){
				System.out.println("ERROR. Folder ROOT "+ rootPath+ " not exsists.");
				rootPath=null;
			}
		}

		URL location = JettyClasshidraStarter.class.getResource('/'+JettyClasshidraStarter.class.getName().replace('.', '/')+".class");
		if(location!=null && location.getProtocol().equalsIgnoreCase("jar")){
			System.setProperty("application.annotation.scanner.asjar", "true");
			mainIntoJar=true;
		}

		try {


			System.setProperty("debug", "true");


            Server server = new Server();

            SelectChannelConnector connector0 = new SelectChannelConnector();
	            connector0.setPort(8080);
	            connector0.setMaxIdleTime(30000);
	            connector0.setRequestHeaderSize(8192);

            server.setConnectors(new Connector[]{ connector0});


            FilterHolder bsfilter = new FilterHolder(new bsFilter());
	            bsfilter.setAsyncSupported(true);
	            bsfilter.setInitParameter("CharacterEncoding", "ISO-8859-1");
	            bsfilter.setInitParameter("ExcludedUrl","/javascript2012/;/css/;/images/;");
	            bsfilter.setInitParameter("ExcludedPattern","^(?!.*/neohort/).*\\.jsp$");
	            bsfilter.setInitParameter("RestSupport","true");


            EnumSet<DispatcherType> all = EnumSet.of(DispatcherType.ASYNC, DispatcherType.ERROR, DispatcherType.FORWARD,
                 DispatcherType.INCLUDE, DispatcherType.REQUEST);




			if(rootPath==null){
	            try{

	            	URL rootUrl = JettyClasshidraStarter.class.getClassLoader().getResource(".");
	            	if(rootUrl!=null){
	            		if(rootPath==null)
	            			rootPath = util_classes.convertUrl2File(rootUrl).getAbsolutePath();
	            	}


	            	URL rootUrl0 = Thread.currentThread().getContextClassLoader().getResource(".");
	            	if(rootUrl0!=null){
	            		if(rootPath==null)
	            			rootPath = util_classes.convertUrl2File(rootUrl0).getAbsolutePath();
	            	}





	            }catch(Exception e){
	            }



			}

			if(mainIntoJar){
            	URL resourceJar = Thread.currentThread().getContextClassLoader().getResource("/WebContent/index.html");
            	if(resourceJar==null)
            		resourceJar = Thread.currentThread().getContextClassLoader().getResource("WebContent/index.html");

            	if(resourceJar!=null){
	            	File directory = util_classes.convertUrl2File(resourceJar);
					if(directory!=null && resourceJar.toURI().getScheme().equalsIgnoreCase("jar")){
						String jarPath = directory.getPath().substring(5, directory.getPath().indexOf("!"));
						if(rootPath==null){

							if(jarPath.contains("/"))
								rootPath = jarPath.substring(0, jarPath.lastIndexOf("/"));

							if(rootPath==null && jarPath.contains("\\"))
								rootPath = jarPath.substring(0, jarPath.lastIndexOf("\\"));

							if(rootPath!=null && (rootPath.startsWith("/") || rootPath.startsWith("\\")))
								rootPath = rootPath.substring(1,rootPath.length());


						}

						if(rootPath!=null && !rootPath.endsWith("\\"))
			        		rootPath+="\\";

						File root = new File(rootPath);
						if(root.exists()){
							File webContent = new File(rootPath+"WebContent");
							webContent.delete();
							webContent.mkdirs();

							JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
							Enumeration en = jar.entries();
							while(en.hasMoreElements()){
								JarEntry entry = (JarEntry)en.nextElement();
								if(entry.getName().startsWith("WebContent/")){
									if(entry.isDirectory()){
										File dir = new File(root.getAbsolutePath()+"/"+entry.getName());
										dir.mkdir();
									}else{
										try {
											InputStream in = jar.getInputStream(entry);
									        OutputStream out = new FileOutputStream(root.getAbsolutePath()+"/"+entry.getName());
									        byte[] buf = new byte[1024];
									        int len;
									        while((len=in.read(buf))>0){
									            out.write(buf,0,len);
									        }
									        out.close();
									        in.close();
										} catch (Exception e) {
										}
									}
								}

							}
						}
					}
				}

			}

        	if(rootPath!=null){
        		if(!rootPath.endsWith("\\"))
        			rootPath+="\\";
				File logDir = new File(rootPath+"log");
				if(!logDir.exists())
					logDir.mkdirs();

        		System.setProperty("application.log.path", logDir.getAbsolutePath()+"\\");

				System.setProperty("application.log.level","DEBUG");
				System.setProperty("application.log.name","classhidra_jetty");
				System.setProperty("application.log.maskname","application");
				System.setProperty("application.log.maskformat","yyyyMMdd_hhmmssss");
				System.setProperty("application.log.maxlength","1000000");
				System.setProperty("application.log.maxfiles","10");
				System.setProperty("application.log.flashrate","0");
				System.setProperty("application.log.flashsize","1024");
				System.setProperty("application.log.write2console","true");
        	}



            WebAppContext webapp = new WebAppContext(
            		(rootPath==null)?"/WebContent":rootPath + "WebContent",
            		"");
	            webapp.setParentLoaderPriority(true);
	            webapp.setContextPath("/");
	            webapp.addFilter(bsfilter, "/*", all);
	            webapp.addServlet(new ServletHolder(new bsController()),"/Controller");
	            webapp.addServlet(new ServletHolder(new creator_iHort()),"/report_creator");
            server.setHandler(webapp);



            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                public void run() {
                	if(rootPath!=null){
						File webContent = new File(rootPath+"WebContent");
						webContent.deleteOnExit();
                	}
                }
            }));

            server.start();

            System.out.println("Classhidra Base start on http://localhost:8080/");

            if(Desktop.isDesktopSupported()){
            	Desktop.getDesktop().browse(new URI("http://localhost:8080/"));
            }

            server.join();


        } catch (Exception ex) {
           System.out.println("ERROR: "+ex.toString());
        }

	}

}
