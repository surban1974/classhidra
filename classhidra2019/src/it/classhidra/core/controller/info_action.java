/**
* Creation date: (07/04/2006)
* @author: Svyatoslav Urbanovych svyatoslav.urbanovych@gmail.com
*/

/********************************************************************************
*
*	Copyright (C) 2005  Svyatoslav Urbanovych
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.

* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*********************************************************************************/
package it.classhidra.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletContext;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.classhidra.annotation.elements.Expose;
import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.v2.Util_sort;

public class info_action extends info_entity implements i_elementBase{
	private static final long serialVersionUID = -6936168913529591521L;
	private String path;
	private String type;
	private String name;
	private String method;
	private String redirect;
	private String error;
	private String memoryInSession;	
	private String memoryInServletContext;
	private String memoryInServletContextLoadOnStartup;
	private String memoryAsLastInstance;
	private String reloadAfterAction;
	private String reloadAfterNextNavigated;
	private String navigated;
	private String navigatedMemoryContent;
	private String help;
	private String syncro;
	private String listener;
	private String statistic;
	private String expose;
	private List<String> exposed;
	private List<info_rest> restmapping;
	private info_redirect iRedirect;
	private info_async iAsync;

	private String wac;
	private HashMap<String,info_redirect> _redirects;
	private HashMap<String,info_transformation> _transformationoutput;
	private HashMap<String,info_redirect> _auth_redirects;
	private HashMap<String,info_call> _calls;
	private HashMap<String,info_bean> _beans;
	private info_bean infobean;

	private Vector<info_redirect>  v_info_redirects;
	private Vector<info_transformation>  v_info_transformationoutput;
	private Vector<info_call>  v_info_calls;
	private Vector<info_bean>  v_info_beans;
	
	private Vector<info_stream>  vm_streams;
	
	private boolean R_R = true;
	private Map<String,String> restParametersMapped;
	
	private HashMap<String,info_tlinked> _tlinked;
	private int checkTLinked = -1; 
	private String locked = "false"; 

	public info_action(){
		super();
		reimposta();
	}

	public void init(Node node, HashMap<String,info_redirect> glob_redirects) throws bsControllerException{
		init(node, glob_redirects, null);
	}
	
