package it.classhidra.core;

import java.io.Serializable;

import it.classhidra.core.controller.i_action;
import it.classhidra.core.init.auth_init;

public interface i_authentication_filter extends Serializable{
	public void validate_actionPermittedForbidden(auth_init auth);
	public boolean check_actionIsPermitted(auth_init auth, String id_action);
	public boolean check_redirectIsPermitted(auth_init auth, i_action _action);
}