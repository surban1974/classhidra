package it.classhidra.serialize;


import java.util.Map;

import it.classhidra.core.controller.i_bean;

public interface JsonMapper {
	final String CONST_ID_CHECKFORDESERIALIZE = "$checkFordeserialize";
	Map mapping(i_bean bean, String json, Map table);
}
