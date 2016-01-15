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
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletContext;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import it.classhidra.core.init.app_init;
import it.classhidra.core.tool.elements.elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_blob;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_provider;
import it.classhidra.core.tool.util.util_xml;

public class load_authentication extends elementBase{
	private static final long serialVersionUID = -1L;

	private HashMap _targets;
	private HashMap _targets_allowed;
	private HashMap _area;
	
	private HashMap _mtargets;
	private HashMap _mtargets_allowed;
	
	
	private boolean readOk_Resource=false;
	private boolean readOk_Folder=false;
	private boolean readOk_File=false;
	private boolean readOk_Db=false;
	private boolean readOk_ExtLoader=false;
	private String externalloader;
	private String area;

	private String xmlEncoding;
	private String loadedFrom="";

	private Vector v_info_relations;
	private Vector v_info_mactions;




public load_authentication(){
	super();
	reimposta();

}
public void reimposta(){
	_targets = new HashMap();
	_targets_allowed = new HashMap();
	_mtargets = new HashMap();
	_mtargets_allowed = new HashMap();
	
	_area = new HashMap();
	readOk_Resource=false;
	readOk_Folder=false;
	readOk_File=false;
	readOk_Db=false;
	readOk_ExtLoader=false;
	externalloader="";
	area="";
	xmlEncoding="";
	v_info_relations=new Vector();
}

public void reInit(i_externalloader _externalloader){
	if(_externalloader==null) return;
	if(	_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_targets)!=null &&
		_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_targets) instanceof HashMap){
		_targets.putAll((HashMap)_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_targets));
	}
	if(	_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_targets_allowed)!=null &&
		_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_targets_allowed) instanceof HashMap){
		_targets_allowed.putAll((HashMap)_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_targets_allowed));
	}
	
	if(	_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_mtargets)!=null &&
		_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_mtargets) instanceof HashMap){
		_mtargets.putAll((HashMap)_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_mtargets));
	}
	if(	_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_mtargets_allowed)!=null &&
		_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_mtargets_allowed) instanceof HashMap){
		_mtargets_allowed.putAll((HashMap)_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_mtargets_allowed));
	}
	
	if(	_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_area)!=null &&
		_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_area) instanceof HashMap){
		_area.putAll((HashMap)_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_area));
	}
	
	if(	_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_info_relations)!=null &&
		_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_info_relations) instanceof Vector){
		v_info_relations.addAll((Vector)_externalloader.getProperty(i_externalloader.AUTHENTICATIONS_info_relations));
	}

	loadedFrom+=" "+_externalloader.getClass().getName();
//	readOk_ExtLoader=true;
}

