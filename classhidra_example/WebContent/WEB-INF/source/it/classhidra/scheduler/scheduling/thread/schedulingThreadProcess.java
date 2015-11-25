package it.classhidra.scheduler.scheduling.thread;




import java.util.Date;
import java.util.HashMap;

import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.scheduler.common.i_4Batch;
import it.classhidra.scheduler.scheduling.DriverScheduling;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEngine;






public class schedulingThreadProcess extends Thread {


    private boolean threadDone = false;
    private Date scan_time;
//    private ProcessBatchEngine pbe;
 


    public schedulingThreadProcess() {
        super();
        clearBatchState();
        threadDone=false;
    }

    public void run() {


    	batch_init binit = DriverScheduling.getConfiguration(); 
    	
        while (!threadDone) {
    		try {
if(DriverScheduling.getPbe()!=null) 
	DriverScheduling.getPbe().interruptThreadEvents();
//else
//	servletBatchScheduling.setPbe(new ProcessBatchEngine());

	
//				pbe = new ProcessBatchEngine();

DriverScheduling.getPbe().launch();
//				pbe.launch();
	
     			if(binit.getLsleep()>0){
     				scan_time = new Date(new Date().getTime()+binit.getLsleep());
     				Thread.sleep(binit.getLsleep());
    			}else{
    				threadDone=true;
//        			pbe=null;
        			Thread.currentThread().interrupt();    				
    			}
    			
    		} catch (InterruptedException e) {
    			threadDone=true;
 //   			pbe=null;
    			Thread.currentThread().interrupt();
    		}
        }
     }

	public void setThreadDone(boolean threadDone) {
		this.threadDone = threadDone;
	}


	public void clearBatchState(){		
		batch_init binit = DriverScheduling.getConfiguration(); 
		
		
		HashMap form = new HashMap();

		try{
			binit.get4BatchManager().operation(i_4Batch.o_CLEAR_BATCH_STATES, form);
		}catch(Exception e){
			new bsException(e,iStub.log_ERROR);
		}		
/*		
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
*/
	}

	public Date getScan_time() {
		return scan_time;
	}

	public ProcessBatchEngine getPbe() {
		return DriverScheduling.getPbe();
//		return pbe;
	}
	
}
