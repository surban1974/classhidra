
package it.classhidra.core.controller.wrappers;

import java.io.IOException;
import java.io.PrintWriter;


public interface i_ResponseWrapper {
	public abstract PrintWriter getWriter() throws IOException;
	public abstract String toString();
	public abstract byte[] getData();
}