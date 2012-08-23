package it.classhidra.core.controller;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_externalloader;
import it.classhidra.core.init.app_init;
import it.classhidra.core.tool.elements.elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_blob;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_xml;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class load_organization extends elementBase{
	private static final long serialVersionUID = 1L;


	public static String CONST_XML_ORGANIZATIONS 	= "organizations.xml";
	public static String CONST_$ORGANIZATIONS 		= "$organizations";
	public static String CONST_$VISIBLE_NODES 		= "$organizations.visible.nodes";
	public static String CONST_$VISIBLE_NODES4SQL 	= "$organizations.visible.nodes4sql";

	private Vector nodes;
	private HashMap _nodes;
	private boolean readOk_Resource=false;
	private boolean readOk_Folder=false;
	private boolean readOk_File=false;
	private boolean readOk_Db=false;
	private boolean readOk_ExtLoader=false;
	private String loadedFrom="";
	private String xmlEncoding;

	private String externalloader;


	public load_organization(){
		super();
		reimposta();
	}
	public info_nodeorganization find(String _id_node){

		if(_nodes.get(_id_node)!=null) return (info_nodeorganization)_nodes.get(_id_node);

		for(int i=0;i<nodes.size();i++){
			info_nodeorganization current = (info_nodeorganization)nodes.get(i);
			info_nodeorganization founded = current.find(_id_node);
			if(founded!=null) return founded;
		}
		return null;
	}

	public boolean remove(String _id_node){

		info_nodeorganization founded = find(_id_node);

		if(founded==null) return false;
		if(founded.getParent()==null){
			_nodes.remove(founded.getId_node());
			for(int i=0;i<nodes.size();i++){
				info_nodeorganization current = (info_nodeorganization)nodes.get(i);
				if(current.getId_node().equals(founded.getId_node())){
					nodes.remove(i);
					return true;
				}
			}
		}else{
			info_nodeorganization parent = founded.getParent();
			parent.get_children().remove(founded.getId_node());
			for(int i=0;i<parent.getChildren().size();i++){
				info_nodeorganization current = (info_nodeorganization)parent.getChildren().get(i);
				if(current.getId_node().equals(founded.getId_node())){
					parent.getChildren().remove(i);
					return true;
				}
			}
		}
		return false;
	}

	public boolean add(info_nodeorganization parent, info_nodeorganization newNode){



		if(newNode==null) return false;
		if(parent==null){
			if(_nodes.get(newNode.getId_node())!=null) return false;
			_nodes.put(newNode.getId_node(), newNode);
			nodes.add(newNode);
			return true;
		}else{
			if(parent.get_children().get(newNode.getId_node())!=null) return false;
			parent.get_children().put(newNode.getId_node(), newNode);
			parent.getChildren().add(newNode);
			return true;
		}

	}

	public info_nodeorganization find_m(String _matriculation){

		for(int i=0;i<nodes.size();i++){
			info_nodeorganization current = (info_nodeorganization)nodes.get(i);
			info_nodeorganization founded = current.find_m(_matriculation);
			if(founded!=null) return founded;
		}
		return null;
	}

	public void reInit(i_externalloader _externalloader){
		if(_externalloader==null) return;
		if(	_externalloader.getProperty(i_externalloader.ORGANIZATIONS_nodes)!=null &&
			_externalloader.getProperty(i_externalloader.ORGANIZATIONS_nodes) instanceof HashMap){
			_nodes.putAll((HashMap)_externalloader.getProperty(i_externalloader.ORGANIZATIONS_nodes));
			nodes = new Vector(_nodes.values());			
		}
		loadedFrom+=" "+_externalloader.getClass().getName();
		readOk_ExtLoader=true;
	}

	public void reimposta(){
		_nodes = new HashMap();
		nodes = new Vector();
		externalloader="";
		xmlEncoding="";
		readOk_Resource=false;
		readOk_File=false;
		readOk_Folder=false;
		readOk_Db=false;
		readOk_ExtLoader=false;
	}

	public void init() throws bsControllerException{


		String app_path="";
		app_init ainit = bsController.getAppInit();
		try{

			if(ainit.get_db_name()!=null){
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
				(ainit.getSynonyms_path().getProperty(bsController.organization_id_xml)==null)?bsController.organization_id_xml:ainit.getSynonyms_path().getProperty(bsController.organization_id_xml)
			);
		if(xml_name==null || xml_name.equals("")) 
			xml_name = System.getProperty(
					(ainit.getSynonyms_path().getProperty(app_path+bsController.organization_id_xml)==null)?app_path+bsController.organization_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.organization_id_xml)
				);

		if(xml_name==null || xml_name.equals(""))
			xml_name = ainit.getResources_path().getProperty(
					(ainit.getSynonyms_path().getProperty(bsController.organization_id_xml)==null)?bsController.organization_id_xml:ainit.getSynonyms_path().getProperty(bsController.organization_id_xml)
				);

		if(xml_name==null || xml_name.equals(""))
			xml_name = ainit.getResources_path().getProperty(
					(ainit.getSynonyms_path().getProperty(app_path+bsController.organization_id_xml)==null)?app_path+bsController.organization_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.organization_id_xml)
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

	public void initData(String xml) throws bsControllerException, Exception{
		initWithData(xml);
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

	private boolean initWithFile(String _xml) throws bsControllerException, Exception{
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
		try{
			xmlData = util_blob.load_from_config(
					(ainit.getSynonyms_path().getProperty(app_path+bsController.organization_id_xml)==null)?app_path+bsController.organization_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.organization_id_xml),
					ainit.get_db_name());
		}catch(Exception e){
			new bsException(e);
		}
		try{			
			if(xmlData==null) xmlData = util_blob.load_from_config(
					(ainit.getSynonyms_path().getProperty(bsController.organization_id_xml)==null)?bsController.organization_id_xml:ainit.getSynonyms_path().getProperty(bsController.organization_id_xml),
					ainit.get_db_name());
		}catch(Exception e){
			new bsException(e);
		}
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
			}catch(Exception e){
				new bsControllerException(e,iStub.log_DEBUG);
			}
			if(node==null) return false;
			if(node.getNodeName().equals("organization")){
				this.initTop(node);
				if(this.getExternalloader()!=null && !this.getExternalloader().equals("")){
					try{
						i_externalloader extl= (i_externalloader)Class.forName(this.getExternalloader()).newInstance();
						extl.load();
						reInit(extl);
					}catch(Exception e){
					}catch(Throwable t){
					}
				}
			}

			try{
				for(int i=0;i<node.getChildNodes().getLength();i++){
					if(node.getChildNodes().item(i).getNodeType()== Node.ELEMENT_NODE && node.getChildNodes().item(i).getNodeName().equals("node-organization")){
						info_nodeorganization infoNode = new info_nodeorganization();
						infoNode.init(node.getChildNodes().item(i));
						if(infoNode.getId_node().equals("")) infoNode.setId_node(infoNode.getMatriculation());
						nodes.add(infoNode);
						_nodes.put(infoNode.getId_node(), infoNode);
					}
				}


			}catch(Exception e){}
			if(node==null) return false;
		}else return false;
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
	public void load_from_resources() {
		String property_name =  CONST_XML_ORGANIZATIONS;

		InputStream is = null;
	    BufferedReader br = null;
	    String result=null;
	    String line="";


	    try {
	    	Object obj = null;
	    	try{
	    		obj = Class.forName("config.Loader").newInstance();
	    	}catch(Exception ex){
	    	}
	    	if(obj==null) return;
	    	is = obj.getClass().getResourceAsStream(property_name);
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
	    			bsController.writeLog("Load_organization from "+property_name+" OK ",iStub.log_INFO);
	    			loadedFrom+=" "+property_name;
	    		}else{
	    			readOk_Resource = false;
	    		}
	    	}
	    }catch (Exception e) {

		}
	}

	public String toXml(){
		String result="";
		if(xmlEncoding!=null && !xmlEncoding.equals(""))
			result+="<?xml version=\"1.0\" encoding=\""+xmlEncoding+"\"?>"+System.getProperty("line.separator");
		result+="<organization";
		result+=" externalloader=\""+util_format.normaliseXMLText(externalloader)+"\"";
		result+=">";
		if(nodes!=null && nodes.size()>0){
			for(int i=0;i<nodes.size();i++){
				info_nodeorganization entity = (info_nodeorganization)nodes.get(i);
				if(entity!=null) result+=entity.toXml();
			}
		}

		result+=System.getProperty("line.separator")+"</organization>";

		return result;

	}

	public boolean isReadOk() {
		return readOk_File || readOk_Folder || readOk_Resource || readOk_Db || readOk_ExtLoader;
	}

	public Vector getNodes() {
		return nodes;
	}

	public void setNodes(Vector nodes) {
		this.nodes = nodes;
	}

	public HashMap get_nodes() {
		return _nodes;
	}

	public void set_nodes(HashMap nodes) {
		_nodes = nodes;
	}

	public boolean isReadOk_Resource() {
		return readOk_Resource;
	}

	public boolean isReadOk_Folder() {
		return readOk_Folder;
	}

	public boolean isReadOk_File() {
		return readOk_File;
	}

	public String getLoadedFrom() {
		return loadedFrom;
	}

	public String getExternalloader() {
		return externalloader;
	}

	public void setExternalloader(String externalloader) {
		this.externalloader = externalloader;
	}
	public String getXmlEncoding() {
		return xmlEncoding;
	}
	public void setXmlEncoding(String xmlEncoding) {
		this.xmlEncoding = xmlEncoding;
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
	public void setReadOk_Folder(boolean readOkFolder) {
		readOk_Folder = readOkFolder;
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
