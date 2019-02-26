package it.classhidra.core.tool.log.statistic;

import java.io.Serializable;
import java.util.List;

public interface I_StatisticProvider extends Serializable{
	void addStatictic(StatisticEntity stat);
	
	List getAllEntities();
	String getAllEntitiesAsXml();
	
	List getLastEntities(int quantity);
	String getLastEntitiesAsXml(int quantity);
	
	List getEntities(String commandDefinedIntoProviderRelise);
	String getEntitiesAsXml(String commandDefinedIntoProviderRelise);
	

	void clearAll();
	void syncroAll();
}