	public void init(Node node, HashMap<String,info_redirect> glob_redirects, HashMap<String,info_bean> glob_beans) throws bsControllerException{
		if(node==null) return;
		try{
			NamedNodeMap nnm = node.getAttributes();
			if (nnm!=null){
				for (int i=0;i<node.getAttributes().getLength();i++){
					String paramName = node.getAttributes().item(i).getNodeName();
					Node node_nnm =	nnm.getNamedItem(paramName);
					String sNodeValue = node_nnm.getNodeValue();
					if(sNodeValue!=null){
						sNodeValue=sNodeValue.replace('\n', ' ').replace('\r', ' ');
					}

					if(!setCampoValue(paramName,sNodeValue)){
						properties.put(paramName, sNodeValue);
					}
				}
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_ERROR);
		}
		NodeList nodeList = node.getChildNodes();
		int order_r=0;
		int order_t=0;
		int order_c=0;
		
		for(int i=0;i<nodeList.getLength();i++){
			if(nodeList.item(i).getNodeType()==Node.ELEMENT_NODE){
				if(nodeList.item(i).getNodeName().toLowerCase().equals("redirect")){
					info_redirect iRedirect = new info_redirect();
					iRedirect.init(nodeList.item(i));
					order_r++;
					iRedirect.setOrder(Integer.valueOf(order_r).toString());
					if(iRedirect.getPath()!=null && !iRedirect.getPath().equals(""))
						_redirects.put(bodyURI(iRedirect.getPath()),iRedirect);
					if(glob_redirects!=null && glob_redirects.get(iRedirect.getPath())==null){
						iRedirect.setOrder(Integer.valueOf(glob_redirects.size()).toString());
						if(iRedirect.getPath()!=null && !iRedirect.getPath().equals(""))
							glob_redirects.put(iRedirect.getPath(),iRedirect);
					}
					if(iRedirect!=null && !iRedirect.isEmpty()){
	    	    		if(getRedirect()==null || getRedirect().equals("")){
	    	    			if(iRedirect.getPath()!=null && !iRedirect.getPath().equals(""))
	    	    				setRedirect(iRedirect.getPath());
	    	    			setIRedirect(iRedirect);
	    	    		}
	        		}
				}
				if(nodeList.item(i).getNodeName().toLowerCase().equals("async")){
					info_async iAsync = new info_async();
					iAsync.init(nodeList.item(i));
				}
				if(nodeList.item(i).getNodeName().toLowerCase().equals("transformationoutput")){
					info_transformation iTransformationoutput = new info_transformation();
					iTransformationoutput.init(nodeList.item(i));
					order_t++;
					iTransformationoutput.setOrder(Integer.valueOf(order_t).toString());
					_transformationoutput.put(iTransformationoutput.getName(),iTransformationoutput);
				}
				if(nodeList.item(i).getNodeName().toLowerCase().equals("call")){
					info_call iCall = new info_call();
					iCall.init(nodeList.item(i), glob_redirects);
					order_c++;
					iCall.setOrder(Integer.valueOf(order_c).toString());
					iCall.setOwner(this.getPath());
					_calls.put(iCall.getName(),iCall);
				}
				if(nodeList.item(i).getNodeName().toLowerCase().equals("form-bean")){
					info_bean iBean = new info_bean();
					iBean.init(node.getChildNodes().item(i));
					_beans.put(iBean.getName(),iBean);
					if(glob_beans!=null && glob_beans.get(iBean.getName())==null){
						iBean.setOrder(Integer.valueOf(glob_beans.size()).toString());
						glob_beans.put(iBean.getName(),iBean);
					}
		    		if(iBean!=null && getName()==null || getName().equals("") && iBean.getName()!=null && !iBean.getName().equals(""))
		    			setName(iBean.getName());
				}
				if(nodeList.item(i).getNodeName().toLowerCase().equals("rest")){
					info_rest iRest = new info_rest();
					iRest.init(nodeList.item(i));
					iRest.setMapped_entity(this);
					restmapping.add(iRest);
				}

			}
		}

		v_info_transformationoutput.addAll(new Vector<info_transformation>(_transformationoutput.values()));
//		v_info_transformationoutput = new util_sort().sort(v_info_transformationoutput,"int_order");
		v_info_transformationoutput = Util_sort.sort(v_info_transformationoutput,"int_order");

		v_info_redirects.addAll(new Vector<info_redirect>(_redirects.values()));
//		v_info_redirects = new util_sort().sort(v_info_redirects,"int_order");
		v_info_redirects = Util_sort.sort(v_info_redirects,"int_order");

		v_info_calls.addAll(new Vector<info_call>(_calls.values()));
//		v_info_calls = new util_sort().sort(v_info_calls,"int_order");
		v_info_calls = Util_sort.sort(v_info_calls,"int_order");
		
		v_info_beans.addAll(new Vector<info_bean>(_beans.values()));
//		v_info_beans = new util_sort().sort(v_info_beans,"int_order");
		v_info_beans = Util_sort.sort(v_info_beans,"int_order");
		

		Object[] keys = _redirects.keySet().toArray();
		for (int i = 0; i < keys.length; i++){
			info_redirect iRedirect = (info_redirect)_redirects.get((String)keys[i]);
			iRedirect.init((info_redirect)glob_redirects.get((String)keys[i]));
			if(!iRedirect.getAuth_id().equals("")) _auth_redirects.put(iRedirect.getAuth_id(),iRedirect);
		}
	}
	public HashMap<String,info_bean> get_beans() {
		return _beans;
	}

	public Vector<info_bean> getV_info_beans() {
		return v_info_beans;
	}

	public void setV_info_beans(Vector<info_bean> v_info_beans) {
		this.v_info_beans = v_info_beans;
	}

