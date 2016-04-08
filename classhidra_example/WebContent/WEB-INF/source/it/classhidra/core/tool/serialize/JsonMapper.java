package it.classhidra.core.tool.serialize;


import java.util.Map;

public interface JsonMapper {
	Map mapping(String json, Map table);
}
