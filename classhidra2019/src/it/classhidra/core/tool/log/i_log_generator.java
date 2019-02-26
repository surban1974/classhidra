package it.classhidra.core.tool.log;

import it.classhidra.core.init.log_init;
import it.classhidra.core.tool.log.stubs.iStub;



public interface i_log_generator {

	public abstract i_log_pattern get_log_Pattern();

	public abstract iStub get_log_Stub();

	public abstract void writeLog(String msg, String level) throws Exception;

	public abstract void writeLog(String msg, String userIP,
			String userMatricola, String classFrom, String level)
			throws Exception;
	
	public abstract void writeLog(Object obj_request, String msg,String userIP,
			String userMatricola,String classFrom, String level)
		throws Exception;


	public abstract boolean isReadError();

	public abstract void setReadError(boolean b);
	
	public void setInit(log_init _init);
	
	public String get_log_Content(String lineSep) throws Exception;

}