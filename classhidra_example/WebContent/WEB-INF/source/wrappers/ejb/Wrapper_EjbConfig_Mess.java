package wrappers.ejb;



import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.controller.i_ProviderWrapper;
import it.classhidra.core.controller.load_message;

@Startup
@Singleton(name="ejb_mess_config")
@Local(i_ProviderWrapper.class)

public class Wrapper_EjbConfig_Mess extends bsProvidedWrapper implements i_ProviderWrapper {
	private static final long serialVersionUID = 1L;
	private load_message instance = new load_message();

	
	@Override
	public Object getInstance() {
		return instance;
	}

	@Override
	public boolean setInstance(Object _instance) {
		if(_instance instanceof load_message)
			instance=(load_message)_instance;
		return true;
	}

}
