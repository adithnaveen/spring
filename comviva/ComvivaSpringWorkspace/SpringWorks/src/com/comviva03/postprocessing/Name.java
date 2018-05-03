package com.comviva03.postprocessing;

public class Name {
	private String firstName; 
	private String lastName; 
	
	public void init(){
		System.out.println("init in Name");
	}
	public void destory(){
		System.out.println("destory in Name");
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	
}
