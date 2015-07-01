package it.classhidra.core.tool.util;

import it.classhidra.core.controller.bsController;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class util_classes {
	
	public  static Class[] getClasses(String pckgname) throws ClassNotFoundException {
		ArrayList classes = new ArrayList();
		URL resource = null;
		// Get a File object for the package

		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			String path = '/' + pckgname.replace('.', '/');
			resource = cld.getResource(path);
			
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}


		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " does not appear to be a valid package");
		}
		if(resource!=null){
			if(resource.getProtocol().equalsIgnoreCase("vfs")){
				Object arr = util_reflect.execStaticMethod("it.classhidra.plugin.was.jboss7.util_vfs_classes", "getResources", new Class[]{URL.class}, new Object[]{resource});
				if(arr!=null && arr instanceof ArrayList){
					for (int i = 0; i < ((ArrayList)arr).size(); i++) {
						// we are only interested in .class files
						String filename =  (String)((ArrayList)arr).get(i);
						if (filename.endsWith(".class")) {
							// removes the .class extension
							classes.add(Class.forName(pckgname + '.'+ filename.substring(0, filename.length() - 6)));
						}
					}					
				}
			}else{
				File directory = convertUrl2File(resource);
				
				if (directory!=null && directory.exists()) {
					// Get the list of the files contained in the package
					String[] files = directory.list();
					for (int i = 0; i < files.length; i++) {
						// we are only interested in .class files
						if (files[i].endsWith(".class")) {
							// removes the .class extension
							classes.add(Class.forName(pckgname + '.'+ files[i].substring(0, files[i].length() - 6)));
						}
					}
					
				} else {
					throw new ClassNotFoundException(pckgname
							+ " does not appear to be a valid package");
				}

			}
		}

		
		Class[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
	}
	
	public static ArrayList getResources(String pckgname) throws ClassNotFoundException {
		ArrayList res = new ArrayList();
		URL resource = null;
		
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			String path = '/' + pckgname.replace('.', '/');
			resource = cld.getResource(path);
			
			if (resource == null) {
				path = pckgname.replace('.', '/');
				resource = cld.getResource(path);
			}
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " does not appear to be a valid package");
		}
		if(resource!=null){
			if(resource.getProtocol().equalsIgnoreCase("vfs")){
				Object arr = util_reflect.execStaticMethod("it.classhidra.plugin.was.jboss7.util_vfs_classes", "getResources", new Class[]{URL.class}, new Object[]{resource});
				if(arr!=null && arr instanceof ArrayList)
					return (ArrayList)arr;
					
			}else{
				File directory = convertUrl2File(resource);
				
				if (directory!=null && directory.exists()) {
					File[] files = directory.listFiles();
					for(int i=0;i<files.length;i++){
						res.add(files[i].getName());
					}
				} else {
					throw new ClassNotFoundException(pckgname
							+ " does not appear to be a valid package");
				}
				
			}
		}
		

		return res;
	}	
	
	
	public  static ArrayList getResourcesAsFile(String pckgname) throws ClassNotFoundException {
		ArrayList res = new ArrayList();
		// Get a File object for the package
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			String path = '/' + pckgname.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null) {
				path = pckgname.replace('.', '/');
				resource = cld.getResource(path);
			}
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
			directory = convertUrl2File(resource);
