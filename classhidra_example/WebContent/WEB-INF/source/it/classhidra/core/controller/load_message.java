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

import it.classhidra.core.init.app_init;
import it.classhidra.core.tool.elements.elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.exception.message;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_blob;
import it.classhidra.core.tool.util.util_classes;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class load_message  extends elementBase{
	private static final long serialVersionUID = 1L;
	private HashMap _messages;
	private boolean readOk_Resource=false;
	private boolean readOk_Folder=false;
	private boolean readOk_File=false;
	private boolean readOk_ExtLoader=false;


	private String externalloader;
	private String view;

	private String loadedFrom="";




public load_message(){
	super();
	if(_messages==null) _messages=new HashMap();
	externalloader="";
	view="";
	readOk_Resource=false;
	readOk_Folder=false;
	readOk_File=false;
	readOk_ExtLoader=false;

}

public void reInit(i_externalloader _externalloader){ 
	if(_externalloader==null) return;
	if(	_externalloader.getProperty(i_externalloader.MESSAGES_messages)!=null &&
		_externalloader.getProperty(i_externalloader.MESSAGES_messages) instanceof HashMap){
		_messages.putAll((HashMap)_externalloader.getProperty(i_externalloader.MESSAGES_messages));
	}
	loadedFrom+=" "+_externalloader.getClass().getName();
//	readOk_ExtLoader=true;

}


public void init() throws bsControllerException{

	String app_path="";
	app_init ainit = bsController.getAppInit(); 
	
	if(ainit.get_external_loader()!=null && !ainit.get_external_loader().equals("")){
		try{ 
			i_externalloader extl= (i_externalloader)Class.forName(ainit.get_external_loader()).newInstance();
			reInit(extl);
		}catch(Exception e){
			bsController.writeLog("Load_messages from "+ainit.get_external_loader()+" ERROR "+e.toString(),iStub.log_ERROR);
		}catch(Throwable t){
			bsController.writeLog("Load_messages from "+ainit.get_external_loader()+" ERROR "+t.toString(),iStub.log_ERROR);
		}
	}


	if(this.getExternalloader()!=null && !this.getExternalloader().equals("")){
		try{ 
			i_externalloader extl= (i_externalloader)Class.forName(this.getExternalloader()).newInstance();
			reInit(extl);
		}catch(Exception e){
			bsController.writeLog("Load_messages from "+this.getExternalloader()+" ERROR "+e.toString(),iStub.log_ERROR);
		}catch(Throwable t){
			bsController.writeLog("Load_messages from "+this.getExternalloader()+" ERROR "+t.toString(),iStub.log_ERROR);
		}
	}

	

	try{

		if(ainit.get_db_name()!=null){
			if(initDB(ainit)){
				loadedFrom=ainit.get_db_name();
				return;
			}
		}

		app_path=ainit.get_path();
		if(app_path==null || app_path.equals("")) app_path="";
		else app_path+=".";
	}catch(Exception e){
	}

	String xml_name = System.getProperty(
			(ainit.getSynonyms_path().getProperty(bsController.messages_id_xml)==null)?bsController.messages_id_xml:ainit.getSynonyms_path().getProperty(bsController.messages_id_xml)
		);
	if(xml_name==null || xml_name.equals("")) 
		xml_name = System.getProperty(
				(ainit.getSynonyms_path().getProperty(app_path+bsController.messages_id_xml)==null)?app_path+bsController.messages_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.messages_id_xml)
			);

	if(xml_name==null || xml_name.equals(""))
		xml_name = ainit.getResources_path().getProperty(
				(ainit.getSynonyms_path().getProperty(bsController.messages_id_xml)==null)?bsController.messages_id_xml:ainit.getSynonyms_path().getProperty(bsController.messages_id_xml)
			);

	if(xml_name==null || xml_name.equals(""))
		xml_name = ainit.getResources_path().getProperty(
				(ainit.getSynonyms_path().getProperty(app_path+bsController.messages_id_xml)==null)?app_path+bsController.messages_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.messages_id_xml)
			);


	if(xml_name!=null && !xml_name.equals("")){
		initProperties(xml_name);
	}
	String folder_name = System.getProperty(bsController.messages_id_folder_xml);
	if(folder_name==null || folder_name.equals("")) folder_name = System.getProperty(app_path+bsController.messages_id_folder_xml);
	if(folder_name!=null && !folder_name.equals("")){
		initWithFOLDER(folder_name);
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
				if(res) bsController.writeLog("Load_messages from "+input.getAbsolutePath()+" OK ",iStub.log_INFO);
				readOk_File=readOk_File || res;

			}catch(Exception e){
			}
		}
		bsController.writeLog("Load_messages from "+input.getAbsolutePath()+" OK ",iStub.log_INFO);
		loadedFrom+=" "+dir;
	}
}
/*
public void initFromXML(String xml) throws bsControllerException{
	if(loadModeAsThread){
		MassageLoaderThreadProcess thProcess = new MassageLoaderThreadProcess(xml);
		thProcess.start();
	}else{
		initFromXMLThread(xml);
	}
}
*/
public boolean initWithData(String _xml) throws bsControllerException, Exception{
	Document documentXML = null;
	documentXML = util_xml.readXMLData(_xml);
	if(readDocumentXml(documentXML)) return true;
	else return false;
}

