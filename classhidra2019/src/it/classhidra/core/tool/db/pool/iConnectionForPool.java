package it.classhidra.core.tool.db.pool;

import java.sql.Connection;



public interface iConnectionForPool  extends Connection{

	public abstract boolean inUse();

}