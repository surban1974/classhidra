package it.classhidra.core.controller;

import java.io.Serializable;

import it.classhidra.core.init.auth_init;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface i_auth_manager extends Serializable{

	public abstract boolean login_JAAS(auth_init auth, String password,HttpServletRequest request);
	public abstract boolean readTicker(auth_init auth,HttpServletRequest request);
	public abstract boolean saveTicker(auth_init auth,HttpServletResponse response);	
	public boolean saveFromForm(auth_init auth, i_bean bean,HttpServletRequest request, HttpServletResponse response);
			
}