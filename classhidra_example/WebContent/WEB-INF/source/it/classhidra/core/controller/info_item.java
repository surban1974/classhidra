/**
* Creation date: (05/07/2011)
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

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_sort;

public class info_item extends info_entity implements i_elementBase{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String type;
	private String defValue;
	private boolean collection=false;
	
	private HashMap _items;
	private Vector v_info_items;
	
	public info_item(){
		super();
		reimposta();
	}
	public void reimposta(){
		name="";
		type="";
		defValue="";
		collection=false;
		_items=new HashMap();
		v_info_items=new Vector();		
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
			NodeList nodeList = node.getChildNodes();
			boolean valueAsText = true;
			if(nodeList.getLength()==0){
				valueAsText=false;
				try{
					String fixedValue = ((Text)node.getFirstChild()).getData();
					if(!fixedValue.trim().equals("")) defValue = fixedValue;
				}catch(Exception e){			
				}
			}else{
				int order_r=0;
				for(int i=0;i<nodeList.getLength();i++){
					if(nodeList.item(i).getNodeType()==Node.ELEMENT_NODE){
						if(nodeList.item(i).getNodeName().toLowerCase().equals("item")){
							valueAsText=false;
							info_item iItem = new info_item();
							iItem.init(nodeList.item(i));
							order_r++;
							iItem.setOrder(Integer.valueOf(order_r).toString());
							iItem.setCollection(false);
							if(iItem!=null) _items.put(iItem.getName(),iItem);
						}
						if(nodeList.item(i).getNodeName().toLowerCase().equals("items")){
							valueAsText=false;
							info_item iItem = new info_item();
							iItem.init(nodeList.item(i));
							order_r++;
							iItem.setOrder(Integer.valueOf(order_r).toString());
							iItem.setCollection(true);
							if(iItem!=null) _items.put(iItem.getName(),iItem);
						}
					}
				}
				v_info_items.addAll(new Vector(_items.values()));
				v_info_items = new util_sort().sort(v_info_items,"int_order");
				
			}
			if(valueAsText){
				try{
					String fixedValue = ((Text)node.getFirstChild()).getData();
					if(!fixedValue.trim().equals("")) defValue = fixedValue;
				}catch(Exception e){			
				}
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_ERROR);
		}
	}	
	
	public String toString(){		
		return toXml();
	}
	
	public String toXml(){
		return toXml("");
	}

	public String toXml(String space){
		String result=System.getProperty("line.separator")+space+"            ";
		if(collection) result+="<items";
		else result+="<item";
		if(name!=null && !name.trim().equals("")) result+=" name=\""+util_format.normaliseXMLText(name)+"\"";
		if(type!=null && !type.trim().equals("")) result+=" type=\""+util_format.normaliseXMLText(type)+"\"";
		result+=super.toXml();
		result+=">";
		result+=util_format.normaliseXMLText(defValue);

		if(v_info_items!=null && v_info_items.size()>0){
			for(int i=0;i<v_info_items.size();i++){
				info_item iItem = (info_item)v_info_items.get(i);
				if(iItem!=null) result+=iItem.toXml(space+"      ");
			}
		}

		
		if(collection) result+="</items>";
		else result+="</item>";
		return result;
	}	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDefValue() {
		return defValue;
	}
	public void setDefValue(String defValue) {
		this.defValue = defValue;
	}
	public boolean getCollection() {
		return collection;
	}
	public void setCollection(boolean collection) {
		this.collection = collection;
	}
	public HashMap get_items() {
		return _items;
	}
	public void set_items(HashMap _items) {
		this._items = _items;
	}
	public Vector getV_info_items() {
		return v_info_items;
	}
	public void setV_info_items(Vector v_info_items) {
		this.v_info_items = v_info_items;
	}	
}
