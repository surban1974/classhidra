/**
* Creation date: (10/09/2010)
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

import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;

public class info_rest extends info_entity implements i_elementBase{

	private static final long serialVersionUID = 1L;
	private String path;
	private String parametermapping;
	private String expose;
	private List<String> parameters;
	private List<String> exposed;
	private info_entity mapped_entity;
	

	public info_rest(){
		super();
		path="";
		parametermapping="";
		expose="";
		parameters=new ArrayList<String>();
		exposed=new ArrayList<String>();
	}





	public void init(Node node) throws bsControllerException{
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

				}
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_ERROR);
		}
	}

	public String toXml(){
		return toXml("");
	}
	public String toXml(String space){
		String result=System.getProperty("line.separator")+space+"      <restmapping ";
		if(path!=null && !path.trim().equals("")) result+=" path=\""+util_format.normaliseXMLText(path)+"\"";
		if(parametermapping!=null && !parametermapping.trim().equals("")) result+=" parametermapping=\""+util_format.normaliseXMLText(parametermapping)+"\"";
			
		result+="/>";

		return result;
	}

	public String getParametermapping() {
		return parametermapping;
	}
	public void setParametermapping(String parametermapping) {
		if(parametermapping==null) return;
		this.parametermapping = parametermapping;
		parameters=new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(parametermapping,"/");
		while(st.hasMoreTokens())
			parameters.add(st.nextToken());
	}
	
	public HashMap<String,String> mapParameterIfCorrect(String extparametermapping){
		if(extparametermapping==null) 
			return null;
		if(extparametermapping.equals("") || extparametermapping.equals("/")){
			if(parameters==null || parameters.size()==0)
				return new HashMap<String,String>();
			else
				return null;
		}

		List<String> extparameters=new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(extparametermapping,"/");
		while(st.hasMoreTokens())
			extparameters.add(st.nextToken());
		
		if(Math.min(parameters.size(),extparameters.size())>0){
			HashMap<String,String> map = new HashMap<String,String>();
			for(int i=0;i<Math.min(parameters.size(),extparameters.size());i++)
				map.put(parameters.get(i), extparameters.get(i));
			return map;
		}
		return null;
	}
	
	public HashMap<String,String> mapParameterAnyway(String extparametermapping){
		if(extparametermapping==null) 
			return new HashMap<String,String>();
		if(extparametermapping.equals("") || extparametermapping.equals("/"))
			return new HashMap<String,String>();

		List<String> extparameters=new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(extparametermapping,"/");
		while(st.hasMoreTokens())
			extparameters.add(st.nextToken());
		
		HashMap<String,String> map = new HashMap<String,String>();
		for(int i=0;i<Math.min(parameters.size(),extparameters.size());i++)
			map.put(parameters.get(i), extparameters.get(i));
		return map;
	}	

	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}



	public List<String> getParameters() {
		return parameters;
	}



	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}



	public info_entity getMapped_entity() {
		return mapped_entity;
	}



	public void setMapped_entity(info_entity mapped_entity) {
		this.mapped_entity = mapped_entity;
	}


	public List<String> getExposed() {
		if(exposed==null)
			exposed=new ArrayList<String>();
		return exposed;
	}
	


	public void setExposed(List<String> expose) {
		this.exposed = expose;
	}

	public String getExpose() {
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
		Map<String,String> result = new HashMap<String,String>();
		if(parameters!=null && parameters.size()>0){
			for(int i=0;i<parameters.size();i++)
				result.put(parameters.get(i).toString(), "");
			
		}
		return result;
	}

}
