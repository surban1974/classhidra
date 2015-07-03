package application.web.streams;


import it.classhidra.annotation.elements.Apply_to_action;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.Stream;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.controller.i_stream;
import it.classhidra.core.controller.stream;
import it.classhidra.core.tool.exception.bsControllerException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Stream(	name="test_stream_annotated",
			applied={
				@Apply_to_action(action="login")
			},
			entity=@Entity(order="10")
)
public class test_stream_annotated extends stream implements i_stream{

	private static final long serialVersionUID = 1L;


	public redirects streamservice_enter(HttpServletRequest request,HttpServletResponse response) throws bsControllerException {
		return super.streamservice_enter(request, response);

	}

	public redirects streamservice_exit(HttpServletRequest request, HttpServletResponse response) throws bsControllerException {
		return super.streamservice_exit(request, response);
	}



}
