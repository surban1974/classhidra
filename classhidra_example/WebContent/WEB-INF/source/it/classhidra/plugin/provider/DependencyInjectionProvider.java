package it.classhidra.plugin.provider;



import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.i_provider;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_provider;

import it.classhidra.plugin.provider.cdi.wrappers.Wrapper_Local_container;
import it.classhidra.plugin.provider.cdi.wrappers.Wrapper_Navigation;
import it.classhidra.plugin.provider.cdi.wrappers.Wrapper_Onlyinssession;

import java.lang.reflect.Proxy;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

public class DependencyInjectionProvider implements i_provider {

	private static final String LOOKUP_BEANMANAGER = 		"java:comp/BeanManager";
	private static final String LOOKUP_ENV_BEANMANAGER = 	"java:comp/env/BeanManager";

	
	private static String correct_jndi_jndi_name;
	private static BeanManager beanManager;

	

	public void set_context(ServletContext context) {
	}

	public Object get_bean(String id_bean) {
		Object instance=null;
		if(id_bean==null) return instance;
		
		try{
			instance = resolveReference(id_bean);
		}catch(Exception e){
		}
		if(instance==null){
			try{
				instance = resolveReference(Class.forName(id_bean));
			}catch(Exception e){
			}			
		}
		
		return instance;
	}
	
	
	
	
//------ Static Implementation	
	
	public static Class getWrapper(String name){
		if(name.equals(bsConstants.CONST_BEAN_$ONLYINSSESSION))
			return Wrapper_Onlyinssession.class;
		if(name.equals(bsConstants.CONST_BEAN_$NAVIGATION))
			return Wrapper_Navigation.class;
		if(name.equals(bsConstants.CONST_BEAN_$LOCAL_CONTAINER))
			return Wrapper_Local_container.class;
		return null;
	}
	
