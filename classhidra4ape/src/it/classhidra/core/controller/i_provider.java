package it.classhidra.core.controller;

import javax.servlet.ServletContext;

public interface i_provider {
	public void set_context(ServletContext context);
	public Object get_bean(String id_bean);
}
