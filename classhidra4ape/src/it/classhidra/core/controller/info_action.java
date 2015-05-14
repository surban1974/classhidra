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

import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_sort;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletContext;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class info_action extends info_entity implements i_elementBase{
	private static final long serialVersionUID = -6936168913529591521L;
	private String path;
	private String type;
	private String name;
	private String redirect;
	private String error;
	private String memoryInSession;
	private String memoryAsLastInstance;
	private String reloadAfterAction;
	private String reloadAfterNextNavigated;
	private String navigated;
	private String help;
	private String syncro;
	private String listener;
	private String statistic;


	private String wac;
	private HashMap _redirects;
	private HashMap _transformationoutput;
	private HashMap _auth_redirects;
	private HashMap _calls;
	private info_bean infobean;

	private Vector  v_info_redirects;
	private Vector  v_info_transformationoutput;
	private Vector  v_info_calls;
	
	private Vector  vm_streams;

	public info_action(){
		super();
		reimposta();
	}

	public void init(Node node, HashMap glob_redirects) throws bsControllerException{
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
			new bsControllerException(e,iStub.log_DEBUG);
		}
		NodeList nodeList = node.getChildNodes();
		int order_r=0;
		int order_t=0;
		for(int i=0;i<nodeList.getLength();i++){
			if(nodeList.item(i).getNodeType()==Node.ELEMENT_NODE){
				if(nodeList.item(i).getNodeName().toLowerCase().equals("redirect")){
					info_redirect iRedirect = new info_redirect();
					iRedirect.init(nodeList.item(i));
					order_r++;
					iRedirect.setOrder(Integer.valueOf(order_r).toString());
					_redirects.put(bodyURI(iRedirect.getPath()),iRedirect);
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
					iCall.init(nodeList.item(i));
					order_t++;
					iCall.setOrder(Integer.valueOf(order_t).toString());
					_calls.put(iCall.getName(),iCall);
				}

			}
		}

		v_info_transformationoutput.addAll(new Vector(_transformationoutput.values()));
		v_info_transformationoutput = new util_sort().sort(v_info_transformationoutput,"int_order");

		v_info_redirects.addAll(new Vector(_redirects.values()));
		v_info_redirects = new util_sort().sort(v_info_redirects,"int_order");

		v_info_calls.addAll(new Vector(_calls.values()));
		v_info_calls = new util_sort().sort(v_info_calls,"int_order");
		

		Object[] keys = _redirects.keySet().toArray();
		for (int i = 0; i < keys.length; i++){
			info_redirect iRedirect = (info_redirect)_redirects.get((String)keys[i]);
			iRedirect.init((info_redirect)glob_redirects.get((String)keys[i]));
			if(!iRedirect.getAuth_id().equals("")) _auth_redirects.put(iRedirect.getAuth_id(),iRedirect);
		}
	}
	public String bodyURI(String uri){
		if(uri==null || uri.indexOf("?")==-1) return uri;
		return uri.substring(0,uri.indexOf("?"));
	}
	public void reimposta(){
		path="";
		type="";
		name="";
		redirect="";
		error="";
		wac="";
		memoryInSession="";
		memoryAsLastInstance="";
		reloadAfterAction="";
		reloadAfterNextNavigated="";
		navigated="";
		help="";
		syncro="false";
		listener="";
		statistic="true";


		_redirects=new HashMap();
		_transformationoutput=new HashMap();
		_auth_redirects=new HashMap();
		_calls=new HashMap();

		v_info_redirects=new Vector();
		v_info_transformationoutput=new Vector();
		v_info_calls=new Vector();

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
	public HashMap get_redirects() {
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
		info_redirect iRedirect = new info_redirect();
		iRedirect.setPath(redirect);
		iRedirect.setSystem("true");
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

	public HashMap get_auth_redirects() {
		return _auth_redirects;
	}

	public HashMap get_transformationoutput() {
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
		if(redirect!=null && !redirect.trim().equals("")) result+=" redirect=\""+util_format.normaliseXMLText(redirect)+"\"";
		if(navigated!=null && !navigated.trim().equals("")) result+=" navigated=\""+util_format.normaliseXMLText(navigated)+"\"";
		if(syncro!=null && !syncro.trim().equals("")) result+=" syncro=\""+util_format.normaliseXMLText(syncro)+"\"";
		if(listener!=null && !listener.trim().equals("")) result+=" listener=\""+util_format.normaliseXMLText(listener)+"\"";
		
		if(memoryInSession!=null && !memoryInSession.trim().equals("")) result+=" memoryInSession=\""+util_format.normaliseXMLText(memoryInSession)+"\"";
		if(memoryAsLastInstance!=null && !memoryAsLastInstance.trim().equals("")) result+=" memoryAsLastInstance=\""+util_format.normaliseXMLText(memoryAsLastInstance)+"\"";

		if(reloadAfterAction!=null && !reloadAfterAction.trim().equals("")) result+=" reloadAfterAction=\""+util_format.normaliseXMLText(reloadAfterAction)+"\"";
		if(reloadAfterNextNavigated!=null && !reloadAfterNextNavigated.trim().equals("")) result+=" reloadAfterNextNavigated=\""+util_format.normaliseXMLText(reloadAfterNextNavigated)+"\"";
		if(help!=null && !help.trim().equals("")) result+=" help=\""+util_format.normaliseXMLText(help)+"\"";
		if(error!=null && !error.trim().equals("")) result+=" error=\""+util_format.normaliseXMLText(error)+"\"";
		result+=super.toXml();
		result+=">";
		boolean isEntity=false;
		if(v_info_redirects!=null && v_info_redirects.size()>0){

			for(int i=0;i<v_info_redirects.size();i++){
				info_redirect iRedirect = (info_redirect)v_info_redirects.get(i);
				if(iRedirect!=null && iRedirect.getSystem().equals("false")){
					result+=iRedirect.toXml();
					isEntity=true;
				}
			}
		}
		if(v_info_transformationoutput!=null && v_info_transformationoutput.size()>0){
			isEntity=true;
			for(int i=0;i<v_info_transformationoutput.size();i++){
				info_transformation iTransformation = (info_transformation)v_info_transformationoutput.get(i);
				if(iTransformation!=null) result+=iTransformation.toXml();
			}
		}
		if(v_info_calls!=null && v_info_calls.size()>0){

			for(int i=0;i<v_info_calls.size();i++){
				info_call iCall = (info_call)v_info_calls.get(i);
				if(iCall!=null && iCall.getSystem().equals("false")){
					result+=iCall.toXml();
					isEntity=true;
				}
			}
		}
		
		if(isEntity)
			result+=System.getProperty("line.separator")+"      </action>";
		else result+="</action>";


		return result;
	}

	public void syncroWithRelations(HashMap _elements){
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
		HashMap _redirects = (HashMap)_elements.get(path);
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
		int count_DISABLED_CHILD=0;
		for(int i=0;i<v_info_redirects.size();i++){
			info_redirect current = (info_redirect)v_info_redirects.get(i);
			if(current.getSystem().equals("false")){
				if(current.getEnabled()==CONST_ENABLED) count_ENABLED++;
				if(current.getEnabled()==CONST_DISABLED) count_DISABLED++;
				if(current.getEnabled()==CONST_DISABLED_CHILD) count_DISABLED_CHILD++;
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

	public Vector getV_info_redirects() {
		return v_info_redirects;
	}

	public void setV_info_redirects(Vector vInfoRedirects) {
		v_info_redirects = vInfoRedirects;
	}

	public Vector getV_info_transformationoutput() {
		return v_info_transformationoutput;
	}

	public void setV_info_transformationoutput(Vector vInfoTransformationoutput) {
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

	public Vector getVm_streams() {
		return vm_streams;
	}

	public void setVm_streams(Vector vmStreams) {
		vm_streams = vmStreams;
	}

	public HashMap get_calls() {
		return _calls;
	}

	public Vector getV_info_calls() {
		return v_info_calls;
	}

	public void setV_info_calls(Vector vInfoCalls) {
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







}
