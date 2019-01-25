package it.classhidra.core.controller;

import java.io.Serializable;

import javax.servlet.ServletContext;

public interface i_provider extends Serializable{
	public void set_context(ServletContext context);
	public Object get_bean(String id_bean);
}
