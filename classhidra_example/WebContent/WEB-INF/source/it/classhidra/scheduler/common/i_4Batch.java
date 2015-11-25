package it.classhidra.scheduler.common;

import java.util.HashMap;

public interface i_4Batch {
	
	public final static String o_FINDFORMLIST = "FINDFORMLIST";
	public final static String o_FIND = "FIND";	
	public final static String o_FIND_SIMPLE = "FIND_SIMPLE";	
	public final static String o_LOAD_BATCH_PROPERTIES = "LOAD_BATCH_PROPERTIES";
	public final static String o_INSERT = "INSERT";
	public final static String o_DELETE = "DELETE";
	public final static String o_UPDATE = "UPDATE";	
	public final static String o_UPDATE_STATE = "UPDATE_STATE";
	public final static String o_UPDATE_STATES_AND_NEXTEXEC = "UPDATE_STATES_AND_NEXTEXEC";
	public final static String o_CLEAR_STATE = "CLEAR_STATE";
	public final static String o_CLEAR_BATCH_STATES = "CLEAR_BATCH_STATES";
	public final static String o_WRITE_LOG = "WRITE_LOG";

	public abstract Object operation(String oper, HashMap form)
			throws Exception;

}