package pool;

import it.classhidra.core.tool.db.pool.iConnectionForPool;
import it.classhidra.core.tool.db.pool.iConnectionPool;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

public class LocalMySqlConnectionForPool implements Connection, iConnectionForPool {

    private iConnectionPool pool;
    private Connection conn;
    private boolean inuse;
    private long timestamp;


    public LocalMySqlConnectionForPool(Connection conn, iConnectionPool pool) {
        this.conn=conn;
        this.pool=pool;
        this.inuse=false;
        this.timestamp=0;
    }

    public synchronized boolean lease() {
       if(inuse)  {
           return false;
       } else {
          inuse=true;
          timestamp=System.currentTimeMillis();
          return true;
       }
    }
    public boolean validate() {
	try {
            conn.getMetaData();
        }catch (Exception e) {
	    return false;
	}
	return true;
    }

    /* (non-Javadoc)
	 * @see pool.iConnectionForPool#inUse()
	 */
    public boolean inUse() {
        return inuse;
    }

    public long getLastUse() {
        return timestamp;
    }

    public void close() throws SQLException {
        pool.returnConnection(this);
    }

    protected void expireLease() {
        inuse=false;
    }

    protected Connection getConnection() {
        return conn;
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return conn.prepareCall(sql);
    }

    public Statement createStatement() throws SQLException {
        return conn.createStatement();
    }

    public String nativeSQL(String sql) throws SQLException {
        return conn.nativeSQL(sql);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        conn.setAutoCommit(autoCommit);
    }

    public boolean getAutoCommit() throws SQLException {
        return conn.getAutoCommit();
    }

    public void commit() throws SQLException {
        conn.commit();
    }

    public void rollback() throws SQLException {
        conn.rollback();
    }

    /* (non-Javadoc)
	 * @see pool.iConnectionForPool#isClosed()
	 */
    public boolean isClosed() throws SQLException {
        return conn.isClosed();
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return conn.getMetaData();
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        conn.setReadOnly(readOnly);
    }
  
    public boolean isReadOnly() throws SQLException {
        return conn.isReadOnly();
    }

    public void setCatalog(String catalog) throws SQLException {
        conn.setCatalog(catalog);
    }

    public String getCatalog() throws SQLException {
        return conn.getCatalog();
    }

    public void setTransactionIsolation(int level) throws SQLException {
        conn.setTransactionIsolation(level);
    }

    public int getTransactionIsolation() throws SQLException {
        return conn.getTransactionIsolation();
    }

    public SQLWarning getWarnings() throws SQLException {
        return conn.getWarnings();
    }

    public void clearWarnings() throws SQLException {
        conn.clearWarnings();
    }

    
    
    
    
// --------------------------------------------------    
    
    
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {

		return conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return conn.createStatement(resultSetType, resultSetConcurrency);
	}

	public int getHoldability() throws SQLException {
		return conn.getHoldability();
	}

	public Map getTypeMap() throws SQLException {
		return conn.getTypeMap();
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		return conn.prepareStatement(sql, autoGeneratedKeys);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		return conn.prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		return conn.prepareStatement(sql, columnNames);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		conn.releaseSavepoint(savepoint);		
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		conn.rollback(savepoint);		
	}

	public void setHoldability(int holdability) throws SQLException {
		conn.setHoldability(holdability);		
	}

	public Savepoint setSavepoint() throws SQLException {
		return conn.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return conn.setSavepoint(name);
	}

	public void setTypeMap(Map arg0) throws SQLException {
		conn.setTypeMap(arg0);		
	}
}
