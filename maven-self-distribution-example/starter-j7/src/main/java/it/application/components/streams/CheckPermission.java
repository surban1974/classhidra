package it.application.components.streams; 


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.annotation.elements.Apply_to_action;
import it.classhidra.annotation.elements.Stream;
import it.classhidra.core.controller.i_stream;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.controller.streams.def_control_permission;
import it.classhidra.core.tool.exception.bsControllerException;

@Stream(	
		name="def_control_permission",
		applied={
			@Apply_to_action(action="*"),
			@Apply_to_action(action="pooling",excluded="true")
		}
)

public class CheckPermission extends def_control_permission implements i_stream{

	private static final long serialVersionUID = 1L;
	


	public redirects streamservice_enter(HttpServletRequest request,HttpServletResponse response) throws bsControllerException {
//		return super.streamservice_enter(request, response);
		return null;
	}

	public redirects streamservice_exit(HttpServletRequest request, HttpServletResponse response) throws bsControllerException {
//		return super.streamservice_enter(request, response);
		return null;
	}

	
	
}
