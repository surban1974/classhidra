package it.classhidra.plugin.provider;



import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.i_provider;
import it.classhidra.core.controller.info_context;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_provider;
import it.classhidra.plugin.provider.cdi.wrappers.Wrapper_Local_container;
import it.classhidra.plugin.provider.cdi.wrappers.Wrapper_Navigation;
import it.classhidra.plugin.provider.cdi.wrappers.Wrapper_Onlyinssession;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Named;
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

		boolean anotherCheck4ejb=false;
		if(instance!=null){
	    	Class clazz=instance.getClass();

	    	if(instance instanceof i_bean){
				if(	clazz.getAnnotation(SessionScoped.class)!=null ||
					clazz.getAnnotation(ApplicationScoped.class)!=null ||
					clazz.getAnnotation(RequestScoped.class)!=null){
						((i_bean)instance).getInfo_context().setProxedCdi(true);
						((i_bean)instance).getInfo_context().setProxedEjb(false);
						if(clazz.getAnnotation(SessionScoped.class)!=null)
							((i_bean)instance).getInfo_context().setSessionScoped(true);
						if(clazz.getAnnotation(ApplicationScoped.class)!=null)
							((i_bean)instance).getInfo_context().setApplicationScoped(true);
						if(clazz.getAnnotation(RequestScoped.class)!=null)
							((i_bean)instance).getInfo_context().setRequestScoped(true);
						if(clazz.getAnnotation(Named.class)!=null)
							((i_bean)instance).getInfo_context().setNamed(true);
				}else{
					try{
						i_bean ibean = ((i_bean)instance).asBean();
						Class clazzi=ibean.getClass();
						if(	clazzi.getAnnotation(SessionScoped.class)!=null ||
							clazzi.getAnnotation(ApplicationScoped.class)!=null ||
							clazzi.getAnnotation(RequestScoped.class)!=null){
							ibean.getInfo_context().setProxedCdi(true);
							ibean.getInfo_context().setProxedEjb(false);
							if(clazz.getAnnotation(SessionScoped.class)!=null)
								ibean.getInfo_context().setSessionScoped(true);
							if(clazz.getAnnotation(ApplicationScoped.class)!=null)
								ibean.getInfo_context().setApplicationScoped(true);
							if(clazz.getAnnotation(RequestScoped.class)!=null)
								ibean.getInfo_context().setRequestScoped(true);
							if(clazz.getAnnotation(Named.class)!=null)
								ibean.getInfo_context().setNamed(true);
						}else if(!clazzi.getName().equals(clazz.getName()))
							anotherCheck4ejb=true;
					}catch(Exception e){						
					}					
				}
			}else if(instance instanceof i_action){
				if(	clazz.getAnnotation(SessionScoped.class)!=null ||
					clazz.getAnnotation(ApplicationScoped.class)!=null ||
					clazz.getAnnotation(RequestScoped.class)!=null){
						((i_action)instance).getInfo_context().setProxedCdi(true);
						((i_action)instance).getInfo_context().setProxedEjb(false);
						if(clazz.getAnnotation(SessionScoped.class)!=null)
							((i_action)instance).getInfo_context().setSessionScoped(true);
						if(clazz.getAnnotation(ApplicationScoped.class)!=null)
							((i_action)instance).getInfo_context().setApplicationScoped(true);
						if(clazz.getAnnotation(RequestScoped.class)!=null)
							((i_action)instance).getInfo_context().setRequestScoped(true);
						if(clazz.getAnnotation(Named.class)!=null)
							((i_action)instance).getInfo_context().setNamed(true);
				}else{
					try{
						i_action iaction = ((i_action)instance).asAction();
						Class clazzi=iaction.getClass();
						if(	clazzi.getAnnotation(SessionScoped.class)!=null ||
							clazzi.getAnnotation(ApplicationScoped.class)!=null ||
							clazzi.getAnnotation(RequestScoped.class)!=null){
							iaction.getInfo_context().setProxedCdi(true);
							iaction.getInfo_context().setProxedEjb(false);
							if(clazz.getAnnotation(SessionScoped.class)!=null)
								iaction.getInfo_context().setSessionScoped(true);
							if(clazz.getAnnotation(ApplicationScoped.class)!=null)
								iaction.getInfo_context().setApplicationScoped(true);
							if(clazz.getAnnotation(RequestScoped.class)!=null)
								iaction.getInfo_context().setRequestScoped(true);
							if(clazz.getAnnotation(Named.class)!=null)
								iaction.getInfo_context().setNamed(true);
						}else if(!clazzi.getName().equals(clazz.getName()))
							anotherCheck4ejb=true;
					}catch(Exception e){						
					}					
				}
			}
		}
		
		if(bsController.getEjbDefaultProvider()!=null && instance!=null && anotherCheck4ejb){
			Object ejb_instance = util_provider.getEjbFromProvider(bsController.getEjbDefaultProvider(), null, id_bean, class_bean, _context);
			if(ejb_instance!=null){
				try{
					if(instance instanceof i_bean){
						((i_bean)instance).getInfo_context().setProxedEjb(true);
						((i_bean)instance).getInfo_context().reInitEjb(((i_bean)ejb_instance).getInfo_context());
					}else if(ejb_instance instanceof i_action){
						((i_bean)instance).getInfo_context().setProxedEjb(true);
						((i_bean)instance).getInfo_context().reInitEjb(((i_action)ejb_instance).getInfo_context());
					}
				}catch(Exception e){
				}
			}
			
		}
		
		
		
		if(bsController.getEjbDefaultProvider()!=null && instance==null){
			Object ejb_instance = util_provider.getEjbFromProvider(bsController.getEjbDefaultProvider(), null, id_bean, class_bean, _context);
			if(ejb_instance!=null){
				try{
			    	Class clazz=ejb_instance.getClass();


			    	if(ejb_instance instanceof i_bean){
			    		((i_bean)ejb_instance).getInfo_context().setProxedEjb(true);
						if(	clazz.getAnnotation(SessionScoped.class)!=null ||
							clazz.getAnnotation(ApplicationScoped.class)!=null ||
							clazz.getAnnotation(RequestScoped.class)!=null){
								((i_bean)ejb_instance).getInfo_context().setProxedCdi(true);
								((i_bean)ejb_instance).getInfo_context().setProxedEjb(false);
								if(clazz.getAnnotation(SessionScoped.class)!=null)
									((i_bean)ejb_instance).getInfo_context().setSessionScoped(true);
								if(clazz.getAnnotation(ApplicationScoped.class)!=null)
									((i_bean)ejb_instance).getInfo_context().setApplicationScoped(true);
								if(clazz.getAnnotation(RequestScoped.class)!=null)
									((i_bean)ejb_instance).getInfo_context().setRequestScoped(true);
								if(clazz.getAnnotation(Named.class)!=null)
									((i_bean)ejb_instance).getInfo_context().setNamed(true);
						}else{
							try{
								i_bean ibean = ((i_bean)ejb_instance).asBean();
								Class clazzi=ibean.getClass();
								if(	clazzi.getAnnotation(SessionScoped.class)!=null ||
									clazzi.getAnnotation(ApplicationScoped.class)!=null ||
									clazzi.getAnnotation(RequestScoped.class)!=null){
									ibean.getInfo_context().setProxedCdi(true);
									ibean.getInfo_context().setProxedEjb(false);
									if(clazz.getAnnotation(SessionScoped.class)!=null)
										ibean.getInfo_context().setSessionScoped(true);
									if(clazz.getAnnotation(ApplicationScoped.class)!=null)
										ibean.getInfo_context().setApplicationScoped(true);
									if(clazz.getAnnotation(RequestScoped.class)!=null)
										ibean.getInfo_context().setRequestScoped(true);
									if(clazz.getAnnotation(Named.class)!=null)
										ibean.getInfo_context().setNamed(true);
								}
							}catch(Exception e){						
							}					
						}
					}else if(ejb_instance instanceof i_action){
			    		((i_action)ejb_instance).getInfo_context().setProxedEjb(true);
						if(	clazz.getAnnotation(SessionScoped.class)!=null ||
							clazz.getAnnotation(ApplicationScoped.class)!=null ||
							clazz.getAnnotation(RequestScoped.class)!=null){
								((i_action)ejb_instance).getInfo_context().setProxedCdi(true);
								((i_action)ejb_instance).getInfo_context().setProxedEjb(false);
								if(clazz.getAnnotation(SessionScoped.class)!=null)
									((i_action)ejb_instance).getInfo_context().setSessionScoped(true);
								if(clazz.getAnnotation(ApplicationScoped.class)!=null)
									((i_action)ejb_instance).getInfo_context().setApplicationScoped(true);
								if(clazz.getAnnotation(RequestScoped.class)!=null)
									((i_action)ejb_instance).getInfo_context().setRequestScoped(true);
								if(clazz.getAnnotation(Named.class)!=null)
									((i_action)ejb_instance).getInfo_context().setNamed(true);
						}else{
							try{
								i_action iaction = ((i_action)ejb_instance).asAction();
								Class clazzi=iaction.getClass();
								if(	clazzi.getAnnotation(SessionScoped.class)!=null ||
									clazzi.getAnnotation(ApplicationScoped.class)!=null ||
									clazzi.getAnnotation(RequestScoped.class)!=null){
									iaction.getInfo_context().setProxedCdi(true);
									iaction.getInfo_context().setProxedEjb(false);
									if(clazz.getAnnotation(SessionScoped.class)!=null)
										iaction.getInfo_context().setSessionScoped(true);
									if(clazz.getAnnotation(ApplicationScoped.class)!=null)
										iaction.getInfo_context().setApplicationScoped(true);
									if(clazz.getAnnotation(RequestScoped.class)!=null)
										iaction.getInfo_context().setRequestScoped(true);
									if(clazz.getAnnotation(Named.class)!=null)
										iaction.getInfo_context().setNamed(true);
								}
							}catch(Exception e){						
							}					
						}
					}
					
					
				}catch(Exception e){
				}
				return ejb_instance;
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
     
     public static info_context checkInfoCdi(info_context info, i_bean bean) {
    	if(info==null)
    		info = new info_context();
    	if(bean!=null){
    		try{
    			Class clazz = bean.getClass();
         		if(clazz.getAnnotation(SessionScoped.class)!=null)
         			info.setSessionScoped(true);
         		if(clazz.getAnnotation(ApplicationScoped.class)!=null)
         			info.setApplicationScoped(true);
         		if(clazz.getAnnotation(RequestScoped.class)!=null)
         			info.setRequestScoped(true);
 
    		 }catch(Exception e){
    			 
    		 }
    	 }
    	 return info;
     }

}