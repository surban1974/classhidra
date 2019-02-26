
package it.classhidra.framework.web.beans;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_menu_element;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.controller.info_menu_element;
import it.classhidra.core.tool.elements.elementBase;
import it.classhidra.core.tool.elements.i_elementBase;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.util.util_format;

public class menu_element extends elementBase implements i_elementBase, i_menu_element{

	private static final long serialVersionUID = 7850850667463869485L;
	private i_menu_element parent;
	private Vector<i_menu_element> children;
	private HashMap<String,i_menu_element> children_info;
	private info_menu_element info_menu;
	private boolean visible;
	private boolean next;
	private boolean prev;
	private int level;
	private int potential_elements;

	public menu_element(i_menu_element ime){
		super();
		reimposta();
		this.setParent(ime.getParent());
		this.setChildren(ime.getChildren());
		this.setChildren_info(ime.getChildren_info());
		this.setInfo_menu(ime.getInfo_menu());
		this.setVisible(ime.isVisible());
		this.setNext(ime.isNext());
		this.setPrev(ime.isPrev());
		this.setLevel(ime.getLevel());
		this.setPotential_elements(ime.getPotential_elements());
	}

	public menu_element() {
		super();
		reimposta();
	}

	public void reimposta(){
		children = new Vector<i_menu_element>();
		children_info = new HashMap<String, i_menu_element>();
		visible = false;
		next = false;
		prev = false;
		parent = null;
	}


	public void authentication_clear(HashMap<String,info_action> forbiden){
		Vector<i_menu_element> buf=new Vector<i_menu_element>();
		for(int i=0;i<children.size();i++){
			i_menu_element founded = (i_menu_element)children.get(i);
			String key = founded.getInfo_menu().getAction();
			if(!bsController.getAppInit().get_extention_do().trim().equals("")){
				if(key!=null && key.indexOf(bsController.getAppInit().get_extention_do())>0){
					key = key.substring(0,key.indexOf(bsController.getAppInit().get_extention_do()));
				}
			}
			if(forbiden.get(key)==null){
				if(key.lastIndexOf("\"")>-1) key=key.substring(key.lastIndexOf("\"")+1,key.length());
				if(key.lastIndexOf("'")>-1) key=key.substring(key.lastIndexOf("\"")+1,key.length());
				if(key.lastIndexOf("(")>-1) key=key.substring(key.lastIndexOf("\"")+1,key.length());
				if(key.lastIndexOf(" ")>-1) key=key.substring(key.lastIndexOf("\"")+1,key.length());
			}
			if(forbiden.get(key)==null){
				String key_auth = founded.getInfo_menu().getAuth_action();
				if(key_auth!=null && key_auth.trim().length()>0)
					key=key_auth;
			}

			if(forbiden.get(key)==null){
				buf.add(founded);
				founded.authentication_clear(forbiden);
			}
		}
		children.clear();
		children_info.clear();
		for(int i=0;i<buf.size();i++){
			i_menu_element founded = (i_menu_element)buf.get(i);
			children.add(founded);
			children_info.put(founded.getInfo_menu().getId(),founded);
		}

	}


	public void init(Node node) throws bsControllerException{
		level = nodeLevel();
		if(node==null) return;
		if(node.getNodeType()!= Node.ELEMENT_NODE) return;
		info_menu = new info_menu_element();
		info_menu.init(node);
		NodeList list = node.getChildNodes();
		Vector<i_menu_element> tmp = new Vector<i_menu_element>();
		for(int i=0;i<list.getLength();i++){
			Node child_node = list.item(i);
			if(child_node.getNodeType()== Node.ELEMENT_NODE){
				i_menu_element child_element = new menu_element();
				child_element.setParent(this);
				
				child_element.init(child_node);
				child_element.getInfo_menu().setParent(info_menu);
				children.add(child_element);
				info_menu.getChildren().add(child_element.getInfo_menu());
				children_info.put(child_element.getInfo_menu().getId(),child_element);
				if(	child_element.getInfo_menu().getType().equals("static")) tmp.add(child_element);
			}
		}
		for(int i=0;i<tmp.size();i++){
			i_menu_element child_element = (i_menu_element)tmp.get(i);
			if(i>0){
				child_element.setPrev(true);
				((i_menu_element)children.get(i-1)).setNext(true);
			}
		}
	}
	private int nodeLevel(){
		int result=0;
		try{
			i_menu_element parent = getParent();
			while(parent!=null){
				parent=parent.getParent();
				result++;
			}
		}catch(Exception e){
		}
		return result;

	}


	public i_menu_element find(String id){
		if(this.getInfo_menu().getId().equals(id)) return this;
		if(children_info.get(id)!=null) return (menu_element)children_info.get(id);
		for(int i=0;i<children.size();i++){
			i_menu_element founded = ((i_menu_element)children.get(i)).find(id);
			if(founded!=null) return founded;
		}
		return null;
	}


