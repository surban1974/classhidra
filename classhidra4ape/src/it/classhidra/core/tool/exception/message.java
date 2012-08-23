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

package it.classhidra.core.tool.exception;

import it.classhidra.core.tool.elements.elementDBBase;
import it.classhidra.core.tool.elements.i_elementDBBase;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;

import java.util.HashMap;
import java.util.Vector;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class message extends elementDBBase implements i_elementDBBase, java.io.Serializable{
	private static final long serialVersionUID = 1L;
	public String TYPE;
	public String CD_MESS;
	public String CD_LANG;
	public String DESC_MESS;
	private String _log_mess;
	private HashMap parameters;
	public String MESS_ID;


	public message(String _TYPE, String _CD_MESS, String _DESC_MESS) {
		super();
		reimposta();
		TYPE=_TYPE;
		CD_MESS=_CD_MESS;
		DESC_MESS=_DESC_MESS;
	}	
	
	public message() {
		super();
		reimposta();
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String value) {
		if(value==null) TYPE=value;
		else{
			if(value.length()>1) TYPE = value.substring(0,1);
			else TYPE = value;
		}
	}
	public String getCD_MESS() {
		return CD_MESS;
	}
	public void setCD_MESS(String value) {
		CD_MESS = value;
	
	}
	public String getCD_LANG() {
		return CD_LANG;
	}
	public void setCD_LANG(String value) {
		if(value==null) CD_LANG=value;
		else{
			if(value.length()>2) CD_LANG = value.substring(0,2);
			else CD_LANG = value;
		}
	}
	public String getDESC_MESS(HashMap _parameters) {	
		this.parameters=_parameters;
		return getDESC_MESS();
	}
	public String getDESC_MESS() {
		String mess_in_desc = DESC_MESS;
		mess_in_desc=decodeParameters(mess_in_desc,parameters);
		return mess_in_desc;
	}
	public void setDESC_MESS(String value) {
		if(value!=null) DESC_MESS=value;
		else DESC_MESS="";
	
	}
	public void setMess(String value) {
		DESC_MESS=value;
	}
	
	public void reimposta() {
		 super.reimposta_super();
		TYPE = "";
		CD_MESS = "";
		CD_LANG = "";
		DESC_MESS = "";
		_log_mess = "";	
		MESS_ID = "";
	}
	public void reInit(i_elementDBBase _i_el) {
		message el = (message)_i_el;
		this.setTYPE(el.getTYPE());
		this.setCD_MESS(el.getCD_MESS());
		this.setCD_LANG(el.getCD_LANG());
		this.setDESC_MESS(el.getDESC_MESS(null));
	}
	public String sql_Delete() {
		String result="";
	
		return result;
	}
	public String sql_Insert() {
	
		String result="";
	
		return result;
	}
	public String sql_Update(i_elementDBBase _i_element_mod) {
		String result="";
		return result;
	}
	public boolean equals(i_elementDBBase _i_el) {
		message el = (message)_i_el;
		if( el==null) return false;
		if( !this.control() && !el.control()) return true;
		return false;
	}
	public boolean control() {
		return true;
	}
	public String get_log_mess() {
		return _log_mess;
	}
	
	public void set_log_mess(String string) {
		_log_mess = string;
	}
	public void init(Node node) throws bsControllerException{
		if(node==null) return;
		try{
			NamedNodeMap nnm = node.getAttributes();	 		
			if (nnm!=null){
				for (int i=0;i<node.getAttributes().getLength();i++){
					String paramName = node.getAttributes().item(i).getNodeName();
					Node node_nnm =	nnm.getNamedItem(paramName);
					if (node_nnm!=null) setCampoValue(paramName.toUpperCase(),node_nnm.getNodeValue());							
				}
			}
			try{
				String fixedValue = ((Text)node.getFirstChild()).getData();
				if(!fixedValue.trim().equals("")) DESC_MESS = fixedValue;
			}catch(Exception e){			
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}
	}
	public String toString(){
		String result = "<message";
		result+=" cd_mess=\""+util_format.normaliseXMLText(CD_MESS)+"\"";
		result+=" cd_lang=\""+util_format.normaliseXMLText(CD_LANG)+"\"";
		if(!TYPE.equals("")) result+=" type=\""+util_format.normaliseXMLText(TYPE)+"\"";
		result+=" desc_mess=\""+util_format.normaliseXMLText(DESC_MESS)+"\"/>";
		return result;
	}
	
	public String toXml(){

		
		String result=System.getProperty("line.separator")+"<message";
		if(CD_MESS!=null && !CD_MESS.trim().equals("")) result+=" cd_mess=\""+util_format.normaliseXMLText(CD_MESS)+"\"";
		if(CD_LANG!=null && !CD_LANG.trim().equals("")) result+=" cd_lang=\""+util_format.normaliseXMLText(CD_LANG)+"\"";
		if(TYPE!=null && !TYPE.trim().equals("")) result+=" type=\""+util_format.normaliseXMLText(TYPE)+"\"";
		if(DESC_MESS!=null && !DESC_MESS.trim().equals("")) result+=" desc_mess=\""+util_format.normaliseXMLText(DESC_MESS)+"\"";

		result+=" />";

		
		
		return result;		
	}	
	
	public void setParameters(HashMap parameters) {
		this.parameters = parameters;
	}
	public static String decodeParameters(String mess_in_desc, HashMap parameters){
		if(parameters!=null){
			Vector p_keys = new Vector(parameters.keySet());
			for(int i=0;i<p_keys.size();i++){
				String key = (String)p_keys.get(i);
				String value=(String)parameters.get(key);
	//			mess_in_desc=mess_in_desc.replace("{"+key+"}", value);
				mess_in_desc=util_format.replace(mess_in_desc, "{"+key+"}", value);
			}		
		}	
		return mess_in_desc;
	}
	public String getMESS_ID() {
		return MESS_ID;
	}
	public void setMESS_ID(String mess_id) {
		MESS_ID = mess_id;
	}
}