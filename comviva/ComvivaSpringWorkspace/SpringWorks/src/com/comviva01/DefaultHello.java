package com.comviva01;

public class DefaultHello implements IHelloService{

	private String name; 
	private String city; 
	
	public DefaultHello(){
		System.out.println("DefaultHello Default Constructor Called.. ");
	}
	
	public DefaultHello(String name, String city){
		this.name = name; 
		this.city = city; 
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String sayHello() {

		return sayHello(name, city);
	}

	@Override
	public String sayHello(String name, String city) {

		// BL 
		String myStr[] = {
				"Hey How are you Mr/Ms/Mrs %s & How is your city %s", 
				"Good Morning Mr/Ms/Mrs: %s, and heard you are in %s", 
				"There is somehing good Mr/Ms/Mrs %s in your city %s" 
		};

		int randomNumber = (int)(Math.random()*3);

		return String.format(myStr[randomNumber].toString(), name, city);
	}
	
	public void init(){
		System.out.println("In init of DefaultHello");
	}
	
	public void destroy(){
		System.out.println("In Destroy of DefaultHello");
	}
	
	
	

}
