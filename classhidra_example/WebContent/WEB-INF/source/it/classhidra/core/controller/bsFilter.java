/**
* Creation date: (07/04/2006)
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com 
*/

/********************************************************************************
*
*	Copyright (C) 2005  Svyatoslav Urbanovych
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*********************************************************************************/
package it.classhidra.core.controller;



import it.classhidra.core.controller.wrappers.a_ResponseWrapper;

import it.classhidra.core.controller.wrappers.responseWrapperFactory;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.statistic.StatisticEntity;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





public class bsFilter implements Filter {
	private FilterConfig config = null;
	public final static String CONST_INIT_CONTENT_TYPE 						= "ContentType";
	public final static String CONST_INIT_CHARACTER_ENCODING 				= "CharacterEncoding";


		public void init(FilterConfig config) throws ServletException {
			this.config = config;
		}

		public void destroy() {

		}
		public void doFilter(
			ServletRequest req,
			ServletResponse resp,
			FilterChain chain)
			throws ServletException, IOException {

			if(req instanceof HttpServletRequest && resp instanceof HttpServletResponse){

				HttpServletRequest request = (HttpServletRequest)req;
				HttpServletResponse response = (HttpServletResponse)resp;

				

				
				String def_ContentType = config.getInitParameter(CONST_INIT_CONTENT_TYPE);
				String def_CharacterEncoding = config.getInitParameter(CONST_INIT_CHARACTER_ENCODING);
				if(def_ContentType!=null){
					if(def_ContentType.toLowerCase().equals("text/html")){
	    				response.setContentType(def_ContentType.toLowerCase());
	    				response.setHeader("Content-Type",def_ContentType.toLowerCase()+
	    						((def_CharacterEncoding!=null)?";charset="+def_CharacterEncoding:"")
	    				);
    				}else{
	    				response.setContentType("Application/"+def_ContentType.toLowerCase());
	    				response.setHeader("Content-Type","Application/"+def_ContentType.toLowerCase()+
	    						((def_CharacterEncoding!=null)?";charset="+def_CharacterEncoding:"")
	    				);
    				}
				}
				
				if(def_CharacterEncoding!=null){
					request.setCharacterEncoding(def_CharacterEncoding);
					response.setHeader("Content-Transfer-Encoding",def_CharacterEncoding);
					response.setCharacterEncoding(def_CharacterEncoding);
				}				
				
				String id_bs = null;
				String url="";
				url = (String)request.getAttribute("javax.servlet.include.request_uri");
				if(url==null)	url = request.getRequestURI();
				boolean elaborateBsCheck=true;
				
				if(url!=null){
					if(url.indexOf("?")>-1){
						url=url.substring(0,url.indexOf("?"));
					}
					if(bsController.getAppInit().get_extention_do().equals("")){
						if(url.indexOf(".")>-1) elaborateBsCheck=false;
					}else{
						if(url.indexOf(bsController.getAppInit().get_extention_do())==-1 && url.indexOf(".")>-1) elaborateBsCheck=false;
					}
				}
				
				if(elaborateBsCheck){
					id_bs=check_BS(request);		
					if(id_bs!=null){
						if(elaborate_BS(id_bs,chain,request,response)) return;
					}
					
				}
				if(elaborate_neoHort(id_bs,chain,request,response)) return;
				if(id_bs==null){
					try{
						chain.doFilter(req, resp);
					}catch(Exception e){
						if(request.getAttribute(bsController.CONST_BEAN_$ERRORACTION)==null) request.setAttribute(bsController.CONST_BEAN_$ERRORACTION, e.toString());
						else request.setAttribute(bsController.CONST_BEAN_$ERRORACTION, request.getAttribute(bsController.CONST_BEAN_$ERRORACTION) + ";" +e.toString());
						
					}catch(Throwable t){
						if(request.getAttribute(bsController.CONST_BEAN_$ERRORACTION)==null) request.setAttribute(bsController.CONST_BEAN_$ERRORACTION, t.toString());
						else request.setAttribute(bsController.CONST_BEAN_$ERRORACTION, request.getAttribute(bsController.CONST_BEAN_$ERRORACTION) + ";" +t.toString());
						
					}
				}
			}
		}

		


		
		public boolean elaborate_BS(String id_current, FilterChain chain,HttpServletRequest request, HttpServletResponse response) throws ServletException{
			boolean result=false;
			if(id_current!=null){
				try{
					HttpServletResponse responseWrapped = null;
					String def_TransformationElPoint = bsController.getAppInit().get_transf_elaborationpoint();
					if(def_TransformationElPoint==null || def_TransformationElPoint.trim().length()==0) def_TransformationElPoint=bsConstants.CONST_TRANSFORMATION_ELPOINT_CONTROLLER;
					
					responseWrapped = bsController.service(id_current, request.getSession().getServletContext(), request,response);

					if(responseWrapped instanceof a_ResponseWrapper){
						
						StatisticEntity stat = null;
						try{
							auth_init auth = bsController.checkAuth_init(request);
							if(auth==null) auth = new auth_init();

							stat = new StatisticEntity(
									String.valueOf(request.getSession().getId()),
									auth.get_user_ip(),
									auth.get_matricola(),
									auth.get_language(),
									"WRAPPED",
									null,
									new Date(),
									null,
									request);
						}catch(Exception e){
						}
						

						String htmlSource = ((a_ResponseWrapper)responseWrapped).toString();
						transformation cTransformation = (transformation)request.getAttribute(bsConstants.CONST_ID_TRANSFORMATION4WRAPPER);
						
						if(cTransformation!=null){
							request.removeAttribute(bsConstants.CONST_ID_TRANSFORMATION4WRAPPER);
							
							byte[] outTransformation = cTransformation.transform(htmlSource, request, response);
							cTransformation.setOutputcontent(outTransformation);
							if(def_TransformationElPoint.toLowerCase().equals(bsConstants.CONST_TRANSFORMATION_ELPOINT_FILTER)){
								if(cTransformation.getOutputcontent()!=null){

									try{
						    			if(cTransformation.getContentType()!=null){
						    				if(cTransformation.getContentType().toLowerCase().equals("text/html")){
							    				response.setContentType(cTransformation.getContentType().toLowerCase());
							    				response.setHeader("Content-Type",cTransformation.getContentType().toLowerCase()+
							    						((cTransformation.getContentTransferEncoding()!=null)?";charset="+cTransformation.getContentTransferEncoding():"")
							    				);
						    				}else{
							    				response.setContentType("Application/"+cTransformation.getContentType().toLowerCase());
							    				response.setHeader("Content-Type","Application/"+cTransformation.getContentType().toLowerCase()+
							    						((cTransformation.getContentTransferEncoding()!=null)?";charset="+cTransformation.getContentTransferEncoding():"")
							    				);
						    				}
						    			}
						    			if(cTransformation.getContentName()!=null) response.setHeader("Content-Disposition","attachment; filename="+cTransformation.getContentName());
						    			if(cTransformation.getContentTransferEncoding()!=null) response.setHeader("Content-Transfer-Encoding",cTransformation.getContentTransferEncoding());

										
										 OutputStream os = response.getOutputStream();
							    		 os.write(cTransformation.getOutputcontent()); 
							    		 os.flush();
							    		 os.close();

							    		 
									}catch(Exception e){
									}catch(Throwable t){
									}
								}
							}else{
								String idInSession= util_format.dataToString(new Date(),"yyyyMMddHHmmsss");
								request.getSession().setAttribute(bsConstants.CONST_ID_TRANSFORMATION4CONTROLLER+"_"+idInSession,cTransformation);
								response.sendRedirect("Controller?$action=bsTransformation&id="+idInSession);
							}
						}
						
						if(stat!=null){
							stat.setFt(new Date());
							bsController.putToStatisticProvider(stat);
						}
						

					}
					
				}catch(Exception e){	
					if(request.getAttribute(bsController.CONST_BEAN_$ERRORACTION)==null) request.setAttribute(bsController.CONST_BEAN_$ERRORACTION, e.toString());
					else request.setAttribute(bsController.CONST_BEAN_$ERRORACTION, request.getAttribute(bsController.CONST_BEAN_$ERRORACTION) + ";" +e.toString());
					new bsControllerException("Filter sendRedirect error.  ->" +e.toString(),iStub.log_ERROR);

				}catch(Throwable t){	
					if(request.getAttribute(bsController.CONST_BEAN_$ERRORACTION)==null) request.setAttribute(bsController.CONST_BEAN_$ERRORACTION, t.toString());
					else request.setAttribute(bsController.CONST_BEAN_$ERRORACTION, request.getAttribute(bsController.CONST_BEAN_$ERRORACTION) + ";" +t.toString());
					new bsControllerException("Filter sendRedirect error.  ->" +t.toString(),iStub.log_ERROR);
				}
				return true;
			}
			return result;
		}

