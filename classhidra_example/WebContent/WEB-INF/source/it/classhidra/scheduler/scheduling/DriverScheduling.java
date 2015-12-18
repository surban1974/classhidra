package it.classhidra.scheduler.scheduling; 


import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.scheduler.common.i_batch;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEngine;
import it.classhidra.scheduler.scheduling.thread.schedulingThreadProcess;
import it.classhidra.scheduler.servlets.servletBatchScheduling;



public class DriverScheduling{
	
	private static IBatchScheduling external;
	private static IBatchFactory factory;
	
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
	
	public static void init(IBatchScheduling _external, IBatchFactory _factory){
		if(_factory!=null)
			factory = _factory;
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
	
	public static void kill4timeout(){
		if(external!=null)
			external.kill4timeout();
		else
			servletBatchScheduling.kill4timeout();
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
	
	public static i_batch getBatchInstance(String cd_btch, String cls_batch) throws Exception{
		i_batch objBatch = null;
		if(factory!=null)
			objBatch = factory.getInstance(cd_btch, cls_batch);
		
		if(objBatch==null){
			objBatch = (i_batch)Class.forName(cls_batch).newInstance();
//			if(objBatch!=null) objBatch.setDb(batch);		
		}
		return objBatch;	
	}

	public static IBatchFactory getFactory() {
		return factory;
	}

	public static void setFactory(IBatchFactory factory) {
		DriverScheduling.factory = factory;
	}

	public static IBatchScheduling getExternal() {
		return external;
	}


}
