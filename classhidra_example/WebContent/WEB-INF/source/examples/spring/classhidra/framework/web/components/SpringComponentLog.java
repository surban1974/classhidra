package examples.spring.classhidra.framework.web.components;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.NavigatedDirective;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.log.log_FilesFilter;
import it.classhidra.core.tool.util.util_sort;



@Action (
	path="log",
	redirect="/jsp/framework/log.jsp",
	redirects={
			@Redirect(
				auth_id="log_id",
				path="*"
			)
	}
)

@NavigatedDirective(memoryContent="true")
@Component
@Scope("prototype")
public class SpringComponentLog extends action implements i_action, Serializable{
	private static final long serialVersionUID = 6534122783978835682L;
	
	@Autowired
	private ApplicationContext applicationContext;

		public class log_element{
			private String Path;
			private String Mod;

			log_element(String path, String mod){
				this.Path = path;
				this.Mod = mod;
			}
			public String getPath() {
				return Path;
			}
			public String getMod() {
				return Mod;
			}
			public void setPath(String string) {
				Path = string;
			}
			public void setSize(String string) {
				Mod = string;
			}
			public String toString(){
				return Path+"|"+Mod;
			}
		}

public SpringComponentLog(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	String path=request.getParameter("path");

	if(path!=null && !path.equals("")){
		String file="";
		try{
			File f = new File(path);
			BufferedInputStream is = new BufferedInputStream(new FileInputStream(f));

			int formDataLength = (int)f.length();
			byte dataBytes[] = new byte[formDataLength];
			int bytesRead = 0;
			int totalBytesRead = 0;
			while (totalBytesRead < formDataLength) {
				bytesRead = is.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += bytesRead;
			}
			file = new String(dataBytes,0,dataBytes.length);
			request.setAttribute("file",file);
			request.setAttribute("type","open");
		}catch(Exception e){
		}

		return new redirects(get_infoaction().getRedirect());
	}
	Vector ListLog = new Vector();

	File f=new File( bsController.getLogInit().get_LogPath());
	log_FilesFilter filter = new log_FilesFilter();
	File[] list = f.listFiles(filter);
	if(list!=null){
		for(int i=0;i<list.length;i++)
			ListLog.add(new log_element(list[i].getAbsolutePath().replace('\\','/'),String.valueOf(list[i].lastModified())));
	}
	ListLog = new util_sort().sort(ListLog,"Mod","D");
	request.setAttribute("ListLog",ListLog);
	request.setAttribute("type","view");
	return new redirects(get_infoaction().getRedirect());
    }

}
