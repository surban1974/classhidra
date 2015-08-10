package it.classhidra.plugin.provider;




import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.EJBContext;
import javax.ejb.MessageDriven;
import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.Bean;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.LOOKUP;
import it.classhidra.annotation.elements.Stream;
import it.classhidra.annotation.elements.Transformation;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.i_provider;
import it.classhidra.core.controller.i_stream;
import it.classhidra.core.controller.i_transformation;
import it.classhidra.core.controller.info_context;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.plugin.provider.ejb.wrappers.Wrapper_EjbContextLocal;


public class EjbProvider implements i_provider {

	private static final String LOOKUP_EJBCONTEXT = 		"java:comp/EJBContext";
	private static final String LOOKUP_ENV_EJBCONTEXT = 	"java:comp/env/EJBContext";
	private static final String LOOKUP_MAPPED_EJBCONTEXT = 	"java:module/WrapperEjbContext";
	private static final String LOOKUP_MAPPED_EJB_PREFIX = 	"java:module/";
	
	private static int correct_context=0; 
	
	private static String correct_ejb_jndi_name;
	private static EJBContext ejbContext;
//	private static InitialContext initialContext;
	private static ConcurrentHashMap namingMap;
	

	
	

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
	
	public static Object getInstance(Object obj, String id_bean, String class_bean, ServletContext _context) {
		Object instance=null;
		String retrivedClassName = null;
		if(retrivedClassName==null && class_bean!=null) retrivedClassName = (String)getNamingMap().get(class_bean);
		if(retrivedClassName==null && id_bean!=null) retrivedClassName = (String)getNamingMap().get(id_bean);
		if(retrivedClassName!=null){
			try{
				StringTokenizer parts = new StringTokenizer(retrivedClassName,"|");
				int type = Integer.valueOf(parts.nextToken());
				String lookup = parts.nextToken();
				instance = resolveReference(type,lookup);
				if(instance!=null)
					return instance;
				
			}catch(Exception e){
			}
		}
		if(correct_context==0){
			instance = getInstanceFromContext(obj, id_bean, class_bean, _context, 1);
			if(instance!=null){
				correct_context = 1;
				return instance;
			}
			if(instance==null){
				instance = getInstanceFromContext(obj, id_bean, class_bean, _context, 2);
				if(instance!=null){
					correct_context = 2;
					return instance;
				}
			}
		}else{
			return
					getInstanceFromContext(obj, id_bean, class_bean, _context, correct_context);
			
		}
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
    	}else if(class_bean!=null){
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

 		
		return instance;
	}	
	

	
	
    public static Object resolveReference(int type, String elName) {
    	if(type==1){
	    	EJBContext ejbc = getEjbContext();
	        try{
	        	Object result = null;
	        	try{
	        		result = ejbc.lookup(elName);
	        		if(result instanceof i_bean)
	        			setInfoContext(((i_bean) result).asBean());
	        		if(result instanceof i_action)
	        			setInfoContext(((i_action) result).asBean());
	        	}catch(Exception e){
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
	        		if(result instanceof i_bean)
	        			setInfoContext(((i_bean) result).asBean());
	        		if(result instanceof i_action)
	        			setInfoContext(((i_action) result).asBean());
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
 	         		if(fromLookup instanceof Wrapper_EjbContextLocal){
 	         			ejbContext = (EJBContext)((Wrapper_EjbContextLocal)fromLookup).getInstance();
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
	 	         		if(fromLookup instanceof Wrapper_EjbContextLocal){
	 	         			ejbContext = (EJBContext)((Wrapper_EjbContextLocal)fromLookup).getInstance();
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
	         		if(fromLookup instanceof Wrapper_EjbContextLocal){
	         			ejbContext = (EJBContext)((Wrapper_EjbContextLocal)fromLookup).getInstance();
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
		if(namingMap==null)
			namingMap = new ConcurrentHashMap();
		return namingMap;
	}	

	public boolean clearNamingMap() {
		if(namingMap!=null)
			namingMap.clear();
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
	         		if(fromLookup instanceof Wrapper_EjbContextLocal){
	         			ejbContext = (EJBContext)((Wrapper_EjbContextLocal)fromLookup).getInstance();
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
		         		if(fromLookup instanceof Wrapper_EjbContextLocal){
		         			ejbContext = (EJBContext)((Wrapper_EjbContextLocal)fromLookup).getInstance();
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
	         		if(fromLookup instanceof Wrapper_EjbContextLocal){
	         			ejbContext = (EJBContext)((Wrapper_EjbContextLocal)fromLookup).getInstance();
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
         }
         
         
         if(ejbContext==null){ 
        	 new bsControllerException("Impossible retrive EjbContext", iStub.log_WARN);
        	 return false;
         }else{
        	 new bsControllerException("EjbContext retrived SUCCEESFULLY from: "+correct_ejb_jndi_name, iStub.log_INFO);
        	 return true;
         }
     }

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
    			bean.getInfo_context().setProxedEjb(true);
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


}
