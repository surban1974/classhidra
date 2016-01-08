package it.classhidra.core.tool.util;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.i_provider;
import it.classhidra.core.controller.i_stream;
import it.classhidra.core.controller.i_transformation;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.controller.info_context;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;


public class util_provider {

	public static i_provider checkDeafaultCdiProvider(String cdi_jndi_name, ServletContext servletContext){
//		i_provider provider  = null;
		try{
			Class c_provider = Class.forName("it.classhidra.plugin.provider.DependencyInjectionProvider");
			try{
				Method m_getInstance = c_provider.getDeclaredMethod("checkInitialContext", new Class[]{String.class, ServletContext.class});
				Object instance = m_getInstance.invoke(null, new Object[]{cdi_jndi_name, servletContext});
				if(instance instanceof Boolean && ((Boolean)instance).booleanValue())
					return (i_provider)c_provider.newInstance();
			}catch(Exception e){				
			}
		}catch (Exception e) {
			return null;
		}catch (Throwable e) {
			return null;
		}
		return null;
	}
	
	public static i_provider checkDeafaultEjbProvider(String ejb_jndi_name, ServletContext servletContext){
//		i_provider provider  = null;
		try{
			Class c_provider = Class.forName("it.classhidra.plugin.provider.EjbProvider");
			try{
				Method m_getInstance = c_provider.getDeclaredMethod("checkInitialContext", new Class[]{String.class, ServletContext.class});
				Object instance = m_getInstance.invoke(null, new Object[]{ejb_jndi_name, servletContext});
				if(instance instanceof Boolean && ((Boolean)instance).booleanValue())
					return (i_provider)c_provider.newInstance();
			}catch(Exception e){				
			}
		}catch (Exception e) {
			return null;
		}catch (Throwable e) {
			return null;
		}
		return null;
	}	
	
	public static boolean clearkDeafaultEjbProviderNamingMap(){
//		i_provider provider  = null;
		try{
			Class c_provider = Class.forName("it.classhidra.plugin.provider.EjbProvider");
			try{
				Method m_getInstance = c_provider.getDeclaredMethod("clearNamingMap", new Class[]{});
				Object instance = m_getInstance.invoke(null, new Object[]{});
				if(instance instanceof Boolean && ((Boolean)instance).booleanValue())
					return ((Boolean)instance).booleanValue();
			}catch(Exception e){				
			}
		}catch (Exception e) {
			return false;
		}catch (Throwable e) {
			return false;
		}
		return false;
	}	
	
	
	public static Object getBeanFromObjectFactory(String id_provider, String id_bean, String class_bean, ServletContext servletContext){
		if(id_bean==null || id_bean.equals("")) return null;
		Object instance = null;
		if(id_provider!=null && !id_provider.equals("")){
				instance = getBeanFromProvider(id_provider, id_bean, class_bean, servletContext);
			if(instance==null)
				instance = getBeanFromProvider(bsConstants.CONST_PROVIDER_PATH+id_provider, id_bean, class_bean, servletContext);
			}
		if(id_provider!=null && bsController.getCdiDefaultProvider()!=null && id_provider.equals(bsController.getCdiDefaultProvider().getClass().getName()))
			return instance;
		
		if(instance==null && bsController.getCdiDefaultProvider()!=null)
			instance = getBeanFromProvider(bsController.getCdiDefaultProvider(), id_bean, class_bean, servletContext);

		if(instance==null && bsController.getEjbDefaultProvider()!=null)
			instance = getBeanFromProvider(bsController.getEjbDefaultProvider(), id_bean, class_bean, servletContext);
		
		return instance;
	}
	
	public static Object getBeanFromObjectFactory(i_provider _provider, String id_bean, String class_bean, ServletContext servletContext){
		if(id_bean==null || id_bean.equals("") || _provider==null) return null;
		Object instance = null;
		instance = getBeanFromProvider(_provider, id_bean, class_bean, servletContext);

		return instance;
	}
	

