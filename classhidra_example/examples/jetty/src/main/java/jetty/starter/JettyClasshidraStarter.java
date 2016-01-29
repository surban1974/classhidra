package jetty.starter;

import java.awt.Desktop;
import java.net.URI;
import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.bsFilter;
import neohort.universal.output.creator_iHort;

public class JettyClasshidraStarter {

	public static void main(String[] args) { 

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
            
            EnumSet<DispatcherType> all = EnumSet.of(DispatcherType.ASYNC, DispatcherType.ERROR, DispatcherType.FORWARD,
                 DispatcherType.INCLUDE, DispatcherType.REQUEST);
          
            
            
            String rootPath = JettyClasshidraStarter.class.getClassLoader().getResource(".").toString();
//            WebAppContext webapp = new WebAppContext(rootPath + "../../src/main/webapp", "");
            WebAppContext webapp = new WebAppContext(rootPath + "WebContent", "");
	            webapp.setParentLoaderPriority(true);
	            webapp.setContextPath("/");
	            webapp.addFilter(bsfilter, "/*", all);
	            webapp.addServlet(new ServletHolder(new bsController()),"/Controller");
	            webapp.addServlet(new ServletHolder(new creator_iHort()),"/report_creator");
            server.setHandler(webapp);
            
            

     
            
          

//            ServletHttpContext context = (ServletHttpContext) server.getContext("/");
//            context.addServlet("/MO", "jetty.HelloWorldServlet");

            server.start();
            
            System.out.println("Classhidra Base start on http://localhost:8080/");            
            
            if(Desktop.isDesktopSupported()){
            	Desktop.getDesktop().browse(new URI("http://localhost:8080/"));
            }            
            
            server.join();

 
        } catch (Exception ex) {
           System.out.println(ex.toString());
        }		

	}

}
