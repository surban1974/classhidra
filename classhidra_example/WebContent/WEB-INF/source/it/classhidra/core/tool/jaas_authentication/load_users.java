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


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_externalloader;
import it.classhidra.core.init.app_init;
import it.classhidra.core.tool.elements.elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_blob;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_provider;
import it.classhidra.core.tool.util.util_sort;
import it.classhidra.core.tool.util.util_xml;

public class load_users extends elementBase{

	private static final long serialVersionUID = -1L;
	private HashMap _users;
	private HashMap _groups;
	private HashMap _targets;
	private HashMap _matriculation;
	private Vector _matr_groups;
	private boolean readOk_Resource=false;
	private boolean readOk_File=false;
	private boolean readOk_Db=false;
	private boolean readOk_ExtLoader=false;

	private String externalloader;
	private String xmlEncoding;

	private String loadedFrom="";


	private Vector v_info_users;
	private Vector v_info_groups;
	private Vector v_info_targets;

public load_users(){
	super();
	reimposta();
}

public void reimposta(){
	_users = new HashMap();
	_groups = new HashMap();
	_targets = new HashMap();
	_matr_groups = new Vector();
	_matriculation = new HashMap();
	v_info_users=new Vector();
	v_info_groups=new Vector();
	v_info_targets=new Vector();

	externalloader="";
	xmlEncoding="";
	readOk_Resource=false;
	readOk_File=false;
	readOk_Db=false;
	readOk_ExtLoader=false;
	
}

public void init() throws bsControllerException{

	String app_path="";
	app_init ainit = bsController.getAppInit(); 
	
	if(ainit.get_external_loader()!=null && !ainit.get_external_loader().equals("")){
		try{ 
			i_externalloader extl= (i_externalloader)util_provider.getInstanceFromProvider(new String[]{bsController.getAppInit().get_cdi_provider()}, ainit.get_external_loader());
			reInit(extl);
		}catch(Exception e){
			bsController.writeLog("Load_users from "+ainit.get_external_loader()+" ERROR "+e.toString(),iStub.log_ERROR);
		}catch(Throwable t){
			bsController.writeLog("Load_users from "+ainit.get_external_loader()+" ERROR "+t.toString(),iStub.log_ERROR);
		}
	}


	if(this.getExternalloader()!=null && !this.getExternalloader().equals("")){
		try{ 
			i_externalloader extl= (i_externalloader)util_provider.getInstanceFromProvider(new String[]{bsController.getAppInit().get_cdi_provider()}, this.getExternalloader());
			extl.load();
			reInit(extl);
		}catch(Exception e){
			bsController.writeLog("Load_users from "+this.getExternalloader()+" ERROR "+e.toString(),iStub.log_ERROR);
		}catch(Throwable t){
			bsController.writeLog("Load_users from "+this.getExternalloader()+" ERROR "+t.toString(),iStub.log_ERROR);
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
			(ainit.getSynonyms_path().getProperty(bsController.users_id_xml)==null)?bsController.users_id_xml:ainit.getSynonyms_path().getProperty(bsController.users_id_xml)
		);
	if(xml_name==null || xml_name.equals("")) 
		xml_name = System.getProperty(
				(ainit.getSynonyms_path().getProperty(app_path+bsController.users_id_xml)==null)?app_path+bsController.users_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.users_id_xml)
			);

	if(xml_name==null || xml_name.equals(""))
		xml_name = ainit.getResources_path().getProperty(
				(ainit.getSynonyms_path().getProperty(bsController.users_id_xml)==null)?bsController.users_id_xml:ainit.getSynonyms_path().getProperty(bsController.users_id_xml)
			);

	if(xml_name==null || xml_name.equals(""))
		xml_name = ainit.getResources_path().getProperty(
				(ainit.getSynonyms_path().getProperty(app_path+bsController.users_id_xml)==null)?app_path+bsController.users_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.users_id_xml)
			);


	if(xml_name!=null && !xml_name.equals("")){
		init(xml_name);
	}


}

public void init(String xml) throws bsControllerException{
	if(_users==null) _users=new HashMap();
	if(_matr_groups==null) _matr_groups=new Vector();

	if(_matriculation==null) _matriculation=new HashMap();
	try{
		Document documentXML = util_xml.readXML(xml);
		if(documentXML!=null){
			xmlEncoding = documentXML.getXmlEncoding();
			if(xmlEncoding==null) xmlEncoding="";
		}

		if(documentXML!=null){
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
			if(node==null) return;
			readFormElements(node);
		}
		bsController.writeLog("Load_users OK ",iStub.log_INFO);
		readOk_File=true;
		loadedFrom+=" "+xml;
	}catch(Exception e){
		readOk_File=false;
		bsController.writeLog("Load_users error: "+e.toString(),iStub.log_ERROR);
	}

}

