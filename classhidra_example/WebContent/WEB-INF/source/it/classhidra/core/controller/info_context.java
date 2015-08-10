package it.classhidra.core.controller;

import java.io.Serializable;

public class info_context implements Serializable{
	private static final long serialVersionUID = 1L;
	private boolean stateless=false;
	private boolean stateful=false;
	private boolean singleton=false;
	private boolean messageDriven=false;

	private boolean sessionScoped=false;
	private boolean applicationScoped=false;
	private boolean requestScoped=false;
	
	private boolean named=false;
	
	private boolean proxedEjb=false;
	private boolean proxedCdi=false;
	
	public info_context() {
		
	}
	
	public void reInitEjb(info_context ic){
		if(ic==null) return;
		if(ic.isProxedEjb())
			this.proxedEjb=true;
		if(ic.isStateless())
			this.stateless=true;
		if(ic.isStateful())
			this.stateful=true;
		if(ic.isSingleton())
			this.singleton=true;
	}
	
	public boolean isScoped(){
		return proxedCdi && (sessionScoped || applicationScoped || requestScoped);
	}
	
	public boolean isOnlyProxed(){
		return proxedEjb && (stateful || singleton);
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
	public boolean isProxedEjb() {
		return proxedEjb;
	}
	public void setProxedEjb(boolean proxedEjb) {
		this.proxedEjb = proxedEjb;
	}
	public boolean isProxedCdi() {
		return proxedCdi;
	}
	public void setProxedCdi(boolean proxedCdi) {
		this.proxedCdi = proxedCdi;
	}

}
