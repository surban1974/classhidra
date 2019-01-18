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

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.classhidra.annotation.elements.Expose;
import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;

public class info_call extends info_entity implements i_elementBase{
	private static final long serialVersionUID = -605542733311006711L;
	private String owner;
	private String name;	
	private String path;
	private String method;
	private String navigated;
	private String expose;
	private info_redirect iRedirect;
	private info_async iAsync;
	private List exposed;
	private List restmapping;
	private Map restParametersMapped;

	private boolean R_R = true;
	
	public info_call(){
		super();
		reimposta();
	}

	public info_call init(Node node, HashMap glob_redirects) throws bsControllerException{
		if(node==null) return this;
		try{
			NamedNodeMap nnm = node.getAttributes();	 		
			if (nnm!=null){
				for (int i=0;i<node.getAttributes().getLength();i++){
					String paramName = node.getAttributes().item(i).getNodeName();
					Node node_nnm =	nnm.getNamedItem(paramName);
					String sNodeValue = node_nnm.getNodeValue();
					if(sNodeValue!=null){
						sNodeValue=sNodeValue.replace('\n', ' ').replace('\r', ' ');
						sNodeValue = util_format.replace(sNodeValue, " ", "");
					}

					if(!setCampoValue(paramName,sNodeValue)){
						properties.put(paramName, sNodeValue);
					}
							
				}
			}
			NodeList nodeList = node.getChildNodes();
			
			for(int i=0;i<nodeList.getLength();i++){
				if(nodeList.item(i).getNodeType()==Node.ELEMENT_NODE){
					if(nodeList.item(i).getNodeName().toLowerCase().equals("redirect")){
						iRedirect = new info_redirect();
						iRedirect.init(nodeList.item(i));

						if(glob_redirects!=null && glob_redirects.get(iRedirect.getPath())==null){
							iRedirect.setOrder(Integer.valueOf(glob_redirects.size()).toString());
							if(iRedirect.getPath()!=null && !iRedirect.getPath().equals(""))
								glob_redirects.put(iRedirect.getPath(),iRedirect);
						}
					}
					if(nodeList.item(i).getNodeName().toLowerCase().equals("async")){
						iAsync = new info_async();
						iAsync.init(nodeList.item(i));
					}
					if(nodeList.item(i).getNodeName().toLowerCase().equals("rest")){
						info_rest iRest = new info_rest();
						iRest.init(nodeList.item(i));
						iRest.setMapped_entity(this);
						restmapping.add(iRest);
					}
				}
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_ERROR);
		}
		return this;
	}

	public void reimposta(){
		owner="";
		name="";
		path="";
		method="";
		navigated="";
		expose="";
		exposed=new ArrayList();
		restmapping=new ArrayList();
	}
	public String getName() {
		return name;
	}
	public info_call setName(String string) {
		name = string;
		return this;
	}

	public String toString(){
		return toXml();
	}
	
	public String toXml(){
		return toXml("");
	}
	
