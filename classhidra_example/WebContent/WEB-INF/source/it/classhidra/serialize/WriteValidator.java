package it.classhidra.serialize;

import java.io.Serializable;
import java.util.Stack;

public interface WriteValidator extends Serializable {
	boolean isWritable(Object current, Stack parents, String name);
}
