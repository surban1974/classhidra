package it.classhidra.annotation;

import java.util.HashMap;

public interface i_annotation_scanner {

	void loadAllObjects(String _package_annotated,
			HashMap redirects);

	void loadAllObjects(HashMap redirects);

	void loadObject();

	HashMap get_streams();

	HashMap get_beans();

	HashMap get_redirects();
	
	HashMap get_redirectsjustloaded();

	HashMap get_transformationoutput();

	HashMap get_actions();

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