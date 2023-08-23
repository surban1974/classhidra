package it.classhidra.core.tool.tlinked;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_stream;

public interface I_TLinkedProvider extends Serializable{
	i_action link(i_action instance, HttpServletRequest request, HttpServletResponse response);
	i_action unlink(i_action instance, HttpServletRequest request, HttpServletResponse response);
	
	i_stream link(i_stream instance, HttpServletRequest request, HttpServletResponse response);
	i_stream unlink(i_stream instance, HttpServletRequest request, HttpServletResponse response);
}
