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

import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.jaas_authentication.info_group;
import it.classhidra.core.tool.jaas_authentication.info_target;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_sort;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class info_relation extends info_entity implements i_elementBase{
	private static final long serialVersionUID = 1L;
	public static String TYPE_FORBIDDEN = "forbidden";
	public static String TYPE_ALLOWED = "allowed";

	
	private String name;
	private String type;
	private String targets;
	private String groups;
	private String elements;
	private String middleactions;
	
	private HashMap _elements;
	private Vector v_elements;
	
	private HashMap _middleactions;
	private Vector v_middleactions;

	
	private HashMap _groups;
	private HashMap _targets;
	private Vector v_info_groups;
	private Vector v_info_targets;
	
	
	public info_relation(){
		super();
		reimposta();
	}
	public void reimposta(){
		name="";
		type=TYPE_FORBIDDEN;
		targets="";
		groups="";
		elements="";
		middleactions="";
		_elements=new HashMap();
		_middleactions=new HashMap();
		v_elements=new Vector();
		v_middleactions=new Vector();
		_groups=new HashMap();
		_targets=new HashMap();
		v_info_groups=new Vector();
		v_info_targets=new Vector();

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
						if(paramName.equals("elements")){
							sNodeValue=sNodeValue.replace('\n', ' ').replace('\r', ' ');
							sNodeValue = util_format.replace(sNodeValue, " ", "");
						}
						if(paramName.equals("middleactions")){
							sNodeValue=sNodeValue.replace('\n', ' ').replace('\r', ' ');
							sNodeValue = util_format.replace(sNodeValue, " ", "");
						}						
					}

					if(!setCampoValue(paramName,sNodeValue)){
						properties.put(paramName, sNodeValue);
					}
							
				}
			}
			try{
				String fixedValue = ((Text)node.getFirstChild()).getData();
				if(!fixedValue.trim().equals("")) elements+= fixedValue;
			}catch(Exception e){			
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_ERROR);
		}
		
		parse();
		
	}	
	
	
	
	public boolean isEmpty(){
		if(type==null || type.equals("")) 
			return true;
		if( (targets==null || targets.equals("")) &&
			(groups==null || groups.equals("")) 
		) return true;
		if( (elements==null || elements.equals("")) &&
			(middleactions==null || middleactions.equals("")) 
		) return true;
		return false;

	}
	
	public void parse(){
		if(targets!=null){
			_targets=new HashMap();
			StringTokenizer st=new StringTokenizer(targets,";");
			while(st.hasMoreTokens()){
				String key = st.nextToken();
				info_target iTarget = new info_target();
				iTarget.setName(key);
				if(_targets.get(iTarget.getName())!=null){
					info_target iOld = (info_target)_targets.get(iTarget.getName());
					iOld.reInit(iTarget);
				}else{
					_targets.put(iTarget.getName(), iTarget);
				}
			}
			refreshV_info_targets();
		}
		
		if(groups!=null){
			_groups=new HashMap();
			StringTokenizer st=new StringTokenizer(groups,";");
			while(st.hasMoreTokens()){
				String key = st.nextToken();
				info_group iGroup = new info_group();
				iGroup.setName(key);
				if(_groups.get(iGroup.getName())!=null){
					info_group iOld = (info_group)_groups.get(iGroup.getName());
					iOld.reInit(iGroup);
				}else{
					_groups.put(iGroup.getName(), iGroup);
				}				
			}
			refreshV_info_groups();
		}
		if(elements!=null && !elements.trim().equals("")){

			_elements=new HashMap();
			StringTokenizer st=new StringTokenizer(elements,";");
			while(st.hasMoreTokens()){
				String key = st.nextToken();
				key=key.trim();
				if(key.length()>0){

					StringTokenizer st_key = new StringTokenizer(key,".");
					String key_action="*";
					String key_redirect="*";
					String key_section="*";
					if(st_key.hasMoreTokens()) key_action = st_key.nextToken();
					if(st_key.hasMoreTokens()) key_redirect = st_key.nextToken();
					if(st_key.hasMoreTokens()) key_section = st_key.nextToken();

					HashMap _redirects = (HashMap)_elements.get(key_action);
					if(_redirects==null){
						_redirects=new HashMap();
						_elements.put(key_action, _redirects);
					}

					HashMap _sections = (HashMap)_redirects.get(key_redirect);
					if(_sections==null){
						_sections=new HashMap();
						_redirects.put(key_redirect, _sections);
					}

					_sections.put(key_section, key_section);

				}
			}
			refreshV_elements();
		}
		if(middleactions!=null && !middleactions.trim().equals("")){

			_middleactions=new HashMap();
			StringTokenizer st=new StringTokenizer(middleactions,";");
			while(st.hasMoreTokens()){
				String key = st.nextToken();
				key=key.trim();
				if(key.length()>0){

					StringTokenizer st_key = new StringTokenizer(key,".");
					String key_action="*";
					String key_middleaction="*";
					if(st_key.hasMoreTokens()) key_action = st_key.nextToken();
					if(st_key.hasMoreTokens()) key_middleaction = st_key.nextToken();

					HashMap _mactions = (HashMap)_middleactions.get(key_action);
					if(_mactions==null){
						_mactions=new HashMap();
						_middleactions.put(key_action, _mactions);
					}
					_mactions.put(key_middleaction, key_middleaction);
				}
			}
			refreshV_middleactions();
		}
	}
	
	public void addElement(String key){
		StringTokenizer st_key = new StringTokenizer(key,".");
		String key_action="*";
		String key_redirect="*";
		String key_section="*";
		if(st_key.hasMoreTokens()) key_action = st_key.nextToken();
		if(st_key.hasMoreTokens()) key_redirect = st_key.nextToken();
		if(st_key.hasMoreTokens()) key_section = st_key.nextToken();

		HashMap _redirects = (HashMap)_elements.get(key_action);
		if(_redirects==null){
			_redirects=new HashMap();
			_elements.put(key_action, _redirects);
		}

		HashMap _sections = (HashMap)_redirects.get(key_redirect);
		if(_sections==null){
			_sections=new HashMap();
			_redirects.put(key_redirect, _sections);
		}

		_sections.put(key_section, key_section);
		refreshV_elements();
	}
	
	public void addMiddleaction(String key){
		StringTokenizer st_key = new StringTokenizer(key,".");
		String key_action="*";
		String key_maction="*";
		if(st_key.hasMoreTokens()) key_action = st_key.nextToken();
		if(st_key.hasMoreTokens()) key_maction = st_key.nextToken();

		HashMap _mactions = (HashMap)_middleactions.get(key_action);
		if(_mactions==null){
			_mactions=new HashMap();
			_middleactions.put(key_action, _mactions);
		}
		_mactions.put(key_maction, key_maction);
		refreshV_middleactions();
	}	
	
	public void removeElement(String key){
		StringTokenizer st_key = new StringTokenizer(key,".");
		String key_action="*";
		String key_redirect="*";
		String key_section="*";
		if(st_key.hasMoreTokens()) key_action = st_key.nextToken();
		if(st_key.hasMoreTokens()) key_redirect = st_key.nextToken();
		if(st_key.hasMoreTokens()) key_section = st_key.nextToken();

		if(key_action.equals("*")){
			_elements.clear();
			refreshV_elements();
			return;
		}
		
		HashMap _redirects = (HashMap)_elements.get(key_action);
		if(_redirects!=null){
			if(key_redirect.equals("*")){
				_elements.remove(key_action);
				refreshV_elements();
				return;
			}
			HashMap _sections = (HashMap)_redirects.get(key_redirect);
			if(_sections!=null){
				if(key_section.equals("*")){
					_redirects.remove(key_redirect);
					refreshV_elements();
					return;
				}else{
					_sections.remove(key_section);
					refreshV_elements();
					return;
				}
			}
		}

	}	
	public void removeMiddleaction(String key){
		StringTokenizer st_key = new StringTokenizer(key,".");
		String key_action="*";
		String key_maction="*";
		if(st_key.hasMoreTokens()) key_action = st_key.nextToken();
		if(st_key.hasMoreTokens()) key_maction = st_key.nextToken();


		
		HashMap _mactions = (HashMap)_middleactions.get(key_action);
		if(_mactions!=null){
			if(key_maction.equals("*")){
				_middleactions.remove(key_action);
				refreshV_middleactions();
				return;
			}
			_mactions.remove(key_maction);
			refreshV_middleactions();
			return;
		}
	}		
	
	public void refreshV_elements(){
		v_elements=new Vector();
		Vector v_actions = new Vector(_elements.keySet());
		for(int i=0;i<v_actions.size();i++){
			String key_action=(String)v_actions.get(i);
			HashMap _redirects = (HashMap)_elements.get(key_action);
			Vector v_redirects = new Vector(_redirects.keySet());
			for(int j=0;j<v_redirects.size();j++){
				String key_redirect=(String)v_redirects.get(j);
				HashMap _sections = (HashMap)_redirects.get(key_redirect);
				Vector v_sections = new Vector(_sections.keySet());
				for(int k=0;k<v_sections.size();k++){
					String key_section=(String)v_sections.get(k);
					v_elements.add(key_action+"."+key_redirect+"."+key_section);
				}
			}
		}
	}	
	public void refreshV_middleactions(){
		v_middleactions=new Vector();
		Vector v_actions = new Vector(_middleactions.keySet());
		for(int i=0;i<v_actions.size();i++){
			String key_action=(String)v_actions.get(i);
			HashMap _mactions = (HashMap)_middleactions.get(key_action);
			Vector v_mactions = new Vector(_mactions.keySet());
			for(int j=0;j<v_mactions.size();j++){
				String key_maction=(String)v_mactions.get(j);
				v_middleactions.add(key_action+"."+key_maction);
			}
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
	public String toString(){		
		return toXml();
	}

	public String toXml(){
		String result=System.getProperty("line.separator")+"   <relation";
		result+=" name=\""+util_format.normaliseXMLText(getName())+"\"";
		result+=" type=\""+util_format.normaliseXMLText(getType())+"\"";
//		if(targets!=null && !targets.trim().equals("")){
			result+=" targets=\"";
			for(int i=0;i<v_info_targets.size();i++){
				result+=((info_target)v_info_targets.get(i)).getName()+";";
			}
			result+="\"";
//		}
//		if(groups!=null && !groups.trim().equals("")){
			result+=" groups=\"";
			for(int i=0;i<v_info_groups.size();i++){
				result+=((info_group)v_info_groups.get(i)).getName()+";";
			}
			result+="\"";			
//		}
//		if(elements!=null && !elements.trim().equals("")){
			result+=" elements=\"";
			for(int i=0;i<v_elements.size();i++){
				result+=System.getProperty("line.separator")+"      "+v_elements.get(i)+";";
			}
			result+="\"";
			result+=" middleactions=\"";
			for(int i=0;i<v_middleactions.size();i++){
				result+=System.getProperty("line.separator")+"      "+v_middleactions.get(i)+";";
			}

			result+=System.getProperty("line.separator")+"      \"";
//		}
		result+=super.toXml();
		result+=">";

		result+=System.getProperty("line.separator")+"   </relation>";
		return result;
	}
	public String getTargets() {
		return targets;
	}
	public void setTargets(String targets) {
		this.targets = targets;
	}
	public String getGroups() {
		return groups;
	}
	public void setGroups(String groups) {
		this.groups = groups;
	}
	public String getElements() {
		return elements;
	}
	public void setElements(String elements) {
		this.elements = elements;
	}
	public Vector getV_elements() {
		return v_elements;
	}
	public void setV_elements(Vector vElements) {
		v_elements = vElements;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashMap get_elements() {
		return _elements;
	}
	public void set_elements(HashMap elements) {
		_elements = elements;
	}
	public HashMap get_groups() {
		return _groups;
	}
	public void set_groups(HashMap groups) {
		_groups = groups;
	}
	public HashMap get_targets() {
		return _targets;
	}
	public void set_targets(HashMap targets) {
		_targets = targets;
	}
	public Vector getV_info_groups() {
		return v_info_groups;
	}
	public void setV_info_groups(Vector vInfoGroups) {
		v_info_groups = vInfoGroups;
	}
	public Vector getV_info_targets() {
		return v_info_targets;
	}
	public void setV_info_targets(Vector vInfoTargets) {
		v_info_targets = vInfoTargets;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMiddleactions() {
		return middleactions;
	}
	public void setMiddleactions(String middleactions) {
		this.middleactions = middleactions;
	}
	public HashMap get_middleactions() {
		return _middleactions;
	}
	public void set_middleactions(HashMap _middleactions) {
		this._middleactions = _middleactions;
	}
	public Vector getV_middleactions() {
		return v_middleactions;
	}
	public void setV_middleactions(Vector v_middleactions) {
		this.v_middleactions = v_middleactions;
	}	
	

}
