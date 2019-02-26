package it.classhidra.core.controller;

import java.io.Serializable;

public abstract class bsProvidedWrapper implements Serializable, i_ProviderWrapper{
	private static final long serialVersionUID = 1L;
	private info_context iContext = new info_context(this.getClass());
	
	public abstract Object getInstance();
	public abstract boolean setInstance(Object instance);
	public info_context getInfo_context(){
		return iContext;
	}
}
