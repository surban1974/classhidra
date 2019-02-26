package it.classhidra.scheduler.scheduling; 


import java.io.Serializable;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_provider;
import it.classhidra.scheduler.common.i_4Batch;
import it.classhidra.scheduler.common.i_batch;
import it.classhidra.scheduler.scheduling.init.batch_init;
import it.classhidra.scheduler.scheduling.process.ProcessBatchEngine;
import it.classhidra.scheduler.scheduling.thread.schedulingThreadProcess;
import it.classhidra.scheduler.servlets.servletBatchScheduling;



public class DriverScheduling implements Serializable{

	private static final long serialVersionUID = 1L;
	private static IBatchScheduling external;
	private static IBatchFactory factory;
	private static batch_init configuration;
	private static i_4Batch batchManager;
	
	
	public static DriverScheduling init(){
		DriverScheduling.init(
				bsController.checkSchedulerContainer(),
				
				new IBatchFactory() {
					
					private static final long serialVersionUID = 1L;

					//					@Override
					public i_batch getInstance(String cd_btch, String cls_batch) {
						try{
							i_batch instance = null;
							if(bsController.getAppInit()!=null && bsController.getAppInit().get_context_provider()!=null && !bsController.getAppInit().get_context_provider().equals("")){
								try{
									instance = (i_batch)util_provider.getBeanFromObjectFactory(bsController.getAppInit().get_context_provider(),  cd_btch, cls_batch, null);
								}catch(Exception e){
								}
							}
							if(instance==null && bsController.getAppInit()!=null && bsController.getAppInit().get_cdi_provider()!=null && !bsController.getAppInit().get_cdi_provider().equals("")){
								try{
									instance = (i_batch)util_provider.getBeanFromObjectFactory(bsController.getAppInit().get_cdi_provider(),  cd_btch, cls_batch, null);
								}catch(Exception e){
								}
							}
							if(instance==null && bsController.getAppInit()!=null && bsController.getAppInit().get_ejb_provider()!=null && !bsController.getAppInit().get_ejb_provider().equals("")){
								try{
									instance = (i_batch)util_provider.getBeanFromObjectFactory(bsController.getAppInit().get_ejb_provider(),  cd_btch, cls_batch, null);
								}catch(Exception e){
								}
							}
							if(instance==null && bsController.getCdiDefaultProvider()!=null){
								try{
									instance = (i_batch)util_provider.getBeanFromObjectFactory(bsController.getCdiDefaultProvider(),   cd_btch, cls_batch, null);
								}catch(Exception e){
								}
							}
							if(instance==null && bsController.getEjbDefaultProvider()!=null){
								try{
									instance = (i_batch)util_provider.getBeanFromObjectFactory(bsController.getEjbDefaultProvider(),   cd_btch, cls_batch, null);
								}catch(Exception e){
								}
							}

							if(instance == null)
								instance = (i_batch)Class.forName(cls_batch).newInstance();

							return instance;
						}catch(Exception e){
						}catch(Throwable t){
						}
						
						return null;
					}
				}
		);
		return null;
	}
	
	public static DriverScheduling init(IBatchScheduling _external){		
		return DriverScheduling.init(_external, null, null, null);
	}	
	
	public static DriverScheduling init(IBatchScheduling _external, IBatchFactory _factory){
		return DriverScheduling.init(_external, _factory, null, null);
	}
	
	public static DriverScheduling init(IBatchScheduling _external, IBatchFactory _factory, batch_init _configuration){
		return DriverScheduling.init(_external, _factory, null, null);
	}	
	
	public static DriverScheduling init(IBatchScheduling _external, IBatchFactory _factory, batch_init _configuration, i_4Batch _batchManager){
		if(_configuration!=null)
			configuration = _configuration;
		if(_factory!=null)
			factory = _factory;
		if(_batchManager!=null)
			batchManager = _batchManager;
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
		return null;
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
		if(configuration!=null)
			return configuration;
		else if(external!=null)
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

	public static i_4Batch getBatchManager() {
		return batchManager;
	}

	public static void setBatchManager(i_4Batch batchManager) {
		DriverScheduling.batchManager = batchManager;
	}


}
