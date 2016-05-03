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




import it.classhidra.annotation.elements.Parameter;
import it.classhidra.core.controller.wrappers.a_ResponseWrapper;
import it.classhidra.core.controller.wrappers.responseWrapperFactory;
import it.classhidra.core.init.app_init;
import it.classhidra.core.init.auth_init;
import it.classhidra.core.init.db_init;
import it.classhidra.core.init.log_init;
import it.classhidra.core.init.version_init;
import it.classhidra.core.tool.db.db_connection;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.exception.message;
import it.classhidra.core.tool.jaas_authentication.load_users;
import it.classhidra.core.tool.log.i_log_generator;
import it.classhidra.core.tool.log.log_generator;
import it.classhidra.core.tool.log.statistic.I_StatisticProvider;
import it.classhidra.core.tool.log.statistic.StatisticEntity;
import it.classhidra.core.tool.log.statistic.StatisticProvider_Simple;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.scheduler.scheduling.IBatchScheduling;
import it.classhidra.core.tool.util.util_beanMessageFactory;
import it.classhidra.core.tool.util.util_classes;
import it.classhidra.core.tool.util.util_cloner;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_makeValue;
import it.classhidra.core.tool.util.util_provider;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_sort;
import it.classhidra.core.tool.util.util_supportbean;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;











import sun.misc.BASE64Encoder;



public class bsController extends HttpServlet implements bsConstants  {
	private static final long serialVersionUID = 1959097983289925130L;
	private static log_init logInit;
	private static db_init dbInit;
	private static app_init appInit;
	
	private static i_log_generator logG;
	private static load_actions action_config;
	private static load_message mess_config;
	private static Object user_config;
	private static load_authentication auth_config;
	private static load_organization org_config;
	private static load_menu menu_config;
	private static String idApp;
	
	private static I_StatisticProvider statisticProvider;
	
	private static i_provider cdiDefaultProvider;
	private static i_provider ejbDefaultProvider;

	private static ConcurrentHashMap local_container = new ConcurrentHashMap();


	private static boolean reInit=false;
	private static boolean canBeProxed=true;
	private static boolean isInitDefProfider=false;


	class LoaderConfigThreadProcess extends Thread {
	    private boolean threadDone = false;


	    public LoaderConfigThreadProcess() {
	        super();
	        threadDone=false;

	    }

	    public void run() {
	        while (!threadDone) {
	    		try {
	    			loadOnInit();
	    			threadDone=true;
	    		} catch (Exception e) {
	    			Thread.currentThread().interrupt();
	    		}
	        }
	     }

	}

	public void init() throws ServletException, UnavailableException {

		StatisticEntity stat = null;
		try{
			stat = new StatisticEntity(
					"ClassHidra",
					"",
					"",
					"",
					"init",
					null,
					new Date(),
					null,
					null);
		}catch(Exception e){
		}


		if(appInit==null){
			appInit = new app_init();
			appInit.init();
		}
			
			
			Properties initParameters = new Properties();
			Enumeration paramNames = getInitParameterNames();
			while (paramNames.hasMoreElements()){
				String name = (String) paramNames.nextElement();
				initParameters.put(name, getInitParameter(name));
			}
			if(initParameters.size()>0)
				appInit.init(initParameters);
			
			checkDefaultProvider(getServletContext());

			try{
				if(idApp==null) idApp = util_format.replace(getContextPathFor5(getServletContext()), "/", "");
			}catch(Exception ex){
			}

			boolean	loadModeAsThread=false;
			try{
				if(appInit.get_load_res_mode().equalsIgnoreCase("thread")) loadModeAsThread=true;
			}catch(Exception e){
			}
				if(loadModeAsThread)
					new LoaderConfigThreadProcess().start();
				else
					loadOnInit();

		
		if(stat!=null){
			stat.setFt(new Date());
			putToStatisticProvider(stat);
		}

	}

	public static void checkDefaultProvider(ServletContext servletContext){
		if(!isInitDefProfider){
			if(	cdiDefaultProvider==null && 
				(
					appInit.get_cdi_provider()==null || 
					(appInit.get_cdi_provider()!=null && !appInit.get_cdi_provider().equalsIgnoreCase("false"))
				)
			)
				cdiDefaultProvider = util_provider.checkDeafaultCdiProvider(appInit.get_cdi_jndi_name(), servletContext);
			if(	ejbDefaultProvider==null && 
				(
					appInit.get_ejb_provider()==null || 
					(appInit.get_ejb_provider()!=null && !appInit.get_ejb_provider().equalsIgnoreCase("false"))
				)
			)
				ejbDefaultProvider = util_provider.checkDeafaultEjbProvider(appInit.get_ejb_jndi_name(), servletContext);
			isInitDefProfider=true;
		}
	}
	
	public void loadOnInit(){

		logInit = new log_init();
		logInit.init();
		dbInit = new db_init();
			dbInit.init();
			
		checkLogGenerator(logInit);	
//		logG = new log_generator(logInit);
			if(getLogG().isReadError()) reloadLog_generator(getServletContext());


		resourcesInit(getServletContext());


		if(!getAction_config().isReadOk()) 	reloadAction_config(getServletContext());
		if(!getMess_config().isReadOk()) 	reloadMess_config(getServletContext());
		if(!getAuth_config().isReadOk()) 	reloadAuth_config(getServletContext());
		if(!getOrg_config().isReadOk()) 	reloadOrg_config(getServletContext());

		if(!getMenu_config().isReadOk()) 	reloadMenu_config(getServletContext(),null);
		



	}

