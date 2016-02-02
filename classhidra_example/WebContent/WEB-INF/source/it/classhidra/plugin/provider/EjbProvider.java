package it.classhidra.plugin.provider;




import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.MessageDriven;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.Bean;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.LOOKUP;
import it.classhidra.annotation.elements.Stream;
import it.classhidra.annotation.elements.Transformation;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_ProviderWrapper;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.i_provider;
import it.classhidra.core.controller.i_stream;
import it.classhidra.core.controller.i_transformation;
import it.classhidra.core.controller.info_context;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
//import it.classhidra.plugin.provider.ejb.wrappers.Wrapper_EjbContextLocal;

@Stateless(name="WrapperEjbContext")
@Local(i_ProviderWrapper.class)
public class EjbProvider implements i_provider, i_ProviderWrapper {

	private static final String LOOKUP_EJBCONTEXT = 		"java:comp/EJBContext";
	private static final String LOOKUP_ENV_EJBCONTEXT = 	"java:comp/env/EJBContext";
	private static final String LOOKUP_MAPPED_EJBCONTEXT = 	"java:module/WrapperEjbContext";
	private static final String LOOKUP_MAPPED_EJB_PREFIX = 	"java:module/";
	private static final String LOOKUP_GLOBAL_NAMES =		"java:global";
	
	private static final String CONST_NULL =				"NULL";
	
	private static int correct_context=0; 
	
	private static String correct_ejb_jndi_name;
	private static EJBContext ejbContext;
//	private static InitialContext initialContext;
	private static ConcurrentHashMap cacheNamingMap;
	private static LinkedHashSet cacheJavaGlobalNames;
	

	@Resource
	private EJBContext ejbLookupContext;
	

	public Object getInstance() {
		return ejbLookupContext;
	}

	public boolean setInstance(Object instance) {
		return false;
	}
	
	@Override
	public info_context getInfo_context() {
		return null;
	}
	

	public void set_context(ServletContext context) {
	}

