package com.comviva04.autowire;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientMobile {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext4.xml");
		Mobile mobile = (Mobile) context.getBean("mobile");
/*		
		System.out.println("__________________________________________________");
		System.out.println("Program to show working of auto beanAutwire ");
		System.out.println("__________________________________________________");
		System.out.println("Camera Details :"  + mobile.getCamera());
		System.out.println("Speaker Details :" + mobile.getSpeaker());
		System.out.println("Screen Details " + mobile.getScr().getBre() +", " + mobile.getScr().getLen());
*/	
		System.out.println(mobile);
		
	
	}
}