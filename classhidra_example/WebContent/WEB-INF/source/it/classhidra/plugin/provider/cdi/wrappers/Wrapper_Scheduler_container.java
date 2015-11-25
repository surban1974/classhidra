package it.classhidra.plugin.provider.cdi.wrappers;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.scheduler.scheduling.BatchScheduling;

@Named(bsConstants.CONST_BEAN_$SCHEDULER_CONTAINER)
@ApplicationScoped

public class Wrapper_Scheduler_container extends bsProvidedWrapper {
	private static final long serialVersionUID = 1L; 

	private static BatchScheduling instance;

	
	@Override
	public Object getInstance() {
		if(instance==null){
			instance = new BatchScheduling();
//			instance.start();
		}
		return instance;
	}

	@Override
	public boolean setInstance(Object _instance) {
		if(instance!=null && _instance instanceof BatchScheduling){
			try{
			instance.stop();
			((BatchScheduling)_instance).stop();
			instance = (BatchScheduling)_instance;
			instance.start();
			}catch(Exception e){
				new bsException(e,iStub.log_ERROR);
				return false;
			}
		}
		return true;
	}

}
