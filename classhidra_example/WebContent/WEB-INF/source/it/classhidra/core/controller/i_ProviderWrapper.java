package it.classhidra.core.controller;

import java.io.Serializable;

public interface i_ProviderWrapper extends Serializable{

	Object getInstance();

	boolean setInstance(Object instance);

	info_context getInfo_context();
}