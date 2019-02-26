package it.classhidra.qreports;

import it.classhidra.core.tool.elements.elementBase;

public class property extends elementBase{
	private static final long serialVersionUID = 1L;

	
	private String name;
	
	public property(){
		super();
		init();
	}
	
	public void init(){
		name="";
	}	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
