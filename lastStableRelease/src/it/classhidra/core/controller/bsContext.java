package it.classhidra.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class bsContext implements iContext {
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public bsContext(){
		super();
	}
	
	public bsContext(HttpServletRequest request, HttpServletResponse response){
		super();
		this.request = request;
		this.response = response;
	}
	
	public HttpServletRequest getRequest(){
		return request;
	}

	public HttpServletResponse getResponse(){
		return response;
	}
	
	public void setResponse(HttpServletResponse response){
		this.response = response;
	}

	public void flushBuffer() throws Exception{
		if(response!=null)
			response.flushBuffer();
	}
	
	public void flush() throws Exception{
		if(response!=null)
			response.getOutputStream().flush();
	}

	public void write(byte[] bytes) throws Exception{
		if(response!=null)
			response.getOutputStream().write(bytes);
	}
}
