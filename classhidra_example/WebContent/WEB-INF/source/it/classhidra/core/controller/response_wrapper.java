package it.classhidra.core.controller;

import java.io.Serializable;

public class response_wrapper implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int responseStatus = 0;
	private String contentType;
	private String contentName;
	private String contentEncoding;
	private Object content;
	private redirects redirect;
	
	public int getResponseStatus() {
		return responseStatus;
	}

	public response_wrapper setResponseStatus(int responseStatus) {
		this.responseStatus = responseStatus;
		return this;
	}
	public String getContentType() {
		return contentType;
	}
	public response_wrapper setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}
	public String getContentName() {
		return contentName;
	}
	public response_wrapper setContentName(String contentName) {
		this.contentName = contentName;
		return this;
	}
	public String getContentEncoding() {
		return contentEncoding;
	}
	public response_wrapper setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
		return this;
	}

	public Object getContent() {
		return content;
	}

	public response_wrapper setContent(Object content) {
		this.content = content;
		return this;
	}

	public redirects getRedirect() {
		return redirect;
	}

	public response_wrapper setRedirect(redirects redirect) {
		this.redirect = redirect;
		return this;
	}	
	
}
