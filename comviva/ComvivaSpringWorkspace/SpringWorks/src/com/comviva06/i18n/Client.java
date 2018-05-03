package com.comviva06.i18n;
import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client {
	public static void main(String[] args) {


		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		System.out.println(context.getMessage("goodmorning", new Object[]{"Naveen", "kumar"},   new Locale("FR")));

		System.out.println("----------------------------------------------------------------------");
		System.out.println(context.getMessage("goodmorning", new Object[]{"Naveen", "kumar"}, "Good Morning in English", null));
		
		System.out.println("----------------------------------------------------------------------");
		
		HelloWorld hw = (HelloWorld) context.getBean("helloWorld");
		hw.sayHelloWorld();
		hw.sayHelloWorld_fr();
		hw.sayHelloWorld_gr();
	}
}
