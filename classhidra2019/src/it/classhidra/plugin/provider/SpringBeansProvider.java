package it.classhidra.plugin.provider;






import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import it.classhidra.core.controller.i_ProviderWrapper;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.i_provider;
import it.classhidra.core.controller.info_context;
import it.classhidra.core.tool.util.util_format;


@Component
@Scope(value="singleton")
//public class SpringBeansProvider implements i_provider, ApplicationContextAware {
public class SpringBeansProvider implements i_provider {
	private static final long serialVersionUID = 1L;

	private static SpringBeansProvider instanceProvider;
	
	@Autowired
	private ApplicationContext applicationContext;	
	
	private static ConcurrentHashMap<String,String> cacheNamingMap;
	
	private static final String CONST_NULL = "NULL";

	@PostConstruct
	public void registerInstance() {
		instanceProvider = this;
	}
	
//	@Override
//	public void setApplicationContext(ApplicationContext _applicationContext) throws BeansException {
//		applicationContext = _applicationContext;   
//	}	
	
	public Object get_bean(String id_bean) {
		Object result=null;
		if(id_bean==null) return result;
		id_bean=util_format.replace(id_bean, "$", "");
		result = instanceProvider.applicationContext.getBean(id_bean);		
		return result;
	}

	public void set_context(ServletContext _context) {
	}
	
	public static Object getInstance(String id_bean, String class_bean, ServletContext _context) {
		Object instance = getInstanceFromContext(id_bean, class_bean, null);
		if(instance==null && _context!=null){
			instance = getInstanceFromContext(id_bean, class_bean, WebApplicationContextUtils.getRequiredWebApplicationContext(_context));
		}
		return elaborateWrapperInfo_context(instance);
	}
	
