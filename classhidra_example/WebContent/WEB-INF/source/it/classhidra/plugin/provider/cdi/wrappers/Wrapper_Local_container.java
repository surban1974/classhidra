package it.classhidra.plugin.provider.cdi.wrappers;

import java.util.Map; 
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsProvidedWrapper;

@Named(bsConstants.CONST_BEAN_$LOCAL_CONTAINER)
@ApplicationScoped

public class Wrapper_Local_container extends bsProvidedWrapper {
	private static final long serialVersionUID = 1L;

	private static ConcurrentHashMap instance = new ConcurrentHashMap();

	
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
