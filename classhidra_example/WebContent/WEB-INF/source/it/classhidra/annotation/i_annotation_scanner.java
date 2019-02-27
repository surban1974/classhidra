package it.classhidra.annotation;

import java.util.HashMap;

import it.classhidra.core.controller.info_action;
import it.classhidra.core.controller.info_bean;
import it.classhidra.core.controller.info_redirect;
import it.classhidra.core.controller.info_stream;
import it.classhidra.core.controller.info_transformation;

public interface i_annotation_scanner {

	void loadAllObjects(String _package_annotated,
			HashMap<String,info_redirect> redirects);

	void loadAllObjects(HashMap<String,info_redirect> redirects);

	void loadObject();

	HashMap<String,info_stream> get_streams();

	HashMap<String,info_bean> get_beans();

	HashMap<String,info_redirect> get_redirects();
	
	HashMap<String,info_redirect> get_redirectsjustloaded();

	HashMap<String,info_transformation> get_transformationoutput();

	HashMap<String,info_action> get_actions();

	String getError();

	String getAuth_error();

	String getSession_error();
	
	String getListener_actions();

	String getListener_beans();

	String getListener_streams();
	
	String getMemoryInContainer_streams();
	
	String getProvider();
	
	String getInstance_navigated();

	String getInstance_local_container();
	
	String getInstance_scheduler_container();
	
	String getInstance_onlysession();
	
	String getInstance_servletcontext();
	

}