	public String bodyURI(String uri){
		if(uri==null || uri.indexOf("?")==-1) return uri;
		return uri.substring(0,uri.indexOf("?"));
	}
	public void reimposta(){
		path="";
		type="";
		name="";
		method="";
		redirect="";
		error="";
		wac="";
		memoryInSession="";
		memoryInServletContext="";
		memoryInServletContextLoadOnStartup="-1";
		memoryAsLastInstance="";
		reloadAfterAction="";
		reloadAfterNextNavigated="";
		navigated="";
		navigatedMemoryContent="";
		help="";
		syncro="false";
		listener="";
		statistic="true";
		expose="";
		exposed=new ArrayList<String>();
		restmapping=new ArrayList<info_rest>();


		_redirects=new HashMap<String, info_redirect>();
		_transformationoutput=new HashMap<String, info_transformation>();
		_auth_redirects=new HashMap<String, info_redirect>();
		_calls=new HashMap<String, info_call>();
		_beans=new HashMap<String, info_bean>();

		v_info_redirects=new Vector<info_redirect>();
		v_info_transformationoutput=new Vector<info_transformation>();
		v_info_calls=new Vector<info_call>();
		v_info_beans=new Vector<info_bean>();

		_tlinked=new HashMap<String, info_tlinked>();
		checkTLinked=-1;
		locked = "false";
	}


	public i_transformation transformationFactory(String transformationName){
		return load_actions.transformationFactory(transformationName,_transformationoutput,null);
	}

	public i_transformation transformationFactory(String transformationName, ServletContext servletContext){
		return load_actions.transformationFactory(transformationName,_transformationoutput, servletContext);
	}

	public String getName() {
		return name;
	}
	public String getPath() {
		return path;
	}
	public String getRedirect() {
		return redirect;
	}
	public String getRedirect(String auth_id) {
		if(auth_id==null || _auth_redirects==null) return null;
		info_redirect iRedirect = (info_redirect)_auth_redirects.get(auth_id);
		if(iRedirect!=null) return iRedirect.getPath();
		return null;
	}	
	public HashMap<String,info_redirect> get_redirects() {
		return _redirects;
	}
	public String getType() {
		return type;
	}
	public void setName(String string) {
		name = string;
	}
	public void setPath(String string) {
		path = string;
	}
	public void setRedirect(String string) {
		redirect = string;
		if(redirect==null || redirect.equals(""))
			return;
		info_redirect iRedirect = new info_redirect();
		iRedirect.setPath(redirect);
		iRedirect.setSystem("true");
		if(_redirects.get(iRedirect.getPath())==null)
			_redirects.put(iRedirect.getPath(),iRedirect);
	}
	public void setType(String string) {
		type = string;
	}
	public info_bean getInfobean() {
		return infobean;
	}
	public void setInfobean(info_bean bean) {
		infobean = bean;
	}
	public String getError() {
		return error;
	}
	public void setError(String string) {
		error = string;
	}

	public String getWac() {
		return wac;
	}
	public void setWac(String string) {
		wac = string;
	}
	public String getMemoryInSession() {
		return memoryInSession;
	}
	public String getReloadAfterAction() {
		return reloadAfterAction;
	}
	public void setMemoryInSession(String string) {
		memoryInSession = string;
	}
	public void setReloadAfterAction(String string) {
		reloadAfterAction = string;
	}
	public String getReloadAfterNextNavigated() {
		return reloadAfterNextNavigated;
	}

	public void setReloadAfterNextNavigated(String reloadAfterNextNavigated) {
		this.reloadAfterNextNavigated = reloadAfterNextNavigated;
	}
	public String getNavigated() {
		return navigated;
	}
	public void setNavigated(String string) {
		navigated = string;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String string) {
		help = string;
	}

	public HashMap<String,info_redirect> get_auth_redirects() {
		return _auth_redirects;
	}

	public HashMap<String,info_transformation> get_transformationoutput() {
		return _transformationoutput;
	}

	public String toString(){
		return toXml();
	}