	public int calculate_potential_elements(){
		if(getInfo_menu().getAction().equals("menuCreator")){
			int loc_potential_elements=0;
			for(int i=0;i<children.size();i++){
				if(((i_menu_element)children.get(i)).getInfo_menu().getAction().equals("menuCreator")){
					loc_potential_elements+=((i_menu_element)children.get(i)).calculate_potential_elements();
				}else{
					if(	((i_menu_element)children.get(i)).getInfo_menu().getType().equals("static") ||
						((i_menu_element)children.get(i)).getInfo_menu().getType().equals("dynamicOn")
					) loc_potential_elements++;
				}
			}
			this.potential_elements=loc_potential_elements;
		}

		return this.potential_elements;
	}


	public void analyse_potential_group(boolean all){
		Vector<i_menu_element> tmp = new Vector<i_menu_element>();
		for(int i=0;i<children.size();i++){
			i_menu_element child_element = (i_menu_element)children.get(i);
			child_element.setNext(false);
			child_element.setPrev(false);
			if(	(child_element.getInfo_menu().getAction().equals("menuCreator") && child_element.getPotential_elements()==0) ||
				(child_element.getInfo_menu().getType().equals("dynamic") )
				){
				if(child_element.getInfo_menu().getAction().equals("menuCreator") && child_element.getPotential_elements()==0) 
					child_element.setVisible(false);
			}else tmp.add(child_element);
			if(all && child_element.getInfo_menu().getAction().equals("menuCreator")){
				child_element.analyse_potential_group(all);
			}
		}
		for(int i=0;i<tmp.size();i++){
			i_menu_element child_element = (i_menu_element)tmp.get(i);
			
			if(i>0) child_element.setPrev(true);
			if(i<tmp.size()-1) child_element.setNext(true);
	
		}
	}

	public void analyse_potential_group_only(){
		Vector<i_menu_element> tmp = new Vector<i_menu_element>();
		for(int i=0;i<children.size();i++){
			i_menu_element child_element = (i_menu_element)children.get(i);
			if(	(child_element.getInfo_menu().getAction().equals("menuCreator") && child_element.getPotential_elements()==0) ||
				child_element.getInfo_menu().getType().equals("dynamic")
				){
			}else tmp.add(child_element);
		}
		for(int i=0;i<tmp.size();i++){
			i_menu_element child_element = (i_menu_element)tmp.get(i);
			if(i>0){
				child_element.setPrev(true);
				((i_menu_element)children.get(i-1)).setNext(true);
			}
		}
	}	

	public void hideDynamicElements(){
		for(int i=0;i<children.size();i++){
			i_menu_element child_element = (i_menu_element)children.get(i);
			if(child_element.getInfo_menu().getAction().equals("menuCreator")) child_element.hideDynamicElements();
			else{
				if(child_element.getInfo_menu().getType().equals("dynamicOn")){
					child_element.getInfo_menu().setType("dynamic");
					child_element.setVisible(false);
				}
			}

		}
	}


