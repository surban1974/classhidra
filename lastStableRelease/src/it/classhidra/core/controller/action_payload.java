package it.classhidra.core.controller;

import java.io.Serializable;

public class action_payload implements Serializable {
	private static final long serialVersionUID = 1L;

	private i_action action;
	private redirects redirect;
	
	public action_payload(i_action action, redirects redirect) {
		super();
		this.action = action;
		this.redirect = redirect;
	}
	
	public i_action getAction() {
		return action;
	}
	public void setAction(i_action action) {
		this.action = action;
	}
	public redirects getRedirect() {
		return redirect;
	}
	public void setRedirect(redirects redirect) {
		this.redirect = redirect;
	}
}