	public String toXml(){
		String result=System.getProperty("line.separator")+"      <action";
		if(path!=null && !path.trim().equals("")) result+=" path=\""+util_format.normaliseXMLText(path)+"\"";
		if(type!=null && !type.trim().equals("")) result+=" type=\""+util_format.normaliseXMLText(type)+"\"";
		if(name!=null && !name.trim().equals("")) result+=" name=\""+util_format.normaliseXMLText(name)+"\"";
		if(method!=null && !method.trim().equals("")) result+=" method=\""+util_format.normaliseXMLText(method)+"\"";
		if(redirect!=null && !redirect.trim().equals("")) result+=" redirect=\""+util_format.normaliseXMLText(redirect)+"\"";
		if(navigated!=null && !navigated.trim().equals("")) result+=" navigated=\""+util_format.normaliseXMLText(navigated)+"\"";
		if(navigatedMemoryContent!=null && !navigatedMemoryContent.trim().equals("")) result+=" navigatedMemoryContent=\""+util_format.normaliseXMLText(navigatedMemoryContent)+"\"";
		if(syncro!=null && !syncro.trim().equals("")) result+=" syncro=\""+util_format.normaliseXMLText(syncro)+"\"";
		if(listener!=null && !listener.trim().equals("")) result+=" listener=\""+util_format.normaliseXMLText(listener)+"\"";
		if(exposed!=null && exposed.size()>0){
			result+=" expose=\"";
			for(int i=0;i<exposed.size();i++){
				if(i>0) result+=",";
				result+=exposed.get(i).toString();
			}
			result+="\"";
		}
		
		if(memoryInSession!=null && !memoryInSession.trim().equals("")) result+=" memoryInSession=\""+util_format.normaliseXMLText(memoryInSession)+"\"";
		if(memoryInServletContext!=null && !memoryInServletContext.trim().equals("")) result+=" memoryInServletContext=\""+util_format.normaliseXMLText(memoryInServletContext)+"\"";
		if(memoryInServletContextLoadOnStartup!=null && !memoryInServletContextLoadOnStartup.trim().equals("")) result+=" memoryInServletContextLoadOnStartup=\""+util_format.normaliseXMLText(memoryInServletContextLoadOnStartup)+"\"";
				
		if(memoryAsLastInstance!=null && !memoryAsLastInstance.trim().equals("")) result+=" memoryAsLastInstance=\""+util_format.normaliseXMLText(memoryAsLastInstance)+"\"";

		if(reloadAfterAction!=null && !reloadAfterAction.trim().equals("")) result+=" reloadAfterAction=\""+util_format.normaliseXMLText(reloadAfterAction)+"\"";
		if(reloadAfterNextNavigated!=null && !reloadAfterNextNavigated.trim().equals("")) result+=" reloadAfterNextNavigated=\""+util_format.normaliseXMLText(reloadAfterNextNavigated)+"\"";
		if(help!=null && !help.trim().equals("")) result+=" help=\""+util_format.normaliseXMLText(help)+"\"";
		if(error!=null && !error.trim().equals("")) result+=" error=\""+util_format.normaliseXMLText(error)+"\"";
		if(locked!=null && !locked.trim().equals("")) result+=" locked=\""+util_format.normaliseXMLText(locked)+"\"";
		result+=super.toXml();
		result+=">";
		boolean isEntity=false;
		
		if(_beans!=null && _beans.size()>0){
			for(Object obj : Util_sort.sort(new Vector<info_bean>(_beans.values()),"int_order")){
				info_bean iBean = (info_bean)obj;
				if(iBean!=null){
					result+=iBean.toXml("      ");
					isEntity=true;
				}
			}
		}
		if(_redirects!=null && _redirects.size()>0){
			for(Object obj : Util_sort.sort(new Vector<info_redirect>(_redirects.values()),"int_order")){
				info_redirect iRedirect = (info_redirect)obj;
				if(iRedirect!=null && iRedirect.getSystem().equals("false")){
					result+=iRedirect.toXml("      ");
					isEntity=true;
				}
			}
		}	
		if(this.iRedirect!=null)
				result+=iRedirect.toXml("      ");
		
		if(this.iAsync!=null)
			result+=iAsync.toXml("      ");
		
		if(_transformationoutput!=null && _transformationoutput.size()>0){
			for(Object obj : Util_sort.sort(new Vector<info_transformation>(_transformationoutput.values()),"int_order")){
				info_transformation iTransformation = (info_transformation)obj;
				if(iTransformation!=null){
					result+=iTransformation.toXml("      ");
					isEntity=true;
				}
			}
		}		
		if(_calls!=null && _calls.size()>0){
			for(Object obj : Util_sort.sort(new Vector<info_call>(_calls.values()),"int_order")){
				info_call iCall = (info_call)obj;
				if(iCall!=null && iCall.getSystem().equals("false")){
					result+=iCall.toXml("      ");
					isEntity=true;
				}
			}
		}	
		if(restmapping!=null && restmapping.size()>0){
			for(int i=0;i<restmapping.size();i++){
				info_rest iRest = (info_rest)restmapping.get(i);
				if(iRest!=null){
					result+=iRest.toXml("      ");
					isEntity=true;
				}
			}
		}		
	
		if(isEntity)
			result+=System.getProperty("line.separator")+"      </action>";
		else result+="</action>";


		return result;
	}