	public String generateHTML(HttpServletRequest request){
		String result = "";
		if(	!visible ||
			(info_menu.getAction().equals("menuCreator") && potential_elements==0)
			) return result;

		boolean visibilityChildren = checkVisibilityChildren();

		result+="<div id='"+info_menu.getId()+"'><table cellspacing='0' cellpadding='0' width='100%' border=0><tr><td height='16' width=1%><nobr>";

		int k=0;
		i_menu_element par = getParent();
		String img ="";
		while(par!=null && k<level){
//			if(par.isNext()) img="<img src='images/menu/win/line.gif' border=0>"+img;
//			else 
				img="<img src='images/menu/win/realblank.gif' border=0>"+img;
			k++;
			par = par.getParent();
		}
		result+=img;

		if(info_menu.getAction().equals("menuCreator")){
//			if(visibilityChildren) result+="<a href=\"javascript:void(actionMenuVisual('false','"+info_menu.getId()+"'))\" ><img src='images/menu/win/minus"+suffixImg()+".gif' border=0></a>";
//			else result+="<a href=\"javascript:void(actionMenuVisual('true','"+info_menu.getId()+"'))\" ><img src='images/menu/win/plus"+suffixImg()+".gif' border=0></a>";
			if(info_menu.getImg().equals("")){
				if(visibilityChildren) result+="<a href=\"javascript:void(actionMenuVisual('false','"+info_menu.getId()+"'))\" ><img src='images/menu/win/folderopen.gif' border=0></a>";
				else result+="<a href=\"javascript:void(actionMenuVisual('true','"+info_menu.getId()+"'))\" ><img src='images/menu/win/folderclosed.gif' border=0></a>";
			}else {
				if(visibilityChildren) result+="<a href=\"javascript:void(actionMenuVisual('false','"+info_menu.getId()+"'))\" ><img src='"+info_menu.getImg()+"' border=0></a>";
				else result+="<a href=\"javascript:void(actionMenuVisual('true','"+info_menu.getId()+"'))\" ><img src='"+info_menu.getImg()+"' border=0></a>";

//				result+="<img src='"+info_menu.getImg()+"' border=0>";
			}

			result+="</td><td height='32'  valign='middle'><nobr>";
//			result+="<span class='leaf'>&nbsp;"+bsController.writeLabel(request,info_menu.getMess_id(),info_menu.getDescr(),null)+"</span>";
			if(visibilityChildren){
				result+="<span class='leaf'><a href=\"javascript:void(actionMenuVisual('false','"+info_menu.getId()+"'))\" ><font color='#00BFFF'>&nbsp;<i>"+bsController.writeLabel(request,info_menu.getMess_id(),info_menu.getDescr(),null)+"</i></font></a></span>";
			}else			
				result+="<span class='leaf'><a href=\"javascript:void(actionMenuVisual('true','"+info_menu.getId()+"'))\" class='leaf'>&nbsp;"+bsController.writeLabel(request,info_menu.getMess_id(),info_menu.getDescr(),null)+"</a></span>";

		}

		else{
//			result+="<img src='images/menu/win/join"+util_format.replace(suffixImg(),"only","bottom")+".gif' border=0>";
			if(info_menu.getImg().equals("")) result+="<a href=\"javascript:void(actionContent('"+info_menu.getAction()+"','"+info_menu.getLoad()+"')) \" ><img src='images/menu/finanziamento/document.gif' border=0></a>";
			else result+="<a href=\"javascript:void(actionContent('"+info_menu.getAction()+"','"+info_menu.getLoad()+"')) \" ><img src='"+info_menu.getImg()+"' border=0></a>";
			result+="</nobr></td><td height='32'  valign='middle'><nobr>";
			result+="<span class='leaf'>&nbsp;";
			if(info_menu.getAction().indexOf("\"")>-1) 
				result+="<a href=\"javascript:void(actionContent('"+util_format.replace(info_menu.getAction(),"\"","\'")+"','"+info_menu.getLoad()+"')) \" >"+bsController.writeLabel(request,info_menu.getMess_id(),info_menu.getDescr(),null)+"</a>";
			else	
				result+="<a href=\"javascript:void(actionContent('"+info_menu.getAction()+"','"+info_menu.getLoad()+"')) \" >"+bsController.writeLabel(request,info_menu.getMess_id(),info_menu.getDescr(),null)+"</a>";
			result+="</span>";
		}

		result+="</nobr></td></tr></table>";
		if(visibilityChildren){
			for(int i=0;i<children.size();i++) result+=((i_menu_element)children.get(i)).generateHTML(request);
		}

		result+="</div>";
		return result;
	}

/*	
	private String suffixImg(){
		if(!isNext() && !isPrev()) return "only";
		if(isNext() && !isPrev()) return "";
		if(!isNext() && isPrev()) return "bottom";
		return "";
	}
*/
	private boolean checkVisibilityChildren(){
		boolean res=false;
		for(int i=0;i<children.size();i++) res=res || ((i_menu_element)children.get(i)).isVisible();
		return res;
	}


	public void setVisibilityChildren(boolean vis){
		for(int i=0;i<children.size();i++){
			if(!((i_menu_element)children.get(i)).getInfo_menu().getType().toLowerCase().equals("dynamic"))
				((i_menu_element)children.get(i)).setVisible(vis);
		}
	}


	public void setVisibilityAllChildren(boolean vis){
		if(!getInfo_menu().getType().toLowerCase().equals("dynamic"))
			this.setVisible(vis);
		for(int i=0;i<children.size();i++)
			((i_menu_element)children.get(i)).setVisibilityAllChildren(vis);
	}



	public Vector<i_menu_element> getChildren() {
		return children;
	}


	public HashMap<String,i_menu_element> getChildren_info() {
		return children_info;
	}


	public info_menu_element getInfo_menu() {
		return info_menu;
	}

	public i_menu_element getParent() {
		return parent;
	}

	public void setParent(i_menu_element menu_element) {
		parent = menu_element;
	}

	public int getLevel() {
		return level;
	}

	public String toString(){
		String space = "";
		for(int i=0;i<level;i++) space+="-";

		String result="";
		result+=space+info_menu.getId()+":"+info_menu.getDescr()+ "("+visible+"|"+potential_elements+")"+ "\n";
			for(int i=0;i<children.size();i++) result+=children.get(i).toString()+"\n";
		return result;
	}
	
	


	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean b) {
		visible = b;
		if(this.getInfo_menu().getType().equals("dynamic")){
			if(b){
				this.getInfo_menu().setType("dynamicOn");
//				if(parent!=null) parent.calculate_potential_elements();
				i_menu_element me = parent;
				while(me!=null){
					me.setVisible(b);
					me = me.getParent();
				}
			}
			return;
		}
	}


	public boolean isNext() {
		return next;
	}

	public boolean isPrev() {
		return prev;
	}


	public void setNext(boolean b) {
		next = b;
	}


	public void setPrev(boolean b) {
		prev = b;
	}


	public int getPotential_elements() {
		return potential_elements;
	}


	public void setPotential_elements(int i) {
		potential_elements = i;
	}

	public void setChildren(Vector<i_menu_element> children) {
		this.children = children;
	}

	public void setChildren_info(HashMap<String,i_menu_element> children_info) {
		this.children_info = children_info;
	}

	public void setInfo_menu(info_menu_element info_menu) {
		this.info_menu = info_menu;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