	public static Object getInstance(String id_bean, String class_bean, ServletContext _context) {
		Object instance=null;
		if(id_bean==null && class_bean==null) return instance;
		if(id_bean!=null && class_bean!=null && id_bean.equals(class_bean))
			id_bean=null;
		if(id_bean!=null){
			try{
				instance = resolveReference(id_bean);
			}catch(Exception e){
				new bsControllerException(e, iStub.log_ERROR);
			}
		}
		if(instance==null && class_bean!=null){
			try{
				instance = resolveReference(Class.forName(class_bean));
			}catch(Exception e){
				new bsControllerException(e, iStub.log_ERROR);
			}			
		}
/*		
		if(bsController.getEjbDefaultProvider()!=null && instance!=null && instance instanceof i_lookup){
			Object lookup_instance = util_provider.getEjbFromProvider(bsController.getEjbDefaultProvider(), instance, id_bean, class_bean, _context);
			if(lookup_instance!=null)
				return lookup_instance;
		}
*/		
		boolean anotherCheck4ejb=false;
		if(instance!=null){
	    	Class clazz=instance.getClass();

	    	if(instance instanceof Proxy) {
				if(bsController.getEjbDefaultProvider()!=null){
					Object lookup_instance = util_provider.getEjbFromProvider(bsController.getEjbDefaultProvider(), null, id_bean, class_bean, _context);
					if(lookup_instance!=null){
						try{
							if(lookup_instance instanceof i_action){
								i_bean ibean = ((i_action)lookup_instance).asBean();
								Class clazz0i=ibean.getClass();
								if(	clazz0i.getAnnotation(SessionScoped.class)!=null ||
									clazz0i.getAnnotation(ApplicationScoped.class)!=null ||
									clazz0i.getAnnotation(RequestScoped.class)!=null){
									ibean.setNavigable(false);
									ibean.setCdi(true);
									ibean.setEjb(true);
								}	
								ibean.setNavigable(false);
								ibean.setEjb(true);
							}else if(lookup_instance instanceof i_bean){						
								i_bean ibean = ((i_bean)lookup_instance).asBean();
								Class clazz0i=ibean.getClass();
								if(	clazz0i.getAnnotation(SessionScoped.class)!=null ||
									clazz0i.getAnnotation(ApplicationScoped.class)!=null ||
									clazz0i.getAnnotation(RequestScoped.class)!=null){
									ibean.setNavigable(false);
									ibean.setCdi(true);
									ibean.setEjb(true);
								}	
								ibean.setNavigable(false);
								ibean.setEjb(true);								
							}
						}catch(Exception e){						
						}					
						
					}
					return lookup_instance;
				}	
	    	}
	    	
			if(instance instanceof i_bean){
				if(	clazz.getAnnotation(SessionScoped.class)!=null ||
					clazz.getAnnotation(ApplicationScoped.class)!=null ||
					clazz.getAnnotation(RequestScoped.class)!=null){
						((i_bean)instance).setNavigable(false);
						((i_bean)instance).asBean().setCdi(true);
				}else{
					try{
						i_bean ibean = ((i_bean)instance).asBean();
						Class clazzi=ibean.getClass();
						if(	clazzi.getAnnotation(SessionScoped.class)!=null ||
							clazzi.getAnnotation(ApplicationScoped.class)!=null ||
							clazzi.getAnnotation(RequestScoped.class)!=null){
							ibean.setNavigable(false);
							ibean.setCdi(true);
							ibean.setEjb(true);
						}else if(!clazzi.getName().equals(clazz.getName()))
							anotherCheck4ejb=true;
					}catch(Exception e){						
					}					
				}
			}else if(instance instanceof i_action){
				if(	clazz.getAnnotation(SessionScoped.class)!=null ||
					clazz.getAnnotation(ApplicationScoped.class)!=null ||
					clazz.getAnnotation(RequestScoped.class)!=null){
						((i_action)instance).asBean().setNavigable(false);
						((i_action)instance).asBean().setCdi(true);
				}else{
					try{
						i_action iaction = ((i_action)instance).asAction();
						Class clazzi=iaction.getClass();
						if(	clazzi.getAnnotation(SessionScoped.class)!=null ||
							clazzi.getAnnotation(ApplicationScoped.class)!=null ||
							clazzi.getAnnotation(RequestScoped.class)!=null){
							iaction.asBean().setNavigable(false);
							iaction.asBean().setCdi(true);
							iaction.asBean().setEjb(true);
						}else if(!clazzi.getName().equals(clazz.getName()))
							anotherCheck4ejb=true;
					}catch(Exception e){						
					}					
				}
			}
		}
		if(bsController.getEjbDefaultProvider()!=null && (instance==null || anotherCheck4ejb)){
			Object lookup_instance = util_provider.getEjbFromProvider(bsController.getEjbDefaultProvider(), null, id_bean, class_bean, _context);
			if(lookup_instance!=null){
				try{
			    	Class clazz=lookup_instance.getClass();
					if(lookup_instance instanceof i_bean){
						((i_bean)lookup_instance).setNavigable(false);
						((i_bean)lookup_instance).setEjb(true);
					}
					if(lookup_instance instanceof i_action){
						((i_action)lookup_instance).asBean().setNavigable(false);
						((i_bean)lookup_instance).setEjb(true);
					}
				}catch(Exception e){
				}
				return lookup_instance;
			}
		}
		return instance;
	}	
	
	public static Object destroyInstance(String id_bean, String class_bean, ServletContext _context) {
		boolean result=false;
		if(id_bean==null && class_bean==null)
			return result;
		if(id_bean!=null && class_bean!=null && id_bean.equals(class_bean))
			id_bean=null;

		if(id_bean!=null){
			try{
		        BeanManager bm = getBeanManager();
		        Bean bean = bm.resolve(bm.getBeans(id_bean));
		        CreationalContext cc = bm.createCreationalContext(bean);		   
		        bean.destroy(bm.getReference(bean, bean.getBeanClass(), cc), cc);
				result=true;
			}catch(Exception e){
			}
		}
		if(class_bean!=null){
			try{
		        BeanManager bm = getBeanManager();
		        Bean bean = bm.resolve(bm.getBeans(Class.forName(class_bean)));
		        CreationalContext cc = bm.createCreationalContext(bean);
		        bean.destroy(bm.getReference(bean, Class.forName(class_bean), cc), cc);
				result=true;
			}catch(Exception e){
			}			
		}
		
		return result;
	}	
	
	
    public static Object resolveReference(String elName) {
        BeanManager bm = getBeanManager();
        Bean bean = bm.resolve(bm.getBeans(elName));
        if(bean!=null)
        	return bm.getReference(bean, bean.getBeanClass(), bm.createCreationalContext(bean));
        return null;
     }

/*
     public static <T> T resolveReference(Class<T> clazz) {
         BeanManager bm = getBeanManager();
         Bean<?> bean = bm.resolve(bm.getBeans(clazz));
         return (T) bm.getReference(bean, bean.getBeanClass(), bm.createCreationalContext(bean));
     }
*/
    
