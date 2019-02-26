package examples.spring.classhidra.framework.web.components;

import java.io.Serializable;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;



@Action (
		path="qmf",
		name="formQmf",
		redirect="/jsp/qmf/qmftool_login.jsp",
		redirects={
				@Redirect(
					auth_id="qmf_id",
					path="*"
				)
		}
)

@Component
@Scope("prototype")
public class SpringComponentQmf extends action implements i_action, Serializable{
	private static final long serialVersionUID = 1L;

	@Autowired
	private ApplicationContext applicationContext;

public SpringComponentQmf(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	String redirect = request.getParameter("redirect");
	if(redirect==null) return new redirects(get_infoaction().getRedirect());
	else{
		if(get_bean().getParametersMP()!=null){
			HashMap cur_file = (HashMap)get_bean().getParametersMP().get("file0");
			if(cur_file!=null){
				byte[] file_content = (byte[])cur_file.get("content");
				if(file_content!=null){
					request.setAttribute("file_content", file_content);
				}
			}
		}

		return new redirects(redirect);
	}
}


}
