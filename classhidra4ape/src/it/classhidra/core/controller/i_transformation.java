package it.classhidra.core.controller;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface i_transformation extends Serializable{
	
	public static String CLASSHIDRA_TRANSFORMATION_PACKAGE = "it.classhidra.core.controller.transformations.";

	public void setResponseHeader(HttpServletRequest request, HttpServletResponse response);	
	public byte[] transform(String input,HttpServletRequest request);		
	public byte[] transform(byte[] input,HttpServletRequest request);	
	public byte[] transform(i_bean form,HttpServletRequest request);	
	public byte[] transform(String input,HttpServletRequest request,HttpServletResponse response);		
	public info_transformation get_infotransformation();
	public void set_infotransformation(info_transformation infotransformation);	
	public byte[] getOutputcontent();
	public void setOutputcontent(byte[] outputcontent);
}