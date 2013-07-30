package it.classhidra.scheduler.scheduling.thread;




import it.classhidra.core.tool.db.db_connection;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEngine;
import it.classhidra.scheduler.servlets.servletBatchScheduling;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;






public class schedulingThreadProcess extends Thread {

    private static final long serialVersionUID = -1L;
    private boolean threadDone = false;
    private Date scan_time;
    private ProcessBatchEngine pbe;
 


    public schedulingThreadProcess() {
        super();
        clearBatchState();
        threadDone=false;
    }

    public void run() {


    	batch_init binit = servletBatchScheduling.getConfiguration(); 
    	
        while (!threadDone) {
    		try {
				pbe = new ProcessBatchEngine();

				pbe.launch();
	
     			if(binit.getLsleep()>0){
     				scan_time = new Date(new Date().getTime()+binit.getLsleep());
     				Thread.sleep(binit.getLsleep());
    			}else{
    				threadDone=true;
        			pbe=null;
        			Thread.currentThread().interrupt();    				
    			}
    			
    		} catch (InterruptedException e) {
    			threadDone=true;
    			pbe=null;
    			Thread.currentThread().interrupt();
    		}
        }
     }

	public void setThreadDone(boolean threadDone) {
		this.threadDone = threadDone;
	}


	public void clearBatchState(){
		batch_init binit = servletBatchScheduling.getConfiguration(); 
		Connection conn=null;
		Statement st=null;
		try{
			conn = new db_connection().getContent();
			st = conn.createStatement();
			conn.setAutoCommit(false);
			st.executeUpdate("update "+binit.get_db_prefix()+"batch set state=0, st_exec=0, tm_next=null");
			conn.commit();
		}catch(Exception ex){
			new bsControllerMessageException(ex);
		}finally{
			db_connection.release(null, st, conn);
		}

	}

	public Date getScan_time() {
		return scan_time;
	}

	public ProcessBatchEngine getPbe() {
		return pbe;
	}
}