		public boolean elaborate_neoHort(String id_current,FilterChain chain, HttpServletRequest request, HttpServletResponse response) throws ServletException{
			boolean result=false;
			String url = request.getRequestURI();
			String xmlSource=null;
			if(	url!=null){
				if(	url.toUpperCase().lastIndexOf("/REPORT_CREATOR")==-1 &&
					(url.toUpperCase().indexOf("REPORTPROVIDER=NEOHORT")>-1 ||
					(request.getParameter("ReportProvider")!=null && request.getParameter("ReportProvider").equals("neoHort"))
					)
				){
					StatisticEntity stat = null;
					if(System.getProperty("application.log.stub")==null || System.getProperty("application.log.stub").equals(""))
						System.setProperty("application.log.stub","it.classhidra.core.tool.log.stubs.stub_neoHort_log");
					try{
						
						
						try{
							auth_init auth = bsController.checkAuth_init(request);
							if(auth==null) auth = new auth_init();

							stat = new StatisticEntity(
									String.valueOf(request.getSession().getId()),
									auth.get_user_ip(),
									auth.get_matricola(),
									auth.get_language(),
									"REPORTPROVIDER",
									null,
									new Date(),
									null,
									request);
						}catch(Exception e){
						}
						
						a_ResponseWrapper responseWrapper =  responseWrapperFactory.getWrapper(response);
						if(id_current!=null)
							bsController.service(id_current, request.getSession().getServletContext(), request,responseWrapper);
						else chain.doFilter(request, responseWrapper);
						xmlSource = responseWrapper.toString();
						xmlSource = analizeXML4neoHort(xmlSource);

						if(response.isCommitted()){
							request.setAttribute("$source_stream",xmlSource);
							request.getSession().getServletContext().getRequestDispatcher("/report_creator").include(request,response);

//							response.getOutputStream().print("<center><img src=\"../images/wait.gif\"><form action=\"report_creator.bs\" method=\"POST\"><div style='visibility:hidden'><textarea name=\"$source_stream\" type=\"hidden\">"+normalXML(xmlSource)+"</textarea></div></form><script>document.forms[0].submit();</script>");
	/*
	  						iHort report = new iHort();
	 						ByteArrayOutputStream baos = new ByteArrayOutputStream();
							report.transformXMLtoReport(xmlSource,baos);

							prepResponseContent(report.get_tagLibrery(),response);
							baos.writeTo(response.getOutputStream());
							baos.close();
	*/
						}else{
							request.setAttribute("$source_stream",xmlSource);
							request.getSession().getServletContext().getRequestDispatcher("/report_creator").forward(request,response);
						}
						return true;
					}catch(Exception ex){
						stat.setException(ex);
						try{
							response.getWriter().write(ex.toString());
						}catch(Exception exp){}
					}
					if(stat!=null){
						stat.setFt(new Date());
						bsController.putToStatisticProvider(stat);
					}

				}
			}
			return result;
		}

