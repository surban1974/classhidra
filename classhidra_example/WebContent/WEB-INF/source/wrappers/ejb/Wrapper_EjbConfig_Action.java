package wrappers.ejb;



import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.controller.i_ProviderWrapper;
import it.classhidra.core.controller.load_actions;


@Startup
@Singleton(name="ejb_action_config")
@Local(i_ProviderWrapper.class)
public class Wrapper_EjbConfig_Action extends bsProvidedWrapper implements i_ProviderWrapper{
	private static final long serialVersionUID = 1L;
	private load_actions instance = new load_actions();

	
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
