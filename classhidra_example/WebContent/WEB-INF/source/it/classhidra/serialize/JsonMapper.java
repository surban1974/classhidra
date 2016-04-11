package it.classhidra.serialize;


import java.util.Map;

public interface JsonMapper {
	final String CONST_ID_CHECKFORDESERIALIZE = "$checkFordeserialize";
	Map mapping(String json, Map table);
}
