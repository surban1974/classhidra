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
import java.util.Vector;

import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.v2.Util_sort;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class info_bean extends info_entity implements i_elementBase{
	private static final long serialVersionUID = -4323177999312177858L;
	private String type;
	private String name;
	private String model;
	private String listener;

	private HashMap<String,info_item> _items;
	private Vector<info_item> v_info_items;

	public info_bean(){
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
			new bsControllerException(e,iStub.log_ERROR);
		}
		NodeList nodeList = node.getChildNodes();
		int order_r=0;
		for(int i=0;i<nodeList.getLength();i++){
			if(nodeList.item(i).getNodeType()==Node.ELEMENT_NODE){
				if(nodeList.item(i).getNodeName().toLowerCase().equals("item")){
					info_item iItem = new info_item();
					iItem.init(nodeList.item(i));
					order_r++;
					iItem.setOrder(Integer.valueOf(order_r).toString());
					iItem.setCollection(false);
					if(iItem!=null) _items.put(iItem.getName(),iItem);
				}
				if(nodeList.item(i).getNodeName().toLowerCase().equals("items")){
					info_item iItem = new info_item();
					iItem.init(nodeList.item(i));
					order_r++;
					iItem.setOrder(Integer.valueOf(order_r).toString());
					iItem.setCollection(true);
					if(iItem!=null) _items.put(iItem.getName(),iItem);
				}
			}
		}
		v_info_items.addAll(new Vector<info_item>(_items.values()));
//		v_info_items = new util_sort().sort(v_info_items,"int_order");
		v_info_items = Util_sort.sort(v_info_items,"int_order");

	}


	public void reimposta(){
		type="";
		name="";
		model="";
		listener="";

		_items=new HashMap<String, info_item>();
		v_info_items=new Vector<info_item>();
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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}


	public String toString(){
		return toXml();
	}
	
	public String toXml(){
		return toXml("");
	}

	public String toXml(String space){
		String result=System.getProperty("line.separator")+space+"      <form-bean";
		if(type!=null && !type.trim().equals("")) result+=" type=\""+util_format.normaliseXMLText(type)+"\"";
		if(name!=null && !name.trim().equals("")) result+=" name=\""+util_format.normaliseXMLText(name)+"\"";
		if(model!=null && !model.trim().equals("")) result+=" model=\""+util_format.normaliseXMLText(model)+"\"";
		if(listener!=null && !listener.trim().equals("")) result+=" listener=\""+util_format.normaliseXMLText(listener)+"\"";
		result+=super.toXml();
		result+=">";
		boolean isEntity=false;
		if(v_info_items!=null && v_info_items.size()>0){
			isEntity=true;
			for(int i=0;i<v_info_items.size();i++){
				info_item iItem = (info_item)v_info_items.get(i);
				if(iItem!=null) result+=iItem.toXml(space);
			}
		}
		if(isEntity)
			result+=System.getProperty("line.separator")+space+"      </form-bean>";
		else result+="</form-bean>";

		return result;
	}

	public Vector<info_item> getV_info_items() {
		return v_info_items;
	}

	public void setV_info_items(Vector<info_item> vInfoItems) {
		v_info_items = vInfoItems;
	}

	public HashMap<String,info_item> get_items() {
		return _items;
	}

	public String getListener() {
		return listener;
	}

	public void setListener(String listener) {
		this.listener = listener;
	}







}