	public void syncroWithRelations(HashMap<String, HashMap<String,HashMap<String,String>>> _elements){
		enabled = CONST_ENABLED;
		for(int i=0;i<v_info_redirects.size();i++){
			info_redirect current = (info_redirect)v_info_redirects.get(i);
			current.setEnableDisable(CONST_ENABLED);
		}

		if(_elements==null) return;
		if(_elements.get("*")!=null){
			setEnableDisable(CONST_DISABLED);
			return;
		}
		 HashMap<String,HashMap<String,String>> _redirects = ( HashMap<String,HashMap<String,String>>)_elements.get(path);
		if(_redirects==null) return;
		if(_redirects.get("*")!=null){
			setEnableDisable(CONST_DISABLED);
			return;
		}
		int dis=0;
		if(_redirects.size()>0) enabled = CONST_DISABLED_CHILD;
		for(int i=0;i<v_info_redirects.size();i++){
			info_redirect current = (info_redirect)v_info_redirects.get(i);
			if(current.getSystem().equals("false")){
				current.syncroWithRelations(_redirects);
				if(current.getEnabled()==CONST_DISABLED) dis++;
			}
		}
		if(dis==0) enabled = CONST_ENABLED;
		if(dis==v_info_redirects.size())  enabled = CONST_DISABLED;

	}

	public void setEnableDisable(int value){
		enabled=value;
		for(int i=0;i<v_info_redirects.size();i++){
			info_redirect current = (info_redirect)v_info_redirects.get(i);
			if(current.getSystem().equals("false"))
				current.setEnableDisable(value);
		}
	}
	public void refreshEnableDisableLevel(){
		int count_TOTAL=0;
		int count_ENABLED=0;
		int count_DISABLED=0;
//		int count_DISABLED_CHILD=0;
		for(int i=0;i<v_info_redirects.size();i++){
			info_redirect current = (info_redirect)v_info_redirects.get(i);
			if(current.getSystem().equals("false")){
				if(current.getEnabled()==CONST_ENABLED) count_ENABLED++;
				if(current.getEnabled()==CONST_DISABLED) count_DISABLED++;
//				if(current.getEnabled()==CONST_DISABLED_CHILD) count_DISABLED_CHILD++;
				count_TOTAL++;
			}
		}
		if(count_TOTAL==count_ENABLED){
			enabled=CONST_ENABLED;
			return;
		}
		if(count_TOTAL==count_DISABLED){
			enabled=CONST_DISABLED;
			return;
		}
		enabled=CONST_DISABLED_CHILD;
	}

	public Vector<info_redirect> getV_info_redirects() {
		return v_info_redirects;
	}

	public void setV_info_redirects(Vector<info_redirect> vInfoRedirects) {
		v_info_redirects = vInfoRedirects;
	}

	public Vector<info_transformation> getV_info_transformationoutput() {
		return v_info_transformationoutput;
	}

	public void setV_info_transformationoutput(Vector<info_transformation> vInfoTransformationoutput) {
		v_info_transformationoutput = vInfoTransformationoutput;
	}

	public String getSyncro() {
		return syncro;
	}

	public void setSyncro(String syncro) {
		this.syncro = syncro;
	}

	public String getStatistic() {
		return statistic;
	}

	public void setStatistic(String statistic) {
		this.statistic = statistic;
	}

	public Vector<info_stream> getVm_streams() {
		return vm_streams;
	}

	public void setVm_streams(Vector<info_stream> vmStreams) {
		vm_streams = vmStreams;
	}

	public HashMap<String,info_call> get_calls() {
		return _calls;
	}

	public Vector<info_call> getV_info_calls() {
		return v_info_calls;
	}

	public void setV_info_calls(Vector<info_call> vInfoCalls) {
		v_info_calls = vInfoCalls;
	}

	public String getListener() {
		return listener;
	}

	public void setListener(String _listener) {
		this.listener = _listener;
	}

	public String getMemoryAsLastInstance() {
		return memoryAsLastInstance;
	}

