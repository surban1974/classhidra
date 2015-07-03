package it.classhidra.core.controller.transformations;

import it.classhidra.core.controller.i_transformation;
import it.classhidra.core.controller.transformation;
import it.classhidra.core.tool.util.util_classes;
import it.classhidra.core.tool.util.util_file;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_xml_xslt;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import it.classhidra.annotation.elements.Transformation;

@Transformation(name="resource2xslt")
public class xslt extends transformation implements i_transformation, Serializable {

	private static final long serialVersionUID = 1L;

	public xslt(){
		super();
	}
	
	public byte[] transform(byte[] input, HttpServletRequest request){
		byte[] result = null;
		
		String path = get_infotransformation().getPath();
		
		byte[] xslt = null;
		
		
		if(xslt==null){
			try{
				InputStream openStream = request.getSession().getServletContext().getResourceAsStream( request.getSession().getServletContext().getRealPath(path) );
				xslt = util_classes.readInputStream2Byte(openStream);
			}catch (Exception e) {
			}
		}	
		
		if(xslt==null){
			try{
				xslt=util_file.getBytesFromFile(path);
			}catch (Exception e) {
			}
		}	
		if(xslt==null){
			try{
				String urlPath = path;
				if(path.toUpperCase().indexOf("HTTP")==-1){
					urlPath="http://"+request.getLocalAddr()+":"+request.getLocalPort()+util_format.replace(request.getRequestURI(), request.getServletPath(), "") +path;
				}
				URL url = new URL(urlPath);
				URLConnection uc = url.openConnection();
				
				InputStream openStream = uc.getInputStream();
				xslt = util_classes.readInputStream2Byte(openStream);

			}catch (Exception e) {
				e.toString();
			}
		}	
		
		try{
			result = util_xml_xslt.xml_xslt_transform2Byte(input, xslt);
		}catch(Exception e){
			
		}
		return result;
	}
	

}
