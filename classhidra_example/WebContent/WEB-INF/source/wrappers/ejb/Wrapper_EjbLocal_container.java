package wrappers.ejb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.controller.i_ProviderWrapper;

@Startup
@Singleton(name="ejb_local_container")
@Local(i_ProviderWrapper.class)
public class Wrapper_EjbLocal_container extends bsProvidedWrapper implements i_ProviderWrapper{
	private static final long serialVersionUID = 1L;
	private  ConcurrentHashMap instance = new ConcurrentHashMap();
	
	@Override
	public Object getInstance() {
		return instance;
	}

	@Override
	public boolean setInstance(Object _instance) {
		if(instance!=null){
			if(_instance!=null && _instance instanceof Map)
				instance.putAll((Map)_instance);
		}
		return true;
	}

}
