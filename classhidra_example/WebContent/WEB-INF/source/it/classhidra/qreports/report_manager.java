package it.classhidra.qreports;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


import it.classhidra.core.tool.db.db_connection;
import it.classhidra.core.tool.util.util_beanMessageFactory;
import it.classhidra.core.tool.util.util_cloner;
import it.classhidra.core.tool.util.util_xml_xslt;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.exception.bsException;




public class report_manager implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public final static String XSLT_XMLDOC2XML = "xmldoc2xml.xslt";
	public final static String XSLT_XML2XMLDOC = "xml2xmldoc.xslt";
	
	private HashMap<String,parameter> h_parameters=null;
	

	
	private String xmldoc;
	private byte[] xmldoc_byte;
	private String xmlreport;
	private report _report;
	
	public report_manager(String _xmldoc){
		super();
		this.xmldoc=_xmldoc;
		this.xmldoc_byte = this.xmldoc.getBytes();
		xmldoc2xml();		
	}
	
	public report_manager(byte[] _xmldoc_byte){
		super();
		this.xmldoc_byte=_xmldoc_byte;
		this.xmldoc = new String(_xmldoc_byte);
		xmldoc2xml();		
	}
	
	private void xmldoc2xml(){
		byte[] xslt=null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		BufferedReader br = null;
		try { 
		    	Object obj = null;
		    	try{
		    		obj = Class.forName("it.classhidra.qreports.xslt.Loader").newInstance();
		    	}catch(Exception ex){    		
		    	}
		    	if(obj==null) return;
		    	is = obj.getClass().getResourceAsStream(XSLT_XMLDOC2XML);
		    	if(is!=null){
		    		baos = new ByteArrayOutputStream();
		    		int nRead;
		    		byte[] data = new byte[16384];

		    		while ((nRead = is.read(data, 0, data.length)) != -1) {
		    			baos.write(data, 0, nRead);
		    		}

		    		baos.flush();
		    		xslt=baos.toByteArray();
		    		
		    		
		    	}	
		 }catch (Exception e) {
			 new bsException(e, iStub.log_ERROR);
		 }finally {
		    	try {
		    		if (br != null) br.close();
		    		if (is != null) is.close();
		    	}catch (Exception e) {
		    	}
		}	
		try{ 
			if(this.xmldoc_byte!=null)
				xmlreport = util_xml_xslt.xml_xslt_transform2String(xmldoc_byte, xslt); 
			else
				xmlreport = util_xml_xslt.xml_xslt_transform2String(xmldoc.getBytes(), xslt); 
			_report = (report)util_beanMessageFactory.message2bean(xmlreport);
			if(h_parameters==null) h_parameters=new HashMap<String, parameter>();
			refreshParameters(h_parameters, parameter.EXEC_IMMEDIATELY);
		}catch(Exception e){			
			new bsException(e, iStub.log_ERROR);
		}	
	}
	
	public void load4view(HashMap<String, parameter> h_parameters) throws Exception{
		load4view(h_parameters, null);
	}
	
	public void load4view(HashMap<String, parameter> h_parameters, String lang) throws Exception{
		if(_report==null || _report.getParameters()==null || _report.getParameters().size()==0) return;
		Connection con = null;
		Statement st = null;
		try{
			con = new db_connection().getContent();
            st = con.createStatement();
			_report.load4view(st, h_parameters, lang);
		} catch (Exception e) {
	        throw e;
	    } finally {
	    	db_connection.release(st, con);
	    	this.h_parameters=h_parameters;
	    }

	}
	
	public void refreshParameters(HashMap<String, parameter> h_parameters, String type) throws Exception{
		if(_report==null || _report.getParameters()==null || _report.getParameters().size()==0) 
			return;
		Connection con = null;
		Statement st = null;
		try{
			con = new db_connection().getContent();
            st = con.createStatement();
			_report.refreshParameters(st, h_parameters,type);
		} catch (Exception e) {
	        throw e;
	    } finally {
	    	db_connection.release(st, con);
	    	this.h_parameters=h_parameters;
	    }

	}


	public String getXmlreport() {
		String xml="";
		try{
			xml= util_beanMessageFactory.bean2message(_report);
		}catch(Exception e){
			new bsException(e, iStub.log_ERROR);
		}
		return xml;
	}
	
	public String getXmldoc_original() {
		return xmldoc;
	}
	
	public byte[] getXmldoc_byte_original() {
		return xmldoc_byte;
	}

	public report getReport() {
		return _report;
	}

	public String getXmldoc(boolean includeSystem) {
		String xml="";
		byte[] xslt=null;
		InputStream is = null;
		BufferedReader br = null;
		try { 
		    	Object obj = null;
		    	try{
		    		obj = Class.forName("it.classhidra.qreports.xslt.Loader").newInstance();
		    	}catch(Exception ex){    		
		    	}
		    	if(obj==null) return xml;
		    	is = obj.getClass().getResourceAsStream(XSLT_XML2XMLDOC);
		    	if(is!=null){
		    		
		    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    		int nRead;
		    		byte[] data = new byte[16384];

		    		while ((nRead = is.read(data, 0, data.length)) != -1) {
		    			baos.write(data, 0, nRead);
		    		}

		    		baos.flush();
		    		xslt=baos.toByteArray();
		    	}	
		 }catch (Exception e) {
			 new bsException(e, iStub.log_ERROR);
		 }finally {
		    	try {
		    		if (br != null) br.close();
		    		if (is != null) is.close();
		    	}catch (Exception e) {
		    	}
		}	
		
		try{
			if(includeSystem){
				report clone = (report)util_cloner.clone(_report);
				clone.getParameters().addAll(new ArrayList<parameter>(h_parameters.values()));
				String xmlRep=util_beanMessageFactory.bean2message(clone);
				xml = util_xml_xslt.xml_xslt_transform2String(xmlRep.getBytes(), xslt); 
			}else{
				String xmlRep=util_beanMessageFactory.bean2message(_report);
				xml = util_xml_xslt.xml_xslt_transform2String(xmlRep.getBytes(), xslt); 
			}

		}catch(Exception e){
			new bsException(e, iStub.log_ERROR);
		}
		return xml;
	}

	public HashMap<String, parameter> getH_parameters() {
		return h_parameters;
	}	
}
