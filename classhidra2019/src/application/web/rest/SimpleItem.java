package application.web.rest;

import java.io.Serializable;

import it.classhidra.serialize.Serialized;

//@Serialized
public class SimpleItem implements Serializable{

	private static final long serialVersionUID = 1L;
	@Serialized
	private int id;
	@Serialized
	private String name;
	@Serialized
	private String surname;
	@Serialized
	private float age;	
	
	public SimpleItem(){
		super();
	}
	
	public SimpleItem(int id, String name, String surname){
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

	public float getAge() {
		return age;
	}

	public void setAge(float age) {
		this.age = age;
	}
}