public void initData(String xml) throws bsControllerException{
	if(_users==null) _users=new HashMap();
	if(_matr_groups==null) _matr_groups=new Vector();

	if(_matriculation==null) _matriculation=new HashMap();
	try{
		Document documentXML = util_xml.readXMLData(xml);
		if(documentXML!=null){
			xmlEncoding = documentXML.getXmlEncoding();
			if(xmlEncoding==null) xmlEncoding="";
		}

		if(documentXML!=null){
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
			if(node==null) return;
			readFormElements(node);

		}
		bsController.writeLog("Load_users OK ",iStub.log_INFO);
		readOk_File=true;
	}catch(Exception e){
		readOk_File=false;
		bsController.writeLog("Load_users error: "+e.toString(),iStub.log_ERROR);
	}

}

public boolean initDB(app_init ainit) throws bsControllerException{
	String app_path=ainit.get_path();
	if(app_path==null || app_path.equals("")) app_path="";
	else app_path+=".";

	String xmlData = null;
	boolean dbValid = false;
	
	try{
		xmlData = util_blob.load_from_config(
				(ainit.getSynonyms_path().getProperty(app_path+bsController.users_id_xml)==null)?app_path+bsController.users_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.users_id_xml),
				ainit.get_db_name());
		dbValid=true;
	}catch(Exception e){

	}
	try{		
		if(xmlData==null) xmlData = util_blob.load_from_config(
				(ainit.getSynonyms_path().getProperty(bsController.users_id_xml)==null)?bsController.users_id_xml:ainit.getSynonyms_path().getProperty(bsController.users_id_xml),
				ainit.get_db_name());
		dbValid=true;
	}catch(Exception e){

	}
	if(!dbValid)
		ainit.setDb_name_valid(false);
	
	if(xmlData==null) return false;

	if(_users==null) _users=new HashMap();
	if(_matr_groups==null) _matr_groups=new Vector();

	if(_matriculation==null) _matriculation=new HashMap();
	try{
		Document documentXML = util_xml.readXMLData(xmlData);
		if(documentXML!=null){
			xmlEncoding = documentXML.getXmlEncoding();
			if(xmlEncoding==null) xmlEncoding="";
		}

		if(documentXML!=null){
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
			readFormElements(node);

		}
		bsController.writeLog("Load_users OK ",iStub.log_INFO);
		readOk_Db=true;
	}catch(Exception e){
		readOk_Db=false;
		bsController.writeLog("Load_users error: "+e.toString(),iStub.log_ERROR);
	}
	return true;

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

private void readFormElements(Node node) throws Exception{
	if(node==null) return;

	if(node.getNodeName().equals("users")){
		this.initTop(node);
//		if(this.getExternalloader()!=null && !this.getExternalloader().equals("")){
//			try{
//				i_externalloader extl= (i_externalloader)Class.forName(this.getExternalloader()).newInstance();
//				extl.load();
//				reInit(extl);
//			}catch(Exception e){
//			}catch(Throwable t){
//			}
//		}
	}

	NodeList list = node.getChildNodes();
	for(int i=0;i<list.getLength();i++){
		Node child_node = list.item(i);
		if(child_node.getNodeType()== Node.ELEMENT_NODE){
			if(child_node.getNodeName().equals("user")){
				info_user iUser = new info_user();
				iUser.init(child_node);
				_users.put(bsController.getIdApp()+"/"+iUser.getName().toUpperCase()+"."+iUser.getPassword(),iUser);
				_matriculation.put(iUser.getMatriculation(),iUser);
				addGroup(iUser);
				addTarget(iUser);
			}
			if(child_node.getNodeName().equals("group")){
				info_group iGroup = new info_group();
				iGroup.init(child_node);
				if(iGroup.getName()!=null && !iGroup.getName().equals("")){
					if(_groups.get(iGroup.getName())!=null){
						info_group iOld = (info_group)_groups.get(iGroup.getName());
						iOld.reInit(iGroup);
					}else{
						_groups.put(iGroup.getName(), iGroup);
					}
				}
			}
			if(child_node.getNodeName().equals("target")){
				info_target iTarget = new info_target();
				iTarget.init(child_node);
				if(iTarget.getName()!=null && !iTarget.getName().equals("")){
					if(_targets.get(iTarget.getName())!=null){
						info_target iOld = (info_target)_targets.get(iTarget.getName());
						iOld.reInit(iTarget);
					}else{
						_targets.put(iTarget.getName(), iTarget);
					}
				}
			}

		}
	}
	v_info_users = (new Vector(_users.values()));
	v_info_users = new util_sort().sort(v_info_users,"name");

	v_info_groups = (new Vector(_groups.values()));
	v_info_groups = new util_sort().sort(v_info_groups,"name");

	v_info_targets = (new Vector(_targets.values()));
	v_info_targets = new util_sort().sort(v_info_targets,"name");


}

