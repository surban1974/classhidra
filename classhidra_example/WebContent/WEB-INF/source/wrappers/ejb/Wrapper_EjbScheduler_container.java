package wrappers.ejb;


import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.controller.i_ProviderWrapper;
import it.classhidra.core.tool.exception.bsException;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.scheduler.scheduling.BatchScheduling;

@Startup
@Singleton(name="ejb_scheduler_container")
@Local(i_ProviderWrapper.class)
public class Wrapper_EjbScheduler_container extends bsProvidedWrapper implements i_ProviderWrapper{
	private static final long serialVersionUID = 1L;
	private BatchScheduling instance;

	
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