	public static void resourcesInit(){
		resourcesInit(null);
	}
	public static void resourcesInit(ServletContext servletContext){

		if(getAppInit()!=null && getAppInit().get_init_loader()!=null && !getAppInit().get_init_loader().equals("")){
			i_externalloader initloader = null;
			if(getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equals("false")){
				try{
					initloader = (i_externalloader)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  app_init.id_init_loader, getAppInit().get_init_loader().trim(), servletContext);
					if(initloader!=null)
						initloader.load();
				}catch(Exception e){
				}
			}
			if(initloader==null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equals("false")){
				try{
					initloader = (i_externalloader)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  app_init.id_init_loader, getAppInit().get_init_loader().trim(), servletContext);
					if(initloader!=null)
						initloader.load();
				}catch(Exception e){
				}
			}
			if(initloader==null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equals("false")){
				try{
					initloader = (i_externalloader)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  app_init.id_init_loader, getAppInit().get_init_loader().trim(), servletContext);
					if(initloader!=null)
						initloader.load();
				}catch(Exception e){
				}
			}			
			checkDefaultProvider(servletContext);
			if(initloader==null && getCdiDefaultProvider()!=null){
				try{
					initloader = (i_externalloader)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  app_init.id_init_loader, getAppInit().get_init_loader().trim(), servletContext);
					if(initloader!=null)
						initloader.load();
				}catch(Exception e){
				}
			}
			if(initloader==null && getEjbDefaultProvider()!=null){
				try{
					initloader = (i_externalloader)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  app_init.id_init_loader, getAppInit().get_init_loader().trim(), servletContext);
					if(initloader!=null)
						initloader.load();
				}catch(Exception e){
				}
			}

			if(initloader==null){
				try{
					initloader = (i_externalloader)Class.forName(getAppInit().get_init_loader().trim()).newInstance();
					initloader.load();
					bsController.setToLocalContainer(app_init.id_init_loader,initloader);
				}catch(Exception ex){
					new bsControllerException(ex, iStub.log_ERROR);
				}catch (Throwable  th) {
					new bsControllerException(th, iStub.log_ERROR);
				}
			}
		}

		i_ProviderWrapper wrapperConfigAction = checkLoadActions(null);
		if(wrapperConfigAction!=null && wrapperConfigAction.getInstance()!=null){
			try{
				((load_actions)wrapperConfigAction.getInstance()).load_from_resources();
				((load_actions)wrapperConfigAction.getInstance()).init();
				if(!((load_actions)wrapperConfigAction.getInstance()).isReadOk()){
					((load_actions)wrapperConfigAction.getInstance()).initProperties(getAppInit().getApplication_path_config()+CONST_XML_ACTIONS);
					if(((load_actions)wrapperConfigAction.getInstance()).isReadOk_File()) getAppInit().set_path_config(getAppInit().getApplication_path_config());
					((load_actions)wrapperConfigAction.getInstance()).initWithFOLDER(getAppInit().get_path_config()+CONST_XML_ACTIONS_FOLDER+"/");
				}
			}catch(bsControllerException je){}

		}else{
			if(action_config==null)
				action_config = new load_actions();
			try{
				action_config.load_from_resources();
				action_config.init();
				if(!action_config.isReadOk()){
					action_config.initProperties(getAppInit().getApplication_path_config()+CONST_XML_ACTIONS);
					if(action_config.isReadOk_File()) getAppInit().set_path_config(getAppInit().getApplication_path_config());
					action_config.initWithFOLDER(getAppInit().get_path_config()+CONST_XML_ACTIONS_FOLDER+"/");
				}
			}catch(bsControllerException je){}
		}
		
		i_ProviderWrapper wrapperConfigMess = checkLoadMessage(null);
		if(wrapperConfigMess!=null && wrapperConfigMess.getInstance()!=null){
			try{
				((load_message)wrapperConfigMess.getInstance()).load_from_resources();
				((load_message)wrapperConfigMess.getInstance()).init();
				if(!((load_message)wrapperConfigMess.getInstance()).isReadOk()){
					((load_message)wrapperConfigMess.getInstance()).initProperties(getAppInit().get_path_config()+CONST_XML_MESSAGES);
					((load_message)wrapperConfigMess.getInstance()).initWithFOLDER(getAppInit().get_path_config()+CONST_XML_MESSAGES_FOLDER+"/");
				}
			}catch(bsControllerException je){}
		}else{
			if(mess_config==null)	
				mess_config = new load_message();
			try{
				mess_config.load_from_resources();
				mess_config.init();
				if(!mess_config.isReadOk()){
					mess_config.initProperties(getAppInit().get_path_config()+CONST_XML_MESSAGES);
					mess_config.initWithFOLDER(getAppInit().get_path_config()+CONST_XML_MESSAGES_FOLDER+"/");
				}
			}catch(bsControllerException je){}
		}
			
		i_ProviderWrapper wrapperConfigAuth = checkLoadAuthentication(null);
		if(wrapperConfigAuth!=null && wrapperConfigAuth.getInstance()!=null){
			try{
				((load_authentication)wrapperConfigAuth.getInstance()).load_from_resources();
				((load_authentication)wrapperConfigAuth.getInstance()).init();
				if(!((load_authentication)wrapperConfigAuth.getInstance()).isReadOk()){
					((load_authentication)wrapperConfigAuth.getInstance()).initProperties(getAppInit().get_path_config()+CONST_XML_MESSAGES);
					((load_authentication)wrapperConfigAuth.getInstance()).initWithFOLDER(getAppInit().get_path_config()+CONST_XML_AUTHENTIFICATIONS_FOLDER+"/");
				}
			}catch(bsControllerException je){}
		}else{
			if(auth_config==null)	
				auth_config = new load_authentication();
			try{
				auth_config.load_from_resources();
				auth_config.init();
				if(!auth_config.isReadOk()){
					auth_config.initProperties(getAppInit().get_path_config()+CONST_XML_MESSAGES);
					auth_config.initWithFOLDER(getAppInit().get_path_config()+CONST_XML_AUTHENTIFICATIONS_FOLDER+"/");

				}
			}catch(bsControllerException je){}
		}
		
		i_ProviderWrapper wrapperConfigOrganization = checkLoadOrganization(null);
		if(wrapperConfigOrganization!=null && wrapperConfigOrganization.getInstance()!=null){
			try{
				((load_organization)wrapperConfigOrganization.getInstance()).load_from_resources();
				((load_organization)wrapperConfigOrganization.getInstance()).init();
				if(!((load_organization)wrapperConfigOrganization.getInstance()).isReadOk()){
					((load_organization)wrapperConfigOrganization.getInstance()).initProperties(getAppInit().get_path_config()+CONST_XML_ORGANIZATION);

				}
			}catch(bsControllerException je){}
		}else{
			if(org_config==null)	
				org_config = new load_organization();
			try{
				org_config.load_from_resources();
				org_config.init();
				if(!org_config.isReadOk()){
					org_config.initProperties(getAppInit().get_path_config()+CONST_XML_ORGANIZATION);

				}
			}catch(bsControllerException je){}
		}

		i_ProviderWrapper wrapperConfigMenu = checkLoadMenu(null);
		if(wrapperConfigMenu!=null && wrapperConfigMenu.getInstance()!=null){
			try{
				((load_menu)wrapperConfigMenu.getInstance()).load_from_resources();
				((load_menu)wrapperConfigMenu.getInstance()).init();
				if(!((load_menu)wrapperConfigMenu.getInstance()).isReadOk()){
					((load_menu)wrapperConfigMenu.getInstance()).initProperties(getAppInit().get_path_config()+CONST_XML_MENU);

				}
			}catch(bsControllerException je){}
		}else{		
			if(menu_config==null)	
				menu_config = new load_menu(null);
				try{
					menu_config.load_from_resources();
					menu_config.init();
					if(!menu_config.isReadOk())
						menu_config.initProperties(getAppInit().get_path_config()+CONST_XML_MENU);
				}catch(bsControllerException je){}
		}


		if(getAppInit()!=null && getAppInit().get_external_loader()!=null && !getAppInit().get_external_loader().equals("")){
			i_externalloader externalloader = null;
			if(getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equals("false")){
				try{
					externalloader = (i_externalloader)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  app_init.id_external_loader, getAppInit().get_external_loader().trim(), null);
					if(externalloader!=null)
						externalloader.load();
				}catch(Exception e){
				}
			}
			if(externalloader==null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equals("false")){
				try{
					externalloader = (i_externalloader)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  app_init.id_external_loader, getAppInit().get_external_loader().trim(), null);
					if(externalloader!=null)
						externalloader.load();
				}catch(Exception e){
				}
			}
			if(externalloader==null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equals("false")){
				try{
					externalloader = (i_externalloader)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  app_init.id_external_loader, getAppInit().get_external_loader().trim(), null);
					if(externalloader!=null)
						externalloader.load();
				}catch(Exception e){
				}
			}		
			checkDefaultProvider(servletContext);
			if(externalloader==null && getCdiDefaultProvider()!=null){
				try{
					externalloader = (i_externalloader)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  app_init.id_external_loader, getAppInit().get_external_loader().trim(), null);
					if(externalloader!=null)
						externalloader.load();
				}catch(Exception e){
				}
			}
			if(externalloader==null && getEjbDefaultProvider()!=null){
				try{
					externalloader = (i_externalloader)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  app_init.id_external_loader, getAppInit().get_external_loader().trim(), null);
					if(externalloader!=null)
						externalloader.load();
				}catch(Exception e){
				}
			}

			if(externalloader==null){
				try{
					externalloader = (i_externalloader)Class.forName(getAppInit().get_external_loader().trim()).newInstance();
					externalloader.load();
					bsController.setToLocalContainer(app_init.id_external_loader,externalloader);
				}catch(Exception ex){
					new bsControllerException(ex, iStub.log_ERROR);
				}catch (Throwable  th) {
					new bsControllerException(th, iStub.log_ERROR);
				}
			}
		}
		
		canBeProxed = checkCanBeProxed();

	}


	public void reInit() throws ServletException, UnavailableException {


		reloadAction_config(getServletContext());
		reloadLog_generator(getServletContext());
		reloadAuth_config(getServletContext());
		reloadOrg_config(getServletContext());
		reloadMenu_config(getServletContext(),null);
		reloadMess_config(getServletContext());
		
		canBeProxed = checkCanBeProxed();


	}


	public void service(HttpServletRequest request, HttpServletResponse response)throws ServletException, UnavailableException {

			boolean isDebug=false;
			try{
				isDebug = System.getProperty("debug").equalsIgnoreCase("true");
			}catch(Exception e){
				try{
					isDebug = bsController.getAppInit().get_debug().equalsIgnoreCase("true");
				}catch(Exception ex){
				}
			}

			String id_action=request.getParameter(CONST_ID_$ACTION);

			String id_rtype=request.getParameter(CONST_ID_REQUEST_TYPE);
			if(id_rtype==null) id_rtype = CONST_REQUEST_TYPE_FORWARD;
			request.setAttribute(CONST_ID_REQUEST_TYPE, id_rtype);

			if(id_action==null) id_action = getPropertyMultipart(CONST_ID_$ACTION, request);
			if(id_action!=null) id_action=id_action.trim();

			if(isDebug && id_action!=null && id_action.equalsIgnoreCase(CONST_DIRECTINDACTION_bsLog)){
				try{
					response.getWriter().write(getLogG().get_log_Content("<br>"+System.getProperty("line.separator")));
				}catch(Exception e){
				}
				return;
			}
			if(isDebug && id_action!=null && id_action.equalsIgnoreCase(CONST_DIRECTINDACTION_bsActions)){
				try{
					String content = "actions.xml<br>ROOT1="+request.getSession().getServletContext().getRealPath("/")+"<br>ROOT2="+request.getSession().getServletContext().getRealPath("/")+"<br>";
					try{
						content+="ROOT3="+util_classes.getPath("config")+"<br>";
					}catch(Exception exp){
						content+="ROOT3=ERROR:"+exp.toString();
					}
					content+=bsController.getAction_config().toString();
					response.getWriter().write(content);
				}catch(Exception e){
					String content = "actions.xml:ERROR<br>";

					content+=e.toString();
					try{
						response.getWriter().write(content);
					}catch(Exception ex){
					}
				}
				return;
			}

			if(isDebug && id_action!=null && id_action.equalsIgnoreCase(CONST_DIRECTINDACTION_bsLogS)){
				try{
					String content = "LOG<br>";
					Vector s_log = (Vector)request.getSession().getAttribute(bsController.CONST_SESSION_LOG);
					if(s_log==null) content="LOG:null";
					else{
						for(int i=0;i<s_log.size();i++){
							content+=s_log.get(i).toString()+"<br>";
						}
					}
					response.getWriter().write(content);
				}catch(Exception e){

				}
				return;
			}


			if(isDebug && id_action!=null && id_action.equalsIgnoreCase(CONST_DIRECTINDACTION_Controller)){
				loadOnInit();
				return;
			}


			if(isDebug && id_action!=null && id_action.equalsIgnoreCase(CONST_DIRECTINDACTION_bsResource)){
				try{
					String content = "Resource<br>";
					content+="APP_INIT="+getAppInit().getLoadedFrom()+"<br>";
					content+="AUTH_INIT="+checkAuth_init(request).getLoadedFrom()+"<br>";
					content+="LOG_INIT="+getLogInit().getLoadedFrom()+"<br>";
					content+="DB_INIT="+getDbInit().getLoadedFrom()+"<br>";
					content+="ACTIONS_LOAD="+getAction_config().getLoadedFrom()+"<br>";
					content+="AUTHENTICATION_LOAD="+getAuth_config().getLoadedFrom()+"<br>";
					content+="ORGANIZATION_LOAD="+getOrg_config().getLoadedFrom()+"<br>";
					content+="MESSAGES_LOAD="+getMess_config().getLoadedFrom()+"<br>";
					content+="MENU_LOAD="+getMenu_config().getLoadedFrom()+"<br>";
					try{
						content+="USERS_LOAD="+((load_users)bsController.getUser_config()).getLoadedFrom()+"<br>";
					}catch(Exception e){
						content+="USERS_LOAD=?";
					}
					response.getWriter().write(content);
				}catch(Exception e){

				}

				return;
			}

			if(isDebug && id_action!=null && id_action.equalsIgnoreCase(CONST_DIRECTINDACTION_bsStatistics)){
				try{
					String content = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n";
					content+="<statistics>\n";
					I_StatisticProvider stack = getStatisticProvider();
					if(stack!=null) content+=stack.getAllEntitiesAsXml();
					content+="</statistics>\n";
					response.getWriter().write(content);
				}catch(Exception e){

				}
				return;
			}

			if(id_action!=null &&
				(
					id_action.equalsIgnoreCase(CONST_DIRECTINDACTION_bsLoadFromResources) ||
					id_action.equalsIgnoreCase(CONST_DIRECTINDACTION_bsLoadFromFramework)
				)
			){
				String loadSrc = request.getParameter(CONST_DIRECTINDACTION_bsLoadSrc);
				if(loadSrc==null) return;
				String loadType = request.getParameter(CONST_DIRECTINDACTION_bsLoadType);

				try{
					byte[] output = null;
					ArrayList resources = null;
					if(id_action.equalsIgnoreCase(CONST_DIRECTINDACTION_bsLoadFromResources)){
						if(loadSrc.trim().equals("") || loadSrc.lastIndexOf('/')==loadSrc.length()-1)
							resources = util_classes.getResourcesAsByte("it/classhidra/core/controller/resources/"+loadSrc, null);
						else
							output = util_classes.getResourceAsByte("it/classhidra/core/controller/resources/"+loadSrc);
						if(output==null && (resources==null || resources.size()==0))
							resources = util_classes.getResourcesAsByte("it/classhidra/core/controller/resources/"+loadSrc, null);
					}
					if(id_action.equalsIgnoreCase(CONST_DIRECTINDACTION_bsLoadFromFramework)){
						if(loadSrc.trim().equals("") || loadSrc.lastIndexOf('/')==loadSrc.length()-1)
							resources = util_classes.getResourcesAsByte("it/classhidra/framework/resources/"+loadSrc, "\n\r".getBytes());
						else
							output = util_classes.getResourceAsByte("it/classhidra/framework/resources/"+loadSrc);
						if(output==null && (resources==null || resources.size()==0))
							resources = util_classes.getResourcesAsByte("it/classhidra/framework/resources/"+loadSrc, "\n\r".getBytes());
					}

					if(output!=null || (resources!=null && resources.size()>0)){
						if(loadType!=null)
							response.setContentType(loadType);
						setCache((HttpServletResponse)response, request.getParameter(bsController.CONST_DIRECTINDACTION_bsLoadCache));
					}

					if(output!=null){
						 OutputStream os = response.getOutputStream();
			    		 os.write(output);
			    		 os.flush();
			    		 os.close();
					}
					if(resources!=null && resources.size()>0){
//						if(loadType!=null)
//							response.setContentType(loadType);
						 OutputStream os = response.getOutputStream();
						 for(int i=0;i<resources.size();i++){
							 byte[] towrite = (byte[])resources.get(i);
							 if(towrite!=null)
								 os.write(towrite);
						 }
			    		 os.flush();
			    		 os.close();
					}
				}catch(Exception e){
				}
				return;
			}


			if(id_action!=null && id_action.equalsIgnoreCase(CONST_DIRECTINDACTION_bsTransformation)){
				transformation cTransformation = (transformation)request.getAttribute(bsConstants.CONST_ID_TRANSFORMATION4CONTROLLER);
				if(cTransformation==null){
					String idInSession = request.getParameter("id");
					if(idInSession!=null){
						cTransformation = (transformation)request.getSession().getAttribute(bsConstants.CONST_ID_TRANSFORMATION4CONTROLLER+"_"+idInSession);
						if(cTransformation!=null) request.getSession().removeAttribute(bsConstants.CONST_ID_TRANSFORMATION4CONTROLLER+"_"+idInSession);
					}
				}
				if(cTransformation!=null){
					request.removeAttribute(bsConstants.CONST_ID_TRANSFORMATION4CONTROLLER);
					if(cTransformation.getOutputcontent()!=null){

						try{
			    			if(cTransformation.getContentType()!=null){
			    				if(cTransformation.getContentType().equalsIgnoreCase("text/html")){
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
				}
				return;
			}

			service(id_action, getServletContext(), request,response);

	}

	public static i_action getActionInstance(String id_action, HttpServletRequest request, HttpServletResponse response) throws bsControllerException,ServletException, UnavailableException{
		return getActionInstance(id_action,null, request, response);
	}

	public static i_action getActionInstance(String id_action,String id_call, HttpServletRequest request, HttpServletResponse response) throws bsControllerException,ServletException, UnavailableException{

		boolean cloned = (request.getParameter(CONST_ID_EXEC_TYPE)==null)?false:request.getParameter(CONST_ID_EXEC_TYPE).equalsIgnoreCase(CONST_ID_EXEC_TYPE_CLONED);

		i_action action_instance = getAction_config().actionFactory(id_action,request.getSession(),request.getSession().getServletContext());

		i_bean bean_instance = getCurrentForm(id_action,request);

		if(bean_instance==null){
			info_bean iBean = (info_bean)getAction_config().get_beans().get(action_instance.get_infoaction().getName());
			if(	action_instance instanceof i_bean &&
				(
					action_instance.get_infoaction().getName().equals("") ||
					(iBean!=null && action_instance.get_infoaction().getType().equals(iBean.getType()))
				)
			){
//				bean_instance = action_instance.asBean();
//				bean_instance = action_instance;
				if(action_instance.asBean().getInfo_context().isStateless())
					bean_instance = action_instance.asBean();
				else
					bean_instance = action_instance;
			}else if(iBean == null && !action_instance.get_infoaction().getName().equals("")){
				if(action_instance.getRealBean()==null){
//					info_context info = checkBeanContext(action_instance.asBean());
					if(action_instance.asBean().getInfo_context().isOnlyProxied())
						bean_instance = action_instance;
					else 
						bean_instance = action_instance.asBean();
				}
			}else
				bean_instance = getAction_config().beanFactory(action_instance.get_infoaction().getName(),request.getSession(), request.getSession().getServletContext(),action_instance);
			
			if(bean_instance!=null){
				if(bean_instance.getCurrent_auth()==null || action_instance.get_infoaction().getMemoryInSession().equalsIgnoreCase("true"))
					bean_instance.setCurrent_auth( bsController.checkAuth_init(request));
				if(bean_instance.getCurrent_auth()==null || action_instance.get_infoaction().getMemoryAsLastInstance().equalsIgnoreCase("true"))
					bean_instance.setCurrent_auth( bsController.checkAuth_init(request));
				bean_instance.reimposta();
			}

			//Modifica 20100521 WARNING
			if(bean_instance!=null){
				if(cloned){
					try{
						action_instance.onPreSet_bean();
						action_instance.set_bean((i_bean)bean_instance.clone());
						action_instance.onPostSet_bean();
					}catch(Exception e){
						action_instance.onPreSet_bean();
						action_instance.set_bean(bean_instance, (bean_instance!=null)?bean_instance.getInfo_context():null);
						action_instance.onPostSet_bean();
					}
				}
				else{
					action_instance.onPreSet_bean();
					action_instance.set_bean(bean_instance, (bean_instance!=null)?bean_instance.getInfo_context():null);
					action_instance.onPostSet_bean();
				} 
			}
		}else{

			if(bean_instance.getCurrent_auth()==null || action_instance.get_infoaction().getMemoryInSession().equalsIgnoreCase("true"))
				bean_instance.setCurrent_auth( bsController.checkAuth_init(request));
			if(bean_instance.getCurrent_auth()==null || action_instance.get_infoaction().getMemoryAsLastInstance().equalsIgnoreCase("true"))
				bean_instance.setCurrent_auth( bsController.checkAuth_init(request));

			if(	action_instance instanceof i_bean &&
					(
						action_instance.get_infoaction().getName().equals("") ||
						bean_instance.asBean().getClass().getName().equals(action_instance.asBean().getClass().getName())
					)
				){
				if(cloned){
					try{
						action_instance = (i_action)util_cloner.clone(bean_instance);
					}catch(Exception e){
						action_instance = bean_instance.asAction();
					}
				}else{
//					if(action_instance!=null && action_instance.get_bean()!=null && !action_instance.get_bean().isNavigable() && action_instance.get_bean().isEjb()){
					if(action_instance!=null && action_instance.get_bean()!=null && action_instance.get_bean().getInfo_context()!=null && action_instance.get_bean().getInfo_context().isProxiedEjb()){
//						info_context info = checkBeanContext(bean_instance.asBean());
						if(action_instance.get_bean().getInfo_context().isOnlyProxied()){
							if(action_instance.getRealBean()==null){
								try{
									action_instance = (i_action)bean_instance;
								}catch(Exception e){
									action_instance.get_bean().reInit(bean_instance);
								}
							}else
								action_instance.set_bean(bean_instance, (bean_instance!=null)?bean_instance.getInfo_context():null);
						}else	
							action_instance.get_bean().reInit(bean_instance);
					}else if(action_instance!=null && action_instance.get_bean()!=null && action_instance.get_bean().getInfo_context()!=null && action_instance.get_bean().getInfo_context().isScoped()){
						action_instance = (i_action)bean_instance;
					}else{
/*
						info_context info = checkBeanContext(bean_instance.asBean());
						if(info.isSingleton() || info.isStateful()){
							if(action_instance.getRealBean()==null){
								try{
									action_instance = (i_action)bean_instance;
								}catch(Exception e){
									action_instance.get_bean().reInit(bean_instance);
								}
							}else
								action_instance.set_bean(bean_instance);
						}else	
*/						
						action_instance = bean_instance.asAction();
					}
				}
			}
			else{
				if(bean_instance!=null){
					if(cloned){
						try{
							action_instance.onPreSet_bean();
							action_instance.set_bean((i_bean)util_cloner.clone(bean_instance));
							action_instance.onPostSet_bean();
						}catch(Exception e){
							action_instance.onPreSet_bean();
							action_instance.set_bean(bean_instance, (bean_instance!=null)?bean_instance.getInfo_context():null);
							action_instance.onPostSet_bean();
						}
					}
					else{
						action_instance.onPreSet_bean();
//						if(action_instance!=null && action_instance.get_bean()!=null && !action_instance.get_bean().isNavigable() && action_instance.get_bean().isEjb())
						if(action_instance!=null && action_instance.get_bean()!=null && action_instance.get_bean().getInfo_context()!=null && action_instance.get_bean().getInfo_context().isProxiedEjb()){
							if(bean_instance==null || action_instance.getInfo_context().getProxiedId()!=bean_instance.getInfo_context().getProxiedId())
								action_instance.set_bean(bean_instance, (bean_instance!=null)?bean_instance.getInfo_context():null);
							else
								action_instance.get_bean().reInit(bean_instance);
						}else
							action_instance.set_bean(bean_instance, (bean_instance!=null)?bean_instance.getInfo_context():null);
						
						action_instance.onPostSet_bean();
					}
				}
			}
		}

		HashMap request2map = null;
		boolean isRemoteEjb=false;
		
		if(action_instance!=null && action_instance.getInfo_context()!=null && action_instance.getInfo_context().isRemote()){
			isRemoteEjb=true;
			try{
				request2map = (HashMap)action_instance.asAction().getClass()
									.getDeclaredMethod("convertRequest2Map", new Class[]{HttpServletRequest.class})
									.invoke(null, new Object[]{request});
			}catch (Exception e) {
				new bsControllerException(e, iStub.log_ERROR);
			}catch (Throwable e) {
				new bsControllerException(e, iStub.log_ERROR);
			}
			if(request2map==null)
				request2map = new HashMap();
//				request2map = util_supportbean.request2map(request);
		}
		
//20160419
		if(action_instance.getCurrent_redirect()!=null)
			action_instance.setCurrent_redirect(null);
//-------
		
		try{
			if(!isRemoteEjb)
				action_instance.onPreInit(request, response);
			else
				action_instance.onPreInit(request2map);
		}catch(Exception e){ 
			action_instance.onPreInit(null, null);
		}
		try{
			if(!isRemoteEjb)
				action_instance.init(request,response);
			else{ 
//				action_instance.init(request2map);
				util_supportbean.init(action_instance, request2map, request);
			}
		}catch(Exception e){
			action_instance.init(null,null);
		}
		try{
			if(!isRemoteEjb)
				action_instance.onPostInit(request, response);
			else
				action_instance.onPostInit(request2map);
		}catch(Exception e){
			action_instance.onPostInit(null,null);
		}

		info_call iCall = null;
		Method iCallMethod = null;
		boolean useAsAction = false;

		if(id_call!=null){
			try{
				iCall = (info_call)action_instance.get_infoaction().get_calls().get(id_call);
				if(iCall==null)
					iCall = (info_call)action_instance.get_infoaction().get_calls().get(id_call+"."+request.getMethod());
				if(iCall==null){
					Object[] method_call = action_instance.getMethodAndCall(id_call);
					if(method_call!=null){
						iCallMethod = (Method)method_call[0];
						iCall =  (info_call)method_call[1];						
						if(iCall.getExposed().size()==0){
								action_instance.get_infoaction().get_calls().put(iCall.getName(),iCall);
						}else{
							for(int e=0;e<iCall.getExposed().size();e++){
								String suffix = "."+iCall.getExposed().get(e).toString();
								if(action_instance.get_infoaction().get_calls().get(iCall.getName()+suffix)==null)
									action_instance.get_infoaction().get_calls().put(iCall.getName()+suffix,iCall);
							}
						}

//						action_instance.get_infoaction().get_calls().put(iCall.getName(),iCall);
					}
				}else{
					List searchErrors = new ArrayList();
						if(iCallMethod==null){
							try{
								if(!isRemoteEjb)
	//								iCallMethod = util_reflect.getMethodName(action_instance,iCall.getMethod(), new Class[]{HttpServletRequest.class, HttpServletResponse.class});
									iCallMethod = action_instance.getClass().getMethod(iCall.getMethod(), new Class[]{HttpServletRequest.class, HttpServletResponse.class});
								else
	//								iCallMethod = util_reflect.getMethodName(action_instance,iCall.getMethod(), new Class[]{HashMap.class});
									iCallMethod = action_instance.getClass().getMethod(iCall.getMethod(), new Class[]{HashMap.class});
							}catch(Exception em){	
								searchErrors.add(em);
//								new bsControllerException(em, iStub.log_ERROR);
							}
						}
						if(iCallMethod==null){
							try{
								if(!isRemoteEjb)
	//								iCallMethod = util_reflect.getMethodName(action_instance.asAction(),iCall.getMethod(), new Class[]{HttpServletRequest.class, HttpServletResponse.class});
									iCallMethod = action_instance.asAction().getClass().getMethod(iCall.getMethod(), new Class[]{HttpServletRequest.class, HttpServletResponse.class});
								else
	//								iCallMethod = util_reflect.getMethodName(action_instance.asAction(),iCall.getMethod(), new Class[]{HashMap.class});
									iCallMethod = action_instance.asAction().getClass().getMethod(iCall.getMethod(), new Class[]{HashMap.class});
								if(iCallMethod!=null)
									useAsAction=true;
							}catch(Exception em){
								searchErrors.add(em);
//								new bsControllerException(em, iStub.log_ERROR);
							}	
						}
						if(iCallMethod==null && iCall.getMappedMethodParameterTypes()!=null){
							try{
								if(!isRemoteEjb)
									iCallMethod = action_instance.getClass().getMethod(iCall.getMethod(), iCall.getMappedMethodParameterTypes());
								else
									iCallMethod = action_instance.getClass().getMethod(iCall.getMethod(), iCall.getMappedMethodParameterTypes());
								if(iCallMethod!=null){
									useAsAction=true;
									iCall.setR_R(false);
								}
							}catch(Exception em){
								searchErrors.add(em);
//								new bsControllerException(em, iStub.log_ERROR);
							}	
						}
						if(iCallMethod==null && iCall.getMappedMethodParameterTypes()!=null){
							try{
								if(!isRemoteEjb)
									iCallMethod = action_instance.asAction().getClass().getMethod(iCall.getMethod(), iCall.getMappedMethodParameterTypes());
								else
									iCallMethod = action_instance.asAction().getClass().getMethod(iCall.getMethod(), iCall.getMappedMethodParameterTypes());
								if(iCallMethod!=null){
									useAsAction=true;
									iCall.setR_R(false);
								}
							}catch(Exception em){
								searchErrors.add(em);
//								new bsControllerException(em, iStub.log_ERROR);
							}	
						}
						if(iCallMethod==null){
							for(int er=0;er<searchErrors.size();er++)
								new bsControllerException((Exception)searchErrors.get(er), iStub.log_ERROR);
						}

					
					
					
				}
			}catch(Exception ex){
				new bsControllerException(ex, iStub.log_ERROR);
			}catch(Throwable th){
				new bsControllerException(th, iStub.log_ERROR);
			}
		}

		if(iCall!=null && iCall.getNavigated().equalsIgnoreCase("false")){
		}else 
			setInfoNav_CurrentForm(action_instance,request);


// ACTIONSEVICE
		redirects current_redirect = null;
		if(id_call==null){
			
			if(!action_instance.get_infoaction().isExposed(request.getMethod()))
				throw new bsControllerException("HTTP Method "+request.getMethod()+" is not supported for /"+action_instance.get_infoaction().getPath(), request, iStub.log_FATAL);
			
			Method iActionMethod = null;
			if(action_instance.get_infoaction().getMethod()!=null && !action_instance.get_infoaction().getMethod().equals("")){
				List searchErrors = new ArrayList();
					if(iActionMethod==null){
						try{
							if(!isRemoteEjb)
								iActionMethod = action_instance.getClass().getMethod(action_instance.get_infoaction().getMethod(), new Class[]{HttpServletRequest.class, HttpServletResponse.class});
							else
								iActionMethod = action_instance.getClass().getMethod(action_instance.get_infoaction().getMethod(), new Class[]{HashMap.class});
								
						}catch(Exception em){
							searchErrors.add(em);
//							new bsControllerException(e, iStub.log_ERROR);
						}
					}
					if(iActionMethod==null){
						try{
							if(!isRemoteEjb)
								iActionMethod = action_instance.asAction().getClass().getMethod(action_instance.get_infoaction().getMethod(), new Class[]{HttpServletRequest.class, HttpServletResponse.class});
							else
								iActionMethod = action_instance.asAction().getClass().getMethod(action_instance.get_infoaction().getMethod(), new Class[]{HashMap.class});
								
							if(iActionMethod!=null)
								useAsAction=true;
						}catch(Exception em){
							searchErrors.add(em);
//							new bsControllerException(e, iStub.log_ERROR);
						}
					}
	
					if(iActionMethod==null && action_instance.get_infoaction().getMappedMethodParameterTypes()!=null){
						try{
							if(!isRemoteEjb)
								iActionMethod = action_instance.asAction().getClass().getMethod(action_instance.get_infoaction().getMethod(), action_instance.get_infoaction().getMappedMethodParameterTypes());
							else
								iActionMethod = action_instance.asAction().getClass().getMethod(action_instance.get_infoaction().getMethod(), action_instance.get_infoaction().getMappedMethodParameterTypes());
								
							if(iActionMethod!=null){
								useAsAction=true;
								action_instance.get_infoaction().setR_R(false);
							}
						}catch(Exception em){
							searchErrors.add(em);
//							new bsControllerException(e, iStub.log_ERROR);
						}
					}
					if(iActionMethod==null){
						for(int er=0;er<searchErrors.size();er++)
							new bsControllerException((Exception)searchErrors.get(er), iStub.log_ERROR);
					}


			}
			

			if(action_instance.get_infoaction().getSyncro().equalsIgnoreCase("true")){
				try{
					if(!isRemoteEjb)
						action_instance.onPreSyncroservice(request,response);
					else
						action_instance.onPreSyncroservice(request2map);
				}catch(Exception e){
					action_instance.onPreSyncroservice(null,null);
				}
				if(action_instance.get_bean().getCurrent_auth()==null)
					action_instance.get_bean().setCurrent_auth(bsController.checkAuth_init(request));
				
				if(iActionMethod==null){
					try{
						if(!isRemoteEjb)
							current_redirect = action_instance.syncroservice(request,response);
						else
							current_redirect = action_instance.syncroservice(request2map);
					}catch(Exception e){
						current_redirect = action_instance.syncroservice(null,null);
					}
					
				}else{
					
					try{
//						current_redirect = (redirects)util_reflect.getValue(action_instance, iActionMethod, new Object[]{request,response})
						if(!isRemoteEjb){
							if(useAsAction)
								current_redirect = prepareActionResponse(
										(action_instance.asAction().get_infoaction()!=null && !action_instance.asAction().get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance.asAction(), iActionMethod, prepareMethod(iActionMethod, action_instance.asAction(), request, response), action_instance.asAction().get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance.asAction(), iActionMethod, new Object[]{request,response})
										,										
										iActionMethod, action_instance.get_infoaction(),
										response);
							else
								current_redirect = prepareActionResponse(
										(action_instance.get_infoaction()!=null && !action_instance.get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance, iActionMethod, prepareMethod(iActionMethod, action_instance, request, response), action_instance.get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance, iActionMethod, new Object[]{request,response})
										,
										iActionMethod, action_instance.get_infoaction(),
										response);
						}else{
							if(useAsAction)
								current_redirect = prepareActionResponse(
										(action_instance.asAction().get_infoaction()!=null && !action_instance.asAction().get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance.asAction(), iActionMethod, prepareMethod(iActionMethod, action_instance.asAction(), request, response), action_instance.asAction().get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance.asAction(), iActionMethod, new Object[]{request2map})
										,
										iActionMethod, action_instance.get_infoaction(),
										response);
							else
								current_redirect = prepareActionResponse(										
										(action_instance.get_infoaction()!=null && !action_instance.get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance, iActionMethod, prepareMethod(iActionMethod, action_instance, request, response), action_instance.get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance, iActionMethod, new Object[]{request2map})
										,
										iActionMethod, action_instance.get_infoaction(),
										response);
							
						}

					}catch(Exception e){
						new bsControllerException(e, iStub.log_ERROR);
					}catch(Throwable t){
						new bsControllerException(t, iStub.log_ERROR);
					}
					
				}
				
				try{
					if(!isRemoteEjb)
						action_instance.onPostSyncroservice(current_redirect,request,response);
					else
						action_instance.onPostSyncroservice(current_redirect,request2map);
				}catch(Exception e){
					action_instance.onPostSyncroservice(current_redirect,null,null);
				}
			}
			else{
				try{
					if(!isRemoteEjb)
						action_instance.onPreActionservice(request,response);
					else
						action_instance.onPreActionservice(request2map);
				}catch(Exception e){
					action_instance.onPreActionservice(null,null);
				}
				if(action_instance.get_bean().getCurrent_auth()==null)
					action_instance.get_bean().setCurrent_auth(bsController.checkAuth_init(request));
				

				
				if(iActionMethod==null){				
					try{
						if(!isRemoteEjb)
							current_redirect = action_instance.actionservice(request,response);
						else
							current_redirect = action_instance.actionservice(request2map);
					}catch(Exception e){
						current_redirect = action_instance.actionservice(null,null);
					}					
				}else{
					
					try{
//						current_redirect = (redirects)util_reflect.getValue(action_instance, iActionMethod, new Object[]{request,response});
						if(!isRemoteEjb){
							if(useAsAction)
								current_redirect = prepareActionResponse(
//										util_reflect.getValue(action_instance.asAction(), iActionMethod, new Object[]{request,response}),
										(action_instance.asAction().get_infoaction()!=null && !action_instance.asAction().get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance.asAction(), iActionMethod, prepareMethod(iActionMethod, action_instance.asAction(), request, response), action_instance.asAction().get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance.asAction(), iActionMethod, new Object[]{request,response})
										,										
										iActionMethod, action_instance.get_infoaction(),
										response);
							else
								current_redirect = prepareActionResponse(
//										util_reflect.getValue(action_instance, iActionMethod, new Object[]{request,response}),
										(action_instance.asAction().get_infoaction()!=null && !action_instance.asAction().get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance.asAction(), iActionMethod, prepareMethod(iActionMethod, action_instance.asAction(), request, response), action_instance.asAction().get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance.asAction(), iActionMethod, new Object[]{request2map})
										,
										iActionMethod, action_instance.get_infoaction(),
										response);
						}else{
							if(useAsAction)
								current_redirect = prepareActionResponse(
//										util_reflect.getValue(action_instance.asAction(), iActionMethod, new Object[]{request2map}),
										(action_instance.asAction().get_infoaction()!=null && !action_instance.asAction().get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance.asAction(), iActionMethod, prepareMethod(iActionMethod, action_instance.asAction(), request, response), action_instance.asAction().get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance.asAction(), iActionMethod, new Object[]{request2map})
										,
										iActionMethod, action_instance.get_infoaction(),
										response);
							else
								current_redirect = prepareActionResponse(
//										util_reflect.getValue(action_instance, iActionMethod, new Object[]{request2map}),
										(action_instance.get_infoaction()!=null && !action_instance.get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance, iActionMethod, prepareMethod(iActionMethod, action_instance, request, response), action_instance.get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance, iActionMethod, new Object[]{request2map})
										,
										iActionMethod, action_instance.get_infoaction(),
										response);
						}
					}catch(Exception e){
						new bsControllerException(e, iStub.log_ERROR);
					}catch(Throwable t){
						new bsControllerException(t, iStub.log_ERROR);
					}
					
				}
				
				try{
					if(!isRemoteEjb)
						action_instance.onPostActionservice(current_redirect,request,response);
					else
						action_instance.onPostActionservice(current_redirect,request2map);
				}catch(Exception e){
					action_instance.onPostActionservice(current_redirect,null,null);
				}
			}
		}else{
			try{
				
					try{
						if(!isRemoteEjb)
							action_instance.onPreActionCall(id_call, request, response);
						else
							action_instance.onPreActionCall(id_call, request2map);	
					}catch(Exception e){
						action_instance.onPreActionCall(id_call, null,null);						
					}
					if(action_instance.get_bean().getCurrent_auth()==null)
						action_instance.get_bean().setCurrent_auth(bsController.checkAuth_init(request));

					try{
						if(iCallMethod!=null){
							if(!isRemoteEjb){
								if(useAsAction)
									current_redirect = prepareActionCallResponse(
											(iCall.isR_R())
											?
												util_reflect.getValue(action_instance.asAction(), iCallMethod, new Object[]{request,response})
											:
												util_reflect.getValue(action_instance.asAction(), iCallMethod, prepareMethod(iCallMethod, action_instance.asAction(), request, response), iCall.getMappedMethodParameterTypes()),
											iCallMethod,
											action_instance.get_infoaction(),
											iCall,
											response);
								else
									current_redirect = prepareActionCallResponse(
											(iCall.isR_R())
											?
												util_reflect.getValue(action_instance, iCallMethod, new Object[]{request,response})
											:
												util_reflect.getValue(action_instance, iCallMethod, prepareMethod(iCallMethod, action_instance, request, response), iCall.getMappedMethodParameterTypes()),
											iCallMethod,
											action_instance.get_infoaction(),
											iCall,
											response);
							}else{
								if(useAsAction)
									current_redirect = prepareActionCallResponse(
											(iCall.isR_R())
											?
												util_reflect.getValue(action_instance.asAction(), iCallMethod, new Object[]{request2map})
											:
												util_reflect.getValue(action_instance.asAction(), iCallMethod, prepareMethod(iCallMethod, action_instance.asAction(), request, response), iCall.getMappedMethodParameterTypes()),
											iCallMethod,
											action_instance.get_infoaction(),
											iCall,
											response);
								else
									current_redirect = prepareActionCallResponse(
											(iCall.isR_R())
											?
												util_reflect.getValue(action_instance, iCallMethod, new Object[]{request2map})
											:
												util_reflect.getValue(action_instance, iCallMethod, prepareMethod(iCallMethod, action_instance, request, response), iCall.getMappedMethodParameterTypes()),
											iCallMethod,
											action_instance.get_infoaction(),
											iCall,
											response);
							}

						}else{
							current_redirect = prepareActionCallResponse(
									null,
									null,
									action_instance.get_infoaction(),
									iCall,
									response);
						}
					}catch(Exception e){
						new bsControllerException(e, iStub.log_ERROR);
					}catch(Throwable t){
						new bsControllerException(t, iStub.log_ERROR);
					}
					try{
						if(!isRemoteEjb)
							action_instance.onPostActionCall(current_redirect,id_call, request, response);
						else
							action_instance.onPostActionCall(current_redirect,id_call, request2map);
					}catch(Exception e){
						action_instance.onPostActionCall(current_redirect,id_call, null,null);
					}
				
			}catch(Exception ex){
				new bsControllerException(ex, iStub.log_ERROR);
			}catch(Throwable th){
				new bsControllerException(th, iStub.log_ERROR);
			}
		}

// Mod 20130923 --
//		if(current_redirect==null) return null;
		action_instance.onPreSetCurrent_redirect();
		action_instance.setCurrent_redirect(current_redirect);
		if(action_instance.getCurrent_redirect()!=null)
			action_instance.getCurrent_redirect().decodeMessage(request);
		action_instance.onPostSetCurrent_redirect();
		
		if(isRemoteEjb){
			bsController.checkAuth_init(request).reInit(action_instance.getCurrent_auth());
			try{
				action_instance.asAction().getClass()
					.getDeclaredMethod("convertMap2Request", new Class[]{HttpServletRequest.class,HttpServletResponse.class})
					.invoke(null, new Object[]{request,response});
			}catch (Exception e) {
				new bsControllerException(e, iStub.log_ERROR);
			}catch (Throwable e) {
				new bsControllerException(e, iStub.log_ERROR);
			}
		}
			

		if(iCall!=null && iCall.getNavigated().equalsIgnoreCase("false")){
			try{
				i_bean form = null;
				if(action_instance.isBeanEqualAction())
					form = action_instance;
				else
					form = action_instance.get_bean();
				
				if(form.get_infoaction().getMemoryInSession().equalsIgnoreCase("true")){
/*
					HashMap fromSession = null;
					fromSession = (HashMap)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
					if(fromSession==null){
						fromSession = new HashMap();
						request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,fromSession);
					}
*/
					if(form!=null)
						form.onAddToSession();
// Mod 20150402 --
//					fromSession.put(form.get_infobean().getName(),form);

					if(form.get_infoaction()!=null && form.get_infoaction().getPath()!=null)
						setToOnlySession(form.get_infoaction().getPath(),form, request);
//						fromSession.put(form.get_infoaction().getPath(),form);
				}
				if(form.get_infoaction().getMemoryAsLastInstance().equalsIgnoreCase("true")){
					if(form!=null)
						form.onAddToLastInstance();
					request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYASLASTINSTANCE,form);
				}
			}catch(Exception e){
			}

		}else setCurrentForm(action_instance,request);


		return action_instance;

	}

	public static i_action getPrevActionInstance(String id_action, String id_current, HttpServletRequest request, HttpServletResponse response) throws bsControllerException,ServletException, UnavailableException{
		i_action prev_action_instance = getAction_config().actionFactory(id_action,request.getSession(),request.getSession().getServletContext());

		i_bean bean_instance = getCurrentForm(id_action,request);
		if(	prev_action_instance.get_infoaction().getReloadAfterAction().equalsIgnoreCase("true") &&
			!id_action.equals(id_current)){
			if(bean_instance!=null){
				HashMap request2map=null;
				boolean isRemoteEjb=false;
				
				if(bean_instance.getInfo_context()!=null && bean_instance.getInfo_context().isRemote()){
					isRemoteEjb=true;
					try{
						request2map = (HashMap)bean_instance.asBean().getClass()
								.getDeclaredMethod("convertRequest2Map", new Class[]{HttpServletRequest.class})
								.invoke(null, new Object[]{request});
					}catch (Exception e) {
						new bsControllerException(e, iStub.log_ERROR);
					}catch (Throwable e) {
						new bsControllerException(e, iStub.log_ERROR);
					}
					if(request2map==null)
						request2map = new HashMap();
//					request2map = util_supportbean.request2map(request);

				}

				try{
					if(!isRemoteEjb)
						bean_instance.onPreInit(request);
					else
						bean_instance.onPreInit(request2map);
				}catch(Exception e){

				}
				try{
					if(!isRemoteEjb)
						bean_instance.init(request);
					else
						util_supportbean.init(bean_instance, request);
				}catch(Exception e){
					util_supportbean.init(bean_instance, request);
				}
				try{
					if(!isRemoteEjb)
						bean_instance.onPostInit(request);
					else
						bean_instance.onPostInit(request2map);
				}catch(Exception e){					
				}
				try{
					if(!isRemoteEjb)
						bean_instance.onPreValidate(request);
					else
						bean_instance.onPreValidate(request2map);
				}catch(Exception e){					
				}
				redirects validate_redirect = null;
				try{
					if(!isRemoteEjb)
						validate_redirect = bean_instance.validate(request);
					else
						validate_redirect = bean_instance.validate(request2map);
				}catch(Exception e){					
				}
				try{
					if(!isRemoteEjb)
						bean_instance.onPostValidate(validate_redirect,request);
					else
						bean_instance.onPostValidate(validate_redirect, request2map);
				}catch(Exception e){
					

				}
				prev_action_instance.onPreSetCurrent_redirect();
				prev_action_instance.setCurrent_redirect(validate_redirect);
				prev_action_instance.onPostSetCurrent_redirect();
				prev_action_instance.onPreSet_bean();
				prev_action_instance.set_bean(bean_instance, (bean_instance!=null)?bean_instance.getInfo_context():null);
				prev_action_instance.onPostSet_bean();
				if(validate_redirect!=null) return prev_action_instance;
			}
		}else{
			i_bean bean_instance_clone = null;
			if(bean_instance==null){
				info_bean iBean = (info_bean)getAction_config().get_beans().get(prev_action_instance.get_infoaction().getName());
				if(	prev_action_instance instanceof i_bean &&
						(
							prev_action_instance.get_infoaction().getName().equals("") ||
							(iBean!=null && prev_action_instance.get_infoaction().getType().equals(iBean.getType()))
						)
				){
//					bean_instance_clone = prev_action_instance.asBean();
//					bean_instance_clone = prev_action_instance;
					if(prev_action_instance.asBean().getInfo_context().isStateless())
						bean_instance_clone = prev_action_instance.asBean();
					else
						bean_instance_clone = prev_action_instance;					
				}else if(iBean == null && !prev_action_instance.get_infoaction().getName().equals("")){
					if(prev_action_instance.getRealBean()==null){
//						bean_instance_clone = prev_action_instance.asBean();
//						info_context info = checkBeanContext(prev_action_instance.asBean());
						if(prev_action_instance.asBean().getInfo_context().isOnlyProxied())
							bean_instance_clone = prev_action_instance;
						else 
							bean_instance_clone = prev_action_instance.asBean();
						
					}
				}else
					bean_instance_clone = getAction_config().beanFactory(prev_action_instance.get_infoaction().getName(),request.getSession(),request.getSession().getServletContext(),prev_action_instance);

				if(bean_instance_clone!=null){
					if(bean_instance_clone.getCurrent_auth()==null || prev_action_instance.get_infoaction().getMemoryInSession().equalsIgnoreCase("true"))
						bean_instance_clone.setCurrent_auth( bsController.checkAuth_init(request));
					if(bean_instance_clone.getCurrent_auth()==null || prev_action_instance.get_infoaction().getMemoryAsLastInstance().equalsIgnoreCase("true"))
						bean_instance_clone.setCurrent_auth( bsController.checkAuth_init(request));

					bean_instance_clone.reimposta();
				}
			}else{
//Modifica 20100521 WARNING
//				bean_instance_clone = (i_bean)bean_instance.clone();
				bean_instance_clone = bean_instance;
			}

			if(bean_instance_clone!=null){
				HashMap request2map = null;
				boolean isRemoteEjb=false;
				
				if(bean_instance_clone.getInfo_context()!=null && bean_instance_clone.getInfo_context().isRemote()){
					isRemoteEjb=true;
					try{
						request2map = (HashMap)bean_instance_clone.asBean().getClass()
											.getDeclaredMethod("convertRequest2Map", new Class[]{HttpServletRequest.class})
											.invoke(null, new Object[]{request});
					}catch (Exception e) {
						new bsControllerException(e, iStub.log_ERROR);
					}catch (Throwable e) {
						new bsControllerException(e, iStub.log_ERROR);
					}
					if(request2map==null)
						request2map = new HashMap();
//					request2map = util_supportbean.request2map(request);

				}

				if(	prev_action_instance.get_infoaction().getReloadAfterAction().equalsIgnoreCase("true")){
					try{
						if(!isRemoteEjb)
							bean_instance_clone.onPreInit(request);
						else
							bean_instance.onPreInit(request2map);
					}catch(Exception e){
						bean_instance.onPreInit(request2map);
					}
					try{
						if(!isRemoteEjb)
							bean_instance_clone.init(request);
						else
							util_supportbean.init(bean_instance_clone, request);
					}catch(Exception e){
						util_supportbean.init(bean_instance_clone, request);
					}
					try{
						if(!isRemoteEjb)
							bean_instance_clone.onPostInit(request);
						else
							bean_instance.onPostInit(request2map);
					}catch(Exception e){
						bean_instance.onPostInit(request2map);
					}						
				}
				if(bean_instance_clone.getCurrent_auth()==null || prev_action_instance.get_infoaction().getMemoryInSession().equalsIgnoreCase("true"))
					bean_instance_clone.setCurrent_auth( bsController.checkAuth_init(request));
				if(bean_instance_clone.getCurrent_auth()==null || prev_action_instance.get_infoaction().getMemoryAsLastInstance().equalsIgnoreCase("true"))
					bean_instance_clone.setCurrent_auth( bsController.checkAuth_init(request));
				
				try{
					if(!isRemoteEjb)
						bean_instance_clone.onPreValidate(request);
					else
						bean_instance.onPreValidate(request2map);
				}catch(Exception e){
					bean_instance.onPreValidate(request2map);					
				}
				redirects validate_redirect = null;
				try{
					if(!isRemoteEjb)
						validate_redirect = bean_instance_clone.validate(request);
					else
						validate_redirect = bean_instance_clone.validate(request2map);
				}catch(Exception e){
					validate_redirect = bean_instance_clone.validate(request2map);
				}
				try{
					if(!isRemoteEjb)
						bean_instance_clone.onPostValidate(validate_redirect,request);
					else
						bean_instance.onPostValidate(validate_redirect, request2map);
				}catch(Exception e){
					bean_instance.onPostValidate(validate_redirect, request2map);						
				}
				if(	prev_action_instance instanceof i_bean &&
						(
							prev_action_instance.get_infoaction().getName().equals("") ||
							bean_instance_clone.asBean().getClass().getName().equals(prev_action_instance.asBean().getClass().getName())
						)
				){
//					info_context info = checkBeanContext(bean_instance_clone.asBean()); 
					if(bean_instance_clone.asBean().getInfo_context().isOnlyProxied()){
						if(prev_action_instance.getRealBean()==null){
							try{
								prev_action_instance = (i_action)bean_instance_clone;
							}catch(Exception e){
								prev_action_instance.get_bean().reInit(bean_instance_clone);
							}
						}else
							prev_action_instance.set_bean(bean_instance_clone, (bean_instance_clone!=null)?bean_instance_clone.getInfo_context():null);
					}else
						prev_action_instance = bean_instance_clone.asAction();
					
				}
//					prev_action_instance = bean_instance_clone.asAction()

				if(prev_action_instance.get_bean()==null){
					prev_action_instance.onPreSet_bean();
					prev_action_instance.set_bean(bean_instance_clone, (bean_instance_clone!=null)?bean_instance_clone.getInfo_context():null);
					prev_action_instance.onPostSet_bean();
				}
				else if(prev_action_instance.equals(prev_action_instance.get_bean()) && !prev_action_instance.get_bean().getClass().getName().equals(bean_instance_clone.asBean().getClass().getName())){
					prev_action_instance.onPreSet_bean();
					prev_action_instance.set_bean(bean_instance_clone, (bean_instance_clone!=null)?bean_instance_clone.getInfo_context():null);
					prev_action_instance.onPostSet_bean();
				}

				prev_action_instance.onPreSetCurrent_redirect();
				prev_action_instance.setCurrent_redirect(validate_redirect);
				prev_action_instance.onPostSetCurrent_redirect();
				if(validate_redirect!=null) return prev_action_instance;
			}

		}

		return prev_action_instance;
	}

	public static Object[] chech4AnotherOutputMode(i_action action_instance, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response,boolean allowAnotherOutput) throws bsControllerException{
		
		if(	allowAnotherOutput &&
				action_instance.get_bean()!=null &&
				(
						(
								action_instance.getCurrent_redirect().get_uri()==null ||
								action_instance.getCurrent_redirect().get_uri().trim().equals("") ||
								action_instance.get_bean().getXmloutput() ||
								action_instance.get_bean().getJsonoutput()
						)
						||
						(
								action_instance.getCurrent_redirect().get_uri()!=null &&
								!action_instance.getCurrent_redirect().get_uri().trim().equals("") &&
								action_instance.getCurrent_redirect().get_transformationName()!=null &&
								!action_instance.getCurrent_redirect().get_transformationName().equals("")
						)


				)
			){

					try{
						
						HashMap request2map = null;
						boolean isRemoteEjb=false;
						
						if(action_instance!=null && action_instance.getInfo_context()!=null && action_instance.getInfo_context().isRemote()){
							isRemoteEjb=true;
							try{
								request2map = (HashMap)action_instance.asAction().getClass()
													.getDeclaredMethod("convertRequest2Map", new Class[]{HttpServletRequest.class})
													.invoke(null, new Object[]{request});
							}catch (Exception e) {
								new bsControllerException(e, iStub.log_ERROR);
							}catch (Throwable e) {
								new bsControllerException(e, iStub.log_ERROR);
							}
							if(request2map==null)
								request2map = new HashMap();
//							request2map = util_supportbean.request2map(request);

						}
						
						
						
						try{
							if(!isRemoteEjb)
								action_instance.actionBeforeRedirect(request,response);
							else
								action_instance.actionBeforeRedirect(request2map);
						}catch(Exception e){
							action_instance.actionBeforeRedirect(null,null);
						}
	
					}catch(Exception e){
						throw new bsControllerException("Controller generic actionBeforeRedirect error. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),request,iStub.log_ERROR);
					}


					String outputappliedfor = action_instance.get_bean().getOutputappliedfor();
					String outputserializedname = action_instance.get_bean().getOutputserializedname();
					
					String output4SOAP = (String)action_instance.get_bean().get(bsConstants.CONST_ID_OUTPUT4SOAP);
					String output4JSON = (String)action_instance.get_bean().get(bsConstants.CONST_ID_OUTPUT4JSON);

					byte[] output4BYTE = (byte[])action_instance.get_bean().get(bsConstants.CONST_ID_OUTPUT4BYTE);


						boolean isDebug=false;
						try{
							isDebug = System.getProperty("debug").equalsIgnoreCase("true");
						}catch(Exception e){
							try{
								isDebug = bsController.getAppInit().get_debug().equalsIgnoreCase("true");
							}catch(Exception ex){
							}
						}
						
						boolean avoidCheckPermission = (isDebug)?true:false;


						if(action_instance.get_bean().getXmloutput()){
							if(output4SOAP==null)
								output4SOAP = util_beanMessageFactory.bean2xml(
										(outputappliedfor==null || outputappliedfor.trim().equals(""))?action_instance.get_bean().asBean():action_instance.get_bean().asBean().get(outputappliedfor),
										(outputserializedname==null || outputserializedname.trim().equals(""))
											?
											(	
												(outputappliedfor==null || outputappliedfor.trim().equals(""))
												?
												(action_instance.get_bean().get_infobean()==null || action_instance.get_bean().get_infobean().getName()==null || action_instance.get_bean().get_infobean().getName().trim().equals(""))
													?
													(action_instance.get_infoaction().getName()==null)?null:action_instance.get_infoaction().getName()
													:
													action_instance.get_bean().get_infobean().getName()
												:
												outputappliedfor
											)
											:
										outputserializedname
										,
										true,
										avoidCheckPermission,
										action_instance.get_bean().getXmloutput_encoding());

							try{
//								if(action_instance.get_bean().getXmloutput_encoding()!=null && !action_instance.get_bean().getXmloutput_encoding().equals(""))
//									response.getOutputStream().write(output4SOAP.getBytes(action_instance.get_bean().getXmloutput_encoding()));
//								else response.getOutputStream().write(output4SOAP.getBytes());
								response.getOutputStream().write(output4SOAP.getBytes());
								return new Object[]{response, Boolean.valueOf(true)};
							}catch(Exception e){
								throw new bsControllerException("Controller generic redirect error. Print Bean as XML. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),request,iStub.log_ERROR);
							}
						}

						if(action_instance.get_bean().getJsonoutput()){
							if(output4JSON==null)
								output4JSON = util_beanMessageFactory.bean2json(
										(outputappliedfor==null || outputappliedfor.trim().equals(""))?action_instance.get_bean().asBean():action_instance.get_bean().asBean().get(outputappliedfor),
										(outputserializedname==null || outputserializedname.trim().equals(""))
											?
											(	
												(outputappliedfor==null || outputappliedfor.trim().equals(""))
												?
												(action_instance.get_bean().get_infobean()==null || action_instance.get_bean().get_infobean().getName()==null || action_instance.get_bean().get_infobean().getName().trim().equals(""))
													?
													(action_instance.get_infoaction().getName()==null)?null:action_instance.get_infoaction().getName()
													:
													action_instance.get_bean().get_infobean().getName()
												:
												outputappliedfor
											)
											:
										outputserializedname
										,
										avoidCheckPermission,
										action_instance.get_bean().getJsonoutput_encoding());

							try{
//								if(action_instance.get_bean().getJsonoutput_encoding()!=null && !action_instance.get_bean().getJsonoutput_encoding().equals(""))
//									response.getOutputStream().write(output4JSON.getBytes(action_instance.get_bean().getJsonoutput_encoding()));
//								else response.getOutputStream().write(output4JSON.getBytes());
								response.getOutputStream().write(output4JSON.getBytes());
								return new Object[]{response, Boolean.valueOf(true)};
							}catch(Exception e){
								throw new bsControllerException("Controller generic redirect error. Print Bean as JSON. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),request,iStub.log_ERROR);
							}
						}


						if(	action_instance.getCurrent_redirect().get_transformationName()!=null &&
							!action_instance.getCurrent_redirect().get_transformationName().equals("")
						){

							i_transformation cTransformation = null;

							if(action_instance.getCurrent_redirect().get_inforedirect()!=null)
								cTransformation = action_instance.getCurrent_redirect().get_inforedirect().transformationFactory(action_instance.getCurrent_redirect().get_transformationName(),request.getSession().getServletContext());
							if(cTransformation==null || cTransformation.get_infotransformation()==null)
								cTransformation = action_instance.get_infoaction().transformationFactory(action_instance.getCurrent_redirect().get_transformationName(),request.getSession().getServletContext());
							if(cTransformation==null)
								cTransformation = getAction_config().transformationFactory(action_instance.getCurrent_redirect().get_transformationName(),request.getSession().getServletContext());


							if(	cTransformation!=null &&
									cTransformation.get_infotransformation()!=null &&
									(
										cTransformation.get_infotransformation().getEvent().equalsIgnoreCase(info_transformation.CONST_EVENT_AFTER) ||
										cTransformation.get_infotransformation().getEvent().equalsIgnoreCase(info_transformation.CONST_EVENT_BOTH)
									)
								){
								cTransformation.setResponseHeader(request,response);
								request.setAttribute(CONST_ID_TRANSFORMATION4WRAPPER, cTransformation);

								return new Object[]{responseWrapperFactory.getWrapper(response),Boolean.valueOf(false)};
							}

							if(	cTransformation!=null &&
								cTransformation.get_infotransformation()!=null &&
								(
									cTransformation.get_infotransformation().getEvent().equalsIgnoreCase(info_transformation.CONST_EVENT_BEFORE) ||
									cTransformation.get_infotransformation().getEvent().equalsIgnoreCase(info_transformation.CONST_EVENT_BOTH)
								)
							){
								byte[] outTranformation = null;
								if(cTransformation.get_infotransformation().getInputformat().equalsIgnoreCase(info_transformation.CONST_INPUTFORMAT_BYTE)){
									action_instance.onPreTransform(output4BYTE);
									outTranformation = cTransformation.transform(output4BYTE, request);
									action_instance.onPostTransform(outTranformation);
								}
								if(cTransformation.get_infotransformation().getInputformat().equalsIgnoreCase(info_transformation.CONST_INPUTFORMAT_FORM)){
									action_instance.onPreTransform(action_instance.get_bean());
									outTranformation = cTransformation.transform(action_instance.get_bean(), request);
									action_instance.onPostTransform(outTranformation);
								}
								if(	cTransformation.get_infotransformation().getInputformat().equalsIgnoreCase(info_transformation.CONST_INPUTFORMAT_STRING) ||
									cTransformation.get_infotransformation().getInputformat().equalsIgnoreCase("")
								){
									if(output4SOAP==null)
										output4SOAP = util_beanMessageFactory.bean2xml(
												(outputappliedfor==null || outputappliedfor.trim().equals(""))?action_instance.get_bean().asBean():action_instance.get_bean().asBean().get(outputappliedfor),
												(outputserializedname==null || outputserializedname.trim().equals(""))
													?
													(	
														(outputappliedfor==null || outputappliedfor.trim().equals(""))
														?
														(action_instance.get_bean().get_infobean()==null || action_instance.get_bean().get_infobean().getName()==null || action_instance.get_bean().get_infobean().getName().trim().equals(""))
															?
															(action_instance.get_infoaction().getName()==null)?null:action_instance.get_infoaction().getName()
															:
															action_instance.get_bean().get_infobean().getName()
														:
														outputappliedfor
													)
													:
												outputserializedname
												,
												true,
												avoidCheckPermission
												);
									action_instance.onPreTransform(output4SOAP);
									outTranformation = cTransformation.transform(output4SOAP, request);
									action_instance.onPostTransform(outTranformation);
								}

								try{
									cTransformation.setResponseHeader(request,response);
									response.getOutputStream().write(outTranformation);
									return new Object[]{response, Boolean.valueOf(true)};
								}catch(Exception e){
									throw new bsControllerException("Controller generic redirect error. Transform BeanAsXML with ["+action_instance.getCurrent_redirect().get_transformationName()+"]. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),request,iStub.log_ERROR);
								}
							}
						}


			}
		
		
		
		
		
		return new Object[]{response, Boolean.valueOf(false)};

	}
	
	
	public static Object[] prepareMethod(Method method, i_action action_instance, HttpServletRequest request, HttpServletResponse response){
		if(method==null || method.getParameterTypes()==null || method.getParameterTypes().length==0)
			return new Object[0];
		Object[] result = new Object[method.getParameterTypes().length];
		for(int i=0;i<method.getParameterTypes().length;i++){
			Class current = method.getParameterTypes()[i];
			Parameter annotationParameter = null;
			if(method.getParameterAnnotations()!=null && method.getParameterAnnotations()[i]!=null){
				for(int j=0;j<method.getParameterAnnotations()[i].length;j++){
					if(method.getParameterAnnotations()[i][j] instanceof Parameter)
						annotationParameter = (Parameter)method.getParameterAnnotations()[i][j];
				}
			}
			if(annotationParameter!=null){
				try{
					Object ret = action_instance.get_bean().asBean().get(annotationParameter.name());
					if(ret!=null && (ret.getClass().isAssignableFrom(current) || current.isAssignableFrom(ret.getClass())))
						result[i] = ret;
					else if(ret!=null){
						result[i] = util_makeValue.makeFormatedValue1(current,ret.toString(),null);
					}else if(request.getParameter(annotationParameter.name())!=null){
						ret = request.getParameter(annotationParameter.name());
						result[i] = util_makeValue.makeFormatedValue1(current,(ret!=null)?ret.toString():"",null);
					}else if(ret==null){
						try{
							ret = current.newInstance();
							if(ret!=null){
								if(	ret.getClass().isPrimitive() ||
										ret instanceof String ||
										ret instanceof Number ||
										ret instanceof Date ||
										ret instanceof Boolean)
									ret = util_supportbean.init(ret.getClass(), annotationParameter.name(), request);
								else
									util_supportbean.init(ret, annotationParameter.name(), request);
							}
							result[i] = ret;
						}catch(Exception e){
							try{
								if(	current.isPrimitive())
									result[i] = util_supportbean.init(current, annotationParameter.name(), request);	
							}catch(Exception ex){
							}
						}
					}
				}catch(Exception e){
					
				}
			}else{
				try{
					if(current.isAssignableFrom(HttpServletRequest.class))
						result[i] = request;
					else if(current.isAssignableFrom(HttpServletResponse.class))
						result[i] = response;
					else if(current.isPrimitive())
						result[i] = util_makeValue.makeFormatedValue1(current,"",null);
				}catch(Exception e){
					
				}
			}
		}
		return result;
	}
	
	public static redirects prepareActionCallResponse(Object retVal, Method method, info_action iAction, info_call iCall, HttpServletResponse response) throws Exception{
		
		redirects current_redirect = null;
		if(method!=null){
			if(method.getReturnType()!=null && !method.getReturnType().equals(Void.TYPE)){
				if(redirects.class.isAssignableFrom(method.getReturnType()))
					current_redirect = (redirects)retVal;
				else if(response_wrapper.class.isAssignableFrom(method.getReturnType())){
					if(retVal!=null){
						response_wrapper rWrapper = (response_wrapper)retVal;
						info_redirect fake = new info_redirect().setContentType(rWrapper.getContentType()).setContentName(rWrapper.getContentName()).setContentEncoding(rWrapper.getContentEncoding());
						if(iCall!=null && iCall.getIRedirect()!=null){
							if(iCall.getIRedirect().getContentType()!=null && !iCall.getIRedirect().getContentType().equals(""))
								fake.setContentType(iCall.getIRedirect().getContentType());
							if(iCall.getIRedirect().getContentName()!=null && !iCall.getIRedirect().getContentName().equals(""))
								fake.setContentName(iCall.getIRedirect().getContentName());		
							if(iCall.getIRedirect().getContentEncoding()!=null && !iCall.getIRedirect().getContentEncoding().equals(""))
								fake.setContentEncoding(iCall.getIRedirect().getContentEncoding());								
						}
						updateResponseContentType(fake, response, rWrapper.getResponseStatus());
						if(rWrapper.getContent()!=null){
							if(String.class.isAssignableFrom(rWrapper.getContent().getClass()))
								response.getOutputStream().write(((String)rWrapper.getContent()).getBytes());
							else if(byte[].class.isAssignableFrom(rWrapper.getContent().getClass()))
								response.getOutputStream().write(((byte[])rWrapper.getContent()));
							else 
								response.getOutputStream().write((rWrapper.getContent().toString()).getBytes());
						}
					}
				}else if(String.class.isAssignableFrom(method.getReturnType()) || byte[].class.isAssignableFrom(method.getReturnType())){
					if(retVal!=null){
						if(iCall!=null && iCall.getIRedirect()!=null)
							updateResponseContentType(iCall.getIRedirect(), response, 0);
						
						if(String.class.isAssignableFrom(method.getReturnType()))
							response.getOutputStream().write(((String)retVal).getBytes());
						if(byte[].class.isAssignableFrom(method.getReturnType()))
							response.getOutputStream().write(((byte[])retVal));
					}
				}
			}else if((method.getReturnType()!=null && method.getReturnType().equals(Void.TYPE)) || method.getReturnType()==null){
				if(iCall!=null && iCall.getIRedirect()!=null && iCall.getIRedirect().getPath()!=null && !iCall.getIRedirect().getPath().equals(""))
					current_redirect = new redirects(iCall.getIRedirect().getPath());
				else if(iCall!=null && iCall.getIRedirect()!=null && iCall.getIRedirect().getAuth_id()!=null && !iCall.getIRedirect().getAuth_id().equals("")){
					if(iAction!=null){
						String uri = iAction.getRedirect(iCall.getIRedirect().getAuth_id());
						if(uri!=null && !uri.equals(""))
							current_redirect = new redirects(uri);
					}
				}else if(iAction!=null && iAction.getRedirect()!=null && !iAction.getRedirect().equals(""))
					current_redirect = new redirects(iAction.getRedirect());
			}
		}else{
			if(iCall!=null && iCall.getIRedirect()!=null && iCall.getIRedirect().getPath()!=null && !iCall.getIRedirect().getPath().equals(""))
				current_redirect = new redirects(iCall.getIRedirect().getPath());
			else if(iCall!=null && iCall.getIRedirect()!=null && iCall.getIRedirect().getAuth_id()!=null && !iCall.getIRedirect().getAuth_id().equals("")){
				if(iAction!=null){
					String uri = iAction.getRedirect(iCall.getIRedirect().getAuth_id());
					if(uri!=null && !uri.equals(""))
						current_redirect = new redirects(uri);
				}
			}else if(iAction!=null && iAction.getRedirect()!=null && !iAction.getRedirect().equals(""))
				current_redirect = new redirects(iAction.getRedirect());			
		}
		return current_redirect;

	}
	
	public static redirects prepareActionResponse(Object retVal, Method method, info_action iAction, HttpServletResponse response) throws Exception{
		
		redirects current_redirect = null;
		if(method!=null){
			if(method.getReturnType()!=null && !method.getReturnType().equals(Void.TYPE)){
				if(redirects.class.isAssignableFrom(method.getReturnType()))
					current_redirect = (redirects)retVal;
				else if(response_wrapper.class.isAssignableFrom(method.getReturnType())){
					if(retVal!=null){
						response_wrapper rWrapper = (response_wrapper)retVal;
						info_redirect fake = new info_redirect().setContentType(rWrapper.getContentType()).setContentName(rWrapper.getContentName()).setContentEncoding(rWrapper.getContentEncoding());
						if(iAction!=null && iAction.getIRedirect()!=null){
							if(iAction.getIRedirect().getContentType()!=null && !iAction.getIRedirect().getContentType().equals(""))
								fake.setContentType(iAction.getIRedirect().getContentType());
							if(iAction.getIRedirect().getContentName()!=null && !iAction.getIRedirect().getContentName().equals(""))
								fake.setContentName(iAction.getIRedirect().getContentName());		
							if(iAction.getIRedirect().getContentEncoding()!=null && !iAction.getIRedirect().getContentEncoding().equals(""))
								fake.setContentEncoding(iAction.getIRedirect().getContentEncoding());								
						}
						updateResponseContentType(fake, response, rWrapper.getResponseStatus());
						if(rWrapper.getContent()!=null){
							if(String.class.isAssignableFrom(rWrapper.getContent().getClass()))
								response.getOutputStream().write(((String)rWrapper.getContent()).getBytes());
							else if(byte[].class.isAssignableFrom(rWrapper.getContent().getClass()))
								response.getOutputStream().write(((byte[])rWrapper.getContent()));
							else 
								response.getOutputStream().write((rWrapper.getContent().toString()).getBytes());
						}
					}
				
				}else if(String.class.isAssignableFrom(method.getReturnType()) || byte[].class.isAssignableFrom(method.getReturnType())){
					if(retVal!=null){
						info_redirect iRedirect = null;
						if(iAction!=null && iAction.getRedirect()!=null && iAction.get_redirects()!=null)
							iRedirect = (info_redirect)iAction.get_redirects().get(iAction.getRedirect());
						
						if(iRedirect!=null)
							updateResponseContentType(iRedirect, response, 0);
						
						if(String.class.isAssignableFrom(method.getReturnType()))
							response.getOutputStream().write(((String)retVal).getBytes());
						if(byte[].class.isAssignableFrom(method.getReturnType()))
							response.getOutputStream().write(((byte[])retVal));
					}
				}
			}else if((method.getReturnType()!=null && method.getReturnType().equals(Void.TYPE)) || method.getReturnType()==null){
				if(iAction!=null && iAction.getRedirect()!=null && !iAction.getRedirect().equals(""))
					current_redirect = new redirects(iAction.getRedirect());
			}
		}else{
			if(iAction!=null && iAction.getRedirect()!=null && !iAction.getRedirect().equals(""))
				current_redirect = new redirects(iAction.getRedirect());
		}
		return current_redirect;

	}	

	public static HttpServletResponse execRedirect(i_action action_instance, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws Exception, bsControllerException,ServletException, UnavailableException{
		return execRedirect(action_instance, servletContext, request, response, false);
	}

	public static HttpServletResponse execRedirect(i_action action_instance, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response,boolean allowAnotherOutput) throws Exception, bsControllerException,ServletException, UnavailableException{

		if(action_instance==null || action_instance.get_infoaction()==null) return response;
		boolean intoWrapper=false;
		Object[] resultC4AOutputMode = chech4AnotherOutputMode(action_instance, servletContext, request, response, allowAnotherOutput);

		if(((Boolean)resultC4AOutputMode[1]).booleanValue()){
			return response;
		}

		if(resultC4AOutputMode[0] instanceof a_ResponseWrapper){
			response = (a_ResponseWrapper)resultC4AOutputMode[0];
			intoWrapper=true;
		}

		action_instance.onPreRedirect();
		RequestDispatcher rd = null;
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
					updateResponseContentType(fake,response,current.getResponseStatus());
				}
			}
			
			
			rd = action_instance.getCurrent_redirect().redirect(servletContext, action_instance.get_infoaction());
		}catch(Exception ex){
			if(getAppInit().get_permit_redirect_resource()!=null && getAppInit().get_permit_redirect_resource().equalsIgnoreCase("true")){
				try{
					redirects current = action_instance.getCurrent_redirect();
					i_transformation resource2response = null;
					if(current.get_inforedirect().getTransformationName()!=null && !current.get_inforedirect().getTransformationName().equals(""))
						resource2response =  getAction_config().transformationFactory(current.get_inforedirect().getTransformationName(), servletContext);
					else	
						resource2response =  getAction_config().transformationFactory("resource2response", servletContext);
					if(resource2response!=null){
						if(resource2response.transform(action_instance, request, response)!=null){
							try{
								action_instance.onPostRedirect(rd);
							}catch(Exception e){							
							}
				    		return response;
						}
					}
				}catch(Exception ex1){
					throw ex;
				}
			}
			action_instance.onPreRedirectError();
			throw ex;
		}
		
		HashMap request2map = null;
		boolean isRemoteEjb=false;
		
		if(action_instance!=null && action_instance.getInfo_context()!=null && action_instance.getInfo_context().isRemote()){
			isRemoteEjb=true;
			try{
				request2map = (HashMap)action_instance.asAction().getClass()
									.getDeclaredMethod("convertRequest2Map", new Class[]{HttpServletRequest.class})
									.invoke(null, new Object[]{request});
			}catch (Exception e) {
				new bsControllerException(e, iStub.log_ERROR);
			}catch (Throwable e) {
				new bsControllerException(e, iStub.log_ERROR);
			}
			if(request2map==null)
				request2map = new HashMap();
//			request2map = util_supportbean.request2map(request);

		}		
		
		try{
			if(!isRemoteEjb)
				action_instance.onPostRedirect(rd);
		}catch(Exception e){
		}
			
		if(rd==null){
			if(action_instance.getCurrent_redirect().get_uri()!=null && !action_instance.getCurrent_redirect().get_uri().trim().equals("")){
				action_instance.onPreRedirectError();
				rd = action_instance.getCurrent_redirect().redirectError(servletContext, action_instance.get_infoaction());
				try{
					if(!isRemoteEjb)
						action_instance.onPostRedirectError(rd);
				}catch(Exception e){
				}
			}
		}
		if(rd==null){
			if(action_instance.getCurrent_redirect().get_uri()!=null && !action_instance.getCurrent_redirect().get_uri().trim().equals("")){
				if(!action_instance.get_infoaction().getError().equals("")) 
					action_instance.getCurrent_redirect().set_uriError(action_instance.get_infoaction().getError());
				else 
					action_instance.getCurrent_redirect().set_uriError(getAction_config().getAuth_error());
				rd = action_instance.getCurrent_redirect().redirectError(servletContext, action_instance.get_infoaction());
			}
		}

		if(rd==null){
			if(action_instance.getCurrent_redirect().get_uri()!=null && !action_instance.getCurrent_redirect().get_uri().trim().equals(""))
				throw new bsControllerException("Controller generic redirect error. Action: ["+action_instance.get_infoaction().getPath()+"] " +action_instance.getCurrent_redirect(),request,iStub.log_ERROR);
		}else{
			try{
				try{
					try{
						if(!isRemoteEjb)
							action_instance.actionBeforeRedirect(request,response);
						else
							action_instance.actionBeforeRedirect(request2map);
					}catch(Exception e){
						action_instance.actionBeforeRedirect(null,null);
					}
				}catch(Exception e){
					throw new bsControllerException("Controller generic actionBeforeRedirect error. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),request,iStub.log_ERROR);
				}



				if(!intoWrapper){


					if(!action_instance.isIncluded()){
						if(response.isCommitted())
							rd.include(request,response);
						else
							rd.forward(request,response);
					}else
						rd.include(request,response);

				}else{

					String tansformationElaborationMode = getAppInit().get_transf_elaborationmode();

					if(tansformationElaborationMode==null || tansformationElaborationMode.trim().length()==0) tansformationElaborationMode=CONST_TRANSFORMATION_ELMODE_INCLUDE;

					if(tansformationElaborationMode.equalsIgnoreCase(CONST_TRANSFORMATION_ELMODE_BOTH)){
						if(response.isCommitted())
							rd.include(request,response);
						else
							rd.forward(request,response);
					}
					if(tansformationElaborationMode.equalsIgnoreCase(CONST_TRANSFORMATION_ELMODE_INCLUDE)){
						rd.include(request,response);
					}
					if(tansformationElaborationMode.equalsIgnoreCase(CONST_TRANSFORMATION_ELMODE_FORWARD)){
						rd.forward(request,response);
					}


				}



			}catch(Exception e){
				if(intoWrapper){
					throw new bsControllerException("Controller generic wrapped redirect error. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),iStub.log_ERROR);
				}else
					throw new bsControllerException("Controller generic redirect error. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),request,iStub.log_ERROR);
			}
		}
		return response;
	}



	public static void execRedirect(i_stream currentStream, redirects currentStreamRedirect, String id_action,
				ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws bsControllerException,ServletException, UnavailableException{
		if(currentStream==null || currentStreamRedirect==null) return;
		currentStream.onPreRedirect(currentStreamRedirect, id_action);
		RequestDispatcher rd =  null;
		

		HashMap request2map = null;
		boolean isRemoteEjb=false;

		info_redirect fake = new info_redirect().setContentType(currentStreamRedirect.getContentType()).setContentName(currentStreamRedirect.getContentName()).setContentEncoding(currentStreamRedirect.getContentEncoding());
		if(currentStreamRedirect.get_inforedirect()!=null){
			if(currentStreamRedirect.get_inforedirect().getContentType()!=null && !currentStreamRedirect.get_inforedirect().getContentType().equals(""))
				fake.setContentType(currentStreamRedirect.get_inforedirect().getContentType());
			if(currentStreamRedirect.get_inforedirect().getContentName()!=null && !currentStreamRedirect.get_inforedirect().getContentName().equals(""))
				fake.setContentName(currentStreamRedirect.get_inforedirect().getContentName());		
			if(currentStreamRedirect.get_inforedirect().getContentEncoding()!=null && !currentStreamRedirect.get_inforedirect().getContentEncoding().equals(""))
				fake.setContentEncoding(currentStreamRedirect.get_inforedirect().getContentEncoding());				
		}
		updateResponseContentType(fake,response,currentStreamRedirect.getResponseStatus());
		

		
		if(currentStreamRedirect.get_uri()==null || currentStreamRedirect.get_uri().equals("")){
			return;
		}
		
			
		if(currentStream!=null && currentStream.getInfo_context()!=null && currentStream.getInfo_context().isRemote()){
			isRemoteEjb=true;
			try{
				request2map = (HashMap)currentStream.asStream().getClass()
									.getDeclaredMethod("convertRequest2Map", new Class[]{HttpServletRequest.class})
									.invoke(null, new Object[]{request});
			}catch (Exception e) {
				new bsControllerException(e, iStub.log_ERROR);
			}catch (Throwable e) {
				new bsControllerException(e, iStub.log_ERROR);
			}
			if(request2map==null)
				request2map = new HashMap();
//			request2map = util_supportbean.request2map(request);

		}

		
		try{
			if(!isRemoteEjb)
				rd = currentStream.redirect(servletContext, currentStreamRedirect, id_action);
			else{
				info_action iaction = currentStream.redirect(request2map, currentStreamRedirect, id_action);
				if(iaction!=null)
					rd = currentStreamRedirect.redirect(servletContext, iaction);				
			}
				
		}catch(Exception e){

		}
		try{
			currentStream.onPostRedirect(rd);
		}catch(Exception e){
		}

		if(rd==null) throw new bsControllerException("Controller generic redirect error. Stream: ["+currentStream.get_infostream().getName()+"] ",request,iStub.log_ERROR);
		else{
			try{

				String id_rtype=(String)request.getAttribute(CONST_ID_REQUEST_TYPE);
				if(id_rtype==null) id_rtype = CONST_REQUEST_TYPE_FORWARD;

				if(id_rtype.equalsIgnoreCase(CONST_REQUEST_TYPE_FORWARD)){
					if(!response.isCommitted()) rd.forward(request,response);
					else
						rd.include(request,response);
				}else{
					rd.include(request,response);
				}

			}catch(Exception e){
				throw new bsControllerException("Controller generic redirect error. Action: ["+currentStream.get_infostream().getName()+"] ->" +e.toString(),request,iStub.log_ERROR);
			}
		}

	}

	
	public static void updateResponseContentType(info_redirect iRedirect, HttpServletResponse response, int status){
		if(iRedirect!=null && response!=null){
			if(iRedirect.getContentType()!=null && !iRedirect.getContentType().equals("")){
				if(iRedirect.getContentEncoding()!=null && !iRedirect.getContentEncoding().equals(""))
    				response.setHeader("Content-Type",iRedirect.getContentType().toLowerCase()+
    						((iRedirect.getContentEncoding()!=null)?";charset="+iRedirect.getContentEncoding():"")
    				);
				else
					response.setHeader("Content-Type",iRedirect.getContentType().toLowerCase());
				
			}
			if(iRedirect.getContentName()!=null && !iRedirect.getContentName().equals(""))
				response.setHeader("Content-Disposition","attachment; filename="+iRedirect.getContentName());
			if(iRedirect.getContentEncoding()!=null && !iRedirect.getContentEncoding().equals(""))
				response.setHeader("Content-Transfer-Encoding",iRedirect.getContentEncoding());
			if(status!=0)
				response.setStatus(status);
		}
	}

	public static Vector getActionStreams(String id_action){
		Vector _streams = null;
		info_action iActionMapped = (info_action)getAction_config().get_actions().get(id_action);
		if(iActionMapped==null)
			return new Vector();
		else if(iActionMapped.getVm_streams()!=null)
			_streams = iActionMapped.getVm_streams();
		else{
			_streams = new Vector();
			Vector _streams_orig = (Vector)getAction_config().get_streams_apply_to_actions().get("*");

			if(_streams_orig!=null) _streams.addAll(_streams_orig);
			Vector _streams4action = (Vector)getAction_config().get_streams_apply_to_actions().get(id_action);
			if(_streams4action!=null){
				Vector _4add = new Vector();
				HashMap _4remove = new HashMap();
				for(int i=0;i<_streams4action.size();i++){
					info_stream currentis = (info_stream)_streams4action.get(i);
					if(currentis.get_apply_to_action()!=null){
						info_apply_to_action currentiata = (info_apply_to_action)currentis.get_apply_to_action().get(id_action);
						if(currentiata.getExcluded()!=null && currentiata.getExcluded().equalsIgnoreCase("true"))
							_4remove.put(currentis.getName(),currentis.getName());
						else _4add.add(currentis);
					}
				}
				_streams.addAll(_4add);
				if(_4remove.size()>0){
					int i=0;
					while(i<_streams.size()){
						info_stream currentis = (info_stream)_streams.get(i);
						if(_4remove.get(currentis.getName())!=null) _streams.remove(i);
						else i++;
					}
				}
				_streams = new util_sort().sort(_streams,"int_order","A");
			}
			iActionMapped.setVm_streams(_streams);
		}
		return _streams;

	}

	public static Vector getActionStreams_(String id_action){

		Vector _streams_orig = (Vector)getAction_config().get_streams_apply_to_actions().get("*");


//Modifica 20100521 Warning
/*
		try{
			_streams = (Vector)util_cloner.clone(_streams_orig);
		}catch(Exception e){
		}
*/
		Vector _streams4action = (Vector)getAction_config().get_streams_apply_to_actions().get(id_action);
		if(_streams4action==null) return (_streams_orig==null)?new Vector():_streams_orig;
		else{
			Vector _streams = new Vector();
			if(_streams_orig!=null) _streams.addAll(_streams_orig);
			
			Vector _4add = new Vector();
			HashMap _4remove = new HashMap();
/*			
			for(int i=0;i<_streams.size();i++){
				info_stream currentis = (info_stream)_streams.get(i);
				if(currentis.get_apply_to_action()!=null){
					info_apply_to_action currentiata = (info_apply_to_action)currentis.get_apply_to_action().get(id_action);
					if(currentiata==null)
						currentiata = (info_apply_to_action)currentis.get_apply_to_action().get("*");
					if(currentiata!=null && currentiata.getExcluded()!=null && currentiata.getExcluded().equalsIgnoreCase("true"))
						_4remove.put(currentis.getName(),currentis.getName());
				}
			}
*/			
			for(int i=0;i<_streams4action.size();i++){
				info_stream currentis = (info_stream)_streams4action.get(i);
				if(currentis.get_apply_to_action()!=null){
					info_apply_to_action currentiata = (info_apply_to_action)currentis.get_apply_to_action().get(id_action);
					if(currentiata!=null){ 
						if(currentiata.getExcluded()!=null && currentiata.getExcluded().equalsIgnoreCase("true"))
							_4remove.put(currentis.getName(),currentis.getName());
						else _4add.add(currentis);
					}
				}
			}
			_streams.addAll(_4add);
			if(_4remove.size()>0){
				int i=0;
				while(i<_streams.size()){
					info_stream currentis = (info_stream)_streams.get(i);
					if(_4remove.get(currentis.getName())!=null) _streams.remove(i);
					else i++;
				}
			}
			_streams = new util_sort().sort(_streams,"int_order","A");
			return _streams;
		}
	}


	public static HttpServletResponse service(String id_action, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response)throws ServletException, UnavailableException {
		return service(id_action, null, id_action, servletContext, request, response);
	}	
	
	public static HttpServletResponse service(String id_action, String id_call, String id_complete, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response)throws ServletException, UnavailableException {

		auth_init auth = checkAuth_init(request);
		try{
			if(idApp==null) idApp = util_format.replace(getContextPathFor5(servletContext), "/", "");
		}catch(Exception ex){
			if(idApp==null) idApp = util_format.replace( request.getContextPath(),"/", "");
		}catch(Throwable ex){
			if(idApp==null) idApp = util_format.replace(request.getContextPath(),"/", "");
		}
		if(auth==null) auth = new auth_init();

		StatisticEntity stat = null;
		try{
			stat = new StatisticEntity(
					String.valueOf(request.getSession().getId()),
					auth.get_user_ip(),
					auth.get_matricola(),
					auth.get_language(),
					id_complete,
					null,
					new Date(),
					null,
					request);
		}catch(Exception e){
		}

		String id_rtype=request.getParameter(CONST_ID_REQUEST_TYPE);
		if(id_rtype==null) id_rtype = (String)request.getAttribute(CONST_ID_REQUEST_TYPE);
		if(id_rtype==null) id_rtype = CONST_REQUEST_TYPE_FORWARD; 

		request.setAttribute(CONST_ID_REQUEST_TYPE, id_rtype);

/*		
		if(id_call==null)
			request.setAttribute(CONST_ID_COMPLETE,id_action);
		else
			request.setAttribute(CONST_ID_COMPLETE,
					id_action+
					((bsController.getAppInit().get_actioncall_separator()==null)?"":bsController.getAppInit().get_actioncall_separator())+
					id_call);
*/
		if(id_complete==null){
			if(id_call==null)
				id_complete = id_action;
			else
				id_complete = id_action+
				((bsController.getAppInit().get_actioncall_separator()==null)?"":bsController.getAppInit().get_actioncall_separator())+
				id_call;
		}
		request.setAttribute(CONST_ID_COMPLETE,id_complete);
		
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
		request.setAttribute(CONST_ID_CALL,id_call);
		request.setAttribute(CONST_ID,id_action);

		if(id_action!=null){

			i_action action_instance = null;

			try{
				Vector _streams = getActionStreams_(id_action);

				info_stream blockStreamEnter = performStream_EnterRS(_streams, id_action,action_instance, servletContext, request, response);
				if(blockStreamEnter!=null){
					isException(action_instance, request);
					if(stat!=null){
						stat.setFt(new Date());
						stat.setException(new Exception("Blocked by STREAM ENTER:["+blockStreamEnter.getName()+"]"));
						putToStatisticProvider(stat);
					}
					return response;
				}


				action_instance = performAction(id_action, id_call, servletContext, request, response);

				if(action_instance==null){
					isException(action_instance, request);
					if(stat!=null){
						stat.setFt(new Date());
						stat.setException(new Exception("ACTION INSTANCE is NULL"));
						putToStatisticProvider(stat);
					}
					return response;
				}

				if(	action_instance.get_infoaction()!=null &&
					action_instance.get_infoaction().getStatistic()!=null &&
					action_instance.get_infoaction().getStatistic().equalsIgnoreCase("false")) stat=null;


				if(!action_instance.isIncluded()){
					if(!id_rtype.equalsIgnoreCase(CONST_REQUEST_TYPE_FORWARD)) action_instance.setIncluded(true);
				}else{
					if(id_rtype.equalsIgnoreCase(CONST_REQUEST_TYPE_FORWARD)) action_instance.setIncluded(false);
				}

				if(request.getAttribute(CONST_BEAN_$INSTANCEACTIONPOOL)==null)
					request.setAttribute(CONST_BEAN_$INSTANCEACTIONPOOL,new HashMap());
				HashMap included_pool = (HashMap)request.getAttribute(CONST_BEAN_$INSTANCEACTIONPOOL);
				if(action_instance.get_infoaction()!=null && action_instance.get_infoaction().getName()!=null)
					included_pool.put(action_instance.get_infoaction().getName(),action_instance);
				else if(action_instance.get_infoaction()!=null && action_instance.get_infoaction().getPath()!=null)
					included_pool.put(action_instance.get_infoaction().getPath(),action_instance);

				request.setAttribute(CONST_BEAN_$INSTANCEACTION,action_instance);

				if(request.getParameter(CONST_ID_JS4AJAX)==null && action_instance!=null && action_instance.get_bean()!=null)
					request.setAttribute(CONST_ID_JS4AJAX,action_instance.get_bean().getJs4ajax());



//				if(action_instance.getCurrent_redirect()!=null){
//					if( !action_instance.getCurrent_redirect().is_avoidPermissionCheck()){
						info_stream blockStreamExit = performStream_ExitRS(_streams, id_action,action_instance, servletContext, request, response);
						if(blockStreamExit!=null){
							isException(action_instance, request);
							if(stat!=null){
								stat.setFt(new Date());
								stat.setException(new Exception("Blocked by STREAM EXIT:["+blockStreamExit.getName()+"]"));
								putToStatisticProvider(stat);
							}
							return response;
						}
//					}
//				}
				if(action_instance.getCurrent_redirect()!=null){
					request.removeAttribute(CONST_ID_REQUEST_TYPE);
					response = execRedirect(action_instance,servletContext,request,response,true);
				}else if(action_instance.get_infoaction()!=null && action_instance.get_infoaction().getIRedirect()!=null){
					updateResponseContentType(action_instance.get_infoaction().getIRedirect(),response,0);
				}

				environmentState(request,id_action);



			}catch(bsControllerException e){
				if(request.getAttribute(CONST_BEAN_$ERRORACTION)==null) request.setAttribute(CONST_BEAN_$ERRORACTION, e.toString());
				else request.setAttribute(CONST_BEAN_$ERRORACTION, request.getAttribute(CONST_BEAN_$ERRORACTION) + ";" +e.toString());
				new bsControllerException(e,iStub.log_ERROR);
				isException(action_instance, request);
				addAsMessage(e,request);
//				request.setAttribute(CONST_BEAN_$ERRORACTION, e);
				if(request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)!=null) request.getSession().removeAttribute(bsController.CONST_BEAN_$LISTMESSAGE);
				service_ErrorRedirect(id_action,servletContext,request, response);
				stat.setException(e);
			}catch(Exception ex){
				if(request.getAttribute(CONST_BEAN_$ERRORACTION)==null) request.setAttribute(CONST_BEAN_$ERRORACTION, ex.toString());
				else request.setAttribute(CONST_BEAN_$ERRORACTION, request.getAttribute(CONST_BEAN_$ERRORACTION) + ";" +ex.toString());

				new bsControllerException(ex,iStub.log_ERROR);
				isException(action_instance, request);
				addAsMessage(ex,request);
//				request.setAttribute(CONST_BEAN_$ERRORACTION, ex);
//				if(request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)!=null) request.getSession().removeAttribute(bsController.CONST_BEAN_$LISTMESSAGE);

				service_ErrorRedirect(id_action,servletContext,request, response);
				stat.setException(ex);
			}catch(Throwable t){
				if(request.getAttribute(CONST_BEAN_$ERRORACTION)==null) request.setAttribute(CONST_BEAN_$ERRORACTION, t.toString());
				else request.setAttribute(CONST_BEAN_$ERRORACTION, request.getAttribute(CONST_BEAN_$ERRORACTION) + ";" +t.toString());

				new bsControllerException(t,iStub.log_ERROR);
				isException(action_instance, request);
				addAsMessage(t,request);
				service_ErrorRedirect(id_action,servletContext,request, response);
				stat.setException(t);
			}finally{
				if(stat!=null){
					stat.setFt(new Date());
					putToStatisticProvider(stat);
				}
			}


		}
		return response;
	}



	public static void addAsMessage(Throwable ex, HttpServletRequest request){
		if(ex!=null && request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)!=null){
			message mess = new message();
			mess.setDESC_MESS(ex.toString());
			mess.setTYPE("E");
			((Vector)request.getSession().getAttribute(bsController.CONST_BEAN_$LISTMESSAGE)).add(mess);
		}
	}
	public static void isException(i_action action_instance, HttpServletRequest request){
		if(action_instance!=null) request.setAttribute(CONST_BEAN_$INSTANCEACTION,action_instance);
		if(request.getParameter(CONST_ID_JS4AJAX)==null && action_instance!=null && action_instance.get_bean()!=null)
			request.setAttribute(CONST_ID_JS4AJAX,action_instance.get_bean().getJs4ajax());
		if(request.getParameter(CONST_ID_JS4AJAX)==null && action_instance==null)
			request.setAttribute(CONST_ID_JS4AJAX,"false");
		request.removeAttribute(CONST_ID_REQUEST_TYPE);

	}

	public static i_action performAction(String id_action,  ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response) throws bsControllerException, Exception, Throwable{
		return performAction(id_action, null, servletContext, request, response);
	}

	public static i_action performAction(String id_action, String id_call, ServletContext servletContext,
				HttpServletRequest request, HttpServletResponse response) throws bsControllerException, Exception, Throwable{
		i_action prev_action_instance = null;

		String id_rtype = (String)request.getAttribute(CONST_ID_REQUEST_TYPE);
		if(id_rtype!=null && id_rtype.equalsIgnoreCase(CONST_REQUEST_TYPE_INCLUDE)){

		}else{
			String id_prev = request.getParameter(CONST_BEAN_$NAVIGATION);
			if(id_prev!=null && id_prev.indexOf(":")>-1){
				id_prev = id_prev.substring(0,id_prev.indexOf(":"));
				prev_action_instance = getPrevActionInstance(id_prev,id_action,request,response);
				if(prev_action_instance!=null && prev_action_instance.getCurrent_redirect()!=null){

					if(request.getAttribute(CONST_BEAN_$INSTANCEACTIONPOOL)==null)
						request.setAttribute(CONST_BEAN_$INSTANCEACTIONPOOL,new HashMap());
					HashMap included_pool = (HashMap)request.getAttribute(CONST_BEAN_$INSTANCEACTIONPOOL);
					if(prev_action_instance.get_infoaction()!=null && prev_action_instance.get_infoaction().getName()!=null)
						included_pool.put(prev_action_instance.get_infoaction().getName(),prev_action_instance);
					else if(prev_action_instance.get_infoaction()!=null && prev_action_instance.get_infoaction().getPath()!=null)
						included_pool.put(prev_action_instance.get_infoaction().getPath(),prev_action_instance);

					request.setAttribute(CONST_BEAN_$INSTANCEACTION,prev_action_instance);
					execRedirect(prev_action_instance,servletContext,request,response,false);
					return null;
				}
			}
		}
		i_action action_instance = null;
		action_instance =getActionInstance(id_action,id_call, request,response);
		return action_instance;
	}

	public static boolean performStream_Enter(Vector _streams, String id_action,i_action action_instance, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws bsControllerException, Exception, Throwable{
		if(performStream_EnterRS(_streams, id_action, action_instance, servletContext, request, response)!=null) return false;
		return true;
	}

	public static info_stream performStream_EnterRS(Vector _streams, String id_action,i_action action_instance, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws bsControllerException, Exception, Throwable{

		for(int i=0;i<_streams.size();i++){
			info_stream iStream = (info_stream)_streams.get(i);
			i_stream currentStream = getAction_config().streamFactory(iStream.getName(),request.getSession(), servletContext);
			if(currentStream!=null){	
				HashMap request2map = null;
				boolean isRemoteEjb=false;
				
				if(currentStream!=null && currentStream.getInfo_context()!=null && currentStream.getInfo_context().isRemote()){
					isRemoteEjb=true;
					try{
						request2map = (HashMap)currentStream.asStream().getClass()
											.getDeclaredMethod("convertRequest2Map", new Class[]{HttpServletRequest.class})
											.invoke(null, new Object[]{request});
					}catch (Exception e) {
						new bsControllerException(e, iStub.log_ERROR);
					}catch (Throwable e) {
						new bsControllerException(e, iStub.log_ERROR);
					}
					if(request2map==null)
						request2map = new HashMap();
//					request2map = util_supportbean.request2map(request);

				}

				try{
					if(!isRemoteEjb)
						currentStream.onPreEnter(request, response);
					else
						currentStream.onPreEnter(request2map);
				}catch(Exception e){
					currentStream.onPreEnter(null,null);
				}
				redirects currentStreamRedirect = null;
				try{
					if(!isRemoteEjb)
						currentStreamRedirect = currentStream.streamservice_enter(request, response);
					else
						currentStream.streamservice_enter(request2map);
				}catch(Exception e){
					currentStream.streamservice_enter(null,null);
				}
				try{
					if(!isRemoteEjb)
						currentStream.onPostEnter(currentStreamRedirect, request, response);
					else
						currentStream.onPostEnter(currentStreamRedirect, request2map);
				}catch(Exception e){
					currentStream.onPostEnter(currentStreamRedirect, null,null);
				}
				if(currentStreamRedirect!=null){
					isException(action_instance, request);
					execRedirect(currentStream, currentStreamRedirect, id_action, servletContext, request, response);
					return iStream;
				}
			}
		}
		return null;
	}

	public static boolean performStream_Exit(Vector _streams, String id_action,i_action action_instance, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws bsControllerException, Exception, Throwable{
		if(performStream_ExitRS(_streams, id_action, action_instance, servletContext, request, response)!=null) return false;
		return true;
	}

	public static info_stream performStream_ExitRS(Vector _streams, String id_action,i_action action_instance, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws bsControllerException, Exception, Throwable{

		for(int i=_streams.size()-1;i>-1;i--){
			info_stream iStream = (info_stream)_streams.get(i);
			i_stream currentStream = getAction_config().streamFactory(iStream.getName(), request.getSession(), servletContext);
			if(currentStream!=null){
				HashMap request2map = null;
				boolean isRemoteEjb=false;
				
				if(currentStream!=null && currentStream.getInfo_context()!=null && currentStream.getInfo_context().isRemote()){
					isRemoteEjb=true;
					try{
						request2map = (HashMap)currentStream.asStream().getClass()
											.getDeclaredMethod("convertRequest2Map", new Class[]{HttpServletRequest.class})
											.invoke(null, new Object[]{request});
					}catch (Exception e) {
						new bsControllerException(e, iStub.log_ERROR);
					}catch (Throwable e) {
						new bsControllerException(e, iStub.log_ERROR);
					}
					if(request2map==null)
						request2map = new HashMap();
//					request2map = util_supportbean.request2map(request);

				}
				
				try{
					if(!isRemoteEjb)
						currentStream.onPreExit(request, response);
					else
						currentStream.onPreExit(request2map);
				}catch(Exception e){
					currentStream.onPreExit(null,null);
				}
				redirects currentStreamRedirect = null;
				try{
					if(!isRemoteEjb)
						currentStreamRedirect =currentStream.streamservice_exit(request, response);
					else
						currentStream.streamservice_exit(request2map);
				}catch(Exception e){
					currentStream.streamservice_exit(null,null);
				}
				try{
					if(!isRemoteEjb)
						currentStream.onPostExit(currentStreamRedirect,request, response);
					else
						currentStream.onPostExit(currentStreamRedirect, request2map);
				}catch(Exception e){
					currentStream.onPostExit(currentStreamRedirect, null,null);
				}
				if(currentStreamRedirect!=null){
					isException(action_instance, request);
					execRedirect(currentStream, currentStreamRedirect, id_action, servletContext, request, response);
					return iStream;
				}
			}
		}
		return null;
	}


	public static i_bean getCurrentForm(String id_current,HttpServletRequest request){
		if(id_current==null) return null;

		info_navigation fromNav = getFromInfoNavigation(null, request);
		if(fromNav!=null){
			info_navigation nav = fromNav.find(id_current);
			if(nav!=null){
				i_bean content = nav.get_content();
				if(content!=null)
					content.onGetFromNavigation();

				return content;
			}
		}
		try{
			info_action infoAction = (info_action)getAction_config().get_actions().get(id_current);
			if(	infoAction.getMemoryInSession().equalsIgnoreCase("true")){
/*
				i_bean content = (i_bean)
					((HashMap)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION))
					.get(infoAction.getPath());
*/
				i_bean content = (i_bean)getFromOnlySession(infoAction.getPath(), request);

				if(content!=null)
					content.onGetFromSession();
				return content;
			}
			if(	infoAction.getMemoryAsLastInstance().equalsIgnoreCase("true")){
				i_bean content = null;
				try{
					content = (i_bean)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYASLASTINSTANCE);
				}catch(Exception e){
				}
				if(	content!=null &&
					content.get_infoaction()!=null &&
					content.get_infoaction().getPath()!=null &&
					content.get_infoaction().getPath().equals(infoAction.getPath())){
						content.onGetFromLastInstance();
						return content;
				}
			}
		}catch(Exception e){
			if( e instanceof java.lang.NullPointerException){
			}else new bsControllerException(e,iStub.log_DEBUG);
		}


		return null;
	}


	public static void setInfoNav_CurrentForm(i_action action_instance, HttpServletRequest request){
		boolean go = false;
		i_bean form = null;
		if(action_instance.isBeanEqualAction())
			form = action_instance;
		else
			form = action_instance.get_bean();
		
		try{
			if(form.get_infoaction().getNavigated().equalsIgnoreCase("true")) go = true;
		}catch(Exception ex){
		}
		if(!go){
			return;
		}

		info_navigation nav = new info_navigation();
		try{
			nav.init(form.get_infoaction(),null,new info_service(request),null);
			info_navigation fromNav = getFromInfoNavigation(null, request);

			if(fromNav!=null)
				fromNav.add(nav);
			else
				setToInfoNavigation(nav, request);
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}

	}


	public static void setCurrentForm(i_action action_instance, HttpServletRequest request){
		boolean cloned = (request.getParameter(CONST_ID_EXEC_TYPE)==null)?false:request.getParameter(CONST_ID_EXEC_TYPE).equalsIgnoreCase(CONST_ID_EXEC_TYPE_CLONED);
		if(cloned) return;
		boolean go = false;
		i_bean form = null;
		if(action_instance.isBeanEqualAction())
			form = action_instance;
		else
			form = action_instance.get_bean();
		
		try{
			if(form.get_infoaction().getNavigated().equalsIgnoreCase("true")) go = true;
		}catch(Exception ex){
		}
		if(!go || action_instance.getCurrent_redirect()==null){
			try{
				if(form.get_infoaction().getMemoryInSession().equalsIgnoreCase("true")){
/*
					HashMap fromSession = null;
					fromSession = (HashMap)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
					if(fromSession==null){
						fromSession = new HashMap();
						request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,fromSession);
					}
*/
					if(form!=null)
						form.onAddToSession();
// Mod 20150402 --
//					fromSession.put(form.get_infobean().getName(),form);
					if(form.get_infoaction()!=null && form.get_infoaction().getPath()!=null){
						form.clearBeforeStore();
						setToOnlySession(form.get_infoaction().getPath(),form, request);
					}
//						fromSession.put(form.get_infoaction().getPath(),form);
				}
				if(form.get_infoaction().getMemoryAsLastInstance().equalsIgnoreCase("true")){
					if(form!=null){
						form.onAddToLastInstance();
						form.clearBeforeStore();
					}
					request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYASLASTINSTANCE,form);
				}

			}catch(Exception e){
			}
			return;
		}else{
			try{
				info_navigation nav = new info_navigation();
				if(form!=null)
					form.clearBeforeStore();
				nav.init(form.get_infoaction(),action_instance.getCurrent_redirect().get_inforedirect(),new info_service(request),form);
				info_navigation fromNav = getFromInfoNavigation(null, request);
				if(fromNav!=null){
					if(form!=null) form.onAddToNavigation();
					fromNav.add(nav);
				}
				else setToInfoNavigation(nav, request);
			}catch(Exception e){
			}
		}
	}



	public static Object getProperty(String key, HttpServletRequest request){
		if(key==null) return null;
		else if(key.equals(bsConstants.CONST_BEAN_$NAVIGATION))
			return getFromInfoNavigation(null, request);
		else if(key.equals(bsConstants.CONST_BEAN_$ONLYINSSESSION))
			return getFromOnlySession(null, request);
		else
			return getFromLocalContainer(key);
	}

	
	public static i_log_generator checkLogGenerator(log_init _logInit){ 
		if(	logG!=null &&
			(_logInit==null || (_logInit!=null && logInit!=null && logInit.equals(_logInit))) &&
			(logInit!=null && !logInit.isChanged()) &&
			!reInit)
			return logG;
		
	
		int cnt = util_reflect.countPresentClassInfo("bsController.java","checkLogGenerator");
		if(cnt>1)
			return null;
		
		if(logInit!=null) 
			logInit.setChanged(false);
		
		if(_logInit!=null){
			_logInit.setChanged(false);
			logInit = _logInit;
		}
			
		if(logInit==null){
			logInit = new log_init();
				logInit.init();
		}
			
		if(logInit.get_LogGenerator()==null || logInit.get_LogGenerator().equals("")){
			if(	(logInit.get_LogPath()==null || logInit.get_LogPath().equals("")) &&
				(logInit.get_LogStub()==null || logInit.get_LogStub().equals(""))
			){
				try{
					Object transf = null;
					if(getAppInit()!=null && getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equals("false")){
						try{
							transf = util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  "it.classhidra.core.tool.exception.bsIntegrator", "it.classhidra.core.tool.exception.bsIntegrator", null);
						}catch(Exception e){
						}
					}
					if(transf==null && getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equals("false")){
						try{
							transf = util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  "it.classhidra.core.tool.exception.bsIntegrator", "it.classhidra.core.tool.exception.bsIntegrator", null);
						}catch(Exception e){
						}
					}
					if(transf==null && getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equals("false")){
						try{
							transf = util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  "it.classhidra.core.tool.exception.bsIntegrator", "it.classhidra.core.tool.exception.bsIntegrator", null);
						}catch(Exception e){
						}
					}
					checkDefaultProvider(null);
					if(transf==null && getCdiDefaultProvider()!=null){
						try{
							transf = util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  "it.classhidra.core.tool.exception.bsIntegrator", "it.classhidra.core.tool.exception.bsIntegrator", null);
						}catch(Exception e){
						}
					}
					if(transf==null && getEjbDefaultProvider()!=null){
						try{
							transf = util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  "it.classhidra.core.tool.exception.bsIntegrator", "it.classhidra.core.tool.exception.bsIntegrator", null);
						}catch(Exception e){
						}
					}

					if(transf == null)
						transf =Class.forName("it.classhidra.core.tool.exception.bsIntegrator").newInstance();

					logInit = (log_init)util_reflect.getValue(transf, "getLogInit", null);
				}catch(Exception e){
				}catch(Throwable t){
				}
			}
		}
		
		
		if(logInit.get_LogGenerator()!=null && !logInit.get_LogGenerator().equals("")){
			try{
				boolean ok = false;
				i_log_generator logGenerator = null;

				if(getAppInit()!=null && getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equals("false")){
					try{
						logGenerator = (i_log_generator)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  logInit.get_LogGenerator(), i_log_generator.class.getName(), null);
						if(logGenerator!=null)
							ok = true;
					}catch(Exception e){
//						logG = (i_log_generator)Class.forName(logInit.get_LogGenerator()).newInstance();
					}
				}
				if(!ok && getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equals("false")){
					try{
						logGenerator = (i_log_generator)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  logInit.get_LogGenerator(), i_log_generator.class.getName(), null);
						if(logGenerator!=null)
							ok = true;
					}catch(Exception e){
//						logG = (i_log_generator)Class.forName(logInit.get_LogGenerator()).newInstance();
					}
				}
				if(!ok && getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equals("false")){
					try{
						logGenerator = (i_log_generator)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  logInit.get_LogGenerator(), i_log_generator.class.getName(), null);
						if(logGenerator!=null)
							ok = true;
					}catch(Exception e){
//						logG = (i_log_generator)Class.forName(logInit.get_LogGenerator()).newInstance();
					}
				}		
				checkDefaultProvider(null);
				if(!ok && getCdiDefaultProvider()!=null){
					try{
						logGenerator = (i_log_generator)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(), logInit.get_LogGenerator(), i_log_generator.class.getName(), null);
						if(logGenerator!=null)
							ok = true;
					}catch(Exception e){
					}
				}
				if(!ok && getEjbDefaultProvider()!=null){
					try{
						logGenerator = (i_log_generator)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(), logInit.get_LogGenerator().replace("$", "ejb_"), i_log_generator.class.getName(), null);
						if(logGenerator!=null)
							ok = true;
					}catch(Exception e){
					}
				}
				
				if(!ok)
					logGenerator = (i_log_generator)Class.forName(logInit.get_LogGenerator()).newInstance();
				else
					logG=null;
				
				logGenerator.setInit(logInit);
				return logGenerator;
			}catch(Exception e){
			}catch (Throwable t) {
			}
		}

		logG = new log_generator(logInit);
		return logG;
	}

	public static void writeLogS(HttpServletRequest request, String mess){
		if(request!=null){
			Vector s_log = (Vector)request.getSession().getAttribute(bsController.CONST_SESSION_LOG);
			if(s_log==null) s_log = new Vector();
			if(mess!=null && !mess.equals(""))  s_log.add(mess);
			request.getSession().setAttribute(bsController.CONST_SESSION_LOG, s_log);
		}
	}

	public static synchronized void writeLog(String msg, String level) {
		try{
			String classInfo=util_reflect.prepareClassInfo(new String[]{"bsController.java","bsControllerException.java","bsControllerMessageException.java"},new String[]{"writeLog","<init>","<init>"});
			if(getLogG()==null) 
				checkLogGenerator(null);
			getLogG().writeLog(msg,"","",classInfo,level);
		}catch(Exception e){}
	}
	public static synchronized void writeLog(Object msg, String level) {
		try{
			String classInfo=util_reflect.prepareClassInfo(new String[]{"bsController.java","bsControllerException.java","bsControllerMessageException.java"},new String[]{"writeLog","<init>","<init>"});
//			if(getLogG()==null) service_mountLog();
			if(getLogG()==null) 
				checkLogGenerator(null);
			getLogG().writeLog(msg.toString(),"","",classInfo,level);
		}catch(Exception e){}
	}

	public static synchronized void writeLog(HttpServletRequest _request, String msg, String level){
		String classInfo="";
//		if (level!=iStub.log_INFO )
			classInfo = util_reflect.prepareClassInfo(new String[]{"bsController.java","bsControllerException.java","bsControllerMessageException.java"},new String[]{"writeLog","<init>","<init>"});
		try{
			auth_init aInit = null;
			try{
				aInit = (auth_init)_request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION);
			}catch(Exception e){
			}
			if(aInit!=null)
				getLogG().writeLog(_request,msg,aInit.get_user_ip(),aInit.get_matricola(),classInfo, level);
			else getLogG().writeLog(msg,"","",classInfo,level);
		}catch(Exception e){}
	}


	public static String writeLabel(String lang, String cd_mess, String def, HashMap parameters) {
		if(lang==null || cd_mess==null) 
			return message.decodeParameters(def,parameters);
		try{
			if(bsController.getMess_config().get_messages().get(lang+"."+cd_mess)!=null)
				return ((message)bsController.getMess_config().get_messages().get(lang+"."+cd_mess)).getDESC_MESS(parameters);
			if(bsController.getMess_config().get_messages().get(lang.toLowerCase()+"."+cd_mess)!=null)
				return ((message)bsController.getMess_config().get_messages().get(lang.toLowerCase()+"."+cd_mess)).getDESC_MESS(parameters);
			if(bsController.getMess_config().get_messages().get(lang.toUpperCase()+"."+cd_mess)!=null)
				return ((message)bsController.getMess_config().get_messages().get(lang.toUpperCase()+"."+cd_mess)).getDESC_MESS(parameters);
			return def;
		}catch(Exception e){
			return def;
		}
	}

	public static String writeLabel(HttpServletRequest request, String cd_mess, String def, HashMap parameters) {
		if(request==null || cd_mess==null)
			return message.decodeParameters(def,parameters);
		String lang="IT";
		try{
			auth_init aInit = (auth_init)request.getSession().getAttribute(bsController.CONST_BEAN_$AUTHENTIFICATION);
			lang = aInit.get_language();
		}catch(Exception e){
		}
		try{
			if(bsController.getMess_config().get_messages().get(lang+"."+cd_mess)!=null)
				return ((message)bsController.getMess_config().get_messages().get(lang+"."+cd_mess)).getDESC_MESS(parameters);
			if(bsController.getMess_config().get_messages().get(lang.toLowerCase()+"."+cd_mess)!=null)
				return ((message)bsController.getMess_config().get_messages().get(lang.toLowerCase()+"."+cd_mess)).getDESC_MESS(parameters);
			if(bsController.getMess_config().get_messages().get(lang.toUpperCase()+"."+cd_mess)!=null)
				return ((message)bsController.getMess_config().get_messages().get(lang.toUpperCase()+"."+cd_mess)).getDESC_MESS(parameters);

			return "::"+message.decodeParameters(def,parameters)+"::";
		}catch(Exception ex){
			if(cd_mess.equals("")) return message.decodeParameters(def,parameters);
			return "::"+message.decodeParameters(def,parameters)+"::";
		}
	}

	public static boolean isSessionValid(HttpServletRequest request){

		String id = request.getParameter(bsController.CONST_ID_$ACTION);
		boolean isController = (request.getRequestURI().indexOf("/Controller")>-1);
		if(id==null) id = request.getParameter(bsController.CONST_ID_MENU_MENUSOURCE);
		if(	request==null ||
			request.getSession(false)==null	||
			(request.getSession(false).isNew() && id!=null && !isController)) return false;
		else return true;
	}


	public static void service_AuthRedirect(String id_action,ServletContext servletContext, HttpServletRequest request, HttpServletResponse response){
		String redirectURI="";
		try{
			if(getAction_config()==null || getAction_config().getAuth_error()==null || getAction_config().getAuth_error().equals("")){
				redirectURI="";
			}else redirectURI = getAction_config().getAuth_error();
			if(!response.isCommitted()) servletContext.getRequestDispatcher(redirectURI).forward(request,response);
			else
				servletContext.getRequestDispatcher(redirectURI).include(request,response);
		}catch(Exception ex){
			writeLog(request, "Controller authentication error. Action: ["+id_action+"] URI: ["+redirectURI+"]");
		}
	}

	public static void service_ErrorRedirect(String id_action,ServletContext servletContext, HttpServletRequest request, HttpServletResponse response){
		String redirectURI="";
		try{
			if(getAction_config()==null || getAction_config().getError()==null || getAction_config().getError().equals(""))
				redirectURI="/error.jsp";
			else redirectURI = getAction_config().getError();
			if(!response.isCommitted()) servletContext.getRequestDispatcher(redirectURI).forward(request,response);
			else
				servletContext.getRequestDispatcher(redirectURI).include(request,response);
		}catch(Exception ex){
			writeLog(request, "Controller generic redirect error. Action: ["+id_action+"] URI: ["+redirectURI+"]");
		}
	}

	public static void service_ActionErrorRedirect(String id_action,ServletContext servletContext, HttpServletRequest request, HttpServletResponse response){
		String redirectURI="";
		try{
			i_action action_instance = getAction_config().actionFactory(id_action,request.getSession(),request.getSession().getServletContext());
			redirectURI = action_instance.get_infoaction().getError();
			if(redirectURI.equals("")){
				if(getAction_config()==null || getAction_config().getError()==null || getAction_config().getError().equals(""))
					redirectURI="";
				else redirectURI = getAction_config().getError();
			}
			if(!response.isCommitted()) servletContext.getRequestDispatcher(redirectURI).forward(request,response);
			else
				servletContext.getRequestDispatcher(redirectURI).include(request,response);

		}catch(Exception ex){
			writeLog(request, "Controller generic redirect error. Action: ["+id_action+"] URI: ["+redirectURI+"]");
		}
	}


	public static void service_Redirect(String redirectURI,ServletContext servletContext, HttpServletRequest request, HttpServletResponse response){
		try{
			String id_rtype=(String)request.getAttribute(CONST_ID_REQUEST_TYPE);
			if(id_rtype==null) id_rtype = CONST_REQUEST_TYPE_FORWARD;
			if(id_rtype.equalsIgnoreCase(CONST_REQUEST_TYPE_FORWARD)){
				if(!response.isCommitted()) servletContext.getRequestDispatcher(redirectURI).forward(request,response);
				else
					servletContext.getRequestDispatcher(redirectURI).include(request,response);
			}else{
				servletContext.getRequestDispatcher(redirectURI).include(request,response);
			}

		}catch(Exception ex){
			writeLog(request, "Controller generic redirect error. Session timeout.");
		}
	}

	public static db_init getDbInit() {
		if(dbInit==null || reInit){
			dbInit=new db_init();
			dbInit.init();
		}
		return dbInit;
	}
	public static i_log_generator getLogG() {
		if(logInit==null){
			logInit = new log_init();
			logInit.init();
		}
		i_log_generator logGenerator = checkLogGenerator(null);
		if(logGenerator!=null && reInit)
			logGenerator.setInit(logInit);
		return logGenerator;
/*		
		if(logG==null|| reInit){
			logG = new log_generator(logInit);
		}
		return logG;
*/		
	}
	public static i_log_generator getLogG(log_init _logInit) {
		i_log_generator logGenerator = checkLogGenerator(_logInit);
		if(logGenerator!=null && reInit)
			logGenerator.setInit(_logInit);
		return logGenerator;

/*		
		if(_logInit!=null && reInit){
			logInit = _logInit;
			logG = new log_generator(logInit);
		}
		return logG;
*/		
	}
	public static log_init getLogInit() {
		if(logInit==null || reInit){
			logInit = new log_init();
			logInit.init();
		}
		return logInit;
	}
	public static load_message getMess_config() {
		i_ProviderWrapper wrapperConfigMess = checkLoadMessage(null);
		if(wrapperConfigMess!=null && wrapperConfigMess.getInstance()!=null){
			if(reInit){
				try{
					((load_message)wrapperConfigMess.getInstance()).reimposta();
					((load_message)wrapperConfigMess.getInstance()).load_from_resources();
					((load_message)wrapperConfigMess.getInstance()).init();
					if(!((load_message)wrapperConfigMess.getInstance()).isReadOk()){
						((load_message)wrapperConfigMess.getInstance()).initProperties(getAppInit().get_path_config()+CONST_XML_MESSAGES);
						((load_message)wrapperConfigMess.getInstance()).initWithFOLDER(getAppInit().get_path_config()+CONST_XML_MESSAGES_FOLDER+"/");
					}
				}catch(bsControllerException je){}
			}
			return ((load_message)wrapperConfigMess.getInstance());			
		}else{		
			if(mess_config==null || reInit){
				mess_config = new load_message();
				try{
					mess_config.load_from_resources();
					mess_config.init();
					if(!mess_config.isReadOk()){
						mess_config.initProperties(getAppInit().get_path_config()+CONST_XML_MESSAGES);
						mess_config.initWithFOLDER(getAppInit().get_path_config()+CONST_XML_MESSAGES_FOLDER+"/");
					}
				}catch(bsControllerException je){}
			}
			return mess_config;
		}
	}

	public static load_menu getMenu_config() {
		i_ProviderWrapper wrapperConfigMenu = checkLoadMenu(null);
		if(wrapperConfigMenu!=null && wrapperConfigMenu.getInstance()!=null){
			if(reInit){
				try{
					((load_menu)wrapperConfigMenu.getInstance()).reimposta();
					((load_menu)wrapperConfigMenu.getInstance()).load_from_resources();
					if(!((load_menu)wrapperConfigMenu.getInstance()).isReadOk())
						((load_menu)wrapperConfigMenu.getInstance()).load_from_resources();
					if(!((load_menu)wrapperConfigMenu.getInstance()).isReadOk())
						((load_menu)wrapperConfigMenu.getInstance()).initProperties(getAppInit().get_path_config()+CONST_XML_MENU);
					
				}catch(bsControllerException je){}
			}
			return ((load_menu)wrapperConfigMenu.getInstance());			
		}else{		
		
			if(menu_config==null || reInit){
				menu_config = new load_menu(null);
				try{
					menu_config.init();
					if(!menu_config.isReadOk()) menu_config.load_from_resources();
					if(!menu_config.isReadOk()) menu_config.initProperties(getAppInit().get_path_config()+CONST_XML_MENU);
				}catch(bsControllerException je){}
			}
			return menu_config;
		}
	}


	public static load_actions getAction_config() {
		i_ProviderWrapper wrapperConfigAction = checkLoadActions(null);
		if(wrapperConfigAction!=null && wrapperConfigAction.getInstance()!=null){
			if(reInit){
				try{
					((load_actions)wrapperConfigAction.getInstance()).reimposta();
					((load_actions)wrapperConfigAction.getInstance()).load_from_resources();
					((load_actions)wrapperConfigAction.getInstance()).init();
					((load_actions)wrapperConfigAction.getInstance()).initProperties(getAppInit().get_path_config()+CONST_XML_ACTIONS);
					((load_actions)wrapperConfigAction.getInstance()).initWithFOLDER(getAppInit().get_path_config()+CONST_XML_ACTIONS_FOLDER+"/");
	
				}catch(bsControllerException je){}
			}
			return ((load_actions)wrapperConfigAction.getInstance());
			
		}else{
			if(action_config==null || reInit){
				action_config = new load_actions();
				try{
					action_config.load_from_resources();
					action_config.init();
					action_config.initProperties(getAppInit().get_path_config()+CONST_XML_ACTIONS);
					action_config.initWithFOLDER(getAppInit().get_path_config()+CONST_XML_ACTIONS_FOLDER+"/");
	
				}catch(bsControllerException je){}
			}
			return action_config;
		}
	}

	public static load_authentication getAuth_config() {
		i_ProviderWrapper wrapperConfigAuth = checkLoadAuthentication(null);
		if(wrapperConfigAuth!=null && wrapperConfigAuth.getInstance()!=null){
			if(reInit){
				try{
					((load_authentication)wrapperConfigAuth.getInstance()).reimposta();
					((load_authentication)wrapperConfigAuth.getInstance()).load_from_resources();
					((load_authentication)wrapperConfigAuth.getInstance()).init();
					if(!((load_authentication)wrapperConfigAuth.getInstance()).isReadOk()){
						((load_authentication)wrapperConfigAuth.getInstance()).initProperties(getAppInit().get_path_config()+CONST_XML_AUTHENTIFICATIONS);
						((load_authentication)wrapperConfigAuth.getInstance()).initWithFOLDER(getAppInit().get_path_config()+CONST_XML_AUTHENTIFICATIONS_FOLDER+"/");
					}
				}catch(bsControllerException je){}
			}
			return ((load_authentication)wrapperConfigAuth.getInstance());
		}else{		
			if(auth_config==null || reInit){
				auth_config = new load_authentication();
				try{
					auth_config.load_from_resources();
					auth_config.init();
					if(!auth_config.isReadOk()){
						auth_config.initProperties(getAppInit().get_path_config()+CONST_XML_AUTHENTIFICATIONS);
						auth_config.initWithFOLDER(getAppInit().get_path_config()+CONST_XML_AUTHENTIFICATIONS_FOLDER+"/");
					}
				}catch(bsControllerException je){}
			}
			return auth_config;
		}
	}

	public static load_organization getOrg_config() {
		i_ProviderWrapper wrapperConfigOrganization = checkLoadOrganization(null);
		if(wrapperConfigOrganization!=null && wrapperConfigOrganization.getInstance()!=null){
			if(reInit){
				((load_organization)wrapperConfigOrganization.getInstance()).reimposta();
				try{
					((load_organization)wrapperConfigOrganization.getInstance()).load_from_resources();
					((load_organization)wrapperConfigOrganization.getInstance()).init();
					if(!((load_organization)wrapperConfigOrganization.getInstance()).isReadOk()){
						((load_organization)wrapperConfigOrganization.getInstance()).initProperties(getAppInit().get_path_config()+CONST_XML_ORGANIZATION);
					}
				}catch(bsControllerException je){}
			}
			return (load_organization)wrapperConfigOrganization.getInstance();
		}else{
			if(org_config==null || reInit){
				org_config = new load_organization();
				try{
					org_config.load_from_resources();
					org_config.init();
					if(!org_config.isReadOk()){
						org_config.initProperties(getAppInit().get_path_config()+CONST_XML_ORGANIZATION);
					}
				}catch(bsControllerException je){}
			}
			return org_config;
		}
	}

	public static app_init getAppInit() {
		if(appInit==null || reInit){
			appInit = new app_init();
			appInit.init();
		}
		return appInit;
	}


	public static String getContextConfigPath(ServletContext servletContext){
		String path=servletContext.getRealPath("/");
		if(appInit!=null && appInit.getApplication_path_config()!=null)
			path+=appInit.getApplication_path_config();
		if(path!=null){
			path=path.replace('\\', '/');
	//		while (path.indexOf("//")>-1) path=path.replace("//", "/");
			while (path.indexOf("//")>-1) path=util_format.replace(path,"//", "/");
		}
		return path;
	}

	public static void reloadAction_config(ServletContext servletContext) {
		i_ProviderWrapper wrapperConfigAction = checkLoadActions(null);
		if(wrapperConfigAction!=null && wrapperConfigAction.getInstance()!=null){
			if(reInit){
				String path = getAppInit().get_path_config()+CONST_XML_ACTIONS;
				String dir = getAppInit().get_path_config()+CONST_XML_ACTIONS_FOLDER+"/";
				try{
					((load_actions)wrapperConfigAction.getInstance()).initProperties(path);
					((load_actions)wrapperConfigAction.getInstance()).initWithFOLDER(dir);
				}catch(bsControllerException je){}
			}
			if(!((load_actions)wrapperConfigAction.getInstance()).isReadOk_File()){
				if(!((load_actions)wrapperConfigAction.getInstance()).load_from_resources(servletContext)){
					String path = getContextConfigPath(servletContext)+CONST_XML_ACTIONS;
					String dir = getContextConfigPath(servletContext)+CONST_XML_ACTIONS_FOLDER+"/";
					try{
						((load_actions)wrapperConfigAction.getInstance()).initProperties(path);
						((load_actions)wrapperConfigAction.getInstance()).initWithFOLDER(dir);
					}catch(bsControllerException je){}
					if(((load_actions)wrapperConfigAction.getInstance()).isReadOk_File())
						getAppInit().set_path_config(getContextConfigPath(servletContext));
				}
			}
			
		}else{
			if(reInit){
				String path = getAppInit().get_path_config()+CONST_XML_ACTIONS;
				String dir = getAppInit().get_path_config()+CONST_XML_ACTIONS_FOLDER+"/";
				action_config = getAction_config();
				try{
					action_config.initProperties(path);
					action_config.initWithFOLDER(dir);
				}catch(bsControllerException je){}
			}
			if(action_config!=null && !action_config.isReadOk_File()){
				if(!action_config.load_from_resources(servletContext)){
					String path = getContextConfigPath(servletContext)+CONST_XML_ACTIONS;
					String dir = getContextConfigPath(servletContext)+CONST_XML_ACTIONS_FOLDER+"/";
					try{
						action_config.initProperties(path);
						action_config.initWithFOLDER(dir);
					}catch(bsControllerException je){}
					if(action_config.isReadOk_File())
						getAppInit().set_path_config(getContextConfigPath(servletContext));
				}
			}
		}
	}

	public static void reloadMess_config(ServletContext servletContext) {
		i_ProviderWrapper wrapperConfigMess = checkLoadMessage(null);
		if(wrapperConfigMess!=null && wrapperConfigMess.getInstance()!=null){
			if(reInit){
				String path = getAppInit().get_path_config()+CONST_XML_MESSAGES;
				String dir = getAppInit().get_path_config()+CONST_XML_MESSAGES_FOLDER+"/";
				((load_message)wrapperConfigMess.getInstance()).reimposta();
				try{
					((load_message)wrapperConfigMess.getInstance()).initProperties(path);
					((load_message)wrapperConfigMess.getInstance()).initWithFOLDER(dir);
				}catch(bsControllerException je){}
			}
	
			if(!((load_message)wrapperConfigMess.getInstance()).isReadOk_File()){
				if(!((load_message)wrapperConfigMess.getInstance()).load_from_resources(servletContext)){
					String path = getContextConfigPath(servletContext)+CONST_XML_MESSAGES;
					String dir = getContextConfigPath(servletContext)+CONST_XML_MESSAGES_FOLDER+"/";
					try{
						((load_message)wrapperConfigMess.getInstance()).initProperties(path);
						((load_message)wrapperConfigMess.getInstance()).initWithFOLDER(dir);
					}catch(bsControllerException je){}
				}
			}
		}else{
			if(reInit){
				String path = getAppInit().get_path_config()+CONST_XML_MESSAGES;
				String dir = getAppInit().get_path_config()+CONST_XML_MESSAGES_FOLDER+"/";
				mess_config = new load_message();
				try{
					mess_config.initProperties(path);
					mess_config.initWithFOLDER(dir);
				}catch(bsControllerException je){}
			}
	
			if(mess_config!=null && !mess_config.isReadOk_File()){
				if(!mess_config.load_from_resources(servletContext)){
					String path = getContextConfigPath(servletContext)+CONST_XML_MESSAGES;
					String dir = getContextConfigPath(servletContext)+CONST_XML_MESSAGES_FOLDER+"/";
					try{
						mess_config.initProperties(path);
						mess_config.initWithFOLDER(dir);
					}catch(bsControllerException je){}
				}
			}
		}
	}

	public static void reloadAuth_config(ServletContext servletContext) {
		i_ProviderWrapper wrapperConfigAuth = checkLoadAuthentication(null);
		if(wrapperConfigAuth!=null && wrapperConfigAuth.getInstance()!=null){
			if(reInit){
				String path = getAppInit().get_path_config()+CONST_XML_AUTHENTIFICATIONS;
				String dir = getAppInit().get_path_config()+CONST_XML_AUTHENTIFICATIONS_FOLDER+"/";
				try{
					((load_authentication)wrapperConfigAuth.getInstance()).reimposta();
					((load_authentication)wrapperConfigAuth.getInstance()).initProperties(path);
					((load_authentication)wrapperConfigAuth.getInstance()).initWithFOLDER(dir);
				}catch(bsControllerException je){}
			}
			if(!((load_authentication)wrapperConfigAuth.getInstance()).isReadOk()){
				if(!((load_authentication)wrapperConfigAuth.getInstance()).load_from_resources(servletContext)){
					String path = getContextConfigPath(servletContext)+CONST_XML_AUTHENTIFICATIONS;
					String dir = getContextConfigPath(servletContext)+CONST_XML_AUTHENTIFICATIONS_FOLDER+"/";
					try{
						((load_authentication)wrapperConfigAuth.getInstance()).initProperties(path);
						((load_authentication)wrapperConfigAuth.getInstance()).initWithFOLDER(dir);
					}catch(bsControllerException je){}
				}
			}

		}else{
			if(reInit){
				String path = getAppInit().get_path_config()+CONST_XML_AUTHENTIFICATIONS;
				String dir = getAppInit().get_path_config()+CONST_XML_AUTHENTIFICATIONS_FOLDER+"/";
				auth_config = new load_authentication();
				try{
					auth_config.initProperties(path);
					auth_config.initWithFOLDER(dir);
				}catch(bsControllerException je){}
			}
			if(auth_config!=null && !auth_config.isReadOk()){
				if(!auth_config.load_from_resources(servletContext)){
					String path = getContextConfigPath(servletContext)+CONST_XML_AUTHENTIFICATIONS;
					String dir = getContextConfigPath(servletContext)+CONST_XML_AUTHENTIFICATIONS_FOLDER+"/";
					try{
						auth_config.initProperties(path);
						auth_config.initWithFOLDER(dir);
					}catch(bsControllerException je){}
				}
			}
		}
	}

	public static void reloadOrg_config(ServletContext servletContext) {
		i_ProviderWrapper wrapperConfigOrganization = checkLoadOrganization(null);
		if(wrapperConfigOrganization!=null && wrapperConfigOrganization.getInstance()!=null){
			if(reInit){
				String path = getAppInit().get_path_config()+CONST_XML_ORGANIZATION;
				((load_organization)wrapperConfigOrganization.getInstance()).reimposta();
				try{
					((load_organization)wrapperConfigOrganization.getInstance()).initProperties(path);
				}catch(bsControllerException je){}
			}
			if(!((load_organization)wrapperConfigOrganization.getInstance()).isReadOk()){
				if(!((load_organization)wrapperConfigOrganization.getInstance()).load_from_resources(servletContext)){
					String path = getContextConfigPath(servletContext)+CONST_XML_ORGANIZATION;
					try{
						((load_organization)wrapperConfigOrganization.getInstance()).initProperties(path);
					}catch(bsControllerException je){}
				}
			}
		}else{
			if(reInit){
				String path = getAppInit().get_path_config()+CONST_XML_ORGANIZATION;
				org_config = new load_organization();
				try{
					org_config.initProperties(path);
				}catch(bsControllerException je){}
			}
			if(org_config!=null && !org_config.isReadOk()){
				if(!org_config.load_from_resources(servletContext)){
					String path = getContextConfigPath(servletContext)+CONST_XML_ORGANIZATION;
					try{
						org_config.initProperties(path);
					}catch(bsControllerException je){}
				}
			}
		}
	}


	public static void reloadMenu_config(ServletContext servletContext, i_menu_element ime) {
		i_ProviderWrapper wrapperConfigMenu = checkLoadMenu(null);
		if(wrapperConfigMenu!=null && wrapperConfigMenu.getInstance()!=null){
			

			if(reInit){
				wrapperConfigMenu.setInstance(new load_menu(ime));
				try{
					((load_menu)wrapperConfigMenu.getInstance()).init();
				}catch(bsControllerException je){}
				if(!((load_menu)wrapperConfigMenu.getInstance()).isReadOk()) 
					((load_menu)wrapperConfigMenu.getInstance()).load_from_resources();
	
				if(!((load_menu)wrapperConfigMenu.getInstance()).isReadOk()){
					String path = getAppInit().get_path_config()+CONST_XML_MENU;
					try{
						((load_menu)wrapperConfigMenu.getInstance()).initProperties(path);
					}catch(bsControllerException je){}
				}
			}
			if(!((load_menu)wrapperConfigMenu.getInstance()).isReadOk()){
				if(!((load_menu)wrapperConfigMenu.getInstance()).load_from_resources(servletContext)){
					wrapperConfigMenu.setInstance(new load_menu(ime));
					try{
						((load_menu)wrapperConfigMenu.getInstance()).init();
					}catch(bsControllerException je){}
					if(!((load_menu)wrapperConfigMenu.getInstance()).isReadOk())
						((load_menu)wrapperConfigMenu.getInstance()).load_from_resources();
					if(!((load_menu)wrapperConfigMenu.getInstance()).isReadOk()){
						String path = getContextConfigPath(servletContext)+CONST_XML_MENU;
						try{
							((load_menu)wrapperConfigMenu.getInstance()).initProperties(path);
						}catch(bsControllerException je){}
					}
				}
			}
			
			
		}else{
			if(reInit){
	
				menu_config = new load_menu(ime);
				try{
					menu_config.init();
				}catch(bsControllerException je){}
				if(!menu_config.isReadOk()) menu_config.load_from_resources();
	
				if(!menu_config.isReadOk()){
					String path = getAppInit().get_path_config()+CONST_XML_MENU;
					try{
						menu_config.initProperties(path);
					}catch(bsControllerException je){}
				}
			}
			if(menu_config!=null && !menu_config.isReadOk()){
				if(!menu_config.load_from_resources(servletContext)){
					menu_config = new load_menu(ime);
					try{
						menu_config.init();
					}catch(bsControllerException je){}
					if(!menu_config.isReadOk()) menu_config.load_from_resources();
					if(!menu_config.isReadOk()){
						String path = getContextConfigPath(servletContext)+CONST_XML_MENU;
						try{
							menu_config.initProperties(path);
						}catch(bsControllerException je){}
					}
				}
			}
		}
	}
	public static void reloadMenu_config( HttpServletRequest request, i_menu_element ime) {
		reloadMenu_config(request.getSession().getServletContext(), ime);
	}


	public static void reloadLog_generator(ServletContext servletContext){
		if(logInit.get_LogPath()==null || logInit.get_LogPath().equals("")){
			logInit.init();
			String new_path =util_classes.getPathWebContent(servletContext)+"/"+CONST_LOG_FOLDER+"/";
			logInit.set_LogPath(new_path);
			
			i_log_generator logGenerator = checkLogGenerator(logInit);
			if(logGenerator.isReadError()) logGenerator.setReadError(false);
			else bsController.writeLog("Load_log OK ",iStub.log_INFO);
/*			
			logG = new log_generator(logInit);
			if(logG.isReadError()) logG.setReadError(false);
			else bsController.writeLog("Load_log OK ",iStub.log_INFO);
*/			
		}
	}




	public static auth_init checkAuth_init(HttpServletRequest request) {
		auth_init auth = null;
		if(request.getSession().getAttribute(CONST_BEAN_$AUTHENTIFICATION)==null){
//			auth = new auth_init();

			try{
				auth = (auth_init)util_provider.getInstanceFromProvider(
							new String[]{
									getAppInit().get_context_provider(),
									getAppInit().get_cdi_provider(),
									getAppInit().get_ejb_provider()
							},
							auth_init.class.getName());
				auth.init(request);
			}catch(bsException je){
			}catch(Exception e){
			}
			request.getSession().setAttribute(CONST_BEAN_$AUTHENTIFICATION,auth);
		}
		auth = (auth_init)request.getSession().getAttribute(CONST_BEAN_$AUTHENTIFICATION);
		return auth;
	}

	public static Connection getDBConnection() throws Exception{
		db_connection DBConnection = new db_connection();
		return DBConnection.getContent(getDbInit());
	}

	public static Connection getDBConnection(String id) throws Exception{
		db_connection DBConnection = new db_connection();
		if(getDbInit().get_another_db_init().get(id)!=null)
			return DBConnection.getContent((db_init)getDbInit().get_another_db_init().get(id));
		else{
			throw new Exception("Connection id="+id+" wasn't load correctly or isn't present into the list of connections.");
		}
	}


	public static String getPropertyMultipart(String key, HttpServletRequest req){

		try{
			String file = (String)req.getAttribute("multipart/form-data");
			DataInputStream in = null;
			String contentType = req.getContentType();
			if (contentType != null && contentType.indexOf("multipart/form-data") != -1) {
				if(file==null){
					in = new DataInputStream(req.getInputStream());
					int formDataLength = req.getContentLength();
					byte dataBytes[] = new byte[formDataLength];
					int bytesRead = 0;
					int totalBytesRead = 0;
					while (totalBytesRead < formDataLength) {
						bytesRead = in.read(dataBytes, totalBytesRead, formDataLength);
						totalBytesRead += bytesRead;
					}
					file = new String(dataBytes,0,dataBytes.length,"ASCII");
					in.close();
					req.setAttribute("multipart/form-data",file);
				}

				String check = "Content-Disposition: form-data; name=\""+key+"\"";

				int pos = file.indexOf(check);

				if(pos>-1){
					int pos1 = file.indexOf("-----------------------------",pos);
					if(pos1>-1){
						String result = file.substring(pos+check.length(),pos1);
						result = result.replace('\n',' ').replace('\r',' ');
						return result.trim();
					}
				}
			}

		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);

			return null;
		}
		return null;
	}

    public static String encrypt(String plaintext) throws NoSuchAlgorithmException, UnsupportedEncodingException{
    	MessageDigest md = null;
        md = MessageDigest.getInstance("SHA");
        md.update(plaintext.getBytes("UTF-8"));
        byte raw[] = md.digest();
		BASE64Encoder encoder = new BASE64Encoder();
        String hash = encoder.encode(raw);
		encoder = null;
        return hash;
    }

