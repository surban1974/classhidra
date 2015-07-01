package it.classhidra.plugin.provider.cdi.wrappers;
 
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import it.classhidra.core.controller.bsConstants;
import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.controller.info_navigation;

@Named(bsConstants.CONST_BEAN_$NAVIGATION)
@SessionScoped
public class Wrapper_Navigation extends bsProvidedWrapper {
	private static final long serialVersionUID = 1L;

	@Inject
	private info_navigation instance;

	Wrapper_Navigation(){
		super();
	}	
	
	@Override
	public Object getInstance() {
		if(instance!=null && (instance.getId()==null || instance.getId().equals("")))
			return null;
		return instance;
	}

	@Override
	public boolean setInstance(Object _instance) {
		if(_instance!=null && _instance instanceof info_navigation){
		if(instance!=null && (instance.getId()==null || instance.getId().equals(""))){
			try{
				instance.reInit((info_navigation)_instance);
			}catch(Exception e){
				
			}
		}
		}
		return true;
/*		
		if(_instance instanceof info_navigation){
			instance = (info_navigation)_instance;
			return true;
		}else
			return false;
*/			
	}

}
