package wrappers.cdi;



import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.controller.load_organization;

@Named(bsConstants.CONST_BEAN_$ORGANIZATION_CONFIG)
@ApplicationScoped

public class Wrapper_CdiConfig_Organization extends bsProvidedWrapper {
	private static final long serialVersionUID = 1L;

	private static load_organization instance = new load_organization();

	
	@Override
	public Object getInstance() {
		return instance;
	}

	@Override
	public boolean setInstance(Object _instance) {
		if(_instance instanceof load_organization)
			instance=(load_organization)_instance;
		return true;
	}

}