// algorithm: SHA, SHA-224, SHA-256, SHA-384, and SHA-512
    public static String encrypt(String plaintext, String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException{
    	MessageDigest md = null;
        md = MessageDigest.getInstance(algorithm);
        md.update(plaintext.getBytes("UTF-8"));
        byte raw[] = md.digest();
		BASE64Encoder encoder = new BASE64Encoder();
        String hash = encoder.encode(raw);
		encoder = null;
        return hash;
    }

 // algorithm: SHA, SHA-224, SHA-256, SHA-384, and SHA-512
    public static String encrypt(String plaintext, String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException{
    	MessageDigest md = null;
        md = MessageDigest.getInstance(algorithm,provider);
        md.update(plaintext.getBytes("UTF-8"));
        byte raw[] = md.digest();
		BASE64Encoder encoder = new BASE64Encoder();
        String hash = encoder.encode(raw);
		encoder = null;
        return hash;
    }

//*********************************************************
	static public int calc(Object oldObj){

		ObjectOutputStream oos = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(oldObj);
			oos.flush();
			return bos.size();

//			ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
//			ois = new ObjectInputStream(bin);

//			retVal = ois.readObject();

		} catch (Exception e) {
			e.toString();
		} finally {
			try {
				oos.close();
//				ois.close();
			} catch (java.io.IOException e) {
//				throw (e);
			}
		}
		return 0;
	}

	public static I_StatisticProvider checkStatisticProvider(){
		if(!canBeProxed){
			if(statisticProvider==null)
				statisticProvider = new StatisticProvider_Simple();
			return statisticProvider;
		}
		
		if(statisticProvider!=null)
			return statisticProvider;
		
		I_StatisticProvider retStatisticProvider=null;
		
		if(appInit.get_statistic()!=null && appInit.get_statistic().equalsIgnoreCase("true")){
			if(getAppInit().get_statistic_provider()==null || getAppInit().get_statistic_provider().equals("")){
				if(getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equals("false")){
					try{
						retStatisticProvider = (I_StatisticProvider)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  app_init.id_statistic_provider,StatisticProvider_Simple.class.getName(), null);
					}catch(Exception e){
					}
				}
				if(retStatisticProvider==null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equals("false")){
					try{
						retStatisticProvider = (I_StatisticProvider)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  app_init.id_statistic_provider,StatisticProvider_Simple.class.getName(), null);
					}catch(Exception e){
					}
				}
				if(retStatisticProvider==null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equals("false")){
					try{
						retStatisticProvider = (I_StatisticProvider)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  app_init.id_statistic_provider,I_StatisticProvider.class.getName(), null);
					}catch(Exception e){
					}
				}
				checkDefaultProvider(null);
				if(retStatisticProvider==null && getCdiDefaultProvider()!=null){
					try{
						retStatisticProvider = (I_StatisticProvider)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  app_init.id_statistic_provider,StatisticProvider_Simple.class.getName(), null);
					}catch(Exception e){
					}
				}
				if(retStatisticProvider==null && getEjbDefaultProvider()!=null){
					try{
						retStatisticProvider = (I_StatisticProvider)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  app_init.id_statistic_provider,I_StatisticProvider.class.getName(), null);
					}catch(Exception e){
					}
				}

				if(retStatisticProvider==null){
					statisticProvider = new StatisticProvider_Simple();
					return statisticProvider;
				}
			}else{
				if(getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equals("false")){
					try{
						retStatisticProvider = (I_StatisticProvider)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  app_init.id_statistic_provider,getAppInit().get_statistic_provider(), null);
					}catch(Exception e){
					}
				}
				if(retStatisticProvider==null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equals("false")){
					try{
						retStatisticProvider = (I_StatisticProvider)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  app_init.id_statistic_provider,getAppInit().get_statistic_provider(), null);
					}catch(Exception e){
					}
				}
				if(retStatisticProvider==null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equals("false")){
					try{
						retStatisticProvider = (I_StatisticProvider)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  app_init.id_statistic_provider,getAppInit().get_statistic_provider(), null);
					}catch(Exception e){
					}
				}
				checkDefaultProvider(null);
				if(retStatisticProvider==null && getCdiDefaultProvider()!=null){
					try{
						retStatisticProvider = (I_StatisticProvider)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(), app_init.id_statistic_provider,getAppInit().get_statistic_provider(), null);
					}catch(Exception e){
					}
				}
				if(retStatisticProvider==null && getEjbDefaultProvider()!=null){
					try{
						retStatisticProvider = (I_StatisticProvider)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(), app_init.id_statistic_provider,getAppInit().get_statistic_provider(), null);
					}catch(Exception e){
					}
				}

				if(retStatisticProvider==null){
					try{
						retStatisticProvider = (I_StatisticProvider)Class.forName(getAppInit().get_statistic_provider()).newInstance();
					}catch(Exception e){
						writeLog("ERROR instance Statistic Provider:"+getAppInit().get_statistic_provider()+" Will be use embeded stack.",iStub.log_ERROR);
					}
				}
				if(retStatisticProvider==null){
					statisticProvider = new StatisticProvider_Simple();
					return statisticProvider;
				}

			}
