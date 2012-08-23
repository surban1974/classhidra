
package it.classhidra.core.controller.wrappers;

import java.io.PrintWriter;


public interface i_ResponseWrapper {
	public abstract PrintWriter getWriter();
	public abstract String toString();
	public abstract byte[] getData();
}