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
import it.classhidra.core.tool.tlinked.I_TLinkedProvider;
import it.classhidra.core.tool.tlinked.TLinkedProvider_Simple;
import it.classhidra.scheduler.scheduling.IBatchScheduling;
import it.classhidra.serialize.JsonWriter;
import it.classhidra.serialize.XmlWriter;
import it.classhidra.core.tool.util.util_beanMessageFactory;
import it.classhidra.core.tool.util.util_classes;
import it.classhidra.core.tool.util.util_cloner;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_makeValue;
import it.classhidra.core.tool.util.util_provider;
import it.classhidra.core.tool.util.util_reflect;
import it.classhidra.core.tool.util.util_supportbean;
import it.classhidra.core.tool.util.v2.Util_sort;

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
import javax.xml.bind.DatatypeConverter;



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
	private static I_TLinkedProvider tLinkedProvider;
	
	private static i_provider cdiDefaultProvider;
	private static i_provider ejbDefaultProvider;
	private static i_provider tagComponentRender;

	private static ConcurrentHashMap<String,Object> local_container = new ConcurrentHashMap<String,Object>();


	private static boolean reInit=false;
	private static boolean canBeProxed=true;
	private static boolean isInitDefProfider=false;
	private static boolean initialized = false;
	private static boolean loadedonstartup = false;


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
	    			threadDone=true;
	    			Thread.currentThread().interrupt();
	    		}
	        }
	     }
	}
	
	static class LoaderConfigThreadProcessS extends Thread {
	    private boolean threadDone = false;
	    private Map<String,Properties> othersProperties = null;
	    private ServletContext servletContext = null;
	    public LoaderConfigThreadProcessS(Map<String,Properties> othersProperties, ServletContext servletContext) {
	        super();
	        this.othersProperties = othersProperties;
	        this.servletContext = servletContext;
	        threadDone=false;
	    }
	    public void run() {
	        while (!threadDone) {
	    		try {
	    			loadOnInitS(othersProperties,servletContext);
	    			threadDone=true;
	    		} catch (Exception e) {
//	    			threadDone=true;
	    			Thread.currentThread().interrupt();
	    		}
	        }
	     }
	}

	public void init() throws ServletException, UnavailableException {
		initialized = true;
		loadedonstartup = true;
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
			
			
		final Properties initParameters = new Properties();
		final Enumeration<?> paramNames = getInitParameterNames();
		while (paramNames.hasMoreElements()){
			final String name = (String) paramNames.nextElement();
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
		if(getLogG().isReadError())
			reloadLog_generator(getServletContext());


		resourcesInit(getServletContext());


		if(!getAction_config().isReadOk()) 	
			reloadAction_config(getServletContext());
		if(!getMess_config().isReadOk()) 	
			reloadMess_config(getServletContext());
		if(!getAuth_config().isReadOk()) 	
			reloadAuth_config(getServletContext());
		if(!getOrg_config().isReadOk()) 	
			reloadOrg_config(getServletContext());
		if(!getMenu_config().isReadOk()) 	
			reloadMenu_config(getServletContext(),null);
		
		loadActionsOnStartup(getServletContext());
		
	}
	
	public static void loadOnInitS(Map<String,Properties> othersProperties){
		loadOnInitS(othersProperties, null);
	}
	
	public static void loadOnInitS(Map<String,Properties> othersProperties,ServletContext servletContext){
		logInit = new log_init();
		if(othersProperties!=null && othersProperties.get(log_init.id_property)!=null && othersProperties.get(log_init.id_property) instanceof Properties)
			logInit.init((Properties)othersProperties.get(log_init.id_property));
		else
			logInit.init();
		
		dbInit = new db_init();
		if(othersProperties!=null && othersProperties.get(db_init.id_property)!=null && othersProperties.get(db_init.id_property) instanceof Properties)
			dbInit.init((Properties)othersProperties.get(db_init.id_property),"");
		else
			dbInit.init();
			
		checkLogGenerator(logInit);	
		if(getLogG().isReadError()) 
			reloadLog_generator(null);

		resourcesInit(null);

		if(!getAction_config().isReadOk())
			reloadAction_config(null);
		if(!getMess_config().isReadOk())
			reloadMess_config(null);
		if(!getAuth_config().isReadOk())
			reloadAuth_config(null);
		if(!getOrg_config().isReadOk())
			reloadOrg_config(null);
		
		if(servletContext!=null)
			loadActionsOnStartup(servletContext);
	}
	
	public static void loadActionsOnStartup(ServletContext servletContext) {
		if(getAction_config().getV_info_actions()!=null && getAction_config().getV_info_actions().size()>0) {
			try {
				Vector<info_action> info_actions = Util_sort.sort(getAction_config().getV_info_actions(),"loadOnStartup");
				for(info_action info : info_actions) {
					if(info.getMemoryInServletContext()!=null && info.getMemoryInServletContext().equalsIgnoreCase("true") && info.getLoadOnStartup()>-1) {
						try {
							loadActionOnStartup(info,servletContext);
						}catch(Throwable e) {
							new bsException("Load action on startup ["+info.getPath()+"]: "+e.toString(), iStub.log_WARN);
						}
					}
				}
			}catch(Exception e) {
				new bsException("Load actions on startup: "+e.toString(), iStub.log_WARN);
			}
		}
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
					new bsControllerException(ex, iStub.log_WARN);
				}catch (Throwable  th) {
					new bsControllerException(th, iStub.log_WARN);
				}
			}
		}

		final i_ProviderWrapper wrapperConfigAction = checkLoadActions(null);
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
			if(action_config==null || !action_config.isReimposted())
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
		
		final i_ProviderWrapper wrapperConfigMess = checkLoadMessage(null);
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
			
		final i_ProviderWrapper wrapperConfigAuth = checkLoadAuthentication(null);
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
				if(!auth_config.isReadOk_Resource())
					auth_config.load_from_resources();
				auth_config.init();
				if(!auth_config.isReadOk()){
					auth_config.initProperties(getAppInit().get_path_config()+CONST_XML_MESSAGES);
					auth_config.initWithFOLDER(getAppInit().get_path_config()+CONST_XML_AUTHENTIFICATIONS_FOLDER+"/");

				}
			}catch(bsControllerException je){}
		}
		
		final i_ProviderWrapper wrapperConfigOrganization = checkLoadOrganization(null);
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

		final i_ProviderWrapper wrapperConfigMenu = checkLoadMenu(null);
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
					new bsControllerException(ex, iStub.log_WARN);
				}catch (Throwable  th) {
					new bsControllerException(th, iStub.log_WARN);
				}
			}
		}
		
		if(getAppInit()!=null && getAppInit().get_tag_component_render()!=null && !getAppInit().get_tag_component_render().equals("")){
			tagComponentRender = null;
			if(getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equals("false")){
				try{
					tagComponentRender = (i_provider)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  app_init.id_tag_component_render, getAppInit().get_tag_component_render().trim(), servletContext);
					if(tagComponentRender!=null)
						tagComponentRender.set_context(servletContext);
				}catch(Exception e){
				}
			}
			if(tagComponentRender==null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equals("false")){
				try{
					tagComponentRender = (i_provider)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  app_init.id_tag_component_render, getAppInit().get_tag_component_render().trim(), servletContext);
					if(tagComponentRender!=null)
						tagComponentRender.set_context(servletContext);
				}catch(Exception e){
				}
			}
			if(tagComponentRender==null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equals("false")){
				try{
					tagComponentRender = (i_provider)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  app_init.id_tag_component_render, getAppInit().get_tag_component_render().trim(), servletContext);
					if(tagComponentRender!=null)
						tagComponentRender.set_context(servletContext);
				}catch(Exception e){
				}
			}			
			checkDefaultProvider(servletContext);
			if(tagComponentRender==null && getCdiDefaultProvider()!=null){
				try{
					tagComponentRender = (i_provider)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  app_init.id_tag_component_render, getAppInit().get_tag_component_render().trim(), servletContext);
					if(tagComponentRender!=null)
						tagComponentRender.set_context(servletContext);
				}catch(Exception e){
				}
			}
			if(tagComponentRender==null && getEjbDefaultProvider()!=null){
				try{
					tagComponentRender = (i_provider)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  app_init.id_tag_component_render, getAppInit().get_tag_component_render().trim(), servletContext);
					if(tagComponentRender!=null)
						tagComponentRender.set_context(servletContext);
				}catch(Exception e){
				}
			}

			if(tagComponentRender==null){
				try{
					tagComponentRender = (i_provider)Class.forName(getAppInit().get_tag_component_render().trim()).newInstance();
					if(tagComponentRender!=null)
						tagComponentRender.set_context(servletContext);
				}catch(Exception ex){
					new bsControllerException(ex, iStub.log_WARN);
				}catch (Throwable  th) {
					new bsControllerException(th, iStub.log_WARN);
				}
			}
		}else {
			try{
				tagComponentRender = (i_provider)Class.forName("it.classhidra.plugin.tagrendering.jasper2.TagComponentRenderingProvider").newInstance();
				if(tagComponentRender!=null)
					tagComponentRender.set_context(servletContext);
			}catch(Exception ex){
				new bsControllerException(ex, iStub.log_WARN);
			}catch (Throwable  th) {
				new bsControllerException(th, iStub.log_WARN);
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
					final Vector<?> s_log = (Vector<?>)request.getSession().getAttribute(bsConstants.CONST_SESSION_LOG);
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
					final I_StatisticProvider stack = getStatisticProvider();
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
				final String loadSrc = request.getParameter(CONST_DIRECTINDACTION_bsLoadSrc);
				if(loadSrc==null) return;
				final String loadType = request.getParameter(CONST_DIRECTINDACTION_bsLoadType);

				try{
					byte[] output = null;
					ArrayList<byte[]> resources = null;
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
						setCache((HttpServletResponse)response, request.getParameter(bsConstants.CONST_DIRECTINDACTION_bsLoadCache));
					}

					if(output!=null){
						final OutputStream os = response.getOutputStream();
			    		 os.write(output);
			    		 os.flush();
			    		 os.close();
					}
					if(resources!=null && resources.size()>0){
//						if(loadType!=null)
//							response.setContentType(loadType);
						final OutputStream os = response.getOutputStream();
						for(int i=0;i<resources.size();i++){
							final byte[] towrite = (byte[])resources.get(i);
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
					final String idInSession = request.getParameter("id");
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


			    			final OutputStream os = response.getOutputStream();
				    		 os.write(cTransformation.getOutputcontent());
				    		 os.flush();
				    		 os.close();


						}catch(Exception e){
							new bsControllerException(e, iStub.log_ERROR);
						}catch(Throwable t){
							new bsControllerException(t, iStub.log_ERROR);
						}
					}
				}
				return;
			}

			service(id_action, getServletContext(), request,response);

	}
	
	public static boolean loadActionOnStartup(info_action info, ServletContext servletContext) throws Exception{
		if(info==null || info.getPath()==null || info.getPath().trim().length()==0)
			return false;
		i_action action_instance = getAction_config().actionFactory(info.getPath(), null,servletContext);

		i_bean bean_instance = null;

		if(action_instance!=null){
			final info_bean iBean = load_actions.get_beans().get(action_instance.get_infoaction().getName());
			if(	action_instance instanceof i_bean &&
				(
					action_instance.get_infoaction().getName().equals("") ||
					(iBean!=null && action_instance.get_infoaction().getType().equals(iBean.getType()))
				)
			){
				if(action_instance.asBean().getInfo_context().isStateless())
					bean_instance = action_instance.asBean();
				else
					bean_instance = action_instance;
			}else if(iBean == null && !action_instance.get_infoaction().getName().equals("")){
				if(action_instance.getRealBean()==null){
					if(action_instance.asBean().getInfo_context().isOnlyProxied())
						bean_instance = action_instance;
					else 
						bean_instance = action_instance.asBean();
				}
			}else
				bean_instance = getAction_config().beanFactory(action_instance.get_infoaction().getName(),null,servletContext,action_instance);
			
			if(bean_instance!=null) {
				bean_instance.reimposta();
				bean_instance.loadOnStartup(servletContext);
				action_instance.onPreSet_bean();
				action_instance.set_bean(bean_instance, (bean_instance!=null)?bean_instance.getInfo_context():null);
				action_instance.onPostSet_bean();

				bean_instance.onAddToServletContext();
				bean_instance.clearBeforeStore();
				
				setToOnlyServletContext(bean_instance.get_infoaction().getPath(),bean_instance, servletContext);
				return true;

			}
		}
		return false;

	}

	public static i_action getActionInstance(String id_action, HttpServletRequest request, HttpServletResponse response) throws bsControllerException,ServletException, UnavailableException{
		return getActionInstance(id_action,null, request, response);
	}

	public static i_action getActionInstance(String id_action,String id_call, HttpServletRequest request, HttpServletResponse response) throws bsControllerException,ServletException, UnavailableException{
		return getActionInstance(id_action, id_call, request, response, true);
	}
	
	public static i_action getActionInstance(String id_action,String id_call, HttpServletRequest request, HttpServletResponse response, boolean beanInitFromRequest) throws bsControllerException,ServletException, UnavailableException{
		return getActionInstance(id_action, id_call, new bsContext(request, response), beanInitFromRequest);
	}

	
	public static i_action getActionInstance(String id_action,String id_call, iContext context, boolean beanInitFromRequest) throws bsControllerException,ServletException, UnavailableException{
		final action_payload payload = getActionInstancePayload(id_action, id_call, context, beanInitFromRequest);
		if(payload!=null)
			return payload.getAction();
		else return null;
	}
	
	public static action_payload getActionInstancePayload(String id_action,String id_call, iContext context, boolean beanInitFromRequest) throws bsControllerException,ServletException, UnavailableException{

		final boolean cloned = (context.getRequest()==null)
				?
					false
				:
					(context.getRequest().getParameter(CONST_ID_EXEC_TYPE)==null)?false:context.getRequest().getParameter(CONST_ID_EXEC_TYPE).equalsIgnoreCase(CONST_ID_EXEC_TYPE_CLONED);

		i_action action_instance = getAction_config().actionFactory(id_action, (context.getRequest()==null)?null:context.getRequest().getSession(),(context.getRequest()==null)?null:context.getRequest().getSession().getServletContext());

		i_bean bean_instance = getCurrentForm(id_action,context.getRequest());

		if(bean_instance==null){
			final info_bean iBean = load_actions.get_beans().get(action_instance.get_infoaction().getName());
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
				bean_instance = getAction_config().beanFactory(action_instance.get_infoaction().getName(),(context.getRequest()==null)?null:context.getRequest().getSession(), (context.getRequest()==null)?null:context.getRequest().getSession().getServletContext(),action_instance);
			
			if(bean_instance!=null){
				if(bean_instance.getCurrent_auth()==null || action_instance.get_infoaction().getMemoryInSession().equalsIgnoreCase("true"))
					bean_instance.setCurrent_auth( bsController.checkAuth_init(context.getRequest()));
				if(bean_instance.getCurrent_auth()==null || action_instance.get_infoaction().getMemoryAsLastInstance().equalsIgnoreCase("true"))
					bean_instance.setCurrent_auth( bsController.checkAuth_init(context.getRequest()));
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
				bean_instance.setCurrent_auth( bsController.checkAuth_init(context.getRequest()));
			if(bean_instance.getCurrent_auth()==null || action_instance.get_infoaction().getMemoryAsLastInstance().equalsIgnoreCase("true"))
				bean_instance.setCurrent_auth( bsController.checkAuth_init(context.getRequest()));

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
					}else
						action_instance = bean_instance.asAction();
					
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

		if(action_instance!=null)
			return action_instance.delegatePayloadProcess(id_call, context, beanInitFromRequest);
		else
			return null;

	}
	
	
	public static action_payload getActionInstancePayloadDelegated(i_action action_instance, String id_call, iContext context, boolean beanInitFromRequest) throws bsControllerException,ServletException, UnavailableException{

		Exception beThrowed = null;
		HashMap<String,Object> request2map = null;
		boolean isRemoteEjb=false;
		
		if(action_instance!=null && action_instance.getInfo_context()!=null && action_instance.getInfo_context().isRemote()){
			isRemoteEjb=true;
			try{
				request2map = convertRequestToMap(action_instance.asAction().getClass(),new Class[]{HttpServletRequest.class}, new Object[]{context.getRequest()});
				
//				request2map = (HashMap<String,Object>)
//						util_reflect.findDeclaredMethod(
//							action_instance.asAction().getClass(),
//							"convertRequest2Map", new Class[]{HttpServletRequest.class})
//						.invoke(null, new Object[]{context.getRequest()});
			}catch (Exception e) {
				new bsControllerException(e, iStub.log_ERROR);
			}catch (Throwable e) {
				new bsControllerException(e, iStub.log_ERROR);
			}
			if(request2map==null)
				request2map = new HashMap<String, Object>();
		}
		
//20160419
		if(action_instance.getCurrent_redirect()!=null)
			action_instance.setCurrent_redirect(null);
//-------
		
		if(beanInitFromRequest){
			try{
				if(!isRemoteEjb)
					action_instance.onPreInit(context.getRequest(), context.getResponse());
				else
					action_instance.onPreInit(request2map);
			}catch(Exception e){ 
				action_instance.onPreInit(null, null);
			}

			try{
				if(!isRemoteEjb)
					action_instance.init(context.getRequest(),context.getResponse());
				else{ 
//				action_instance.init(request2map);
					util_supportbean.init(action_instance, request2map, context.getRequest());
				}
			}catch(Exception e){
				action_instance.init(null,null);
			}

			try{
				if(!isRemoteEjb)
					action_instance.onPostInit(context.getRequest(), context.getResponse());
				else
					action_instance.onPostInit(request2map);
			}catch(Exception e){
				action_instance.onPostInit(null,null);
			}
		}

		
		info_call iCall = null;
		Method iCallMethod = null;
		boolean useAsAction = false;

		if(id_call!=null){
			try{
				iCall = (info_call)action_instance.get_infoaction().get_calls().get(id_call);
				if(iCall==null)
					iCall = (info_call)action_instance.get_infoaction().get_calls().get(id_call+"."+((context.getRequest()==null)?"":context.getRequest().getMethod()));
				if(iCall==null){
					final Object[] method_call = action_instance.getMethodAndCall(id_call);
					if(method_call!=null){
						iCallMethod = (Method)method_call[0];
						iCall =  (info_call)method_call[1];
						useAsAction=true;
						if(iCall.getExposed().size()==0){
							action_instance.get_infoaction().get_calls().put(iCall.getName(),iCall);
						}else{
							for(int e=0;e<iCall.getExposed().size();e++){
								final String suffix = "."+iCall.getExposed().get(e).toString();
								if(action_instance.get_infoaction().get_calls().get(iCall.getName()+suffix)==null)
									action_instance.get_infoaction().get_calls().put(iCall.getName()+suffix,iCall);
							}
						}
					}
				}else{
					final List<Exception> searchErrors = new ArrayList<Exception>();
						if(iCallMethod==null){
							try{
								if(!isRemoteEjb)
									iCallMethod = action_instance.getClass().getMethod(iCall.getMethod(), new Class[]{HttpServletRequest.class, HttpServletResponse.class});
								else
									iCallMethod = action_instance.getClass().getMethod(iCall.getMethod(), new Class[]{HashMap.class});
							}catch(Exception em){	
								searchErrors.add(em);
							}
						}
						if(iCallMethod==null){
							try{
								if(!isRemoteEjb)
									iCallMethod = action_instance.asAction().getClass().getMethod(iCall.getMethod(), new Class[]{HttpServletRequest.class, HttpServletResponse.class});
								else
									iCallMethod = action_instance.asAction().getClass().getMethod(iCall.getMethod(), new Class[]{HashMap.class});
								if(iCallMethod!=null)
									useAsAction=true;
							}catch(Exception em){
								searchErrors.add(em);
							}	
						}
						if(iCallMethod==null){
							try{
								if(!isRemoteEjb)
									iCallMethod = action_instance.getClass().getMethod(iCall.getMethod(), new Class[]{bsContext.class});
								else
									iCallMethod = action_instance.getClass().getMethod(iCall.getMethod(), new Class[]{HashMap.class});
								if(iCallMethod!=null)
									useAsAction=true;
							}catch(Exception em){
								searchErrors.add(em);
							}	
						}	
						if(iCallMethod==null){
							try{
								if(!isRemoteEjb)
									iCallMethod = action_instance.asAction().getClass().getMethod(iCall.getMethod(), new Class[]{bsContext.class});
								else
									iCallMethod = action_instance.asAction().getClass().getMethod(iCall.getMethod(), new Class[]{HashMap.class});
								if(iCallMethod!=null)
									useAsAction=true;
							}catch(Exception em){
								searchErrors.add(em);
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
							}	
						}
						if(iCallMethod==null){
							for(int er=0;er<searchErrors.size();er++)
								new bsControllerException((Exception)searchErrors.get(er), iStub.log_ERROR);
						}
				}
			}catch(Exception ex){
				new bsControllerException(ex, iStub.log_WARN);
			}catch(Throwable th){
				new bsControllerException(th, iStub.log_ERROR);
			}
		}




// ACTIONSEVICE
		redirects current_redirect = null;
		if(id_call==null){
			
			
			if(context.getRequest()!=null && !action_instance.get_infoaction().isExposed((context.getRequest()==null)?"":context.getRequest().getMethod()))
				throw new bsControllerException("HTTP Method "+((context.getRequest()==null)?"":context.getRequest().getMethod())+" is not supported for /"+action_instance.get_infoaction().getPath(), context.getRequest(), iStub.log_FATAL);
			
			Method iActionMethod = null;
			if(action_instance.get_infoaction().getMethod()!=null && !action_instance.get_infoaction().getMethod().equals("")){
				final List<Exception> searchErrors = new ArrayList<Exception>();
					if(iActionMethod==null){
						try{
							if(!isRemoteEjb)
								iActionMethod = action_instance.getClass().getMethod(action_instance.get_infoaction().getMethod(), new Class[]{HttpServletRequest.class, HttpServletResponse.class});
							else
								iActionMethod = action_instance.getClass().getMethod(action_instance.get_infoaction().getMethod(), new Class[]{HashMap.class});
								
						}catch(Exception em){
							searchErrors.add(em);
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
						}
					}
					if(iActionMethod==null){
						try{
							if(!isRemoteEjb)
								iActionMethod = action_instance.getClass().getMethod(action_instance.get_infoaction().getMethod(), new Class[]{bsContext.class});
							else
								iActionMethod = action_instance.getClass().getMethod(action_instance.get_infoaction().getMethod(), new Class[]{HashMap.class});
								
							if(iActionMethod!=null)
								useAsAction=true;
						}catch(Exception em){
							searchErrors.add(em);
						}
					}
					if(iActionMethod==null){
						try{
							if(!isRemoteEjb)
								iActionMethod = action_instance.asAction().getClass().getMethod(action_instance.get_infoaction().getMethod(), new Class[]{bsContext.class});
							else
								iActionMethod = action_instance.asAction().getClass().getMethod(action_instance.get_infoaction().getMethod(), new Class[]{HashMap.class});
								
							if(iActionMethod!=null)
								useAsAction=true;
						}catch(Exception em){
							searchErrors.add(em);
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
						}
					}
					if(iActionMethod==null){
						for(int er=0;er<searchErrors.size();er++)
							new bsControllerException((Exception)searchErrors.get(er), iStub.log_ERROR);
					}


			}
			

			if(action_instance.get_infoaction().getSyncro().equalsIgnoreCase("true")){
				
				try {
					action_instance = getTLinkedProvider().link(action_instance, context.getRequest(),context.getResponse());
				}catch(Exception ex){
					new bsControllerException(ex, iStub.log_WARN);
				}catch(Throwable th){
					new bsControllerException(th, iStub.log_ERROR);
				}
				
				if(action_instance.get_bean().getCurrent_auth()==null)
					action_instance.get_bean().setCurrent_auth(bsController.checkAuth_init(context.getRequest()));
				
				try{
					if(!isRemoteEjb)
						action_instance.onPreSyncroservice(context.getRequest(),context.getResponse());
					else
						action_instance.onPreSyncroservice(request2map);
				}catch(Exception e){
					action_instance.onPreSyncroservice(null,null);
				}
				

				
				if(iActionMethod==null){
					try{
						if(!isRemoteEjb)
							current_redirect = action_instance.syncroservice(context.getRequest(),context.getResponse());
						else
							current_redirect = action_instance.syncroservice(request2map);
					}catch(Exception e){
						new bsControllerException(e, iStub.log_ERROR);
						beThrowed = e;
					}
					
				}else{
					
					try{
						if(!isRemoteEjb){
							if(useAsAction)
								current_redirect = prepareActionResponse(
										(action_instance.asAction().get_infoaction()!=null && !action_instance.asAction().get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance.asAction(), iActionMethod, prepareMethod(iActionMethod, action_instance.asAction(), action_instance.asAction().get_infoaction().getRestParametersMapped(), context), action_instance.asAction().get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance.asAction(), iActionMethod, new Object[]{context.getRequest(),context.getResponse()})
										,										
										iActionMethod, action_instance.get_infoaction(),
										context);
							else
								current_redirect = prepareActionResponse(
										(action_instance.get_infoaction()!=null && !action_instance.get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance, iActionMethod, prepareMethod(iActionMethod, action_instance, action_instance.get_infoaction().getRestParametersMapped(), context), action_instance.get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance, iActionMethod, new Object[]{context.getRequest(),context.getResponse()})
										,
										iActionMethod, action_instance.get_infoaction(),
										context);
						}else{
							if(useAsAction)
								current_redirect = prepareActionResponse(
										(action_instance.asAction().get_infoaction()!=null && !action_instance.asAction().get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance.asAction(), iActionMethod, prepareMethod(iActionMethod, action_instance.asAction(), action_instance.asAction().get_infoaction().getRestParametersMapped(), context), action_instance.asAction().get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance.asAction(), iActionMethod, new Object[]{request2map})
										,
										iActionMethod, action_instance.get_infoaction(),
										context);
							else
								current_redirect = prepareActionResponse(										
										(action_instance.get_infoaction()!=null && !action_instance.get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance, iActionMethod, prepareMethod(iActionMethod, action_instance, action_instance.get_infoaction().getRestParametersMapped(), context), action_instance.get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance, iActionMethod, new Object[]{request2map})
										,
										iActionMethod, action_instance.get_infoaction(),
										context);
							
						}

					}catch(Exception e){
						new bsControllerException(e, iStub.log_ERROR);
						beThrowed = e;
					}catch(Throwable t){
						new bsControllerException(t, iStub.log_ERROR);
						beThrowed = new Exception(t);
					}
					
				}
				
				try{
					if(!isRemoteEjb)
						action_instance.onPostSyncroservice(current_redirect,context.getRequest(),context.getResponse());
					else
						action_instance.onPostSyncroservice(current_redirect,request2map);
				}catch(Exception e){
					action_instance.onPostSyncroservice(current_redirect,null,null);
				}
				
				try {
					action_instance = getTLinkedProvider().unlink(action_instance, context.getRequest(),context.getResponse());
				}catch(Exception ex){
					new bsControllerException(ex, iStub.log_WARN);
				}catch(Throwable th){
					new bsControllerException(th, iStub.log_ERROR);
				}
				
			} else{
				
				if(action_instance.get_bean().getCurrent_auth()==null)
					action_instance.get_bean().setCurrent_auth(bsController.checkAuth_init(context.getRequest()));

				
				try {
					action_instance = getTLinkedProvider().link(action_instance, context.getRequest(),context.getResponse());
				}catch(Exception ex){
					new bsControllerException(ex, iStub.log_WARN);
				}catch(Throwable th){
					new bsControllerException(th, iStub.log_ERROR);
				}
				
				try{
					if(!isRemoteEjb)
						action_instance.onPreActionservice(context.getRequest(),context.getResponse());
					else
						action_instance.onPreActionservice(request2map);
				}catch(Exception e){
					action_instance.onPreActionservice(null,null);
				}
				

				
				if(iActionMethod==null){				
					try{
						if(!isRemoteEjb)
							current_redirect = action_instance.actionservice(context.getRequest(),context.getResponse());
						else
							current_redirect = action_instance.actionservice(request2map);
					}catch(Exception e){
						new bsControllerException(e, iStub.log_ERROR);
						beThrowed = e;
					}					
				}else{
					
					try{
						if(!isRemoteEjb){
							if(useAsAction)
								current_redirect = prepareActionResponse(
										(action_instance.asAction().get_infoaction()!=null && !action_instance.asAction().get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance.asAction(), iActionMethod, prepareMethod(iActionMethod, action_instance.asAction(), action_instance.asAction().get_infoaction().getRestParametersMapped(), context), action_instance.asAction().get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance.asAction(), iActionMethod, new Object[]{context.getRequest(),context.getResponse()})
										,										
										iActionMethod, action_instance.get_infoaction(),
										context);
							else
								current_redirect = prepareActionResponse(
										(action_instance.asAction().get_infoaction()!=null && !action_instance.asAction().get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance, iActionMethod, prepareMethod(iActionMethod, action_instance.asAction(), action_instance.asAction().get_infoaction().getRestParametersMapped(), context), action_instance.asAction().get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance, iActionMethod, new Object[]{context.getRequest(),context.getResponse()})
										,
										iActionMethod, action_instance.get_infoaction(),
										context);
						}else{
							if(useAsAction)
								current_redirect = prepareActionResponse(
										(action_instance.asAction().get_infoaction()!=null && !action_instance.asAction().get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance.asAction(), iActionMethod, prepareMethod(iActionMethod, action_instance.asAction(), action_instance.asAction().get_infoaction().getRestParametersMapped(), context), action_instance.asAction().get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance.asAction(), iActionMethod, new Object[]{request2map})
										,
										iActionMethod, action_instance.get_infoaction(),
										context);
							else
								current_redirect = prepareActionResponse(
										(action_instance.get_infoaction()!=null && !action_instance.get_infoaction().isR_R())
										?
											util_reflect.getValue(action_instance, iActionMethod, prepareMethod(iActionMethod, action_instance, action_instance.get_infoaction().getRestParametersMapped(), context), action_instance.get_infoaction().getMappedMethodParameterTypes())
										:
											util_reflect.getValue(action_instance, iActionMethod, new Object[]{request2map})
										,
										iActionMethod, action_instance.get_infoaction(),
										context);
						}
					}catch(Exception e){
						new bsControllerException(e, iStub.log_ERROR);
						beThrowed = e;
					}catch(Throwable t){
						new bsControllerException(t, iStub.log_ERROR);
						beThrowed = new Exception(t);
					}
					
				}
				
				try{
					if(!isRemoteEjb)
						action_instance.onPostActionservice(current_redirect,context.getRequest(),context.getResponse());
					else
						action_instance.onPostActionservice(current_redirect,request2map);
				}catch(Exception e){
					action_instance.onPostActionservice(current_redirect,null,null);
				}
				
				try {
					action_instance = getTLinkedProvider().unlink(action_instance, context.getRequest(),context.getResponse());
				}catch(Exception ex){
					new bsControllerException(ex, iStub.log_WARN);
				}catch(Throwable th){
					new bsControllerException(th, iStub.log_ERROR);
				}
			}
		}else{
			try{
				
				if(action_instance.get_bean().getCurrent_auth()==null)
					action_instance.get_bean().setCurrent_auth(bsController.checkAuth_init(context.getRequest()));
				
				try {
					action_instance = getTLinkedProvider().link(action_instance, context.getRequest(),context.getResponse());
				}catch(Exception ex){
					new bsControllerException(ex, iStub.log_WARN);
				}catch(Throwable th){
					new bsControllerException(th, iStub.log_ERROR);
				}
				
				try{
					if(!isRemoteEjb)
						action_instance.onPreActionCall(id_call, context.getRequest(), context.getResponse());
					else
						action_instance.onPreActionCall(id_call, request2map);	
				}catch(Exception e){
					action_instance.onPreActionCall(id_call, null,null);						
				}


				try{
					if(iCallMethod!=null){
						if(!isRemoteEjb){
							if(useAsAction)
								current_redirect = prepareActionCallResponse(
										(iCall.isR_R())
										?
												(iCallMethod.getParameterTypes().length==1)
												?
												util_reflect.getValue(action_instance.asAction(), iCallMethod, new Object[]{context})
												:
												util_reflect.getValue(action_instance.asAction(), iCallMethod, new Object[]{context.getRequest(),context.getResponse()})
										:
											util_reflect.getValue(action_instance.asAction(), iCallMethod, prepareMethod(iCallMethod, action_instance.asAction(), iCall.getRestParametersMapped(), context), iCall.getMappedMethodParameterTypes()),
										iCallMethod,
										action_instance.get_infoaction(),
										iCall,
										context);
							else
								current_redirect = prepareActionCallResponse(
										(iCall.isR_R())
										?
												(iCallMethod.getParameterTypes().length==1)
												?
												util_reflect.getValue(action_instance, iCallMethod, new Object[]{context})
												:
												util_reflect.getValue(action_instance, iCallMethod, new Object[]{context.getRequest(),context.getResponse()})
										:
											util_reflect.getValue(action_instance, iCallMethod, prepareMethod(iCallMethod, action_instance, iCall.getRestParametersMapped(), context), iCall.getMappedMethodParameterTypes()),
										iCallMethod,
										action_instance.get_infoaction(),
										iCall,
										context);
						}else{
							if(useAsAction)
								current_redirect = prepareActionCallResponse(
										(iCall.isR_R())
										?
											util_reflect.getValue(action_instance.asAction(), iCallMethod, new Object[]{request2map})
										:
											util_reflect.getValue(action_instance.asAction(), iCallMethod, prepareMethod(iCallMethod, action_instance.asAction(), iCall.getRestParametersMapped(), context), iCall.getMappedMethodParameterTypes()),
										iCallMethod,
										action_instance.get_infoaction(),
										iCall,
										context);
							else
								current_redirect = prepareActionCallResponse(
										(iCall.isR_R())
										?
											util_reflect.getValue(action_instance, iCallMethod, new Object[]{request2map})
										:
											util_reflect.getValue(action_instance, iCallMethod, prepareMethod(iCallMethod, action_instance, iCall.getRestParametersMapped(), context), iCall.getMappedMethodParameterTypes()),
										iCallMethod,
										action_instance.get_infoaction(),
										iCall,
										context);
						}

					}else{
						current_redirect = prepareActionCallResponse(
								null,
								null,
								action_instance.get_infoaction(),
								iCall,
								context);
					}
				}catch(Exception e){
					new bsControllerException(e, iStub.log_ERROR);
					beThrowed = e;
				}catch(Throwable t){
					new bsControllerException(t, iStub.log_ERROR);
					beThrowed = new Exception(t);
				}
				try{
					if(!isRemoteEjb)
						action_instance.onPostActionCall(current_redirect,id_call, context.getRequest(), context.getResponse());
					else
						action_instance.onPostActionCall(current_redirect,id_call, request2map);
				}catch(Exception e){
					action_instance.onPostActionCall(current_redirect,id_call, null,null);
				}
				
				try {
					action_instance = getTLinkedProvider().unlink(action_instance, context.getRequest(),context.getResponse());
				}catch(Exception ex){
					new bsControllerException(ex, iStub.log_WARN);
				}catch(Throwable th){
					new bsControllerException(th, iStub.log_ERROR);
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
		if(current_redirect!=null)
			current_redirect.decodeMessage(context.getRequest());
		action_instance.setCurrent_redirect(current_redirect);

		action_instance.onPostSetCurrent_redirect();
		
		action_payload payload = new action_payload(action_instance, current_redirect);
		
		if(iCall!=null && iCall.getNavigated().equalsIgnoreCase("false")){
		}else 
			setInfoNav_CurrentForm(action_instance,current_redirect, context.getRequest());

		
		if(isRemoteEjb){
			bsController.checkAuth_init(context.getRequest()).reInit(action_instance.getCurrent_auth());
			try{
				util_reflect.findDeclaredMethod(
						action_instance.asAction().getClass(),
						"convertMap2Request", new Class[]{HttpServletRequest.class,HttpServletResponse.class})
					.invoke(null, new Object[]{context.getRequest(),context.getResponse()});
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
					if(form!=null)
						form.onAddToSession();
					if(form.get_infoaction()!=null && form.get_infoaction().getPath()!=null)
						setToOnlySession(form.get_infoaction().getPath(),form, context.getRequest());
				}
				if(form.get_infoaction().getMemoryInServletContext().equalsIgnoreCase("true")){
					if(form!=null)
						form.onAddToServletContext();
					if(form.get_infoaction()!=null && form.get_infoaction().getPath()!=null)
						setToOnlyServletContext(form.get_infoaction().getPath(),form, context.getRequest());
				}				
				if(form.get_infoaction().getMemoryAsLastInstance().equalsIgnoreCase("true")){
					if(form!=null)
						form.onAddToLastInstance();
					if(context.getRequest()!=null)
						context.getRequest().getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYASLASTINSTANCE,form);
				}
			}catch(Exception e){
			}

		}else setCurrentForm(action_instance,context.getRequest());


		if(beThrowed!=null)
			throw new bsControllerException(beThrowed, iStub.log_ERROR) ;
		
		return payload;

	}
	
	public static i_action getPrevActionInstance(String id_action, String id_current, HttpServletRequest request, HttpServletResponse response) throws bsControllerException,ServletException, UnavailableException{
		return getPrevActionInstance(id_action, id_current, request, response, true);
	}

	public static i_action getPrevActionInstance(String id_action, String id_current, HttpServletRequest request, HttpServletResponse response, boolean beanInitFromRequest) throws bsControllerException,ServletException, UnavailableException{
		return getPrevActionInstance(id_action, id_current, new bsContext(request, response), beanInitFromRequest);
	}
	
	public static i_action getPrevActionInstance(String id_action, String id_current, iContext context, boolean beanInitFromRequest) throws bsControllerException,ServletException, UnavailableException{
		return getPrevActionInstance(id_action, null, id_current, context, beanInitFromRequest);
	}	
	
	public static i_action getPrevActionInstance(String id_action, String id_current, String id_current_call, iContext context, boolean beanInitFromRequest) throws bsControllerException,ServletException, UnavailableException{
		i_action prev_action_instance = getAction_config().actionFactory(id_action,context.getRequest().getSession(),context.getRequest().getSession().getServletContext());

		i_bean bean_instance = getCurrentForm(id_action,context.getRequest());
		if(	prev_action_instance.get_infoaction().getReloadAfterAction().equalsIgnoreCase("true") &&
			!id_action.equals(id_current)){
			if(bean_instance!=null){
				HashMap<String,Object> request2map=null;
				boolean isRemoteEjb=false;
				
				if(bean_instance.getInfo_context()!=null && bean_instance.getInfo_context().isRemote()){
					isRemoteEjb=true;
					try{
						request2map = convertRequestToMap(bean_instance.asBean().getClass(),new Class[]{HttpServletRequest.class}, new Object[]{context.getRequest()});

//						request2map = (HashMap<String,Object>)
//								util_reflect.findDeclaredMethod(
//										bean_instance.asBean().getClass(),
//									"convertRequest2Map", new Class[]{HttpServletRequest.class})
//								.invoke(null, new Object[]{context.getRequest()});
					}catch (Exception e) {
						new bsControllerException(e, iStub.log_ERROR);
					}catch (Throwable e) {
						new bsControllerException(e, iStub.log_ERROR);
					}
					if(request2map==null)
						request2map = new HashMap<String, Object>();

				}

				if(beanInitFromRequest){
					try{
						if(!isRemoteEjb)
							bean_instance.onPreInit(context.getRequest());
						else
							bean_instance.onPreInit(request2map);
					}catch(Exception e){
	
					}
					try{
						if(!isRemoteEjb)
							bean_instance.init(context.getRequest());
						else
							util_supportbean.init(bean_instance, context.getRequest());
					}catch(Exception e){
						util_supportbean.init(bean_instance, context.getRequest());
					}
					try{
						if(!isRemoteEjb)
							bean_instance.onPostInit(context.getRequest());
						else
							bean_instance.onPostInit(request2map);
					}catch(Exception e){					
					}
				}
				try{
					if(!isRemoteEjb)
						bean_instance.onPreValidate(context.getRequest());
					else
						bean_instance.onPreValidate(request2map);
				}catch(Exception e){					
				}
				redirects validate_redirect = null;
				try{
					if(!isRemoteEjb)
						validate_redirect = bean_instance.validate(context.getRequest());
					else
						validate_redirect = bean_instance.validate(request2map);
				}catch(Exception e){					
				}
				if(validate_redirect==null) {
					try{
						if(!isRemoteEjb)
							validate_redirect = bean_instance.validate(id_action, id_current,  id_current_call, context.getRequest());
						else
							validate_redirect = bean_instance.validate(id_action, id_current,  id_current_call, request2map);
					}catch(Exception e){					
					}					
				}
				try{
					if(!isRemoteEjb)
						bean_instance.onPostValidate(validate_redirect,context.getRequest());
					else
						bean_instance.onPostValidate(validate_redirect, request2map);
				}catch(Exception e){
					

				}
				
// 20180909 UPDATE START			
				if(	prev_action_instance instanceof i_bean &&
						(
							prev_action_instance.get_infoaction().getName().equals("") ||
							bean_instance.asBean().getClass().getName().equals(prev_action_instance.asBean().getClass().getName())
						)
				){
//					info_context info = checkBeanContext(bean_instance_clone.asBean()); 
					if(bean_instance.asBean().getInfo_context().isOnlyProxied()){
						if(prev_action_instance.getRealBean()==null){
							try{
								prev_action_instance = (i_action)bean_instance;
							}catch(Exception e){
								prev_action_instance.get_bean().reInit(bean_instance);
							}
						}else
							prev_action_instance.set_bean(bean_instance, (bean_instance!=null)?bean_instance.getInfo_context():null);
					}else
						prev_action_instance = bean_instance.asAction();					
				}				
// 20180909 UPDATE FINISH
				
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
				final info_bean iBean = load_actions.get_beans().get(prev_action_instance.get_infoaction().getName());
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
					bean_instance_clone = getAction_config().beanFactory(prev_action_instance.get_infoaction().getName(),context.getRequest().getSession(),context.getRequest().getSession().getServletContext(),prev_action_instance);

				if(bean_instance_clone!=null){
					if(bean_instance_clone.getCurrent_auth()==null || prev_action_instance.get_infoaction().getMemoryInSession().equalsIgnoreCase("true"))
						bean_instance_clone.setCurrent_auth( bsController.checkAuth_init(context.getRequest()));
					if(bean_instance_clone.getCurrent_auth()==null || prev_action_instance.get_infoaction().getMemoryAsLastInstance().equalsIgnoreCase("true"))
						bean_instance_clone.setCurrent_auth( bsController.checkAuth_init(context.getRequest()));

					bean_instance_clone.reimposta();
				}
			}else{
//Modifica 20100521 WARNING
//				bean_instance_clone = (i_bean)bean_instance.clone();
				bean_instance_clone = bean_instance;
			}

			if(bean_instance_clone!=null){
				HashMap<String,Object> request2map = null;
				boolean isRemoteEjb=false;
				
				if(bean_instance_clone.getInfo_context()!=null && bean_instance_clone.getInfo_context().isRemote()){
					isRemoteEjb=true;
					try{
						request2map = convertRequestToMap(bean_instance_clone.asBean().getClass(),new Class[]{HttpServletRequest.class}, new Object[]{context.getRequest()});

//						request2map = (HashMap<String,Object>)
//								util_reflect.findDeclaredMethod(
//										bean_instance_clone.asBean().getClass(),
//									"convertRequest2Map", new Class[]{HttpServletRequest.class})
//								.invoke(null, new Object[]{context.getRequest()});
					}catch (Exception e) {
						new bsControllerException(e, iStub.log_ERROR);
					}catch (Throwable e) {
						new bsControllerException(e, iStub.log_ERROR);
					}
					if(request2map==null)
						request2map = new HashMap<String, Object>();

				}

				if(	prev_action_instance.get_infoaction().getReloadAfterAction().equalsIgnoreCase("true")){
					try{
						if(!isRemoteEjb)
							bean_instance_clone.onPreInit(context.getRequest());
						else
							bean_instance.onPreInit(request2map);
					}catch(Exception e){
						bean_instance.onPreInit(request2map);
					}
					try{
						if(!isRemoteEjb)
							bean_instance_clone.init(context.getRequest());
						else
							util_supportbean.init(bean_instance_clone, context.getRequest());
					}catch(Exception e){
						util_supportbean.init(bean_instance_clone, context.getRequest());
					}
					try{
						if(!isRemoteEjb)
							bean_instance_clone.onPostInit(context.getRequest());
						else
							bean_instance.onPostInit(request2map);
					}catch(Exception e){
						bean_instance.onPostInit(request2map);
					}						
				}
				if(bean_instance_clone.getCurrent_auth()==null || prev_action_instance.get_infoaction().getMemoryInSession().equalsIgnoreCase("true"))
					bean_instance_clone.setCurrent_auth( bsController.checkAuth_init(context.getRequest()));
				if(bean_instance_clone.getCurrent_auth()==null || prev_action_instance.get_infoaction().getMemoryAsLastInstance().equalsIgnoreCase("true"))
					bean_instance_clone.setCurrent_auth( bsController.checkAuth_init(context.getRequest()));
				
				try{
					if(!isRemoteEjb)
						bean_instance_clone.onPreValidate(context.getRequest());
					else
						bean_instance.onPreValidate(request2map);
				}catch(Exception e){
					bean_instance.onPreValidate(request2map);					
				}
				redirects validate_redirect = null;
				try{
					if(!isRemoteEjb)
						validate_redirect = bean_instance_clone.validate(context.getRequest());
					else
						validate_redirect = bean_instance_clone.validate(request2map);
				}catch(Exception e){
					validate_redirect = bean_instance_clone.validate(request2map);
				}
				if(validate_redirect==null) {
					try{
						if(!isRemoteEjb)
							validate_redirect = bean_instance_clone.validate(id_action, id_current, id_current_call, context.getRequest());
						else
							validate_redirect = bean_instance_clone.validate(id_action, id_current, id_current_call, request2map);
					}catch(Exception e){
						validate_redirect = bean_instance_clone.validate(request2map);
					}
				}
				try{
					if(!isRemoteEjb)
						bean_instance_clone.onPostValidate(validate_redirect,context.getRequest());
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

	public static Object[] chech4AnotherOutputMode(i_action action_instance,  ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, boolean allowAnotherOutput) throws bsControllerException{
		return chech4AnotherOutputMode(action_instance, null, servletContext, new bsContext(request, response), allowAnotherOutput);
	}
	
	public static Object[] chech4AnotherOutputMode(i_action action_instance, redirects current_redirect, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, boolean allowAnotherOutput) throws bsControllerException{
		return chech4AnotherOutputMode(action_instance, current_redirect, servletContext, new bsContext(request, response), allowAnotherOutput);
	}
	
	public static Object[] chech4AnotherOutputMode(i_action action_instance, redirects current_redirect, ServletContext servletContext, iContext context, boolean allowAnotherOutput) throws bsControllerException{
		if(current_redirect==null)
			current_redirect = action_instance.getCurrent_redirect();
		if(	allowAnotherOutput &&
				action_instance.get_bean()!=null &&
				(
						(
								current_redirect.get_uri()==null ||
										current_redirect.get_uri().trim().equals("") ||
								action_instance.get_bean().getXmloutput() ||
								action_instance.get_bean().getJsonoutput()
						)
						||
						(
								current_redirect.get_uri()!=null &&
								!current_redirect.get_uri().trim().equals("") &&
								current_redirect.get_transformationName()!=null &&
								!current_redirect.get_transformationName().equals("")
						)


				)
			){

					try{
						
						HashMap<String,Object> request2map = null;
						boolean isRemoteEjb=false;
						
						if(action_instance!=null && action_instance.getInfo_context()!=null && action_instance.getInfo_context().isRemote()){
							isRemoteEjb=true;
							try{
								request2map = convertRequestToMap(action_instance.asAction().getClass(),new Class[]{HttpServletRequest.class}, new Object[]{context.getRequest()});

//								request2map = (HashMap<String,Object>)
//										util_reflect.findDeclaredMethod(
//												action_instance.asAction().getClass(),
//											"convertRequest2Map", new Class[]{HttpServletRequest.class})
//										.invoke(null, new Object[]{context.getRequest()});
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
								action_instance.actionBeforeRedirect(context.getRequest(),context.getResponse());
							else
								action_instance.actionBeforeRedirect(request2map);
						}catch(Exception e){
							action_instance.actionBeforeRedirect(null,null);
						}
	
					}catch(Exception e){
						throw new bsControllerException("Controller generic actionBeforeRedirect error. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),context.getRequest(),iStub.log_ERROR);
					}


					final String outputappliedfor = action_instance.get_bean().getOutputappliedfor();
					final String outputserializedname = action_instance.get_bean().getOutputserializedname();
					
					String output4SOAP = (String)action_instance.get_bean().get(bsConstants.CONST_ID_OUTPUT4SOAP);
					String output4JSON = (String)action_instance.get_bean().get(bsConstants.CONST_ID_OUTPUT4JSON);

					final byte[] output4BYTE = (byte[])action_instance.get_bean().get(bsConstants.CONST_ID_OUTPUT4BYTE);


						boolean isDebug=false;
						try{
							isDebug = System.getProperty("debug").equalsIgnoreCase("true");
						}catch(Exception e){
							try{
								isDebug = bsController.getAppInit().get_debug().equalsIgnoreCase("true");
							}catch(Exception ex){
							}
						}
						
						final boolean avoidCheckPermission = (isDebug)?true:false;


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
								context.write(output4SOAP.getBytes());
								return new Object[]{context.getResponse(), Boolean.valueOf(true)};
							}catch(Exception e){
								throw new bsControllerException("Controller generic redirect error. Print Bean as XML. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),context.getRequest(),iStub.log_ERROR);
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
								context.write(output4JSON.getBytes());
								return new Object[]{context.getResponse(), Boolean.valueOf(true)};
							}catch(Exception e){
								throw new bsControllerException("Controller generic redirect error. Print Bean as JSON. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),context.getRequest(),iStub.log_ERROR);
							}
						}


						if(	current_redirect.get_transformationName()!=null &&
							!current_redirect.get_transformationName().equals("")
						){

							i_transformation cTransformation = null;

							if(current_redirect.get_inforedirect()!=null)
								cTransformation = current_redirect.get_inforedirect().transformationFactory(current_redirect.get_transformationName(),context.getRequest().getSession().getServletContext());
							if(cTransformation==null || cTransformation.get_infotransformation()==null)
								cTransformation = action_instance.get_infoaction().transformationFactory(current_redirect.get_transformationName(),context.getRequest().getSession().getServletContext());
							if(cTransformation==null)
								cTransformation = getAction_config().transformationFactory(current_redirect.get_transformationName(),context.getRequest().getSession().getServletContext());


							if(	cTransformation!=null &&
									cTransformation.get_infotransformation()!=null &&
									(
										cTransformation.get_infotransformation().getEvent().equalsIgnoreCase(info_transformation.CONST_EVENT_AFTER) ||
										cTransformation.get_infotransformation().getEvent().equalsIgnoreCase(info_transformation.CONST_EVENT_BOTH)
									)
								){
								cTransformation.setResponseHeader(context.getRequest(),context.getResponse());
								context.getRequest().setAttribute(CONST_ID_TRANSFORMATION4WRAPPER, cTransformation);

								return new Object[]{responseWrapperFactory.getWrapper(context.getResponse()),Boolean.valueOf(false)};
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
									outTranformation = cTransformation.transform(output4BYTE, context.getRequest());
									action_instance.onPostTransform(outTranformation);
								}
								if(cTransformation.get_infotransformation().getInputformat().equalsIgnoreCase(info_transformation.CONST_INPUTFORMAT_FORM)){
									action_instance.onPreTransform(action_instance.get_bean());
									outTranformation = cTransformation.transform(action_instance.get_bean(), context.getRequest());
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
									outTranformation = cTransformation.transform(output4SOAP, context.getRequest());
									action_instance.onPostTransform(outTranformation);
								}

								try{
									cTransformation.setResponseHeader(context.getRequest(),context.getResponse());
									context.write(outTranformation);
									return new Object[]{context.getResponse(), Boolean.valueOf(true)};
								}catch(Exception e){
									throw new bsControllerException("Controller generic redirect error. Transform BeanAsXML with ["+current_redirect.get_transformationName()+"]. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),context.getRequest(),iStub.log_ERROR);
								}
							}
						}


			}
		
		
		
		
		
		return new Object[]{context.getResponse(), Boolean.valueOf(false)};

	}
	
	public static Object[] prepareMethod(Method method, i_action action_instance, HttpServletRequest request, HttpServletResponse response){
		return prepareMethod(method, action_instance, null, request, response);
	}
	public static Object[] prepareMethod(Method method, i_action action_instance, Map<String,String> restParametersMapped, HttpServletRequest request, HttpServletResponse response){
		return prepareMethod(method, action_instance, restParametersMapped, new bsContext(request,response));
	}
	public static Object[] prepareMethod(Method method, i_action action_instance, Map<String,String> restParametersMapped, iContext context){
		if(method==null || method.getParameterTypes()==null || method.getParameterTypes().length==0)
			return new Object[0];
		final Object[] result = new Object[method.getParameterTypes().length];
		boolean inputBase64 = false;
		String charset = "UTF-8";

		if(context.getRequest()!=null){
			inputBase64 = (context.getRequest().getParameter(bsController.CONST_ID_INPUTBASE64)!=null &&
					(
							context.getRequest().getParameter(bsController.CONST_ID_INPUTBASE64).equalsIgnoreCase("true") ||
							context.getRequest().getParameter(bsController.CONST_ID_INPUTBASE64).equalsIgnoreCase(DatatypeConverter.printBase64Binary("true".getBytes()))
					)
				);
			charset = (context.getRequest().getCharacterEncoding()==null || context.getRequest().getCharacterEncoding().equals(""))?"UTF-8":context.getRequest().getCharacterEncoding();
		}
		for(int i=0;i<method.getParameterTypes().length;i++){
			final Class<?> current = method.getParameterTypes()[i];
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
					if(restParametersMapped==null || restParametersMapped.get(annotationParameter.name())==null){
						if(ret!=null && action_instance.get_bean().asBean().getParametersFly().get(annotationParameter.name())!=null && action_instance.get_bean().asBean().getParametersFly().get(annotationParameter.name()).equals(ret))
							ret=null;
						if(ret==null && action_instance.get_bean().asBean().getParametersMP()!=null)
							ret=action_instance.get_bean().asBean().getParametersMP().get(annotationParameter.name());
					}
					if(ret!=null && (ret.getClass().isAssignableFrom(current) || current.isAssignableFrom(ret.getClass())))
						result[i] = ret;
					else if(ret!=null){
						result[i] = util_makeValue.makeFormatedValue1(current,ret.toString(),null);
					}else if(context.getRequest().getParameter(annotationParameter.name())!=null){
						ret = context.getRequest().getParameter(annotationParameter.name());
						if(ret!=null && ret instanceof String && inputBase64){
							try{
								ret=new String(DatatypeConverter.parseBase64Binary((String)ret),charset);
							}catch(Exception e){}
						}
						result[i] = util_makeValue.makeFormatedValue1(current,(ret!=null)?ret.toString():"",null);
					}else if(ret==null){
						try{
							if(current.isAssignableFrom(Boolean.class))
								ret = new Boolean(false);
							else if(current.isAssignableFrom(Number.class)) {
								try {
									ret = current.getConstructor(String.class).newInstance("0");
								}catch(Exception e) {									
								}
							}
							else	
								ret = current.newInstance();
							if(ret!=null){
								if(	ret.getClass().isPrimitive() ||
										ret instanceof String ||
										ret instanceof Number ||
										ret instanceof Date ||
										ret instanceof Boolean)
									ret = util_supportbean.init(ret.getClass(), annotationParameter.name(), context.getRequest(), action_instance);
								else
									util_supportbean.init(ret, annotationParameter.name(), context.getRequest(), action_instance);
							}
							result[i] = ret;
						}catch(Exception e){
							try{
								if(	current.isPrimitive()){
									try{
										result[i] = util_supportbean.init(current, annotationParameter.name(), context.getRequest(), action_instance);
									}catch(Exception ex1){
									}
									if(result[i]==null)
										result[i] = util_supportbean.assignPrimitiveDefault(current);
									
								}								
							}catch(Exception ex){
							}
						}
					}
				}catch(Exception e){
					
				}
			}else{
				try{
					if(current.isAssignableFrom(HttpServletRequest.class))
						result[i] = context.getRequest();
					else if(current.isAssignableFrom(HttpServletResponse.class))
						result[i] = context.getResponse();
					else if(current.isAssignableFrom(bsContext.class))
						result[i] = context;
					else if(current.isPrimitive())
						result[i] = util_makeValue.makeFormatedValue1(current,"",null);
				}catch(Exception e){
					
				}
			}
		}
		return result;
	}
	
	public static redirects prepareActionCallResponse(Object retVal, Method method, info_action iAction, info_call iCall, HttpServletResponse response) throws Exception{
		return prepareActionCallResponse(retVal, method, iAction, iCall, new bsContext(null,response));
	}
	
	public static redirects prepareActionCallResponse(Object retVal, Method method, info_action iAction, info_call iCall, iContext context) throws Exception{
		
		redirects current_redirect = null;
		if(method!=null){
			if(method.getReturnType()!=null && !method.getReturnType().equals(Void.TYPE)){
				if(redirects.class.isAssignableFrom(method.getReturnType()))
					current_redirect = (redirects)retVal;
				else if(response_wrapper.class.isAssignableFrom(method.getReturnType())){
					if(retVal!=null){
						final response_wrapper rWrapper = (response_wrapper)retVal;
						if(rWrapper.getRedirect()!=null)
							current_redirect = rWrapper.getRedirect();
						else{
							final info_redirect fake = new info_redirect().setContentType(rWrapper.getContentType()).setContentName(rWrapper.getContentName()).setContentEncoding(rWrapper.getContentEncoding());
							if(iCall!=null && iCall.getIRedirect()!=null){
								if(iCall.getIRedirect().getContentType()!=null && !iCall.getIRedirect().getContentType().equals(""))
									fake.setContentType(iCall.getIRedirect().getContentType());
								if(iCall.getIRedirect().getContentName()!=null && !iCall.getIRedirect().getContentName().equals(""))
									fake.setContentName(iCall.getIRedirect().getContentName());		
								if(iCall.getIRedirect().getContentEncoding()!=null && !iCall.getIRedirect().getContentEncoding().equals(""))
									fake.setContentEncoding(iCall.getIRedirect().getContentEncoding());								
							}
							updateResponseContentType(fake, context.getResponse(), rWrapper.getResponseStatus());
							if(rWrapper.getContent()!=null){
								if(context.getResponse()!=null){
									if(String.class.isAssignableFrom(rWrapper.getContent().getClass()))
										context.write(((String)rWrapper.getContent()).getBytes());
									else if(byte[].class.isAssignableFrom(rWrapper.getContent().getClass()))
										context.write(((byte[])rWrapper.getContent()));
									else {
										if(
											(iCall!=null && iCall.getIRedirect()!=null && iCall.getIRedirect().getContentType()!=null && iCall.getIRedirect().getContentType().indexOf("/json")>-1) ||
											(rWrapper.getContentType()!=null && rWrapper.getContentType().indexOf("/json")>-1)
										){
											if(context.getResponse()!=null){
												try {
													context.write(((String)JsonWriter.object2json(rWrapper.getContent(), "")).getBytes());
												}catch (Exception e) {
													new bsException(e,iStub.log_ERROR);
													context.write((rWrapper.getContent().toString()).getBytes());
												}
											}
										}else if(
												(iCall!=null && iCall.getIRedirect()!=null && iCall.getIRedirect().getContentType()!=null && iCall.getIRedirect().getContentType().indexOf("/xml")>-1) ||
												(rWrapper.getContentType()!=null && rWrapper.getContentType().indexOf("/xml")>-1)
											){
											if(context.getResponse()!=null){
												try {
													context.write(((String)XmlWriter.object2xml(rWrapper.getContent(),"response")).getBytes());
												}catch (Exception e) {
													new bsException(e,iStub.log_ERROR);
													context.write((rWrapper.getContent().toString()).getBytes());
												}
											}
										}										
									}
									
									if(fake!=null && fake.getFlushBuffer()!=null && fake.getFlushBuffer().equalsIgnoreCase("true")){
										try{
											context.flush();
										}catch(Exception e){
											bsController.writeLog(e.toString(), iStub.log_ERROR);
										}
									}
	
									
								}else
									current_redirect = new redirects(rWrapper);
							}
						}
					}
				}else if(String.class.isAssignableFrom(method.getReturnType()) || byte[].class.isAssignableFrom(method.getReturnType())){
					if(iCall!=null && iCall.getIRedirect()!=null)
						updateResponseContentType(iCall.getIRedirect(), context.getResponse(), 0);
					if(retVal!=null){
						if(context.getResponse()!=null){
							if(String.class.isAssignableFrom(method.getReturnType()))
								context.write(((String)retVal).getBytes());
							if(byte[].class.isAssignableFrom(method.getReturnType()))
								context.write(((byte[])retVal));
							
							if(iCall!=null && iCall.getIRedirect()!=null && iCall.getIRedirect().getFlushBuffer()!=null && iCall.getIRedirect().getFlushBuffer().equalsIgnoreCase("true")){
								try{
									context.flush();
								}catch(Exception e){
									bsController.writeLog(e.toString(), iStub.log_ERROR);
								}
							}
							
						}else
							current_redirect = new redirects(new response_wrapper().setContent(retVal));
					}
				}else {
					if(iCall!=null && iCall.getIRedirect()!=null && iCall.getIRedirect().getContentType()!=null && iCall.getIRedirect().getContentType().indexOf("/json")>-1) {
						if(retVal!=null && context.getResponse()!=null){
							if(iCall!=null && iCall.getIRedirect()!=null)
								updateResponseContentType(iCall.getIRedirect(), context.getResponse(), 0);
							try {
								context.write(((String)JsonWriter.object2json(retVal, "")).getBytes());
							}catch (Exception e) {
								new bsException(e,iStub.log_ERROR);
								context.write((retVal.toString()).getBytes());
							}
						}
					}else if(iCall!=null && iCall.getIRedirect()!=null && iCall.getIRedirect().getContentType()!=null && iCall.getIRedirect().getContentType().indexOf("/xml")>-1) {
						if(iCall!=null && iCall.getIRedirect()!=null)
							updateResponseContentType(iCall.getIRedirect(), context.getResponse(), 0);
						if(retVal!=null && context.getResponse()!=null){
							try {
								context.write(((String)XmlWriter.object2xml(retVal,"response")).getBytes());
							}catch (Exception e) {
								new bsException(e,iStub.log_ERROR);
								context.write((retVal.toString()).getBytes());
							}
						}
					}else {
						if(retVal!=null && context.getResponse()!=null){
							if(iCall!=null && iCall.getIRedirect()!=null)
								updateResponseContentType(iCall.getIRedirect(), context.getResponse(), 0);
							try {
								context.write(((String)JsonWriter.object2json(retVal, "")).getBytes());
							}catch (Exception e) {
								new bsException(e,iStub.log_ERROR);
								context.write((retVal.toString()).getBytes());
							}
						}
						
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
		return prepareActionResponse(retVal, method, iAction, new bsContext(null, response));
	}
	
	public static redirects prepareActionResponse(Object retVal, Method method, info_action iAction, iContext context) throws Exception{
		redirects current_redirect = null;
		if(method!=null){
			if(method.getReturnType()!=null && !method.getReturnType().equals(Void.TYPE)){
				if(redirects.class.isAssignableFrom(method.getReturnType()))
					current_redirect = (redirects)retVal;
				else if(response_wrapper.class.isAssignableFrom(method.getReturnType())){
					if(retVal!=null){
						final response_wrapper rWrapper = (response_wrapper)retVal;
						if(rWrapper.getRedirect()!=null)
							current_redirect = rWrapper.getRedirect();
						else{
							final info_redirect fake = new info_redirect().setContentType(rWrapper.getContentType()).setContentName(rWrapper.getContentName()).setContentEncoding(rWrapper.getContentEncoding());
							if(iAction!=null && iAction.getIRedirect()!=null){
								if(iAction.getIRedirect().getContentType()!=null && !iAction.getIRedirect().getContentType().equals(""))
									fake.setContentType(iAction.getIRedirect().getContentType());
								if(iAction.getIRedirect().getContentName()!=null && !iAction.getIRedirect().getContentName().equals(""))
									fake.setContentName(iAction.getIRedirect().getContentName());		
								if(iAction.getIRedirect().getContentEncoding()!=null && !iAction.getIRedirect().getContentEncoding().equals(""))
									fake.setContentEncoding(iAction.getIRedirect().getContentEncoding());								
							}
							updateResponseContentType(fake, context.getResponse(), rWrapper.getResponseStatus());
							if(rWrapper.getContent()!=null){
								if(context.getResponse()!=null){
									if(String.class.isAssignableFrom(rWrapper.getContent().getClass()))
										context.write(((String)rWrapper.getContent()).getBytes());
									else if(byte[].class.isAssignableFrom(rWrapper.getContent().getClass()))
										context.write(((byte[])rWrapper.getContent()));
									else {
										if(
												(iAction!=null && iAction.getRedirect()!=null && iAction.getIRedirect().getContentType()!=null && iAction.getIRedirect().getContentType().indexOf("/json")>-1) ||
												(rWrapper.getContentType()!=null && rWrapper.getContentType().indexOf("/json")>-1)
											){
												if(context.getResponse()!=null){
													try {
														context.write(((String)JsonWriter.object2json(rWrapper.getContent(), "")).getBytes());
													}catch (Exception e) {
														new bsException(e,iStub.log_ERROR);
														context.write((rWrapper.getContent().toString()).getBytes());
													}
												}
											}else if(
													(iAction!=null && iAction.getIRedirect()!=null && iAction.getIRedirect().getContentType()!=null && iAction.getIRedirect().getContentType().indexOf("/xml")>-1) ||
													(rWrapper.getContentType()!=null && rWrapper.getContentType().indexOf("/xml")>-1)
												){
												if(context.getResponse()!=null){
													try {
														context.write(((String)XmlWriter.object2xml(rWrapper.getContent(),"response")).getBytes());
													}catch (Exception e) {
														new bsException(e,iStub.log_ERROR);
														context.write((rWrapper.getContent().toString()).getBytes());
													}
												}
											}
									}
									if(fake!=null && fake.getFlushBuffer()!=null && fake.getFlushBuffer().equalsIgnoreCase("true")){
										try{
											context.flush();
										}catch(Exception e){
											bsController.writeLog(e.toString(), iStub.log_ERROR);
										}
									}
								}else
									current_redirect = new redirects(rWrapper);
							}
						}
					}
				
				}else if(String.class.isAssignableFrom(method.getReturnType()) || byte[].class.isAssignableFrom(method.getReturnType())){
					if(retVal!=null){
						info_redirect iRedirect = null;
						if(iAction!=null && iAction.getRedirect()!=null && iAction.get_redirects()!=null)
							iRedirect = (info_redirect)iAction.get_redirects().get(iAction.getRedirect());
						
						if(iRedirect!=null)
							updateResponseContentType(iRedirect, context.getResponse(), 0);
						if(context.getResponse()!=null){
							if(String.class.isAssignableFrom(method.getReturnType()))
								context.write(((String)retVal).getBytes());
							if(byte[].class.isAssignableFrom(method.getReturnType()))
								context.write(((byte[])retVal));
							
							if(iRedirect!=null && iRedirect.getFlushBuffer()!=null && iRedirect.getFlushBuffer().equalsIgnoreCase("true")){
								try{
									context.flush();
								}catch(Exception e){
									bsController.writeLog(e.toString(), iStub.log_ERROR);
								}
							}
						}else
							current_redirect = new redirects(new response_wrapper().setContent(retVal));
					}
				}else {
					if(iAction!=null && iAction.getIRedirect()!=null && iAction.getIRedirect().getContentType()!=null && iAction.getIRedirect().getContentType().indexOf("/json")>-1) {
						if(retVal!=null && context.getResponse()!=null){
							info_redirect iRedirect = null;
							if(iAction!=null && iAction.getRedirect()!=null && iAction.get_redirects()!=null)
								iRedirect = (info_redirect)iAction.get_redirects().get(iAction.getRedirect());
							if(iRedirect!=null)
								updateResponseContentType(iRedirect, context.getResponse(), 0);
							try {
								context.write(((String)JsonWriter.object2json(retVal, "")).getBytes());
							}catch (Exception e) {
								new bsException(e,iStub.log_ERROR);
								context.write((retVal.toString()).getBytes());
							}
						}
					}else if(iAction!=null && iAction.getIRedirect()!=null && iAction.getIRedirect().getContentType()!=null && iAction.getIRedirect().getContentType().indexOf("/xml")>-1) {
						if(retVal!=null && context.getResponse()!=null){
							info_redirect iRedirect = null;
							if(iAction!=null && iAction.getRedirect()!=null && iAction.get_redirects()!=null)
								iRedirect = (info_redirect)iAction.get_redirects().get(iAction.getRedirect());
							if(iRedirect!=null)
								updateResponseContentType(iRedirect, context.getResponse(), 0);
							try {
								context.write(((String)XmlWriter.object2xml(retVal,"response")).getBytes());
							}catch (Exception e) {
								new bsException(e,iStub.log_ERROR);
								context.write((retVal.toString()).getBytes());
							}
						}
					}else {
						if(retVal!=null && context.getResponse()!=null){
							info_redirect iRedirect = null;
							if(iAction!=null && iAction.getRedirect()!=null && iAction.get_redirects()!=null)
								iRedirect = (info_redirect)iAction.get_redirects().get(iAction.getRedirect());
							if(iRedirect!=null)
								updateResponseContentType(iRedirect, context.getResponse(), 0);
							try {
								context.write(((String)JsonWriter.object2json(retVal, "")).getBytes());
							}catch (Exception e) {
								new bsException(e,iStub.log_ERROR);
								context.write((retVal.toString()).getBytes());
							}
						}
						
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
		return execRedirect(action_instance, null, servletContext, request, response, false);
	}
	
	public static HttpServletResponse execRedirect(i_action action_instance, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, boolean allowAnotherOutput) throws Exception, bsControllerException,ServletException, UnavailableException{
		return execRedirect(action_instance, null, servletContext, new bsContext(request, response), allowAnotherOutput);
	}
	
	public static HttpServletResponse execRedirect(i_action action_instance, redirects current_redirect, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, boolean allowAnotherOutput) throws Exception, bsControllerException,ServletException, UnavailableException{
		return execRedirect(action_instance, current_redirect, servletContext, new bsContext(request, response), allowAnotherOutput);
	}

	public static HttpServletResponse execRedirect(i_action action_instance, redirects current_redirect, ServletContext servletContext, iContext context, boolean allowAnotherOutput) throws Exception, bsControllerException,ServletException, UnavailableException{
		if(current_redirect==null)
			current_redirect = action_instance.getCurrent_redirect();
		if(action_instance==null || action_instance.get_infoaction()==null) return context.getResponse();
		boolean intoWrapper=false;
		final Object[] resultC4AOutputMode = chech4AnotherOutputMode(action_instance, current_redirect, servletContext, context, allowAnotherOutput);

		if(((Boolean)resultC4AOutputMode[1]).booleanValue()){
			return context.getResponse();
		}

		if(resultC4AOutputMode[0] instanceof a_ResponseWrapper){
//			response = (a_ResponseWrapper)resultC4AOutputMode[0];
			context.setResponse((a_ResponseWrapper)resultC4AOutputMode[0]);
			intoWrapper=true;
		}

		action_instance.onPreRedirect();
		RequestDispatcher rd = null;
		try{
			final redirects current = current_redirect;
			if(current!=null){
				final info_redirect fake = new info_redirect().setContentType(current.getContentType()).setContentName(current.getContentName()).setContentEncoding(current.getContentEncoding());
				if(current.get_inforedirect()!=null){
					if(current.get_inforedirect().getContentType()!=null && !current.get_inforedirect().getContentType().equals(""))
						fake.setContentType(current.get_inforedirect().getContentType());
					if(current.get_inforedirect().getContentName()!=null && !current.get_inforedirect().getContentName().equals(""))
						fake.setContentName(current.get_inforedirect().getContentName());		
					if(current.get_inforedirect().getContentEncoding()!=null && !current.get_inforedirect().getContentEncoding().equals(""))
						fake.setContentEncoding(current.get_inforedirect().getContentEncoding());				
					updateResponseContentType(fake,context.getResponse(),current.getResponseStatus());
				}
			}
			
			
			rd = current_redirect.redirect(servletContext, action_instance.get_infoaction());
		}catch(Exception ex){
			if(getAppInit().get_permit_redirect_resource()!=null && getAppInit().get_permit_redirect_resource().equalsIgnoreCase("true")){
				try{
					final redirects current = current_redirect;
					i_transformation resource2response = null;
					if(current.get_inforedirect().getTransformationName()!=null && !current.get_inforedirect().getTransformationName().equals(""))
						resource2response =  getAction_config().transformationFactory(current.get_inforedirect().getTransformationName(), servletContext);
					else	
						resource2response =  getAction_config().transformationFactory("resource2response", servletContext);
					if(resource2response!=null){
						if(resource2response.transform(action_instance, context.getRequest(), context.getResponse())!=null){
							try{
								action_instance.onPostRedirect(rd);
							}catch(Exception e){							
							}
				    		return context.getResponse();
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
				request2map = convertRequestToMap(action_instance.asAction().getClass(),new Class[]{HttpServletRequest.class}, new Object[]{context.getRequest()});

//				request2map = (HashMap<String,Object>)
//						util_reflect.findDeclaredMethod(
//								action_instance.asAction().getClass(),
//							"convertRequest2Map", new Class[]{HttpServletRequest.class})
//						.invoke(null, new Object[]{context.getRequest()});
			}catch (Exception e) {
				new bsControllerException(e, iStub.log_ERROR);
			}catch (Throwable e) {
				new bsControllerException(e, iStub.log_ERROR);
			}
			if(request2map==null)
				request2map = new HashMap<String, Object>();
//			request2map = util_supportbean.request2map(request);

		}		
		
		try{
			if(!isRemoteEjb)
				action_instance.onPostRedirect(rd);
		}catch(Exception e){
		}
			
		if(rd==null){
			if(current_redirect.get_uri()!=null && !current_redirect.get_uri().trim().equals("")){
				action_instance.onPreRedirectError();
				rd = current_redirect.redirectError(servletContext, action_instance.get_infoaction());
				try{
					if(!isRemoteEjb)
						action_instance.onPostRedirectError(rd);
				}catch(Exception e){
				}
			}
		}
		if(rd==null){
			if(current_redirect.get_uri()!=null && !current_redirect.get_uri().trim().equals("")){
				if(!action_instance.get_infoaction().getError().equals("")) 
					current_redirect.set_uriError(action_instance.get_infoaction().getError());
				else 
					current_redirect.set_uriError(getAction_config().getAuth_error());
				rd = current_redirect.redirectError(servletContext, action_instance.get_infoaction());
			}
		}

		if(rd==null){
			if(current_redirect.get_uri()!=null && !current_redirect.get_uri().trim().equals(""))
				throw new bsControllerException("Controller generic redirect error. Action: ["+action_instance.get_infoaction().getPath()+"] " +current_redirect,context.getRequest(),iStub.log_ERROR);
		}else{
			try{
				try{
					try{
						if(!isRemoteEjb)
							action_instance.actionBeforeRedirect(context.getRequest(),context.getResponse());
						else
							action_instance.actionBeforeRedirect(request2map);
					}catch(Exception e){
						action_instance.actionBeforeRedirect(null,null);
					}
				}catch(Exception e){
					throw new bsControllerException("Controller generic actionBeforeRedirect error. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),context.getRequest(),iStub.log_ERROR);
				}



				if(!intoWrapper){


					if(!action_instance.isIncluded()){
						if(context.getResponse().isCommitted())
							rd.include(context.getRequest(),context.getResponse());
						else
							rd.forward(context.getRequest(),context.getResponse());
					}else
						rd.include(context.getRequest(),context.getResponse());

				}else{

					String tansformationElaborationMode = getAppInit().get_transf_elaborationmode();

					if(tansformationElaborationMode==null || tansformationElaborationMode.trim().length()==0) tansformationElaborationMode=CONST_TRANSFORMATION_ELMODE_INCLUDE;

					if(tansformationElaborationMode.equalsIgnoreCase(CONST_TRANSFORMATION_ELMODE_BOTH)){
						if(context.getResponse().isCommitted())
							rd.include(context.getRequest(),context.getResponse());
						else
							rd.forward(context.getRequest(),context.getResponse());
					}
					if(tansformationElaborationMode.equalsIgnoreCase(CONST_TRANSFORMATION_ELMODE_INCLUDE)){
						rd.include(context.getRequest(),context.getResponse());
					}
					if(tansformationElaborationMode.equalsIgnoreCase(CONST_TRANSFORMATION_ELMODE_FORWARD)){
						rd.forward(context.getRequest(),context.getResponse());
					}


				}



			}catch(Exception e){
				if(intoWrapper){
					throw new bsControllerException("Controller generic wrapped redirect error. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),iStub.log_ERROR);
				}else
					throw new bsControllerException("Controller generic redirect error. Action: ["+action_instance.get_infoaction().getPath()+"] ->" +e.toString(),context.getRequest(),iStub.log_ERROR);
			}
		}
		return context.getResponse();
	}



	public static void execRedirect(i_stream currentStream, redirects currentStreamRedirect, String id_action,
				ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws bsControllerException,ServletException, UnavailableException{
		if(currentStream==null || currentStreamRedirect==null) return;
		currentStream.onPreRedirect(currentStreamRedirect, id_action);
		RequestDispatcher rd =  null;
		

		HashMap<String,Object> request2map = null;
		boolean isRemoteEjb=false;
		
		info_redirect fake = null;

		if(currentStreamRedirect.getWrapper()==null){
			fake = new info_redirect().setContentType(currentStreamRedirect.getContentType()).setContentName(currentStreamRedirect.getContentName()).setContentEncoding(currentStreamRedirect.getContentEncoding());
			if(currentStreamRedirect.get_inforedirect()!=null){
				if(currentStreamRedirect.get_inforedirect().getContentType()!=null && !currentStreamRedirect.get_inforedirect().getContentType().equals(""))
					fake.setContentType(currentStreamRedirect.get_inforedirect().getContentType());
				if(currentStreamRedirect.get_inforedirect().getContentName()!=null && !currentStreamRedirect.get_inforedirect().getContentName().equals(""))
					fake.setContentName(currentStreamRedirect.get_inforedirect().getContentName());		
				if(currentStreamRedirect.get_inforedirect().getContentEncoding()!=null && !currentStreamRedirect.get_inforedirect().getContentEncoding().equals(""))
					fake.setContentEncoding(currentStreamRedirect.get_inforedirect().getContentEncoding());				
			}
			updateResponseContentType(fake,response,currentStreamRedirect.getResponseStatus());
		}else{
			fake = new info_redirect().setContentType(currentStreamRedirect.getWrapper().getContentType()).setContentName(currentStreamRedirect.getWrapper().getContentName()).setContentEncoding(currentStreamRedirect.getWrapper().getContentEncoding());
			if(currentStreamRedirect.get_inforedirect()!=null){
				if(currentStreamRedirect.getWrapper().getContentType()!=null && !currentStreamRedirect.getWrapper().getContentType().equals(""))
					fake.setContentType(currentStreamRedirect.getWrapper().getContentType());
				if(currentStreamRedirect.getWrapper().getContentName()!=null && !currentStreamRedirect.getWrapper().getContentName().equals(""))
					fake.setContentName(currentStreamRedirect.getWrapper().getContentName());		
				if(currentStreamRedirect.getWrapper().getContentEncoding()!=null && !currentStreamRedirect.getWrapper().getContentEncoding().equals(""))
					fake.setContentEncoding(currentStreamRedirect.getWrapper().getContentEncoding());				
			}
			updateResponseContentType(fake,response,currentStreamRedirect.getWrapper().getResponseStatus());
			if(currentStreamRedirect.getWrapper().getContent()!=null){
				if(response!=null){
					try{
						if(String.class.isAssignableFrom(currentStreamRedirect.getWrapper().getContent().getClass()))
							response.getOutputStream().write(((String)currentStreamRedirect.getWrapper().getContent()).getBytes());
						else if(byte[].class.isAssignableFrom(currentStreamRedirect.getWrapper().getContent().getClass()))
							response.getOutputStream().write(((byte[])currentStreamRedirect.getWrapper().getContent()));
						else 
							response.getOutputStream().write((currentStreamRedirect.getWrapper().getContent().toString()).getBytes());
					}catch(Exception e){
						bsController.writeLog(e.toString(), iStub.log_ERROR);
					}					
					if(fake!=null && fake.getFlushBuffer()!=null && fake.getFlushBuffer().equalsIgnoreCase("true")){
						try{
							response.getOutputStream().flush();
						}catch(Exception e){
							bsController.writeLog(e.toString(), iStub.log_ERROR);
						}
					}
				}	
			}
		}
		

		
		if(currentStreamRedirect.get_uri()==null || currentStreamRedirect.get_uri().equals("")){
			return;
		}
		
			
		if(currentStream!=null && currentStream.getInfo_context()!=null && currentStream.getInfo_context().isRemote()){
			isRemoteEjb=true;
			try{
				request2map = convertRequestToMap(currentStream.asStream().getClass(),new Class[]{HttpServletRequest.class}, new Object[]{request});

//				request2map = (HashMap<String,Object>)
//						util_reflect.findDeclaredMethod(
//								currentStream.asStream().getClass(),
//							"convertRequest2Map", new Class[]{HttpServletRequest.class})
//						.invoke(null, new Object[]{request});
			}catch (Exception e) {
				new bsControllerException(e, iStub.log_ERROR);
			}catch (Throwable e) {
				new bsControllerException(e, iStub.log_ERROR);
			}
			if(request2map==null)
				request2map = new HashMap<String, Object>();
//			request2map = util_supportbean.request2map(request);

		}

		
		try{
			if(!isRemoteEjb)
				rd = currentStream.redirect(servletContext, currentStreamRedirect, id_action);
			else{
				final info_action iaction = currentStream.redirect(request2map, currentStreamRedirect, id_action);
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

	public static Vector<info_stream> getActionStreams(String id_action){
		Vector<info_stream> _streams = null;
		final info_action iActionMapped = load_actions.get_actions().get(id_action);
		if(iActionMapped==null)
			return new Vector<info_stream>();
		else if(iActionMapped.getVm_streams()!=null)
			_streams = iActionMapped.getVm_streams();
		else{
			_streams = new Vector<info_stream>();
			final Vector<info_stream> _streams_orig = getAction_config().get_streams_apply_to_actions().get("*");

			if(_streams_orig!=null) _streams.addAll(_streams_orig);
			final Vector<info_stream> _streams4action = getAction_config().get_streams_apply_to_actions().get(id_action);
			if(_streams4action!=null){
				final Vector<info_stream> _4add = new Vector<info_stream>();
				final HashMap<String,String> _4remove = new HashMap<String, String>();
				for(int i=0;i<_streams4action.size();i++){
					final info_stream currentis = (info_stream)_streams4action.get(i);
					if(currentis.get_apply_to_action()!=null){
						final info_apply_to_action currentiata = (info_apply_to_action)currentis.get_apply_to_action().get(id_action);
						if(currentiata.getExcluded()!=null && currentiata.getExcluded().equalsIgnoreCase("true"))
							_4remove.put(currentis.getName(),currentis.getName());
						else _4add.add(currentis);
					}
				}
				_streams.addAll(_4add);
				if(_4remove.size()>0){
					int i=0;
					while(i<_streams.size()){
						final info_stream currentis = (info_stream)_streams.get(i);
						if(_4remove.get(currentis.getName())!=null) _streams.remove(i);
						else i++;
					}
				}
				_streams = Util_sort.sort(_streams,"int_order","A");
			}
			iActionMapped.setVm_streams(_streams);
		}
		return _streams;

	}

	public static Vector<info_stream> getActionStreams_(String id_action){

		final Vector<info_stream> _streams_orig = getAction_config().get_streams_apply_to_actions().get("*");


//Modifica 20100521 Warning
/*
		try{
			_streams = (Vector)util_cloner.clone(_streams_orig);
		}catch(Exception e){
		}
*/
		final Vector<info_stream> _streams4action = getAction_config().get_streams_apply_to_actions().get(id_action);
		if(_streams4action==null) 
			return (_streams_orig==null)?new Vector<info_stream>():_streams_orig;
		else{
			Vector<info_stream> _streams = new Vector<info_stream>();
			if(_streams_orig!=null) _streams.addAll(_streams_orig);
			
			final Vector<info_stream> _4add = new Vector<info_stream>();
			final HashMap<String,String> _4remove = new HashMap<String, String>();
		
			for(int i=0;i<_streams4action.size();i++){
				final info_stream currentis = (info_stream)_streams4action.get(i);
				if(currentis.get_apply_to_action()!=null){
					final info_apply_to_action currentiata = (info_apply_to_action)currentis.get_apply_to_action().get(id_action);
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
					final info_stream currentis = (info_stream)_streams.get(i);
					if(_4remove.get(currentis.getName())!=null) _streams.remove(i);
					else i++;
				}
			}
			_streams = Util_sort.sort(_streams,"int_order","A");
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
				final char separator=bsController.getAppInit().get_actioncall_separator().charAt(0);
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
				final Vector<info_stream> _streams = getActionStreams_(id_action);

				final info_stream blockStreamEnter = performStream_EnterRS(_streams, id_action,action_instance, servletContext, request, response);
				if(blockStreamEnter!=null){
					isException(action_instance, request);
					if(stat!=null){
						stat.setFt(new Date());
						stat.setException(new Exception("Blocked by STREAM ENTER:["+blockStreamEnter.getName()+"]"));
						putToStatisticProvider(stat);
					}
					return response;
				}

				final action_payload payload = performActionPayload(id_action, id_call, servletContext,  new bsContext(request,response), true);
				action_instance = payload.getAction();

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
					request.setAttribute(CONST_BEAN_$INSTANCEACTIONPOOL,new HashMap<String,i_action>());
				@SuppressWarnings("unchecked")
				final HashMap<String,i_action> included_pool = (HashMap<String, i_action>)request.getAttribute(CONST_BEAN_$INSTANCEACTIONPOOL);
				if(action_instance.get_infoaction()!=null && action_instance.get_infoaction().getName()!=null)
					included_pool.put(action_instance.get_infoaction().getName(),action_instance);
				else if(action_instance.get_infoaction()!=null && action_instance.get_infoaction().getPath()!=null)
					included_pool.put(action_instance.get_infoaction().getPath(),action_instance);

				request.setAttribute(CONST_BEAN_$INSTANCEACTION,action_instance);

				if(request.getParameter(CONST_ID_JS4AJAX)==null && action_instance!=null && action_instance.get_bean()!=null)
					request.setAttribute(CONST_ID_JS4AJAX,action_instance.get_bean().getJs4ajax());



//				if(action_instance.getCurrent_redirect()!=null){
//					if( !action_instance.getCurrent_redirect().is_avoidPermissionCheck()){
						final info_stream blockStreamExit = performStream_ExitRS(_streams, id_action,action_instance, servletContext, request, response);
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
				if(payload.getRedirect()!=null){
					request.removeAttribute(CONST_ID_REQUEST_TYPE);
					response = execRedirect(action_instance,payload.getRedirect(), servletContext,request,response,true);
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
				if(request.getSession().getAttribute(bsConstants.CONST_BEAN_$LISTMESSAGE)!=null) request.getSession().removeAttribute(bsConstants.CONST_BEAN_$LISTMESSAGE);
				service_ErrorRedirect(id_action,servletContext,request, response);
				stat.setException(e);
			}catch(Exception ex){
				if(request.getAttribute(CONST_BEAN_$ERRORACTION)==null) request.setAttribute(CONST_BEAN_$ERRORACTION, ex.toString());
				else request.setAttribute(CONST_BEAN_$ERRORACTION, request.getAttribute(CONST_BEAN_$ERRORACTION) + ";" +ex.toString());

				new bsControllerException(ex,iStub.log_ERROR);
				isException(action_instance, request);
				addAsMessage(ex,request);
//				request.setAttribute(CONST_BEAN_$ERRORACTION, ex);
//				if(request.getSession().getAttribute(bsConstants.CONST_BEAN_$LISTMESSAGE)!=null) request.getSession().removeAttribute(bsConstants.CONST_BEAN_$LISTMESSAGE);

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



	@SuppressWarnings("unchecked")
	public static void addAsMessage(Throwable ex, HttpServletRequest request){
		if(ex!=null && request.getSession().getAttribute(bsConstants.CONST_BEAN_$LISTMESSAGE)!=null){
			final message mess = new message();
			mess.setDESC_MESS(ex.toString());
			mess.setTYPE("E");
			((Vector<message>)request.getSession().getAttribute(bsConstants.CONST_BEAN_$LISTMESSAGE)).add(mess);
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
		return performAction(id_action, id_call, servletContext, new bsContext(request,response), true);
	}
	
	public static i_action performAction(String id_action, String id_call, ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response, boolean beanInitFromRequest) throws bsControllerException, Exception, Throwable{
		return performAction(id_action, id_call, servletContext, new bsContext(request, response), beanInitFromRequest);
	}
	
	public static i_action performAction(String id_action, String id_call, ServletContext servletContext,
			iContext context, boolean beanInitFromRequest) throws bsControllerException, Exception, Throwable{
		final action_payload payload = performActionPayload(id_action, id_call, servletContext, context, beanInitFromRequest);
		return payload.getAction();
	}
	
	public static action_payload performActionPayload(String id_action, String id_call, ServletContext servletContext,
				iContext context, boolean beanInitFromRequest) throws bsControllerException, Exception, Throwable{
		i_action prev_action_instance = null;
		String id_prev = null;
		String id_rtype = (String)context.getRequest().getAttribute(CONST_ID_REQUEST_TYPE);
		if(id_rtype!=null && id_rtype.equalsIgnoreCase(CONST_REQUEST_TYPE_INCLUDE)){

		}else{
			id_prev = context.getRequest().getParameter(CONST_ID_$NAVIGATION);
			
// 20180909 UPDATE	START			
			if(id_prev==null) {
				id_prev = (String)context.getRequest().getSession().getAttribute(CONST_ID_$LAST_NAVIGATION);
				if(id_prev!=null)
					context.getRequest().getSession().removeAttribute(CONST_ID_$LAST_NAVIGATION);
			}
			if(id_call!=null && (id_call.equals("componentupdate") || id_call.equals("asyncupdate")))
				id_prev=null;
// 20180909 UPDATE FINISH	
			
			if(id_prev!=null && id_prev.indexOf(":")>-1){
				id_prev = id_prev.substring(0,id_prev.indexOf(":"));
				prev_action_instance = getPrevActionInstance(id_prev,id_action,id_call,context,beanInitFromRequest);
				
//	20180909 UPDATE	START			
				if(prev_action_instance!=null && prev_action_instance.getCurrent_redirect()!=null)
					return new action_payload(prev_action_instance,prev_action_instance.getCurrent_redirect());
				
// 20180909 UPDATE FINISH			
				
			}			
		}
		final action_payload payload = getActionInstancePayload(id_action,id_call,context,beanInitFromRequest);
		final i_action action_instance = payload.getAction();
		
//		20180909 UPDATE	START			
		if(action_instance!=null && id_prev!=null && prev_action_instance!=null && !id_action.equals(id_prev)) {
			try{
				HashMap<String,Object> request2map = null;
				boolean isRemoteEjb=false;
				if(prev_action_instance.getInfo_context()!=null && prev_action_instance.getInfo_context().isRemote()){
					isRemoteEjb=true;
					try{
						request2map = convertRequestToMap(prev_action_instance.asBean().getClass(),new Class[]{HttpServletRequest.class}, new Object[]{context.getRequest()});

//						request2map = (HashMap<String,Object>)
//								util_reflect.findDeclaredMethod(
//										prev_action_instance.asBean().getClass(),
//									"convertRequest2Map", new Class[]{HttpServletRequest.class})
//								.invoke(null, new Object[]{context.getRequest()});
					}catch (Exception e) {
						new bsControllerException(e, iStub.log_ERROR);
					}catch (Throwable e) {
						new bsControllerException(e, iStub.log_ERROR);
					}
					if(request2map==null)
						request2map = new HashMap<String, Object>();
				}

				if(!isRemoteEjb) {					
					prev_action_instance.leaveActionContext(action_instance, context.getRequest(), context.getResponse());
					action_instance.enterActionContext(prev_action_instance, context.getRequest(), context.getResponse());
				}
				else {
					prev_action_instance.leaveActionContext(action_instance,request2map);
					action_instance.enterActionContext(prev_action_instance,request2map);
				}
			}catch(Exception e){					
			}
		}
//		20180909 UPDATE	FINISH		
			
		return payload;
	}

	public static boolean performStream_Enter(Vector<info_stream> _streams, String id_action,i_action action_instance, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws bsControllerException, Exception, Throwable{
		if(performStream_EnterRS(_streams, id_action, action_instance, servletContext, request, response)!=null) return false;
		return true;
	}

	public static info_stream performStream_EnterRS(Vector<info_stream> _streams, String id_action,i_action action_instance, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws bsControllerException, Exception, Throwable{

		for(int i=0;i<_streams.size();i++){
			final info_stream iStream = (info_stream)_streams.get(i);
			final i_stream currentStream = getAction_config().streamFactory(iStream.getName(),request.getSession(), servletContext);
			if(currentStream!=null){	
				HashMap<String,Object> request2map = null;
				boolean isRemoteEjb=false;
				
				if(currentStream!=null && currentStream.getInfo_context()!=null && currentStream.getInfo_context().isRemote()){
					isRemoteEjb=true;
					try{
						request2map = convertRequestToMap(currentStream.asStream().getClass(),new Class[]{HttpServletRequest.class}, new Object[]{request});

//						request2map = (HashMap<String,Object>)
//								util_reflect.findDeclaredMethod(
//										currentStream.asStream().getClass(),
//									"convertRequest2Map", new Class[]{HttpServletRequest.class})
//								.invoke(null, new Object[]{request});
					}catch (Exception e) {
						new bsControllerException(e, iStub.log_ERROR);
					}catch (Throwable e) {
						new bsControllerException(e, iStub.log_ERROR);
					}
					if(request2map==null)
						request2map = new HashMap<String, Object>();
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

	public static boolean performStream_Exit(Vector<info_stream> _streams, String id_action,i_action action_instance, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws bsControllerException, Exception, Throwable{
		if(performStream_ExitRS(_streams, id_action, action_instance, servletContext, request, response)!=null) return false;
		return true;
	}

	public static info_stream performStream_ExitRS(Vector<info_stream> _streams, String id_action,i_action action_instance, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) throws bsControllerException, Exception, Throwable{

		for(int i=_streams.size()-1;i>-1;i--){
			final info_stream iStream = (info_stream)_streams.get(i);
			final i_stream currentStream = getAction_config().streamFactory(iStream.getName(), request.getSession(), servletContext);
			if(currentStream!=null){
				HashMap<String,Object> request2map = null;
				boolean isRemoteEjb=false;
				
				if(currentStream!=null && currentStream.getInfo_context()!=null && currentStream.getInfo_context().isRemote()){
					isRemoteEjb=true;
					try{
						request2map = convertRequestToMap(currentStream.asStream().getClass(),new Class[]{HttpServletRequest.class}, new Object[]{request});

//						request2map = (HashMap<String,Object>)
//								util_reflect.findDeclaredMethod(
//										currentStream.asStream().getClass(),
//									"convertRequest2Map", new Class[]{HttpServletRequest.class})
//								.invoke(null, new Object[]{request});
					}catch (Exception e) {
						new bsControllerException(e, iStub.log_ERROR);
					}catch (Throwable e) {
						new bsControllerException(e, iStub.log_ERROR);
					}
					if(request2map==null)
						request2map = new HashMap<String, Object>();
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

		final info_navigation fromNav = getFromInfoNavigation(null, request);
		if(fromNav!=null){
			final info_navigation nav = fromNav.find(id_current);
			if(nav!=null){
				final i_bean content = nav.get_content();
				if(content!=null)
					content.onGetFromNavigation();
				
				if(content==null) {
					final info_action infoAction = load_actions.get_actions().get(id_current);				
					if(infoAction!=null && infoAction.getNavigatedMemoryContent()!=null && infoAction.getNavigatedMemoryContent().equalsIgnoreCase("false")) {
					}else
						return content;
				}else				
					return content;
			}
		}
		try{
			final info_action infoAction = load_actions.get_actions().get(id_current);
			
			if(	infoAction.getMemoryInSession().equalsIgnoreCase("true")){
				final i_bean content = (i_bean)getFromOnlySession(infoAction.getPath(), request);

				if(content!=null)
					content.onGetFromSession();
				return content;
			}
			if(	infoAction.getMemoryInServletContext().equalsIgnoreCase("true")){
				final i_bean content = (i_bean)getFromOnlyServletContext(infoAction.getPath(), request);
				
				if(content!=null)
					content.onGetFromServletContext();
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
			}else new bsControllerException(e,iStub.log_ERROR);
		}


		return null;
	}

	
	public static Map<String,i_bean> getCurrentForm(Class<?> reference, HttpServletRequest request){
		if(reference==null) return null;
		Map<String,i_bean> result = new HashMap<String, i_bean>();
		info_navigation fromNav = getFromInfoNavigation(null, request);
		if(fromNav!=null){
			while(fromNav!=null) {
				if(fromNav.get_content()!=null && (fromNav.get_content().getClass().isAssignableFrom(reference) || isAssignableFromInterface(fromNav.get_content().getClass().getInterfaces(),reference))) {
					if(fromNav.getIAction()!=null)
						result.put(fromNav.getIAction().getPath(),fromNav.get_content());
				}
				fromNav=fromNav.getChild();
			}
		}
		try{
			Map<String,i_bean> instanceSession = checkOnlySession(request);
			if(instanceSession==null) {
				@SuppressWarnings("unchecked")
				Map<String,i_bean> attribute = 
						 (Map<String,i_bean>)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
				instanceSession = attribute;
			}
			if(instanceSession!=null){
				Iterator<Map.Entry<String,i_bean>> entry = instanceSession.entrySet().iterator();
				while (entry.hasNext()) {
					Map.Entry<String,i_bean> current = (Map.Entry<String,i_bean>)entry.next();
					if(current.getValue()!=null && (current.getValue().getClass().isAssignableFrom(reference) || isAssignableFromInterface(current.getValue().getClass().getInterfaces(),reference)))
						result.put(current.getKey().toString(),current.getValue());
				}
			}
			
			Map<String,i_bean> instanceServletContext = checkOnlyServletContext(request);
			if(instanceServletContext==null) {
				@SuppressWarnings("unchecked")
				Map<String,i_bean> attribute = 
						 (Map<String,i_bean>)request.getSession().getServletContext().getAttribute(bsConstants.CONST_BEAN_$SERVLETCONTEXT);
				instanceServletContext = attribute;			
			}
			if(instanceServletContext!=null){
				Iterator<Map.Entry<String,i_bean>> entry = instanceServletContext.entrySet().iterator();
				while (entry.hasNext()) {
					Map.Entry<String,i_bean> current = (Map.Entry<String,i_bean>)entry.next();
					if(current.getValue()!=null && (current.getValue().getClass().isAssignableFrom(reference) || isAssignableFromInterface(current.getValue().getClass().getInterfaces(),reference)))
						result.put(current.getKey().toString(),current.getValue());
				}
			}
			
			i_bean content = null;
			try{
				content = (i_bean)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYASLASTINSTANCE);
			}catch(Exception e){
			}
			if(	content!=null &&
				content.get_infoaction()!=null &&
				content.get_infoaction().getPath()!=null &&
				content.getClass().isAssignableFrom(reference)
			)
				result.put(content.get_infoaction().getPath(),content);
			
			
		}catch(Exception e){
			if( e instanceof java.lang.NullPointerException){
			}else new bsControllerException(e,iStub.log_ERROR);
		}


		return result;
	}
	
	public static boolean isAssignableFromInterface(Class<?>[] interfaces, Class<?> interf) {
		if(interfaces==null || interf==null)
			return false;
		for(Class<?> current:interfaces) {
			if(current==interf)
				return true;
		}
		return false;
	}

	public static void setInfoNav_CurrentForm(i_action action_instance, HttpServletRequest request){
		setInfoNav_CurrentForm(action_instance, null, request);
	}

	public static void setInfoNav_CurrentForm(i_action action_instance, redirects current_redirect, HttpServletRequest request){
		if(current_redirect==null)
			current_redirect = action_instance.getCurrent_redirect();
		boolean go = false;
		i_bean form = null;
		if(action_instance.isBeanEqualAction())
			form = action_instance;
		else
			form = action_instance.get_bean();
		
		info_action infoAction = (form.get_infoaction()!=null)?form.get_infoaction():action_instance.get_infoaction();
		
		try{
			if(infoAction!=null && infoAction.getNavigated().equalsIgnoreCase("true")) go = true;
		}catch(Exception ex){
		}
		if(!go){
			return;
		}

		final info_navigation nav = new info_navigation();
		try{
			
			boolean redirectNavigatedFalse=false;
			if(	current_redirect!=null &&
				current_redirect.get_inforedirect()!=null &&
				current_redirect.get_inforedirect().getNavigated()!=null &&
				current_redirect.get_inforedirect().getNavigated().equalsIgnoreCase("false"))
				redirectNavigatedFalse=true;
			nav.init(infoAction,null,new info_service(request),null);
			
			final info_navigation fromNav = getFromInfoNavigation(null, request);
			if(fromNav!=null){
				if(!redirectNavigatedFalse)
					fromNav.add(nav);
				else if(action_instance.get_infoaction()!=null &&
						action_instance.get_infoaction().getPath()!=null &&
						fromNav.find(action_instance.get_infoaction().getPath())!=null)
					fromNav.add(nav);
			}
			else if(!redirectNavigatedFalse)
				setToInfoNavigation(nav, request);
			
			
		}catch(Exception e){
			new bsControllerException(e,iStub.log_ERROR);
		}

	}


	public static void setCurrentForm(i_action action_instance, HttpServletRequest request){
		final boolean cloned = (request==null)
						?
							false
						:	
							(request.getParameter(CONST_ID_EXEC_TYPE)==null)?false:request.getParameter(CONST_ID_EXEC_TYPE).equalsIgnoreCase(CONST_ID_EXEC_TYPE_CLONED);
		if(cloned) return;
		boolean go = false;
		i_bean form = null;
		if(action_instance.isBeanEqualAction())
			form = action_instance;
		else
			form = action_instance.get_bean();
		
		info_action infoAction = (form.get_infoaction()!=null)?form.get_infoaction():action_instance.get_infoaction();

		
		try{
			if(infoAction!=null && infoAction.getNavigated().equalsIgnoreCase("true")) go = true;
		}catch(Exception ex){
		}

		if(!go){
			setCurrentFormIfNotNavigated(form, infoAction, request);
			return;
		}else{
			try{
				boolean redirectNavigatedFalse=false;
				final info_navigation nav = new info_navigation();
				if(form!=null)
					form.clearBeforeStore();
				if(action_instance.getCurrent_redirect()==null){
					nav.init(infoAction,null,new info_service(request),form);
				}else if(	action_instance.getCurrent_redirect().get_inforedirect()!=null &&
							action_instance.getCurrent_redirect().get_inforedirect().getNavigated()!=null &&
							action_instance.getCurrent_redirect().get_inforedirect().getNavigated().equalsIgnoreCase("false")){	
					nav.init(infoAction,action_instance.getCurrent_redirect().get_inforedirect(),new info_service(request),form);
					redirectNavigatedFalse=true;
				}else
					nav.init(infoAction,action_instance.getCurrent_redirect().get_inforedirect(),new info_service(request),form);

				final info_navigation fromNav = getFromInfoNavigation(null, request);
				if(fromNav!=null){
					if(form!=null) form.onAddToNavigation();
					if(!redirectNavigatedFalse)
						fromNav.add(nav);
					else if(action_instance.get_infoaction()!=null &&
							action_instance.get_infoaction().getPath()!=null &&
							fromNav.find(action_instance.get_infoaction().getPath())!=null)
						fromNav.add(nav);
				}
				else if(!redirectNavigatedFalse)
					setToInfoNavigation(nav, request);
			}catch(Exception e){
			}
			
			if(infoAction!=null && infoAction.getNavigatedMemoryContent()!=null &&  infoAction.getNavigatedMemoryContent().equalsIgnoreCase("false")) {
				setCurrentFormIfNotNavigated(form, infoAction, request);
			}
		}
	}
	
	public static void setCurrentFormIfNotNavigated(i_bean form, HttpServletRequest request) {
		setCurrentFormIfNotNavigated(form, null, request);
	}

	public static void setCurrentFormIfNotNavigated(i_bean form, info_action infoAction, HttpServletRequest request) {
		if(infoAction==null)
			infoAction = form.get_infoaction();
		try{
			if(form.get_infoaction().getMemoryInSession().equalsIgnoreCase("true")){
				if(form!=null)
					form.onAddToSession();
				if(form.get_infoaction()!=null && form.get_infoaction().getPath()!=null){
					form.clearBeforeStore();
					setToOnlySession(form.get_infoaction().getPath(),form, request);
				}
			}
			if(form.get_infoaction().getMemoryInServletContext().equalsIgnoreCase("true")){
				if(form!=null)
					form.onAddToServletContext();
				if(form.get_infoaction()!=null && form.get_infoaction().getPath()!=null){
					form.clearBeforeStore();
					setToOnlyServletContext(form.get_infoaction().getPath(),form, request);
				}
			}
			if(form.get_infoaction().getMemoryAsLastInstance().equalsIgnoreCase("true")){
				if(form!=null){
					form.onAddToLastInstance();
					form.clearBeforeStore();
				}
				if(request!=null)
					request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYASLASTINSTANCE,form);
			}

		}catch(Exception e){
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
					}catch(Throwable t){
					}
				}
				if(!ok && getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equals("false")){
					try{
						logGenerator = (i_log_generator)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  logInit.get_LogGenerator(), i_log_generator.class.getName(), null);
						if(logGenerator!=null)
							ok = true;
					}catch(Exception e){
					}catch(Throwable t){
					}
				}
				if(!ok && getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equals("false")){
					try{
						logGenerator = (i_log_generator)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  logInit.get_LogGenerator(), i_log_generator.class.getName(), null);
						if(logGenerator!=null)
							ok = true;
					}catch(Exception e){
					}catch(Throwable t){
					}
				}		
				checkDefaultProvider(null);
				if(!ok && getCdiDefaultProvider()!=null){
					try{
						logGenerator = (i_log_generator)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(), logInit.get_LogGenerator(), i_log_generator.class.getName(), null);
						if(logGenerator!=null)
							ok = true;
					}catch(Exception e){
					}catch(Throwable t){
					}
				}
				if(!ok && getEjbDefaultProvider()!=null){
					try{
						logGenerator = (i_log_generator)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(), logInit.get_LogGenerator().replace("$", "ejb_"), i_log_generator.class.getName(), null);
						if(logGenerator!=null)
							ok = true;
					}catch(Exception e){
					}catch(Throwable t){
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
			@SuppressWarnings("unchecked")
			Vector<String> attribute = 
					 (Vector<String>)request.getSession().getAttribute(bsConstants.CONST_SESSION_LOG);
			Vector<String> s_log = attribute;
			if(s_log==null) s_log = new Vector<String>();
			if(mess!=null && !mess.equals(""))  s_log.add(mess);
			request.getSession().setAttribute(bsConstants.CONST_SESSION_LOG, s_log);
		}
	}

	public static synchronized void writeLog(String msg, String level) {
		try{
			final String classInfo=util_reflect.prepareClassInfo(new String[]{"bsController.java","bsControllerException.java","bsControllerMessageException.java"},new String[]{"writeLog","<init>","<init>"});
			if(getLogG()==null) 
				checkLogGenerator(null);
			getLogG().writeLog(msg,"","",classInfo,level);
		}catch(Exception e){}
	}
	public static synchronized void writeLog(Object msg, String level) {
		try{
			final String classInfo=util_reflect.prepareClassInfo(new String[]{"bsController.java","bsControllerException.java","bsControllerMessageException.java"},new String[]{"writeLog","<init>","<init>"});
//			if(getLogG()==null) service_mountLog();
			if(getLogG()==null) 
				checkLogGenerator(null);
			getLogG().writeLog(msg.toString(),"","",classInfo,level);
		}catch(Exception e){}
	}

	public static synchronized void writeLog(HttpServletRequest _request, String msg, String level){
		final String classInfo= util_reflect.prepareClassInfo(new String[]{"bsController.java","bsControllerException.java","bsControllerMessageException.java"},new String[]{"writeLog","<init>","<init>"});
		try{
			auth_init aInit = null;
			try{
				aInit = (auth_init)_request.getSession().getAttribute(bsConstants.CONST_BEAN_$AUTHENTIFICATION);
			}catch(Exception e){
			}
			if(aInit!=null)
				getLogG().writeLog(_request,msg,aInit.get_user_ip(),aInit.get_matricola(),classInfo, level);
			else getLogG().writeLog(msg,"","",classInfo,level);
		}catch(Exception e){}
	}


	public static String writeLabel(String lang, String cd_mess, String def, HashMap<String,String> parameters) {
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

	public static String writeLabel(HttpServletRequest request, String cd_mess, String def, HashMap<String,String> parameters) {
		if(request==null || cd_mess==null)
			return message.decodeParameters(def,parameters);
		String lang="IT";
		try{
			final auth_init aInit = (auth_init)request.getSession().getAttribute(bsConstants.CONST_BEAN_$AUTHENTIFICATION);
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

		String id = request.getParameter(bsConstants.CONST_ID_$ACTION);
		boolean isController = (request.getRequestURI().indexOf("/Controller")>-1);
		if(id==null) id = request.getParameter(bsConstants.CONST_ID_MENU_MENUSOURCE);
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
			final i_action action_instance = getAction_config().actionFactory(id_action,request.getSession(),request.getSession().getServletContext());
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
		final i_log_generator logGenerator = checkLogGenerator(_logInit);
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
		final i_ProviderWrapper wrapperConfigMess = checkLoadMessage(null);
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
		final i_ProviderWrapper wrapperConfigMenu = checkLoadMenu(null);
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
		final i_ProviderWrapper wrapperConfigAction = checkLoadActions(null);
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
		final i_ProviderWrapper wrapperConfigAuth = checkLoadAuthentication(null);
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
		final i_ProviderWrapper wrapperConfigOrganization = checkLoadOrganization(null);
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
		if(servletContext==null)
			return "";
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
		final i_ProviderWrapper wrapperConfigAction = checkLoadActions(null);
		if(wrapperConfigAction!=null && wrapperConfigAction.getInstance()!=null){
			if(reInit){
				final String path = getAppInit().get_path_config()+CONST_XML_ACTIONS;
				final String dir = getAppInit().get_path_config()+CONST_XML_ACTIONS_FOLDER+"/";
				try{
					((load_actions)wrapperConfigAction.getInstance()).initProperties(path);
					((load_actions)wrapperConfigAction.getInstance()).initWithFOLDER(dir);
				}catch(bsControllerException je){}
			}
			if(!((load_actions)wrapperConfigAction.getInstance()).isReadOk_File()){
				if(!((load_actions)wrapperConfigAction.getInstance()).load_from_resources(servletContext)){
					final String path = getContextConfigPath(servletContext)+CONST_XML_ACTIONS;
					final String dir = getContextConfigPath(servletContext)+CONST_XML_ACTIONS_FOLDER+"/";
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
				final String path = getAppInit().get_path_config()+CONST_XML_ACTIONS;
				final String dir = getAppInit().get_path_config()+CONST_XML_ACTIONS_FOLDER+"/";
				action_config = getAction_config();
				try{
					action_config.initProperties(path);
					action_config.initWithFOLDER(dir);
				}catch(bsControllerException je){}
			}
			if(action_config!=null && !action_config.isReadOk_File()){
				if(!action_config.load_from_resources(servletContext)){
					final String path = getContextConfigPath(servletContext)+CONST_XML_ACTIONS;
					final String dir = getContextConfigPath(servletContext)+CONST_XML_ACTIONS_FOLDER+"/";
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
		final i_ProviderWrapper wrapperConfigMess = checkLoadMessage(null);
		if(wrapperConfigMess!=null && wrapperConfigMess.getInstance()!=null){
			if(reInit){
				final String path = getAppInit().get_path_config()+CONST_XML_MESSAGES;
				final String dir = getAppInit().get_path_config()+CONST_XML_MESSAGES_FOLDER+"/";
				((load_message)wrapperConfigMess.getInstance()).reimposta();
				try{
					((load_message)wrapperConfigMess.getInstance()).initProperties(path);
					((load_message)wrapperConfigMess.getInstance()).initWithFOLDER(dir);
				}catch(bsControllerException je){}
			}
	
			if(!((load_message)wrapperConfigMess.getInstance()).isReadOk_File()){
				if(!((load_message)wrapperConfigMess.getInstance()).load_from_resources(servletContext)){
					final String path = getContextConfigPath(servletContext)+CONST_XML_MESSAGES;
					final String dir = getContextConfigPath(servletContext)+CONST_XML_MESSAGES_FOLDER+"/";
					try{
						((load_message)wrapperConfigMess.getInstance()).initProperties(path);
						((load_message)wrapperConfigMess.getInstance()).initWithFOLDER(dir);
					}catch(bsControllerException je){}
				}
			}
		}else{
			if(reInit){
				final String path = getAppInit().get_path_config()+CONST_XML_MESSAGES;
				final String dir = getAppInit().get_path_config()+CONST_XML_MESSAGES_FOLDER+"/";
				mess_config = new load_message();
				try{
					mess_config.initProperties(path);
					mess_config.initWithFOLDER(dir);
				}catch(bsControllerException je){}
			}
	
			if(mess_config!=null && !mess_config.isReadOk_File()){
				if(!mess_config.load_from_resources(servletContext)){
					final String path = getContextConfigPath(servletContext)+CONST_XML_MESSAGES;
					final String dir = getContextConfigPath(servletContext)+CONST_XML_MESSAGES_FOLDER+"/";
					try{
						mess_config.initProperties(path);
						mess_config.initWithFOLDER(dir);
					}catch(bsControllerException je){}
				}
			}
		}
	}

	public static void reloadAuth_config(ServletContext servletContext) {
		final i_ProviderWrapper wrapperConfigAuth = checkLoadAuthentication(null);
		if(wrapperConfigAuth!=null && wrapperConfigAuth.getInstance()!=null){
			if(reInit){
				final String path = getAppInit().get_path_config()+CONST_XML_AUTHENTIFICATIONS;
				final String dir = getAppInit().get_path_config()+CONST_XML_AUTHENTIFICATIONS_FOLDER+"/";
				try{
					((load_authentication)wrapperConfigAuth.getInstance()).reimposta();
					((load_authentication)wrapperConfigAuth.getInstance()).initProperties(path);
					((load_authentication)wrapperConfigAuth.getInstance()).initWithFOLDER(dir);
				}catch(bsControllerException je){}
			}
			if(!((load_authentication)wrapperConfigAuth.getInstance()).isReadOk()){
				if(!((load_authentication)wrapperConfigAuth.getInstance()).load_from_resources(servletContext)){
					final String path = getContextConfigPath(servletContext)+CONST_XML_AUTHENTIFICATIONS;
					final String dir = getContextConfigPath(servletContext)+CONST_XML_AUTHENTIFICATIONS_FOLDER+"/";
					try{
						((load_authentication)wrapperConfigAuth.getInstance()).initProperties(path);
						((load_authentication)wrapperConfigAuth.getInstance()).initWithFOLDER(dir);
					}catch(bsControllerException je){}
				}
			}

		}else{
			if(reInit){
				final String path = getAppInit().get_path_config()+CONST_XML_AUTHENTIFICATIONS;
				final String dir = getAppInit().get_path_config()+CONST_XML_AUTHENTIFICATIONS_FOLDER+"/";
				auth_config = new load_authentication();
				try{
					auth_config.initProperties(path);
					auth_config.initWithFOLDER(dir);
				}catch(bsControllerException je){}
			}
			if(auth_config!=null && !auth_config.isReadOk()){
				if(!auth_config.load_from_resources(servletContext)){
					final String path = getContextConfigPath(servletContext)+CONST_XML_AUTHENTIFICATIONS;
					final String dir = getContextConfigPath(servletContext)+CONST_XML_AUTHENTIFICATIONS_FOLDER+"/";
					try{
						auth_config.initProperties(path);
						auth_config.initWithFOLDER(dir);
					}catch(bsControllerException je){}
				}
			}
		}
	}

	public static void reloadOrg_config(ServletContext servletContext) {
		final i_ProviderWrapper wrapperConfigOrganization = checkLoadOrganization(null);
		if(wrapperConfigOrganization!=null && wrapperConfigOrganization.getInstance()!=null){
			if(reInit){
				final String path = getAppInit().get_path_config()+CONST_XML_ORGANIZATION;
				((load_organization)wrapperConfigOrganization.getInstance()).reimposta();
				try{
					((load_organization)wrapperConfigOrganization.getInstance()).initProperties(path);
				}catch(bsControllerException je){}
			}
			if(!((load_organization)wrapperConfigOrganization.getInstance()).isReadOk()){
				if(!((load_organization)wrapperConfigOrganization.getInstance()).load_from_resources(servletContext)){
					final String path = getContextConfigPath(servletContext)+CONST_XML_ORGANIZATION;
					try{
						((load_organization)wrapperConfigOrganization.getInstance()).initProperties(path);
					}catch(bsControllerException je){}
				}
			}
		}else{
			if(reInit){
				final String path = getAppInit().get_path_config()+CONST_XML_ORGANIZATION;
				org_config = new load_organization();
				try{
					org_config.initProperties(path);
				}catch(bsControllerException je){}
			}
			if(org_config!=null && !org_config.isReadOk()){
				if(!org_config.load_from_resources(servletContext)){
					final String path = getContextConfigPath(servletContext)+CONST_XML_ORGANIZATION;
					try{
						org_config.initProperties(path);
					}catch(bsControllerException je){}
				}
			}
		}
	}


	public static void reloadMenu_config(ServletContext servletContext, i_menu_element ime) {
		final i_ProviderWrapper wrapperConfigMenu = checkLoadMenu(null);
		if(wrapperConfigMenu!=null && wrapperConfigMenu.getInstance()!=null){
			

			if(reInit){
				wrapperConfigMenu.setInstance(new load_menu(ime));
				try{
					((load_menu)wrapperConfigMenu.getInstance()).init();
				}catch(bsControllerException je){}
				if(!((load_menu)wrapperConfigMenu.getInstance()).isReadOk()) 
					((load_menu)wrapperConfigMenu.getInstance()).load_from_resources();
	
				if(!((load_menu)wrapperConfigMenu.getInstance()).isReadOk()){
					final String path = getAppInit().get_path_config()+CONST_XML_MENU;
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
						final String path = getContextConfigPath(servletContext)+CONST_XML_MENU;
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
					final String path = getAppInit().get_path_config()+CONST_XML_MENU;
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
						final String path = getContextConfigPath(servletContext)+CONST_XML_MENU;
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
		if(servletContext!=null && (logInit.get_LogPath()==null || logInit.get_LogPath().equals(""))){
			logInit.init();
			final String new_path =util_classes.getPathWebContent(servletContext)+"/"+CONST_LOG_FOLDER+"/";
			logInit.set_LogPath(new_path);
			
			final i_log_generator logGenerator = checkLogGenerator(logInit);
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
		if(request==null)
			return auth;
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
		final db_connection DBConnection = new db_connection();
		return DBConnection.getContent(getDbInit());
	}

	public static Connection getDBConnection(String id) throws Exception{
		final db_connection DBConnection = new db_connection();
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
			final String contentType = req.getContentType();
			if (contentType != null && contentType.indexOf("multipart/form-data") != -1) {
				if(file==null){
					in = new DataInputStream(req.getInputStream());
					final int formDataLength = req.getContentLength();
					final byte dataBytes[] = new byte[formDataLength];
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

				final String check = "Content-Disposition: form-data; name=\""+key+"\"";

				final int pos = file.indexOf(check);

				if(pos>-1){
					final int pos1 = file.indexOf("-----------------------------",pos);
					if(pos1>-1){
						return 
								file.substring(pos+check.length(),pos1).replace('\n',' ').replace('\r',' ').trim();
					}
				}
			}

		}catch(Exception e){
			new bsControllerException(e,iStub.log_ERROR);

			return null;
		}
		return null;
	}

    public static String encrypt(String plaintext) throws NoSuchAlgorithmException, UnsupportedEncodingException{
    	MessageDigest md = null;
        md = MessageDigest.getInstance("SHA");
        md.update(plaintext.getBytes("UTF-8"));
        final byte raw[] = md.digest();
//		BASE64Encoder encoder = new BASE64Encoder();
        final String hash = DatatypeConverter.printBase64Binary(raw);
//		encoder = null;
        return hash;
    }

// algorithm: SHA, SHA-224, SHA-256, SHA-384, and SHA-512
    public static String encrypt(String plaintext, String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException{
    	MessageDigest md = null;
        md = MessageDigest.getInstance(algorithm);
        md.update(plaintext.getBytes("UTF-8"));
        final byte raw[] = md.digest();
//		BASE64Encoder encoder = new BASE64Encoder();
        final String hash = DatatypeConverter.printBase64Binary(raw);
//		encoder = null;
        return hash;
    }

 // algorithm: SHA, SHA-224, SHA-256, SHA-384, and SHA-512
    public static String encrypt(String plaintext, String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException{
    	MessageDigest md = null;
        md = MessageDigest.getInstance(algorithm,provider);
        md.update(plaintext.getBytes("UTF-8"));
        final byte raw[] = md.digest();
//		BASE64Encoder encoder = new BASE64Encoder();
        final String hash = DatatypeConverter.printBase64Binary(raw);
//		encoder = null;
        return hash;
    }

//*********************************************************
	static public int calc(Object oldObj){

		ObjectOutputStream oos = null;
		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
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
		final I_StatisticProvider sProvider = checkStatisticProvider();
		if(sProvider!=null)
			sProvider.addStatictic(stat);
	}

	public static I_StatisticProvider getStatisticProvider(){
		return checkStatisticProvider();
	}

	
	public static I_TLinkedProvider checkTLinkedProvider(){
		if(!canBeProxed){
			if(tLinkedProvider==null)
				tLinkedProvider = new TLinkedProvider_Simple();
			return tLinkedProvider;
		}
		
		if(tLinkedProvider!=null)
			return tLinkedProvider;
		
		I_TLinkedProvider retTLinkedProvider=null;
		
			if(getAppInit().get_temporary_linked_provider()==null || getAppInit().get_temporary_linked_provider().equals("")){
				if(getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equals("false")){
					try{
						retTLinkedProvider = (I_TLinkedProvider)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  app_init.id_temporary_linked_provider,TLinkedProvider_Simple.class.getName(), null);
					}catch(Exception e){
					}
				}
				if(retTLinkedProvider==null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equals("false")){
					try{
						retTLinkedProvider = (I_TLinkedProvider)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  app_init.id_temporary_linked_provider,TLinkedProvider_Simple.class.getName(), null);
					}catch(Exception e){
					}
				}
				if(retTLinkedProvider==null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equals("false")){
					try{
						retTLinkedProvider = (I_TLinkedProvider)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  app_init.id_temporary_linked_provider,I_TLinkedProvider.class.getName(), null);
					}catch(Exception e){
					}
				}
				checkDefaultProvider(null);
				if(retTLinkedProvider==null && getCdiDefaultProvider()!=null){
					try{
						retTLinkedProvider = (I_TLinkedProvider)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  app_init.id_temporary_linked_provider,TLinkedProvider_Simple.class.getName(), null);
					}catch(Exception e){
					}
				}
				if(retTLinkedProvider==null && getEjbDefaultProvider()!=null){
					try{
						retTLinkedProvider = (I_TLinkedProvider)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  app_init.id_temporary_linked_provider,I_TLinkedProvider.class.getName(), null);
					}catch(Exception e){
					}
				}

				if(retTLinkedProvider==null){
					tLinkedProvider = new TLinkedProvider_Simple();
					return tLinkedProvider;
				}
			}else{
				if(getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equals("false")){
					try{
						retTLinkedProvider = (I_TLinkedProvider)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  app_init.id_temporary_linked_provider,getAppInit().get_statistic_provider(), null);
					}catch(Exception e){
					}
				}
				if(retTLinkedProvider==null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equals("false")){
					try{
						retTLinkedProvider = (I_TLinkedProvider)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  app_init.id_temporary_linked_provider,getAppInit().get_statistic_provider(), null);
					}catch(Exception e){
					}
				}
				if(retTLinkedProvider==null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equals("false")){
					try{
						retTLinkedProvider = (I_TLinkedProvider)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  app_init.id_temporary_linked_provider,getAppInit().get_statistic_provider(), null);
					}catch(Exception e){
					}
				}
				checkDefaultProvider(null);
				if(retTLinkedProvider==null && getCdiDefaultProvider()!=null){
					try{
						retTLinkedProvider = (I_TLinkedProvider)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(), app_init.id_temporary_linked_provider,getAppInit().get_statistic_provider(), null);
					}catch(Exception e){
					}
				}
				if(retTLinkedProvider==null && getEjbDefaultProvider()!=null){
					try{
						retTLinkedProvider = (I_TLinkedProvider)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(), app_init.id_temporary_linked_provider,getAppInit().get_statistic_provider(), null);
					}catch(Exception e){
					}
				}

				if(retTLinkedProvider==null){
					try{
						retTLinkedProvider = (I_TLinkedProvider)Class.forName(getAppInit().get_statistic_provider()).newInstance();
					}catch(Exception e){
						writeLog("ERROR instance Statistic Provider:"+getAppInit().get_statistic_provider()+" Will be use embeded stack.",iStub.log_ERROR);
					}
				}
				if(retTLinkedProvider==null){
					tLinkedProvider = new TLinkedProvider_Simple();
					return tLinkedProvider;
				}

			
		}
/*			
		if(retTLinkedProvider==null){
			tLinkedProvider = new TLinkedProvider_Simple();
			return tLinkedProvider;
		}
*/		
		return retTLinkedProvider;

	}
	
	public static I_TLinkedProvider getTLinkedProvider(){
		return checkTLinkedProvider();
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
				Enumeration<?> en = request.getSession().getServletContext().getAttributeNames();
				while(en.hasMoreElements()){
					final String key = (String)en.nextElement();
					final int dim=calc(request.getSession().getServletContext().getAttribute(key));
					log_detail+="          ["+key+"]="+dim+"\n";
					only_app+=dim;
				}
				total+=only_app;

				log_detail+="     ClassHidra Local Container: \n";
				en = local_container.keys();
				while(en.hasMoreElements()){
					final String key = (String)en.nextElement();
					final int dim=calc(local_container.get(key));
					log_detail+="          ["+key+"]="+dim+"\n";
					only_app+=dim;
				}
				total+=only_app;


				log_detail+="     Session: \n";
				en = request.getSession().getAttributeNames();
				while(en.hasMoreElements()){
					final String key = (String)en.nextElement();
					final int dim=calc(request.getSession().getAttribute(key));
					log_detail+="          ["+key+"]="+dim+"\n";
					only_session+=dim;
				}
				total+=only_session;



				log_detail+="     Request Attributes: \n";
				en = request.getAttributeNames();
				while(en.hasMoreElements()){
					final String key = (String)en.nextElement();
					final int dim=calc(request.getAttribute(key));
					log_detail+="          ["+key+"]="+dim+"\n";
					total+=dim;
				}

				log_detail+="     Request Parameters: \n";
				en = request.getParameterNames();
				while(en.hasMoreElements()){
					final String key = (String)en.nextElement();
					final int dim=calc(request.getParameter(key));
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
		final i_ProviderWrapper wrapperConfigUsers = checkLoadUsers(null);
		if(wrapperConfigUsers!=null && wrapperConfigUsers.getInstance()!=null){
			return wrapperConfigUsers.getInstance();
		}else
			return user_config;
	}

	public static void setUser_config(Object new_load_users) {
		final i_ProviderWrapper wrapperConfigUsers = checkLoadUsers(null);
		if(wrapperConfigUsers!=null)
			wrapperConfigUsers.setInstance(new_load_users);
		else	
			user_config = new_load_users;
	}

	public static load_users checkUser_config4load_users(){
		final i_ProviderWrapper wrapperConfigUsers = checkLoadUsers(null);
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
			final long now = System.currentTimeMillis();
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
			final String path = servletContext.getResource("/").getPath();
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
		
		final String bean_id = bsConstants.CONST_BEAN_$USERS_CONFIG;

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
		
		final String bean_id = bsConstants.CONST_BEAN_$ACTION_CONFIG;

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
		
		final String bean_id = bsConstants.CONST_BEAN_$MESS_CONFIG;

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
		
		final String bean_id = bsConstants.CONST_BEAN_$AUTH_CONFIG;

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
		
		final String bean_id = bsConstants.CONST_BEAN_$ORGANIZATION_CONFIG;

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
		
		final String bean_id = bsConstants.CONST_BEAN_$MENU_CONFIG;

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

	public static Map<String,i_bean> checkOnlySession(HttpServletRequest request){
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
		
			if(wrapper!=null) {
				final Object fromWrapper = wrapper.getInstance();
				if(fromWrapper!=null && fromWrapper instanceof Map<?,?>) {
					@SuppressWarnings("unchecked")
					Map<String,i_bean> fromWrapper2 = (Map<String,i_bean>)fromWrapper;
					return fromWrapper2;
				}

			}

		return null;
	}

	public static i_bean getFromOnlySession(String id,HttpServletRequest request){
		Map<String,i_bean> instance = checkOnlySession(request);

		if(instance==null) {
			@SuppressWarnings("unchecked")
			Map<String,i_bean> attribute = 
					 (Map<String,i_bean>)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
			instance = attribute; 		
		}if(instance==null){
			instance = new HashMap<String, i_bean>();
			request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,instance);
		}
		return instance.get(id);
	}
	
	public static boolean setToOnlySession(String id,i_bean obj,HttpServletRequest request){
		try{
			Map<String,i_bean> instance = checkOnlySession(request);

			if(instance==null) {
				@SuppressWarnings("unchecked")
				Map<String,i_bean> attribute = 
						 (Map<String,i_bean>)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
				instance = attribute; 		
			}
			if(instance==null){
				instance = new HashMap<String, i_bean>();
				request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,instance);
			}
			if(obj!=null){
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

	public static i_bean removeFromOnlySession(String id,HttpServletRequest request){
		Map<String,i_bean> instance = checkOnlySession(request);
		if(instance!=null)
			util_provider.destroyInstanceFromProvider(instance.get(id), null);

		if(instance==null) {
			@SuppressWarnings("unchecked")
			Map<String,i_bean> attribute = 
					 (Map<String,i_bean>)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
			instance = attribute; 		
		}if(instance==null){
				instance = new HashMap<String, i_bean>();
			request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,instance);
		}
		return instance.remove(id);
	}

	public static Map<String,i_bean> getOnlySession(HttpServletRequest request){
		Map<String,i_bean> instance = checkOnlySession(request);

		if(instance==null) {
			@SuppressWarnings("unchecked")
			Map<String,i_bean> attribute = 
					 (Map<String,i_bean>)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
			instance = attribute; 		
		}
		if(instance==null){
			instance = new HashMap<String, i_bean>();
			request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,instance);
		}
		return instance;
	}

	public static boolean setOnlySession(Map<String,i_bean> newInstance, HttpServletRequest request){

		if(newInstance == null)
			return false;

		Map<String,i_bean> instance = checkOnlySession(request);

		if(instance==null) {
			@SuppressWarnings("unchecked")
			Map<String,i_bean> attribute = 
					 (Map<String,i_bean>)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
			instance = attribute; 		
		}
		if(instance==null){
			request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,newInstance);
		}else{
			instance.clear();
			instance.putAll(newInstance);
		}
		return true;
	}


	public static void clearOnlySession(HttpServletRequest request){
		Map<String,i_bean> instance = checkOnlySession(request);
		if(instance!=null){
			Iterator<i_bean> it = instance.values().iterator();
			while(it.hasNext())
				util_provider.destroyInstanceFromProvider(it.next(), null);
		}
		if(instance==null) {
			@SuppressWarnings("unchecked")
			Map<String,i_bean> attribute = 
					 (Map<String,i_bean>)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
			instance = attribute; 		
		}
		if(instance==null){
			instance = new HashMap<String, i_bean>();
			request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,instance);
		}
		instance.clear();

	}
	
	public static Map<String,i_bean> checkOnlyServletContext(HttpServletRequest request){
		i_ProviderWrapper wrapper = null;
		if(!canBeProxed)
			return null;
		
		String bean_id = bsConstants.CONST_BEAN_$SERVLETCONTEXT;
		if(!getAction_config().getInstance_servletcontext().equals("") && !getAction_config().getInstance_servletcontext().equalsIgnoreCase("false"))
			bean_id = getAction_config().getInstance_servletcontext();

			if(getAction_config()!=null && getAction_config().getProvider()!=null && !getAction_config().getProvider().equals("") && !getAction_config().getProvider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAction_config().getProvider(),  bean_id, getAction_config().getInstance_servletcontext(), (request==null)?null:request.getSession().getServletContext());
				}catch(Exception e){
				}
			} 
			if(wrapper==null && getAppInit()!=null && getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  bean_id, getAction_config().getInstance_servletcontext(), (request==null)?null:request.getSession().getServletContext());
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  bean_id, getAction_config().getInstance_servletcontext(), (request==null)?null:request.getSession().getServletContext());
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  bean_id, getAction_config().getInstance_servletcontext(), (request==null)?null:request.getSession().getServletContext());
				}catch(Exception e){
				}
			}
			checkDefaultProvider((request==null)?null:request.getSession().getServletContext());
			if(wrapper==null && getCdiDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  bean_id, getAction_config().getInstance_servletcontext(), (request==null)?null:request.getSession().getServletContext());
				}catch(Exception e){
				}
			}
//			if(wrapper==null && getEjbDefaultProvider()!=null){
//				try{
//				
//					wrapper = (i_ProviderWrapper)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION+"$wrapper");
//					if(wrapper!=null){
//						try{
//							wrapper.getInstance();
//						}catch(Exception e){
//							new bsControllerException(e, iStub.log_ERROR);
//							wrapper=null;
//							request.getSession().removeAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION+"$wrapper");
//						}
//					}
//					if(wrapper==null){
//						wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getEjbDefaultProvider(),  bean_id.replace("$", "ejb_"), getAction_config().getInstance_servletcontext(), (request==null)?null:request.getSession().getServletContext());
//						if(wrapper!=null && request!=null && wrapper.getInfo_context()!=null && wrapper.getInfo_context().isStateful())
//							request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION+"$wrapper", wrapper);
//					}
//				}catch(Exception e){
//				}
//			}
		
			if(wrapper!=null) {
				@SuppressWarnings("unchecked")
				Map<String,i_bean> instance = (Map<String,i_bean>)wrapper.getInstance();
				return instance;
			}

		return null;
	}
	
	public static Map<String,i_bean> checkOnlyServletContext(ServletContext servletContext){
		i_ProviderWrapper wrapper = null;
		if(!canBeProxed)
			return null;
		
		String bean_id = bsConstants.CONST_BEAN_$SERVLETCONTEXT;
		if(!getAction_config().getInstance_servletcontext().equals("") && !getAction_config().getInstance_servletcontext().equalsIgnoreCase("false"))
			bean_id = getAction_config().getInstance_servletcontext();

			if(getAction_config()!=null && getAction_config().getProvider()!=null && !getAction_config().getProvider().equals("") && !getAction_config().getProvider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAction_config().getProvider(),  bean_id, getAction_config().getInstance_servletcontext(), servletContext);
				}catch(Exception e){
				}
			} 
			if(wrapper==null && getAppInit()!=null && getAppInit().get_context_provider()!=null && !getAppInit().get_context_provider().equals("") && !getAppInit().get_context_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_context_provider(),  bean_id, getAction_config().getInstance_servletcontext(), servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_cdi_provider()!=null && !getAppInit().get_cdi_provider().equals("") && !getAppInit().get_cdi_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_cdi_provider(),  bean_id, getAction_config().getInstance_servletcontext(), servletContext);
				}catch(Exception e){
				}
			}
			if(wrapper==null && getAppInit()!=null && getAppInit().get_ejb_provider()!=null && !getAppInit().get_ejb_provider().equals("") && !getAppInit().get_ejb_provider().equalsIgnoreCase("false")){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getAppInit().get_ejb_provider(),  bean_id, getAction_config().getInstance_servletcontext(), servletContext);
				}catch(Exception e){
				}
			}
			checkDefaultProvider(servletContext);
			if(wrapper==null && getCdiDefaultProvider()!=null){
				try{
					wrapper = (i_ProviderWrapper)util_provider.getBeanFromObjectFactory(getCdiDefaultProvider(),  bean_id, getAction_config().getInstance_servletcontext(), servletContext);
				}catch(Exception e){
				}
			}
	
			if(wrapper!=null) {
				@SuppressWarnings("unchecked")
				Map<String,i_bean> instance = (Map<String,i_bean>)wrapper.getInstance();
				return instance;
			}

		return null;
	}

	
	public static i_bean getFromOnlyServletContext(String id, HttpServletRequest request){
		Map<String,i_bean> instance = checkOnlyServletContext(request);

		if(instance==null) {
			@SuppressWarnings("unchecked")
			Map<String,i_bean> attribute = 
					 (Map<String,i_bean>)request.getSession().getServletContext().getAttribute(bsConstants.CONST_BEAN_$SERVLETCONTEXT);
			instance = attribute;		
		}
		if(instance==null){
			instance = new HashMap<String, i_bean>();
			request.getSession().getServletContext().setAttribute(bsConstants.CONST_BEAN_$SERVLETCONTEXT,instance);
		}
		return instance.get(id);
	}
	
	
	public static i_bean getFromOnlyServletContext(String id, ServletContext servletContext){
		Map<String,i_bean> instance = checkOnlyServletContext(servletContext);

		if(instance==null) {
			@SuppressWarnings("unchecked")
			Map<String,i_bean> attribute = 
					 (Map<String,i_bean>)servletContext.getAttribute(bsConstants.CONST_BEAN_$SERVLETCONTEXT);
			instance = attribute;		
		}		
		if(instance==null){
			instance = new HashMap<String, i_bean>();
			servletContext.setAttribute(bsConstants.CONST_BEAN_$SERVLETCONTEXT,instance);
		}
		return instance.get(id);
	}
	
	public static boolean setToOnlyServletContext(String id,i_bean obj,HttpServletRequest request){
		try{
			Map<String,i_bean> instance = checkOnlyServletContext(request);

			if(instance==null) {
				@SuppressWarnings("unchecked")
				Map<String,i_bean> attribute = 
						 (Map<String,i_bean>)request.getSession().getServletContext().getAttribute(bsConstants.CONST_BEAN_$SERVLETCONTEXT);
				instance = attribute;		
			}
			if(instance==null){
				instance = new HashMap<String, i_bean>();
				request.getSession().getServletContext().setAttribute(bsConstants.CONST_BEAN_$SERVLETCONTEXT,instance);
			}
			if(obj!=null){
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
	
	public static boolean setToOnlyServletContext(String id,i_bean obj,ServletContext servletContext){
		try{
			Map<String,i_bean> instance = checkOnlyServletContext(servletContext);

			if(instance==null) {
				@SuppressWarnings("unchecked")
				Map<String,i_bean> attribute = 
						 (Map<String,i_bean>)servletContext.getAttribute(bsConstants.CONST_BEAN_$SERVLETCONTEXT);
				instance = attribute;		
			}	
			if(instance==null){
				instance = new HashMap<String, i_bean>();
				servletContext.setAttribute(bsConstants.CONST_BEAN_$SERVLETCONTEXT,instance);
			}
			if(obj!=null){
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
		final info_navigation nav = getInfoNavigation(request);
	
		if(nav==null || id==null)
			return nav;
		return nav.find(id);
	}

	public static boolean setToInfoNavigation(info_navigation nav,HttpServletRequest request){
		try{

			final i_ProviderWrapper inw = checkInfoNavigationWrapper(request);

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

	public static info_navigation getInfoNavigation(HttpServletRequest request){
		info_navigation nav = null;
		final i_ProviderWrapper inw = checkInfoNavigationWrapper(request);
		if(inw!=null)
			nav = (info_navigation)inw.getInstance();

		if(nav==null){
			try{
				nav = (info_navigation)request.getSession().getAttribute(bsConstants.CONST_BEAN_$NAVIGATION);
			}catch(Exception e){
			}
		}
		return nav;
	}

	
	public static Map<String,Object> checkLocalContainer(){
		if(!canBeProxed)
			return local_container;
		
		final i_ProviderWrapper wrapperConfigAction = checkLoadActions(null);
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
						@SuppressWarnings("unchecked")
						Map<String,Object> instance = (Map<String,Object>)wrapper.getInstance();
						return instance;
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
						@SuppressWarnings("unchecked")
						Map<String,Object> instance = (Map<String,Object>)wrapper.getInstance();
						return instance;
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
						@SuppressWarnings("unchecked")
						Map<String,Object> instance = (Map<String,Object>)wrapper.getInstance();
						return instance;
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
						@SuppressWarnings("unchecked")
						Map<String,Object> instance = (Map<String,Object>)wrapper.getInstance();
						return instance;
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
						if(wrapper!=null) {
							@SuppressWarnings("unchecked")
							Map<String,Object> instance = (Map<String,Object>)wrapper.getInstance();
							return instance;
						}
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
						if(wrapper!=null) {
							@SuppressWarnings("unchecked")
							Map<String,Object> instance = (Map<String,Object>)wrapper.getInstance();
							return instance;
						}
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
		
		final i_ProviderWrapper wrapperConfigAction = checkLoadActions(null);
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
		final Map<String,Object> container = checkLocalContainer();
		if(container!=null)
			return container.get(key);
		if(local_container!=null)
			return local_container.get(key);
		return null;
	}

	public static Object removeFromLocalContainer(String key) {
		final Map<String,Object> container = checkLocalContainer();
		if(container!=null){
			util_provider.destroyInstanceFromProvider(container.get(key), null);
			return container.remove(key);
		}
		if(local_container!=null)
			return local_container.remove(key);
		return null;
	}

	public static void setToLocalContainer(String key, Object value) {
		final Map<String,Object> container = checkLocalContainer();
		if(container!=null){
			if(container instanceof ConcurrentHashMap<?,?>)
				((ConcurrentHashMap<String,Object>)container).putIfAbsent(key,value);
			else
				container.put(key,value);
		}
		if(local_container!=null)
			local_container.putIfAbsent(key,value);
	}

	public static void putToLocalContainer(String key, Object value) {
		final Map<String,Object> container = checkLocalContainer();
		if(container!=null)
			container.put(key,value);
		if(local_container!=null)
			local_container.put(key,value);
	}

	public static Map<String,Object> getLocalContainer(){
		final Map<String,Object> container = checkLocalContainer();
		if(container!=null)
			return container;
		if(local_container!=null)
			return local_container;
		return null;
	}

	public static boolean setLocalContainer(Map<String,Object> newInstance){
		if(newInstance==null)
			return false;
		final Map<String,Object> container = checkLocalContainer();
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
		final Map<String,Object> container = checkLocalContainer();
		if(container!=null){
			final Iterator<Object> it = container.values().iterator();
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
		@SuppressWarnings("unchecked")
		Map<String,Object> fromLocalContainer = 
				 (Map<String,Object>)getFromLocalContainer(bsConstants.CONST_CONTAINER_STREAMS_INSTANCE);
		Map<String,Object> container_streams_instance = fromLocalContainer;
		if(container_streams_instance==null){
			container_streams_instance = new HashMap<String, Object>();
			bsController.putToLocalContainer(bsConstants.CONST_CONTAINER_STREAMS_INSTANCE, container_streams_instance);				
		}
		return (i_stream)container_streams_instance.get(id_stream);
	}
	
	public static void putStreamIntoContainer(String id_stream, i_stream rStream){
		if(id_stream==null)
			return;
		@SuppressWarnings("unchecked")
		Map<String,Object> fromLocalContainer = 
				 (Map<String,Object>)getFromLocalContainer(bsConstants.CONST_CONTAINER_STREAMS_INSTANCE);
		Map<String,Object> container_streams_instance = fromLocalContainer;
		if(container_streams_instance==null){
			container_streams_instance = new HashMap<String, Object>();
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

	public static boolean isInitialized() {
		return isInitialized(null,null,null);
	}
	
	public static boolean isInitialized(Properties appInitProperty, Map<String,Properties> othersProperties) {
		return isInitialized(appInitProperty, othersProperties, null);
	}
	
	public static boolean isInitialized(Properties appInitProperty, Map<String,Properties> othersProperties, ServletContext servletContext) {
		if(!initialized){
			try{
				initialized = true;
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
				
				if(appInitProperty!=null && appInitProperty.size()>0)
					appInit.init(appInitProperty);
					
					
					
				checkDefaultProvider(null);

				boolean	loadModeAsThread=false;
				try{
					if(appInit.get_load_res_mode().equalsIgnoreCase("thread")) loadModeAsThread=true;
				}catch(Exception e){
				}
				if(loadModeAsThread)
					new LoaderConfigThreadProcessS(othersProperties,servletContext).start();
				else
					loadOnInitS(othersProperties,servletContext);

				
				if(stat!=null){
					stat.setFt(new Date());
					putToStatisticProvider(stat);
				}
				
			}catch(Exception e){
				new bsControllerException(e, iStub.log_ERROR);
			}catch(Throwable th){
				new bsControllerException(th, iStub.log_ERROR);
			}
		}
		return initialized;
	}
	
	public static void clean(){
		logInit = null;
		dbInit = null;
		appInit = null;
		
		logG = null;
		action_config = null;
		mess_config = null;
		user_config = null;
		auth_config = null;
		org_config = null;
		menu_config = null;
		idApp = null;
		
		statisticProvider = null;
		
		cdiDefaultProvider = null;
		ejbDefaultProvider = null;

		if(local_container!=null)
			local_container.clear();


		reInit=false;
		canBeProxed=true;
		isInitDefProfider=false;
		initialized = false;
		loadedonstartup = false;
	}

	public static boolean isLoadedonstartup() {
		return loadedonstartup;
	}

	public static i_provider getTagComponentRender() {
		return tagComponentRender;
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
