package wrappers.ejb;



import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.controller.i_ProviderWrapper;
import it.classhidra.core.controller.load_organization;

@Startup
@Singleton(name="ejb_organization_config")
@Local(i_ProviderWrapper.class)
public class Wrapper_EjbConfig_Organization extends bsProvidedWrapper implements i_ProviderWrapper {
	private static final long serialVersionUID = 1L;
	private load_organization instance = new load_organization();

	
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