public void reInit(i_externalloader _externalloader){
	if(_externalloader==null) return;
	if(	_externalloader.getProperty(i_externalloader.USERS_users)!=null &&
		_externalloader.getProperty(i_externalloader.USERS_users) instanceof HashMap){
		_users.putAll((HashMap)_externalloader.getProperty(i_externalloader.USERS_users));
	}
	if(	_externalloader.getProperty(i_externalloader.USERS_groups)!=null &&
		_externalloader.getProperty(i_externalloader.USERS_groups) instanceof HashMap){
		_groups.putAll((HashMap)_externalloader.getProperty(i_externalloader.USERS_groups));
	}
	if(	_externalloader.getProperty(i_externalloader.USERS_targets)!=null &&
		_externalloader.getProperty(i_externalloader.USERS_targets) instanceof HashMap){
		_targets.putAll((HashMap)_externalloader.getProperty(i_externalloader.USERS_targets));
	}
	if(	_externalloader.getProperty(i_externalloader.USERS_matriculations)!=null &&
		_externalloader.getProperty(i_externalloader.USERS_matriculations) instanceof HashMap){
		_matriculation.putAll((HashMap)_externalloader.getProperty(i_externalloader.USERS_matriculations));
	}

	v_info_users = (new Vector(_users.values()));
	v_info_users = new util_sort().sort(v_info_users,"name");

	v_info_groups = (new Vector(_groups.values()));
	v_info_groups = new util_sort().sort(v_info_groups,"name");

	v_info_targets = (new Vector(_targets.values()));
	v_info_targets = new util_sort().sort(v_info_targets,"name");
	
	loadedFrom+=" "+_externalloader.getClass().getName();
//	readOk_ExtLoader=true;

}

