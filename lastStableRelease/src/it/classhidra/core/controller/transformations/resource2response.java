package it.classhidra.core.controller.transformations;



import java.io.OutputStream;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_transformation;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.controller.transformation;
import it.classhidra.core.tool.util.util_classes;

import it.classhidra.annotation.elements.Transformation;

@Transformation(name="resource2response")

public class resource2response extends transformation implements i_transformation, Serializable {

	private static final long serialVersionUID = 1L;

	public resource2response(){
		super();
	}


	public byte[] transform(i_action action_instance, HttpServletRequest request, HttpServletResponse response) {
		byte[] output = null;
		try{
			redirects current = action_instance.getCurrent_redirect();
			String url = action_instance.getCurrent_redirect().getRedirectUri(action_instance.get_infoaction());
			if(url!=null){
				output = util_classes.getResourceAsByte(url);
				if(output!=null){
/*					
	    			if(current.get_inforedirect().getContentType()!=null && !current.get_inforedirect().getContentType().equals("")){
	    				if(current.get_inforedirect().getContentType().equalsIgnoreCase("text/html")){
		    				response.setContentType(current.get_inforedirect().getContentType().toLowerCase());
		    				if(current.get_inforedirect().getContentEncoding()!=null && !current.get_inforedirect().getContentEncoding().equals(""))
			    				response.setHeader("Content-Type",current.get_inforedirect().getContentType().toLowerCase()+
			    						((current.get_inforedirect().getContentEncoding()!=null)?";charset="+current.get_inforedirect().getContentEncoding():"")
			    				);
		    				else
		    					response.setHeader("Content-Type",current.get_inforedirect().getContentType().toLowerCase());
	    				}else{
		    				response.setContentType("Application/"+current.get_inforedirect().getContentType().toLowerCase());
		    				response.setHeader("Content-Type","Application/"+current.get_inforedirect().getContentType().toLowerCase()+
		    						((current.get_inforedirect().getContentEncoding()!=null)?";charset="+current.get_inforedirect().getContentEncoding():"")
		    				);
	    				}
	    			}
*/	    			
	    			if(current.get_inforedirect().getContentType()!=null && !current.get_inforedirect().getContentType().equals("")){
	    				if(current.get_inforedirect().getContentEncoding()!=null && !current.get_inforedirect().getContentEncoding().equals(""))
		    				response.setHeader("Content-Type",current.get_inforedirect().getContentType().toLowerCase()+
		    						((current.get_inforedirect().getContentEncoding()!=null)?";charset="+current.get_inforedirect().getContentEncoding():"")
		    				);
	    				else
	    					response.setHeader("Content-Type",current.get_inforedirect().getContentType().toLowerCase());
	    				
	    			}
	    			if(current.get_inforedirect().getContentName()!=null && !current.get_inforedirect().getContentName().equals(""))
	    				response.setHeader("Content-Disposition","attachment; filename="+current.get_inforedirect().getContentName());
	    			if(current.get_inforedirect().getContentEncoding()!=null && !current.get_inforedirect().getContentEncoding().equals(""))
	    				response.setHeader("Content-Transfer-Encoding",current.get_inforedirect().getContentEncoding());
					
					 
					 OutputStream os = response.getOutputStream();
		    		 os.write(output);
		    		 os.flush();
		    		 os.close();
	
				}
			}
		}catch(Exception e){			
		}
		return output;
	}
	

	

}
