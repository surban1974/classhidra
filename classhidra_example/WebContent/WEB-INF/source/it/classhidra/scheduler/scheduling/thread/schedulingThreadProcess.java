package it.classhidra.scheduler.scheduling.thread;




import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.scheduler.common.i_4Batch;
import it.classhidra.scheduler.scheduling.DriverScheduling;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEngine;






public class schedulingThreadProcess extends Thread implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean threadDone = false;
    private Date scan_time;

    public schedulingThreadProcess() {
        super();
        clearBatchState();
        kill4Timeout();
        threadDone=false;
    }

    public void run() {


    	batch_init binit = DriverScheduling.getConfiguration(); 
    	
        while (!threadDone) {
    		try {
				if(DriverScheduling.getPbe()!=null) 
					DriverScheduling.getPbe().interruptThreadEvents();
				
				DriverScheduling.getPbe().launch();
	
     			if(binit.getLsleep()>0){
     				scan_time = new Date(new Date().getTime()+binit.getLsleep());
     				Thread.sleep(binit.getLsleep());
    			}else{
    				threadDone=true;
        			Thread.currentThread().interrupt();    				
    			}
    			
    		} catch (InterruptedException e) {
    			threadDone=true;
    			Thread.currentThread().interrupt();
    		}
        }
     }

	public void setThreadDone(boolean threadDone) {
		this.threadDone = threadDone;
	}


	public void clearBatchState(){		
		batch_init binit = DriverScheduling.getConfiguration(); 
		
		
		HashMap<String,Object> form = new HashMap<String, Object>();

		try{
			binit.get4BatchManager().operation(i_4Batch.o_CLEAR_BATCH_STATES, form);
		}catch(Exception e){
			new bsException(e,iStub.log_ERROR);
		}		
	}
	
	public void kill4Timeout(){		
		batch_init binit = DriverScheduling.getConfiguration(); 		
		
		HashMap<String,Object> form = new HashMap<String, Object>();

		try{
			binit.get4BatchManager().operation(i_4Batch.o_KILL4TIMEOUT, form);
		}catch(Exception e){
			new bsException(e,iStub.log_ERROR);
		}		
	}	

	public Date getScan_time() {
		return scan_time;
	}

	public ProcessBatchEngine getPbe() {
		return DriverScheduling.getPbe();
	}
	
}