	/*
		private void prepResponseContent(Hashtable _tags, HttpServletResponse response) throws Exception{
			if(_tags==null) return;
			Enumeration enum = _tags.keys();
			Object gen = null;
			while(enum.hasMoreElements() && gen==null){
				String key = (String)enum.nextElement();
				if(key.toUpperCase().indexOf("GENERAL:")==0) gen = _tags.get(key);
			}
			if(gen!=null){

				String TYPE_DOCUMENT = (String) util_reflect.getValue(gen,"getTYPE_DOCUMENT",null);
				String LIB = (String) util_reflect.getValue(gen,"getLIB",null);
				String ID = (String) util_reflect.getValue(gen,"getID",null);
				if (TYPE_DOCUMENT!=null && TYPE_DOCUMENT.trim().equalsIgnoreCase("ATTACHMENT")){
					response.setHeader("Content-Disposition","attachment; filename="+ID+"."+LIB);
					response.setHeader("Content-Transfer-Encoding","base64");
					response.setContentType("Application/"+LIB);
				}
				if (TYPE_DOCUMENT!=null && TYPE_DOCUMENT.trim().equalsIgnoreCase("STREAM")){
					response.setHeader("Content-Type","Application/"+LIB);
				}
			}
		}
	*/
		
		public String analizeXML4neoHort(String input){
			if(	input!=null && input.toUpperCase().indexOf("<?XML")!=0 && input.toUpperCase().indexOf("<?XML")>-1){
				input = input.substring(input.toUpperCase().indexOf("<?XML"));
			}	
				if(input.toUpperCase().indexOf("<CONTENT>")>0){
					String tmp_input="";
					
					int current_position=0;					
					
					int current_position_starttagdata=input.toUpperCase().indexOf("<CONTENT>",current_position);
					while(current_position_starttagdata>-1){
						tmp_input+= input.substring(current_position,current_position_starttagdata);
						
						current_position_starttagdata+=9;
						int current_position_finishtagdata = input.toUpperCase().indexOf("</CONTENT>",current_position);
						
						
						
						if(current_position_finishtagdata>current_position_starttagdata){ 
							current_position = current_position_finishtagdata+10;
						
						
						
							String tag_content_data = null;
							if(current_position_starttagdata<input.length() && current_position_finishtagdata<input.length()){
								tag_content_data = input.substring(current_position_starttagdata,current_position_finishtagdata);
								if(tag_content_data.indexOf("<![CDATA[")==-1 || tag_content_data.indexOf("]]>")==-1) 
									tag_content_data = "<![CDATA["+tag_content_data+"]]>";
							}
							tmp_input+="<CONTENT>"+tag_content_data+"</CONTENT>";
						}else{
							current_position = current_position_starttagdata;
							tmp_input+="<CONTENT>";
						}
						current_position_starttagdata=input.toUpperCase().indexOf("<CONTENT>",current_position);
					}					
					tmp_input+=input.substring(current_position,input.length());
					input=tmp_input;
				}
				
	
				return input;

		}

