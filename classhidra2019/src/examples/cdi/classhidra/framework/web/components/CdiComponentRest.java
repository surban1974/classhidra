package examples.cdi.classhidra.framework.web.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.web.rest.SimpleItem;
import it.classhidra.annotation.elements.Action;
import it.classhidra.annotation.elements.ActionCall;
import it.classhidra.annotation.elements.Entity;
import it.classhidra.annotation.elements.Expose;
import it.classhidra.annotation.elements.NavigatedDirective;
import it.classhidra.annotation.elements.Parameter;
import it.classhidra.annotation.elements.Redirect;
import it.classhidra.annotation.elements.Rest;
import it.classhidra.core.controller.action;
import it.classhidra.core.controller.i_action;
import it.classhidra.core.controller.redirects;
import it.classhidra.core.tool.exception.bsControllerException;
import it.classhidra.serialize.JsonWriter;
import it.classhidra.serialize.Serialized;



@Action (
	path="rest",
	entity=@Entity(
			property="always:public"
	),
	Redirect=@Redirect(path="/jsp/pages/rest.jsp"),
	Expose=@Expose(
			method="GET,POST"
			)
)

@NavigatedDirective(memoryContent="true")
public class CdiComponentRest extends action implements i_action, Serializable{
	private static final long serialVersionUID = -1L;

	@Serialized
	private List items;
	
/*
	public class Item{
		
		@Serialized
		private int id;
		@Serialized
		private String name;
		@Serialized
		private String surname;
		
		public Item(){
			super();
		}
		
		public Item(int id, String name, String surname){
			super();
			this.id=id;
			this.name=name;
			this.surname=surname;
		}
		

		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getSurname() {
			return surname;
		}
		public void setSurname(String surname) {
			this.surname = surname;
		}

		
	}
*/	
	public redirects actionservice(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, UnavailableException, bsControllerException {

		return new redirects(get_infoaction().getRedirect());
	}
	
	
	@ActionCall(
			name="getitems",
			Expose=@Expose(
					method="GET",
					restmapping={
							@Rest(path="/rest/examples/")
					}
			),
			Redirect=@Redirect(contentType="application/json")
			)
	public String prepareItems(String modelName){
		if(modelName==null || modelName.equals("")){
			if(get_infobean()!=null && get_infobean().getName()!=null && !get_infobean().getName().equals(""))
				modelName = get_infobean().getName();
			else if(get_infoaction()!=null && get_infoaction().getName()!=null && !get_infoaction().getName().equals(""))
				modelName = get_infoaction().getName();
			else
				modelName = "model";
		}
		return JsonWriter.object2json(this.get_bean().asBean(), modelName);
	}	
	
	@ActionCall(
			name="getitem",
			Expose=@Expose(
					method="GET",
					restmapping={
							@Rest(path="/rest/examples/",parametermapping="/selectedId/")
					}
			),
			Redirect=@Redirect(contentType="application/json")
			)
	public String prepareItem(@Parameter(name="selectedId") int id, String name, @Parameter(name="items") List list){
		String result="{}";
		for(int i=0;i<items.size();i++){
			SimpleItem current = (SimpleItem)items.get(i);
			if(current.getId()==id){
				if(name==null || name.equals(""))
					result = JsonWriter.object2json(current, "item");
				else
					result = JsonWriter.object2json(current, name);
				return result;
			}
		}
		return result;
	}
	
	@ActionCall(
			name="putitem",
			Expose=@Expose(
					method="PUT",
					restmapping={
							@Rest(path="/rest/examples/",parametermapping="/selectedId/")
					}
			),
			Redirect=@Redirect(contentType="application/json")
			)
	public String postItem(
			@Parameter(name="selectedId") int id,
			@Parameter(name="item") SimpleItem item,
			@Parameter(name="item.name") String item_name){
		String result="{}";
		if(item==null)
			return "{\"error\":\"Item entity is missing\"}";
		for(int i=0;i<items.size();i++){
			SimpleItem current = (SimpleItem)items.get(i);
			if(current.getId()==id){
				current.setName(item.getName());
				current.setSurname(item.getSurname());
				result = JsonWriter.object2json(current, "item");

				return result;
			}
		}
		return "{\"error\":\"Item entity ["+id+"] is missing\"}";
	}	
	
	@ActionCall(
			name="postitem",
			Expose=@Expose(
					method="POST",
					restmapping={
							@Rest(path="/rest/examples/")
					}
			),
			Redirect=@Redirect(contentType="application/json")
			)
	public String putItem(@Parameter(name="item") SimpleItem item){
		String result="{}";
		if(item==null)
			return "{\"error\":\"Item entity is missing\"}";
		
		for(int i=0;i<items.size();i++){
			SimpleItem current = (SimpleItem)items.get(i);
			if(current.getId()==item.getId()){
				return "{\"error\":\"Dupplicated item entity\"}";
			}
		}
		items.add(item);
		String modelName = "";
		if(modelName==null || modelName.equals("")){
			if(get_infobean()!=null && get_infobean().getName()!=null && !get_infobean().getName().equals(""))
				modelName = get_infobean().getName();
			else if(get_infoaction()!=null && get_infoaction().getName()!=null && !get_infoaction().getName().equals(""))
				modelName = get_infoaction().getName();
			else
				modelName = "model";
		}		
		return JsonWriter.object2json(this.get_bean().asBean(), modelName);
	}		
	
	@ActionCall(
			name="deleteitem",
			Expose=@Expose(
					method="DELETE",
					restmapping={
							@Rest(path="/rest/examples/",parametermapping="/selectedId/")
					}
			),
			Redirect=@Redirect(contentType="application/json")
			)
	public String deleteItem(
			@Parameter(name="selectedId") int id){
		String result="{}";
		for(int i=0;i<items.size();i++){
			SimpleItem current = (SimpleItem)items.get(i);
			if(current.getId()==id){
				items.remove(current);
				String modelName = "";
				if(modelName==null || modelName.equals("")){
					if(get_infobean()!=null && get_infobean().getName()!=null && !get_infobean().getName().equals(""))
						modelName = get_infobean().getName();
					else if(get_infoaction()!=null && get_infoaction().getName()!=null && !get_infoaction().getName().equals(""))
						modelName = get_infoaction().getName();
					else
						modelName = "model";
				}		
				return JsonWriter.object2json(this.get_bean().asBean(), modelName);
			}
		}
		return "{\"error\":\"Item entity ["+id+"] is missing\"}";
	}	
	

	public List getItems() {
		return items;
	}

	public void setItems(List items) {
		this.items = items;
	}


	public void reimposta() {
		super.reimposta();
		items = new ArrayList();
		items.add(new SimpleItem(10,"Mario","Rossi"));
		items.add(new SimpleItem(11,"Bob","Dylan"));
	}

	
	
}
