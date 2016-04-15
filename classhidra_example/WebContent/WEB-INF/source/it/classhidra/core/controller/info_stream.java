/**
* Creation date: (29/01/2009)
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

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class info_stream extends info_entity implements i_elementBase{

	private static final long serialVersionUID = -1;
	private String type;
	private String name;
	private String listener;
	private info_redirect iRedirect;

	private HashMap _apply_to_action;
	private Vector v_info_apply_to_action;






	public info_stream(){
		super();
		reimposta();
	}
	
	public void init(Node node) throws bsControllerException{
		init(node,null);
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
					if (node_nnm!=null){
						if(!setCampoValue(paramName,sNodeValue)){
							properties.put(paramName, sNodeValue);
						}
					}
				}
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}
		NodeList nodeList = node.getChildNodes();
		int order=0;
		for(int i=0;i<nodeList.getLength();i++){
			if(nodeList.item(i).getNodeType()==Node.ELEMENT_NODE){
				if(nodeList.item(i).getNodeName().toLowerCase().equals("apply-to-action")){
					info_apply_to_action iApply = new info_apply_to_action();
					iApply.init(nodeList.item(i));
					order++;
					iApply.setOrder(Integer.valueOf(order).toString());
					if(iApply!=null)
						_apply_to_action.put(iApply.getAction(),iApply);
				}
				
				if(nodeList.item(i).getNodeName().toLowerCase().equals("redirect")){
					iRedirect = new info_redirect();
					iRedirect.init(nodeList.item(i));

					if(glob_redirects!=null && glob_redirects.get(iRedirect.getPath())==null){
						iRedirect.setOrder(Integer.valueOf(glob_redirects.size()).toString());
						if(iRedirect.getPath()!=null && !iRedirect.getPath().equals(""))
							glob_redirects.put(iRedirect.getPath(),iRedirect);
					}
				}
			}
		}
		v_info_apply_to_action.addAll(new Vector(_apply_to_action.values()));
		v_info_apply_to_action = new util_sort().sort(v_info_apply_to_action,"int_order");


	}


	public void reimposta(){
		type="";
		name="";
		listener="";
		_apply_to_action=new HashMap();
		v_info_apply_to_action=new Vector();



	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public void setName(String string) {
		name = string;
	}
	public void setType(String string) {
		type = string;
	}
	public HashMap get_apply_to_action() {
		return _apply_to_action;
	}





	public String toString(){
		return toXml();
	}

	public String toXml(){
		return toXml("");
	}
	public String toXml(String space){
		String result=System.getProperty("line.separator")+"      <stream";
		if(name!=null && !name.trim().equals("")) result+=" name=\""+util_format.normaliseXMLText(name)+"\"";
		if(type!=null && !type.trim().equals("")) result+=" type=\""+util_format.normaliseXMLText(type)+"\"";
		if(listener!=null && !listener.trim().equals("")) result+=" listener=\""+util_format.normaliseXMLText(listener)+"\"";
		result+=super.toXml();
		result+=">";
		boolean isEntity=false;
		if(v_info_apply_to_action!=null && v_info_apply_to_action.size()>0){
			isEntity=true;
			for(int i=0;i<v_info_apply_to_action.size();i++){
				info_apply_to_action iApply_to_action = (info_apply_to_action)v_info_apply_to_action.get(i);
				if(iApply_to_action!=null)
					result+=iApply_to_action.toXml(space+"            ");
			}
		}
		if(iRedirect!=null)
			result+=iRedirect.toXml(space+"            ");
		
		if(isEntity)
			result+=System.getProperty("line.separator")+"      </stream>";
		else result+="</stream>";


		return result;
	}

	public Vector getV_info_apply_to_action() {
		return v_info_apply_to_action;
	}

	public void setV_info_apply_to_action(Vector vInfoApplyToAction) {
		v_info_apply_to_action = vInfoApplyToAction;
	}

	public String getListener() {
		return listener;
	}

	public void setListener(String listener) {
		this.listener = listener;
	}

	public info_redirect getIRedirect() {
		return iRedirect;
	}

	public void setIRedirect(info_redirect iRedirect) {
		this.iRedirect = iRedirect;
	}
	
	

}