		public String check_BS(HttpServletRequest request) throws ServletException{
			String id_current = null;
			String url = null;
			boolean isIncluded = false;
			url = (String)request.getAttribute("javax.servlet.include.request_uri");
			if(url==null)	url = request.getRequestURI();
			else isIncluded=true;
			
			
			

			if(	request.getMethod()!=null){
				if(isIncluded) request.setAttribute(bsConstants.CONST_ID_REQUEST_TYPE, bsConstants.CONST_REQUEST_TYPE_INCLUDE);
				
				if(id_current==null){
					try{
						if(url.lastIndexOf("/actions")+8==url.length()) url+="/";
						if(url.lastIndexOf("/")>-1) id_current = url.substring(url.lastIndexOf("/")+1);
					}catch(Exception e){}
				}
				if(id_current!=null){	
					boolean checkExt=true;
					if(bsController.getAppInit().get_extention_do()==null || bsController.getAppInit().get_extention_do().trim().equals("")){
						checkExt=false;						
					}
					id_current=id_current.trim();
					if(checkExt){
						int lastInOf = id_current.lastIndexOf(bsController.getAppInit().get_extention_do());
						if(lastInOf>-1){
							String f_id_current=id_current.substring(0,lastInOf);
							if(f_id_current.length()+bsController.getAppInit().get_extention_do().length()!=id_current.length())
								return null;
							id_current=f_id_current;
							
							if(load_actions.get_actions().get(id_current)!=null)
								return id_current;
							else return null;
						}
					}
					if(	
						(id_current.equals("") && url.lastIndexOf("/actions/")+9==url.length()) ||
						(id_current.equals("") && url.lastIndexOf("actions/")+8==url.length()) ||
						(id_current.equals("") && url.lastIndexOf("/")+1==url.length())
					){
						id_current = bsController.getAppInit().get_enterpoint();
						if(load_actions.get_actions().get(id_current)!=null)
							return id_current;
						else return null;
					}
				}
			}
			if(load_actions.get_actions().get(id_current)!=null)
				return id_current;
			else return null;

		}
		
		

}