	public String toXml(String space){
		String result=System.getProperty("line.separator")+space+"      <call";
		if(owner!=null && !owner.trim().equals("")) result+=" owner=\""+util_format.normaliseXMLText(owner)+"\"";
		if(name!=null && !name.trim().equals("")) result+=" name=\""+util_format.normaliseXMLText(name)+"\"";
		if(path!=null && !path.trim().equals("")) result+=" path=\""+util_format.normaliseXMLText(path)+"\"";
		if(method!=null && !method.trim().equals("")) result+=" method=\""+util_format.normaliseXMLText(method)+"\"";
		if(exposed!=null && exposed.size()>0){
			result+=" expose=\"";
			for(int i=0;i<exposed.size();i++){
				if(i>0) result+=",";
				result+=exposed.get(i).toString();
			}
			result+="\"";
		}
		result+=super.toXml();
		result+=">";
		boolean ls=false;
		if(iRedirect!=null){
			result+=iRedirect.toXml(space+"            ");
			ls=true;
		}
		if(iAsync!=null){
			result+=iAsync.toXml(space+"            ");
			ls=true;
		}		
		if(restmapping!=null && restmapping.size()>0){
			for(int i=0;i<restmapping.size();i++){
				info_rest iRest = (info_rest)restmapping.get(i);
				if(iRest!=null){
					result+=iRest.toXml(space+"            ");
				}
			}
			ls=true;
		}
		
		result+= (ls)?(System.getProperty("line.separator")+space+"      </call>"):"</call>";
/*		
		if(iRedirect==null){
			if(restmapping!=null && restmapping.size()>0){
				for(int i=0;i<restmapping.size();i++){
					info_rest iRest = (info_rest)restmapping.get(i);
					if(iRest!=null){
						result+=iRest.toXml(space+"            ");
					}
				}
				result+=System.getProperty("line.separator")+space+"      </call>";
			}else			
				result+="/>";
		}else{
			result+=">";
			result+=iRedirect.toXml(space+"            ");
			if(restmapping!=null && restmapping.size()>0){
				for(int i=0;i<restmapping.size();i++){
					info_rest iRest = (info_rest)restmapping.get(i);
					if(iRest!=null){
						result+=iRest.toXml(space+"            ");
					}
				}
			}
			result+=System.getProperty("line.separator")+space+"      </call>";
		}
*/		
		return result;
	}
	

	
	public info_call setEnableDisable(int value){
		enabled=value;
		return this;
	}

	public String getMethod() {
		return method;
	}

	public info_call setMethod(String method) {
		this.method = method;
		return this;
	}

	public String getNavigated() {
		return navigated;
	}

	public info_call setNavigated(String navigated) {
		this.navigated = navigated;
		return this;
	}

	public String getOwner() {
		return owner;
	}

	public info_call setOwner(String owner) {
		this.owner = owner;
		return this;
	}

	public info_redirect getIRedirect() {
		return iRedirect;
	}

	public info_call setIRedirect(info_redirect iRedirect) {
		this.iRedirect = iRedirect;
		return this;
	}

	public String getPath() {
		return path;
	}

	public info_call setPath(String path) {
		this.path = path;
		return this;
	}

	public List getExposed() {
		if(exposed==null)
			exposed=new ArrayList();
		return exposed;
	}
	
	public info_call addExposed(String method) {
		if(method!=null && (method.equals(Expose.GET) || method.equals(Expose.POST) || method.equals(Expose.PATCH) || method.equals(Expose.PUT) || method.equals(Expose.DELETE)))
			getExposed().add(method);
		return this;
	}
	
	public info_call addExposed(String[] methods) {
		if(methods!=null){
			for(int i=0;i<methods.length;i++)
				addExposed(methods[i]);
		}
		return this;
	}	

	public void setExposed(List expose) {
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
			exposed=new ArrayList();
		exposed.clear();
		if(expose!=null){
			this.expose = expose;
			StringTokenizer st = new StringTokenizer(this.expose, ",");
			while(st.hasMoreTokens())
				exposed.add(st.nextToken().toUpperCase());
		}
		
	}
	
	public Map getRestParametersMapped(){
		if(restParametersMapped==null){
			restParametersMapped = new HashMap();
			if(restmapping!=null && restmapping.size()>0){
				for(int i=0;i<restmapping.size();i++){
					info_rest iRest = (info_rest)restmapping.get(i);
					restParametersMapped.putAll(iRest.getRestParametersMapped());
				}
			}
		}
		return restParametersMapped;
	}

	public List getRestmapping() {
		return restmapping;
	}

	public info_call setRestmapping(List restmapping) {
		this.restmapping = restmapping;
		return this;
	}


	public boolean isR_R() {
		return R_R;
	}

	public info_call setR_R(boolean r_R) {
		R_R = r_R;
		return this;
	}

	public info_async getiAsync() {
		return iAsync;
	}

	public info_call setiAsync(info_async iAsync) {
		this.iAsync = iAsync;
		return this;
	}

	
}
