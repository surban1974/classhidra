package it.classhidra.qreports;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.classhidra.core.tool.elements.elementBase;




public class report extends elementBase{
	private static final long serialVersionUID = 1L;
	private filename filename;
	private String sql;
	private List<parameter> parameters;
	private List<transport> transports;
	private String lang;
	
	public report(){
		super();
		init();
	}

	
	public void init(){
		sql="";
		parameters=new ArrayList<parameter>();
		transports=new ArrayList<transport>();
		filename=new filename();
	}
	
	public void load4view(Statement st, HashMap<String, parameter> h_parameters){
		load4view(st, h_parameters, null);
	}

	public void load4view(Statement st, HashMap<String, parameter> h_parameters, String lang){
		for(parameter current:parameters)
			h_parameters.put(current.getName(), current);
		
		for(parameter current:parameters){			
			if(current.getExec_type().equals(parameter.EXEC_NORMALLY))
				current.load4view(st,h_parameters, lang);
		}
	}
	
	public void refreshParameters(Statement st, HashMap<String, parameter> h_parameters, String type){
		for(parameter current:parameters){
			if(current.getExec_type().equals(type))
				current.loadParameter(st,h_parameters);
		}
	}	
	public parameter findParameter(String name){
		for(parameter current:parameters){
			if(current.getName().equals(name))
				return current;
		}
		return null;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List<parameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<parameter> parameters) {
		this.parameters = parameters;
	}
	public String getTagname(){
		return "report";
	}

	public filename getFilename() {
		return filename;
	}

	public void setFilename(filename fileName) {
		this.filename = fileName;
	}

	public List<transport> getTransports() {
		return transports;
	}

	public void setTransports(List<transport> transports) {
		this.transports = transports;
	}

	public String getLang() {
		return lang;
	}
}
