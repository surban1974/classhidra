package it.classhidra.plugin.provider;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import it.classhidra.core.controller.i_provider;
import it.classhidra.core.tool.util.util_format;

public class spring implements i_provider {
	private ServletContext servletContext=null;
	public Object get_bean(String id_bean) {
		Object result=null;
		if(id_bean==null) return result;
//		id_bean=id_bean.replace("$", "");
		id_bean=util_format.replace(id_bean, "$", "");
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

		result = wac.getBean(id_bean);		
	
		return result;
	}

	public void set_context(ServletContext _context) {
		servletContext=_context;
	}

}
