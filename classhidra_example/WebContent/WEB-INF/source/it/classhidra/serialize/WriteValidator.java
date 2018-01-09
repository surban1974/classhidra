package it.classhidra.serialize;

import java.io.Serializable;

public interface WriteValidator extends Serializable {
	boolean isWritable(Object obj);
}