public void init() throws bsControllerException{

	String app_path="";
	app_init ainit = bsController.getAppInit(); 
	
	if(ainit.get_external_loader()!=null && !ainit.get_external_loader().equals("")){
		try{ 
			i_externalloader extl= (i_externalloader)util_provider.getInstanceFromProvider(
							new String[]{
									bsController.getAppInit().get_context_provider(),
									bsController.getAppInit().get_cdi_provider(),
									bsController.getAppInit().get_ejb_provider()
							},
							ainit.get_external_loader());
			reInit(extl);
		}catch(Exception e){
			bsController.writeLog("Load_authentication from "+ainit.get_external_loader()+" ERROR "+e.toString(),iStub.log_ERROR);
		}catch(Throwable t){
			bsController.writeLog("Load_authentication from "+ainit.get_external_loader()+" ERROR "+t.toString(),iStub.log_ERROR);
		}
	}


	if(this.getExternalloader()!=null && !this.getExternalloader().equals("")){
		try{ 
			i_externalloader extl= (i_externalloader)util_provider.getInstanceFromProvider(
							new String[]{
									bsController.getAppInit().get_context_provider(),
									bsController.getAppInit().get_cdi_provider(),
									bsController.getAppInit().get_ejb_provider()
							},
							this.getExternalloader());
			extl.load();
			reInit(extl);
		}catch(Exception e){
			bsController.writeLog("Load_authentication from "+this.getExternalloader()+" ERROR "+e.toString(),iStub.log_ERROR);
		}catch(Throwable t){
			bsController.writeLog("Load_authentication from "+this.getExternalloader()+" ERROR "+t.toString(),iStub.log_ERROR);
		}
	}	


	try{

		if(ainit.get_db_name()!=null && ainit.isDb_name_valid()){
			if(initDB(ainit)){
				loadedFrom=ainit.get_db_name();
				readOk_Db=true;
				return;
			}
		}

		app_path=ainit.get_path();
		if(app_path==null || app_path.equals("")) app_path="";
		else app_path+=".";
	}catch(Exception e){
	}

	String xml_name = System.getProperty(
			(ainit.getSynonyms_path().getProperty(bsController.authentication_id_xml)==null)?bsController.authentication_id_xml:ainit.getSynonyms_path().getProperty(bsController.authentication_id_xml)
		);
	if(xml_name==null || xml_name.equals(""))
		xml_name = System.getProperty(
				(ainit.getSynonyms_path().getProperty(app_path+bsController.authentication_id_xml)==null)?app_path+bsController.authentication_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.authentication_id_xml)
			);

	if(xml_name==null || xml_name.equals(""))
		xml_name = ainit.getResources_path().getProperty(
				(ainit.getSynonyms_path().getProperty(bsController.authentication_id_xml)==null)?bsController.authentication_id_xml:ainit.getSynonyms_path().getProperty(bsController.authentication_id_xml)
			);

	if(xml_name==null || xml_name.equals(""))
		xml_name = ainit.getResources_path().getProperty(
				(ainit.getSynonyms_path().getProperty(app_path+bsController.authentication_id_xml)==null)?app_path+bsController.authentication_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.authentication_id_xml)
			);

	if(xml_name!=null && !xml_name.equals("")){
		initProperties(xml_name);
	}


}

public void initProperties(String xml) throws bsControllerException{
	try{
		if(initWithFile(xml)){
			readOk_File = true;
			loadedFrom+=" "+xml;
		}else readOk_File = false;
	}catch(Exception e){
		readOk_File=false;
	}
}
public void initWithFOLDER(String dir) throws bsControllerException{
	File input = new File(dir);
	if(input.exists()){
		File[] list = input.listFiles();
		for(int i=0;i<list.length;i++){
			try{
				boolean res = initWithFile(list[i].getAbsolutePath());
				if(res) bsController.writeLog("Load_authentication from "+input.getAbsolutePath()+" OK ",iStub.log_INFO);
				readOk_File=readOk_File || res;
			}catch(Exception e){
			}
		}
	}
	if(isReadOk()){
		loadedFrom+=" "+dir;
	}
}

public boolean initData(String _xml) throws bsControllerException, Exception{
	return initWithData(_xml);
}

private boolean initWithData(String _xml) throws bsControllerException, Exception{
	Document documentXML = null;
	documentXML = util_xml.readXMLData(_xml);
	if(documentXML!=null){
		xmlEncoding = documentXML.getXmlEncoding();
		if(xmlEncoding==null) xmlEncoding="";
	}
	if(readDocumentXml(documentXML)) return true;
	else return false;
}

public boolean initWithFile(String _xml) throws bsControllerException, Exception{
	Document documentXML = null;
	documentXML = util_xml.readXML(_xml);
	if(documentXML!=null){
		xmlEncoding = documentXML.getXmlEncoding();
		if(xmlEncoding==null) xmlEncoding="";
	}
	if(readDocumentXml(documentXML)) return true;
	else return false;
}

