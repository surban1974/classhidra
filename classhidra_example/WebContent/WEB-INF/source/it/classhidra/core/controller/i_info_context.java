package it.classhidra.core.controller;

import java.io.Serializable;

public interface i_info_context extends Serializable{

	void reInitEjb(info_context ic);

	boolean isScoped();

	boolean isOnlyProxied();

	String toString();

	boolean isStateless();

	void setStateless(boolean stateless);

	boolean isStateful();

	void setStateful(boolean stateful);

	boolean isSingleton();

	void setSingleton(boolean singleton);

	boolean isMessageDriven();

	void setMessageDriven(boolean messageDriven);

	boolean isSessionScoped();

	void setSessionScoped(boolean sessionScoped);

	boolean isApplicationScoped();

	void setApplicationScoped(boolean applicationScoped);

	boolean isRequestScoped();

	void setRequestScoped(boolean requestScoped);

	boolean isNamed();

	void setNamed(boolean named);

	boolean isProxiedEjb();

	void setProxiedEjb(boolean proxiedEjb);

	boolean isProxiedCdi();

	void setProxiedCdi(boolean proxiedCdi);

	Class getOwnerClass();

	boolean isLocal();

	void setLocal(boolean local);

	boolean isRemote();

	void setRemote(boolean remote);

	String getName();

	void setName(String name);

	String getDescription();

	void setDescription(String description);

	String getMappedName();

	void setMappedName(String mappedName);

	Class[] getValue();

	void setValue(Class[] value);

	boolean isLocalBean();

	void setLocalBean(boolean localBean);

	boolean isTransactionManagementBean();

	void setTransactionManagementBean(boolean transactionManagementBean);

	boolean isStartup();

	void setStartup(boolean startup);

	Class getProxiedClass();

	void setProxiedClass(Class proxiedClass);

	int getProxiedId();

	void setProxiedId(int proxiedId);

	Object getProxy();

	void setProxy(Object proxy);

}