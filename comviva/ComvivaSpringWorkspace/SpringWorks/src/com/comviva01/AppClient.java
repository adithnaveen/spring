package com.comviva01;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppClient {
	public static void main(String[] args) {
		/*
		 * XmlBeanFactory beanFactory = new XmlBeanFactory(new
		 * ClassPathResource("applicationContext.xml"));
		 */

		// ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		AbstractApplicationContext context = new 
					ClassPathXmlApplicationContext("applicationContext.xml");
		

		IHelloService helloService = (IHelloService) context.getBean("hello");
		IHelloService helloService1 = (IHelloService) context.getBean("hello");
		IHelloService helloService2 = (IHelloService) context.getBean("hello");
		
		
		String retVal = helloService.sayHello();
		System.out.println(retVal);
		
		context.registerShutdownHook();
		
	}
}
