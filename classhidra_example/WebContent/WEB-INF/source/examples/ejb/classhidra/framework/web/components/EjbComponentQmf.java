package examples.ejb.classhidra.framework.web.components;

import java.io.Serializable;
import java.util.HashMap;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

@Stateless
@Local(i_action.class)
public class EjbComponentQmf extends action implements i_action, Serializable{
	private static final long serialVersionUID = 1L;



public EjbComponentQmf(){
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
