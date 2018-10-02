package it.classhidra.plugin.tagrendering.jasper2;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.jsp.PageContext;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_provider;
import it.classhidra.core.controller.i_tag_helper;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsTagEndRendering;
import it.classhidra.core.tool.log.stubs.iStub;





public class TagComponentRenderingProvider implements i_provider {
	private ServletContext servletContext=null;
	private Map components = new ConcurrentHashMap();
	
	
	
	private final i_tag_helper helper = new i_tag_helper() {
		private static final long serialVersionUID = 1L;

		public String getTagExecutor(final String callerClassName, final boolean fullpage) {		
			final StackTraceElement[] stel = new Exception().getStackTrace();
			boolean foundCaller=false;
			String executorClassName=null;
			for(int i=0;i<stel.length;i++) {
				if(fullpage) {
					if(!foundCaller && stel[i].getClassName().equals(callerClassName))
						foundCaller=true;
					if(foundCaller && executorClassName==null && !stel[i].getClassName().equals(callerClassName))
						executorClassName = stel[i].getClassName();
					if(foundCaller && executorClassName!=null && !stel[i].getClassName().equals(executorClassName) && (i-1)>=0)
						return stel[i-1].getClassName()+":"+stel[i-1].getMethodName();					
				}else {
					if(!foundCaller && stel[i].getClassName().equals(callerClassName))
						foundCaller=true;
					if(foundCaller && !stel[i].getClassName().equals(callerClassName))
						return stel[i].getClassName()+":"+stel[i].getMethodName();
				}
			}
			return null;
		}
		

		public String getTagContent(final String key, final String info, final i_action action_instance, final HttpServletRequest request,
				final HttpServletResponse response) {
			if(info==null || !info.contains(":"))
				return "ERROR: COMPONENT ["+key+"] CANNOT BE FOUND. Caused by [UNABLE FIND CALLER]";
			String[] arrayOfString = info.split(":");
			if(arrayOfString.length!=2)
				return "ERROR: COMPONENT ["+key+"] CANNOT BE FOUND. Caused by [UNABLE FIND CALLER]";
			try{
				
				if(request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTIONPOOL)==null)
					request.setAttribute(bsController.CONST_BEAN_$INSTANCEACTIONPOOL,new HashMap());
				final HashMap included_pool = (HashMap)request.getAttribute(bsController.CONST_BEAN_$INSTANCEACTIONPOOL);
				if(action_instance.get_infoaction()!=null && action_instance.get_infoaction().getName()!=null)
					included_pool.put(action_instance.get_infoaction().getName(),action_instance);
				else if(action_instance.get_infoaction()!=null && action_instance.get_infoaction().getPath()!=null)
					included_pool.put(action_instance.get_infoaction().getPath(),action_instance);
				request.setAttribute(bsController.CONST_BEAN_$INSTANCEACTION,action_instance);

				
				final ClPageContext clPageContext = new ClPageContext(request, new ResponseWrapper(response) );
				
				final Object o_provider = components.get(arrayOfString[0]);
				final Class c_provider = o_provider.getClass();

				
				Method m_getInstance = null;
				for(final Method method : c_provider.getDeclaredMethods()) {
					if(method.getName().equals(arrayOfString[1])) {
						m_getInstance = method;
						break;
					}
				}
				if(m_getInstance!=null) {
					boolean setPC = false;
					final Object[] parameters = new Object[m_getInstance.getParameterTypes().length];
					for(int i=0;i<m_getInstance.getParameterTypes().length;i++) {
						final Class parameter = m_getInstance.getParameterTypes()[i];
						if(parameter.isAssignableFrom(PageContext.class)) {						
							parameters[i]= clPageContext;
							setPC=true;
						}else if(parameter.isAssignableFrom(HttpServletRequest.class)) {						
							parameters[i]= request;
						}else if(parameter.isAssignableFrom(HttpServletResponse.class)) {						
							parameters[i]= new ResponseWrapper(response) ;
						}else {
							parameters[i]=null;
						}
					}
					boolean changedAccessible = false;
					if(!m_getInstance.isAccessible()) {
						m_getInstance.setAccessible(true);
						changedAccessible=true;
					}
					
					if(!setPC){
						request.setAttribute(i_tag_helper.CONST_TAG_PAGE_CONTEXT, clPageContext);
						request.setAttribute(i_tag_helper.CONST_TAG_COMPONENT_ID, key.split(":")[2]);
					}
					try {
						m_getInstance.invoke(o_provider, parameters);
					}catch(Exception e) {
						if(e instanceof bsTagEndRendering || e.getCause().toString().indexOf("bsTagEndRendering")>-1) {
								
						}else {
							if(changedAccessible)
								m_getInstance.setAccessible(false);
							throw e;
						}
					}
					if(changedAccessible)
						m_getInstance.setAccessible(false);
					final String out = ((ClJspWriter)clPageContext.getOut()).getAsString();
					clPageContext.getOut().close();
					return out;

					
				}
			}catch (Exception e) {
				new bsControllerException(e, iStub.log_ERROR);
				return "ERROR: COMPONENT ["+key+"] CANNOT BE FOUND. Caused by ["+e.toString()+"]";
			}catch (Throwable e) {
				new bsControllerException(e, iStub.log_ERROR);
				return "ERROR: COMPONENT ["+key+"] CANNOT BE FOUND. Caused by ["+e.toString()+"]";
			}
			
			return "ERROR: COMPONENT ["+key+"] CANNOT BE FOUND";
		}
	};
	
	
	class ResponseWrapper extends HttpServletResponseWrapper {
		
		private PrintWriter pw;
		
		ResponseWrapper(HttpServletResponse response){
			super(response);
			pw = new PrintWriter(new StringWriter());
		}

		public PrintWriter getWriter() throws IOException {
			return pw;
		}

		public boolean isCommitted() {
			return false;
		}
	}
	
	public Object get_bean(String id_bean) {
		if(id_bean.equals(i_tag_helper.CONST_TAG_EXECUTORS))
			return components;
		if(id_bean.equals(i_tag_helper.CONST_TAG_HELPER))
			return helper;		
		return null;
	}

	public void set_context(ServletContext _context) {
		servletContext=_context;
	}
	

	

}
