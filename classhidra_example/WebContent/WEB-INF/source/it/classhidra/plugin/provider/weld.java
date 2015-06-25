package it.classhidra.plugin.provider;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import it.classhidra.core.controller.i_provider;

public class weld implements i_provider {



	public void set_context(ServletContext context) {

	}

	public Object get_bean(String id_bean) {
		Object instance=null;
		if(id_bean==null) return instance;
		
		try{
			instance = of(id_bean);
		}catch(Exception e){
		}
		
		return instance;
	}
	
	public static Object getInstance(String id_bean, String class_bean, ServletContext _context) {
		Object instance=null;
		if(id_bean==null && class_bean==null) return instance;
		if(id_bean!=null){
			try{
				instance = of(id_bean);
			}catch(Exception e){
			}
		}
		if(instance==null && class_bean!=null){
			try{
				instance = of(Class.forName(class_bean));
			}catch(Exception e){
			}			
		}
		
		return instance;
	}	
	
    public static Object of(String elName) {
        BeanManager bm = getBeanManager();
        Bean<?> bean = bm.resolve(bm.getBeans(elName));
        return bm.getReference(bean, bean.getBeanClass(), bm.createCreationalContext(bean));
     }

/*
     public static <T> T of(Class<T> clazz) {
         BeanManager bm = getBeanManager();
         Bean<?> bean = bm.resolve(bm.getBeans(clazz));
         return (T) bm.getReference(bean, bean.getBeanClass(), bm.createCreationalContext(bean));
     }
*/
    
    public static Object of(Class clazz) {
        BeanManager bm = getBeanManager();
        Bean<?> bean = bm.resolve(bm.getBeans(clazz));
        return  bm.getReference(bean, bean.getBeanClass(), bm.createCreationalContext(bean));
    }
    
     private static BeanManager getBeanManager() {
     	BeanManager beanManager = null;
         try {
         	InitialContext initialContext = new InitialContext();
         	try{
         		beanManager = (BeanManager)initialContext.lookup("java:comp/BeanManager");
         	}catch(Exception le){}
        		
         	if(beanManager==null) 
         		beanManager = (BeanManager)initialContext.lookup("java:comp/env/BeanManager");

         } catch (NamingException e) {
             e.printStackTrace();
         }
         return beanManager;
     }
	

}
