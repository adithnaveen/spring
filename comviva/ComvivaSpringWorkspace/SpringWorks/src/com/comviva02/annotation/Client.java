package com.comviva02.annotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Client {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		
		Greeting greet = (Greeting) context.getBean("english");

		System.out.println(greet.sayHello());
	
	}
}

