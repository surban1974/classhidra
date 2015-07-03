package it.classhidra.framework.web.components;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.ActionMapping;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.controller.*;

import java.io.Serializable;
import java.util.HashMap;

import javax.servlet.http.*;
import javax.servlet.*;

@ActionMapping (
		redirects={
				@Redirect(
						path="/jsp/qmf/qmftool_login.jsp",
						descr="DBMS",
						mess_id="title_fw_Qmf"
				)
			}
)

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

public class componentQmf extends action implements i_action, Serializable{
	private static final long serialVersionUID = 1L;



public componentQmf(){
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
