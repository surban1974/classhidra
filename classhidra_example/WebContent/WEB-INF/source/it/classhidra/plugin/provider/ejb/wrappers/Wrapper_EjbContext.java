package it.classhidra.plugin.provider.ejb.wrappers;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Stateless;

import it.classhidra.core.controller.bsProvidedWrapper;



//@Stateless(name="Wrapper_EjbContext", mappedName = DependencyInjection.LOOKUP_MAPPED_EJBCONTEXT)
@Stateless(name="WrapperEjbContext")
@Local(Wrapper_EjbContextLocal.class)
public class Wrapper_EjbContext extends bsProvidedWrapper implements Wrapper_EjbContextLocal {

	private static final long serialVersionUID = -1L;
	
	
	@Resource
	private EJBContext ejbContext;
	

	public Object getInstance() {
		return ejbContext;
	}

	public boolean setInstance(Object instance) {
		return false;
	}

}
