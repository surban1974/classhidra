package wrappers.cdi;



import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.tool.jaas_authentication.load_users;


@Named(bsConstants.CONST_BEAN_$USERS_CONFIG)
@ApplicationScoped

public class Wrapper_CdiConfig_Users extends bsProvidedWrapper {
	private static final long serialVersionUID = 1L;
	private static load_users instance;

	
	@Override
	public Object getInstance() {
		return instance;
	}

	@Override
	public boolean setInstance(Object _instance) {
		if(_instance instanceof load_users)
			instance=(load_users)_instance;
		return true;
	}
}
