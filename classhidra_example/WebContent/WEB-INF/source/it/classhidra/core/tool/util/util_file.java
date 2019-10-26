package it.classhidra.core.tool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

public class util_file {
	public static boolean writeByteToFile( byte[] bytes, String path ) throws Exception {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(path, false);		
			fileOutputStream.write(bytes);
		} finally {
			if(fileOutputStream!=null)
				fileOutputStream.close();
		}
		return true;
	}
	public static boolean appendByteToFile( byte[] bytes, String path ) throws Exception {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(path, true);
			fileOutputStream.write(bytes);
		} finally {
			if(fileOutputStream!=null)
				fileOutputStream.close();
		}
		return true;
	}	
	 public static byte[] getBytesFromFile(String path) throws Exception {
		 File file = new File(path);
		 return getBytesFromFile(file);
	 }	
	 public static byte[] getBytesFromFile(File file) throws Exception {

		 if(!file.exists()){
			 throw new IOException("File "+file.getAbsolutePath()+" non exist");
		 }
	     InputStream is = new FileInputStream(file);
	     long length = file.length();
	     if (length > Integer.MAX_VALUE) {
	     }
	     byte[] bytes = new byte[(int)length];
	     int offset = 0;
	     int numRead = 0;
	     while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	           offset += numRead;
	     }
	     is.close();
	     if (offset < bytes.length) {
	         throw new IOException("Could not completely read file "+file.getName());
	     }

	     return bytes;
	 }		 
	 
	 public static boolean copyFileUsingStream(File source, File dest, boolean append) throws IOException {
		    InputStream is = null;
		    OutputStream os = null;
		    try {
		        is = new FileInputStream(source);
		        os = new FileOutputStream(dest,append);
		        byte[] buffer = new byte[1024];
		        int length;
		        while ((length = is.read(buffer)) > 0) {
		            os.write(buffer, 0, length);
		        }
		    } finally {
		    	if(is!=null)
		    		is.close();
		    	if(os!=null)
		    		os.close();
		    }
		    return true;
		}

	public static Properties loadExternalProperty(String property_name) throws Exception{
			Properties property = new Properties();
			InputStream instrm = null;
			FileInputStream fis = null;
			try{				
				instrm = ClassLoader.getSystemResourceAsStream(property_name);
				if(instrm!=null) {
					property.load(instrm);
					instrm.close();
				}
				else {
					fis = new FileInputStream(property_name);
					property.load(fis);		
					fis.close();
				}
			}catch(Exception e){
				property=null;
				throw e;
			}finally {
				try {
					if(instrm!=null)
						instrm.close();
				}catch (Exception e) {
				}
				try {
					if(fis!=null)
						fis.close();
				}catch (Exception e) {
				}				
			}
			return property;
	}
	
	
	public static Properties loadProperty(String property_name) throws Exception{
		Properties property = new Properties();

		try{
			ResourceBundle rb = ResourceBundle.getBundle(property_name);

			Enumeration<String> en = rb.getKeys();
			while(en.hasMoreElements()){
				if(property==null) property = new Properties();
				String key = (String)en.nextElement();
				if(rb.getString(key)==null || rb.getString(key).equals("")){}
				else property.setProperty(key,rb.getString(key));
			}
		}catch(Exception e){	

			try{
				if(property_name.indexOf(".properties")==-1) property_name+=".properties";		
				property = util_file.loadExternalProperty(property_name);					
			}catch (Exception ex) {
				throw e;
			}
			
		}
		return property;
	}
}
