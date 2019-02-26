

package it.classhidra.core.init;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_file;

import java.io.Serializable;
import java.util.Properties;

public class project_init implements Serializable{
	private static final long serialVersionUID = -1L;

	public final static String id_property =	"application.suiteprojects.property";

	Properties property = null;
	private String loadedFrom="";


public project_init(String property_name) {
	super();
	property = new Properties();
	init(property_name);
}

public void init(String property_name) {



		String app_path="";
		app_init ainit = bsController.getAppInit();
		try{


			app_path=ainit.get_path();
			if(app_path==null || app_path.equals("")) app_path="";
			else app_path+=".";
		}catch(Exception e){
		}


		if(property_name==null || property_name.equals("")) property_name = System.getProperty(id_property);

		if(property_name==null || property_name.equals(""))
			property_name = System.getProperty(app_path+id_property);

		if(property_name==null || property_name.equals(""))
			property_name = ainit.getResources_path().getProperty(id_property);

		if(property_name==null || property_name.equals(""))
			property_name = ainit.getResources_path().getProperty(app_path+id_property);


		if(property_name==null || property_name.equals("")){
			property_name = "projects";
			try{
				property = util_file.loadProperty(ainit.get_path_config()+property_name);
				loadedFrom+=" "+ainit.get_path_config()+property_name;
			}catch(Exception e){
				try{
					property = util_file.loadProperty(property_name);
					loadedFrom+=" "+property_name;
				}catch(Exception ex){
					new bsException(ex,iStub.log_DEBUG);
				}
			}
		}else{
			try{
				property = util_file.loadProperty(property_name);
				loadedFrom+=" "+property_name;
			}catch(Exception e){
				new bsException(e,iStub.log_DEBUG);
			}
		}

}

public Properties getProperty() {
	return property;
}

public void setProperty(Properties property) {
	this.property = property;
}

public String getLoadedFrom() {
	return loadedFrom;
}

}
