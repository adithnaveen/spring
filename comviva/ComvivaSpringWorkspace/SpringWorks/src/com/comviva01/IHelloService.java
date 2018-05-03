package com.comviva01;

public interface IHelloService {
	// Assuming that the values are injected 
	// by resources 
	public String sayHello();
	// or this can be hard coded in the application 
	public String sayHello(String name, String city);
}
