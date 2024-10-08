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

import java.util.HashMap;

import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class info_section extends info_entity implements i_elementBase{
	private static final long serialVersionUID = -605542733311006711L;
	private String name;	
	private boolean allowed=true;
	
	public info_section(){
		super();
		reimposta();
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
						sNodeValue = util_format.replace(sNodeValue, " ", "");
					}

					if(!setCampoValue(paramName,sNodeValue)){
						properties.put(paramName, sNodeValue);
					}
							
				}
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}
	}

	public void reimposta(){
		name="";
		allowed=true;
	}
	public String getName() {
		return name;
	}
	public void setName(String string) {
		name = string;
	}

	public String toString(){
		return toXml();
	}
	
	public String toXml(){
		String result=System.getProperty("line.separator")+"            <section";
		if(name!=null && !name.trim().equals("")) result+=" name=\""+util_format.normaliseXMLText(name)+"\"";
		result+=super.toXml();
		result+="/>";
		return result;
	}
	
	public void syncroWithRelations(HashMap _elements){
		enabled = CONST_ENABLED;
		if(_elements==null) return;
		if(_elements.get("*")!=null){
			setEnableDisable(CONST_DISABLED);
			return;
		}
		String _sections = (String)_elements.get(name);
		if(_sections==null) return;
		if(_sections.equals("*") || _sections.equals(name)){
			setEnableDisable(CONST_DISABLED);
			return;
		}

	}	
	
	public void setEnableDisable(int value){
		enabled=value;
	}

	public boolean isAllowed() {
		return allowed;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}
	
}