public boolean initWithFile(String _xml) throws bsControllerException, Exception{
	Document documentXML = null;
	documentXML = util_xml.readXML(_xml);
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
		Node node = null;
		try{
			int first=0;
			while(node==null && first < documentXML.getChildNodes().getLength()){
				if(documentXML.getChildNodes().item(first).getNodeType()== Node.ELEMENT_NODE)
					node = documentXML.getChildNodes().item(first);
				first++;
			}
		}catch(Exception e){}
		if(node==null) return false;
		if(node.getNodeName().equals("messages")){
			this.initTop(node);
//			if(this.getExternalloader()!=null && !this.getExternalloader().equals("")){
//				try{
//					i_externalloader extl= (i_externalloader)Class.forName(this.getExternalloader()).newInstance();
//					extl.load();
//					reInit(extl);
//				}catch(Exception e){
//				}catch(Throwable t){
//				}
//			}
		}

		NodeList list = node.getChildNodes();
		for(int i=0;i<list.getLength();i++){
			Node child_node = list.item(i);
			if(child_node.getNodeType()== Node.ELEMENT_NODE){

				message mess = new message();
				mess.init(child_node);
				_messages.put(mess.getCD_LANG()+"."+mess.getCD_MESS(),mess);
			}
		}
	}else return false;
	return true;
}

public void init(Node node) throws bsControllerException{
	if(node==null) return;
	if(node.getNodeType()!= Node.ELEMENT_NODE) return;
	message mess = new message();
	mess.init(node);
}

public HashMap get_messages() {
	return _messages;
}
public void set_messages(HashMap map) {
	_messages = map;
}
public boolean isReadOk() {
	return readOk_File || readOk_Folder || readOk_Resource || readOk_ExtLoader;
}


public void load_from_resources() { 
		load_from_resources("/config/"+bsController.CONST_XML_MESSAGES);
		String property_name =  "config."+bsController.CONST_XML_MESSAGES_FOLDER;
		ArrayList array = new ArrayList();

		try{
			array = util_classes.getResources(property_name);
		}catch(Exception e){
			util_format.writeToConsole(bsController.getLogInit(),"LoadMessages: Array.ERROR:"+e.toString());
		}

		for(int i=0;i<array.size();i++){
			String property_name0 =  bsController.CONST_XML_MESSAGES_FOLDER+"/"+array.get(i);
			if(property_name0!=null && property_name0.toLowerCase().indexOf(".xml")>-1)
				load_from_resources("/config/"+property_name0);
		}

}
private void load_from_resources(String property_name) {


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
	    		if(initWithData(result)){
	    			bsController.writeLog("Load_messages from "+property_name+" OK ",iStub.log_INFO);
	    			readOk_Resource = readOk_Resource || true;
	    			loadedFrom+= " "+property_name;
	    		}
	    	}
	    }catch (Exception e) {

		}

}

public boolean initDB(app_init ainit) throws bsControllerException, Exception{
		String app_path=ainit.get_path();
		if(app_path==null || app_path.equals("")) app_path="";
		else app_path+=".";
		
		String xmlData = null;
		try{
			xmlData = util_blob.load_from_config(
					(ainit.getSynonyms_path().getProperty(app_path+bsController.messages_id_xml)==null)?app_path+bsController.messages_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.messages_id_xml),
					ainit.get_db_name());
		}catch(Exception e){
			new bsException(e);
		}
		try{
			if(xmlData==null) xmlData = util_blob.load_from_config(
					(ainit.getSynonyms_path().getProperty(bsController.messages_id_xml)==null)?bsController.messages_id_xml:ainit.getSynonyms_path().getProperty(bsController.messages_id_xml),
					ainit.get_db_name());
		}catch(Exception e){
			new bsException(e);
		}
		if(xmlData==null) return false;

		Document documentXML = null;
		documentXML = util_xml.readXMLData(xmlData);
		if(readDocumentXml(documentXML)) return true;
		else return false;


}

	
public boolean isReadOk_File() {
	return readOk_File;
}

public void setReadOk_File(boolean readOk_File) {
	this.readOk_File = readOk_File;
}

public String getExternalloader() {
	return externalloader;
}

public void setExternalloader(String externalloader) {
	this.externalloader = externalloader;
}

public String getView() {
	return view;
}

public void setView(String view) {
	this.view = view;
}

public String getLoadedFrom() {
	return loadedFrom;
}

public boolean isReadOk_Resource() {
	return readOk_Resource;
}

public boolean isReadOk_Folder() {
	return readOk_Folder;
}

public boolean isReadOk_ExtLoader() {
	return readOk_ExtLoader;
}

public void setReadOk_Resource(boolean readOkResource) {
	readOk_Resource = readOkResource;
}

public void setReadOk_Folder(boolean readOkFolder) {
	readOk_Folder = readOkFolder;
}

public void setReadOk_ExtLoader(boolean readOkExtLoader) {
	readOk_ExtLoader = readOkExtLoader;
}

}