	public static Object getBeanFromObjectFactory(String[] providers, String id_bean, String class_bean, ServletContext servletContext){
		Object instance = null;
		boolean isBreak=false;
		if(providers!=null && providers.length>0){
			int i=0;
			while(instance==null && i<providers.length){
				if(providers[i]!=null && !providers[i].equals("") && !providers[i].equalsIgnoreCase("false")){
					instance = getBeanFromProvider(providers[i], id_bean, class_bean, servletContext);
					if(instance==null)
						instance = getBeanFromProvider(bsConstants.CONST_PROVIDER_PATH+providers[i], id_bean, class_bean, servletContext);
				}else if(providers[i]!=null && providers[i].equalsIgnoreCase("false")){
					isBreak=true;
					break;			
				}
				i++;
			}		
		}
		if(!isBreak && instance==null && bsController.getCdiDefaultProvider()!=null)
			instance = getBeanFromProvider(bsController.getCdiDefaultProvider(), id_bean, class_bean, servletContext);

		if(instance==null && bsController.getEjbDefaultProvider()!=null)
			instance = getBeanFromProvider(bsController.getEjbDefaultProvider(), id_bean, class_bean, servletContext);
		
		return instance;
	}

	public static Object getBeanFromProvider(String id_provider, String id_bean, String class_bean, ServletContext servletContext){
		try{		
			i_provider provider  = null;
			try{
				Class c_provider = Class.forName(id_provider);
				try{
					Method m_getInstance = c_provider.getDeclaredMethod("getInstance", new Class[]{String.class,String.class,ServletContext.class});
					Object instance = m_getInstance.invoke(null, new Object[]{id_bean,class_bean,servletContext});
					return instance;
				}catch(Exception e){				
				}
				provider = (i_provider)Class.forName(id_provider).newInstance();
			}catch (Exception e) {
				return null;
			}
			if(provider==null) 
				return null;
			provider.set_context(servletContext);
			return 
				provider.get_bean(id_bean);
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}catch (Throwable t) {
			new bsControllerException(t,iStub.log_DEBUG);
		}
		return null;
	}
	
	public static Object getBeanFromProvider(i_provider _provider, String id_bean, String class_bean, ServletContext servletContext){
		if(_provider==null)
			return null;
		try{		
			i_provider provider  = null;
			try{
				Class c_provider = _provider.getClass();
				try{
					Method m_getInstance = c_provider.getDeclaredMethod("getInstance", new Class[]{String.class,String.class,ServletContext.class});
					Object instance = m_getInstance.invoke(null, new Object[]{id_bean,class_bean,servletContext});
					return instance;
				}catch(Exception e){				
				}
				provider = _provider;
			}catch (Exception e) {
				return null;
			}
			provider.set_context(servletContext);
			return 
				provider.get_bean(id_bean);
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}catch (Throwable t) {
			new bsControllerException(t,iStub.log_DEBUG);
		}
		return null;
	}
	
	
	public static Object getInstanceFromProvider(String[] providers, String id_bean, String class_name, ServletContext servletContext) throws Exception{
		Object instance = null;
		boolean isBreak=false;

		if(providers!=null && providers.length>0){
			int i=0;
			while(instance==null && i<providers.length){
				if(providers[i]!=null && !providers[i].equals("") && !providers[i].equalsIgnoreCase("false"))
					instance = getBeanFromObjectFactory(providers[i], id_bean, class_name, servletContext);
				else if(providers[i]!=null && providers[i].equalsIgnoreCase("false")){
					isBreak=true;
					break;			
				}
				i++;
			}
		}
		if(!isBreak && instance==null && bsController.getCdiDefaultProvider()!=null)
			instance = getBeanFromObjectFactory(bsController.getCdiDefaultProvider(), id_bean, class_name, servletContext);

		if(instance==null && bsController.getEjbDefaultProvider()!=null)
			instance = getBeanFromObjectFactory(bsController.getEjbDefaultProvider(), id_bean, class_name, servletContext);
		
		if(instance==null && class_name!=null)
			instance = Class.forName(class_name).newInstance();
		
		return instance;
	}


