package wrappers.cdi;



import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.controller.load_authentication;


@Named(bsConstants.CONST_BEAN_$AUTH_CONFIG)
@ApplicationScoped

public class Wrapper_CdiConfig_Auth extends bsProvidedWrapper {
	private static final long serialVersionUID = 1L;

	private static load_authentication instance = new load_authentication();

	
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
