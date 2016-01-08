package wrappers.cdi;



import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.controller.load_message;

@Named(bsConstants.CONST_BEAN_$MESS_CONFIG)
@ApplicationScoped

public class Wrapper_CdiConfig_Mess extends bsProvidedWrapper {
	private static final long serialVersionUID = 1L;

	private static load_message instance = new load_message();

	
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