	public static Object getInstanceFromProvider(String[] providers, String class_name) throws Exception{
		Object instance = null;
		boolean isBreak=false;
		if(providers!=null && providers.length>0){
			int i=0;
			while(instance==null && i<providers.length){
				if(providers[i]!=null && !providers[i].equals("") && !providers[i].equalsIgnoreCase("false"))
					instance = getBeanFromObjectFactory(providers[i], class_name, class_name, null);
				else if(providers[i]!=null && providers[i].equalsIgnoreCase("false")){
					isBreak=true;
					break;			
				}
				i++;
			}
		}
		if(!isBreak && instance==null && bsController.getCdiDefaultProvider()!=null)
			instance = getBeanFromObjectFactory(bsController.getCdiDefaultProvider(), class_name, class_name, null);

		if(instance==null && bsController.getEjbDefaultProvider()!=null)
			instance = getBeanFromObjectFactory(bsController.getEjbDefaultProvider(), class_name, class_name, null);
		
		
		if(instance==null && class_name!=null)
			instance = Class.forName(class_name).newInstance();
		
		return instance;
	}

	public static Object getInstanceFromProvider(String[] providers, String class_name, Class[] arg, Object[] par) throws Exception{
		Object instance = null;
		boolean isBreak=false;
		if(providers!=null && providers.length>0){
			int i=0;
			while(instance==null && i<providers.length){			
				if(providers[i]!=null && !providers[i].equals("") && !providers[i].equalsIgnoreCase("false"))
					instance = getBeanFromObjectFactory(providers[i], class_name, class_name, null);
				else if(providers[i]!=null && providers[i].equalsIgnoreCase("false")){
					isBreak=true;
					break;			
				}
				i++;
			}
		}
		if(!isBreak && instance==null && bsController.getCdiDefaultProvider()!=null)
			instance = getBeanFromObjectFactory(bsController.getCdiDefaultProvider(), class_name, class_name, null);

		if(instance==null && bsController.getEjbDefaultProvider()!=null)
			instance = getBeanFromObjectFactory(bsController.getEjbDefaultProvider(), class_name, class_name, null);

		if(instance==null && class_name!=null)
			instance = Class.forName(class_name).getConstructor(arg).newInstance(par);
		
		return instance;
	}

	public static Object getInstanceFromProvider(info_action i_action, String class_name) throws Exception{
		if(class_name==null) 
			return null;
		if(i_action==null)
			return getInstanceFromProvider(new String[]{bsController.getAction_config().getProvider(),bsController.getAppInit().get_cdi_provider(), bsController.getAppInit().get_ejb_provider()}, class_name);
		return getInstanceFromProvider(new String[]{i_action.getProvider(),bsController.getAction_config().getProvider(),bsController.getAppInit().get_cdi_provider(), bsController.getAppInit().get_ejb_provider()}, class_name);
	}

	public static Object getInstanceFromProvider(info_action i_action, String class_name, Class[] arg, Object[] par) throws Exception{
		if(class_name==null) 
			return null;
		if(i_action==null)
			return getInstanceFromProvider(new String[]{bsController.getAction_config().getProvider(),bsController.getAppInit().get_cdi_provider(), bsController.getAppInit().get_ejb_provider()}, class_name, arg, par);
		return getInstanceFromProvider(new String[]{i_action.getProvider(),bsController.getAction_config().getProvider(),bsController.getAppInit().get_cdi_provider(), bsController.getAppInit().get_ejb_provider()}, class_name, arg, par);
	}


