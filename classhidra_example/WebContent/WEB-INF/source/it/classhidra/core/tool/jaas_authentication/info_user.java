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
import java.util.StringTokenizer;
import java.util.Vector;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.info_entity;
import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_sort;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class info_user extends info_entity implements i_elementBase{
	private static final long serialVersionUID = -2463122657123773030L;
	private String name;
	
	private String password;
	private String original;
	private String originalc;
	private String pass_expired;
	private String matriculation;
	private String language;
	private String group;
	private String target;
	private String crypted;
	private String wac_fascia;
	private String description;
	private String currentGroup;
	private String email;
	private HashMap _groups;
	private HashMap _targets;
	private Vector v_info_groups;
	private Vector v_info_targets;
	



	
	
	public info_user(){
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
			if(crypted!=null && crypted.trim().toUpperCase().equals("TRUE")) originalc=password;
			else original=password;
			
			if(crypted!=null && !crypted.trim().toUpperCase().equals("TRUE")) password = bsController.encrypt(password);
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}
	}

	
	public void reimposta(){
		name="";
		password="";
		pass_expired="";
		matriculation="";
		language="";
		group="";
		target="";
		crypted="";
		wac_fascia="";
		description="";
		currentGroup="";
		email="";		
		_groups=new HashMap();
		_targets=new HashMap();
		v_info_groups=new Vector();
		v_info_targets=new Vector();


	}
	public String getName() {
		return name;
	}
	public void setName(String string) {
		name = string;
	}
	public String getGroup() {
		return group;
	}

	public String getLanguage() {
		return language;
	}

	public String getPassword() {
		return password;
	}

	public String getTarget() {
		return target;
	}

	public String getWac_fascia() {
		return wac_fascia;
	}

	public void setGroup(String string) {
		group = string;
		if(group!=null){
			StringTokenizer st = new StringTokenizer(group.replace(';','^'),"^");
			while(st.hasMoreTokens()){
				String key = st.nextToken();
				if(_groups.get(key)==null){
					info_group iGroup = new info_group();
					iGroup.setName(key);
					_groups.put(key, iGroup);
				}			
			}
			v_info_groups = (new Vector(_groups.values()));
			v_info_groups = new util_sort().sort(v_info_groups,"name");

		}
	}

	public void refreshV_info_groups(){
		v_info_groups = (new Vector(_groups.values()));
		v_info_groups = new util_sort().sort(v_info_groups,"name");
	}
	public void refreshV_info_targets(){
		v_info_targets = (new Vector(_targets.values()));
		v_info_targets = new util_sort().sort(v_info_targets,"name");
	}	
	
	
	public void setLanguage(String string) {
		language = string;
	}

	public void setPassword(String string) {
		password = string;
		original = null;
		originalc = null;
	}

	public void setTarget(String string) {
		target = string;
		if(target!=null){
			StringTokenizer st = new StringTokenizer(target.replace(';','^'),"^");
			while(st.hasMoreTokens()){
				String key = st.nextToken();
				if(_targets.get(key)==null){
					info_target iTarget = new info_target();
					iTarget.setName(key);
					_targets.put(key, iTarget);
				}			
			}
			v_info_targets = (new Vector(_targets.values()));
			v_info_targets = new util_sort().sort(v_info_targets,"name");

		}
		
	}

	public void setWac_fascia(String string) {
		wac_fascia = string;
	}

	public String getMatriculation() {
		return matriculation;
	}

	public void setMatriculation(String string) {
		matriculation = string;
	}

	public String getCrypted() {
		return crypted;
	}
	public void setCrypted(String crypt) {
		this.crypted = crypt;
	}
	
	public String toString(){
		return toXml();
	}
	
	public String toXml(){
		String result=System.getProperty("line.separator")+"   ";
		try{
			result+= "<user";
			result+=System.getProperty("line.separator")+"      "+" name=\""+util_format.normaliseXMLText(getName())+"\"";
			if(original==null && originalc==null)
				result+=System.getProperty("line.separator")+"      "+" password=\""+util_format.normaliseXMLText(getPassword())+"\"";
			else if(getCrypted().toUpperCase().equals("TRUE")) 
				result+=System.getProperty("line.separator")+"      "+" password=\""+util_format.normaliseXMLText(getOriginalc())+"\"";
			else result+=System.getProperty("line.separator")+"      "+" password=\""+util_format.normaliseXMLText(getOriginal())+"\"";

			result+=System.getProperty("line.separator")+"      "+" pass_expired=\""+util_format.normaliseXMLText(getPass_expired())+"\"";
			result+=System.getProperty("line.separator")+"      "+" matriculation=\""+util_format.normaliseXMLText(getMatriculation())+"\"";
			result+=System.getProperty("line.separator")+"      "+" language=\""+util_format.normaliseXMLText(getLanguage())+"\"";
			if(v_info_groups==null || v_info_groups.size()==0)
				result+=System.getProperty("line.separator")+"      "+" group=\""+util_format.normaliseXMLText(getGroup().replace('^',';'))+"\"";
			else{
				result+=System.getProperty("line.separator")+"      "+" group=\"";
				for(int i=0;i<v_info_groups.size();i++){
					info_group current = (info_group)v_info_groups.get(i);
					result+=current.getName()+";";
				}
				result+="\"";
			}
			result+=System.getProperty("line.separator")+"      "+" target=\""+util_format.normaliseXMLText(getTarget().replace('^',';'))+"\"";
			result+=System.getProperty("line.separator")+"      "+" wac_fascia=\""+util_format.normaliseXMLText(getWac_fascia())+"\"";
			result+=System.getProperty("line.separator")+"      "+" description=\""+util_format.normaliseXMLText(getDescription())+"\"";
			result+=System.getProperty("line.separator")+"      "+" email=\""+util_format.normaliseXMLText(getEmail())+"\"";
			result+=System.getProperty("line.separator")+"      "+" crypted=\""+util_format.normaliseXMLText(getCrypted())+"\"";
			result+=super.toXml();
			
			result+="/>";
		}catch(Exception e){
			new bsControllerMessageException(e);		
		}
		return result;
		
	}

	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	


	public String getCurrentGroup() {
		return currentGroup;
	}

	public void setCurrentGroup(String currentGroup) {
		this.currentGroup = currentGroup;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Vector getV_info_groups() {
		return v_info_groups;
	}

	public void setV_info_groups(Vector vInfoGroups) {
		v_info_groups = vInfoGroups;
	}

	public HashMap get_groups() {
		return _groups;
	}

	public void set_groups(HashMap groups) {
		_groups = groups;
	}

	public String getPass_expired() {
		return pass_expired;
	}

	public void setPass_expired(String passExpired) {
		pass_expired = passExpired;
	}

	public HashMap get_targets() {
		return _targets;
	}

	public void set_targets(HashMap targets) {
		_targets = targets;
	}

	public Vector getV_info_targets() {
		return v_info_targets;
	}

	public void setV_info_targets(Vector vInfoTargets) {
		v_info_targets = vInfoTargets;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getOriginalc() {
		return originalc;
	}

	public void setOriginalc(String originalc) {
		this.originalc = originalc;
	}


	

}
