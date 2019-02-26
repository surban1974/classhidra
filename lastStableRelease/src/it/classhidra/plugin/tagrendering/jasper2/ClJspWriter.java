package it.classhidra.plugin.tagrendering.jasper2;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.jsp.JspWriter;

public class ClJspWriter extends JspWriter {
	private PrintWriter out;
	private StringWriter sout;
	
	public ClJspWriter() {
		super(4096,true);
		sout = new StringWriter();
		out = new PrintWriter(sout);
	}

	
	public void clear() throws IOException {
		sout.getBuffer().delete(0, sout.getBuffer().length());
	}

	
	public void clearBuffer() throws IOException {
		sout.getBuffer().delete(0, sout.getBuffer().length());
	}

	
	public void close() throws IOException {
		try {
			sout.close();
			out.close();
		}catch(Exception e) {			
		}
	}

	
	public void flush() throws IOException {
		sout.flush();
		out.flush();		
	}

	
	public int getRemaining() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public void newLine() throws IOException {
		out.println("");
	}

	
	public void print(boolean b) throws IOException {
		out.print(b);		
	}

	
	public void print(char c) throws IOException {
		out.print(c);			
	}

	
	public void print(int i) throws IOException {
		out.print(i);	
	}

	
	public void print(long l) throws IOException {
		out.print(l);	
	}

	
	public void print(float f) throws IOException {
		out.print(f);	
	}

	
	public void print(double d) throws IOException {
		out.print(d);	
	}

	
	public void print(char[] s) throws IOException {
		out.print(s);	
	}

	
	public void print(String s) throws IOException {
		out.print(s);	
	}

	
	public void print(Object obj) throws IOException {
		out.print(obj);	
	}

	
	public void println() throws IOException {
		out.println("");	
	}

	
	public void println(boolean x) throws IOException {
		out.println(x);	
	}

	
	public void println(char x) throws IOException {
		out.println(x);	
	}

	
	public void println(int x) throws IOException {
		out.println(x);	
	}

	
	public void println(long x) throws IOException {
		out.println(x);	
	}

	
	public void println(float x) throws IOException {
		out.println(x);	
	}

	
	public void println(double x) throws IOException {
		out.println(x);	
	}

	
	public void println(char[] x) throws IOException {
		out.println(x);	
	}

	
	public void println(String x) throws IOException {
		out.println(x);	
	}

	
	public void println(Object x) throws IOException {
		out.println(x);	
	}

	
	public void write(char[] cbuf, int off, int len) throws IOException {
		out.write(cbuf, off, len);			
	}
	
	public String getAsString() {		
		if(sout!=null) {			
			sout.flush();
			return sout.toString();
		}else
			return null;
	}
	
}