	public static boolean destroyInstanceFromProvider(Object obj, ServletContext servletContext){
		boolean result = false;
		if(obj==null) return result;
		String[] providers = null;
		if(obj instanceof i_action){
			i_action iaction = (i_action)obj;
			if(iaction.get_infoaction()!=null){
				providers = new String[]{iaction.get_infoaction().getProvider(),bsController.getAction_config().getProvider(),bsController.getAppInit().get_cdi_provider(), bsController.getAppInit().get_ejb_provider()};
				result = destroyInstanceFromObjectFactory(providers, iaction.get_infoaction().getPath(), obj.getClass().getName(), servletContext);
			}else{
				providers = new String[]{bsController.getAction_config().getProvider(),bsController.getAppInit().get_cdi_provider(), bsController.getAppInit().get_ejb_provider()};
				result = destroyInstanceFromObjectFactory(providers, obj.getClass().getName(), obj.getClass().getName(), servletContext);
			}
		}else if(obj instanceof i_bean){
			i_bean ibean = (i_bean)obj;
			if(ibean.get_infobean()!=null){
				providers = new String[]{ibean.get_infobean().getProvider(),bsController.getAction_config().getProvider(),bsController.getAppInit().get_cdi_provider(), bsController.getAppInit().get_ejb_provider()};
				result = destroyInstanceFromObjectFactory(providers, ibean.get_infobean().getName(), obj.getClass().getName(), servletContext);
			}else{
				providers = new String[]{bsController.getAction_config().getProvider(),bsController.getAppInit().get_cdi_provider(), bsController.getAppInit().get_ejb_provider()};
				result = destroyInstanceFromObjectFactory(providers, obj.getClass().getName(), obj.getClass().getName(), servletContext);
			}
		}else if(obj instanceof i_stream){
			i_stream istream = (i_stream)obj;
			if(istream.get_infostream()!=null){
				providers = new String[]{istream.get_infostream().getProvider(),bsController.getAction_config().getProvider(),bsController.getAppInit().get_cdi_provider(), bsController.getAppInit().get_ejb_provider()};
				result = destroyInstanceFromObjectFactory(providers, istream.get_infostream().getName(), obj.getClass().getName(), servletContext);
			}else{
				providers = new String[]{bsController.getAction_config().getProvider(),bsController.getAppInit().get_cdi_provider(), bsController.getAppInit().get_ejb_provider()};
				result = destroyInstanceFromObjectFactory(providers, obj.getClass().getName(), obj.getClass().getName(), servletContext);
			}			
		}else if(obj instanceof i_transformation){
			i_transformation itrans = (i_transformation)obj;
			if(itrans.get_infotransformation()!=null){
				providers = new String[]{itrans.get_infotransformation().getProvider(),bsController.getAction_config().getProvider(),bsController.getAppInit().get_cdi_provider(), bsController.getAppInit().get_ejb_provider()};
				result = destroyInstanceFromObjectFactory(providers, itrans.get_infotransformation().getName(), obj.getClass().getName(), servletContext);
			}else{
				providers = new String[]{bsController.getAction_config().getProvider(),bsController.getAppInit().get_cdi_provider(), bsController.getAppInit().get_ejb_provider()};
				result = destroyInstanceFromObjectFactory(providers, obj.getClass().getName(), obj.getClass().getName(), servletContext);
			}			
		}else{
			providers = new String[]{bsController.getAppInit().get_cdi_provider(), bsController.getAppInit().get_ejb_provider()};
			result = destroyInstanceFromObjectFactory(providers, obj.getClass().getName(), obj.getClass().getName(), servletContext);
		}
		
		return result;
	}
	
	public static boolean destroyInstanceFromObjectFactory(String[] providers, String id_bean, String class_bean, ServletContext servletContext){
		boolean result = false;
		boolean isBreak=false;
		if(providers!=null && providers.length>0){
			int i=0;
			while(!result && i<providers.length){
				if(providers[i]!=null && !providers[i].equals("") && !providers[i].equalsIgnoreCase("false")){
					result = destroyInstanceFromProvider(providers[i], id_bean, class_bean, servletContext);
					if(!result)
						result = destroyInstanceFromProvider(bsConstants.CONST_PROVIDER_PATH+providers[i], id_bean, class_bean, servletContext);
				}else if(providers[i]!=null && providers[i].equalsIgnoreCase("false")){
					isBreak=true;
					break;			
				}
				i++;
			}
			if(!isBreak && bsController.getCdiDefaultProvider()!=null)
				result = destroyInstanceFromProvider(bsController.getCdiDefaultProvider(), id_bean, class_bean, servletContext);
		}
		return result;
	}

	
	
