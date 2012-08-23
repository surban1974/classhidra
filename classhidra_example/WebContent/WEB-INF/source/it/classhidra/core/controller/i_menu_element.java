package it.classhidra.core.controller;

import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;

public interface i_menu_element extends i_elementBase{

	public abstract void reimposta();

	public abstract void authentication_clear(HashMap forbiden);

	public abstract void init(Node node) throws bsControllerException;

	public abstract i_menu_element find(String id);

	public abstract int calculate_potential_elements();

	public abstract void analyse_potential_group(boolean all);

	public abstract void hideDynamicElements();

	public abstract String generateHTML(HttpServletRequest request);

	public abstract void setVisibilityChildren(boolean vis);

	public abstract void setVisibilityAllChildren(boolean vis);

	public abstract Vector getChildren();

	public abstract HashMap getChildren_info();

	public abstract info_menu_element getInfo_menu();

	public abstract i_menu_element getParent();

	public abstract void setParent(i_menu_element menu_element);

	public abstract int getLevel();

	public abstract String toString();

	public abstract boolean isVisible();

	public abstract void setVisible(boolean b);

	public abstract boolean isNext();

	public abstract boolean isPrev();

	public abstract void setNext(boolean b);

	public abstract void setPrev(boolean b);

	public abstract int getPotential_elements();

	public abstract void setPotential_elements(int i);

	public abstract void setChildren(Vector children);

	public abstract void setChildren_info(HashMap children_info);

	public abstract void setInfo_menu(info_menu_element info_menu);

	public abstract void setLevel(int level);

}