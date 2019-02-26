package it.classhidra.core.controller.transformations;

import it.classhidra.core.controller.i_transformation;
import it.classhidra.core.controller.transformation;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import it.classhidra.annotation.elements.Transformation;

@Transformation(name="resource2xml")
public class xml extends transformation implements i_transformation, Serializable {

	private static final long serialVersionUID = 1L;

	public xml(){
		super();
	}
	
	public byte[] transform(byte[] input, HttpServletRequest request){
		byte[] result = input;
		
		return result;
	}
	

}
