package it.classhidra.scheduler.common;

import java.io.Serializable;
import java.util.HashMap;

public interface i_4Batch extends Serializable{
	
	public final static String o_FINDFORMLIST = "FINDFORMLIST";
	public final static String o_FIND = "FIND";	
	public final static String o_FIND_SIMPLE = "FIND_SIMPLE";	
	
	public final static String o_LOAD_BATCH_PROPERTIES = "LOAD_BATCH_PROPERTIES";
	public final static String o_DELETE_BATCH_PROPERTIES = "DELETE_BATCH_PROPERTIES";
	public final static String o_INSERT_BATCH_PROPERTY = "INSERT_BATCH_PROPERTY";
	
	public final static String o_INSERT = "INSERT";
	public final static String o_DELETE = "DELETE";
	public final static String o_UPDATE = "UPDATE";	
	
	public final static String o_UPDATE_STATE = "UPDATE_STATE";
	public final static String o_UPDATE_STATES_AND_NEXTEXEC = "UPDATE_STATES_AND_NEXTEXEC";
	public final static String o_CLEAR_STATE = "CLEAR_STATE";
	public final static String o_CLEAR_BATCH_STATES = "CLEAR_BATCH_STATES";
	public final static String o_KILL4TIMEOUT = "KILL4TIMEOUT";
	public final static String o_WRITE_LOG = "WRITE_LOG";
	public final static String o_INSTANCE_LOG_OBJECT = "INSTANCE_LOG_OBJECT";

	public abstract Object operation(String oper, HashMap<String,?> form)
			throws Exception;

}