    public static Object resolveReference(Class clazz) {
        BeanManager bm = getBeanManager();
        Bean<?> bean = bm.resolve(bm.getBeans(clazz));
        if(bean!=null)
        	return  bm.getReference(bean, bean.getBeanClass(), bm.createCreationalContext(bean));
        return null;
    }
    
     public static BeanManager getBeanManager() {
    	 if(beanManager != null) 
    		 return beanManager;
         try {
       		 InitialContext initialContext = new InitialContext();
        	 
         	if(correct_jndi_jndi_name!=null){
	         	try{
	         		beanManager = (BeanManager)initialContext.lookup(correct_jndi_jndi_name);
	         	}catch(Exception e){
	         		new bsControllerException(e, iStub.log_ERROR);
//	         		le.printStackTrace();
	         	}        		
        	}        	
         	if(beanManager==null){ 
         		correct_jndi_jndi_name=null;
		        try{
		        	beanManager = (BeanManager)initialContext.lookup(LOOKUP_BEANMANAGER);
		         	if(beanManager!=null)
		         		correct_jndi_jndi_name = LOOKUP_BEANMANAGER;
		         }catch(Exception e){
		         }catch (Throwable e) {
		         }
         	}
	         	
        		
         	if(beanManager==null){ 
         		beanManager = (BeanManager)initialContext.lookup(LOOKUP_ENV_BEANMANAGER);
         		if(beanManager!=null)
         			correct_jndi_jndi_name = LOOKUP_ENV_BEANMANAGER;
         	}

         } catch (NamingException e) {
        	 new bsControllerException(e, iStub.log_ERROR);
//             e.printStackTrace();
         }
         return beanManager;
     }
     
     public static boolean checkInitialContext(String cdiJndiName,  ServletContext _context) {
 //   	 BeanManager beanManager = null;
    	 beanManager = null;   	 
         try {
        	InitialContext initialContext = new InitialContext();
        	
        	if(correct_jndi_jndi_name!=null){
	         	try{
	         		beanManager = (BeanManager)initialContext.lookup(correct_jndi_jndi_name);
	         	}catch(Exception e){
	         		new bsControllerException(e, iStub.log_ERROR);
//	         		le.printStackTrace();
	         	}        		
        	}  
        	
        	if(beanManager==null){
        		correct_jndi_jndi_name=null;
	        	if(cdiJndiName!=null && !cdiJndiName.equals("")){
		         	try{
		         		beanManager = (BeanManager)initialContext.lookup(cdiJndiName);
		         		if(beanManager!=null)
		         			correct_jndi_jndi_name = cdiJndiName;
		         	}catch(Exception e){
		         		new bsControllerException(e, iStub.log_ERROR);
//		         		le.printStackTrace();
		         	}        		
	        	}
        	}
        	 
         	if(beanManager==null){
	         	try{
	         		beanManager = (BeanManager)initialContext.lookup(LOOKUP_BEANMANAGER);
	         		if(beanManager!=null)
	         			correct_jndi_jndi_name = LOOKUP_BEANMANAGER;
	         	}catch(Exception e){
	         		new bsControllerException(e, iStub.log_ERROR);
//	         		le.printStackTrace();
	         	}
         	}
        		
         	if(beanManager==null){ 
	         	try{
	         		beanManager = (BeanManager)initialContext.lookup(LOOKUP_ENV_BEANMANAGER);
	         		if(beanManager!=null)
	         			correct_jndi_jndi_name = LOOKUP_ENV_BEANMANAGER;
	         	}catch(Exception e){
         			new bsControllerException(e, iStub.log_ERROR);
 //        			le.printStackTrace();
         		}
         	}

         } catch (NamingException e) {
        	 new bsControllerException(e, iStub.log_ERROR);
//             e.printStackTrace();
         }
         
         
         if(beanManager==null){ 
        	 new bsControllerException("Impossible retrive CDI BeanManager", iStub.log_WARN);
        	 return false;
         }else{
        	 new bsControllerException("CDI BeanManager retrived SUCCEESFULLY from: "+correct_jndi_jndi_name, iStub.log_INFO);
        	 return true;
         }
     }
     

}
