package it.classhidra.scheduler.common;

import java.util.HashMap;

public interface i_4Batch {
	
	public final static String o_FINDFORMLIST = "FINDFORMLIST";
	public final static String o_FIND = "FIND";	
	public final static String o_INSERT = "INSERT";
	public final static String o_DELETE = "DELETE";
	public final static String o_UPDATE = "UPDATE";	
	public final static String o_CLEAR_STATE = "CLEAR_STATE";

	public abstract Object operation(String oper, HashMap form)
			throws Exception;

}