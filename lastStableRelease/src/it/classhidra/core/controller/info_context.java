package it.classhidra.core.controller;

import java.io.Serializable;

public class info_context implements Serializable, i_info_context{
	private static final long serialVersionUID = 1L;
	private boolean stateless=false;
	private boolean stateful=false;
	private boolean singleton=false;
	private boolean messageDriven=false;

	private boolean sessionScoped=false;
	private boolean applicationScoped=false;
	private boolean requestScoped=false;
	
	private boolean named=false;
	
	private boolean proxiedEjb=false;
	private boolean proxiedCdi=false;
	private boolean proxiedContext=false;
	private int proxiedId=-1;
	
	private boolean local=false;
	private boolean remote=false;
	private boolean localBean=false;
	private boolean transactionManagementBean=false;
	private boolean startup=false;
	private String name;
	private String description;
	private String mappedName;
	
	private Class proxiedClass;
	private Class ownerClass;
	private Class[] value;
	private Object proxy;
	
	
	public info_context() {
		
	}
	
	public info_context(Class _ownerClass) {
		if(_ownerClass!=null) 
			this.ownerClass=_ownerClass;
	}	
	
	public void reInitEjb(info_context ic){
		if(ic==null) return;
		if(ic.isProxiedEjb())
			this.proxiedEjb=true;
		if(ic.isStateless())
			this.stateless=true;
		if(ic.isStateful())
			this.stateful=true;
		if(ic.isSingleton())
			this.singleton=true;
	}
	
	public boolean isScoped(){
		return proxiedCdi && (sessionScoped || applicationScoped || requestScoped);
	}
	
	public boolean isOnlyProxied(){
		if(proxiedEjb && (stateful || singleton))
			return true;
		if(proxiedContext && (stateful || singleton))
			return true;
		return false;
	}
	

	public String toString(){
		String result = ""+System.getProperty("line.separator");
		if(proxiedId>-1)
			result+="proxiedId: "+proxiedId+System.getProperty("line.separator");
		if(proxiedClass!=null)
			result+="proxied: "+proxiedClass+System.getProperty("line.separator");		
		if(proxy!=null)
			result+="proxy: present"+System.getProperty("line.separator");			
		if(ownerClass!=null)
			result+="owner: "+ownerClass+System.getProperty("line.separator");
		if(proxiedContext)
			result+="proxiedContext: "+proxiedContext+System.getProperty("line.separator");
		if(proxiedCdi)
			result+="proxiedCdi: "+proxiedCdi+System.getProperty("line.separator");
		if(named)
			result+="	named: "+named+System.getProperty("line.separator");
		if(requestScoped)
			result+="	requestScoped: "+requestScoped+System.getProperty("line.separator");
		if(sessionScoped)
			result+="	sessionScoped: "+sessionScoped+System.getProperty("line.separator");
		if(applicationScoped)
			result+="	applicationScoped: "+applicationScoped+System.getProperty("line.separator");
		if(proxiedEjb)
			result+="proxiedEjb: "+proxiedEjb+System.getProperty("line.separator");
		if(stateless)
			result+="	@Stateless: "+stateless+System.getProperty("line.separator");
		if(stateful)
			result+="	@Stateful: "+stateful+System.getProperty("line.separator");
		if(messageDriven)
			result+="	@MessageDriven: "+messageDriven+System.getProperty("line.separator");
		if(singleton)
			result+="	@Singleton: "+singleton+System.getProperty("line.separator");
		if(startup)
			result+="	@Startup: "+startup+System.getProperty("line.separator");
		if(name!=null && !name.equals(""))
			result+="	name: "+name+System.getProperty("line.separator");
		if(description!=null && !description.equals(""))
			result+="	description: "+description+System.getProperty("line.separator");
		if(mappedName!=null && !mappedName.equals(""))
			result+="	mappedName: "+mappedName+System.getProperty("line.separator");
		if(local)
			result+="	@Local: "+local+System.getProperty("line.separator");
		if(remote)
			result+="	@Remote: "+remote+System.getProperty("line.separator");
		if(value!=null && value.length>0){
			result+="	value: ";
			for(Class clazz: value)
				result+=clazz.getName()+";";			
		}
		if(localBean)
			result+="	@LocalBean: "+localBean+System.getProperty("line.separator");
		if(transactionManagementBean)
			result+="	TransactionManagementType.BEAN: "+transactionManagementBean+System.getProperty("line.separator");
		
		return result;
	}	
	
	public boolean isStateless() {
		return stateless;
	}
	public void setStateless(boolean stateless) {
		this.stateless = stateless;
	}
	public boolean isStateful() {
		return stateful;
	}
	public void setStateful(boolean stateful) {
		this.stateful = stateful;
	}
	public boolean isSingleton() {
		return singleton;
	}
	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}
	public boolean isMessageDriven() {
		return messageDriven;
	}
	public void setMessageDriven(boolean messageDriven) {
		this.messageDriven = messageDriven;
	}
	public boolean isSessionScoped() {
		return sessionScoped;
	}
	public void setSessionScoped(boolean sessionScoped) {
		this.sessionScoped = sessionScoped;
	}
	public boolean isApplicationScoped() {
		return applicationScoped;
	}
	public void setApplicationScoped(boolean applicationScoped) {
		this.applicationScoped = applicationScoped;
	}
	public boolean isRequestScoped() {
		return requestScoped;
	}
	public void setRequestScoped(boolean requestScoped) {
		this.requestScoped = requestScoped;
	}
	public boolean isNamed() {
		return named;
	}
	public void setNamed(boolean named) {
		this.named = named;
	}
	public boolean isProxiedEjb() {
		return proxiedEjb;
	}
	public void setProxiedEjb(boolean proxiedEjb) {
		this.proxiedEjb = proxiedEjb;
	}
	public boolean isProxiedCdi() {
		return proxiedCdi;
	}
	public void setProxiedCdi(boolean proxiedCdi) {
		this.proxiedCdi = proxiedCdi;
	}

	public Class getOwnerClass() {
		return ownerClass;
	}

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public boolean isRemote() {
		return remote;
	}

	public void setRemote(boolean remote) {
		this.remote = remote;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMappedName() {
		return mappedName;
	}

	public void setMappedName(String mappedName) {
		this.mappedName = mappedName;
	}

	public Class[] getValue() {
		return value;
	}

	public void setValue(Class[] value) {
		this.value = value;
	}
	
	public boolean isLocalBean() {
		return localBean;
	}

	public void setLocalBean(boolean localBean) {
		this.localBean = localBean;
	}

	public boolean isTransactionManagementBean() {
		return transactionManagementBean;
	}

	public void setTransactionManagementBean(boolean transactionManagementBean) {
		this.transactionManagementBean = transactionManagementBean;
	}

	public boolean isStartup() {
		return startup;
	}

	public void setStartup(boolean startup) {
		this.startup = startup;
	}

	public Class getProxiedClass() {
		return proxiedClass;
	}

	public void setProxiedClass(Class proxiedClass) {
		this.proxiedClass = proxiedClass;
	}

	public int getProxiedId() {
		return proxiedId;
	}

	public void setProxiedId(int proxiedId) {
		this.proxiedId = proxiedId;
	}

	public Object getProxy() {
		return proxy;
	}

	public void setProxy(Object proxy) {
		this.proxy = proxy;
	}

	public boolean isProxiedContext() {
		return proxiedContext;
	}

	public void setProxiedContext(boolean proxiedContext) {
		this.proxiedContext = proxiedContext;
	}

}
