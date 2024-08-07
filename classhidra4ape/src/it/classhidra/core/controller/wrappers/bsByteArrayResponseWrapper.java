
package it.classhidra.core.controller.wrappers;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class bsByteArrayResponseWrapper extends a_ResponseWrapper implements i_ResponseWrapper {
	  
    private ByteArrayOutputStream output;

    public bsByteArrayResponseWrapper(HttpServletResponse response) {
      super(response);
      output=new ByteArrayOutputStream();
    }

    public ServletOutputStream getOutputStream() {
      return new FilterServletOutputStream(output);
    }
  
    public PrintWriter getWriter() throws IOException{
      return new PrintWriter(getOutputStream(),true);
    }

    public String toString() {
      return output.toString();
    }
    
	public byte[] getData() { 
	  return output.toByteArray(); 
	} 

    
    
    public class FilterServletOutputStream extends ServletOutputStream { 
    	  
        public ByteArrayOutputStream  output= null; 
        public DataOutputStream stream; 
          
          public FilterServletOutputStream() { 
        	  output = new ByteArrayOutputStream(); 
        	  stream = new DataOutputStream(output); 
          } 

          public FilterServletOutputStream(ByteArrayOutputStream Pout) { 
        	  output = Pout; 
        	  stream = new DataOutputStream(output); 
          } 

         
          public void write(int b) throws IOException  { 
        	  stream.write(b); 
          } 
          
         
         public void write(byte b[]) throws IOException { 
        	 stream.write(b); 
         } 
          
         
          public void write( byte buf[], int offset, int len) throws IOException { 
        	  stream.write(buf, offset, len); 
          }   
          
         
          public void flush() throws IOException  { 
        	  stream.flush(); 
          } 
          
         
          public void close() throws IOException  { 
        	  stream.close(); 
          } 
          
          
          public ByteArrayOutputStream getBuffer()   { 
        	  return output; 
          } 
          
          
        }     
  }