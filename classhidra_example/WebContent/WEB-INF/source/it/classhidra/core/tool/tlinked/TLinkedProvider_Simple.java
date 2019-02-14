package it.classhidra.core.tool.tlinked;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.classhidra.annotation.elements.TemporaryLinked;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.info_tlinked;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;

public class TLinkedProvider_Simple implements I_TLinkedProvider {

	private static final long serialVersionUID = 1L;


	public i_action link(i_action instance, HttpServletRequest request, HttpServletResponse response) {
		try {
			if(instance!=null && instance.get_infoaction()!=null) {
				if(instance.get_infoaction().getCheckTLinked()==-1 || instance.get_infoaction().get_tlinked()==null) {
					instance.get_infoaction().setCheckTLinked(0);
					if(instance.get_infoaction().get_tlinked()==null)
						instance.get_infoaction().set_tlinked(new HashMap());
					instance.get_infoaction().get_tlinked().clear();
					Map fields = new HashMap();
					fields = getAllFields(fields, instance.getClass());
					for(Object obj: fields.values()) {
//					for(Field field: instance.getClass().getDeclaredFields() ) {
						if(obj instanceof Field) {
							Field field = (Field)obj;
							TemporaryLinked annotation = field.getAnnotation(TemporaryLinked.class);
							if(annotation!=null) {
								instance.get_infoaction().get_tlinked().put(field.getName(), new info_tlinked(annotation));
								instance.get_infoaction().setCheckTLinked(1);
							}
						}
					}
				}
				if(instance.get_infoaction().getCheckTLinked()==1 && instance.get_infoaction().get_tlinked()!=null && instance.get_infoaction().get_tlinked().size()>0) {
					Map fields = new HashMap();
					fields = getAllFields(fields, instance.getClass());
					Iterator it = instance.get_infoaction().get_tlinked().entrySet().iterator();
				    while (it.hasNext()) {
				        Map.Entry pair = (Map.Entry)it.next();
				        try {
//				        	Field field = instance.getClass().getDeclaredField(pair.getKey().toString());
				        	Field field = (Field)fields.get(pair.getKey().toString());
				        	if(field!=null) {
				        		if(checkInterfaces(i_bean.class, field.getType().getInterfaces())) {
				        			if(pair.getValue() instanceof info_tlinked) {
				        				info_tlinked tlinked = (info_tlinked)pair.getValue();
				        				if(tlinked.getValue()!=null && !tlinked.getValue().equals("")) {
				        					i_bean bean = bsController.getCurrentForm(tlinked.getValue(), request);
				        					if(bean!=null) {
				        						boolean isAccessible = field.isAccessible();
							        			if(!isAccessible)
							        				field.setAccessible(true);			        			
							        			field.set(instance, bean);			        			
							        			if(!isAccessible)
							        				field.setAccessible(false);	
				        					}
				        				}
				        			}	
				        		}else if(field.getType().isAssignableFrom(Map.class)) {	
				        			if(pair.getValue() instanceof info_tlinked) {
				        				info_tlinked tlinked = (info_tlinked)pair.getValue();
					        			if(tlinked.getReference()!=null && tlinked.getReference()!=void.class) {
				        					Map references = bsController.getCurrentForm(tlinked.getReference(), request);
				        					boolean isAccessible = field.isAccessible();
						        			if(!isAccessible)
						        				field.setAccessible(true);			        			
						        			field.set(instance, references);			        			
						        			if(!isAccessible)
						        				field.setAccessible(false);	
				        				}
				        			}
				        		}else if(field.getType().isAssignableFrom(HttpServletRequest.class)) {
				        			boolean isAccessible = field.isAccessible();
				        			if(!isAccessible)
				        				field.setAccessible(true);			        			
				        			field.set(instance, request);			        			
				        			if(!isAccessible)
				        				field.setAccessible(false);	
				        		}else if(field.getType().isAssignableFrom(HttpServletResponse.class)) {
				        			boolean isAccessible = field.isAccessible();
				        			if(!isAccessible)
				        				field.setAccessible(true);			        			
				        			field.set(instance, response);			        			
				        			if(!isAccessible)
				        				field.setAccessible(false);	
				        		}else if(field.getType().isAssignableFrom(HttpSession.class)) {
				        			boolean isAccessible = field.isAccessible();
				        			if(!isAccessible)
				        				field.setAccessible(true);			        			
				        			field.set(instance, request.getSession());			        			
				        			if(!isAccessible)
				        				field.setAccessible(false);	
				        		}else if(field.getType().isAssignableFrom(ServletContext.class)) {
				        			boolean isAccessible = field.isAccessible();
				        			if(!isAccessible)
				        				field.setAccessible(true);			        			
				        			field.set(instance, request.getSession().getServletContext());			        			
				        			if(!isAccessible)
				        				field.setAccessible(false);	
				        		}
				        	}
				        }catch(Exception e) {			        	
				        }
				    }
				}
			}
		}catch(Exception e) {
			new bsException(e,iStub.log_WARN);
		}catch(Throwable e) {
			new bsException(e,iStub.log_ERROR);
		}
		return instance;
	}


	public i_action unlink(i_action instance, HttpServletRequest request, HttpServletResponse response) {
		try {
			if(instance!=null && instance.get_infoaction()!=null) {
				if(instance.get_infoaction().getCheckTLinked()==1 && instance.get_infoaction().get_tlinked()!=null && instance.get_infoaction().get_tlinked().size()>0) {
					Iterator it = instance.get_infoaction().get_tlinked().entrySet().iterator();
				    while (it.hasNext()) {
				        Map.Entry pair = (Map.Entry)it.next();
				        try {
				        	Field field = instance.getClass().getDeclaredField(pair.getKey().toString());
				        	if(field!=null) {
				        		if(pair.getValue() instanceof info_tlinked) {
			        				info_tlinked tlinked = (info_tlinked)pair.getValue();
			        				if(tlinked.isUnlinkAndSetNull()) {
			        					boolean isAccessible = field.isAccessible();
					        			if(!isAccessible)
					        				field.setAccessible(true);			        			
					        			field.set(instance, null);			        			
					        			if(!isAccessible)
					        				field.setAccessible(false);	
			        				}
				        		}else {
		        					boolean isAccessible = field.isAccessible();
				        			if(!isAccessible)
				        				field.setAccessible(true);			        			
				        			field.set(instance, null);			        			
				        			if(!isAccessible)
				        				field.setAccessible(false);	
				        		}
				        	}

				        }catch(Exception e) {			        	
				        }
				    }
				}
			}
		}catch(Exception e) {
			new bsException(e,iStub.log_WARN);
		}catch(Throwable e) {
			new bsException(e,iStub.log_ERROR);
		}
		return instance;
	}

	private boolean checkInterfaces(Class clazz, Class[] interfaces_) {
		if(clazz==null || interfaces_.length==0) 
			return false;
		for(Class interface_:interfaces_) {
			if(interface_.isAssignableFrom(clazz))
				return true;
		}
		return false;
	}
	
	private Map getAllFields(Map fields, Class type) {
		for(Field field: type.getDeclaredFields() ) {
			if(field.getAnnotation(TemporaryLinked.class)!=null && fields.get(field.getName())==null)
				fields.put(field.getName(),field);
		}

	    if (type.getSuperclass() != null && checkInterfaces(i_action.class, type.getSuperclass().getInterfaces())) {
	    	getAllFields(fields, type.getSuperclass());
	    }

	    return fields;
	}
}
