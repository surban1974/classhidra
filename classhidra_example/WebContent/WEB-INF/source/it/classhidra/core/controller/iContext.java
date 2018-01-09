package it.classhidra.core.controller;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface iContext extends Serializable {
	HttpServletRequest getRequest();
	HttpServletResponse getResponse();
	void setResponse(HttpServletResponse response);
	void flushBuffer() throws Exception;
	void flush() throws Exception;
	void write(byte[] bytes) throws Exception;
}
