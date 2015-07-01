package it.classhidra.plugin.provider.ejb.wrappers;

import javax.ejb.Local;

@Local
public interface Wrapper_EjbContextLocal {

	public abstract Object getInstance();

	public abstract boolean setInstance(Object instance);

}