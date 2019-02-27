package it.classhidra.plugin.async;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_transformation;
import it.classhidra.core.controller.info_async;
import it.classhidra.core.controller.info_redirect;
import it.classhidra.core.controller.info_stream;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.controller.wrappers.a_ResponseWrapper;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.statistic.StatisticEntity;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_reflect;


@WebServlet(urlPatterns = "/AsyncController", asyncSupported=true)
public class AsyncController extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		final info_async iAsync = (info_async)request.getAttribute(bsConstants.CONST_ASYNC_INFO);
		request.removeAttribute(bsConstants.CONST_ASYNC_INFO);
		if(iAsync==null || iAsync.getValue()==null || !iAsync.getValue().equalsIgnoreCase("true"))
			return;
		
		long timeout = 0;
		try{
			timeout = Long.valueOf(iAsync.getTimeout());
		}catch(Exception e){
		}
		
		
	
		
		request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
		
		
		final AsyncContext ac = request.startAsync(request,response);
			ac.setTimeout(timeout);

			ac.addListener(new AsyncListener() {
				
					public void onComplete(AsyncEvent event) throws IOException {
//				      System.out.println("onComplete called");
				    }
				    public void onTimeout(AsyncEvent event) throws IOException {
//				    	System.out.println("onTimeout called");
				    	((HttpServletResponse)event.getSuppliedResponse()).setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
				    	((HttpServletResponse)event.getSuppliedResponse()).flushBuffer();
				    	try{
				    		event.getAsyncContext().complete();
				    	}catch(Exception e){
				    		new bsControllerException(e, iStub.log_ERROR);
				    	}
				    }
				    public void onError(AsyncEvent event) throws IOException {
//				    	System.out.println("onError called");
				    	((HttpServletResponse)event.getSuppliedResponse()).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				    	((HttpServletResponse)event.getSuppliedResponse()).flushBuffer();
				    	try{
				    		event.getAsyncContext().complete();
				    	}catch(Exception e){
				    		new bsControllerException(e, iStub.log_ERROR);
				    	}				    	
				    }
				    public void onStartAsync(AsyncEvent event) throws IOException {
//				    	System.out.println("onStartAsync called");
				    }


			});
			
			AsyncContainer acc = new AsyncContainer()
					.setInfoAsync(iAsync)
					.setAsyncContext(ac)
					;

			ac.start(acc);

	}
	
	public static AsyncContext getAsyncContext(String id_action, String id_call, String id_complete, HttpServletRequest request, HttpServletResponse response){
		AsyncContext asyncCtx = null;
		
		if(id_complete==null){
			if(id_call==null)
				id_complete = id_action;
			else
				id_complete = id_action+
				((bsController.getAppInit().get_actioncall_separator()==null)?"":bsController.getAppInit().get_actioncall_separator())+
				id_call;
		}
		
		
		if(id_call==null){
			if(bsController.getAppInit().get_actioncall_separator()!=null && !bsController.getAppInit().get_actioncall_separator().equals("")){
				char separator=bsController.getAppInit().get_actioncall_separator().charAt(0);
				if(id_action!=null && id_action.indexOf(separator)>-1){
					try{
						id_call = id_action.substring(id_action.indexOf(separator)+1,id_action.length());
					}catch(Exception e){
					}
					id_action = id_action.substring(0,id_action.indexOf(separator));
				}
			}
		}

		
		return asyncCtx;
	}
	
	public static i_action service(String id_action, String id_call, String id_complete, AsyncContainer asyncContainer, boolean initBean)throws ServletException, UnavailableException {
		
		if(id_complete==null){
			if(id_call==null)
				id_complete = id_action;
			else
				id_complete = id_action+
				((bsController.getAppInit().get_actioncall_separator()==null)?"":bsController.getAppInit().get_actioncall_separator())+
				id_call;
		}
		
		
		if(id_call==null){
			if(bsController.getAppInit().get_actioncall_separator()!=null && !bsController.getAppInit().get_actioncall_separator().equals("")){
				char separator=bsController.getAppInit().get_actioncall_separator().charAt(0);
				if(id_action!=null && id_action.indexOf(separator)>-1){
					try{
						id_call = id_action.substring(id_action.indexOf(separator)+1,id_action.length());
					}catch(Exception e){
					}
					id_action = id_action.substring(0,id_action.indexOf(separator));
				}
			}
		}
		
		
		


		asyncContainer.getRequest().setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
		asyncContainer.getRequest().setAttribute(bsConstants.CONST_ID_COMPLETE,id_complete);
		
		auth_init auth = bsController.checkAuth_init(asyncContainer.getRequest());

		if(auth==null) auth = new auth_init();

		StatisticEntity stat = null;
		try{
			stat = new StatisticEntity(
					String.valueOf(asyncContainer.getRequest().getSession().getId()),
					auth.get_user_ip(),
					auth.get_matricola(),
					auth.get_language(),
					id_complete,
					null,
					new Date(),
					null,
					asyncContainer.getRequest());
		}catch(Exception e){
		}



		asyncContainer.getRequest().setAttribute(bsConstants.CONST_ID_CALL,id_call);
		asyncContainer.getRequest().setAttribute(bsConstants.CONST_ID,id_action);

		if(id_action!=null){

			i_action action_instance = null;

			try{
				Vector<info_stream> _streams = bsController.getActionStreams_(id_action);

				info_stream blockStreamEnter = bsController.performStream_EnterRS(_streams, id_action,action_instance, asyncContainer.getRequest().getServletContext(), asyncContainer.getRequest(), asyncContainer.getResponse());
				if(blockStreamEnter!=null){
					bsController.isException(action_instance, asyncContainer.getRequest());
					if(stat!=null){
						stat.setFt(new Date());
						stat.setException(new Exception("Blocked by STREAM ENTER:["+blockStreamEnter.getName()+"]"));
						bsController.putToStatisticProvider(stat);
					}
					return null;
				}


				action_instance = bsController.performAction(id_action, id_call, asyncContainer.getRequest().getServletContext(), asyncContainer, initBean);

				if(action_instance==null){
					bsController.isException(action_instance, asyncContainer.getRequest());
					if(stat!=null){
						stat.setFt(new Date());
						stat.setException(new Exception("ACTION INSTANCE is NULL"));
						bsController.putToStatisticProvider(stat);
					}
					return action_instance;
				}

				if(	action_instance.get_infoaction()!=null &&
					action_instance.get_infoaction().getStatistic()!=null &&
					action_instance.get_infoaction().getStatistic().equalsIgnoreCase("false")) stat=null;




				if(asyncContainer.getRequest().getAttribute(bsConstants.CONST_BEAN_$INSTANCEACTIONPOOL)==null)
					asyncContainer.getRequest().setAttribute(bsConstants.CONST_BEAN_$INSTANCEACTIONPOOL,new HashMap<String,i_action>());
				@SuppressWarnings("unchecked")
				HashMap<String,i_action> included_pool = (HashMap<String,i_action>)asyncContainer.getRequest().getAttribute(bsConstants.CONST_BEAN_$INSTANCEACTIONPOOL);
				if(action_instance.get_infoaction()!=null && action_instance.get_infoaction().getName()!=null)
					included_pool.put(action_instance.get_infoaction().getName(),action_instance);
				else if(action_instance.get_infoaction()!=null && action_instance.get_infoaction().getPath()!=null)
					included_pool.put(action_instance.get_infoaction().getPath(),action_instance);

				asyncContainer.getRequest().setAttribute(bsConstants.CONST_BEAN_$INSTANCEACTION,action_instance);

				if(asyncContainer.getRequest().getParameter(bsConstants.CONST_ID_JS4AJAX)==null && action_instance!=null && action_instance.get_bean()!=null)
					asyncContainer.getRequest().setAttribute(bsConstants.CONST_ID_JS4AJAX,action_instance.get_bean().getJs4ajax());



//				if(action_instance.getCurrent_redirect()!=null){
//					if( !action_instance.getCurrent_redirect().is_avoidPermissionCheck()){
						info_stream blockStreamExit = bsController.performStream_ExitRS(_streams, id_action,action_instance, asyncContainer.getRequest().getServletContext(), asyncContainer.getRequest(), asyncContainer.getResponse());
						if(blockStreamExit!=null){
							bsController.isException(action_instance, asyncContainer.getRequest());
							if(stat!=null){
								stat.setFt(new Date());
								stat.setException(new Exception("Blocked by STREAM EXIT:["+blockStreamExit.getName()+"]"));
								bsController.putToStatisticProvider(stat);
							}
							return action_instance;
						}
//					}
//				}
				if(action_instance.getCurrent_redirect()!=null){
					asyncContainer.setResponse(dispatchAsyncContext(asyncContainer,action_instance,true));
				}else if(action_instance.get_infoaction()!=null && action_instance.get_infoaction().getIRedirect()!=null){
					bsController.updateResponseContentType(action_instance.get_infoaction().getIRedirect(),asyncContainer.getResponse(),0);
				}

				return action_instance;

				
			}catch(bsControllerException e){
				if(asyncContainer.getRequest().getAttribute(bsConstants.CONST_BEAN_$ERRORACTION)==null) asyncContainer.getRequest().setAttribute(bsConstants.CONST_BEAN_$ERRORACTION, e.toString());
				else asyncContainer.getRequest().setAttribute(bsConstants.CONST_BEAN_$ERRORACTION, asyncContainer.getRequest().getAttribute(bsConstants.CONST_BEAN_$ERRORACTION) + ";" +e.toString());
				new bsControllerException(e,iStub.log_ERROR);
				bsController.isException(action_instance, asyncContainer.getRequest());
				bsController.addAsMessage(e,asyncContainer.getRequest());
				if(asyncContainer.getRequest().getSession().getAttribute(bsConstants.CONST_BEAN_$LISTMESSAGE)!=null) asyncContainer.getRequest().getSession().removeAttribute(bsConstants.CONST_BEAN_$LISTMESSAGE);
				service_ErrorDispatch(id_action,asyncContainer);
				stat.setException(e);
			}catch(Exception ex){
				if(asyncContainer.getRequest().getAttribute(bsConstants.CONST_BEAN_$ERRORACTION)==null) asyncContainer.getRequest().setAttribute(bsConstants.CONST_BEAN_$ERRORACTION, ex.toString());
				else asyncContainer.getRequest().setAttribute(bsConstants.CONST_BEAN_$ERRORACTION, asyncContainer.getRequest().getAttribute(bsConstants.CONST_BEAN_$ERRORACTION) + ";" +ex.toString());

				new bsControllerException(ex,iStub.log_ERROR);
				bsController.isException(action_instance, asyncContainer.getRequest());
				bsController.addAsMessage(ex,asyncContainer.getRequest());

				service_ErrorDispatch(id_action,asyncContainer);
				stat.setException(ex);
			}catch(Throwable t){
				if(asyncContainer.getRequest().getAttribute(bsConstants.CONST_BEAN_$ERRORACTION)==null) asyncContainer.getRequest().setAttribute(bsConstants.CONST_BEAN_$ERRORACTION, t.toString());
				else asyncContainer.getRequest().setAttribute(bsConstants.CONST_BEAN_$ERRORACTION, asyncContainer.getRequest().getAttribute(bsConstants.CONST_BEAN_$ERRORACTION) + ";" +t.toString());

				new bsControllerException(t,iStub.log_ERROR);
				bsController.isException(action_instance, asyncContainer.getRequest());
				bsController.addAsMessage(t,asyncContainer.getRequest());
				service_ErrorDispatch(id_action,asyncContainer);
				stat.setException(t);
			}finally{
				if(stat!=null){
					stat.setFt(new Date());
					bsController.putToStatisticProvider(stat);
				}
			}


		}
		return null;
	}


	public static HttpServletResponse dispatchAsyncContext(AsyncContainer asyncContainer, i_action action_instance,boolean allowAnotherOutput) throws Exception, bsControllerException,ServletException, UnavailableException{

		
		if(action_instance==null || action_instance.get_infoaction()==null) 
			return asyncContainer.getResponse();
		boolean intoWrapper=false;
		Object[] resultC4AOutputMode = bsController.chech4AnotherOutputMode(action_instance, asyncContainer.getRequest().getServletContext(), asyncContainer.getRequest(), asyncContainer.getResponse(), allowAnotherOutput);

		if(((Boolean)resultC4AOutputMode[1]).booleanValue()){
			return asyncContainer.getResponse();
		}

		if(resultC4AOutputMode[0] instanceof a_ResponseWrapper){
			asyncContainer.setResponse((a_ResponseWrapper)resultC4AOutputMode[0]);
			intoWrapper=true;
		}

		action_instance.onPreRedirect();
		
		String rd = null;
		
		try{
			redirects current = action_instance.getCurrent_redirect();
			if(current!=null){
				info_redirect fake = new info_redirect().setContentType(current.getContentType()).setContentName(current.getContentName()).setContentEncoding(current.getContentEncoding());
				if(current.get_inforedirect()!=null){
					if(current.get_inforedirect().getContentType()!=null && !current.get_inforedirect().getContentType().equals(""))
						fake.setContentType(current.get_inforedirect().getContentType());
					if(current.get_inforedirect().getContentName()!=null && !current.get_inforedirect().getContentName().equals(""))
						fake.setContentName(current.get_inforedirect().getContentName());		
					if(current.get_inforedirect().getContentEncoding()!=null && !current.get_inforedirect().getContentEncoding().equals(""))
						fake.setContentEncoding(current.get_inforedirect().getContentEncoding());				
					bsController.updateResponseContentType(fake,asyncContainer.getResponse(),current.getResponseStatus());
				}
			}
			
			
			rd = action_instance.getCurrent_redirect().getDispatchedURI( action_instance.get_infoaction());
		}catch(Exception ex){
			if(bsController.getAppInit().get_permit_redirect_resource()!=null && bsController.getAppInit().get_permit_redirect_resource().equalsIgnoreCase("true")){
				try{
					redirects current = action_instance.getCurrent_redirect();
					i_transformation resource2response = null;
					if(current.get_inforedirect().getTransformationName()!=null && !current.get_inforedirect().getTransformationName().equals(""))
						resource2response =  bsController.getAction_config().transformationFactory(current.get_inforedirect().getTransformationName(), asyncContainer.getRequest().getServletContext());
					else	
						resource2response =  bsController.getAction_config().transformationFactory("resource2response", asyncContainer.getRequest().getServletContext());
					if(resource2response!=null){
						if(resource2response.transform(action_instance, asyncContainer.getRequest(), asyncContainer.getResponse())!=null){
							try{
								action_instance.onPostRedirect(null);
							}catch(Exception e){							
							}
				    		return asyncContainer.getResponse();
						}
					}
				}catch(Exception ex1){
					throw ex;
				}
			}
			action_instance.onPreRedirectError();
			throw ex;
		}
		
		HashMap<String,Object> request2map = null;
		boolean isRemoteEjb=false;
		
		if(action_instance!=null && action_instance.getInfo_context()!=null && action_instance.getInfo_context().isRemote()){
			isRemoteEjb=true;
			try{
				request2map = convertRequestToMap(action_instance.asAction().getClass(),new Class[]{HttpServletRequest.class}, new Object[]{asyncContainer.getRequest()});

//				request2map = (HashMap)
//						util_reflect.findDeclaredMethod(
//							action_instance.asAction().getClass(),
//							"convertRequest2Map", new Class[]{HttpServletRequest.class})
//						.invoke(null, new Object[]{asyncContainer.getRequest()});
			}catch (Exception e) {
				new bsControllerException(e, iStub.log_ERROR);
			}catch (Throwable e) {
				new bsControllerException(e, iStub.log_ERROR);
			}
			if(request2map==null)
				request2map = new HashMap<String, Object>();

		}		
		
		try{
			if(!isRemoteEjb)
				action_instance.onPostRedirect(null);
		}catch(Exception e){
		}
			
		if(rd==null){
			if(action_instance.getCurrent_redirect().get_uri()!=null && !action_instance.getCurrent_redirect().get_uri().trim().equals("")){
				action_instance.onPreRedirectError();
				rd = action_instance.getCurrent_redirect().getDispatchedErrorURI( action_instance.get_infoaction());
				try{
					if(!isRemoteEjb)
						action_instance.onPostRedirectError(null);
				}catch(Exception e){
				}
			}
		}
		if(rd==null){
			if(action_instance.getCurrent_redirect().get_uri()!=null && !action_instance.getCurrent_redirect().get_uri().trim().equals("")){
				if(!action_instance.get_infoaction().getError().equals("")) 
					action_instance.getCurrent_redirect().set_uriError(action_instance.get_infoaction().getError());
				else 
					action_instance.getCurrent_redirect().set_uriError(bsController.getAction_config().getAuth_error());
				rd = action_instance.getCurrent_redirect().getDispatchedErrorURI(action_instance.get_infoaction());
			}
		}

		if(rd==null){
			if(action_instance.getCurrent_redirect().get_uri()!=null && !action_instance.getCurrent_redirect().get_uri().trim().equals(""))
				throw new bsControllerException("Controller generic redirect error. Action: ["+action_instance.get_infoaction().getPath()+"] " +action_instance.getCurrent_redirect(),asyncContainer.getRequest(),iStub.log_ERROR);
		}else{
			try{
				try{
					try{
						if(!isRemoteEjb)
							action_instance.actionBeforeRedirect(asyncContainer.getRequest(),asyncContainer.getResponse());
						else
							action_instance.actionBeforeRedirect(request2map);
					}catch(Exception e){
						action_instance.actionBeforeRedirect(null,null);
					}
				}catch(Exception e){
					throw new bsControllerException("Controller generic actionBeforeRedirect error. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),asyncContainer.getRequest(),iStub.log_ERROR);
				}

				
				asyncContainer.getAsyncContext().dispatch(asyncContainer.getRequest().getServletContext(), rd);
				asyncContainer.setAsyncDispatched(true);
				



			}catch(Exception e){
				if(intoWrapper){
					throw new bsControllerException("Controller generic wrapped redirect error. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),iStub.log_ERROR);
				}else
					throw new bsControllerException("Controller generic redirect error. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),asyncContainer.getRequest(),iStub.log_ERROR);
			}
		}
		return asyncContainer.getResponse();
	}
	
	public static void service_ErrorDispatch(String id_action,AsyncContainer asyncContainer){
		HttpServletRequest request = (HttpServletRequest)asyncContainer.getAsyncContext().getRequest();
		ServletContext servletContext = request.getServletContext();
		String redirectURI="";
		try{
			if(bsController.getAction_config()==null || bsController.getAction_config().getError()==null || bsController.getAction_config().getError().equals(""))
				redirectURI="/error.jsp";
			else redirectURI = bsController.getAction_config().getError();
			asyncContainer.getAsyncContext().dispatch(servletContext,redirectURI);
		}catch(Exception ex){
			bsController.writeLog(request, "Controller generic redirect error. Action: ["+id_action+"] URI: ["+redirectURI+"]");
		}
	}	
	
	@SuppressWarnings("unchecked")
	public static HashMap<String,Object> convertRequestToMap(Class<?> clazz,  Class<?>[] types, Object[] paramiters) throws Exception {
		return
				(HashMap<String,Object>)
					util_reflect.findDeclaredMethod(
							clazz,
							"convertRequest2Map",
							types)
					.invoke(null, paramiters);
	}
}