	private static Object getInstanceFromContext(String id_bean, String class_bean, ApplicationContext _applicationContext) {
		Object instance=null;
		if(_applicationContext==null)
			_applicationContext = instanceProvider.applicationContext;
		if(_applicationContext==null)
			return instance;
		if((id_bean==null || id_bean.equals("")) && (class_bean==null || class_bean.equals(""))) 
			return instance;

		String retrivedClassName = null;
		if(id_bean!=null) retrivedClassName = (String)getNamingMap().get(id_bean);
		if(retrivedClassName==null && class_bean!=null) retrivedClassName = (String)getNamingMap().get(class_bean);
		if(retrivedClassName!=null){
			if(!retrivedClassName.equals(CONST_NULL)){
				try{
					try{
						instance = _applicationContext.getBean(retrivedClassName);	
					}catch(Exception e){		
					}
					if(instance!=null)
						return elaborateWrapperInfo_context(instance);
					
				}catch(Exception e){
				}
			}else
				return null;
		}		
		
		try{
			instance = _applicationContext.getBean(id_bean);	
			getNamingMap().put(id_bean, id_bean);
		}catch(Exception e){	
			e.toString();
		}
		if(instance==null && class_bean!=null){
			try{
				Class<?> clazz = Class.forName(class_bean);
				try{
					instance = _applicationContext.getBean(clazz);	
					getNamingMap().put(id_bean, clazz.getName());
				}catch(Exception e){			
				}
				if(instance==null){
					try{
						instance = _applicationContext.getBean(clazz.getName(), clazz);	
						getNamingMap().put(id_bean, clazz.getName());
					}catch(Exception e){			
					}
				}
				if(instance==null){
					try{
						instance = _applicationContext.getBean(clazz.getSimpleName(), clazz);	
						getNamingMap().put(id_bean, clazz.getSimpleName());
					}catch(Exception e){			
					}				
				}
			
			}catch(Exception e){
			}
		}
		


		
		return instance;
	}		
	

	
	
	
	
//------ Static Implementation	

	
	private static Object elaborateWrapperInfo_context(Object instance){
		
		if(instance!=null){

			info_context iContext = null;
			if(instance instanceof i_ProviderWrapper){
				if(((i_ProviderWrapper)instance).getInfo_context()!=null){
					((i_ProviderWrapper)instance).getInfo_context().setProxiedContext(true);
					iContext = ((i_ProviderWrapper)instance).getInfo_context();
				}
			}else if(instance instanceof i_bean){
				if(((i_bean)instance).getInfo_context()!=null){
					((i_bean)instance).getInfo_context().setProxiedContext(true);
					iContext = ((i_bean)instance).getInfo_context();
				}				
			}else if(instance instanceof i_action){
				if(((i_action)instance).getInfo_context()!=null){
					((i_action)instance).getInfo_context().setProxiedContext(true);
					iContext = ((i_action)instance).getInfo_context();
				}				
			}		
			if(iContext!=null){
				iContext.setProxiedId(System.identityHashCode(instance));
				iContext.setProxiedClass(instance.getClass());
				iContext.setProxy(instance);
				if(iContext.getOwnerClass()!=null){
					if(iContext.getOwnerClass().getAnnotation(Scope.class)!=null){
						Scope annotation = (Scope)iContext.getOwnerClass().getAnnotation(Scope.class);
						iContext.setSingleton(true);
						if(annotation.value()!=null && annotation.value().equals("request")){
							iContext.setRequestScoped(true);
							iContext.setSingleton(false);
						}
						if(annotation.scopeName()!=null && annotation.scopeName().equals("request")){
							iContext.setRequestScoped(true);
							iContext.setSingleton(false);
						}
						if(annotation.value()!=null && annotation.value().equals("session")){
							iContext.setSessionScoped(true);
							iContext.setSingleton(false);
						}
						if(annotation.scopeName()!=null && annotation.scopeName().equals("session")){
							iContext.setSessionScoped(true);						
							iContext.setSingleton(false);
						}
						if(annotation.value()!=null && annotation.value().equals("singleton"))
							iContext.setSingleton(true);
						if(annotation.scopeName()!=null && annotation.scopeName().equals("singleton"))
							iContext.setSingleton(true);	
						if(annotation.value()!=null && annotation.value().equals("prototype"))
							iContext.setSingleton(false);
						if(annotation.scopeName()!=null && annotation.scopeName().equals("prototype"))
							iContext.setSingleton(false);						
					}
					Component component = (Component)iContext.getOwnerClass().getAnnotation(Component.class);
					if(component!=null)
						iContext.setName(component.value());
					Service service = (Service)iContext.getOwnerClass().getAnnotation(Service.class);
					if(service!=null)
						iContext.setName(service.value());
					Repository repository = (Repository)iContext.getOwnerClass().getAnnotation(Repository.class);
					if(repository!=null)
						iContext.setName(repository.value());
					Controller controller = (Controller)iContext.getOwnerClass().getAnnotation(Controller.class);
					if(controller!=null)
						iContext.setName(controller.value());
				}
			}
				
			return instance;
		}else 
			return instance;
	}	
	

	
	public static Object destroyInstance(String id_bean, String class_bean, ServletContext _context) {
		boolean result=false;
		return result;
	}	
	
	


/*
     public static <T> T resolveReference(Class<T> clazz) {
         BeanManager bm = getBeanManager();
         Bean<?> bean = bm.resolve(bm.getBeans(clazz));
         return (T) bm.getReference(bean, bean.getBeanClass(), bm.createCreationalContext(bean));
     }
*/
    
    
     
     public static boolean checkInitialContext(String cdiJndiName,  ServletContext _context) {
    	 if(instanceProvider.applicationContext==null)
    		 return false;
    	 else 
    		 return true;
     }

     public static ConcurrentHashMap<String,String> getNamingMap() {
 		if(SpringBeansProvider.cacheNamingMap==null)
 			SpringBeansProvider.cacheNamingMap = new ConcurrentHashMap<String, String>();
 		return SpringBeansProvider.cacheNamingMap;
 	}	

     

}
