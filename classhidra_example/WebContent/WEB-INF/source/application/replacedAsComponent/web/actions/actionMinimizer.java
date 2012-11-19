package application.replacedAsComponent.web.actions; 

import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.util.util_cloner;
import it.classhidra.core.controller.*;
import it.classhidra.framework.web.beans.option_element;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.*;
import javax.servlet.*;



public class actionMinimizer extends action implements i_action, Serializable{
	private static final long serialVersionUID = 77477796283694045L;

public actionMinimizer(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	
	if(get_bean().getMiddleAction()==null) get_bean().setMiddleAction("");
	HashMap elements = (HashMap)get_bean().get("elements");
	HashMap labels = (HashMap)get_bean().get("labels");
	HashMap fromSessions = (HashMap)get_bean().get("fromSessions");
	
	if(get_bean().getMiddleAction().equals("view")){
		return new redirects(get_infoaction().getRedirect());
	}
	if(get_bean().getMiddleAction().equals("close")){
		HashMap fromSession = (HashMap)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
		info_navigation	formInfoNavigation	= (info_navigation)request.getSession().getAttribute(bsController.CONST_BEAN_$NAVIGATION);
		info_navigation active = formInfoNavigation.getPrevIRedirect();
		if(active==null){
			active = formInfoNavigation;
		}else{
			info_navigation current = formInfoNavigation.lastIRedirect();
			try{
				HashMap fromSession_clone = new HashMap();
				if(fromSession!=null){
					fromSession_clone= (HashMap)util_cloner.clone(fromSession);
					fromSession_clone.remove("formMinimizer");
				}
				fromSessions.put(current.getIAction().getPath()+"", fromSession_clone);
				
				elements.put(current.getIAction().getPath()+"", util_cloner.clone(formInfoNavigation));
				labels.put(current.getIAction().getPath()+"", current.getDesc_second());
				get_bean().set("keys",prepareKeys(elements, labels));
			}catch(Exception e){
			}
			
		}
	
		return new redirects(active.getIAction().getPath()+"");
	}
	if(get_bean().getMiddleAction().equals("open")){
		HashMap fromSession = (HashMap)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION);
		info_navigation	formInfoNavigation	= (info_navigation)request.getSession().getAttribute(bsController.CONST_BEAN_$NAVIGATION);
		info_navigation active = formInfoNavigation.getPrevIRedirect();
		if(active==null){
			active = formInfoNavigation;
		}else{
			info_navigation current = formInfoNavigation.lastIRedirect();
			try{
				HashMap fromSession_clone = new HashMap();
				if(fromSession!=null){
					fromSession_clone= (HashMap)util_cloner.clone(fromSession);
					fromSession_clone.remove("formMinimizer");
				}
				fromSessions.put(current.getIAction().getPath()+"", fromSession_clone);
				elements.put(current.getIAction().getPath()+"", util_cloner.clone(formInfoNavigation));
				labels.put(current.getIAction().getPath()+"", current.getDesc_second());
			}catch(Exception e){
			}
			
		}
		
		
		
		String source = get_bean().get("source").toString();
		HashMap fromSession_clone = (HashMap)fromSessions.get(source);
		fromSession_clone.put("formMinimizer", get_bean());
		request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,fromSession_clone);
		
		info_navigation fromElements = (info_navigation)elements.get(source);
		if(fromElements!=null){
			request.getSession().setAttribute(bsController.CONST_BEAN_$NAVIGATION,fromElements);
		}
		elements.remove(source);
		get_bean().set("keys",prepareKeys(elements, labels));

		return new redirects(source);
	}
	if(get_bean().getMiddleAction().equals("delete")){
		String source = get_bean().get("source").toString();
		elements.remove(source);
		get_bean().set("keys",prepareKeys(elements, labels));
		return new redirects(get_infoaction().getRedirect());
	}
	return new redirects(get_infoaction().getRedirect());
}

private Vector prepareKeys(HashMap elements, HashMap labels){
	Vector result=new Vector();
	Vector keys =new Vector(elements.keySet());
	for(int i=0;i<keys.size();i++){
		result.add(
				new option_element(keys.get(i).toString(),labels.get(keys.get(i)).toString())
			);
		
	}
	return result;
}
}
