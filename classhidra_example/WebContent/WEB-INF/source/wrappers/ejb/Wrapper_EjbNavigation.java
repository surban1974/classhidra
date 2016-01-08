package wrappers.ejb;
 
import javax.ejb.Local;
import javax.ejb.Stateful;

import it.classhidra.core.controller.bsProvidedWrapper;
import it.classhidra.core.controller.i_ProviderWrapper;
import it.classhidra.core.controller.info_navigation;


@Stateful(name="ejb_navigation")
@Local(i_ProviderWrapper.class)
public class Wrapper_EjbNavigation extends bsProvidedWrapper implements i_ProviderWrapper{
	private static final long serialVersionUID = 1L;
	private info_navigation instance = new info_navigation();

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
	}

}
