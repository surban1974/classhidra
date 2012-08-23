
package it.classhidra.core.controller.wrappers;

import java.io.CharArrayWriter;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class bsCharResponseWrapper  extends a_ResponseWrapper implements i_ResponseWrapper{
	private CharArrayWriter output;


    public String toString() {
        return output.toString();
    }
      
    public bsCharResponseWrapper(HttpServletResponse response) {
        super(response);
        output = new CharArrayWriter();
    }

    public PrintWriter getWriter() {
        return new PrintWriter(output);
    }
    
    public byte[] getData(){
    	return output.toString().getBytes();
    }

}