public boolean initDB(app_init ainit) throws bsControllerException, Exception{
	String app_path=ainit.get_path();
	if(app_path==null || app_path.equals("")) app_path="";
	else app_path+=".";
	
	String xmlData = null;
	boolean dbValid = false;
	
	try{
		xmlData = util_blob.load_from_config(
				(ainit.getSynonyms_path().getProperty(app_path+bsController.authentication_id_xml)==null)?app_path+bsController.authentication_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.authentication_id_xml),
				ainit.get_db_name()
			);
		dbValid=true;
	}catch(Exception e){

	}
	try{
		if(xmlData==null) xmlData = util_blob.load_from_config(
				(ainit.getSynonyms_path().getProperty(bsController.authentication_id_xml)==null)?bsController.authentication_id_xml:ainit.getSynonyms_path().getProperty(bsController.authentication_id_xml),
				ainit.get_db_name()
			);
		dbValid=true;
	}catch(Exception e){

	}
	if(!dbValid)
		ainit.setDb_name_valid(false);
	
	if(xmlData==null) return false;

	Document documentXML = null;
	documentXML = util_xml.readXMLData(xmlData);
	if(documentXML!=null){
		xmlEncoding = documentXML.getXmlEncoding();
		if(xmlEncoding==null) xmlEncoding="";
	}
	if(readDocumentXml(documentXML)) return true;
	else return false;


}

public void initTop(Node node) throws bsControllerException{
	if(node==null) return;
	try{
		NamedNodeMap nnm = node.getAttributes();
		if (nnm!=null){
			for (int i=0;i<node.getAttributes().getLength();i++){
				String paramName = node.getAttributes().item(i).getNodeName();
				Node node_nnm =	nnm.getNamedItem(paramName);
				if (node_nnm!=null) setCampoValue(paramName,node_nnm.getNodeValue());
			}
		}
	}catch(Exception e){
		new bsControllerException(e,iStub.log_DEBUG);
	}
}

private boolean readDocumentXml(Document documentXML) throws Exception{
	if(documentXML!=null){
		if(_targets==null)
			_targets = new HashMap();
		if(_targets_allowed==null)
			_targets_allowed = new HashMap();
		Node node = null;


		try{
			int first=0;
			while(node==null && first < documentXML.getChildNodes().getLength()){
				if(documentXML.getChildNodes().item(first).getNodeType()== Node.ELEMENT_NODE)
					node = documentXML.getChildNodes().item(first);
				first++;
			}
		}catch(Exception e){
			new bsControllerException(e,iStub.log_DEBUG);
		}
		if(node==null) return false;
		if(node.getNodeName().equals("authentication-forbidden") || node.getNodeName().equals("authentication")){
			this.initTop(node);
		}

		try{
				NamedNodeMap nnm = node.getAttributes();
				if (nnm!=null){
					for (int j=0;j<node.getAttributes().getLength();j++){
						String paramName = node.getAttributes().item(j).getNodeName();
						Node node_nnm =	nnm.getNamedItem(paramName);
						if(paramName.equals("area")){
							String sArea = node_nnm.getNodeValue();
							if(sArea!=null){
								sArea=sArea.replace('\n', ' ').replace('\r', ' ');
								sArea = util_format.replace(sArea, " ", "");
							}

							StringTokenizer st = new StringTokenizer(sArea,";");
							if(_area==null) _area=new HashMap();
							while(st.hasMoreTokens()) _area.put(st.nextToken(),Boolean.valueOf(true));
						}

					}
				}

		}catch(Exception e){}
		if(node==null) return false;
		int order=1;
		for(int i=0;i<node.getChildNodes().getLength();i++){
			if(node.getChildNodes().item(i).getNodeType()== Node.ELEMENT_NODE){
				readGroupElements(node.getChildNodes().item(i),order);
				order++;
			}
		}
	}else return false;
	return true;
}