public void refreshV_info_users(){
	v_info_users = (new Vector(_users.values()));
	v_info_users = new util_sort().sort(v_info_users,"name");
}
public void refreshV_info_groups(){
	v_info_groups = (new Vector(_groups.values()));
	v_info_groups = new util_sort().sort(v_info_groups,"name");
}
public void refreshV_info_targets(){
	v_info_targets = (new Vector(_targets.values()));
	v_info_targets = new util_sort().sort(v_info_targets,"name");
}

	private void addGroup(info_user iUser){
		StringTokenizer st = new StringTokenizer(iUser.getGroup().replace(';','^'),"^");
		while(st.hasMoreTokens()){
			String key = st.nextToken();
			if(key!=null && !key.equals("")){
				_matr_groups.add(iUser.getMatriculation()+"_"+key);
				if(_groups.get(key)==null){
					info_group iGroup = new info_group();
					iGroup.setName(key);
					iGroup.get_users().put(iUser.getName(), iUser);
					iGroup.refreshV_info_users();
					_groups.put(key, iGroup);
					if(iUser.get_groups().get(iGroup.getName())!=null){
						iUser.get_groups().put(iGroup.getName(), iGroup);
						iUser.refreshV_info_groups();
					}
						
				}else{
					info_group iGroup =(info_group)_groups.get(key);
					iGroup.get_users().put(iUser.getName(), iUser);
					iGroup.refreshV_info_users();
					if(iUser.get_groups().get(iGroup.getName())!=null){
						iUser.get_groups().put(iGroup.getName(), iGroup);
						iUser.refreshV_info_groups();
					}

				}
			}
		}
	}

	private void addTarget(info_user iUser){

			String key = iUser.getTarget();
			if(key==null || key.equals("")) return;

			if(_targets.get(key)==null){
				info_target iTarget = new info_target();
				iTarget.setName(key);
				iTarget.get_users().put(iUser.getName(), iUser);
				iTarget.refreshV_info_users();
				_targets.put(key, iTarget);
				if(iUser.get_targets().get(iTarget.getName())!=null){
					iUser.get_targets().put(iTarget.getName(), iTarget);
					iUser.refreshV_info_targets();
				}
			}else{
				info_target iTarget =(info_target)_targets.get(key);
				iTarget.get_users().put(iUser.getName(), iUser);
				iTarget.refreshV_info_users();
				if(iUser.get_targets().get(iTarget.getName())!=null){
					iUser.get_targets().put(iTarget.getName(), iTarget);
					iUser.refreshV_info_targets();
				}				
			}

	}


	public String checkTickerWithDate(String cript){

		for(int i=0;i<_matr_groups.size();i++){
			String orig = _matr_groups.get(i).toString()+"_"+util_format.dataToString(new java.util.Date(),"yyyyMMddhh");
			try{
				if(bsController.encrypt(orig).equals(cript)) return orig;
			}catch(Exception ex){
			}
		}
		return "";
	}

	public String checkTickerWithoutDate(String cript){

		for(int i=0;i<_matr_groups.size();i++){
			String orig = _matr_groups.get(i).toString();
			try{
				if(bsController.encrypt(orig).equals(cript)) return orig;
			}catch(Exception ex){
			}
		}
		return "";
	}

	public boolean isReadOk() {
		return readOk_File || readOk_Resource || readOk_Db || readOk_ExtLoader;
	}

	public boolean isReadError() {
		return !(isReadOk());
	}

	public void setReadError(boolean b) {
		readOk_File = b;
		readOk_Resource = b;
		readOk_Db = b;
		readOk_ExtLoader = b;
	}

	public HashMap get_users() {
		return _users;
	}

	public void set_users1(HashMap map) {
		_users = map;
	}

	public HashMap get_matriculation() {
		return _matriculation;
	}

	public void set_matriculation(HashMap map) {
		_matriculation = map;
	}
	public info_user get_user(Object first, Object second) {
		String key=null;
		String key_crypted=null;
		try{
			key = ((String)first).toUpperCase()+"."+bsController.encrypt((String)second);
			key_crypted = ((String)first).toUpperCase()+"."+(String)second;
		}catch(Exception e){
		}
		if(key==null || _users==null) return null;
		if(_users.get(bsController.getIdApp()+"/"+key)==null)
			return (info_user)_users.get(bsController.getIdApp()+"/"+key_crypted);
		return (info_user)_users.get(bsController.getIdApp()+"/"+key);
	}



	public void load_from_resources() {

		String property_name = "/config/"+bsController.CONST_XML_USERS;

		InputStream is = null;
	    BufferedReader br = null;
	    String result=null;
	    String line="";


	    try {
	    	is = getClass().getResourceAsStream(property_name);
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
	    		initData(result);
    			readOk_Resource = true;
    			bsController.writeLog("Load_users from "+property_name+" OK ",iStub.log_INFO);
    			loadedFrom+=" "+property_name;
    		}else{
    			readOk_Resource = false;
    		}
	    }catch (Exception e) {

		}
	}

	public String toString(){
		return toXml();
	}

	public String toXml(){
		String result="";
		if(xmlEncoding!=null && !xmlEncoding.equals(""))
			result+="<?xml version=\"1.0\" encoding=\""+xmlEncoding+"\"?>"+System.getProperty("line.separator");
		result+="<users";
		result+=" externalloader=\""+util_format.normaliseXMLText(externalloader)+"\"";
		result+=">";

		if(v_info_users!=null && v_info_users.size()>0){
			for(int i=0;i<v_info_users.size();i++){
				info_user entity = (info_user)v_info_users.get(i);
				if(entity!=null) result+=entity.toXml();
			}
		}
		if(v_info_groups!=null && v_info_groups.size()>0){
			for(int i=0;i<v_info_groups.size();i++){
				info_group entity = (info_group)v_info_groups.get(i);
				if(entity!=null) result+=entity.toXml();
			}
		}
		if(v_info_targets!=null && v_info_targets.size()>0){
			for(int i=0;i<v_info_targets.size();i++){
				info_target entity = (info_target)v_info_targets.get(i);
				if(entity!=null) result+=entity.toXml();
			}
		}

		result+=System.getProperty("line.separator")+"</users>";

		return result;
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

	public Vector getV_info_users() {
		return v_info_users;
	}

	public void setV_info_users(Vector vInfoUsers) {
		v_info_users = vInfoUsers;
	}

	public Vector get_matr_groups() {
		return _matr_groups;
	}

	public void set_matr_groups(Vector matrGroups) {
		_matr_groups = matrGroups;
	}

	public HashMap get_groups() {
		return _groups;
	}

	public void set_groups(HashMap groups) {
		_groups = groups;
	}

	public void set_users(HashMap users) {
		_users = users;
	}

	public Vector getV_info_groups() {
		return v_info_groups;
	}

	public void setV_info_groups(Vector vInfoGroups) {
		v_info_groups = vInfoGroups;
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

	public String getExternalloader() {
		return externalloader;
	}

	public void setExternalloader(String externalloader) {
		this.externalloader = externalloader;
	}

	public boolean isReadOk_Resource() {
		return readOk_Resource;
	}

	public boolean isReadOk_File() {
		return readOk_File;
	}

	public boolean isReadOk_Db() {
		return readOk_Db;
	}

	public boolean isReadOk_ExtLoader() {
		return readOk_ExtLoader;
	}

	public void setReadOk_Resource(boolean readOkResource) {
		readOk_Resource = readOkResource;
	}

	public void setReadOk_File(boolean readOkFile) {
		readOk_File = readOkFile;
	}

	public void setReadOk_Db(boolean readOkDb) {
		readOk_Db = readOkDb;
	}

	public void setReadOk_ExtLoader(boolean readOkExtLoader) {
		readOk_ExtLoader = readOkExtLoader;
	}
}
