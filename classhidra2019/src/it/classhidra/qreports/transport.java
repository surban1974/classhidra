package it.classhidra.qreports;

import java.util.ArrayList;
import java.util.List;

import it.classhidra.core.tool.elements.elementBase;

public class transport extends elementBase{
	private static final long serialVersionUID = 1L;

	
	private String name;
	private String worker;
	private String type;
	private List<property> properties;
	
	public transport(){
		super();
		init();
	}
	
	public void init(){
		name="";
		worker="";
		type="";
		properties=new ArrayList<property>();
	}	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getWorker() {
		return worker;
	}

	public void setWorker(String worker) {
		this.worker = worker;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<property> getProperties() {
		return properties;
	}

	public void setProperties(List<property> properties) {
		this.properties = properties;
	}
}