public void addIRelation(info_relation iRelation) throws Exception{
	if(iRelation==null) return;

			v_info_relations.add(iRelation);

			String sTargets=iRelation.getTargets();
			String sGroups=iRelation.getGroups();
			String sElements=iRelation.getElements();
			String sMiddleactions=iRelation.getMiddleactions();


			if(sTargets!=null){
				sTargets=sTargets.replace('\n', ' ').replace('\r', ' ');
				sTargets = util_format.replace(sTargets, " ", "");
			}
			if(sGroups!=null){
				sGroups=sGroups.replace('\n', ' ').replace('\r', ' ');
				sGroups = util_format.replace(sGroups, " ", "");
			}
			if(sElements!=null){
				sElements=sElements.replace('\n', ' ').replace('\r', ' ');
				sElements = util_format.replace(sElements, " ", "");
			}
			if(sMiddleactions!=null){
				sMiddleactions=sMiddleactions.replace('\n', ' ').replace('\r', ' ');
				sMiddleactions = util_format.replace(sMiddleactions, " ", "");
			}
			

			if(!sTargets.equals("") && !sGroups.equals("") && !sElements.equals("")){

				HashMap _type_targets = null;
				if(iRelation.getType().equals(info_relation.TYPE_FORBIDDEN))
					_type_targets = _targets;
				if(iRelation.getType().equals(info_relation.TYPE_ALLOWED))
					_type_targets = _targets_allowed;

				StringTokenizer st_targets = new StringTokenizer(sTargets,";");
				while(st_targets.hasMoreTokens()){
					String target = st_targets.nextToken();
					if(_type_targets.get(target)==null) _type_targets.put(target,new HashMap());
					HashMap current_groups = (HashMap)_type_targets.get(target);
					StringTokenizer st_groups = new StringTokenizer(sGroups,";");
					while(st_groups.hasMoreTokens()){
						String group = st_groups.nextToken();
						if(current_groups.get(group)==null) current_groups.put(group,new HashMap());
						HashMap current_actions = (HashMap)current_groups.get(group);
						StringTokenizer st_elements = new StringTokenizer(sElements,";");
						while(st_elements.hasMoreTokens()){
							String element = st_elements.nextToken();
							StringTokenizer st_actions =new StringTokenizer(element,".");
							if(st_actions.hasMoreTokens()){
								String action = st_actions.nextToken();
								if(current_actions.get(action)==null) current_actions.put(action,new HashMap());
								HashMap redirects = (HashMap)current_actions.get(action);
								if(st_actions.hasMoreTokens()){
									String redirect = st_actions.nextToken();
									if(redirects.get(redirect)==null) redirects.put(redirect,new HashMap());
									HashMap sections = (HashMap)redirects.get(redirect);
									if(st_actions.hasMoreTokens()){
									
										String section = st_actions.nextToken();
										sections.put(section,Boolean.valueOf(true));
									}else{
										sections.put("*",Boolean.valueOf(true));
									}
								}else{
									HashMap sections = new HashMap();
									sections.put("*",Boolean.valueOf(true));
									redirects.put("*",sections);
								}
							}
						}
					}
				}
			}
			
			
			if(!sTargets.equals("") && !sGroups.equals("") && !sMiddleactions.equals("")){

				HashMap _type_targets = null;
				if(iRelation.getType().equals(info_relation.TYPE_FORBIDDEN))
					_type_targets = _mtargets;
				if(iRelation.getType().equals(info_relation.TYPE_ALLOWED))
					_type_targets = _mtargets_allowed;

				StringTokenizer st_targets = new StringTokenizer(sTargets,";");
				while(st_targets.hasMoreTokens()){
					String target = st_targets.nextToken();
					if(_type_targets.get(target)==null) _type_targets.put(target,new HashMap());
					HashMap current_groups = (HashMap)_type_targets.get(target);
					StringTokenizer st_groups = new StringTokenizer(sGroups,";");
					while(st_groups.hasMoreTokens()){
						String group = st_groups.nextToken();
						if(current_groups.get(group)==null) current_groups.put(group,new HashMap());
						HashMap current_actions = (HashMap)current_groups.get(group);
						StringTokenizer st_elements = new StringTokenizer(sMiddleactions,";");
						while(st_elements.hasMoreTokens()){
							String element = st_elements.nextToken();
							StringTokenizer st_actions =new StringTokenizer(element,".");
							if(st_actions.hasMoreTokens()){
								String action = st_actions.nextToken();
								if(current_actions.get(action)==null) current_actions.put(action,new HashMap());
								HashMap mactions = (HashMap)current_actions.get(action);
								if(st_actions.hasMoreTokens()){
									String maction = st_actions.nextToken();
									mactions.put(maction,maction);
								}else{
									mactions.put("*","*");
								}
							}
						}
					}
				}
			}			

}
private void readGroupElements(Node node, int order) throws Exception{
	if(node==null) return;

	if(node.getNodeName().equals("relation")){
		if(node.getNodeType()== Node.ELEMENT_NODE){

			info_relation iRelation = new info_relation();
			iRelation.init(node);
			iRelation.setOrder(Integer.valueOf(order).toString());
			if(iRelation.getName()==null || iRelation.getName().equals(""))
				iRelation.setName("relation: "+iRelation.getOrder());
			addIRelation(iRelation);
		}
	}
}
/*
private void readGroupElements(Node node, int order) throws Exception{
	if(node==null) return;

	if(node.getNodeName().equals("relation")){
		if(node.getNodeType()== Node.ELEMENT_NODE){

			info_relation iRelation = new info_relation();
			iRelation.init(node);
			iRelation.setOrder(Integer.valueOf(order).toString());
			if(iRelation.getName()==null || iRelation.getName().equals(""))
				iRelation.setName("relation: "+iRelation.getOrder());
			v_info_relations.add(iRelation);

			String sTargets=iRelation.getTargets();
			String sGroups=iRelation.getGroups();
			String sElements=iRelation.getElements();
			String sMiddleactions=iRelation.getMiddleactions();


			if(sTargets!=null){
				sTargets=sTargets.replace('\n', ' ').replace('\r', ' ');
				sTargets = util_format.replace(sTargets, " ", "");
			}
			if(sGroups!=null){
				sGroups=sGroups.replace('\n', ' ').replace('\r', ' ');
				sGroups = util_format.replace(sGroups, " ", "");
			}
			if(sElements!=null){
				sElements=sElements.replace('\n', ' ').replace('\r', ' ');
				sElements = util_format.replace(sElements, " ", "");
			}
			if(sMiddleactions!=null){
				sMiddleactions=sMiddleactions.replace('\n', ' ').replace('\r', ' ');
				sMiddleactions = util_format.replace(sMiddleactions, " ", "");
			}
			

			if(!sTargets.equals("") && !sGroups.equals("") && !sElements.equals("")){

				HashMap _type_targets = null;
				if(iRelation.getType().equals(info_relation.TYPE_FORBIDDEN))
					_type_targets = _targets;
				if(iRelation.getType().equals(info_relation.TYPE_ALLOWED))
					_type_targets = _targets_allowed;

				StringTokenizer st_targets = new StringTokenizer(sTargets,";");
				while(st_targets.hasMoreTokens()){
					String target = st_targets.nextToken();
					if(_type_targets.get(target)==null) _type_targets.put(target,new HashMap());
					HashMap current_groups = (HashMap)_type_targets.get(target);
					StringTokenizer st_groups = new StringTokenizer(sGroups,";");
					while(st_groups.hasMoreTokens()){
						String group = st_groups.nextToken();
						if(current_groups.get(group)==null) current_groups.put(group,new HashMap());
						HashMap current_actions = (HashMap)current_groups.get(group);
						StringTokenizer st_elements = new StringTokenizer(sElements,";");
						while(st_elements.hasMoreTokens()){
							String element = st_elements.nextToken();
							StringTokenizer st_actions =new StringTokenizer(element,".");
							if(st_actions.hasMoreTokens()){
								String action = st_actions.nextToken();
								if(current_actions.get(action)==null) current_actions.put(action,new HashMap());
								HashMap redirects = (HashMap)current_actions.get(action);
								if(st_actions.hasMoreTokens()){
									String redirect = st_actions.nextToken();
									if(redirects.get(redirect)==null) redirects.put(redirect,new HashMap());
									HashMap sections = (HashMap)redirects.get(redirect);
									if(st_actions.hasMoreTokens()){
									
										String section = st_actions.nextToken();
										sections.put(section,Boolean.valueOf(true));
									}else{
										sections.put("*",Boolean.valueOf(true));
									}
								}else{
									HashMap sections = new HashMap();
									sections.put("*",Boolean.valueOf(true));
									redirects.put("*",sections);
								}
							}
						}
					}
				}
			}
			
			
			if(!sTargets.equals("") && !sGroups.equals("") && !sMiddleactions.equals("")){

				HashMap _type_targets = null;
				if(iRelation.getType().equals(info_relation.TYPE_FORBIDDEN))
					_type_targets = _mtargets;
				if(iRelation.getType().equals(info_relation.TYPE_ALLOWED))
					_type_targets = _mtargets_allowed;

				StringTokenizer st_targets = new StringTokenizer(sTargets,";");
				while(st_targets.hasMoreTokens()){
					String target = st_targets.nextToken();
					if(_type_targets.get(target)==null) _type_targets.put(target,new HashMap());
					HashMap current_groups = (HashMap)_type_targets.get(target);
					StringTokenizer st_groups = new StringTokenizer(sGroups,";");
					while(st_groups.hasMoreTokens()){
						String group = st_groups.nextToken();
						if(current_groups.get(group)==null) current_groups.put(group,new HashMap());
						HashMap current_actions = (HashMap)current_groups.get(group);
						StringTokenizer st_elements = new StringTokenizer(sMiddleactions,";");
						while(st_elements.hasMoreTokens()){
							String element = st_elements.nextToken();
							StringTokenizer st_actions =new StringTokenizer(element,".");
							if(st_actions.hasMoreTokens()){
								String action = st_actions.nextToken();
								if(current_actions.get(action)==null) current_actions.put(action,new HashMap());
								HashMap mactions = (HashMap)current_actions.get(action);
								if(st_actions.hasMoreTokens()){
									String maction = st_actions.nextToken();
									mactions.put(maction,maction);
								}else{
									mactions.put("*","*");
								}
							}
						}
					}
				}
			}			
		}
	}
}
*/
public HashMap get_targets() {
	return _targets;
}
public void set_targets(HashMap map) {
	_targets = map;
}
public String toString(){
	return toXml();
}

