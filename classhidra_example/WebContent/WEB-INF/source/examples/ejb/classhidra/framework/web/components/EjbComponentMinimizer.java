package examples.ejb.classhidra.framework.web.components;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.info_navigation;
import it.classhidra.core.controller.info_redirect;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.util.util_cloner;
import it.classhidra.framework.web.beans.option_element;



@Action (
		path="minimizer",
		name="formMinimizer",
		redirect="/jsp/framework/minimizer.jsp",
        statistic="false",
        entity=@Entity(
				property="allway:public"
		)
)

@Named("minimizer")
@SessionScoped
@Stateful
@Local(i_action.class)
public class EjbComponentMinimizer extends action implements i_action, Serializable{
	private static final long serialVersionUID = 77477796283694045L;

	private HashMap fromSessions;
	private HashMap fromLastInstance;
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
		info_navigation active = prepareDump(request);
		if(active!=null)
			return new redirects(active.getIAction().getPath()+"");
		else if(source!=null){
			String tmp = source;
			source = "";
			return new redirects(tmp);
		}
		else return new redirects(get_infoaction().getRedirect());

	}

	if(this.getMiddleAction().equals("open")){
/*
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
					fromSession_clone.remove("minimizer");
				}
				fromSessions.put(current.getIAction().getPath()+"", fromSession_clone);
				elements.put(current.getIAction().getPath()+"", util_cloner.clone(formInfoNavigation));
				labels.put(current.getIAction().getPath()+"", current.getDesc_second());
			}catch(Exception e){
			}

		}
*/


		HashMap fromSession_clone = (HashMap)fromSessions.get(source);
		if(fromSession_clone!=null){
			fromSession_clone.put(get_infoaction().getPath(), get_bean());
			bsController.setOnlySession(fromSession_clone, request);
			fromSessions.remove(source);
		}

		info_navigation fromElements = (info_navigation)elements.get(source);
		if(fromElements!=null){
			bsController.setToInfoNavigation(fromElements, request);
			elements.remove(source);
		}

		i_bean lastInstance = (i_bean)fromLastInstance.get(source);
		if(lastInstance!=null){
			request.getSession().setAttribute(bsConstants.CONST_BEAN_$ONLYASLASTINSTANCE,lastInstance);
			fromLastInstance.remove(source);
		}


		keys=prepareKeys(elements, labels);
		String tmp = source;
		source = "";
		return new redirects(tmp);
	}
	if(this.getMiddleAction().equals("delete")){
		fromSessions.remove(source);
		fromLastInstance.remove(source);
		elements.remove(source);
		source = "";
		keys=prepareKeys(elements, labels);
		return new redirects(get_infoaction().getRedirect());
	}
	return new redirects(get_infoaction().getRedirect());
}

private info_navigation prepareDump(HttpServletRequest request){
	info_navigation active = null;

	HashMap 		fromSessionGlobal = 	 new HashMap(bsController.getOnlySession(request));
	info_navigation	formInfoNavigation	= 	 bsController.getFromInfoNavigation(null, request);
	i_bean 			lastInstance = 			(i_bean)request.getSession().getAttribute(bsConstants.CONST_BEAN_$ONLYASLASTINSTANCE);
	i_bean 			lastInstance_clone = null;

	if(formInfoNavigation!=null){
		active = formInfoNavigation.getPrevIRedirect();
		if(active==null){
			active = formInfoNavigation;
		}else{
			info_navigation current = formInfoNavigation.lastIRedirect();
			if(source!=null && !current.getIAction().getPath().equals(source)){
				if(lastInstance!=null && lastInstance.get_infoaction().getPath().equals(source)){
					active = formInfoNavigation.lastIRedirect();
					current = new info_navigation();
					try{
						current.init(lastInstance.get_infoaction(), (info_redirect)lastInstance.get_infoaction().get_redirects().get(lastInstance.get_infoaction().getRedirect()), null, lastInstance);
						current.decodeMessage(request);
					}catch(Exception e){
					}
					try{
						lastInstance_clone = (i_bean)util_cloner.clone(lastInstance);
					}catch(Exception e){
					}
				}
			}
			try{
				HashMap fromSession_clone = new HashMap();
				if(fromSessionGlobal!=null){
					Iterator it = fromSessionGlobal.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry)it.next();
					    if(!pair.getKey().toString().equalsIgnoreCase(get_infoaction().getPath()))
					    	fromSession_clone.put(pair.getKey(), util_cloner.clone(pair.getValue()));
					}
				}

				fromSessions.put(current.getIAction().getPath(), fromSession_clone);
				elements.put(current.getIAction().getPath(), util_cloner.clone(formInfoNavigation));
				if(lastInstance_clone!=null)
					fromLastInstance.put(current.getIAction().getPath(), lastInstance_clone);
				labels.put(current.getIAction().getPath(), current.getDesc_second());
				keys=prepareKeys(elements, labels);

			}catch(Exception e){
			}
		}
	}

	return active;
}

private Vector prepareKeys(HashMap elements, HashMap labels){
	Vector result=new Vector();
	Vector keys =new Vector(elements.keySet());
	for(int i=0;i<keys.size();i++){
		try{
			result.add(
					new option_element(keys.get(i).toString(),labels.get(keys.get(i)).toString())
				);
		}catch(Exception e){
		}

	}
	return result;
}

public void reimposta(){
	fromLastInstance=new HashMap();
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

public HashMap getFromLastInstance() {
	return fromLastInstance;
}

public void setFromLastInstance(HashMap fromlastInstance) {
	this.fromLastInstance = fromlastInstance;
}




}
