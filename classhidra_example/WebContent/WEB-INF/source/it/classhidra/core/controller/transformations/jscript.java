package it.classhidra.core.controller.transformations;



import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.i_transformation;
import it.classhidra.core.controller.transformation;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import it.classhidra.annotation.elements.Transformation;

@Transformation(name="resource2jscript")
public class jscript extends transformation implements i_transformation, Serializable {

	private static final long serialVersionUID = 1L;

	public jscript(){
		super();
	}
	
	public byte[] transform(i_bean form, HttpServletRequest request){
		byte[] result = null;
		
		return result;
	}
	

}