public String toXml(){
	String result="";
	if(xmlEncoding!=null && !xmlEncoding.equals(""))
		result+="<?xml version=\"1.0\" encoding=\""+xmlEncoding+"\"?>"+System.getProperty("line.separator");
	result+="<authentication-forbidden";
	result+=" externalloader=\""+util_format.normaliseXMLText(externalloader)+"\"";
	if(area!=null && !area.trim().equals("")) result+=" area=\""+util_format.normaliseXMLText(area)+"\"";
	result+=">";
	if(v_info_relations!=null && v_info_relations.size()>0){
		for(int i=0;i<v_info_relations.size();i++){
			info_relation entity = (info_relation)v_info_relations.get(i);
			if(entity!=null) result+=entity.toXml();
		}
	}
	result+=System.getProperty("line.separator")+"</authentication-forbidden >";

	return result;
}
public HashMap get_area() {
	return _area;
}

public boolean isReadOk() {
	return readOk_File || readOk_Folder || readOk_Resource || readOk_Db || readOk_ExtLoader;
}

public void load_from_resources() { 
	load_from_resources("/config/"+bsController.CONST_XML_AUTHENTIFICATIONS);
	load_from_resources("/config/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_AUTHENTIFICATIONS);
	
	load_from_resources("META-INF/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_AUTHENTIFICATIONS);	
	load_from_resources("WEB-INF/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_AUTHENTIFICATIONS);
	
}
private boolean load_from_resources(String property_name) {


		InputStream is = null;
	    BufferedReader br = null;
	    String result=null;
	    String line="";


	    try {
	    	is = getClass().getResourceAsStream(property_name);
	    	if(is==null)
	    		is = this.getClass().getClassLoader().getResourceAsStream(property_name);
	    	if(is==null)
	    		is = ClassLoader.getSystemClassLoader().getResourceAsStream(property_name);
	    	if(is!=null){
	    		result="";
		    	br = new BufferedReader(new InputStreamReader(is));
		    	while (null != (line = br.readLine())) {
		    		result+=(line+"\n");
		    	}
	    	}
	    }catch (Exception e) {
	    }finally {
	    	try {
	    		if (br != null) br.close();
	    		if (is != null) is.close();
	    	}catch (Exception e) {
	    	}
		}

	    try{
	    	if(result!=null){
	    		if(initWithData(result)){
	    			readOk_Resource = true;
	    			bsController.writeLog("Load_authentication from "+property_name+" OK ",iStub.log_INFO);
	    			loadedFrom+=" "+property_name;
	    			return true;
	    		}else{
	    			readOk_Resource = false;
	    		}
	    	}
	    }catch (Exception e) {

		}
	    return false;
	}

public boolean load_from_resources(ServletContext ctx) {

	boolean read = load_from_resources(ctx,"/WEB-INF/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_AUTHENTIFICATIONS);
	if(!read) 
		read = load_from_resources(ctx,"/WEB-INF/config/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_AUTHENTIFICATIONS);
	if(!read) 
		read = load_from_resources(ctx,"/META-INF/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_AUTHENTIFICATIONS);
	if(!read) 
		read = load_from_resources(ctx,"/META-INF/config/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_AUTHENTIFICATIONS);

	return read;
}

private boolean load_from_resources(ServletContext ctx, String property_name) {

	InputStream is = null;
    BufferedReader br = null;
    String result=null;
    String line="";


    try {
    	is = ctx.getResourceAsStream(property_name);
      	if(is!=null){
    		result="";
	    	br = new BufferedReader(new InputStreamReader(is));
	    	while (null != (line = br.readLine())) {
	    		result+=(line+"\n");
	    	}
    	}
    }catch (Exception e) {
    	bsController.writeLog("Load_authentication from "+property_name+" ERROR "+e.toString(),iStub.log_ERROR);
    }finally {
    	try {
    		if (br != null) br.close();
    		if (is != null) is.close();
    	}catch (Exception e) {
    	}
	}

    if(result!=null){
	    try{
	    	if(initWithData(result)){
	    		bsController.writeLog("Load_authentication from "+property_name+" OK ",iStub.log_INFO);
	    		readOk_Resource = readOk_Resource || true;
	    		loadedFrom+=" "+property_name;
	    		return true;
	    	}
		}catch(Exception e){
			bsController.writeLog("Load_authentication from "+property_name+" ERROR "+e.toString(),iStub.log_ERROR);
		}
    }
    
    return false;

}

	public boolean isReadOk_File() {
		return readOk_File;
	}
	public String getExternalloader() {
		return externalloader;
	}
	public void setExternalloader(String externalloader) {
		this.externalloader = externalloader;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getLoadedFrom() {
		return loadedFrom;
	}
	public String getXmlEncoding() {
		return xmlEncoding;
	}
	public void setXmlEncoding(String xmlEncoding) {
		this.xmlEncoding = xmlEncoding;
	}
	public Vector getV_info_relations() {
		return v_info_relations;
	}
	public void setV_info_relations(Vector vInfoRelations) {
		v_info_relations = vInfoRelations;
	}
	public void setReadOk_File(boolean readOkFile) {
		readOk_File = readOkFile;
	}
	public boolean isReadOk_Resource() {
		return readOk_Resource;
	}
	public void setReadOk_Resource(boolean readOkResource) {
		readOk_Resource = readOkResource;
	}
	public boolean isReadOk_Folder() {
		return readOk_Folder;
	}
	public void setReadOk_Folder(boolean readOkFolder) {
		readOk_Folder = readOkFolder;
	}
	public boolean isReadOk_Db() {
		return readOk_Db;
	}
	public void setReadOk_Db(boolean readOkDb) {
		readOk_Db = readOkDb;
	}
	public void set_targets_allowed(HashMap targetsAllowed) {
		_targets_allowed = targetsAllowed;
	}
	public HashMap get_mtargets() {
		return _mtargets;
	}
	public void set_mtargets(HashMap _mtargets) {
		this._mtargets = _mtargets;
	}
	public HashMap get_mtargets_allowed() {
		return _mtargets_allowed;
	}
	public void set_mtargets_allowed(HashMap _mtargets_allowed) {
		this._mtargets_allowed = _mtargets_allowed;
	}
	public Vector getV_info_mactions() {
		return v_info_mactions;
	}
	public void setV_info_mactions(Vector v_info_mactions) {
		this.v_info_mactions = v_info_mactions;
	}
	public HashMap get_targets_allowed() {
		return _targets_allowed;
	}
	public boolean isReadOk_ExtLoader() {
		return readOk_ExtLoader;
	}
	public void setReadOk_ExtLoader(boolean readOkExtLoader) {
		readOk_ExtLoader = readOkExtLoader;
	}
}