/*			
			if(statisticProvider==null){
				try{
					statisticProvider = (I_StatisticProvider)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(), StatisticProvider_Simple.class.getName(),StatisticProvider_Simple.class.getName(), null);
				}catch(Exception e){
				}
				if(statisticProvider==null)
					statisticProvider = new StatisticProvider_Simple();
			}
*/			
		}
		if(retStatisticProvider==null){
			statisticProvider = new StatisticProvider_Simple();
			return statisticProvider;
		}
		
		return retStatisticProvider;

	}

	public static void putToStatisticProvider(StatisticEntity stat){
		I_StatisticProvider sProvider = checkStatisticProvider();
		if(sProvider!=null)
			sProvider.addStatictic(stat);
/*
		if(statisticProvider==null)
			statisticProvider = checkStatisticProvider();
		if(statisticProvider!=null)
			statisticProvider.addStatictic(stat);

		if(appInit.get_statistic()!=null && appInit.get_statistic().equalsIgnoreCase("true")){
			I_StatisticProvider statProvider = (I_StatisticProvider) getFromLocalContainer(CONST_ID_STATISTIC_PROVIDER);
			if(statProvider==null){
				if(getAppInit().get_statistic_provider()==null || getAppInit().get_statistic_provider().equals(""))
					statProvider = new StatisticProvider_Simple();
				else{
					if(getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("")){
						try{
							statProvider = (I_StatisticProvider)util_reflect.providerObjectFactory(getAppInit().get_cdi_provider(),  app_init.id_statistic_provider,getAppInit().get_statistic_provider(), null);
						}catch(Exception e){
						}
					}
					if(statProvider==null){
						try{
							statProvider = (I_StatisticProvider)Class.forName(getAppInit().get_statistic_provider()).newInstance();
						}catch(Exception e){
							writeLog("ERROR instance Statistic Provider:"+getAppInit().get_statistic_provider()+" Will be use embeded stack.",iStub.log_ERROR);
						}
					}
				}
				if(statProvider==null) statProvider = new StatisticProvider_Simple();
				putToLocalContainer(CONST_ID_STATISTIC_PROVIDER,statProvider);
			}
			statProvider.addStatictic(stat);
		}
*/
	}

	public static I_StatisticProvider getStatisticProvider(){
		
		return checkStatisticProvider();
/*		
		if(statisticProvider==null)
			statisticProvider = checkStatisticProvider();
		return statisticProvider;
*/
/*
		I_StatisticProvider statProvider = null;
		if(getAppInit().get_statistic()!=null && getAppInit().get_statistic().equalsIgnoreCase("true"))
			statProvider = (I_StatisticProvider) getFromLocalContainer(CONST_ID_STATISTIC_PROVIDER);
		if(statProvider==null){
			statProvider = checkStatisticProvider();
			putToLocalContainer(CONST_ID_STATISTIC_PROVIDER,statProvider);
		}
		return statProvider;
*/
	}


	private static void environmentState(HttpServletRequest request, String id_action){
		if(System.getProperty("application.environment.debug")!=null && System.getProperty("application.environment.debug").equalsIgnoreCase("true")){
			String log = "ClassHidra: Application Environment Memory Damp for ["+id_action+"] (only serialization)";
			String log_detail = "\n";
			int total=0;
			int only_session=0;
			int only_app=0;
			try{
				log_detail+="     ServletContext: \n";
				Enumeration en = request.getSession().getServletContext().getAttributeNames();
				while(en.hasMoreElements()){
					String key = (String)en.nextElement();
					int dim=calc(request.getSession().getServletContext().getAttribute(key));
					log_detail+="          ["+key+"]="+dim+"\n";
					only_app+=dim;
				}
				total+=only_app;

				log_detail+="     ClassHidra Local Container: \n";
				en = local_container.keys();
				while(en.hasMoreElements()){
					String key = (String)en.nextElement();
					int dim=calc(local_container.get(key));
					log_detail+="          ["+key+"]="+dim+"\n";
					only_app+=dim;
				}
				total+=only_app;


				log_detail+="     Session: \n";
				en = request.getSession().getAttributeNames();
				while(en.hasMoreElements()){
					String key = (String)en.nextElement();
					int dim=calc(request.getSession().getAttribute(key));
					log_detail+="          ["+key+"]="+dim+"\n";
					only_session+=dim;
				}
				total+=only_session;



				log_detail+="     Request Attributes: \n";
				en = request.getAttributeNames();
				while(en.hasMoreElements()){
					String key = (String)en.nextElement();
					int dim=calc(request.getAttribute(key));
					log_detail+="          ["+key+"]="+dim+"\n";
					total+=dim;
				}

				log_detail+="     Request Parameters: \n";
				en = request.getParameterNames();
				while(en.hasMoreElements()){
					String key = (String)en.nextElement();
					int dim=calc(request.getParameter(key));
					log_detail+="          ["+key+"]="+dim+"\n";
					total+=dim;
				}
				try{
					log_detail+="     Heap (very approximately): \n";
					log_detail+="          [HeapSize]="+Runtime.getRuntime().totalMemory()+"\n";
					log_detail+="          [HeapMaxSize]="+Runtime.getRuntime().maxMemory()+"\n";
					log_detail+="          [HeapFreeSize]="+Runtime.getRuntime().maxMemory()+"\n";
				}catch(Exception ex){
				}catch(Throwable th){
				}
				writeLog(log + "all="+total+"; servletContext="+only_app+"; session="+only_session+";"+log_detail,iStub.log_INFO);
			}catch(Exception e){
			}
		}
	}

	public static Object getUser_config() {
		i_ProviderWrapper wrapperConfigUsers = checkLoadUsers(null);
		if(wrapperConfigUsers!=null && wrapperConfigUsers.getInstance()!=null){
			return wrapperConfigUsers.getInstance();
		}else
			return user_config;
	}

	public static void setUser_config(Object new_load_users) {
		i_ProviderWrapper wrapperConfigUsers = checkLoadUsers(null);
		if(wrapperConfigUsers!=null)
			wrapperConfigUsers.setInstance(new_load_users);
		else	
			user_config = new_load_users;
	}

	public static load_users checkUser_config4load_users(){
		i_ProviderWrapper wrapperConfigUsers = checkLoadUsers(null);
		if(wrapperConfigUsers!=null && wrapperConfigUsers.getInstance()!=null){
			try{
				((load_users)wrapperConfigUsers.getInstance()).setReadError(false);
				((load_users)wrapperConfigUsers.getInstance()).init();
				if(((load_users)wrapperConfigUsers.getInstance()).isReadError()) ((load_users)wrapperConfigUsers.getInstance()).load_from_resources();
				if(((load_users)wrapperConfigUsers.getInstance()).isReadError()) ((load_users)wrapperConfigUsers.getInstance()).init(getAppInit().get_path_config()+CONST_XML_USERS);
				if(((load_users)wrapperConfigUsers.getInstance()).isReadError()){
					((load_users)wrapperConfigUsers.getInstance()).setReadError(false);
					((load_users)wrapperConfigUsers.getInstance()).load_from_resources();
					if(((load_users)wrapperConfigUsers.getInstance()).isReadError()) 
						wrapperConfigUsers.setInstance(null);
				}
			}catch(bsControllerException je){
				wrapperConfigUsers.setInstance(null);
			}
			return (load_users)wrapperConfigUsers.getInstance();
		}else{
			if(user_config==null){
				setUser_config(new load_users());
					try{
						((load_users)user_config).setReadError(false);
						((load_users)user_config).init();
						if(((load_users)user_config).isReadError()) ((load_users)user_config).load_from_resources();
						if(((load_users)user_config).isReadError()) ((load_users)user_config).init(getAppInit().get_path_config()+CONST_XML_USERS);
						if(((load_users)user_config).isReadError()){
							((load_users)user_config).setReadError(false);
							((load_users)user_config).load_from_resources();
							if(((load_users)user_config).isReadError()) user_config = null;
						}
					}catch(bsControllerException je){
						user_config = null;
					}
			}
			return (load_users)user_config;
		}
	}

	public static void setCache(HttpServletResponse response, String cacheInSec){
		if(cacheInSec==null) return;
		try{
			final int CACHE_DURATION_IN_SECOND = Integer.valueOf(cacheInSec).intValue();
			long now = System.currentTimeMillis();
			((HttpServletResponse)response).addHeader("Cache-Control", "max-age=" + CACHE_DURATION_IN_SECOND);
			((HttpServletResponse)response).addHeader("Cache-Control", "must-revalidate");//optional
			((HttpServletResponse)response).setDateHeader("Last-Modified", now);
			((HttpServletResponse)response).setDateHeader("Expires", now + CACHE_DURATION_IN_SECOND  * 1000);
		}catch(Exception e){
		}

	}


	public static String getIdApp() {
		return idApp;
	}

	private static String getContextPathFor5(ServletContext servletContext) {
		// Get the context path without the request.
		String contextPath = "";
		try {
			String path = servletContext.getResource("/").getPath();
			contextPath = path.substring(0, path.lastIndexOf("/"));
			contextPath = contextPath.substring(contextPath.lastIndexOf("/"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contextPath;
	}

	public static String getInfoVersion(String prop) {
		try{
			return new version_init(prop).getInfo();
		}catch(Exception e){
		}catch(Throwable t){
		}
		return "???";
	}

	public static i_ProviderWrapper checkLoadUsers(ServletContext servletContext){
		i_ProviderWrapper wrapper = null;
		if(!canBeProxed)
			return wrapper;
		
		String bean_id = bsConstants.CONST_BEAN_$USERS_CONFIG;

			if(getAppInit()!=null && getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  bean_id, load_users.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  bean_id, load_users.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  bean_id, load_users.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			checkDefaultProvider(servletContext);
			if(wrapper==null && getCdiDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  bean_id, load_users.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getEjbDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  bean_id.replace("$", "ejb_"), load_users.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
		return wrapper;	

	}	
	
	public static i_ProviderWrapper checkLoadActions(ServletContext servletContext){
		i_ProviderWrapper wrapper = null;
		if(!canBeProxed)
			return wrapper;
		
		String bean_id = bsConstants.CONST_BEAN_$ACTION_CONFIG;

			if(getAppInit()!=null && getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  bean_id, load_actions.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  bean_id, load_actions.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  bean_id, load_actions.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			checkDefaultProvider(servletContext);
			if(wrapper==null && getCdiDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  bean_id, load_actions.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getEjbDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  bean_id.replace("$", "ejb_"), load_actions.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
		return wrapper;	
	}
	
	public static i_ProviderWrapper checkLoadMessage(ServletContext servletContext){
		i_ProviderWrapper wrapper = null;
		if(!canBeProxed)
			return wrapper;
		
		String bean_id = bsConstants.CONST_BEAN_$MESS_CONFIG;

			if(getAppInit()!=null && getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  bean_id, load_message.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  bean_id, load_message.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  bean_id, load_message.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			checkDefaultProvider(servletContext);
			if(wrapper==null && getCdiDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  bean_id, load_message.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getEjbDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  bean_id.replace("$", "ejb_"), load_message.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
		return wrapper;
	}
	
	public static i_ProviderWrapper checkLoadAuthentication(ServletContext servletContext){
		i_ProviderWrapper wrapper = null;
		if(!canBeProxed)
			return wrapper;
		
		String bean_id = bsConstants.CONST_BEAN_$AUTH_CONFIG;

			if(getAppInit()!=null && getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  bean_id, load_authentication.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  bean_id, load_authentication.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  bean_id, load_authentication.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			checkDefaultProvider(servletContext);
			if(wrapper==null && getCdiDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  bean_id, load_authentication.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getEjbDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  bean_id.replace("$", "ejb_"), load_authentication.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
		return wrapper;	
		
	}	
	
	public static i_ProviderWrapper checkLoadOrganization(ServletContext servletContext){
		i_ProviderWrapper wrapper = null;
		if(!canBeProxed)
			return wrapper;
		
		String bean_id = bsConstants.CONST_BEAN_$ORGANIZATION_CONFIG;

			if(getAppInit()!=null && getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  bean_id, load_organization.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  bean_id, load_organization.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  bean_id, load_organization.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			checkDefaultProvider(servletContext);
			if(wrapper==null && getCdiDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  bean_id, load_organization.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getEjbDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  bean_id.replace("$", "ejb_"), load_organization.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
		return wrapper;	
		
	}	

	public static i_ProviderWrapper checkLoadMenu(ServletContext servletContext){
		i_ProviderWrapper wrapper = null;
		if(!canBeProxed)
			return wrapper;
		
		String bean_id = bsConstants.CONST_BEAN_$MENU_CONFIG;

			if(getAppInit()!=null && getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  bean_id, load_menu.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  bean_id, load_menu.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  bean_id, load_menu.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			checkDefaultProvider(servletContext);
			if(wrapper==null && getCdiDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  bean_id, load_menu.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getEjbDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  bean_id.replace("$", "ejb_"), load_menu.class.getName(), (servletContext==null)?null:servletContext);
				}catch(Exception e){
				}
			}
		return wrapper;	
		
	}		

	public static Map checkOnlySession(HttpServletRequest request){
		i_ProviderWrapper wrapper = null;
		if(!canBeProxed)
			return null;
		
		String bean_id = bsConstants.CONST_BEAN_$ONLYINSSESSION;
		if(!getAction_config().getInstance_onlysession().equals("") && !getAction_config().getInstance_onlysession().equalsIgnoreCase("false"))
			bean_id = getAction_config().getInstance_onlysession();

			if(getAction_config()!=null && getAction_config().getProvider()!=null && !getAction_config().getProvider().equals("") && !getAction_config().getProvider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAction_config().getProvider(),  bean_id, getAction_config().getInstance_onlysession(), (request==null)?null:request.getSession().getServletContext());
				}catch(Exception e){
				}
			} 
			if(wrapper==null && getAppInit()!=null && getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  bean_id, getAction_config().getInstance_onlysession(), (request==null)?null:request.getSession().getServletContext());
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  bean_id, getAction_config().getInstance_onlysession(), (request==null)?null:request.getSession().getServletContext());
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  bean_id, getAction_config().getInstance_onlysession(), (request==null)?null:request.getSession().getServletContext());
				}catch(Exception e){
				}
			}
			checkDefaultProvider((request==null)?null:request.getSession().getServletContext());
			if(wrapper==null && getCdiDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  bean_id, getAction_config().getInstance_onlysession(), (request==null)?null:request.getSession().getServletContext());
				}catch(Exception e){
				}
			}
			if(wrapper==null && getEjbDefaultProvider()!=null){
				try{
				
					wrapper = (i_ProviderWrapper)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION+"$wrapper");
					if(wrapper!=null){
						try{
							wrapper.getInstance();
						}catch(Exception e){
							new bsControllerException(e, iStub.log_ERROR);
							wrapper=null;
							request.getSession().removeAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION+"$wrapper");
						}
					}
					if(wrapper==null){
						wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  bean_id.replace("$", "ejb_"), getAction_config().getInstance_onlysession(), (request==null)?null:request.getSession().getServletContext());
						if(wrapper!=null && request!=null && wrapper.getInfo_context()!=null && wrapper.getInfo_context().isStateful())
							request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION+"$wrapper", wrapper);
					}
				}catch(Exception e){
				}
			}
		
			if(wrapper!=null)
				return (Map)wrapper.getInstance();

		return null;
	}

	public static Object getFromOnlySession(String id,HttpServletRequest request){
		Map instance = checkOnlySession(request);

		if(instance==null)
			instance = (Map)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
		if(instance==null){
			instance = new HashMap();
			request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,instance);
		}
		return instance.get(id);
	}

	public static boolean setToOnlySession(String id,i_bean obj,HttpServletRequest request){
		try{
			Map instance = checkOnlySession(request);

			if(instance==null)
				instance = (Map)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
			if(instance==null){
				instance = new HashMap();
				request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,instance);
			}
			if(obj!=null){
//				info_context info = checkBeanContext(obj.asBean());
				if(obj.asBean().getInfo_context().isOnlyProxied()){
					if(instance.get(id)==null)
						instance.put(id, obj);
					return true;
				}
			}
			instance.put(id, obj);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public static Object removeFromOnlySession(String id,HttpServletRequest request){
			Map instance = checkOnlySession(request);
			if(instance!=null)
				util_provider.destroyInstanceFromProvider(instance.get(id), null);

			if(instance==null)
				instance = (Map)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
			if(instance==null){
				instance = new HashMap();
				request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,instance);
			}
			return instance.remove(id);
	}

	public static Map getOnlySession(HttpServletRequest request){
		Map instance = checkOnlySession(request);

		if(instance==null){
			try{
				instance = (Map)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
			}catch(Exception e){
			}
		}if(instance==null){
			instance = new HashMap();
			request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,instance);
		}
		return instance;
	}

	public static boolean setOnlySession(Map newInstance, HttpServletRequest request){

		if(newInstance == null)
			return false;

		Map instance = checkOnlySession(request);

		if(instance==null)
			instance = (Map)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
		if(instance==null){
			request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,newInstance);
		}else{
			instance.clear();
			instance.putAll(newInstance);
		}
		return true;
	}


	public static void clearOnlySession(HttpServletRequest request){
		Map instance = checkOnlySession(request);
		if(instance!=null){
			Iterator it = instance.values().iterator();
			while(it.hasNext())
				util_provider.destroyInstanceFromProvider(it.next(), null);
		}
		if(instance==null)
			instance = (Map)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
		if(instance==null){
			instance = new HashMap();
			request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,instance);
		}
		instance.clear();

	}


	public static i_ProviderWrapper checkInfoNavigationWrapper(HttpServletRequest request){
		i_ProviderWrapper wrapper = null;
		if(!canBeProxed)
			return wrapper;
		
		String bean_id = bsConstants.CONST_BEAN_$NAVIGATION;
		if(!getAction_config().getInstance_navigated().equals("") && !getAction_config().getInstance_navigated().equalsIgnoreCase("false"))
			bean_id = getAction_config().getInstance_navigated();

			if(getAction_config()!=null && getAction_config().getProvider()!=null && !getAction_config().getProvider().equals("") && !getAction_config().getProvider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAction_config().getProvider(),  bean_id, getAction_config().getInstance_navigated(), (request==null)?null:request.getSession().getServletContext());
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  bean_id, getAction_config().getInstance_navigated(), (request==null)?null:request.getSession().getServletContext());
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  bean_id, getAction_config().getInstance_navigated(), (request==null)?null:request.getSession().getServletContext());
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  bean_id, getAction_config().getInstance_navigated(), (request==null)?null:request.getSession().getServletContext());
				}catch(Exception e){
				}
			}
			checkDefaultProvider((request==null)?null:request.getSession().getServletContext());
			if(wrapper==null && getCdiDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  bean_id, getAction_config().getInstance_navigated(), (request==null)?null:request.getSession().getServletContext());
				}catch(Exception e){
				}
			}
			if(wrapper==null && getEjbDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)request.getSession().getAttribute(bsConstants.CONST_BEAN_$NAVIGATION+"$wrapper");
					if(wrapper!=null){
						try{
							wrapper.getInstance();
						}catch(Exception e){
							new bsControllerException(e, iStub.log_ERROR);
							wrapper=null;
							request.getSession().removeAttribute(bsConstants.CONST_BEAN_$NAVIGATION+"$wrapper");
						}
					}
					if(wrapper==null){
						wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  bean_id.replace("$", "ejb_"), getAction_config().getInstance_navigated(), (request==null)?null:request.getSession().getServletContext());
						if(wrapper!=null && request!=null && wrapper.getInfo_context()!=null && wrapper.getInfo_context().isStateful())
							request.getSession().setAttribute(bsConstants.CONST_BEAN_$NAVIGATION+"$wrapper", wrapper);
					}
				}catch(Exception e){
				}
			}
			
		return wrapper;
	}

	public static info_navigation getFromInfoNavigation(String id,HttpServletRequest request){
		info_navigation nav = null;
		i_ProviderWrapper inw = checkInfoNavigationWrapper(request);
		if(inw!=null)
			nav = (info_navigation)inw.getInstance();

		if(nav==null){
			try{
				nav = (info_navigation)request.getSession().getAttribute(bsConstants.CONST_BEAN_$NAVIGATION);
			}catch(Exception e){
			}
		}
		if(nav==null)
			return null;
		if(id==null)
			return nav;
		return nav.find(id);
	}

	public static boolean setToInfoNavigation(info_navigation nav,HttpServletRequest request){
		try{

			i_ProviderWrapper inw = checkInfoNavigationWrapper(request);

			if(inw!=null){
				inw.setInstance(nav);
				return true;
			}

			request.getSession().setAttribute(bsConstants.CONST_BEAN_$NAVIGATION, nav);
			return true;
		}catch(Exception e){
			return false;
		}
	}



	
	public static Map checkLocalContainer(){
		if(!canBeProxed)
			return local_container;
		
		i_ProviderWrapper wrapperConfigAction = checkLoadActions(null);
		load_actions current_action_config = null;
		if(wrapperConfigAction!=null && wrapperConfigAction.getInstance()!=null)
			current_action_config = (load_actions)wrapperConfigAction.getInstance();
		else
			current_action_config = action_config;
		if(current_action_config==null || !current_action_config.isReadOk()){
			return local_container;
		}else{
			i_ProviderWrapper wrapper = null;
			String bean_id = bsConstants.CONST_BEAN_$LOCAL_CONTAINER;
			if(!getAction_config().getInstance_local_container().equals("") && !getAction_config().getInstance_local_container().equalsIgnoreCase("false"))
				bean_id = getAction_config().getInstance_local_container();

				if(getAction_config()!=null && getAction_config().getProvider()!=null && !getAction_config().getProvider().equals("") && !getAction_config().getProvider().equalsIgnoreCase("false")){
					try{
						wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAction_config().getProvider(), bean_id, getAction_config().getInstance_local_container(), null);
						if(wrapper!=null && local_container!=null && local_container.size()>0){
							wrapper.setInstance(local_container);
							local_container.clear();
							local_container=null;
						}
						return (Map)wrapper.getInstance();
					}catch(Exception e){
					}
				} 
				if(wrapper==null && getAppInit()!=null && getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equalsIgnoreCase("false")){
					try{
						wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  bean_id, getAction_config().getInstance_local_container(), null);
						if(wrapper!=null && local_container!=null && local_container.size()>0){
							wrapper.setInstance(local_container);
							local_container.clear();
							local_container=null;
						}
						return (Map)wrapper.getInstance();
					}catch(Exception e){
					}
				}
				if(wrapper==null && getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equalsIgnoreCase("false")){
					try{
						wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  bean_id, getAction_config().getInstance_local_container(), null);
						if(wrapper!=null && local_container!=null && local_container.size()>0){
							wrapper.setInstance(local_container);
							local_container.clear();
							local_container=null;
						}
						return (Map)wrapper.getInstance();
					}catch(Exception e){
					}
				}
				if(wrapper==null && getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equalsIgnoreCase("false")){
					try{
						wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  bean_id, getAction_config().getInstance_local_container(), null);
						if(wrapper!=null && local_container!=null && local_container.size()>0){
							wrapper.setInstance(local_container);
							local_container.clear();
							local_container=null;
						}
						return (Map)wrapper.getInstance();
					}catch(Exception e){
					}
				}
				checkDefaultProvider(null);
				if(wrapper==null && getCdiDefaultProvider()!=null){
					try{
						wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  bean_id, getAction_config().getInstance_local_container(), null);
						if(wrapper!=null && local_container!=null && local_container.size()>0){
							wrapper.setInstance(local_container);
							local_container.clear();
							local_container=null;
						}
						if(wrapper!=null)
							return (Map)wrapper.getInstance();
					}catch(Exception e){
					}
				}

				if(wrapper==null && getEjbDefaultProvider()!=null){
					try{
						wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  bean_id.replace("$", "ejb_"), getAction_config().getInstance_local_container(), null);
						if(wrapper!=null && local_container!=null && local_container.size()>0){
							wrapper.setInstance(local_container);
							local_container.clear();
							local_container=null;
						}
						if(wrapper!=null)
							return (Map)wrapper.getInstance();
					}catch(Exception e){
					}
				}

