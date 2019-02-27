package it.classhidra.scheduler.scheduling.db;

import java.io.Serializable;
import java.util.HashMap;

import it.classhidra.core.tool.elements.i_elementDBBase;

public interface i_batch_log extends Serializable{

	void reimposta();

	void reInit(i_elementDBBase _i_el);

	void reInit(java.sql.ResultSet rs);

	String sql_Select();

	String sql_Delete();

	String sql_Insert();

	String sql_Update(i_elementDBBase _i_element_mod);

	boolean find(i_elementDBBase _i_el);

	boolean equals(i_elementDBBase _i_el);

	boolean control();

	String getCd_btch();

	void setCd_btch(String value);

	java.sql.Timestamp getTm_start();

	void setTm_start(java.sql.Timestamp value);

	java.sql.Timestamp getTm_fin();

	void setTm_fin(java.sql.Timestamp value);

	Integer getSt_exec();

	void setSt_exec(Integer value);

	String getDsc_exec();

	String getDsc_exec_br();

	void setDsc_exec(String value);

	boolean isWriteLog();

	void setWriteLog(boolean writeLog);

	Integer getCd_ist();

	void setCd_ist(Integer cdIst);
	
	HashMap<String,?> getFields();

}