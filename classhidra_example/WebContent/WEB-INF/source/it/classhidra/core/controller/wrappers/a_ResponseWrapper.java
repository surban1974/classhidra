
package it.classhidra.core.controller.wrappers;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;


public abstract class a_ResponseWrapper  extends HttpServletResponseWrapper implements i_ResponseWrapper{
	
	public  a_ResponseWrapper(HttpServletResponse response){
		super(response);
	}

	public abstract PrintWriter getWriter();
	public abstract String toString();
	public abstract byte[] getData();

}
