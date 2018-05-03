package com.comviva03.postprocessing;

public class Employee {
	private int empId; 
	private String email; 
	private double salary; 
	private Name name; 
	
	public void init(){
		System.out.println("init in Employee");
	}
	public void destory(){
		System.out.println("destory in Employee");
	}
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public Name getName() {
		return name;
	}
	public void setName(Name name) {
		this.name = name;
	}
	
	
}
