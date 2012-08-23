package it.classhidra.core.controller;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class transformation implements i_transformation, Serializable {

	private static final long serialVersionUID = 1L;
	private info_transformation _infotransformation;
	private byte[] outputcontent;
	private String contentType;
	private String contentName;
	private String contentTransferEncoding;


	
	public transformation(){
		super();
	}
	
	public void setResponseHeader(HttpServletRequest request,
			HttpServletResponse response){
		
	}
	
	public byte[] transform(String input, HttpServletRequest request){
		byte[] result = null;
		return result;
	}
	
	public byte[] transform(byte[] input, HttpServletRequest request){
		byte[] result = null;
		return result;
	}

	public byte[] transform(i_bean form, HttpServletRequest request){
		byte[] result = null;
		return result;
	}	

	public byte[] transform(String input, HttpServletRequest request, HttpServletResponse response){
		byte[] result = null;
		return result;
	}		
	
	public info_transformation get_infotransformation() {
		return _infotransformation;
	}

	public void set_infotransformation(info_transformation infotransformation) {
		_infotransformation = infotransformation;
	}

	public byte[] getOutputcontent() {
		return outputcontent;
	}

	public void setOutputcontent(byte[] outputcontent) {
		this.outputcontent = outputcontent;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getContentTransferEncoding() {
		return contentTransferEncoding;
	}

	public void setContentTransferEncoding(String contentTransferEncoding) {
		this.contentTransferEncoding = contentTransferEncoding;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
