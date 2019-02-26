
package it.classhidra.core.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import it.classhidra.core.init.app_init;
import it.classhidra.core.tool.elements.elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_blob;
import it.classhidra.core.tool.util.util_cloner;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.core.tool.util.util_provider;
import it.classhidra.core.tool.util.util_xml;


public class load_menu  extends elementBase{
	private static final long serialVersionUID = 1L;
	private i_menu_element _menu;
	private boolean readOk_Resource=false;
	private boolean readOk_Folder=false;
	private boolean readOk_File=false;
	private boolean readOk_Db=false;
	private boolean readOk_ExtLoader=false;

	private String externalloader;
	private String xmlEncoding;

	private String loadedFrom="";

 

	public load_menu(i_menu_element mn) {
		super();
		reimposta(mn);
	}

	public load_menu reInit(i_menu_element mn) {
		reimposta(mn);
		return this;
	}
	
	public info_menu_element find(String _id_node){
		if(_menu==null) return null;
		return _menu.getInfo_menu().find(_id_node);
	}

	public boolean remove(String _id_node){

		info_menu_element founded = find(_id_node);
		if(founded!=null && founded.getParent()!=null){
			return founded.getParent().remove(_id_node);
		}
		return false;
	}
	
	public boolean move(String _id_node, int position){

		info_menu_element founded = find(_id_node);
		if(founded!=null && founded.getParent()!=null){
			return founded.getParent().move(_id_node,position);
		}
		return false;
	}

	public boolean add(info_menu_element parent, info_menu_element newNode){



		if(newNode==null) return false;
		if(parent==null){			
			return false;
		}else{
			return parent.add(newNode);
		}

	}
	
	
	public void reimposta(){
//		_menu = new i_menu_element();
		readOk_Resource=false;
		readOk_Folder=false;
		readOk_File=false;
		readOk_Db=false;
		readOk_ExtLoader=false;
		externalloader="";
		xmlEncoding="";
	}
	public void reimposta(i_menu_element mn){
		_menu = mn;
		if(_menu!=null && _menu.getInfo_menu()==null) _menu.setInfo_menu(new info_menu_element());
		readOk_Resource=false;
		readOk_Folder=false;
		readOk_File=false;
		readOk_Db=false;
		readOk_ExtLoader=false;
		externalloader="";
		xmlEncoding="";

	}

	public void reInit(i_externalloader _externalloader){
		if(_externalloader==null) return;
		boolean loaded = false;
		if(	_externalloader.getProperty(i_externalloader.MENU_menu)!=null &&
			_externalloader.getProperty(i_externalloader.MENU_menu) instanceof i_menu_element){
			_menu=((i_menu_element)_externalloader.getProperty(i_externalloader.MENU_menu));
			loaded=true;
		}
		if(loaded)
			loadedFrom+=" "+_externalloader.getClass().getName();
//		readOk_ExtLoader=true;

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
				bsController.writeLog("Load_menu from "+ainit.get_external_loader()+" ERROR "+e.toString(),iStub.log_WARN);
			}catch(Throwable t){
				bsController.writeLog("Load_menu from "+ainit.get_external_loader()+" ERROR "+t.toString(),iStub.log_ERROR);
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
				bsController.writeLog("Load_menu from "+this.getExternalloader()+" ERROR "+e.toString(),iStub.log_WARN);
			}catch(Throwable t){
				bsController.writeLog("Load_menu from "+this.getExternalloader()+" ERROR "+t.toString(),iStub.log_ERROR);
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
				(ainit.getSynonyms_path().getProperty(bsController.menu_id_xml)==null)?bsController.menu_id_xml:ainit.getSynonyms_path().getProperty(bsController.menu_id_xml)
			);
		if(xml_name==null || xml_name.equals(""))
			xml_name = System.getProperty(
					(ainit.getSynonyms_path().getProperty(app_path+bsController.menu_id_xml)==null)?app_path+bsController.menu_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.menu_id_xml)
				);

		if(xml_name==null || xml_name.equals(""))
			xml_name = ainit.getResources_path().getProperty(
					(ainit.getSynonyms_path().getProperty(bsController.menu_id_xml)==null)?bsController.menu_id_xml:ainit.getSynonyms_path().getProperty(bsController.menu_id_xml)
				);

		if(xml_name==null || xml_name.equals(""))
			xml_name = ainit.getResources_path().getProperty(
					(ainit.getSynonyms_path().getProperty(app_path+bsController.menu_id_xml)==null)?app_path+bsController.menu_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.menu_id_xml)
				);

		if(xml_name!=null && !xml_name.equals("")){
			initProperties(xml_name);
		}


	}

	public void initProperties(String xml) throws bsControllerException{
		try{
			if(initWithFile(xml)){
				readOk_File = true;
				loadedFrom+=" "+ xml;
			}else readOk_File = false;
		}catch(Exception e){
			readOk_File=false;
		}
	}

	public void initData(String xml) throws bsControllerException, Exception{
		initWithData(xml);
	}

	private boolean initWithData(String _xml) throws bsControllerException, Exception{
		if(_menu==null){
			readOk_Resource=false;
			readOk_Folder=false;
			readOk_File=false;
			readOk_Db=false;
			readOk_ExtLoader=false;
			return false;
		}
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
	if(_menu==null){
		readOk_Resource=false;
		readOk_Folder=false;
		readOk_File=false;
		readOk_Db=false;
		readOk_ExtLoader=false;
		return false;
	}
	Document documentXML = null;
	documentXML = util_xml.readXML(_xml);
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
		if(node.getNodeName().equals("menu")){
//			this.initTop(node);

		}

		_menu.init(node);

		_menu.setVisible(true);
		_menu.calculate_potential_elements();
		_menu.analyse_potential_group(true);
	}else return false;
	return true;
}


