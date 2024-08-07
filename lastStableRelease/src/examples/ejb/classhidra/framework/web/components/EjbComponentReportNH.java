package examples.ejb.classhidra.framework.web.components;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;


@Action (
	path="report_neohort",
	entity=@Entity(
			property="always:public"
	),

	redirects={
			@Redirect(
				auth_id="rep_id",
				path="*"
			)
	}
)

@Stateless
@Local(i_action.class)
public class EjbComponentReportNH extends action implements i_action, Serializable{
	private static final long serialVersionUID = 6534122783978835682L;

	private String source;
	private String lib;

	@Resource
	SessionContext sessionContext;		
	
public EjbComponentReportNH(){
	super();

}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
    if(source==null) return null;
    return new redirects(source+"?ReportProvider=neoHort&$lib="+lib);
}

public void reimposta() {
	super.reimposta();
	source="";
	lib="pdf";
}

public String getSource() {
	return source;
}

public void setSource(String source) {
	this.source = source;
}

public String getLib() {
	return lib;
}

public void setLib(String lib) {
	this.lib = lib;
}

}
