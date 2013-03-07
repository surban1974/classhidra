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

	HashMap get_transformationoutput();

	HashMap get_actions();

	String getError();

	String getAuth_error();

	String getSession_error();

}