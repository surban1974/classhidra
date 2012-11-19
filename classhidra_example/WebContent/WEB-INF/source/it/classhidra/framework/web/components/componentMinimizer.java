package it.classhidra.framework.web.components; 

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.Bean;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.util.util_cloner;
import it.classhidra.core.controller.*;
import it.classhidra.framework.web.beans.option_element;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.*;
import javax.servlet.*;
  

@Bean (	name="formMinimizer")

@Action (
		path="minimizer",
		name="formMinimizer",
		redirect="/jsp/framework/minimizer.jsp",
        memoryInSession="true",
        entity=@Entity(
				property="allway:public"
		)	
)

public class componentMinimizer extends action implements i_action, Serializable{
	private static final long serialVersionUID = 77477796283694045L;

	private HashMap fromSessions;
	private HashMap elements;
	private HashMap labels;
	private Vector keys;
	private String source;


public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	
	if(this.getMiddleAction()==null) this.setMiddleAction("");
	
	if(this.getMiddleAction().equals("view")){
		return new redirects(get_infoaction().getRedirect());
	}
	if(this.getMiddleAction().equals("close")){
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
				keys=prepareKeys(elements, labels);
			}catch(Exception e){
			}
			
		}
	
		return new redirects(active.getIAction().getPath()+"");
	}
	if(this.getMiddleAction().equals("open")){
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
		
		
		
		HashMap fromSession_clone = (HashMap)fromSessions.get(source);
		fromSession_clone.put("formMinimizer", get_bean());
		request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYINSSESSION,fromSession_clone);
		
		info_navigation fromElements = (info_navigation)elements.get(source);
		if(fromElements!=null){
			request.getSession().setAttribute(bsController.CONST_BEAN_$NAVIGATION,fromElements);
		}
		elements.remove(source);
		keys=prepareKeys(elements, labels);

		return new redirects(source);
	}
	if(this.getMiddleAction().equals("delete")){
		elements.remove(source);
		keys=prepareKeys(elements, labels);
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

public void reimposta(){
	fromSessions=new HashMap();
	elements=new HashMap();
	labels=new HashMap();
	keys=new Vector();
	source="";
}

public redirects validate(HttpServletRequest request){
	return null;
}

public HashMap getElements() {
	return elements;
}

public void setElements(HashMap elements) {
	this.elements = elements;
}

public Vector getKeys() {
	return keys;
}

public void setKeys(Vector keys) {
	this.keys = keys;
}

public String getSource() {
	return source;
}

public void setSource(String source) {
	this.source = source;
}

public HashMap getLabels() {
	return labels;
}

public void setLabels(HashMap labels) {
	this.labels = labels;
}

public HashMap getFromSessions() {
	return fromSessions;
}

public void setFromSessions(HashMap fromSessions) {
	this.fromSessions = fromSessions;
}


}