//			}
		}
		return local_container;

	}
	
	public static IBatchScheduling checkSchedulerContainer(){
		if(!canBeProxed)
			return null;
		
		i_ProviderWrapper wrapperConfigAction = checkLoadActions(null);
		load_actions current_action_config = null;
		if(wrapperConfigAction!=null && wrapperConfigAction.getInstance()!=null)
			current_action_config = (load_actions)wrapperConfigAction.getInstance();
		else
			current_action_config = action_config;
		if(current_action_config==null || !current_action_config.isReadOk()){
			return null;
		}else{
			i_ProviderWrapper wrapper = null;
			String bean_id = bsConstants.CONST_BEAN_$SCHEDULER_CONTAINER;
			if(!getAction_config().getInstance_scheduler_container().equals("") && !getAction_config().getInstance_scheduler_container().equalsIgnoreCase("false"))
				bean_id = getAction_config().getInstance_scheduler_container();

				if(getAction_config()!=null && getAction_config().getProvider()!=null && !getAction_config().getProvider().equals("") && !getAction_config().getProvider().equalsIgnoreCase("false")){
					try{
						wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAction_config().getProvider(), bean_id, getAction_config().getInstance_scheduler_container(), null);
						if(wrapper!=null)
							return (IBatchScheduling)wrapper.getInstance();
					}catch(Exception e){
					}
				} 
				if(wrapper==null && getAppInit()!=null && getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equalsIgnoreCase("false")){
					try{
						wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  bean_id, getAction_config().getInstance_scheduler_container(), null);
						if(wrapper!=null)
							return (IBatchScheduling)wrapper.getInstance();
					}catch(Exception e){
					}
				}
				if(wrapper==null && getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equalsIgnoreCase("false")){
					try{
						wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  bean_id, getAction_config().getInstance_scheduler_container(), null);
						if(wrapper!=null)
							return (IBatchScheduling)wrapper.getInstance();
					}catch(Exception e){
					}
				}
				if(wrapper==null && getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equalsIgnoreCase("false")){
					try{
						wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  bean_id, getAction_config().getInstance_scheduler_container(), null);
						if(wrapper!=null)
							return (IBatchScheduling)wrapper.getInstance();
					}catch(Exception e){
					}
				}
				checkDefaultProvider(null);
				if(wrapper==null && getCdiDefaultProvider()!=null){
					try{
						wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  bean_id, getAction_config().getInstance_scheduler_container(), null);
						if(wrapper!=null)
							return (IBatchScheduling)wrapper.getInstance();
					}catch(Exception e){
					}
				}
				if(wrapper==null && getEjbDefaultProvider()!=null){
					try{
						wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  bean_id.replace("$", "ejb_"), getAction_config().getInstance_scheduler_container(), null);
						if(wrapper!=null)
							return (IBatchScheduling)wrapper.getInstance();
					}catch(Exception e){
					}
				}
				
				
