package it.classhidra.plugin.was.jboss.loader;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.tool.elements.elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.message;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_format;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class messages_loader  extends elementBase{
	private static final long serialVersionUID = 1L;
	private HashMap _messages;

	
	public void load_Messages() {
		String property_name =  "config."+bsController.CONST_XML_MESSAGES_FOLDER;
		VirtualFile vFile=null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null)
				throw new ClassNotFoundException("Can't get class loader.");
			String path = null;
			if(property_name.lastIndexOf(".class")==property_name.length()-6){
				property_name=property_name.substring(0,property_name.length()-6);
				path = '/' + property_name.replace('.', '/')+".class";
			}else
				path = '/' + property_name.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null) {
				path = property_name.replace('.', '/');
				resource = cld.getResource(path);
			}
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
			
			try{
				vFile = VFS.getRoot(resource);
			}catch(Exception e){
			}


		}catch(Exception ex){				
		}
		
		if(vFile==null) return;
		


		try{
			List array = vFile.getChildren();
				for(int i=0;i<array.size();i++){
					String property_name0 =  ((VirtualFile)array.get(i)).getName();
					if(property_name0!=null && property_name0.toLowerCase().indexOf(".xml")>-1){
						load_from_resources("/config/"+bsController.CONST_XML_MESSAGES_FOLDER+"/"+property_name0);
					}
				}

		}catch(Exception e){
			util_format.writeToConsole(bsController.getLogInit(),"LoadMessages: Array.ERROR:"+e.toString());
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
	    		if(bsController.getMess_config().initWithData(result)){
	    			bsController.writeLog("Load_messages from "+property_name+" OK ",iStub.log_INFO);
	    		}
	    	}
	    }catch (Exception e) {

		}

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
	public HashMap get_messages() {
		return _messages;
	}
	public void set_messages(HashMap _messages) {
		this._messages = _messages;
	}	

}
