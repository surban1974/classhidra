package it.classhidra.scheduler.common;

import it.classhidra.core.tool.db.db_connection;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_file;
import it.classhidra.core.tool.util.util_format;
import it.classhidra.scheduler.scheduling.db.db_batch;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.servlets.servletBatchScheduling;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;


public abstract class generic_batch implements i_batch,Serializable{

	private static final long serialVersionUID = 1L;
	protected db_batch db = null;
	protected HashMap input = new HashMap();
	protected HashMap output = new HashMap();
	protected String exit = "OK";
	protected int state = 0;
	protected static String db_property_name = "batchdb";
	
	public void readInput(String xml){
		if(xml==null || xml.trim().equals("")) return;
		try{
			Document documentXML = null;
			if (xml==null) return ;
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
			
			if(input==null) input = new HashMap();
			
			for (int i=0;i<node.getChildNodes().getLength();i++) {
				if (node.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE){
					String valueTxt =  ((Text)node.getFirstChild()).getData();
					String key = getNodeAttr(node, "name");
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
	
	public static String map2xml(HashMap map){
		if(map==null) return "";
		String preffix="<area>"+System.getProperty("line.separator");
		String suffix="</area>";
		String content="";
		if(map==null) return preffix+content+suffix;
		Vector keys = new Vector(map.keySet());
		for(int i=0;i<keys.size();i++){
			String key = (String)keys.get(i);
			content+="\t<item name=\""+key+"\">"+map.get(key)+"</item>"+System.getProperty("line.separator");
		}
		return preffix+content+suffix;
	}
	
	public abstract String getId();
	
	public static Properties getDbInitProperty(){
		Properties property = null;
		try{
			
			ResourceBundle rb = ResourceBundle.getBundle(db_property_name);

			Enumeration en = rb.getKeys();
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
	    	b_init = servletBatchScheduling.getConfiguration();
	    }catch(Exception e){
	    	b_init = new batch_init();
	    }

		Properties property = new Properties();
		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		String sql="SELECT * FROM "+b_init.get_db_prefix()+"batch_property \n";
			sql+="where cd_btch='"+util_format.convertAp(_batch.getCd_btch())+"' and cd_ist="+_batch.getCd_ist()+" \n";
		
		try{
			conn = new db_connection().getContent();
			st = conn.createStatement();

			rs = st.executeQuery(sql);
			
			while(rs.next()){
				String key = rs.getString("cd_property");
				String value = rs.getString("value");
				if(key!=null && value!=null){
					property.put(key, value);
				}
			}
		}catch(Exception ex){

		}finally{
			db_connection.release(rs, st, conn);
		}
		return property;
	}

	
	public static Properties getProperty(String property_name){
		Properties property = null;
		try{
			
			ResourceBundle rb = ResourceBundle.getBundle(property_name);

			Enumeration en = rb.getKeys();
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

	public HashMap getInput() {
		return input;
	}

	public HashMap getOutput() {
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
}