public boolean initDB(app_init ainit) throws bsControllerException, Exception{
	String app_path=ainit.get_path();
	if(app_path==null || app_path.equals("")) app_path="";
	else app_path+=".";
	
	String xmlData = null;
	boolean dbValid = false;
	
	try{
		xmlData = util_blob.load_from_config(
				(ainit.getSynonyms_path().getProperty(app_path+bsController.menu_id_xml)==null)?app_path+bsController.menu_id_xml:ainit.getSynonyms_path().getProperty(app_path+bsController.menu_id_xml),
				ainit.get_db_name());
		dbValid=true;
	}catch(Exception e){

	}
	try{		
		if(xmlData==null) xmlData = util_blob.load_from_config(
				(ainit.getSynonyms_path().getProperty(bsController.menu_id_xml)==null)?bsController.menu_id_xml:ainit.getSynonyms_path().getProperty(bsController.menu_id_xml),
				ainit.get_db_name());
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

	public i_menu_element get_menu() {
		try{
			return (i_menu_element)util_cloner.clone(_menu);
		}catch(Exception e){
		}
		return null;
	}

	public boolean isReadOk() {
		return readOk_File || readOk_Folder || readOk_Resource || readOk_Db || readOk_ExtLoader;
	}


	public void setReadError(boolean b) {
		readOk_File = b;
	}

	public void load_from_resources() {
		
		if(_menu==null) return;
		
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
			}catch(Throwable t){
			}
		}

		app_init ainit = bsController.getAppInit();

		try{

			if(ainit.get_db_name()!=null){
				if(initDB(ainit)){
					loadedFrom=ainit.get_db_name();
					return;
				}
			}
		}catch (Exception e) {
		}

		load_from_resources("/config/"+bsController.CONST_XML_MENU);
		load_from_resources("/config/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_MENU);
		
		load_from_resources("META-INF/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_MENU);	
		load_from_resources("WEB-INF/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_MENU);

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
	    			bsController.writeLog("Load_menu from "+property_name+" OK ",iStub.log_INFO);
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

		boolean read = load_from_resources(ctx,"/WEB-INF/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_MENU);
		if(!read) 
			read = load_from_resources(ctx,"/WEB-INF/config/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_MENU);
		if(!read) 
			read = load_from_resources(ctx,"/META-INF/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_MENU);
		if(!read) 
			read = load_from_resources(ctx,"/META-INF/config/"+bsController.CONST_XML_PREFIX+bsController.CONST_XML_MENU);

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
	    	bsController.writeLog("Load_menu from "+property_name+" ERROR "+e.toString(),iStub.log_WARN);
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
		    		bsController.writeLog("Load_menu from "+property_name+" OK ",iStub.log_INFO);
		    		readOk_Resource = readOk_Resource || true;
		    		loadedFrom+=" "+property_name;
		    		return true;
		    	}
			}catch(Exception e){
				bsController.writeLog("Load_menu from "+property_name+" ERROR "+e.toString(),iStub.log_WARN);
			}
	    }
	    
	    return false;

	}
	
	

	public String toXml(){
		String result="";
		if(xmlEncoding!=null && !xmlEncoding.equals(""))
			result+="<?xml version=\"1.0\" encoding=\""+xmlEncoding+"\"?>"+System.getProperty("line.separator");
		if(_menu!=null){
			result+="<menu ";
			result+=" externalloader=\""+util_format.normaliseXMLText(externalloader)+"\"";
			result+=" id=\""+util_format.normaliseXMLText(_menu.getInfo_menu().getId())+"\"";
			result+=" action=\""+util_format.normaliseXMLText(_menu.getInfo_menu().getAction())+"\"";
			result+=" descr=\""+util_format.normaliseXMLText(_menu.getInfo_menu().getDescr())+"\"";
			result+=" mess_id=\""+util_format.normaliseXMLText(_menu.getInfo_menu().getMess_id())+"\"";
			result+=" img=\""+util_format.normaliseXMLText(_menu.getInfo_menu().getImg())+"\"";
			result+=" type=\""+util_format.normaliseXMLText(_menu.getInfo_menu().getType())+"\"";
			result+=" load=\""+util_format.normaliseXMLText(_menu.getInfo_menu().getLoad())+"\"";
			
			result+=">";
			if(_menu.getInfo_menu().getChildren()!=null && _menu.getInfo_menu().getChildren().size()>0){
				for(int i=0;i<_menu.getInfo_menu().getChildren().size();i++){
					info_menu_element entity = (info_menu_element)_menu.getInfo_menu().getChildren().get(i);
					if(entity!=null) result+=entity.toXml();
				}
			}
	
			result+=System.getProperty("line.separator")+"</menu>";
		}

		return result;

	}

	
	public String getExternalloader() {
		return externalloader;
	}

	public void setExternalloader(String externalloader) {
		this.externalloader = externalloader;
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

	public boolean isReadOk_Resource() {
		return readOk_Resource;
	}

	public boolean isReadOk_Folder() {
		return readOk_Folder;
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
