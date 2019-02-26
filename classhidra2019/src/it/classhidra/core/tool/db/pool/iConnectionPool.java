package it.classhidra.core.tool.db.pool;

import it.classhidra.core.init.db_init;

import java.sql.Connection;
import java.util.Vector;

public interface iConnectionPool {

	public abstract void init(db_init init);
	
	public abstract void reapConnections();

	public abstract void closeConnections();

	public abstract Connection getConnection() throws Exception;

	public abstract void returnConnection(Connection conn);

	public abstract Vector<Connection> getConnections();

}