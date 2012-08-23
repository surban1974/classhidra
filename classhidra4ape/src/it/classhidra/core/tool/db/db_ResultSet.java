package it.classhidra.core.tool.db;

import it.classhidra.core.tool.util.util_container;
import it.classhidra.core.tool.util.util_format;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class db_ResultSet {
	public static String CONST_RS_LOCAL_CONTAINER="CONST_RS_LOCAL_CONTAINER";
	private ResultSet instance = null;

	public class entity_ResultSet {
		private String entity_id;
		private String point;
		private String time;
		private ResultSet entity;

		public entity_ResultSet(ResultSet _instance){
			super();
			entity = _instance;
			entity_id = Integer.valueOf(instance.hashCode()).toString();
			time = util_format.dataToString(new java.util.Date(), "yyyyMMddHHmmssssss");
			point = "???";
			try{
				StackTraceElement ste = (StackTraceElement)new Exception().getStackTrace()[3];
				point = ste.getClassName()+":"+ste.getMethodName();
			}catch(Exception e){

			}catch(Throwable t){

			}
		}
		public void close(){
			try{
				entity.close();
			}catch(Exception e){
			}catch(Throwable t){
			}
		}
		public String getEntity_id() {
			return entity_id;
		}
		public String getPoint() {
			return point;
		}
		public ResultSet getEntity() {
			return entity;
		}
		public String getTime() {
			return time;
		}
		public String toString(){
			String result="";
			result+="<entity id=\""+entity_id+"\" point=\""+point+"\""+" time=\""+time+"\"/>";
			return result;
		}
	}

public ResultSet getInstance() {
		return instance;
	}

public void addToRegister(){
	HashMap rs_l_c = (HashMap)util_container.getContentAsObject(CONST_RS_LOCAL_CONTAINER);
	if(rs_l_c==null) rs_l_c=new HashMap();
	entity_ResultSet ers = new entity_ResultSet(instance);
		rs_l_c.put(ers.getEntity_id(),ers);
	util_container.setContentAsObject(CONST_RS_LOCAL_CONTAINER, rs_l_c);
}

public void removeFromRegister(){
	HashMap rs_l_c = (HashMap)util_container.getContentAsObject(CONST_RS_LOCAL_CONTAINER);
	if(rs_l_c==null) rs_l_c=new HashMap();
	rs_l_c.remove(Integer.valueOf(instance.hashCode()).toString());
	util_container.setContentAsObject(CONST_RS_LOCAL_CONTAINER, rs_l_c);
}

public 	db_ResultSet(ResultSet rs){
	super();
	instance = rs;
	addToRegister();
}

public boolean next() throws SQLException{
	return instance.next();
}

public void close() throws SQLException{
	instance.close();
	removeFromRegister();
}

public boolean wasNull() throws SQLException{
	return instance.wasNull();
}

public String getString(int paramInt) throws SQLException{
	return instance.getString(paramInt);
}

public boolean getBoolean(int paramInt) throws SQLException{
	return instance.getBoolean(paramInt);
}

public byte getByte(int paramInt) throws SQLException{
	return instance.getByte(paramInt);
}

public short getShort(int paramInt) throws SQLException{
	return instance.getShort(paramInt);
}

public int getInt(int paramInt) throws SQLException{
	return instance.getInt(paramInt);
}

public long getLong(int paramInt) throws SQLException{
	return instance.getLong(paramInt);
}

public float getFloat(int paramInt) throws SQLException{
	return instance.getFloat(paramInt);
}

public double getDouble(int paramInt) throws SQLException{
	return instance.getDouble(paramInt);
}



public byte[] getBytes(int paramInt) throws SQLException{
	return instance.getBytes(paramInt);
}

public Date getDate(int paramInt) throws SQLException{
	return instance.getDate(paramInt);
}

public Time getTime(int paramInt) throws SQLException{
	return instance.getTime(paramInt);
}

public Timestamp getTimestamp(int paramInt) throws SQLException{
	return instance.getTimestamp(paramInt);
}

public InputStream getAsciiStream(int paramInt) throws SQLException{
	return instance.getAsciiStream(paramInt);
}

public InputStream getBinaryStream(int paramInt) throws SQLException{
	return instance.getBinaryStream(paramInt);
}

public String getString(String paramString) throws SQLException{
	return instance.getString(paramString);
}

public boolean getBoolean(String paramString) throws SQLException{
	return instance.getBoolean(paramString);
}

public byte getByte(String paramString) throws SQLException{
	return instance.getByte(paramString);
}

public short getShort(String paramString) throws SQLException{
	return instance.getShort(paramString);
}

public int getInt(String paramString) throws SQLException{
	return instance.getInt(paramString);
}

public long getLong(String paramString) throws SQLException{
	return instance.getLong(paramString);
}

public float getFloat(String paramString) throws SQLException{
	return instance.getFloat(paramString);
}

public double getDouble(String paramString) throws SQLException{
	return instance.getDouble(paramString);
}



public byte[] getBytes(String paramString) throws SQLException{
	return instance.getBytes(paramString);
}

public Date getDate(String paramString) throws SQLException{
	return instance.getDate(paramString);
}

public Time getTime(String paramString) throws SQLException{
	return instance.getTime(paramString);
}

public Timestamp getTimestamp(String paramString) throws SQLException{
	return instance.getTimestamp(paramString);
}

public InputStream getAsciiStream(String paramString) throws SQLException{
	return instance.getAsciiStream(paramString);
}

public InputStream getBinaryStream(String paramString) throws SQLException{
	return instance.getBinaryStream(paramString);
}

public SQLWarning getWarnings() throws SQLException{
	return instance.getWarnings();
}

public void clearWarnings() throws SQLException{
	instance.clearWarnings();
}

public String getCursorName() throws SQLException{
	return instance.getCursorName();
}

public ResultSetMetaData getMetaData() throws SQLException{
	return instance.getMetaData();
}

public Object getObject(int paramInt) throws SQLException{
	return instance.getObject(paramInt);
}

public Object getObject(String paramString) throws SQLException{
	return instance.getObject(paramString);
}

public int findColumn(String paramString) throws SQLException{
	return instance.findColumn(paramString);
}

public Reader getCharacterStream(int paramInt) throws SQLException{
	return instance.getCharacterStream(paramInt);
}

public Reader getCharacterStream(String paramString) throws SQLException{
	return instance.getCharacterStream(paramString);
}

public BigDecimal getBigDecimal(int paramInt) throws SQLException{
	return instance.getBigDecimal(paramInt);
}

public BigDecimal getBigDecimal(String paramString) throws SQLException{
	return instance.getBigDecimal(paramString);
}

public boolean isBeforeFirst() throws SQLException{
	return instance.isBeforeFirst();
}

public boolean isAfterLast() throws SQLException{
	return instance.isAfterLast();
}

public boolean isFirst() throws SQLException{
	return instance.isFirst();
}

public boolean isLast() throws SQLException{
	return instance.isLast();
}

public void beforeFirst() throws SQLException{
	instance.beforeFirst();
}

public void afterLast() throws SQLException{
	instance.afterLast();
}

public boolean first() throws SQLException{
	return instance.first();
}

public boolean last() throws SQLException{
	return instance.last();
}

public int getRow() throws SQLException{
	return instance.getRow();
}

public boolean absolute(int paramInt) throws SQLException{
	return instance.absolute(paramInt);
}

public boolean relative(int paramInt) throws SQLException{
	return instance.relative(paramInt);
}

public boolean previous() throws SQLException{
	return instance.previous();
}

public void setFetchDirection(int paramInt) throws SQLException{
	instance.setFetchDirection(paramInt);
}

public int getFetchDirection() throws SQLException{
	return instance.getFetchDirection();
}

public void setFetchSize(int paramInt) throws SQLException{
	instance.setFetchSize(paramInt);
}

public int getFetchSize() throws SQLException{
	return instance.getFetchSize();
}

public int getType() throws SQLException{
	return instance.getType();
}

public int getConcurrency() throws SQLException{
	return instance.getConcurrency();
}

public boolean rowUpdated() throws SQLException{
	return instance.rowUpdated();
}

public boolean rowInserted() throws SQLException{
	return instance.rowInserted();
}

public boolean rowDeleted() throws SQLException{
	return instance.rowDeleted();
}

public void updateNull(int paramInt) throws SQLException{
	instance.updateNull(paramInt);
}

public void updateBoolean(int paramInt, boolean paramBoolean) throws SQLException{
	instance.updateBoolean(paramInt, paramBoolean);
}

public void updateByte(int paramInt, byte paramByte) throws SQLException{
	instance.updateByte(paramInt, paramByte);
}

public void updateShort(int paramInt, short paramShort) throws SQLException{
	instance.updateShort(paramInt, paramShort);
}

public void updateInt(int paramInt1, int paramInt2) throws SQLException{
	instance.updateInt(paramInt1, paramInt2);
}

public void updateLong(int paramInt, long paramLong) throws SQLException{
	instance.updateLong(paramInt, paramLong);
}

public void updateFloat(int paramInt, float paramFloat) throws SQLException{
	instance.updateFloat(paramInt, paramFloat);
}

public void updateDouble(int paramInt, double paramDouble) throws SQLException{
	instance.updateDouble(paramInt, paramDouble);
}

public void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException{
	instance.updateBigDecimal(paramInt, paramBigDecimal);
}

public void updateString(int paramInt, String paramString) throws SQLException{
	instance.updateString(paramInt, paramString);
}

public void updateBytes(int paramInt, byte[] paramArrayOfByte) throws SQLException{
	instance.updateBytes(paramInt, paramArrayOfByte);
}

public void updateDate(int paramInt, Date paramDate) throws SQLException{
	instance.updateDate(paramInt, paramDate);
}

public void updateTime(int paramInt, Time paramTime) throws SQLException{
	instance.updateTime(paramInt, paramTime);
}

public void updateTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException{
	instance.updateTimestamp(paramInt, paramTimestamp);
}

public void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException{
	instance.updateAsciiStream(paramInt1, paramInputStream, paramInt2);
}

public void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException{
	instance.updateBinaryStream(paramInt1, paramInputStream, paramInt2);
}

public void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException{
	instance.updateCharacterStream(paramInt1, paramReader, paramInt2);
}

public void updateObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException{
	instance.updateObject(paramInt1, paramObject,paramInt2);
}

public void updateObject(int paramInt, Object paramObject) throws SQLException{
	instance.updateObject(paramInt, paramObject);
}

public void updateNull(String paramString) throws SQLException{
	instance.updateNull(paramString);
}

public void updateBoolean(String paramString, boolean paramBoolean) throws SQLException{
	instance.updateBoolean(paramString, paramBoolean);
}

public void updateByte(String paramString, byte paramByte) throws SQLException{
	instance.updateByte(paramString, paramByte);
}

public void updateShort(String paramString, short paramShort) throws SQLException{
	instance.updateShort(paramString, paramShort);
}

public void updateInt(String paramString, int paramInt) throws SQLException{
	instance.updateInt(paramString, paramInt);
}

public void updateLong(String paramString, long paramLong) throws SQLException{
	instance.updateLong(paramString, paramLong);
}

public void updateFloat(String paramString, float paramFloat) throws SQLException{
	instance.updateFloat(paramString, paramFloat);
}

public void updateDouble(String paramString, double paramDouble) throws SQLException{
	instance.updateDouble(paramString, paramDouble);
}

public void updateBigDecimal(String paramString, BigDecimal paramBigDecimal) throws SQLException{
	instance.updateBigDecimal(paramString, paramBigDecimal);
}

public void updateString(String paramString1, String paramString2) throws SQLException{
	instance.updateString(paramString1, paramString2);
}

public void updateBytes(String paramString, byte[] paramArrayOfByte) throws SQLException{
	instance.updateBytes(paramString, paramArrayOfByte);
}

public void updateDate(String paramString, Date paramDate) throws SQLException{
	instance.updateDate(paramString, paramDate);
}

public void updateTime(String paramString, Time paramTime) throws SQLException{
	instance.updateTime(paramString, paramTime);
}

public void updateTimestamp(String paramString, Timestamp paramTimestamp) throws SQLException{
	instance.updateTimestamp(paramString, paramTimestamp);
}

public void updateAsciiStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException{
	instance.updateAsciiStream(paramString, paramInputStream, paramInt);
}

public void updateBinaryStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException{
	instance.updateBinaryStream(paramString, paramInputStream, paramInt);
}

public void updateCharacterStream(String paramString, Reader paramReader, int paramInt) throws SQLException{
	instance.updateCharacterStream(paramString, paramReader, paramInt);
}

public void updateObject(String paramString, Object paramObject, int paramInt) throws SQLException{
	instance.updateObject(paramString, paramObject,paramInt);
}

public void updateObject(String paramString, Object paramObject) throws SQLException{
	instance.updateObject(paramString, paramObject);
}

public void insertRow() throws SQLException{
	instance.insertRow();
}

public void updateRow() throws SQLException{
	instance.updateRow();
}

public void deleteRow() throws SQLException{
	instance.deleteRow();
}

public void refreshRow() throws SQLException{
	instance.refreshRow();
}

public void cancelRowUpdates() throws SQLException{
	instance.cancelRowUpdates();
}

public void moveToInsertRow() throws SQLException{
	instance.moveToInsertRow();
}

public void moveToCurrentRow() throws SQLException{
	instance.moveToCurrentRow();
}

public Statement getStatement() throws SQLException{
	return instance.getStatement();
}

public Object getObject(int paramInt, Map paramMap) throws SQLException{
	return instance.getObject(paramInt, paramMap);
}

public Ref getRef(int paramInt) throws SQLException{
	return instance.getRef(paramInt);
}

public Blob getBlob(int paramInt) throws SQLException{
	return instance.getBlob(paramInt);
}

public Clob getClob(int paramInt) throws SQLException{
	return instance.getClob(paramInt);
}

public Array getArray(int paramInt) throws SQLException{
	return instance.getArray(paramInt);
}

public Object getObject(String paramString, Map paramMap) throws SQLException{
	return instance.getObject(paramString,paramMap);
}

public Ref getRef(String paramString) throws SQLException{
	return instance.getRef(paramString);
}

public Blob getBlob(String paramString) throws SQLException{
	return instance.getBlob(paramString);
}

public Clob getClob(String paramString) throws SQLException{
	return instance.getClob(paramString);
}

public Array getArray(String paramString) throws SQLException{
	return instance.getArray(paramString);
}

public Date getDate(int paramInt, Calendar paramCalendar) throws SQLException{
	return instance.getDate(paramInt, paramCalendar);
}

public Date getDate(String paramString, Calendar paramCalendar) throws SQLException{
	return instance.getDate(paramString, paramCalendar);
}

public Time getTime(int paramInt, Calendar paramCalendar) throws SQLException{
	return instance.getTime(paramInt, paramCalendar);
}

public Time getTime(String paramString, Calendar paramCalendar) throws SQLException{
	return instance.getTime(paramString, paramCalendar);
}

public Timestamp getTimestamp(int paramInt, Calendar paramCalendar) throws SQLException{
	return instance.getTimestamp(paramInt, paramCalendar);
}

public Timestamp getTimestamp(String paramString, Calendar paramCalendar) throws SQLException{
	return instance.getTimestamp(paramString, paramCalendar);
}

public URL getURL(int paramInt) throws SQLException{
	return instance.getURL(paramInt);
}

public URL getURL(String paramString)throws SQLException{
	return instance.getURL(paramString);
}

public void updateRef(int paramInt, Ref paramRef)throws SQLException{
	instance.updateRef(paramInt, paramRef);
}

public void updateRef(String paramString, Ref paramRef)throws SQLException{
	instance.updateRef(paramString, paramRef);
}

public void updateBlob(int paramInt, Blob paramBlob)throws SQLException{
	instance.updateBlob(paramInt, paramBlob);
}

public void updateBlob(String paramString, Blob paramBlob)throws SQLException{
	instance.updateBlob(paramString, paramBlob);
}

public void updateClob(int paramInt, Clob paramClob)throws SQLException{
	instance.updateClob(paramInt, paramClob);
}

public void updateClob(String paramString, Clob paramClob)throws SQLException{
	instance.updateClob(paramString, paramClob);
}

public void updateArray(int paramInt, Array paramArray)throws SQLException{
	instance.updateArray(paramInt, paramArray);
}

public void updateArray(String paramString, Array paramArray)throws SQLException{
	instance.updateArray(paramString, paramArray);
}

}
