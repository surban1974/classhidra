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
	
	public info_context() {
		
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

}
