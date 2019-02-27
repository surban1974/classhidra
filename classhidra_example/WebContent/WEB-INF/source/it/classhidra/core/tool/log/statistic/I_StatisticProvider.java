package it.classhidra.core.tool.log.statistic;

import java.io.Serializable;
import java.util.List;

public interface I_StatisticProvider extends Serializable{
	void addStatictic(StatisticEntity stat);
	
	List<StatisticEntity> getAllEntities();
	String getAllEntitiesAsXml();
	
	List<StatisticEntity> getLastEntities(int quantity);
	String getLastEntitiesAsXml(int quantity);
	
	List<StatisticEntity> getEntities(String commandDefinedIntoProviderRelise);
	String getEntitiesAsXml(String commandDefinedIntoProviderRelise);
	

	void clearAll();
	void syncroAll();
}
