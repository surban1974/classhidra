package wrappers.ejb;



import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.controller.i_ProviderWrapper;
import it.classhidra.core.tool.jaas_authentication.load_users;


@Startup
@Singleton(name="ejb_users_config")
@Local(i_ProviderWrapper.class)
public class Wrapper_EjbConfig_Users extends bsProvidedWrapper implements i_ProviderWrapper{
	private static final long serialVersionUID = 1L;
	private load_users instance;

	
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
