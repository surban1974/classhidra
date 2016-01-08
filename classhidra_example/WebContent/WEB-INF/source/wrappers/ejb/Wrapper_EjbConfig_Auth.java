package wrappers.ejb;



import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.controller.i_ProviderWrapper;
import it.classhidra.core.controller.load_authentication;


@Startup
@Singleton(name="ejb_auth_config")
@Local(i_ProviderWrapper.class)

public class Wrapper_EjbConfig_Auth extends bsProvidedWrapper implements i_ProviderWrapper {
	private static final long serialVersionUID = 1L;
	private load_authentication instance = new load_authentication();
	
	@Override
	public Object getInstance() {
		return instance;
	}

	@Override
	public boolean setInstance(Object _instance) {
		if(_instance instanceof load_authentication)
			instance=(load_authentication)_instance;
		return true;
	}

}
