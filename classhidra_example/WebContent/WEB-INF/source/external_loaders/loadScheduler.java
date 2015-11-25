package external_loaders;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_externalloader;
import it.classhidra.scheduler.scheduling.DriverScheduling;


public class loadScheduler implements i_externalloader{


	
	public Object getProperty(String key) {
		return null;
	}

	
	
	public void load() {
		try{
			DriverScheduling.init(bsController.checkSchedulerContainer());
			DriverScheduling.reStart();
//			servletBatchScheduling.reStart();

		}catch(Exception e){
			e.toString();
		}catch(Throwable t){
			t.toString();
		}

	}



	public void setProperty(String key, Object value) {
	
	}

}