//			directory = new File(resource.getPath());
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " (" + directory
					+ ") does not appear to be a valid package");
		}
		if (directory.exists()) {
			// Get the list of the files contained in the package
			File[] files = directory.listFiles();
			
			for(int i=0;i<files.length;i++){
				res.add(files[i]);
			}
		} else {
			throw new ClassNotFoundException(pckgname
					+ " does not appear to be a valid package");
		}

		return res;
	}	
	
	public  static String getResourceAsTxt(String pckgname, String rsname) throws ClassNotFoundException {

		InputStream is = null;
	    BufferedReader br = null;
	    String result=null;
	    String line="";
	    

	    try { 
	    	Object obj = null;
	    	try{
	    		obj = Class.forName(pckgname).newInstance();
	    	}catch(Exception ex){    		
	    	}
	    	if(obj==null) return null;
	    	is = obj.getClass().getResourceAsStream(rsname);
	    	if(is!=null){
	    		result="";
		    	br = new BufferedReader(new InputStreamReader(is));
		    	while (null != (line = br.readLine())) {
		    		result+=(line+"\n");
		    	}
	    	}	
	    }catch (Exception e) {
	    }finally {
	    	try {
	    		if (br != null) br.close();
	    		if (is != null) is.close();
	    	}catch (Exception e) {
	    	}
		}
		
		return result;
	}	
	
	public static File convertUrl2File(URL url){	
		File f = null;
		try{
			f = new File(new URI(url.toString()));
		}catch(URISyntaxException e) {
			f = new File(url.getPath());
		} catch (Exception e) {				
		}
		return f;
	}
	
	public  static String getPath(String pckgname) throws ClassNotFoundException {
		String res = "?";
		// Get a File object for the package
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			String path = '/' + pckgname.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
			directory = new File(resource.getFile());
			res=directory.getAbsolutePath();
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " (" + directory
					+ ") does not appear to be a valid package");
		}


		return res;
	}
	
	public  static String getPathWebContent(HttpServletRequest request){
		if(bsController.getAppInit().get_path_root()!=null && !bsController.getAppInit().get_path_root().equals(""))
			return bsController.getAppInit().get_path_root();

		if(request.getSession().getServletContext().getRealPath("/")!=null) return request.getSession().getServletContext().getRealPath("/");
		
		String pckgname = "config";
		String res = "?";
		// Get a File object for the package
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			String path = '/' + pckgname.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
			directory = new File(resource.getFile());
			res=directory.getAbsolutePath();
		} catch (Exception x) {
			
		}


		if(!res.equals("?")){
			res=res.replace('\\', '/');
			int index = res.indexOf("WEB-INF/classes");
			res = res.substring(0,index);
		
		}
		return res;
	}		
	public  static String getPathWebContent(ServletContext context){
		if(bsController.getAppInit().get_path_root()!=null && !bsController.getAppInit().get_path_root().equals(""))
			return bsController.getAppInit().get_path_root();

		if(context.getRealPath("/")!=null) return context.getRealPath("/");
		
		String pckgname = "config";
		String res = "?";
		// Get a File object for the package
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			String path = '/' + pckgname.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
			directory = new File(resource.getFile());
			res=directory.getAbsolutePath();
		} catch (Exception x) {
			
		}


		if(!res.equals("?")){
			res=res.replace('\\', '/');
			int index = res.indexOf("WEB-INF/classes");
			res = res.substring(0,index);
		}
		return res;
	}				

	public  static String getPathPackageConfig(){
		
		String pckgname = "config";
		String res = "?";
		// Get a File object for the package
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			String path = '/' + pckgname.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
			directory = new File(resource.getFile());
			res=directory.getAbsolutePath();
		} catch (Exception x) {
			
		}


		if(!res.equals("?")){
			res=res.replace('\\', '/');			
			res+= "/";
		}
		return res;
	}
	
	public static byte[] readInputStream2Byte(InputStream openStream) throws Exception{
		Vector buf_v = new Vector();
		long size=0;
		int read;
		byte[] buf = new byte[8 * 1024];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while((read = openStream.read(buf)) != -1) {
			out.write(buf, 0, read);
			size += read;
			byte[] buf_tmp = new byte[read];
			System.arraycopy(buf,0,buf_tmp,0,read);
			buf_v.add(buf_tmp);
		}
		byte[] xslt= new byte[(int)size];
		for(int i=0;i<buf_v.size();i++){
			byte[] current = (byte[])buf_v.get(i);
			System.arraycopy(buf_v.get(i),0,xslt,i*8 * 1024,current.length);
		}
		return xslt;
	}	


	public static byte[] getResourceAsByte(String pckgname, String rsname) throws ClassNotFoundException { 
		
		byte[] result = null;
		String property_name =  rsname;
		
		InputStream is = null;
	    try { 
	    	Object obj = null;
	    	try{
	    		obj = Class.forName(pckgname).newInstance();
	    	}catch(Exception ex){    		
	    	}
	    	if(obj==null) return null;
	    	is = obj.getClass().getResourceAsStream(property_name);
	    	if(is!=null){
	    		result = readInputStream2Byte(is);
	    	}
	    }catch (Exception e) {
	    }finally {
	    	try {
	    		if (is != null) is.close();
	    	}catch (Exception e) {
	    	}
		}
	
	    return result;
	}	
	
	public static byte[] getResourceAsByte(String rsname) throws ClassNotFoundException { 
		
		byte[] result = null;
		String property_name =  rsname;
		
		InputStream is = null;
	    try { 
	    	ClassLoader cld = Thread.currentThread().getContextClassLoader();
	    	 if (cld == null)
	    		 is=ClassLoader.getSystemResourceAsStream( property_name );
	    	 else 
	    		 is = cld.getResourceAsStream(property_name);
	    	
	    	if(is!=null){
	    		result = readInputStream2Byte(is);
	    	}
	    }catch (Exception e) {
	    }finally {
	    	try {
	    		if (is != null) is.close();
	    	}catch (Exception e) {
	    	}
		}
	
	    return result;
	}	
	
	public static ArrayList getResourcesAsByte(String rsname, byte[] separator) throws ClassNotFoundException { 
		if(rsname==null || rsname.trim().equals("")) return null;
		ArrayList result = new ArrayList();
		
		ArrayList array = new ArrayList();

		try{
			array = util_classes.getResources(rsname);
			for(int i=0;i<array.size();i++){
				String property_name =  "";
				if(rsname.charAt(rsname.length()-1)=='/')
					property_name = rsname+array.get(i);
				else property_name = rsname+"/"+array.get(i);
				try{
					if(i!=0 && separator!=null)
						result.add(separator);
					result.add(getResourceAsByte(property_name));
				}catch(Exception e){
				}
			}

		}catch(Exception e){
			util_format.writeToConsole(bsController.getLogInit(),"LoadMessages: Array.ERROR:"+e.toString());
		}
		return result;
	}


	
}
