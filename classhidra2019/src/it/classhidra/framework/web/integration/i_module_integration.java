package it.classhidra.framework.web.integration;

import it.classhidra.core.tool.integration.i_integration;

public interface i_module_integration extends i_integration{
	public final static String FROM = "APP";
//	public final static String o_FINDFORMLIST = "FINDFORMLIST";
	public final static String o_FIND = "FIND";	
	public final static String o_FINDCONTROL = "FINDCONTROL";
	public final static String o_FINDTOTAL = "FINDTOTAL";
	public final static String o_INSERT = "INSERT";
	public final static String o_DELETE = "DELETE";
	public final static String o_UPDATE = "UPDATE";
	public final static String o_LOGIN = "LOGIN";
	public final static String o_SEND = "SEND";
	public final static String o_FROMSTORAGE = "FROMSTORAGE";
	
	public abstract Object operation(String oper, Object form) throws Exception;

}