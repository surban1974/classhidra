package it.classhidra.scheduler.common;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_file;
import it.classhidra.core.tool.util.util_xml;
import it.classhidra.scheduler.scheduling.DriverScheduling;
import it.classhidra.scheduler.scheduling.db.db_batch;
import it.classhidra.scheduler.scheduling.init.batch_init;


public abstract class generic_batch implements i_batch,Serializable{

	private static final long serialVersionUID = 1L;
	protected db_batch db = null;
	protected HashMap<String,Object> input = new HashMap<String, Object>();
	protected HashMap<String,Object> output = new HashMap<String, Object>();
	protected String exit = "OK";
	protected int state = 0;
	protected static String db_property_name 				= "batchdb";
 
	
	
	public void readInput(String xml){
		if(xml==null || xml.trim().equals("")) return;
		try{
			Document documentXML = null;

			ByteArrayInputStream xmlSrcStream = new	ByteArrayInputStream(xml.getBytes());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setValidating(false);
				documentXML = dbf.newDocumentBuilder().parse(xmlSrcStream);
			Node node = null;
			try {
				int first=0;
				while (node==null && first < documentXML.getChildNodes().getLength()) {
					if (documentXML.getChildNodes().item(first).getNodeType() == Node.ELEMENT_NODE)
						node = documentXML.getChildNodes().item(first);
					first++;
				}
			}
			catch (Exception e) {
				
			}
			if (node==null) return ;
			
			if(input==null) input = new HashMap<String, Object>();
			
			for (int i=0;i<node.getChildNodes().getLength();i++) {
				Node child = node.getChildNodes().item(i);
				if (child.getNodeType() == Node.ELEMENT_NODE){
					String valueTxt =  ((Text)child.getFirstChild()).getData();
					String key = getNodeAttr(child, "name");
					input.put(key,valueTxt);
			
				}					
			}
		}catch(Exception e){
			new bsException("Scheduler: "+e.toString(), iStub.log_ERROR);
		}
	}

	private static String getNodeAttr(Node node, String attr){
		String result="";
		NamedNodeMap nnm = node.getAttributes();	 		
		if (nnm!=null){
			for (int i=0;i<node.getAttributes().getLength();i++){
				String paramName = node.getAttributes().item(i).getNodeName();
				Node node_nnm =	nnm.getNamedItem(paramName);
				if (node_nnm!=null){
					if(paramName.equals(attr)) result = node_nnm.getNodeValue();
				}
			}
		}		
		return result;
	}	
	
	
	public abstract String execute() throws Exception;	
	
	public String writeOutput(){
		return map2xml(output);
	}
	
	public static String map2xml(Map<String,Object> map){
		if(map==null) return "";
		String preffix="<area>"+System.getProperty("line.separator");
		String suffix="</area>";
		String content="";
		for(String key:map.keySet())
			content+="\n<item name=\""+key+"\">"+util_xml.normalXML((String)map.get(key),null)+"</item>"+System.getProperty("line.separator");
		
		return preffix+content+suffix;
	}
	
	public abstract String getId();
	
	public static Properties getDbInitProperty(){
		Properties property = null;
		try{
			
			ResourceBundle rb = ResourceBundle.getBundle(db_property_name);

			Enumeration<String> en = rb.getKeys();
			while(en.hasMoreElements()){
				if(property==null) property = new Properties();
				String key = (String)en.nextElement();
				if(rb.getString(key)==null || rb.getString(key).equals("")){}
				else property.setProperty(key,rb.getString(key));
			}
		}catch(Exception e){	
			try{
				if(db_property_name.indexOf(".properties")==-1) db_property_name+=".properties";		
				property = util_file.loadExternalProperty(db_property_name);					
			}catch (Exception ex) {
				new bsException("Scheduler: "+e.toString(), iStub.log_ERROR);
			}
			
		}
		return property;
	}
	
	public static Properties loadProperty(db_batch _batch){
		batch_init b_init = null;
	    try{
	    	b_init = DriverScheduling.getConfiguration();
	    }catch(Exception e){
	    	b_init = new batch_init();
	    }

		Properties property = new Properties();
		

		HashMap<String,db_batch> form = new HashMap<String, db_batch>();
		form.put("selected",_batch);

		try{
			property = (Properties)b_init.get4BatchManager().operation(i_4Batch.o_LOAD_BATCH_PROPERTIES, form);
			if(property==null)
				 property = new Properties();
		}catch(Exception e){
			new bsException(e,iStub.log_ERROR);
		}		
		
		return property;
	}

	
	public static Properties getProperty(String property_name){
		Properties property = null;
		try{
			
			ResourceBundle rb = ResourceBundle.getBundle(property_name);

			Enumeration<String> en = rb.getKeys();
			while(en.hasMoreElements()){
				if(property==null) property = new Properties();
				String key = (String)en.nextElement();
				if(rb.getString(key)==null || rb.getString(key).equals("")){}
				else property.setProperty(key,rb.getString(key));
			}
		}catch(Exception e){	
			try{
				if(property_name.indexOf(".properties")==-1) property_name+=".properties";		
				property = util_file.loadExternalProperty(property_name);					
			}catch (Exception ex) {
				new bsException("Scheduler: "+e.toString(), iStub.log_ERROR);
			}
			
		}
		return property;
	}

	public String getExit() {
		return exit;
	}

	public int getState() {
		return state;
	}

	public HashMap<String,Object> getInput() {
		return input;
	}

	public HashMap<String,Object> getOutput() {
		return output;
	}
	
	public String getVersion(){
		return "0";
	}
	
	public boolean isWriteLog(){
		return true;
	}

	public void setDb(db_batch db) {
		this.db = db;
	}
	
	
	public void onBeforeReadInput(String xml) {
	}

	
	public void onAfterReadInput(String xml) {
	}

	
	public String onBeforeExecute() {
		return null;
	}

	
	public String onAfterExecute() {
		return null;
	}

	
	public String onErrorExecute() {
		return null;
	}

	
	public String onBeforeWriteOutput() {
		return null;
	}

	
	public String onAfterWriteOutput() {
		return null;
	}

	
	public int getOrder() {
		return 0;
	}

	
	public String getDescription() {
		return null;
	}	
}
