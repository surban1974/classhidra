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
	
	public String getListener_actions();

	public String getListener_beans();

	public String getListener_streams();
	
	public String getMemoryInContainer_streams();
	
	public String getProvider();
	
	public String getInstance_navigated();

	public String getInstance_local_container();

}