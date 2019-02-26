package it.classhidra.serialize;


import java.util.Map;
import it.classhidra.core.controller.i_bean;

public interface XmlMapper {
	final String CONST_ID_CHECKFORDESERIALIZE = "$checkFordeserialize";
	Map mapping(i_bean bean, String xml, Map table);
}
