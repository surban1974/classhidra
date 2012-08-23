package application.web.actions;   


import it.classhidra.core.controller.action;
import it.classhidra.core.controller.bsController;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.info_action;
import it.classhidra.core.controller.info_apply_to_action;
import it.classhidra.core.controller.info_bean;
import it.classhidra.core.controller.info_item;
import it.classhidra.core.controller.info_redirect;
import it.classhidra.core.controller.info_section;
import it.classhidra.core.controller.info_stream;
import it.classhidra.core.controller.info_transformation;
import it.classhidra.core.controller.load_actions;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.core.tool.exception.bsControllerMessageException;
import it.classhidra.core.tool.exception.message;
import it.classhidra.core.tool.log.stubs.iStub;
import it.classhidra.core.tool.util.util_cloner;
import it.classhidra.core.tool.util.util_find;
import it.classhidra.core.tool.util.util_sort;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.web.forms.formBuilder;





public class actionBuilder extends action implements i_action, Serializable{
	private static final long serialVersionUID = 6641876370416839602L;

public actionBuilder(){
	super();
}

public redirects actionservice(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnavailableException, bsControllerException {
	
	formBuilder form = (formBuilder)get_bean();
	
	
	if(!form.getL_actions().isReadOk() || get_bean().getMiddleAction().equals("reload")){
		try{
			load_actions tmp = (load_actions)util_cloner.clone(bsController.getAction_config());
			if(tmp.getXmlEncoding().equals("")) tmp.setXmlEncoding("ISO-8859-1");
			form.getL_actions().reimposta();
			form.getL_actions().getV_info_actions().clear();
			form.getL_actions().getV_info_beans().clear();
			form.getL_actions().getV_info_redirects().clear();
			form.getL_actions().getV_info_streams().clear();
			form.getL_actions().getV_info_transformationoutput().clear();
			form.getL_actions().initWithData(tmp.toXml());
			form.getL_actions().setReadOk_File(true);
		}catch(Exception ex){
			new bsControllerMessageException(new message("E", "", ex.toString()), request, iStub.log_ERROR);
		}
		get_bean().setMiddleAction("");
	}


	
	if(form.getMiddleAction().equals("preview")){
		if(form.getL_actions()!=null){
			form.setXmlContent(form.getL_actions().toXml());
			
		}
		return new redirects("/jsp/builder/preview.jsp");
	}
	
	if(form.getMiddleAction().equals("load")){
		try{
			form.getL_actions().initBuilder(form.getXmlContent());
		}catch(Exception e){
			new bsControllerMessageException(new message("E", "", e.toString()), request, iStub.log_ERROR);
		}
		return new redirects(get_infoaction().getRedirect());
	}
	
	if(form.getMiddleAction().equals("syncro")){
		try{
			if(form.getL_actions()!=null){
				bsController.getAction_config().initBuilder(form.getL_actions().toXml());
				bsController.getAction_config().syncroWithBuilder();
			}
		}catch(Exception e){
			new bsControllerMessageException(new message("E", "", e.toString()), request, iStub.log_ERROR);
		}
		return new redirects(get_infoaction().getRedirect());
	}	
	
	if(form.getMiddleAction().equals("clear")){
		try{
			if(form.getL_actions()!=null){
				form.getL_actions().getV_info_actions().clear();
				form.getL_actions().getV_info_beans().clear();
				form.getL_actions().getV_info_redirects().clear();
				form.getL_actions().getV_info_streams().clear();
				form.getL_actions().getV_info_transformationoutput().clear();
				form.getL_actions().initBuilder(form.getL_actions().toXml());;
				
			}

		}catch(Exception e){
			new bsControllerMessageException(new message("E", "", e.toString()), request, iStub.log_ERROR);
		}
		return new redirects(get_infoaction().getRedirect());
	}
	
	
	if(form.getMiddleAction().indexOf("view_")>-1){
		if(form.getMiddleAction().equals("view_stream")){
			info_stream selected = (info_stream)util_find.findElementFromList(form.getL_actions().getV_info_streams(), form.getId_selected_stream(), "name");
			form.setSelected_stream(selected);
		}
		
		if(form.getMiddleAction().equals("view_apply_to_action")){
			info_stream selected = (info_stream)util_find.findElementFromList(form.getL_actions().getV_info_streams(), form.getId_selected_stream(), "name");
			form.setSelected_stream(selected);
			if(form.getSelected_stream()!=null){
				info_apply_to_action selected_a = (info_apply_to_action)util_find.findElementFromList(form.getSelected_stream().getV_info_apply_to_action(), form.getId_selected_apply_to_action(), "action");
				form.setSelected_apply_to_action(selected_a);
			}
		}	
		
		if(form.getMiddleAction().equals("view_action")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			form.setSelected_action(selected);
		}
		if(form.getMiddleAction().equals("view_bean")){
			info_bean selected = (info_bean)util_find.findElementFromList(form.getL_actions().getV_info_beans(), form.getId_selected_bean(), "name");
			form.setSelected_bean(selected);
		}
		if(form.getMiddleAction().equals("view_item")){
			info_bean selected = (info_bean)util_find.findElementFromList(form.getL_actions().getV_info_beans(), form.getId_selected_bean(), "name");
			form.setSelected_bean(selected);
			if(form.getSelected_bean()!=null){
				info_item selected_a = (info_item)util_find.findElementFromList(form.getSelected_bean().getV_info_items(), form.getId_selected_item(), "name");
				form.setSelected_item(selected_a);
			}
		}	
		
		if(form.getMiddleAction().equals("view_redirect")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			form.setSelected_action(selected);
			if(form.getSelected_action()!=null){
				info_redirect selected_a = (info_redirect)util_find.findElementFromList(form.getSelected_action().getV_info_redirects(), form.getId_selected_redirect(), "path");
				form.setSelected_redirect(selected_a);
			}
		}	
		if(form.getMiddleAction().equals("view_redirect1")){
			info_redirect selected = (info_redirect)util_find.findElementFromList(form.getL_actions().getV_info_redirects(), form.getId_selected_redirect(), "path");
			form.setSelected_redirect(selected);
			
			prepareListRedirects(form);
			
		}		
		
		if(form.getMiddleAction().equals("view_transformationoutput")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			form.setSelected_action(selected);
			if(form.getSelected_action()!=null){
				info_transformation selected_a = (info_transformation)util_find.findElementFromList(form.getSelected_action().getV_info_transformationoutput(), form.getId_selected_transformationoutput(), "name");
				form.setSelected_transformationoutput(selected_a);
			}
		}		
		if(form.getMiddleAction().equals("view_transformationoutput1")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			form.setSelected_action(selected);
			if(form.getSelected_action()!=null){
				info_redirect selected_a = (info_redirect)util_find.findElementFromList(form.getSelected_action().getV_info_redirects(), form.getId_selected_redirect(), "path");
				form.setSelected_redirect(selected_a);
				if(form.getSelected_redirect()!=null){
					info_transformation selected_b = (info_transformation)util_find.findElementFromList(form.getSelected_redirect().getV_info_transformationoutput(), form.getId_selected_transformationoutput(), "name");
					form.setSelected_transformationoutput(selected_b);
				}
			}
		}		
		if(form.getMiddleAction().equals("view_section")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			form.setSelected_action(selected);
			if(form.getSelected_action()!=null){
				info_redirect selected_a = (info_redirect)util_find.findElementFromList(form.getSelected_action().getV_info_redirects(), form.getId_selected_redirect(), "path");
				form.setSelected_redirect(selected_a);
				if(form.getSelected_redirect()!=null){
					info_section selected_b = (info_section)util_find.findElementFromList(form.getSelected_redirect().getV_info_sections(), form.getId_selected_section(), "name");
					form.setSelected_section(selected_b);
				}
			}
		}	
		return new redirects("/jsp/builder/entity.jsp");
	}
	
	if(form.getMiddleAction().indexOf("update_")>-1){
		String name=request.getParameter("name");
		String value=request.getParameter("value");
		
		if(form.getMiddleAction().equals("update_general")){
			if(form.getL_actions()!=null){
				try{
					form.getL_actions().setCampoValue(name, value);
				}catch(Exception e){
				}
			}
			form.setMiddleAction("view_general");
			return new redirects("/jsp/builder/entity.jsp");

		}
		if(form.getMiddleAction().equals("update_stream")){
			info_stream selected = (info_stream)util_find.findElementFromList(form.getL_actions().getV_info_streams(), form.getId_selected_stream(), "name");
			try{
				selected.setCampoValue(name, value);
			}catch(Exception e){
			}
			form.setSelected_stream(selected);
			form.setMiddleAction("view_stream");
			return new redirects("/jsp/builder/entity.jsp");
			
		}
		if(form.getMiddleAction().equals("update_action")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			try{
				selected.setCampoValue(name, value);
			}catch(Exception e){
			}
			form.setSelected_action(selected);
			form.setMiddleAction("view_action");
			return new redirects("/jsp/builder/entity.jsp");
			
		}

		if(form.getMiddleAction().equals("update_bean")){
			info_bean selected = (info_bean)util_find.findElementFromList(form.getL_actions().getV_info_beans(), form.getId_selected_bean(), "name");
			try{
				selected.setCampoValue(name, value);
			}catch(Exception e){
			}
			form.setSelected_bean(selected);
			form.setMiddleAction("view_bean");
			return new redirects("/jsp/builder/entity.jsp");
			
		}
		if(form.getMiddleAction().equals("update_item")){
			info_bean selected = (info_bean)util_find.findElementFromList(form.getL_actions().getV_info_beans(), form.getId_selected_bean(), "name");
			if(selected!=null){
				info_item selected_a = (info_item)util_find.findElementFromList(selected.getV_info_items(), form.getId_selected_item(), "name");

				try{
					selected_a.setCampoValue(name, value);
				}catch(Exception e){
				}
				form.setSelected_item(selected_a);
			}
			form.setSelected_bean(selected);
			form.setMiddleAction("view_item");
			return new redirects("/jsp/builder/entity.jsp");
			
		}
			
		
		if(form.getMiddleAction().equals("update_redirect1")){
			info_redirect selected = (info_redirect)util_find.findElementFromList(form.getL_actions().getV_info_redirects(), form.getId_selected_redirect(), "path");
			try{
				selected.setCampoValue(name, value);
			}catch(Exception e){
			}
			prepareListRedirects(form);
			form.setSelected_redirect(selected);
			form.setMiddleAction("view_redirect1");
			return new redirects("/jsp/builder/entity.jsp");
		
		}
		
		if(form.getMiddleAction().equals("update_apply_to_action")){
			info_stream selected = (info_stream)util_find.findElementFromList(form.getL_actions().getV_info_streams(), form.getId_selected_stream(), "name");
			if(selected!=null){
				info_apply_to_action selected_a = (info_apply_to_action)util_find.findElementFromList(selected.getV_info_apply_to_action(), form.getId_selected_apply_to_action(), "action");

				try{
					selected_a.setCampoValue(name, value);
				}catch(Exception e){
				}
				form.setSelected_apply_to_action(selected_a);
			}
			form.setSelected_stream(selected);
			form.setMiddleAction("view_apply_to_action");
			return new redirects("/jsp/builder/entity.jsp");
			
		}
		
		if(form.getMiddleAction().equals("update_redirect")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			if(selected!=null){
				info_redirect selected_a = (info_redirect)util_find.findElementFromList(selected.getV_info_redirects(), form.getId_selected_redirect(), "path");

				try{
					selected_a.setCampoValue(name, value);
				}catch(Exception e){
				}
				form.setSelected_redirect(selected_a);
			}
			form.setSelected_action(selected);
			form.setMiddleAction("view_redirect");
			return new redirects("/jsp/builder/entity.jsp");
			
		}
		if(form.getMiddleAction().equals("update_transformationoutput")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			if(selected!=null){
				info_transformation selected_a = (info_transformation)util_find.findElementFromList(selected.getV_info_transformationoutput(), form.getId_selected_transformationoutput(), "name");

				try{
					selected_a.setCampoValue(name, value);
				}catch(Exception e){
				}
				form.setSelected_transformationoutput(selected_a);
			}
			form.setSelected_action(selected);
			form.setMiddleAction("view_transformationoutput");
			return new redirects("/jsp/builder/entity.jsp");
			
		}		

		if(form.getMiddleAction().equals("update_transformationoutput1")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			if(selected!=null){
				info_redirect selected_a = (info_redirect)util_find.findElementFromList(selected.getV_info_redirects(), form.getId_selected_redirect(), "path");
				if(selected_a!=null){
					info_transformation selected_b = (info_transformation)util_find.findElementFromList(selected_a.getV_info_transformationoutput(), form.getId_selected_transformationoutput(), "name");
	
					try{
						selected_b.setCampoValue(name, value);
					}catch(Exception e){
					}
					form.setSelected_transformationoutput(selected_b);
				}
				form.setSelected_redirect(selected_a);
			}
			form.setSelected_action(selected);
			form.setMiddleAction("view_transformationoutput1");
			return new redirects("/jsp/builder/entity.jsp");
			
		}		
		if(form.getMiddleAction().equals("update_section")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			if(selected!=null){
				info_redirect selected_a = (info_redirect)util_find.findElementFromList(selected.getV_info_redirects(), form.getId_selected_redirect(), "path");
				if(selected_a!=null){
					info_section selected_b = (info_section)util_find.findElementFromList(selected_a.getV_info_sections(), form.getId_selected_section(), "name");
					try{
						selected_b.setCampoValue(name, value);
					}catch(Exception e){
					}
					form.setSelected_section(selected_b);
				}
				form.setSelected_redirect(selected_a);
			}
			form.setSelected_action(selected);
			form.setMiddleAction("view_section");
			return new redirects("/jsp/builder/entity.jsp");
			
		}			
		
		return null;
	}
	if(form.getMiddleAction().indexOf("remove_")>-1){
		if(form.getMiddleAction().equals("remove_stream")){
			info_stream selected = (info_stream)util_find.findElementFromList(form.getL_actions().getV_info_streams(), form.getId_selected_stream(), "name");
			if(form.getL_actions().getV_info_streams().remove(selected)){				
			}
			form.setSelected_stream(selected);
			return new redirects(get_infoaction().getRedirect());
		}
		if(form.getMiddleAction().equals("remove_apply_to_action")){
			info_stream selected = (info_stream)util_find.findElementFromList(form.getL_actions().getV_info_streams(), form.getId_selected_stream(), "name");
			if(selected!=null){
				info_apply_to_action selected_a = (info_apply_to_action)util_find.findElementFromList(selected.getV_info_apply_to_action(), form.getId_selected_apply_to_action(), "action");
				if(selected.getV_info_apply_to_action().remove(selected_a)){
				}			
			}
			form.setSelected_stream(selected);
			form.setMiddleAction("view_stream");
			return new redirects("/jsp/builder/entity.jsp");
		}
		if(form.getMiddleAction().equals("remove_action")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			if(form.getL_actions().getV_info_actions().remove(selected)){
			}
			form.setSelected_action(selected);
			return new redirects(get_infoaction().getRedirect());
		}
		if(form.getMiddleAction().equals("remove_bean")){
			info_bean selected = (info_bean)util_find.findElementFromList(form.getL_actions().getV_info_beans(), form.getId_selected_bean(), "name");
			if(form.getL_actions().getV_info_beans().remove(selected)){
			}
			form.setSelected_bean(selected);
			return new redirects(get_infoaction().getRedirect());
		}
		if(form.getMiddleAction().equals("remove_item")){
			info_bean selected = (info_bean)util_find.findElementFromList(form.getL_actions().getV_info_beans(), form.getId_selected_bean(), "name");
			if(selected!=null){
				info_item selected_a = (info_item)util_find.findElementFromList(selected.getV_info_items(), form.getId_selected_item(), "name");
				if(selected.getV_info_items().remove(selected_a)){
				}			
			}
			form.setSelected_bean(selected);
			form.setMiddleAction("view_bean");
			return new redirects("/jsp/builder/entity.jsp");

		}	
		
		if(form.getMiddleAction().equals("remove_redirect1")){
			info_redirect selected = (info_redirect)util_find.findElementFromList(form.getL_actions().getV_info_redirects(), form.getId_selected_redirect(), "path");
			if(selected!=null){
				if(form.getL_actions().getV_info_redirects().remove(selected)){
				}	
			}
			form.setSelected_redirect(selected);
			return new redirects(get_infoaction().getRedirect());
		}
		
		if(form.getMiddleAction().equals("remove_redirect")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			if(selected!=null){
				info_redirect selected_a = (info_redirect)util_find.findElementFromList(selected.getV_info_redirects(), form.getId_selected_redirect(), "path");
				if(selected_a!=null){
					if(selected.getV_info_redirects().remove(selected_a)){
					}
				}
			}
			form.setSelected_action(selected);
			form.setMiddleAction("view_action");
			return new redirects("/jsp/builder/entity.jsp");

		}	
		if(form.getMiddleAction().equals("remove_transformationoutput")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			if(selected!=null){
				info_transformation selected_a = (info_transformation)util_find.findElementFromList(selected.getV_info_transformationoutput(), form.getId_selected_transformationoutput(), "name");
				if(selected_a!=null){
					if(selected.getV_info_transformationoutput().remove(selected_a)){
					}
				}
			}
			form.setSelected_action(selected);
			form.setMiddleAction("view_action");
			return new redirects("/jsp/builder/entity.jsp");
			
		}	
		if(form.getMiddleAction().equals("remove_transformationoutput1")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			if(selected!=null){

				info_redirect selected_a = (info_redirect)util_find.findElementFromList(selected.getV_info_redirects(), form.getId_selected_redirect(), "path");
				if(selected_a!=null){
					info_transformation selected_b = (info_transformation)util_find.findElementFromList(selected_a.getV_info_transformationoutput(), form.getId_selected_transformationoutput(), "name");
					if(selected_b!=null){
						if(selected_a.getV_info_transformationoutput().remove(selected_b)){
						}
					}
				}
				form.setSelected_redirect(selected_a);
			}
			form.setSelected_action(selected);
			form.setMiddleAction("view_redirect");
			return new redirects("/jsp/builder/entity.jsp");
			
		}	
		
		if(form.getMiddleAction().equals("remove_section")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			if(selected!=null){
				info_redirect selected_a = (info_redirect)util_find.findElementFromList(selected.getV_info_redirects(), form.getId_selected_redirect(), "path");
				if(selected_a!=null){
					info_section selected_b = (info_section)util_find.findElementFromList(selected_a.getV_info_sections(), form.getId_selected_section(), "name");
					if(selected_b!=null){
						if(selected_a.getV_info_transformationoutput().remove(selected_b)){
						}
					}
				}			
				form.setSelected_redirect(selected_a);
			}
			form.setSelected_action(selected);
			form.setMiddleAction("view_redirect");
			return new redirects("/jsp/builder/entity.jsp");
		}	
		
		return null;
	}	
	if(form.getMiddleAction().indexOf("add_")>-1){
		if(form.getMiddleAction().equals("add_stream")){
			info_stream selected = (info_stream)util_find.findElementFromList(form.getL_actions().getV_info_streams(), "[new stream]", "name");
			if(selected==null){
				selected = new info_stream();
				selected.setName("[new stream]");
				form.getL_actions().getV_info_streams().add(selected);
			}
			form.setSelected_stream(selected);
			form.setMiddleAction("");
			form.setDisplay_streams(true);
			form.setDisplay_actions(false);
			form.setDisplay_beans(false);
			form.setDisplay_redirects(false);
			return new redirects(get_infoaction().getRedirect());
		}
		if(form.getMiddleAction().equals("add_action")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), "[new action]", "path");
			if(selected==null){
				selected = new info_action();
				selected.setPath("[new action]");
				form.getL_actions().getV_info_actions().add(selected);
			}
			form.setSelected_action(selected);
			form.setMiddleAction("");
			form.setDisplay_streams(false);
			form.setDisplay_actions(true);
			form.setDisplay_beans(false);
			form.setDisplay_redirects(false);
			return new redirects(get_infoaction().getRedirect());
		}
		if(form.getMiddleAction().equals("add_bean")){
			info_bean selected = (info_bean)util_find.findElementFromList(form.getL_actions().getV_info_beans(), "[new bean]", "name");
			if(selected==null){
				selected = new info_bean();
				selected.setName("[new bean]");
				form.getL_actions().getV_info_beans().add(selected);
			}
			form.setSelected_bean(selected);
			form.setMiddleAction("");
			form.setDisplay_streams(false);
			form.setDisplay_actions(false);
			form.setDisplay_beans(true);
			form.setDisplay_redirects(false);
			return new redirects(get_infoaction().getRedirect());
		}
		if(form.getMiddleAction().equals("add_item")){
			info_bean selected = (info_bean)util_find.findElementFromList(form.getL_actions().getV_info_beans(), form.getId_selected_bean(), "name");
			info_item selected_a = (info_item)util_find.findElementFromList(selected.getV_info_items(), "[new item]", "name");
			if(selected_a==null){
				selected_a = new info_item();
				selected_a.setName("[new item]");
				selected.getV_info_items().add(selected_a);
			}
			form.setSelected_item(selected_a);
			form.setMiddleAction("view_bean");
		}	
		
		if(form.getMiddleAction().equals("add_redirect1")){
			info_redirect selected = (info_redirect)util_find.findElementFromList(form.getL_actions().getV_info_redirects(), "[new redirect]", "path");
			if(selected==null){
				selected = new info_redirect();
				selected.setPath("[new redirect]");
				form.getL_actions().getV_info_redirects().add(selected);
			}
			form.setSelected_redirect(selected);
			form.setMiddleAction("");
			form.setDisplay_streams(false);
			form.setDisplay_actions(false);
			form.setDisplay_beans(false);
			form.setDisplay_redirects(true);
			return new redirects(get_infoaction().getRedirect());
		}
		
		if(form.getMiddleAction().equals("add_apply_to_action")){
			info_stream selected = (info_stream)util_find.findElementFromList(form.getL_actions().getV_info_streams(), form.getId_selected_stream(), "name");
			info_apply_to_action selected_a = (info_apply_to_action)util_find.findElementFromList(selected.getV_info_apply_to_action(),"[new apply-to-action]", "action");
			if(selected_a==null){
				selected_a = new info_apply_to_action();
				selected_a.setAction("[new apply-to-action]");
				selected.getV_info_apply_to_action().add(selected_a);
			}
			form.setSelected_apply_to_action(selected_a);
			form.setMiddleAction("view_stream");
		}		
		if(form.getMiddleAction().equals("add_redirect")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			info_redirect selected_a = (info_redirect)util_find.findElementFromList(selected.getV_info_redirects(), "[new redirect]", "path");
			if(selected_a==null){
				selected_a = new info_redirect();
				selected_a.setPath("[new redirect]");
				selected.getV_info_redirects().add(selected_a);
			}
			form.setSelected_redirect(selected_a);
			form.setMiddleAction("view_action");
		}	
		if(form.getMiddleAction().equals("add_transformationoutput")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			info_transformation selected_a = (info_transformation)util_find.findElementFromList(selected.getV_info_transformationoutput(), "[new transformation]", "name");
			if(selected_a==null){
				selected_a = new info_transformation();
				selected_a.setName("[new transformation]");
				selected.getV_info_transformationoutput().add(selected_a);
			}
			form.setSelected_transformationoutput(selected_a);
			form.setMiddleAction("view_action");
		}
		if(form.getMiddleAction().equals("add_transformationoutput1")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			info_redirect selected_a = (info_redirect)util_find.findElementFromList(selected.getV_info_redirects(), form.getId_selected_redirect(), "path");
			info_transformation selected_b = (info_transformation)util_find.findElementFromList(selected_a.getV_info_transformationoutput(), "[new transformation]", "name");
			if(selected_b==null){
				selected_b = new info_transformation();
				selected_b.setName("[new transformation]");
				selected_a.getV_info_transformationoutput().add(selected_b);
			}
			form.setSelected_transformationoutput(selected_b);
			form.setMiddleAction("view_redirect");
		}		
		if(form.getMiddleAction().equals("add_section")){
			info_action selected = (info_action)util_find.findElementFromList(form.getL_actions().getV_info_actions(), form.getId_selected_action(), "path");
			info_redirect selected_a = (info_redirect)util_find.findElementFromList(selected.getV_info_redirects(), form.getId_selected_redirect(), "path");
			info_section selected_b = (info_section)util_find.findElementFromList(selected_a.getV_info_sections(), "[new section]", "name");
			if(selected_b==null){
				selected_b = new info_section();
				selected_b.setName("[new section]");
				selected_a.getV_info_sections().add(selected_b);
			}
			form.setSelected_section(selected_b);
			form.setMiddleAction("view_redirect");
		}					
		
		return new redirects("/jsp/builder/entity.jsp");
	}		
	return new redirects(get_infoaction().getRedirect());
}
private void prepareListRedirects(formBuilder form){
	if(form.getType_selected().equals("open")){
		Vector tmp = new Vector();
		
		info_redirect selected_a = new info_redirect();
		selected_a.setPath("[new redirect]");
		tmp.add(selected_a);
		
		
		for(int i=0;i<form.getL_actions().getV_info_actions().size();i++){
			info_action current = (info_action)form.getL_actions().getV_info_actions().get(i);
			tmp.addAll(current.getV_info_redirects());
		}

		HashMap h_tmp=new HashMap();
		for(int i=0;i<tmp.size();i++){
			info_redirect current = (info_redirect)tmp.get(i);
			if(h_tmp.get(current.getPath())==null){
				h_tmp.put(current.getPath(), current);
			}
		}
		for(int i=0;i<form.getL_actions().getV_info_redirects().size();i++){
			info_redirect iRedirect = (info_redirect)form.getL_actions().getV_info_redirects().get(i);
			if(!iRedirect.getPath().equals("[new redirect]"))
				h_tmp.remove(iRedirect.getPath());
		}
		Vector all_redirects = new Vector(h_tmp.values());
		all_redirects = new util_sort().sort(all_redirects,"path");
		form.setElements_all_redirects(all_redirects);
	}

}
}

