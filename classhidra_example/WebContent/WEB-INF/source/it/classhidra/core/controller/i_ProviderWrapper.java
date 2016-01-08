package it.classhidra.core.controller;

public interface i_ProviderWrapper {

	Object getInstance();

	boolean setInstance(Object instance);

	info_context getInfo_context();
}