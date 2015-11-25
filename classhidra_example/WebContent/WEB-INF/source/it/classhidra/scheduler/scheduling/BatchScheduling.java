package it.classhidra.scheduler.scheduling; 


import java.io.Serializable;

import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEngine;
import it.classhidra.scheduler.scheduling.thread.schedulingThreadProcess;



public class BatchScheduling implements Serializable, IBatchScheduling{
	private static final long serialVersionUID = 1L;
	private schedulingThreadProcess thProcess=null;   
	private batch_init configuration;
	private final ProcessBatchEngine pbe = new ProcessBatchEngine();
	private boolean active = false;

	
	@Override
	public void start(){

		configuration=new batch_init();
		
		try{
			if(configuration.get_active().toLowerCase().trim().equals("true")){
				if(thProcess!=null){
					clearContainer();
					thProcess.setThreadDone(true);
					thProcess.interrupt();
					thProcess=null;
					
				}
				thProcess = new schedulingThreadProcess();
				thProcess.start();
				new bsException("Scheduler:Start schedulingThreadProcess");
				active = true;
			}
		}catch(Exception e){	
			new bsException("ERROR Scheduler:Start schedulingThreadProcess "+e.toString(),iStub.log_ERROR);
		}
	}	

	@Override
	public void reScan(){
		reStart();
	}
	
	@Override
	public void reStart(){
		active = false;
		try{
			configuration=new batch_init();
			clearContainer();
			
			if(thProcess!=null){
				thProcess.setThreadDone(true);
				thProcess.interrupt();
				thProcess=null;
			}

			thProcess = new schedulingThreadProcess();
			thProcess.start();
			new bsException("Scheduler: ReStart schedulingThreadProcess", iStub.log_INFO);
			active = true;
		}catch(Exception e){	
			new bsException("ERROR Scheduler:ReStart schedulingThreadProcess "+e.toString(),iStub.log_ERROR);
		}
	}
	
	@Override
	public void stop(){
		try{
			configuration=new batch_init();
			clearContainer();
			if(thProcess!=null){
				thProcess.setThreadDone(true);
				thProcess.interrupt();
				thProcess=null;
			}
			new bsException("Scheduler: Stop schedulingThreadProcess", iStub.log_INFO);
			active = false;
		}catch(Exception e){	
			new bsException("ERROR Scheduler:Stop schedulingThreadProcess "+e.toString(),iStub.log_ERROR);
		}
	}




	@Override
	public void clearContainer(){
		try{
			if(pbe!=null)
				pbe.interruptThreadEvents();
		}catch(Exception e){
			new bsException("ERROR Scheduler:Clean schedulingThreadProcess "+e.toString(),iStub.log_ERROR);
		}
	}
	


	@Override
	public batch_init getConfiguration() {
		if(configuration==null) configuration=new batch_init(); 
		if(configuration.getLoadedFrom().trim().equals("")){
			configuration=new batch_init();
		}
		return configuration;
	}

	@Override
	public ProcessBatchEngine getPbe() {
		return pbe;
	}
	
	@Override
	public schedulingThreadProcess getThProcess() {
		return thProcess;
	}

	@Override
	public boolean isActive() {
		return active;
	}	


}
