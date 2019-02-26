package it.classhidra.core.tool.util;





import it.classhidra.core.tool.util.multipart.MultipartData;
import it.classhidra.core.tool.util.multipart.MultipartDataFile;
import it.classhidra.core.tool.util.multipart.MultipartDataParameter;
import it.classhidra.core.tool.util.multipart.MultipartParser;

import java.io.ByteArrayOutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class util_multipart {
	public static HashMap<String,Object>  popolateHashMap(HttpServletRequest req){
		HashMap<String,Object> parametersMP = null;
		int file_counter=0;
		if(parametersMP==null){
			parametersMP = new HashMap<String,Object>();
			parametersMP.put("$REQUEST_CHARSET",req.getCharacterEncoding());
			Enumeration<?> headerNames = req.getHeaderNames();
			while(headerNames.hasMoreElements()) {
				String headerName = (String)headerNames.nextElement();
				parametersMP.put(headerName,req.getHeader(headerName));
			}
			try{
				MultipartParser mp = new MultipartParser(req, -1); 
				MultipartData part;
				while ((part = mp.readNextPart()) != null) {
//					String name = part.getName();
					if (part.isParameter()) {
						MultipartDataParameter paramPart = (MultipartDataParameter) part;
//						if (paramPart.getName().equals(key)){
							parametersMP.put(paramPart.getName(),paramPart.getStringValue());
//						}
					}	
					if (part.isFile()) {
						MultipartDataFile filePart = (MultipartDataFile)part;
						String sFilename = filePart.getFileName();
						if(sFilename!=null){
			
							Vector<byte[]> buf_v = new Vector<byte[]>();
							
							long size=0;
							int read;
							byte[] buf = new byte[8 * 1024];
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							while((read = filePart.getInputStream().read(buf)) != -1) {
								out.write(buf, 0, read);
								size += read;
								byte[] buf_tmp = new byte[read];
								System.arraycopy(buf,0,buf_tmp,0,read);
								buf_v.add(buf_tmp);
							}
							byte[] fine = new byte[(int)size];
							for(int i=0;i<buf_v.size();i++){
//								int current_length = 8 * 1024;
								byte[] current = (byte[])buf_v.get(i);
								System.arraycopy(buf_v.get(i),0,fine,i*8 * 1024,current.length);
	
							}
							HashMap<String,Object> current_file = new HashMap<String,Object>();
							current_file.put("content",fine);
							current_file.put("contenttype",filePart.getContentType());
							current_file.put("name",filePart.getFileName());
							current_file.put("inputname",filePart.getName());
							parametersMP.put("file"+file_counter,current_file);
							file_counter++;
						}

					}
				}
				
				
				
				
			}catch(Exception ex){		
			}
			
			try{
				Enumeration<?> en = req.getParameterNames();
				while(en.hasMoreElements()){
					String key = (String)en.nextElement();
					String value = req.getParameter(key);
					parametersMP.put(key,value);
				}
			}catch(Exception ex){
			}
		}

		return parametersMP;
	}	
}
