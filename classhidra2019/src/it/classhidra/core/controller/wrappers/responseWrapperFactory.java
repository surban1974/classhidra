
package it.classhidra.core.controller.wrappers;





import it.classhidra.core.controller.bsController;

import javax.servlet.http.HttpServletResponse;


public class responseWrapperFactory {

	
	public static a_ResponseWrapper getWrapper(String wrapperName,   HttpServletResponse response){
		a_ResponseWrapper result = null;
		try{
			result = (a_ResponseWrapper)Class.forName(wrapperName).getConstructor(new Class[]{HttpServletResponse.class}).newInstance(new Object[]{response});
		}catch(Exception e){
			result = new bsCharResponseWrapper(response);
		}catch(Throwable e){
			result = new bsCharResponseWrapper(response);
		}
		return result;
	}
	
	public static a_ResponseWrapper getWrapper( HttpServletResponse response){
		a_ResponseWrapper result = null;
		try{
			
			result = (a_ResponseWrapper)Class.forName(bsController.getAppInit().get_transf_elaborationwrapper()).getConstructor(new Class[]{HttpServletResponse.class}).newInstance(new Object[]{response});
		}catch(Exception e){
			result = new bsCharResponseWrapper(response);
		}
		return result;
	}	
}
