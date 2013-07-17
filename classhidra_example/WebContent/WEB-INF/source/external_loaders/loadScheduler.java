package external_loaders;

import it.classhidra.core.controller.i_externalloader;
import it.classhidra.scheduler.servlets.servletBatchScheduling;

public class loadScheduler implements i_externalloader{


	
	public Object getProperty(String key) {
		return null;
	}

	
	
	public void load() {
		try{
			servletBatchScheduling.reStart();

		}catch(Exception e){
			e.toString();
		}catch(Throwable t){
			t.toString();
		}

	}



	public void setProperty(String key, Object value) {
	
	}

}