	public void setMemoryAsLastInstance(String _memoryAsLastInstance) {
		this.memoryAsLastInstance = _memoryAsLastInstance;
	}

	public String getNavigatedMemoryContent() {
		return navigatedMemoryContent;
	}

	public void setNavigatedMemoryContent(String navigatedMemoryContent) {
		this.navigatedMemoryContent = navigatedMemoryContent;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}



	public List<String> getExposed() {
		if(exposed==null)
			exposed=new ArrayList<String>();
		return exposed;
	}
	
	public info_action addExposed(String method) {
		if(method!=null && (method.equals(Expose.GET) || method.equals(Expose.POST) || method.equals(Expose.PATCH) || method.equals(Expose.PUT) || method.equals(Expose.DELETE)))
			getExposed().add(method);
		return this;
	}
	
	public info_action addExposed(String[] methods) {
		if(methods!=null){
			for(int i=0;i<methods.length;i++)
				addExposed(methods[i]);
		}
		return this;
	}	

	public void setExposed(List<String> expose) {
		this.exposed = expose;
	}

	public String getExpose() {
		if(exposed!=null && exposed.size()>0){
			String result="";
			for(int i=0;i<exposed.size();i++){
				if(i>0) result+=",";
				result+=exposed.get(i).toString();
			}
			return result;
		}
		return expose;
	}
	
	public boolean isExposed(String method){
		if(exposed==null || exposed.size()==0)
			return true;
		else{
			for(int i=0;i<exposed.size();i++){
				if(exposed.get(i)!=null && exposed.get(i).toString().equalsIgnoreCase(method))
					return true;
			}
		}
		return false;
	}

	public void setExpose(String expose) {
		if(exposed==null)
			exposed=new ArrayList<String>();
		exposed.clear();
		if(expose!=null){
			this.expose = expose;
			StringTokenizer st = new StringTokenizer(this.expose, ",");
			while(st.hasMoreTokens())
				exposed.add(st.nextToken().toUpperCase());
		}
		
	}
	
	public Map<String,String> getRestParametersMapped(){
		if(restParametersMapped==null){
			restParametersMapped = new HashMap<String,String>();
			if(restmapping!=null && restmapping.size()>0){
				for(int i=0;i<restmapping.size();i++){
					info_rest iRest = (info_rest)restmapping.get(i);
					restParametersMapped.putAll(iRest.getRestParametersMapped());
				}
			}
		}
		return restParametersMapped;
	}

	public List<info_rest> getRestmapping() {
		return restmapping;
	}

	public void setRestmapping(List<info_rest> restmapping) {
		this.restmapping = restmapping;
	}

	public info_redirect getIRedirect() {
		return iRedirect;
	}

	public void setIRedirect(info_redirect iRedirect) {
		this.iRedirect = iRedirect;
	}


	public boolean isR_R() {
		return R_R;
	}

	public void setR_R(boolean r_R) {
		R_R = r_R;
	}

	public info_async getiAsync() {
		return iAsync;
	}

	public void setiAsync(info_async iAsync) {
		this.iAsync = iAsync;
	}

	public String getMemoryInServletContext() {
		return memoryInServletContext;
	}

	public String getMemoryInServletContextLoadOnStartup() {
		return memoryInServletContextLoadOnStartup;
	}

	public void setMemoryInServletContextLoadOnStartup(String memoryInServletContextLoadOnStartup) {
		this.memoryInServletContextLoadOnStartup = memoryInServletContextLoadOnStartup;
	}

	public void setMemoryInServletContext(String memoryInServletContext) {
		this.memoryInServletContext = memoryInServletContext;
	}
	
	public int getLoadOnStartup() {
		try {
			return Integer.valueOf(memoryInServletContextLoadOnStartup).intValue();
		}catch (Exception e) {
			return -1;
		}
	}

	public HashMap<String,info_tlinked> get_tlinked() {
		return _tlinked;
	}

	public void set_tlinked(HashMap<String,info_tlinked> _tlinked) {
		this._tlinked = _tlinked;
	}

	public int getCheckTLinked() {
		return checkTLinked;
	}

	public void setCheckTLinked(int checkTLinked) {
		this.checkTLinked = checkTLinked;
	}

	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}




}
