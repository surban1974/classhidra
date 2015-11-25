package it.classhidra.scheduler.scheduling; 


import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEngine;
import it.classhidra.scheduler.scheduling.thread.schedulingThreadProcess;
import it.classhidra.scheduler.servlets.servletBatchScheduling;



public class DriverScheduling{
	
	private static IBatchScheduling external;
	
	public static void init(IBatchScheduling _external){
		if(_external!=null){
			try{
				_external.stop();
			}catch(Exception e){
				new bsException("Scheduler: "+e.toString(),iStub.log_ERROR);
			}			
		}
		external = _external;
		if(servletBatchScheduling.isActive()){
			try{
				servletBatchScheduling.stop();
			}catch(Exception e){
				new bsException("Scheduler: "+e.toString(),iStub.log_ERROR);
			}
		}
	}
	
	public static void start(){
		if(external!=null)
			external.start();
		else
			servletBatchScheduling.start();
	}	

	public static void reScan(){
		if(external!=null)
			external.reScan();
		else
			servletBatchScheduling.reScan();
	}
	
	public static void reStart(){
		if(external!=null)
			external.reStart();
		else
			servletBatchScheduling.reStart();
	}
	
	public static void stop(){
		if(external!=null)
			external.stop();
		else
			servletBatchScheduling.stop();
	}

	public static schedulingThreadProcess getThProcess() {
		if(external!=null)
			return external.getThProcess();
		else
			return servletBatchScheduling.getThProcess();
	}

	public static void clearContainer(){
		if(external!=null)
			external.clearContainer();
		else
			servletBatchScheduling.clearContainer();
	}

	public static batch_init getConfiguration() {
		if(external!=null)
			return external.getConfiguration();
		else
			return servletBatchScheduling.getConfiguration();
	}

	public static ProcessBatchEngine getPbe() {
		if(external!=null)
			return external.getPbe();
		else
			return servletBatchScheduling.getPbe();
	}

	public static boolean isActive() {
		if(external!=null)
			return external.isActive();
		else
			return servletBatchScheduling.isActive();
	}	

}
