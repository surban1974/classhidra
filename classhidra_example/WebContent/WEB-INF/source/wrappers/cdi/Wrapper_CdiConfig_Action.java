package wrappers.cdi;



import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.controller.load_actions;


@Named(bsConstants.CONST_BEAN_$ACTION_CONFIG)
@ApplicationScoped

public class Wrapper_CdiConfig_Action extends bsProvidedWrapper {
	private static final long serialVersionUID = 1L;
	private static load_actions instance = new load_actions();

	
	@Override
	public Object getInstance() {
		return instance;
	}

	@Override
	public boolean setInstance(Object _instance) {
		if(_instance instanceof load_actions)
			instance=(load_actions)_instance;
		return true;
	}
}
