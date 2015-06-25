package it.classhidra.core.controller;

import java.io.Serializable;

public abstract class bsProvidedWrapper implements Serializable{
	private static final long serialVersionUID = 1L;
	public abstract Object getInstance();
	public abstract boolean setInstance(Object instance);
}
