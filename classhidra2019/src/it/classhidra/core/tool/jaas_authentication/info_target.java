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
package it.classhidra.core.tool.jaas_authentication;

import java.util.HashMap;
import java.util.Vector;

import it.classhidra.core.controller.info_entity;
import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.v2.Util_sort;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class info_target extends info_entity implements i_elementBase{
	private static final long serialVersionUID = -1L;
	private String name;
	private HashMap<String,info_user> _users;
	private Vector<info_user> v_info_users;

	
	
	public info_target(){
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
		_users=new HashMap<String, info_user>();
		v_info_users=new Vector<info_user>();

	}
	
	public void refreshV_info_users(){
		v_info_users = new Vector<info_user>(_users.values());
		v_info_users = Util_sort.sort(v_info_users,"name");
	}
	
	public String toString(){
		return toXml();
	}
	
	public String toXml(){
		String result=System.getProperty("line.separator")+"   ";
		try{
			result+= "<target";
			result+=" name=\""+util_format.normaliseXMLText(getName())+"\"";
			result+=super.toXml();		
			result+="/>"+System.getProperty("line.separator");
		}catch(Exception e){
			new bsControllerMessageException(e);		
		}
		return result;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String,info_user> get_users() {
		return _users;
	}

	public void set_users(HashMap<String,info_user> users) {
		_users = users;
	}

	public Vector<info_user> getV_info_users() {
		return v_info_users;
	}

	public void setV_info_users(Vector<info_user> vInfoUsers) {
		v_info_users = vInfoUsers;
	}
	public boolean convert2xml(){
		return true;
	}
}
