package it.classhidra.plugin.provider;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import it.classhidra.core.controller.i_provider;
import it.classhidra.core.tool.util.util_format;

@Deprecated
public class spring implements i_provider {
	private static final long serialVersionUID = 1L;
	private ServletContext servletContext=null;
	public Object get_bean(String id_bean) {
		Object result=null;
		if(id_bean==null) return result;
		id_bean=util_format.replace(id_bean, "$", "");
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		result = wac.getBean(id_bean);		
		return result;
	}

	public void set_context(ServletContext _context) {
		servletContext=_context;
	}
	
	public static Object getInstance(String id_bean, String class_bean, ServletContext _context) {
		Object instance=null;
		if(id_bean==null) return instance;
		id_bean=util_format.replace(id_bean, "$", "");
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(_context);
		try{
			instance = wac.getBean(id_bean);	
		}catch(Exception e){			
		}
		if(instance==null && class_bean!=null){
			try{
				instance = wac.getBean(id_bean, Class.forName(class_bean));	
			}catch(Exception e){			
			}
		}
		
		return instance;
	}		
	
	

}