	public static boolean destroyInstanceFromProvider(String id_provider, String id_bean, String class_bean, ServletContext servletContext){
		try{		
			try{
				Class c_provider = Class.forName(id_provider);
				try{
					Method m_destroyInstance = c_provider.getDeclaredMethod("destroyInstance", new Class[]{String.class,String.class,ServletContext.class});
					Object instance = m_destroyInstance.invoke(null, new Object[]{id_bean,class_bean,servletContext});
					return ((Boolean)instance).booleanValue();
				}catch(Exception e){				
				}
				return false;
			}catch (Exception e) {
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}catch (Throwable t) {
			new bsControllerException(t,iStub.log_DEBUG);
		}
		return false;
	}
	
	public static boolean destroyInstanceFromProvider(i_provider _provider, String id_bean, String class_bean, ServletContext servletContext){
		try{		
			try{
				Class c_provider = _provider.getClass();
				try{
					Method m_destroyInstance = c_provider.getDeclaredMethod("destroyInstance", new Class[]{String.class,String.class,ServletContext.class});
					Object instance = m_destroyInstance.invoke(null, new Object[]{id_bean,class_bean,servletContext});
					return ((Boolean)instance).booleanValue();
				}catch(Exception e){				
				}
				return false;
			}catch (Exception e) {
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}catch (Throwable t) {
			new bsControllerException(t,iStub.log_DEBUG);
		}
		return false;
	}
	
	public static Object getEjbFromProvider(i_provider _provider, Object obj, String id_bean, String class_bean, ServletContext servletContext){
		if(_provider==null)
			return null;
		try{		
//			i_provider provider  = null;
			try{
				Class c_provider = _provider.getClass();
				try{
					Method m_getInstance = c_provider.getDeclaredMethod("getInstance", new Class[]{Object.class, String.class, String.class,ServletContext.class});
					Object instance = m_getInstance.invoke(null, new Object[]{obj, id_bean,class_bean,servletContext});
					return instance;
				}catch(Exception e){				
				}
			}catch (Exception e) {
				return null;
			}
			return 
				null;
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}catch (Throwable t) {
			new bsControllerException(t,iStub.log_DEBUG);
		}
		return null;
	}	

	public static info_context checkInfoEjbContext(info_context info, i_provider _provider, i_bean bean){
		if(info==null)
			info = new info_context();
		if(_provider==null)
			return info;
		try{		
			Class c_provider = _provider.getClass();
			Method m_getInstance = c_provider.getDeclaredMethod("checkInfoEjb", new Class[]{info_context.class, i_bean.class});
			info = (info_context)m_getInstance.invoke(null, new Object[]{info, bean});
			return info;
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}catch (Throwable t) {
			new bsControllerException(t,iStub.log_DEBUG);
		}
		return info;
	}	
	
	public static info_context checkInfoCdiContext(info_context info, String id_provider, i_bean bean){
		if(info==null)
			info = new info_context();
		if(id_provider==null)
			return info;

		try{		
			Class c_provider = Class.forName(id_provider);
			Method m_getInstance = c_provider.getDeclaredMethod("checkInfoCdi", new Class[]{info_context.class, i_bean.class});
			info = (info_context)m_getInstance.invoke(null, new Object[]{info, bean});
			return info;
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}catch (Throwable t) {
			new bsControllerException(t,iStub.log_DEBUG);
		}
		return null;
	}

	public static info_context checkInfoCdiContext(info_context info, i_provider _provider, i_bean bean){
		if(info==null)
			info = new info_context();
		if(_provider==null)
			return info;

		try{		
			Class c_provider = _provider.getClass();
			Method m_getInstance = c_provider.getDeclaredMethod("checkInfoCdi", new Class[]{info_context.class, i_bean.class});
			info = (info_context)m_getInstance.invoke(null, new Object[]{info, bean});
			return info;
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}catch (Throwable t) {
			new bsControllerException(t,iStub.log_DEBUG);
		}
		return null;
	}
	
	
	public static info_context checkInfoContext(String id_provider, i_bean bean){
		info_context info = new info_context();
		
		if(id_provider==null || id_provider.equals("") || bean==null)
			return info;
		info = checkInfoCdiContext(null, id_provider, bean);
		if(info==null)
			info = checkInfoCdiContext(null, bsConstants.CONST_PROVIDER_PATH+id_provider, bean);
	
		if(info==null && bsController.getCdiDefaultProvider()!=null)
			info = checkInfoCdiContext(null, bsController.getCdiDefaultProvider(), bean);

		if(info==null)
			info = new info_context();
		info = checkInfoEjbContext(info, bsController.getEjbDefaultProvider(), bean);
		
		return info;
	}

	public static info_context checkInfoContext(i_provider _provider, i_bean bean){
		info_context info = new info_context();
		
		if(_provider==null || bean==null)
			return info;
		info = checkInfoCdiContext(null, _provider, bean);

		if(info==null)
			info = new info_context();
		info = checkInfoEjbContext(info, bsController.getEjbDefaultProvider(), bean);
		
		return info;
	}
	

}
