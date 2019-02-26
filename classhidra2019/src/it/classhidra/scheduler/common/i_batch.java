package it.classhidra.scheduler.common;

import it.classhidra.scheduler.scheduling.db.db_batch;

import java.util.HashMap;




public interface i_batch extends listener_batch{
	
	public final static String PROPERTY_EXCLUDE_DAYOFWEEK 	= "exclude_dayofweek";
	public final static String PROPERTY_EXCLUDE_DATE 		= "exclude_date";	
    public final static short STATE_NORMAL = 0; 
    public final static short STATE_SCHEDULED = -1;
    public final static short STATE_INEXEC = 1;
    public final static short STATE_SUSPEND = 10;
    public final static short STATE_WARNING = 1;
    public final static short STATE_OK = 0;
    public final static short STATE_KO = 2;   	
	
	public void readInput(String xml);
	public String execute() throws Exception;	
	public String writeOutput();
	public String getId();
	public String getVersion();
	public int getOrder();
	public String getDescription();
	public int getState();
	public String getExit();
	public HashMap<String,String> getInput();
	public HashMap<String,String> getOutput();
	public boolean isWriteLog();
	public void setDb(db_batch db);
}
