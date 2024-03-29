package starter.components; 




import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.ActionCall;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.Expose;
import it.classhidra.annotation.elements.Parameter;
import it.classhidra.annotation.elements.SessionDirective;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.i_bean;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.util.util_classes;


@Action (
	path="hello",
	name="model",
	redirect="/starter/pages/hello.html",
	Expose=@Expose(methods = {Expose.POST,Expose.GET}),
	entity=@Entity(
			property="allway:public"
	) 
)

@SessionDirective
//@NavigatedDirective(memoryContent="true")
public class HelloWorld extends action implements i_action, i_bean, Serializable{
	private static final long serialVersionUID = 1L;

public HelloWorld(){
	super();
}

@Override
public redirects actionservice(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, UnavailableException, bsControllerException {
	return super.actionservice(request, response);
}


@ActionCall(
		name="resource",
		Expose=@Expose(method=Expose.POST)
		)
public byte[] resource(@Parameter(name="resource") String resource) throws Exception{
	return util_classes.getResourceAsByte(resource);
}


}