//			}
		}
		return null;

	}	

	public static Object getFromLocalContainer(String key) {
		Map container = checkLocalContainer();
		if(container!=null)
			return container.get(key);
		if(local_container!=null)
			return local_container.get(key);
		return null;
	}

	public static Object removeFromLocalContainer(String key) {
		Map container = checkLocalContainer();
		if(container!=null){
			util_provider.destroyInstanceFromProvider(container.get(key), null);
			return container.remove(key);
		}
		if(local_container!=null)
			return local_container.remove(key);
		return null;
	}

	public static void setToLocalContainer(String key, Object value) {
		Map container = checkLocalContainer();
		if(container!=null){
			if(container instanceof ConcurrentHashMap)
				((ConcurrentHashMap)container).putIfAbsent(key,value);
			else
				container.put(key,value);
		}
		if(local_container!=null)
			local_container.putIfAbsent(key,value);
	}

	public static void putToLocalContainer(String key, Object value) {
		Map container = checkLocalContainer();
		if(container!=null)
			container.put(key,value);
		if(local_container!=null)
			local_container.put(key,value);
	}

	public static Map getLocalContainer(){
		Map container = checkLocalContainer();
		if(container!=null)
			return container;
		if(local_container!=null)
			return local_container;
		return null;
	}

	public static boolean setLocalContainer(Map newInstance){
		if(newInstance==null)
			return false;
		Map container = checkLocalContainer();
		if(container!=null){
			container.clear();
			container.putAll(newInstance);
			return true;
		}
		if(local_container!=null){
			local_container.clear();
			local_container.putAll(newInstance);
			return true;
		}
		return false;
	}

	public static boolean clearLocalContainer(){
		Map container = checkLocalContainer();
		if(container!=null){
			Iterator it = container.values().iterator();
			while(it.hasNext())
				util_provider.destroyInstanceFromProvider(it.next(), null);

			container.clear();
			return true;
		}
		if(local_container!=null){
			local_container.clear();
			return true;
		}
		return false;
	}
	
	public static boolean checkCanBeProxed(){
		checkDefaultProvider(null);
		if( (getAction_config()!=null && getAction_config().getProvider()!=null && !getAction_config().getProvider().equals("") && !getAction_config().getProvider().equalsIgnoreCase("false")) ||
			(getAppInit()!=null && getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equalsIgnoreCase("false")) ||
			(getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equalsIgnoreCase("false")) ||
			(getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equalsIgnoreCase("false")) ||
			 getCdiDefaultProvider()!=null ||
			 getEjbDefaultProvider()!=null)
			return true;
		
		return false;
	}

	
	public static i_stream getStreamFromContainer(String id_stream){
		HashMap container_streams_instance = (HashMap)getFromLocalContainer(bsConstants.CONST_CONTAINER_STREAMS_INSTANCE);
		if(container_streams_instance==null){
			container_streams_instance = new HashMap();
			bsController.putToLocalContainer(bsConstants.CONST_CONTAINER_STREAMS_INSTANCE, container_streams_instance);				
		}
		return (i_stream)container_streams_instance.get(id_stream);
	}
	
	public static void putStreamIntoContainer(String id_stream, i_stream rStream){
		if(id_stream==null)
			return;
		HashMap container_streams_instance = (HashMap)bsController.getFromLocalContainer(bsConstants.CONST_CONTAINER_STREAMS_INSTANCE);
		if(container_streams_instance==null){
			container_streams_instance = new HashMap();
			bsController.putToLocalContainer(bsConstants.CONST_CONTAINER_STREAMS_INSTANCE, container_streams_instance);				
		}
		container_streams_instance.put(id_stream, rStream);
	}
	


	public static void setReInit(boolean reInit) {
		bsController.reInit = reInit;
	}

	public static i_provider getCdiDefaultProvider() {
		return cdiDefaultProvider;
	}

	public static i_provider getEjbDefaultProvider() {
		return ejbDefaultProvider;
	}

	public static boolean isCanBeProxed() {
		return canBeProxed;
	}
}