	public Object get_bean(String id_bean) {
		Object instance=null;
		if(id_bean==null) return instance;
		
		try{
			instance = resolveReference(1,id_bean);
		}catch(Exception e){
		}
		if(instance==null){
			try{
				instance = resolveReference(1,Class.forName(id_bean));
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
//					((i_ProviderWrapper)instance).getInfo_context().setProxiedEjb(true);
					iContext = ((i_ProviderWrapper)instance).getInfo_context();
					iContext.setProxiedEjb(true);
				}
			}else if(instance instanceof i_bean){
				if(((i_bean)instance).getInfo_context()!=null){
//					((i_bean)instance).getInfo_context().setProxiedEjb(true);
					iContext = ((i_bean)instance).getInfo_context();
					iContext.setProxiedEjb(true);
				}				
			}else if(instance instanceof i_action){
				if(((i_action)instance).getInfo_context()!=null){
//					((i_action)instance).getInfo_context().setProxiedEjb(true);
					iContext = ((i_action)instance).getInfo_context();
					iContext.setProxiedEjb(true);
				}				
			}else if(instance instanceof i_stream){
				if(((i_stream)instance).getInfo_context()!=null){
//					((i_stream)instance).getInfo_context().setProxiedEjb(true);
					iContext = ((i_stream)instance).getInfo_context();
					iContext.setProxiedEjb(true);
				}				
			}			
			if(iContext!=null){
				iContext.setProxiedClass(instance.getClass());
				iContext.setProxiedId(System.identityHashCode(instance));
				iContext.setProxy(instance);
				if(iContext.getOwnerClass()!=null){
					if(iContext.getOwnerClass().getAnnotation(Stateful.class)!=null){
						iContext.setStateful(true);
						Stateful annotation = (Stateful)iContext.getOwnerClass().getAnnotation(Stateful.class);
						if(annotation.name()!=null && !annotation.name().equals(""))
							iContext.setName(annotation.name());
						if(annotation.description()!=null && !annotation.description().equals(""))
							iContext.setDescription(annotation.description());
						if(annotation.mappedName()!=null && !annotation.mappedName().equals(""))
							iContext.setMappedName(annotation.mappedName());
					}
					if(iContext.getOwnerClass().getAnnotation(Stateless.class)!=null){
						iContext.setStateless(true);
						Stateless annotation = (Stateless)iContext.getOwnerClass().getAnnotation(Stateless.class);
						if(annotation.name()!=null && !annotation.name().equals(""))
							iContext.setName(annotation.name());
						if(annotation.description()!=null && !annotation.description().equals(""))
							iContext.setDescription(annotation.description());
						if(annotation.mappedName()!=null && !annotation.mappedName().equals(""))
							iContext.setMappedName(annotation.mappedName());
					}
					if(iContext.getOwnerClass().getAnnotation(MessageDriven.class)!=null){
						iContext.setMessageDriven(true);
						MessageDriven annotation = (MessageDriven)iContext.getOwnerClass().getAnnotation(MessageDriven.class);
						if(annotation.name()!=null && !annotation.name().equals(""))
							iContext.setName(annotation.name());
						if(annotation.description()!=null && !annotation.description().equals(""))
							iContext.setDescription(annotation.description());
						if(annotation.mappedName()!=null && !annotation.mappedName().equals(""))
							iContext.setMappedName(annotation.mappedName());
					}
					if(iContext.getOwnerClass().getAnnotation(Singleton.class)!=null){
						iContext.setSingleton(true);
						Singleton annotation = (Singleton)iContext.getOwnerClass().getAnnotation(Singleton.class);
						if(annotation.name()!=null && !annotation.name().equals(""))
							iContext.setName(annotation.name());
						if(annotation.description()!=null && !annotation.description().equals(""))
							iContext.setDescription(annotation.description());
						if(annotation.mappedName()!=null && !annotation.mappedName().equals(""))
							iContext.setMappedName(annotation.mappedName());
					}
					if(iContext.getOwnerClass().getAnnotation(Local.class)!=null){
						iContext.setLocal(true);
						Local annotation = (Local)iContext.getOwnerClass().getAnnotation(Local.class);
						iContext.setValue(annotation.value());
					}
					if(iContext.getOwnerClass().getAnnotation(Remote.class)!=null){
						iContext.setRemote(true);
						Remote annotation = (Remote)iContext.getOwnerClass().getAnnotation(Remote.class);
						iContext.setValue(annotation.value());
					}
				}
			}
			if(iContext!=null && instance instanceof i_action && iContext.isStateless() && iContext.getProxy()!=null)
				return iContext;
			
			if(iContext!=null && iContext.isRemote()){
				if(instance instanceof i_bean)
					((i_bean)instance).setInfo_context(iContext);
				else if(instance instanceof i_action)
					((i_action)instance).setInfo_context(iContext);
				else if(instance instanceof i_stream)
					((i_stream)instance).setInfo_context(iContext);
			}
			return instance;
		}else 
			return instance;
	}
	
	public static Object getInstance(String id_bean, String class_bean, ServletContext _context) {
		
		String retrivedClassName = null;
		if(id_bean!=null) retrivedClassName = (String)getNamingMap().get(id_bean);
		if(retrivedClassName==null && class_bean!=null) retrivedClassName = (String)getNamingMap().get(class_bean);
		if(retrivedClassName!=null){
			if(!retrivedClassName.equals(CONST_NULL)){
				try{
					StringTokenizer parts = new StringTokenizer(retrivedClassName,"|");
					int type = Integer.valueOf(parts.nextToken());
					String lookup = parts.nextToken();
					Object instance = resolveReference(type,lookup);
					if(instance!=null)
						return elaborateWrapperInfo_context(instance);
					
				}catch(Exception e){
				}
			}else
				return null;
		}

		if(cacheJavaGlobalNames==null)
			retriveGlobalNames(null,false);
		
		if(cacheJavaGlobalNames!=null && !cacheJavaGlobalNames.isEmpty() && (id_bean!=null || (class_bean!=null && !class_bean.equals("")))){
			String id_class_bean = null;
	   		if(class_bean!=null && !class_bean.equals("")){
	   			if(class_bean.lastIndexOf(".")>-1)
	   				id_class_bean = class_bean.substring(class_bean.lastIndexOf("."),class_bean.length()).replace(".", "");
	   		}
	   		Iterator it = cacheJavaGlobalNames.iterator();
	   		while(it.hasNext()){
				String key = (String)it.next();
				if(id_bean!=null){
					if(key.indexOf("/"+id_bean+"!")>-1){
						Object instance = resolveReference(1,key);						
						if(instance!=null){
							getNamingMap().put(id_bean, "1|"+key);
							return elaborateWrapperInfo_context(instance);
						}
					}
				}
				if(id_class_bean!=null){
					if(key.indexOf("/"+id_class_bean+"!")>-1){
						Object instance = resolveReference(1,key);
						if(instance!=null){
							getNamingMap().put(id_class_bean, "1|"+key);
							return elaborateWrapperInfo_context(instance);
						}
					}
				}
				
			}
		}
		
		return getInstance(null, id_bean, class_bean, _context);

	}
	
	public static Object getInstance(Object obj, String id_bean, String class_bean, ServletContext _context) { 
		Object instance=null;
		String retrivedClassName = null;
		if(retrivedClassName==null && class_bean!=null) retrivedClassName = (String)getNamingMap().get(class_bean);
		if(retrivedClassName==null && id_bean!=null) retrivedClassName = (String)getNamingMap().get(id_bean);
		if(retrivedClassName!=null){
			if(!retrivedClassName.equals(CONST_NULL)){
				try{
					StringTokenizer parts = new StringTokenizer(retrivedClassName,"|");
					int type = Integer.valueOf(parts.nextToken());
					String lookup = parts.nextToken();
					instance = resolveReference(type,lookup);
					if(instance!=null)
						return elaborateWrapperInfo_context(instance);
					
				}catch(Exception e){
				}
			}else
				return null;
		}
		if(correct_context==0){
			instance = getInstanceFromContext(obj, id_bean, class_bean, _context, 1);
			if(instance!=null){
				correct_context = 1;
				return elaborateWrapperInfo_context(instance);
			}
			if(instance==null){
				instance = getInstanceFromContext(obj, id_bean, class_bean, _context, 2);
				if(instance!=null){
					correct_context = 2;
					return elaborateWrapperInfo_context(instance);
				}
			}
		}else{
			return
					elaborateWrapperInfo_context(
							getInstanceFromContext(obj, id_bean, class_bean, _context, correct_context)
					);
			
		}
		if(id_bean!=null)
			getNamingMap().put(id_bean, CONST_NULL);
		return null;

	}
	
	public static Object getInstanceFromContext(Object obj, String id_bean, String class_bean, ServletContext _context, int type) {
		Object instance=null;
		boolean objIsNull=true;
		if(obj==null && id_bean==null && class_bean==null) return instance;
    	String prefix = bsController.getAppInit().get_ejb_jndi_name_prefix();
    	if(prefix==null)
    		prefix = LOOKUP_MAPPED_EJB_PREFIX;
    	String lookup = null;
    	if(obj!=null){
    		objIsNull=false;
    		if(obj instanceof i_action){
    			i_action iaction = (i_action)obj;
    			if(iaction.get_infoaction()!=null)
    				lookup = iaction.get_infoaction().getLookup();
     		}else if(obj instanceof i_bean){
    			i_bean ibean = (i_bean)obj;
    			if(ibean.get_infobean()!=null)
    				lookup = ibean.get_infobean().getLookup();
    		}else if(obj instanceof i_stream){
    			i_stream istream = (i_stream)obj;
    			if(istream.get_infostream()!=null)
    				lookup = istream.get_infostream().getLookup();
    		}else if(obj instanceof i_transformation){
    			i_transformation itrans = (i_transformation)obj;
    			if(itrans.get_infotransformation()!=null)
    				lookup = itrans.get_infotransformation().getLookup();
    		}


        	if(lookup!=null && !lookup.equals("")){
        		try{
    				instance = resolveReference(type,lookup);
    				if(instance!=null)
    					return instance;
    			}catch(Exception e){
    			}    		
        	}
    	}
    	
    	Class clazz=null;
    	if(obj!=null){
    		clazz=obj.getClass();
    	}else if(class_bean!=null && !class_bean.equals("")){
    		try{
    			clazz=Class.forName(class_bean);
    		}catch(Exception e){
    		}
    	}
    	if(clazz!=null){
        	if(instance==null){
           		LOOKUP a_lookup = (LOOKUP)clazz.getAnnotation(LOOKUP.class);
           		if(a_lookup!=null && a_lookup.name()!=null && !a_lookup.name().equals("")){
            		try{
        				instance = resolveReference(type,a_lookup.name());        				
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+a_lookup.name());
        					return instance;
        				}
        			}catch(Exception e){
        			} 
           		}
        		
        		if(objIsNull){
        			Action ac_lookup = (Action)clazz.getAnnotation(Action.class);
        			if(ac_lookup!=null && ac_lookup.entity()!=null && ac_lookup.entity().lookup()!=null && !ac_lookup.entity().lookup().equals("")){
                		try{
            				instance = resolveReference(type,ac_lookup.entity().lookup());        				
            				if(instance!=null){
            					getNamingMap().put(clazz.getName(), type+"|"+ac_lookup.entity().lookup());
            					return instance;
            				}
            			}catch(Exception e){
            			} 
        			}
        			Bean bn_lookup = (Bean)clazz.getAnnotation(Bean.class);
        			if(bn_lookup!=null && bn_lookup.entity()!=null && bn_lookup.entity().lookup()!=null && !bn_lookup.entity().lookup().equals("")){
                		try{
            				instance = resolveReference(type,bn_lookup.entity().lookup());        				
            				if(instance!=null){
            					getNamingMap().put(clazz.getName(), type+"|"+bn_lookup.entity().lookup());
            					return instance;
            				}
            			}catch(Exception e){
            			} 
        			}
        			Stream st_lookup = (Stream)clazz.getAnnotation(Stream.class);
        			if(st_lookup!=null && st_lookup.entity()!=null && st_lookup.entity().lookup()!=null && !st_lookup.entity().lookup().equals("")){
                		try{
            				instance = resolveReference(type,st_lookup.entity().lookup());        				
            				if(instance!=null){
            					getNamingMap().put(clazz.getName(), type+"|"+st_lookup.entity().lookup());
            					return instance;
            				}
            			}catch(Exception e){
            			} 
        			}
        			Transformation tr_lookup = (Transformation)clazz.getAnnotation(Transformation.class);
        			if(tr_lookup!=null && tr_lookup.entity()!=null && tr_lookup.entity().lookup()!=null && !tr_lookup.entity().lookup().equals("")){
                		try{
            				instance = resolveReference(type,tr_lookup.entity().lookup());        				
            				if(instance!=null){
            					getNamingMap().put(clazz.getName(), type+"|"+tr_lookup.entity().lookup());
            					return instance;
            				}
            			}catch(Exception e){
            			} 
        			}
           			Entity en_lookup = (Entity)clazz.getAnnotation(Entity.class);
        			if(en_lookup!=null && en_lookup.lookup()!=null && !en_lookup.lookup().equals("")){
                		try{
            				instance = resolveReference(type,en_lookup.lookup());        				
            				if(instance!=null){
            					getNamingMap().put(clazz.getName(), type+"|"+en_lookup.lookup());
            					return instance;
            				}
            			}catch(Exception e){
            			} 
        			}
        			
        		}
        		
            	
        		
           	}
        	if(instance==null){
        		Stateless a_ejb = (Stateless)clazz.getAnnotation(Stateless.class);
        		if(a_ejb!=null && a_ejb.mappedName()!=null && !a_ejb.mappedName().equals("")){
            		try{
        				instance = resolveReference(type,a_ejb.mappedName());
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+a_ejb.mappedName());
        					return instance;
        				}
        			}catch(Exception e){
        			} 
            		try{
        				instance = resolveReference(type,prefix+a_ejb.mappedName());
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+prefix+a_ejb.mappedName());
        					return instance;
        				}
        			}catch(Exception e){
        			} 
        		}
        		if(a_ejb!=null && a_ejb.name()!=null && !a_ejb.name().equals("")){
            		try{
        				instance = resolveReference(type,a_ejb.name());
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+a_ejb.name());
        					return instance;
        				}
        			}catch(Exception e){
        			}  
            		try{
        				instance = resolveReference(type,prefix+a_ejb.name());
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+prefix+a_ejb.name());
        					return instance;
        				}
        			}catch(Exception e){
        			}             		
        		}
        	}
        	
        	if(instance==null){
        		Stateful a_ejb = (Stateful)clazz.getAnnotation(Stateful.class);
        		if(a_ejb!=null && a_ejb.mappedName()!=null && !a_ejb.mappedName().equals("")){
            		try{
        				instance = resolveReference(type,a_ejb.mappedName());
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+a_ejb.mappedName());
        					return instance;
        				}
        			}catch(Exception e){
        			} 
            		try{
        				instance = resolveReference(type,prefix+a_ejb.mappedName());
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+prefix+a_ejb.mappedName());
        					return instance;
        				}
        			}catch(Exception e){
        			}         
        		}
        		if(a_ejb!=null && a_ejb.name()!=null && !a_ejb.name().equals("")){
            		try{
        				instance = resolveReference(type,a_ejb.name());
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+a_ejb.name());
        					return instance;
        				}
        			}catch(Exception e){
        			}  
            		try{
        				instance = resolveReference(type,prefix+a_ejb.name());
        				if(instance!=null)
        					return instance;
        			}catch(Exception e){
        			}             		
        		}
           	}
        	if(instance==null){
        		Singleton a_ejb = (Singleton)clazz.getAnnotation(Singleton.class);
        		if(a_ejb!=null && a_ejb.mappedName()!=null && !a_ejb.mappedName().equals("")){
            		try{
        				instance = resolveReference(type,a_ejb.mappedName());
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+a_ejb.mappedName());
        					return instance;
        				}
        			}catch(Exception e){
        			} 
            		try{
        				instance = resolveReference(type,prefix+a_ejb.mappedName());
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+prefix+a_ejb.mappedName());
        					return instance;
        				}
        			}catch(Exception e){
        			}      
        		}
        		if(a_ejb!=null && a_ejb.name()!=null && !a_ejb.name().equals("")){
            		try{
        				instance = resolveReference(type,a_ejb.name());
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+a_ejb.name());
        					return instance;
        				}
        			}catch(Exception e){
        			}  
            		try{
        				instance = resolveReference(type,prefix+a_ejb.name());
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+prefix+a_ejb.name());
        					return instance;
        				}
        			}catch(Exception e){
        			}             		
        		}        		
        	} 
        	if(instance==null){
        		MessageDriven a_ejb = (MessageDriven)clazz.getAnnotation(MessageDriven.class);
        		if(a_ejb!=null && a_ejb.mappedName()!=null && !a_ejb.mappedName().equals("")){
            		try{
        				instance = resolveReference(type,a_ejb.mappedName());
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+a_ejb.mappedName());
        					return instance;
        				}
        			}catch(Exception e){
        			} 
            		try{
        				instance = resolveReference(type,prefix+a_ejb.mappedName());
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+prefix+a_ejb.mappedName());
        					return instance;
        				}
        			}catch(Exception e){
        			}      
        		}
        		if(a_ejb!=null && a_ejb.name()!=null && !a_ejb.name().equals("")){
            		try{
        				instance = resolveReference(type,a_ejb.name());
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+a_ejb.name());
        					return instance;
        				}
        			}catch(Exception e){
        			}  
            		try{
        				instance = resolveReference(type,prefix+a_ejb.name());
        				if(instance!=null){
        					getNamingMap().put(clazz.getName(), type+"|"+prefix+a_ejb.name());
        					return instance;
        				}
        			}catch(Exception e){
        			}             		
        		}        		
        	} 
    	}	
 

    	if(instance==null && id_bean!=null && !id_bean.equals("")){
       		try{
    			instance = resolveReference(type,id_bean);
				if(instance!=null){
					getNamingMap().put(clazz.getName(), type+"|"+id_bean);
					return instance;
				}
    		}catch(Exception e){
    		} 
        	try{
    			instance = resolveReference(type,prefix+id_bean);
				if(instance!=null){
					getNamingMap().put(clazz.getName(), type+"|"+prefix+id_bean);
					return instance;
				}
    		}catch(Exception e){
    		}             		
     	}
    	
    	if(instance==null && clazz!=null){
       		try{
    			instance = resolveReference(type,clazz);
    			if(instance!=null)
    				return instance;
    		}catch(Exception e){
    		} 
     	}    	

		if(id_bean!=null)
			getNamingMap().put(id_bean, CONST_NULL);		
		return instance;
	}	
	

	
	
    public static Object resolveReference(int type, String elName) {
    	if(type==1){
	    	EJBContext ejbc = getEjbContext();
	        try{
	        	Object result = null;
	        	try{
	        		result = ejbc.lookup(elName);
	        		if(result!=null){
		        		if(result instanceof i_bean)
		        			setInfoContext(((i_bean) result).asBean());
		        		if(result instanceof i_action)
		        			setInfoContext(((i_action) result).asBean());
		        		if(result instanceof i_stream)
		        			setInfoContext(((i_stream) result).asStream());
	        		}
	        		return result;
	        	}catch(Exception e){
	        		if(result==null){
	        			result = resolveReference(2, elName);
	        			if(result!=null)
	        				getNamingMap().put(elName, "2|"+elName);
	        		}	
	        		return result;
	        	}
	        }catch(Exception e){
	        	return null;
	        }
    	}
    	if(type==2){    		
	        try{
	        	Object result = null;
	        	try{
	        		result = getStaticInitialContext().lookup(elName);
	        		if(result!=null){
		        		if(result instanceof i_bean)
		        			setInfoContext(((i_bean) result).asBean());
		        		if(result instanceof i_action)
		        			setInfoContext(((i_action) result).asBean());
		        		if(result instanceof i_stream)
		        			setInfoContext(((i_stream) result).asStream());		        		
	        		}
	        		return result;
	        	}catch(Exception e){
	        		return result;
	        	}
	        }catch(Exception e){
	        	return null;
	        }
    	}  
    	return null;
     }


    
    public static Object resolveReference(int type, Class clazz) {
       	if(type==1){    	
	    	EJBContext ejbc = getEjbContext();
	        try{
	        	String prefix = bsController.getAppInit().get_ejb_jndi_name_prefix();
	        	if(prefix==null)
	        		prefix = LOOKUP_MAPPED_EJB_PREFIX;
	        	Object result = null;
	        	try{
	        		result = ejbc.lookup(clazz.getSimpleName());
	        		if(result!=null){
       					getNamingMap().put(clazz.getName(),type+"|"+clazz.getSimpleName());
       					try{
	    	        		if(result instanceof i_bean)
	    	        			setInfoContext(((i_bean) result).asBean());
	    	        		if(result instanceof i_action)
	    	        			setInfoContext(((i_action) result).asBean());
			        		if(result instanceof i_stream)
			        			setInfoContext(((i_stream) result).asStream());	    	        		
       					}catch(Exception e){
       					}
	        			return result;
	        		}
	        	}catch(Exception e){        		
	        	}        	
	        	try{
	        		result = ejbc.lookup(prefix+clazz.getSimpleName());
	        		if(result!=null){
       					getNamingMap().put(clazz.getName(),type+"|"+prefix+clazz.getSimpleName());
       					try{
	    	        		if(result instanceof i_bean)
	    	        			setInfoContext(((i_bean) result).asBean());
	    	        		if(result instanceof i_action)
	    	        			setInfoContext(((i_action) result).asBean());
			        		if(result instanceof i_stream)
			        			setInfoContext(((i_stream) result).asStream());	    	        		
       					}catch(Exception e){
       					}
	        			return result;
	        		}
	        	}catch(Exception e){        		
	        	}
	        	try{
	        		result = ejbc.lookup(clazz.getName());
	        		if(result!=null){
       					getNamingMap().put(clazz.getName(),type+"|"+clazz.getName());
       					try{
	    	        		if(result instanceof i_bean)
	    	        			setInfoContext(((i_bean) result).asBean());
	    	        		if(result instanceof i_action)
	    	        			setInfoContext(((i_action) result).asBean());
			        		if(result instanceof i_stream)
			        			setInfoContext(((i_stream) result).asStream());	    	        		
       					}catch(Exception e){
       					}
	        			return result;
	        		}
	        	}catch(Exception e){        		
	        	}          	
	        	try{
	        		result = ejbc.lookup(prefix+clazz.getName());
	        		if(result!=null){
       					getNamingMap().put(clazz.getName(),type+"|"+prefix+clazz.getName());
       					try{
	    	        		if(result instanceof i_bean)
	    	        			setInfoContext(((i_bean) result).asBean());
	    	        		if(result instanceof i_action)
	    	        			setInfoContext(((i_action) result).asBean());
			        		if(result instanceof i_stream)
			        			setInfoContext(((i_stream) result).asStream());	    	        		
       					}catch(Exception e){
       					}
	        			return result;
	        		}
	        	}catch(Exception e){        		
	        	}
        		if(result==null)
        			result = resolveReference(2, clazz);
        		return result;
	        }catch(Exception e){
	        	return null;
	        }    
       	}
       	if(type==2){    	
	    	
	        try{
	        	
	        	String prefix = bsController.getAppInit().get_ejb_jndi_name_prefix();
	        	if(prefix==null)
	        		prefix = LOOKUP_MAPPED_EJB_PREFIX;
	        	Object result = null;
	        	try{
	        		result = getStaticInitialContext().lookup(clazz.getSimpleName());
	        		if(result!=null){
       					getNamingMap().put(clazz.getName(),type+"|"+clazz.getSimpleName());
       					try{
	    	        		if(result instanceof i_bean)
	    	        			setInfoContext(((i_bean) result).asBean());
	    	        		if(result instanceof i_action)
	    	        			setInfoContext(((i_action) result).asBean());
			        		if(result instanceof i_stream)
			        			setInfoContext(((i_stream) result).asStream());	    	        		
       					}catch(Exception e){
       					}
	        			return result;
	        		}
	        	}catch(Exception e){        		
	        	}        	
	        	try{
	        		result = getStaticInitialContext().lookup(prefix+clazz.getSimpleName());
	        		if(result!=null){
       					getNamingMap().put(clazz.getName(),type+"|"+prefix+clazz.getSimpleName());
       					try{
	    	        		if(result instanceof i_bean)
	    	        			setInfoContext(((i_bean) result).asBean());
	    	        		if(result instanceof i_action)
	    	        			setInfoContext(((i_action) result).asBean());
			        		if(result instanceof i_stream)
			        			setInfoContext(((i_stream) result).asStream());	    	        		
       					}catch(Exception e){
       					}
	        			return result;
	        		}
	        	}catch(Exception e){        		
	        	}
	        	try{
	        		result = getStaticInitialContext().lookup(clazz.getName());
	        		if(result!=null){
       					getNamingMap().put(clazz.getName(),type+"|"+clazz.getName());
       					try{
	    	        		if(result instanceof i_bean)
	    	        			setInfoContext(((i_bean) result).asBean());
	    	        		if(result instanceof i_action)
	    	        			setInfoContext(((i_action) result).asBean());
			        		if(result instanceof i_stream)
			        			setInfoContext(((i_stream) result).asStream());	    	        		
       					}catch(Exception e){
       					}
	        			return result;
	        		}
	        	}catch(Exception e){        		
	        	}          	
	        	try{
	        		result = getStaticInitialContext().lookup(prefix+clazz.getName());
	        		if(result!=null){
       					getNamingMap().put(clazz.getName(),type+"|"+prefix+clazz.getName());
       					try{
	    	        		if(result instanceof i_bean)
	    	        			setInfoContext(((i_bean) result).asBean());
	    	        		if(result instanceof i_action)
	    	        			setInfoContext(((i_action) result).asBean());
			        		if(result instanceof i_stream)
			        			setInfoContext(((i_stream) result).asStream());	    	        		
       					}catch(Exception e){
       					}
	        			return result;
	        		}
	        	}catch(Exception e){        		
	        	}        	
	        	return null;
	        }catch(Exception e){
	        	return null;
	        }    
       	}
       	
       	return null;
    }
    
    
    
     public static EJBContext getEjbContext() {
    	 if(ejbContext != null) 
    		 return ejbContext;
         try {
        	 InitialContext initialContext = getStaticInitialContext();
         	if(initialContext==null)
         		initialContext = new InitialContext();
       	 
         	if(correct_ejb_jndi_name!=null){
 	         	try{
 	         		
 	         		Object fromLookup = initialContext.lookup(correct_ejb_jndi_name);
 	         		if(fromLookup instanceof i_ProviderWrapper){
 	         			ejbContext = (EJBContext)((i_ProviderWrapper)fromLookup).getInstance();
// 	         		if(fromLookup instanceof Wrapper_EjbContextLocal){
// 	         			ejbContext = (EJBContext)((Wrapper_EjbContextLocal)fromLookup).getInstance();
 	         		}else
 	         			ejbContext = (EJBContext)fromLookup;
 	         	}catch(Exception e){
 	         		new bsControllerException(e, iStub.log_ERROR);
// 	         		le.printStackTrace();
 	         	}        		
        		
         	}
         	
         	String ejbJndiName = bsController.getAppInit().get_ejb_jndi_name();
         	if(ejbContext==null){
         		correct_ejb_jndi_name=null;
	         	if(ejbJndiName!=null && !ejbJndiName.equals("")){
	 	         	try{
	 	         		
	 	         		Object fromLookup = initialContext.lookup(ejbJndiName);
	 	         		if(fromLookup instanceof i_ProviderWrapper){
	 	         			ejbContext = (EJBContext)((i_ProviderWrapper)fromLookup).getInstance();	 	         		
//	 	         		if(fromLookup instanceof Wrapper_EjbContextLocal){
//	 	         			ejbContext = (EJBContext)((Wrapper_EjbContextLocal)fromLookup).getInstance();
	 	         		}else
	 	         			ejbContext = (EJBContext)fromLookup;
	 	         		if(ejbContext!=null)
	 	         			correct_ejb_jndi_name = ejbJndiName;
	 	         	}catch(Exception e){
	 	         		new bsControllerException(e, iStub.log_ERROR);
// 	         			le.printStackTrace();
	 	         	}        		
	         	}
         	}
       	        	 
          	if(ejbContext==null){
 	         	try{
 	         		ejbContext = (EJBContext)initialContext.lookup(LOOKUP_EJBCONTEXT);
 	         		if(ejbContext!=null)
 	         			correct_ejb_jndi_name = LOOKUP_EJBCONTEXT;
 	         		
 	         	}catch(Exception e){
 	         		new bsControllerException(e, iStub.log_ERROR);
// 	         		le.printStackTrace();
 	         	}
          	}
         		
          	if(ejbContext==null){
          		try{
 	         		ejbContext = (EJBContext)initialContext.lookup(LOOKUP_ENV_EJBCONTEXT);
 	         		if(ejbContext!=null)
 	         			correct_ejb_jndi_name = LOOKUP_ENV_EJBCONTEXT;
 	         		}catch(Exception e){
          			new bsControllerException(e, iStub.log_ERROR);
//          			le.printStackTrace();
          		}
          	}
          	
        	if(ejbContext==null){ 
         		try{        			
	         		Object fromLookup = initialContext.lookup(LOOKUP_MAPPED_EJBCONTEXT);
	         		if(fromLookup instanceof i_ProviderWrapper){
	         			ejbContext = (EJBContext)((i_ProviderWrapper)fromLookup).getInstance();
//	         		if(fromLookup instanceof Wrapper_EjbContextLocal){
//	         			ejbContext = (EJBContext)((Wrapper_EjbContextLocal)fromLookup).getInstance();
	         		}else
	         			ejbContext = (EJBContext)fromLookup;
	         		if(ejbContext!=null)
	         			correct_ejb_jndi_name = LOOKUP_MAPPED_EJBCONTEXT;
         		}catch(Exception e){
         			new bsControllerException(e, iStub.log_ERROR);
 //        			le.printStackTrace();
         		}
         	}     
        	
         } catch (NamingException e) {
        	 new bsControllerException(e, iStub.log_ERROR);
//             e.printStackTrace();
         }
         return ejbContext;
     }
     

 	public static InitialContext getStaticInitialContext() {
 		try{
 			return new InitialContext();
 		}catch(Exception e){
 			return null;
 		}
 /*		
 		try{
 			if(initialContext==null)
 				initialContext = new InitialContext();
 			return initialContext;
 		}catch(Exception e){
 			return null;
 		}
*/ 		
 	}

	public static ConcurrentHashMap getNamingMap() {
		if(cacheNamingMap==null)
			cacheNamingMap = new ConcurrentHashMap();
		return cacheNamingMap;
	}	

	public boolean clearNamingMap() {
		if(cacheNamingMap!=null)
			cacheNamingMap.clear();
		return true;
	}	
	
	
     public static boolean checkInitialContext(String ejbJndiName, ServletContext _context) {

    	 ejbContext = null;   	 
         try {
        	 InitialContext initialContext = getStaticInitialContext();
        	 if(initialContext==null)
        		 initialContext = new InitialContext();
        	 
        	if(correct_ejb_jndi_name!=null){
	         	try{
	         		
	         		Object fromLookup = initialContext.lookup(correct_ejb_jndi_name);
	         		if(fromLookup instanceof i_ProviderWrapper){
	         			ejbContext = (EJBContext)((i_ProviderWrapper)fromLookup).getInstance();	         		
//	         		if(fromLookup instanceof Wrapper_EjbContextLocal){
//	         			ejbContext = (EJBContext)((Wrapper_EjbContextLocal)fromLookup).getInstance();
	         		}else
	         			ejbContext = (EJBContext)fromLookup;
	         	}catch(Exception e){
	         		new bsControllerException(e, iStub.log_ERROR);
//	         		le.printStackTrace();
	         	}        		
       		
        	}
        	if(ejbContext==null){
        		correct_ejb_jndi_name=null;
	        	if(ejbJndiName!=null && !ejbJndiName.equals("")){
		         	try{
		         		
		         		Object fromLookup = initialContext.lookup(ejbJndiName);
		         		if(fromLookup instanceof i_ProviderWrapper){
		         			ejbContext = (EJBContext)((i_ProviderWrapper)fromLookup).getInstance();		         		
//		         		if(fromLookup instanceof Wrapper_EjbContextLocal){
//		         			ejbContext = (EJBContext)((Wrapper_EjbContextLocal)fromLookup).getInstance();
		         		}else
		         			ejbContext = (EJBContext)fromLookup;
		         		if(ejbContext!=null)
		         			correct_ejb_jndi_name = ejbJndiName;
		         	}catch(Exception e){
		         		new bsControllerException(e, iStub.log_ERROR);
//	         			le.printStackTrace();
		         	}        		
	        	}
        	}
      	        	 
         	if(ejbContext==null){
	         	try{
	         		ejbContext = (EJBContext)initialContext.lookup(LOOKUP_EJBCONTEXT);
	         		if(ejbContext!=null)
	         			correct_ejb_jndi_name = LOOKUP_EJBCONTEXT;
	         	}catch(Exception e){
	         		new bsControllerException(e, iStub.log_ERROR);
//	         		le.printStackTrace();
	         	}
         	}
        		
         	if(ejbContext==null){
         		try{
	         		ejbContext = (EJBContext)initialContext.lookup(LOOKUP_ENV_EJBCONTEXT);
	         		if(ejbContext!=null)
	         			correct_ejb_jndi_name = LOOKUP_ENV_EJBCONTEXT;
	         		}catch(Exception e){
         			new bsControllerException(e, iStub.log_ERROR);
//         			le.printStackTrace();
         		}
         	}
         	
         	if(ejbContext==null){ 
         		try{        			
	         		Object fromLookup = initialContext.lookup(LOOKUP_MAPPED_EJBCONTEXT);
	         		if(fromLookup instanceof i_ProviderWrapper){
	         			ejbContext = (EJBContext)((i_ProviderWrapper)fromLookup).getInstance();
//		         	if(fromLookup instanceof Wrapper_EjbContextLocal){
//		         		ejbContext = (EJBContext)((Wrapper_EjbContextLocal)fromLookup).getInstance();	         			
	         		}else
	         			ejbContext = (EJBContext)fromLookup;
	         		if(ejbContext!=null)
	         			correct_ejb_jndi_name = LOOKUP_MAPPED_EJBCONTEXT;
         		}catch(Exception e){
         			new bsControllerException(e, iStub.log_ERROR);
 //        			le.printStackTrace();
         		}
         	}         	

         	if(ejbContext!=null)
         		retriveGlobalNames(initialContext, true);

         } catch (NamingException e) {
        	 new bsControllerException(e, iStub.log_ERROR);
         }
         
         
         if(ejbContext==null){ 
        	 new bsControllerException("Impossible retrive EjbContext", iStub.log_WARN);
        	 return false;
         }else{
        	 new bsControllerException("EjbContext retrived SUCCEESFULLY from: "+correct_ejb_jndi_name, iStub.log_INFO);
        	 return true;
         }
     }

     
     public synchronized static void retriveGlobalNames(InitialContext initialContext, boolean reInit){
    	 if(initialContext==null)
    		 initialContext = getStaticInitialContext();
    	 if(reInit){
    		 cacheJavaGlobalNames = null;
    		 cacheNamingMap = null;
    	 }
    	 try{
    		 List firstPart_ear = new ArrayList();
    		 NamingEnumeration root = null;
    		 try{
    			 Context rootCtx = (Context)initialContext.lookup("java:global");
    			 if(rootCtx!=null)
    				 root = (NamingEnumeration)rootCtx.list("");
    		 }catch(Exception e){    			 
    		 }
    		 if(root==null){
        		 try{
        			 root = initialContext.list(LOOKUP_GLOBAL_NAMES);
        		 }catch(Exception e){    			 
        		 }
    		 }
    		 if(root!=null){
	    		 while(root.hasMore())
	    			 firstPart_ear.add(
	    				((NameClassPair)root.next()).getName()
	    			);
	    		 if(firstPart_ear!=null && firstPart_ear.size()>0){
	    			 for(int i=0;i<firstPart_ear.size();i++){
	    				 NamingEnumeration ne = initialContext.list(LOOKUP_GLOBAL_NAMES+"/"+firstPart_ear.get(i));
		        		 while(ne.hasMore()){
		        			 String secondPart_war = ((NameClassPair)ne.next()).getName();	    			 
			           		 String key = LOOKUP_GLOBAL_NAMES + ((firstPart_ear.get(i)==null)?"":"/"+firstPart_ear.get(i))+((secondPart_war==null)?"":"/"+secondPart_war);
			           		 NamingEnumeration ne2 = initialContext.list(key);
			           		 if(cacheJavaGlobalNames==null)
			           			cacheJavaGlobalNames = new LinkedHashSet();
			           		 try{
				        		 while(ne2.hasMore())
				        			 cacheJavaGlobalNames.add(key+"/"+((NameClassPair)ne2.next()).getName());
			           		 }catch(Exception e){
			           			cacheJavaGlobalNames.add(key);
			           		 }
		        		 }
	        		 }
	    		 }
    		 }
    		 
    	 }catch(Exception e){
    	 }
     }
     
 /*    
     private static List retriveNames(InitialContext initialContext,String prefix){
    	 List result = new ArrayList();
    	 try{
    		 String ear = null;
    		 String war = null;
    		 NamingEnumeration ne = initialContext.list(prefix);
    		 if(ne.hasMore())
    			 ear = ((NameClassPair)ne.next()).getName();
    		 if(ear!=null){
    			 ne = initialContext.list(prefix+"/"+ear);
        		 if(ne.hasMore())
        			 war = ((NameClassPair)ne.next()).getName();
    		 }
    		 String key = prefix + ((ear==null)?"":"/"+ear)+((war==null)?"":"/"+war);
    		 ne = initialContext.list(key);
    		 while(ne.hasMore())
    			 result.add(key+"/"+((NameClassPair)ne.next()).getName());
    		 
    	 }catch(Exception e){
    		 
    	 }
    	 return result;
     }
*/
     public static info_context checkInfoEjb(info_context info, i_bean bean) {
    	if(info==null)
    		info = new info_context();
    	if(bean!=null){
    		try{
    			Class clazz = bean.getClass();
         		if(clazz.getAnnotation(Stateless.class)!=null)
         			info.setStateless(true);
         		if(clazz.getAnnotation(Stateful.class)!=null)
         			info.setStateful(true);
         		if(clazz.getAnnotation(Singleton.class)!=null)
         			info.setSingleton(true);
         		if(clazz.getAnnotation(MessageDriven.class)!=null)
         			info.setMessageDriven(true);
         		
         		

    		 }catch(Exception e){
    			 
    		 }
    	 }
    	 return info;
     }

     public static void setInfoContext(i_bean bean) {
    	if(bean!=null){
    		try{
    			Class clazz = bean.getClass();
    			bean.getInfo_context().setProxiedEjb(true);
         		if(clazz.getAnnotation(Stateless.class)!=null)
         			bean.getInfo_context().setStateless(true);
         		if(clazz.getAnnotation(Stateful.class)!=null)
         			bean.getInfo_context().setStateful(true);
         		if(clazz.getAnnotation(Singleton.class)!=null)
         			bean.getInfo_context().setSingleton(true);
         		if(clazz.getAnnotation(MessageDriven.class)!=null)
         			bean.getInfo_context().setMessageDriven(true);
    		 }catch(Exception e){
    			 
    		 }
    	 }
     }
     public static void setInfoContext(i_stream stream) {
    	if(stream!=null){
    		try{
    			Class clazz = stream.getClass();
    			stream.getInfo_context().setProxiedEjb(true);
         		if(clazz.getAnnotation(Stateless.class)!=null)
         			stream.getInfo_context().setStateless(true);
         		if(clazz.getAnnotation(Stateful.class)!=null)
         			stream.getInfo_context().setStateful(true);
         		if(clazz.getAnnotation(Singleton.class)!=null)
         			stream.getInfo_context().setSingleton(true);
         		if(clazz.getAnnotation(MessageDriven.class)!=null)
         			stream.getInfo_context().setMessageDriven(true);
    		 }catch(Exception e){
    			 
    		 }
    	 }
     }



}
