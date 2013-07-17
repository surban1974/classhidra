package it.classhidra.scheduler.common;

import it.classhidra.scheduler.scheduling.db.db_batch;

import java.util.HashMap;




public interface i_batch {
	public void readInput(String xml);
	public String execute() throws Exception;	
	public String writeOutput();
	public String getId();
	public String getVersion();
	public int getOrder();
	public String getDescription();
	public int getState();
	public String getExit();
	public HashMap getInput();
	public HashMap getOutput();
	public boolean isWriteLog();
	public void setDb(db_batch db);
}
